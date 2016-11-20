package letsapps.com.letscube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.AverageCalculator;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimerStats;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.CubeEvents;

public class DatabaseStatsHandler extends DatabaseHandler {

    public static final int NO_STATS = 0;

    public DatabaseStatsHandler(Context context) {
        super(context);
    }

    public void instantiateStats(){

        open();

        String query = "SELECT * FROM " + Database.TABLE_STATS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null) {
            if(cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    final TimerStats stats = new TimerStats(
                            cursor.getInt(cursor.getColumnIndex(Database.STATS_EVENT)));
                    stats.setNbTimes(
                            cursor.getInt(cursor.getColumnIndex(Database.STATS_NB_TIMES)));
                    stats.setTotalAverage(
                            cursor.getInt(cursor.getColumnIndex(Database.STATS_AVERAGE)));
                    stats.setStandardDeviation(
                            cursor.getInt(cursor.getColumnIndex(Database.STATS_STANDARD_DEVIATION)));

                    AppInstance.getInstance().getTimes(
                            CubeEvents.getEvent(context, stats.getEventId())).setStats(stats);
                } while (cursor.moveToNext());
            }else{
                Log.e("DStH (getStats)", "No stats in database");
            }
        }else{
            Log.e("DStH (getStats)", "No stats in database");
        }
        close();

    }

    public void updateStats(TimerStats newStats){

        open();

        db.execSQL("DELETE FROM " + Database.TABLE_STATS +
                " WHERE " + Database.STATS_EVENT + " = " + newStats.getEventId());

        db.execSQL("INSERT INTO " + Database.TABLE_STATS + " (" +
                Database.STATS_EVENT + ", " +
                Database.STATS_NB_TIMES + ", " +
                Database.STATS_AVERAGE + ", " +
                Database.STATS_STANDARD_DEVIATION + ") VALUES (" +
                newStats.getEventId() + ", " +
                newStats.getNbTimes() + ", " +
                newStats.getTotalAverage() + ", " +
                newStats.getStandardDeviation() + ")");

        AppInstance.getInstance().getTimes(
                CubeEvents.getEvent(context, newStats.getEventId())).setStats(newStats);

        close();
    }

    public TimerStats getStats(int eventId, String columnName){

        final TimerStats stats = new TimerStats(eventId);

        open();

        String query = "SELECT * FROM " + Database.TABLE_STATS +
                " WHERE " + Database.STATS_EVENT + " = " + eventId;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null) {
            if(cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    stats.setNbTimes(cursor.getInt(cursor.getColumnIndex(Database.STATS_NB_TIMES)));
                    stats.setTotalAverage(
                            cursor.getInt(cursor.getColumnIndex(Database.STATS_AVERAGE)));
                    stats.setStandardDeviation(
                            cursor.getInt(cursor.getColumnIndex(Database.STATS_STANDARD_DEVIATION)));
                } while (cursor.moveToNext());
            }else{
                close();
                recomputeStats(eventId);
                Log.e("DStH (getStats)", "No stats for " + eventId + " in database");
                return null;
            }
        }else{
            close();
            recomputeStats(eventId);
            Log.e("DStH (getStats)", "No stats for " + eventId + " in database");
            return null;
        }

        close();

        return stats;
    }

    //TODO : /!\ C'EST LA MERDE AVEC LE RECALCUL DE L'ECART-TYPE /!\
    public void timeAdded(DatabaseTime time){
        TimerStats previousStats =
                AppInstance.getInstance().getTimes(time.getEvent()).getStats();
        TimerStats newStats = new TimerStats(previousStats.getEventId());
        newStats.setNbTimes(previousStats.getNbTimes() + 1);
        if(newStats.getNbTimes() > 0) {
            newStats.setTotalAverage((((float) previousStats.getNbTimes() / newStats.getNbTimes()) *
                    previousStats.getTotalAverage()) + (time.getTime() / newStats.getNbTimes()));
        }

        newStats.setStandardDeviation(TimerUtils.TIME_NO_VALUE);
        updateStats(newStats);
    }

    public void timeRemoved(DatabaseTime time){
        TimerStats previousStats =
                AppInstance.getInstance().getTimes(time.getEvent()).getStats();
        TimerStats newStats = new TimerStats(previousStats.getEventId());
        newStats.setNbTimes(previousStats.getNbTimes() - 1);
        if(newStats.getNbTimes() > 0) {
            newStats.setTotalAverage((((float)previousStats.getNbTimes() / newStats.getNbTimes()) *
                    previousStats.getTotalAverage() - time.getTime() / newStats.getNbTimes()));
        }else{
            newStats.setTotalAverage(TimerUtils.TIME_NO_VALUE);
        }
        newStats.setStandardDeviation(TimerUtils.TIME_NO_VALUE);
        updateStats(newStats);

    }

    public void resetStats(int eventId){
        final TimerStats stats = new TimerStats(eventId);
        stats.setNbTimes(0);
        stats.setTotalAverage(TimerUtils.TIME_NO_VALUE);
        stats.setStandardDeviation(TimerUtils.TIME_NO_VALUE);
        updateStats(stats);
    }

    public TimerStats recomputeStats(int eventId){
        final TimerStats stats = new TimerStats(eventId);

        final DatabaseTimeHandler timesDatabase = new DatabaseTimeHandler(context);
        final int[] times = timesDatabase.getAllTimes(eventId);

        stats.setNbTimes(times.length);
        stats.setTotalAverage(AverageCalculator.calculateMean(times));

        double variance = 0;
        if(times.length != 0) {
            for (int i = 0; i < stats.getNbTimes(); i++) {
                variance += Math.pow(times[i] - stats.getTotalAverage(), 2);
            }
            variance *= (1 / stats.getNbTimes());
            stats.setStandardDeviation(Math.sqrt(variance));

        }else{
            stats.setStandardDeviation(TimerUtils.TIME_NO_VALUE);
        }

        open();

        db.execSQL("DELETE FROM " + Database.TABLE_STATS +
                " WHERE " + Database.STATS_EVENT + " = " + eventId);

        db.execSQL("INSERT INTO " + Database.TABLE_STATS + " (" +
                Database.STATS_EVENT + ", " +
                Database.STATS_NB_TIMES + ", " +
                Database.STATS_AVERAGE + ", " +
                Database.STATS_STANDARD_DEVIATION + ") VALUES (" +
                eventId + ", " +
                stats.getNbTimes() + ", " +
                stats.getTotalAverage() + ", " +
                stats.getStandardDeviation() + ")");

        close();

        AppInstance.getInstance().getTimes(
                CubeEvents.getEvent(context, stats.getEventId())).setStats(stats);

        return stats;
    }

    public void printDB(){
        open();

        final String query = "SELECT * FROM " + Database.TABLE_STATS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null) {
            if(cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    Log.d("DStH", "EventId : " + cursor.getInt(cursor.getColumnIndex(Database.STATS_EVENT)) +
                            " | nbTimes : " + cursor.getInt(cursor.getColumnIndex(Database.STATS_NB_TIMES)) +
                            " | totalAvg : " + cursor.getInt(cursor.getColumnIndex(Database.STATS_AVERAGE)) +
                            " | standardDev. : " + cursor.getInt(cursor.getColumnIndex(Database.STATS_STANDARD_DEVIATION)));
                } while (cursor.moveToNext());
            }else{
                Log.e("DStH", "No stats in database");
            }
        }else{
            Log.e("DStH", "No stats in database");
        }
        Log.d("DStH", "------------------------------------------------------");

        close();
    }

}

package letsapps.com.letscube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import letsapps.com.letscube.listener.RecordListener;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.AverageCalculator;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.cube.CubeEvents;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.RecordList;

public class DatabaseTimeHandler extends DatabaseHandler {

    private RecordListener recordListener;
    DatabaseSessionHandler sessionDatabase;
    DatabaseRecordsHandler recordsDatabase;
    DatabaseEventHandler eventsDatabase;

    public DatabaseTimeHandler(Context context){
        super(context);
        sessionDatabase = new DatabaseSessionHandler(context);
        recordsDatabase = new DatabaseRecordsHandler(context);
        eventsDatabase = new DatabaseEventHandler(context);
    }

    //return the time with db ID.
    public final DatabaseTime addTime(DatabaseTime time, TimeList times){
        times.add(time);
        time.setMo3(AverageCalculator.calculateLastMean(times, 3));
        time.setAvg5(AverageCalculator.calculateLastAverage(times, 5));
        time.setAvg12(AverageCalculator.calculateLastAverage(times, 12));
        time.setMo50(AverageCalculator.calculateLastMean(times, 50));
        time.setMo100(AverageCalculator.calculateLastMean(times, 100));

        if(time.getEvent() == null){
            Log.d("DTH", "time.getEvent() == null");
        }else{
            Log.d("DTH", "time.getEvent() == " + time.getEvent().getName(context));
        }

        open();

        ContentValues values = new ContentValues();
        values.put(Database.TIMES_SINGLE, time.getTime());
        values.put(Database.TIMES_SINGLE2, time.getTime2());
        values.put(Database.TIMES_MO3, time.getMo3());
        values.put(Database.TIMES_AVG5, time.getAvg5());
        values.put(Database.TIMES_AVG12, time.getAvg12());
        values.put(Database.TIMES_MO50, time.getMo50());
        values.put(Database.TIMES_MO100, time.getMo100());
        values.put(Database.TIMES_SCRAMBLE, time.getScramble());
        values.put(Database.TIMES_EVENT, time.getEvent().getId());
        if(time.getSession() != null) {
            values.put(Database.TIMES_SESSION, time.getSession().getId());
        }
        values.put(Database.TIMES_PENALTY, time.getPenalty());
        values.put(Database.TIMES_DATE, time.getDate());
        time.setId((int)db.insert(Database.TABLE_TIMES, null, values));

        Log.d("DTH (addTime)", "time added : " + time.toString());

        close();

        final int timesSize = times.size();
        Log.d("DTH (addTime)", "timesSize : " + timesSize);

        // To fill recordDialog :
        final ArrayList<Integer> recordsType = new ArrayList<>();
        final ArrayList<Integer> previousRecordsTime = new ArrayList<>();

        if(times.getRecord() == null){
            times.setRecord(new RecordList());
            Log.e("DTH", "times.getRecord() == null");
        }

        Log.d("DTH", "singleRecord : " + times.getRecord().getSingle());

        if(time.getPenalty() != DatabaseTime.PENALTY_DNF) {
            if (times.getRecord().getSingle() == RecordList.RECORD_NO_VALUE) {
                Log.d("DTH", "first single : " + time.getTime());
                previousRecordsTime.add(TimerUtils.TIME_NO_VALUE);
                times.getRecord().setSingle(time.getTime());
                recordsDatabase.updateRecord(
                        time.getEvent().getId(), CubeRecord.TYPE_SINGLE, time.getTime());
                recordsType.add(CubeRecord.TYPE_SINGLE);
            } else {
                if (time.getTime() < times.getRecord().getSingle()) {
                    Log.d("DTH", "new singleRecord : " + time.getTime());

                    previousRecordsTime.add(times.getRecord().getSingle());
                    times.getRecord().setSingle(time.getTime());
                    recordsDatabase.updateRecord(
                            time.getEvent().getId(), CubeRecord.TYPE_SINGLE, time.getTime());
                    recordsType.add(CubeRecord.TYPE_SINGLE);
                }
            }
        }
        final int mo3 = AverageCalculator.calculateLastMean(times, 3);
        if(mo3 > TimerUtils.TIME_NO_VALUE) {
            if (times.getRecord().getMo3() == RecordList.RECORD_NO_VALUE && timesSize >= 3) {
                previousRecordsTime.add(TimerUtils.TIME_NO_VALUE);
                times.getRecord().setMo3(mo3);
                recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_MO3, mo3);
                recordsType.add(CubeRecord.TYPE_MO3);
            } else {
                if (AverageCalculator.calculateLastMean(times, 3) < times.getRecord().getMo3()) {
                    previousRecordsTime.add(times.getRecord().getMo3());
                    times.getRecord().setMo3(mo3);
                    recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_MO3, mo3);
                    recordsType.add(CubeRecord.TYPE_MO3);
                }
            }
        }

        final int avg5 = AverageCalculator.calculateLastAverage(times, 5);
        if(avg5 > TimerUtils.TIME_NO_VALUE) {
            if (times.getRecord().getAvg5() == RecordList.RECORD_NO_VALUE && timesSize >= 5) {
                previousRecordsTime.add(TimerUtils.TIME_NO_VALUE);
                times.getRecord().setAvg5(avg5);
                recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_AVG5, avg5);
                recordsType.add(CubeRecord.TYPE_AVG5);
            } else {
                if (avg5 < times.getRecord().getAvg5()){
                    previousRecordsTime.add(times.getRecord().getAvg5());
                    times.getRecord().setAvg5(avg5);
                    recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_AVG5, avg5);
                    recordsType.add(CubeRecord.TYPE_AVG5);
                }
            }
        }

        final int avg12 = AverageCalculator.calculateLastAverage(times, 12);
        if(AverageCalculator.calculateLastAverage(times, 12) > TimerUtils.TIME_NO_VALUE) {
            if (times.getRecord().getAvg12() == RecordList.RECORD_NO_VALUE && timesSize >= 12) {
                previousRecordsTime.add(TimerUtils.TIME_NO_VALUE);
                times.getRecord().setAvg12(avg12);
                recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_AVG12, avg12);
                recordsType.add(CubeRecord.TYPE_AVG12);
            } else {
                if (avg12 < times.getRecord().getAvg12()) {
                    previousRecordsTime.add(times.getRecord().getAvg12());
                    times.getRecord().setAvg12(avg12);
                    recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_AVG12, avg12);
                    recordsType.add(CubeRecord.TYPE_AVG12);
                }
            }
        }

        final int mo50 = AverageCalculator.calculateLastMean(times, 50);
        if(mo50 > TimerUtils.TIME_NO_VALUE) {
            if (times.getRecord().getMo50() == RecordList.RECORD_NO_VALUE && timesSize >= 50) {
                previousRecordsTime.add(TimerUtils.TIME_NO_VALUE);
                times.getRecord().setMo50(mo50);
                recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_MO50, mo50);
                recordsType.add(CubeRecord.TYPE_MO50);
            } else {
                if (mo50 < times.getRecord().getMo50()) {
                    previousRecordsTime.add(times.getRecord().getMo50());
                    times.getRecord().setMo50(mo50);
                    recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_MO50, mo50);
                    recordsType.add(CubeRecord.TYPE_MO50);
                }
            }
        }

        final int mo100 = AverageCalculator.calculateLastMean(times, 100);
        if(AverageCalculator.calculateLastMean(times, 100) > TimerUtils.TIME_NO_VALUE) {
            if (times.getRecord().getMo100() == RecordList.RECORD_NO_VALUE && timesSize >= 100) {
                previousRecordsTime.add(TimerUtils.TIME_NO_VALUE);
                times.getRecord().setMo100(mo100);
                recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_MO100, mo100);
                recordsType.add(CubeRecord.TYPE_MO100);
            } else {
                if (mo100 < times.getRecord().getMo100()){
                    previousRecordsTime.add(times.getRecord().getMo100());
                    times.getRecord().setMo100(mo100);
                    recordsDatabase.updateRecord(time.getEvent().getId(), CubeRecord.TYPE_MO100, mo100);
                    recordsType.add(CubeRecord.TYPE_MO100);
                }
            }
        }

        if(recordListener != null && recordsType.size() != 0){
            recordListener.newRecords(time, recordsType, previousRecordsTime);
        }

        return time;
    }

    public final void instantiateTimeList(){

        Log.d("DTHinst", "instantiateTimeList() start!");

        final String query = "SELECT * FROM " + Database.TABLE_TIMES +
                " WHERE " + Database.TIMES_EVENT + " = ?" +
                " ORDER BY " + Database.TIMES_ID + " DESC LIMIT 200";

        Log.d("DTHinst", "query : " + query);

        final Event[] events = CubeEvents.getEvents(context);
        Log.d("DTHinst", "there is " + events.length + " events to get");

        for(int i = 0; i < events.length; i++){

            // Pour limiter le chargement à 200 temps max. par event.

            open();

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(events[i].getId())});

            if(cursor.getCount() != 0) {
                Log.d("DTHinst", "there is actually " + cursor.getCount() +
                        " times in DB for event : " + events[i].getName(context));

                for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
                    final DatabaseTime time = getCursorTime(cursor);
                    if(time.getSessionId() == 0){
                        time.setSession(sessionDatabase.getLastSession(events[i].getId()));
                    }else{
                        time.setSession(sessionDatabase.getSession(time.getSessionId()));
                    }
                    AppInstance.getInstance().getTimes().add(time);
                }

            }else{
                Log.d("DTHinst", "there is actually no time for event : " +
                        events[i].getName(context));
            }

            close();


            AppInstance.getInstance().getTimes(events[i]).setRecord(
                    recordsDatabase.getRecord(events[i].getId()));

        }
    }

    public int[] getAllTimes(int eventId){

        open();

        String query = "SELECT " + Database.TIMES_SINGLE + " FROM " + Database.TABLE_TIMES +
                " WHERE " + Database.TIMES_EVENT + " = " + eventId;

        Cursor cursor = db.rawQuery(query, null);

        final int[] times = new int[cursor.getCount()];

        int i = 0;

        if(cursor.getCount() != 0) {
            Log.d("DTH (getTimes)", "there is actually " + cursor.getCount() +
                    " times in DB");

            if(cursor.moveToFirst()) {
                do {
                    times[i] = cursor.getInt(cursor.getColumnIndex(Database.TIMES_SINGLE));
                    i++;
                } while (cursor.moveToNext());
            }
        }else{
            Log.d("DTH (getTimes)", "there is actually no time for event : " +
                    eventId);
        }

        close();

        return times;
    }

    public DatabaseTime[] getTimes(int eventId, int nbTimes){

        final DatabaseTime lastTimeOfBestAverage = getBestTime(getColumnName(nbTimes), eventId);

        if(lastTimeOfBestAverage == null){
            Log.e("DTH (getTimes)", "there is no (best) time in BDD.");
            return null;
        }

        open();

        String query = "SELECT " + Database.TIMES_SINGLE + ", " + Database.TIMES_PENALTY + ", " +
                Database.TIMES_SCRAMBLE + ", " +Database.TIMES_DATE +
                " FROM " + Database.TABLE_TIMES +
                " WHERE " + Database.TIMES_EVENT + " = " + eventId +
                " AND " + Database.TIMES_ID + " <= " + lastTimeOfBestAverage.getId() +
                " ORDER BY " + Database.TIMES_ID + " DESC LIMIT " + nbTimes;

        Cursor cursor = db.rawQuery(query, null);

        DatabaseTime[] timesOfBestAverage = new DatabaseTime[nbTimes];

        Log.d("DTH (getBestAverage)", "there is " + cursor.getCount() + " times according to the query : " + query + "{" + nbTimes + "}");

        if (cursor.getCount() == nbTimes) {
            Log.d("DTH (getBestAverage)", "getting best avg");

            int i = 0;

            if(cursor.moveToFirst()) {
                do {
                    timesOfBestAverage[i] = new DatabaseTime();
                    timesOfBestAverage[i].setTime(
                            cursor.getInt(cursor.getColumnIndex(Database.TIMES_SINGLE)));
                    timesOfBestAverage[i].setPenalty(
                            cursor.getInt(cursor.getColumnIndex(Database.TIMES_PENALTY)));
                    timesOfBestAverage[i].setScramble(
                            cursor.getString(cursor.getColumnIndex(Database.TIMES_SCRAMBLE)));
                    timesOfBestAverage[i].setDate(
                            cursor.getString(cursor.getColumnIndex(Database.TIMES_DATE)));
                    i++;
                } while (cursor.moveToNext());
            }
            close();
            return timesOfBestAverage;

        } else {
            Log.e("DTH (getTimes)", "there is only " + cursor.getCount() + " times before this one, and querying " + nbTimes + " times.");
            Log.e("DTH (getTimes)", "lastTimeOfBestAverage.getId() = " + lastTimeOfBestAverage.getId());
            close();
            return null;
        }

    }

    public final DatabaseTime getTime(int timeId){

        open();

        String query = "SELECT * FROM " + Database.TABLE_TIMES + " WHERE " + Database.TIMES_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(timeId)});

        if(cursor.getCount() != 0) {

            cursor.moveToFirst();
            final DatabaseTime time = getCursorTime(cursor);

            close();
            return time;

        }else{
            Log.e("DTH (getTime)", "the time with ID " + timeId + " don't exist.");
            close();
            return null;
        }


    }

    // rafraîchit toutes les données du time concerné.
    public void updateTimePenalty(TimeList times, DatabaseTime timeToUpdate){

        // update values of avg concerned by an evetually time modification

        final int updatedTimePosInList = times.indexOf(timeToUpdate);
        times.set(updatedTimePosInList, timeToUpdate);

        open();

        final ContentValues updatedTimesValues = new ContentValues();
        updatedTimesValues.put(Database.TIMES_PENALTY, timeToUpdate.getPenalty());

        db.update(Database.TABLE_TIMES, updatedTimesValues,
                Database.TIMES_ID + "=" + timeToUpdate.getId(), null);

        updateMeanConcernedByTimePenaltyUpdating(times, timeToUpdate, updatedTimePosInList, 3);
        updateAverageConcernedByTimePenaltyUpdating(times, timeToUpdate, updatedTimePosInList, 5);
        updateAverageConcernedByTimePenaltyUpdating(
                times, timeToUpdate, updatedTimePosInList, 12);
        updateMeanConcernedByTimePenaltyUpdating(times, timeToUpdate, updatedTimePosInList, 50);
        updateMeanConcernedByTimePenaltyUpdating(times, timeToUpdate, updatedTimePosInList, 100);

        close();
    }

    private void updateMeanConcernedByTimePenaltyUpdating(TimeList times,
                                                   DatabaseTime updatedTime,
                                                   int updatedTimePosInList,
                                                   int meanLength){

        //Log.d("DTH (updateMeanPenalty)", "updatedTimePosInList = " + updatedTimePosInList);

        String query = "SELECT * FROM " + Database.TABLE_TIMES +
                " WHERE " + Database.TIMES_EVENT +  " = " + updatedTime.getEvent().getId() +
                " AND " + Database.TIMES_ID + " >= " + updatedTime.getId() +
                " LIMIT " + meanLength;
        Cursor cursor = db.rawQuery(query, null);

        //Log.d("DTH (updateMeanPenalty)", cursor.getCount() +
        //        " rows needs to change their Mo" + meanLength);

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                DatabaseTime iteratedTime = getCursorTime(cursor);

                final int posInList = updatedTimePosInList + cursor.getPosition();
                //Log.d("DTH (updateMeanPenalty)", "posInList = " + posInList);
                //Log.d("DTH (updateMeanPenalty)", "iteratedTime = " + iteratedTime.getTime() +
                //        " | posInList = " + posInList);

                if(posInList >= meanLength - 1) {

                    DatabaseTime[] timesOfMean = new DatabaseTime[meanLength];
                    for(int i = 0; i < meanLength; i++){
                        timesOfMean[i] = times.get(posInList - i);
                    }

                    final int meanValue = AverageCalculator.calculateMean(timesOfMean);

                    iteratedTime.setMean(meanLength, meanValue);
                    times.set(posInList, iteratedTime);

                    final ContentValues values = new ContentValues();
                    switch (meanLength){
                        case 3 : values.put(Database.TIMES_MO3, meanValue); break;
                        case 50 : values.put(Database.TIMES_MO50, meanValue); break;
                        case 100 : values.put(Database.TIMES_MO100, meanValue); break;
                        default : Log.e("DTH (updateMeanPenalty)", "unavailable meanLength" +
                                " : " + meanLength);
                    }
                    db.update(Database.TABLE_TIMES, values,
                            Database.TIMES_ID + " = " + iteratedTime.getId(), null);
                }

            } while (cursor.moveToNext());

        } else {
            Log.d("DTH (removeTime)", "0 times returned by the cursor");
        }
    }

    private void updateAverageConcernedByTimePenaltyUpdating(TimeList times,
                                                             DatabaseTime updatedTime,
                                                             int updatedTimePosInList,
                                                             int avgLength){

        //Log.d("DTH (updateAvgPenalty)", "updatedTimePosInList = " + updatedTimePosInList);

        String query = "SELECT * FROM " + Database.TABLE_TIMES +
                " WHERE " + Database.TIMES_EVENT +  " = " + updatedTime.getEvent().getId() +
                " AND " + Database.TIMES_ID + " >= " + updatedTime.getId() +
                " LIMIT " + avgLength;
        Cursor cursor = db.rawQuery(query, null);

        //Log.d("DTH (updateAvgPenalty)", cursor.getCount() +
        //        " rows needs to change their Avg" + avgLength);

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                DatabaseTime iteratedTime = getCursorTime(cursor);

                final int posInList = updatedTimePosInList + cursor.getPosition();
                //Log.d("DTH (updateMeanPenalty)", "posInList = " + posInList);
                //Log.d("DTH (updateAvgPenalty)", "iteratedTime = " + iteratedTime.getTime() +
                //        " | posInList = " + posInList);

                if(posInList >= avgLength - 1) {

                    DatabaseTime[] timesOfAverage = new DatabaseTime[avgLength];
                    for(int i = 0; i < avgLength; i++){
                        timesOfAverage[i] = times.get(posInList - i);
                    }

                    final int averageValue =
                            AverageCalculator.calculateAverage(timesOfAverage);

                    iteratedTime.setAverage(avgLength, averageValue);
                    times.set(posInList, iteratedTime);

                    final ContentValues values = new ContentValues();
                    switch (avgLength){
                        case 5 : values.put(Database.TIMES_AVG5, averageValue); break;
                        case 12 : values.put(Database.TIMES_AVG12, averageValue); break;
                        default : Log.e("DTH (updateAvgPenalty)",
                                "unavailable avgLength : " + avgLength);
                    }
                    db.update(Database.TABLE_TIMES, values,
                            Database.TIMES_ID + " = " + iteratedTime.getId(), null);
                }

            } while (cursor.moveToNext());

        } else {
            Log.d("DTH (removeTime)", "0 times returned by the cursor");
        }
    }

    public int removeTime(DatabaseTime timeToDelete, TimeList times){
        open();

        int timePos = 0;
        for(int i = 0; i < times.size(); i++){
            if(timeToDelete.getId() == times.get(i).getId()){
                timePos = i;
                break;
            }
        }
        times.remove(timePos);

        // Modifie les avgs et means des temps d'après. ex: modifie le mo3 des
        // 3 temps d'après

        updateMeanConcernedByTimeRemoving(times, timeToDelete, timePos, 3);
        updateAverageConcernedByTimeRemoving(times, timeToDelete, timePos, 5);
        updateAverageConcernedByTimeRemoving(times, timeToDelete, timePos, 12);
        updateMeanConcernedByTimeRemoving(times, timeToDelete, timePos, 50);
        updateMeanConcernedByTimeRemoving(times, timeToDelete, timePos, 100);

        if(!db.isOpen()){
            open();
        }

        final int nbRowsDeleted = db.delete(
                Database.TABLE_TIMES, Database.TIMES_ID + "=" + timeToDelete.getId(), null);
        Log.d("DTH (removeTime)", "time deleted : " + timeToDelete.getTime());

        close();

        return nbRowsDeleted;
    }

    public int deleteTimes(ArrayList<DatabaseTime> timesToDelete, TimeList times){
        int nbTimesDeleted = 0;
        for(DatabaseTime time : timesToDelete){
            nbTimesDeleted += removeTime(time, times);
        }
        return nbTimesDeleted;
    }

    public int deleteEventTimes(Event event){
        open();

        final int nbDeletedTimes =
                db.delete(Database.TABLE_TIMES, Database.TIMES_EVENT + "=" + event.getId(), null);

        Log.i("DTH (removeTimes)", "deleted all time(" + nbDeletedTimes +  " times) of " +
                        event.getName() + " event");

        close();

        return nbDeletedTimes;
    }

    private void updateMeanConcernedByTimeRemoving(TimeList times,
                                                   DatabaseTime deletedTime,
                                                   int deletedTimeListPos,
                                                   int meanLength){

        if(!db.isOpen()){
            open();
        }

        //récupération des x temps (ou moins selon la position du temps supprimé)
        // auxquels il faut modifier l'avgX.
        String query = "SELECT * FROM " + Database.TABLE_TIMES +
                " WHERE " + Database.TIMES_EVENT +  " = " + deletedTime.getEvent().getId() +
                " AND " + Database.TIMES_ID + " > " + deletedTime.getId() +
                " LIMIT " + (meanLength - 1);
        Cursor cursor = db.rawQuery(query, null);

        Log.d("DTH (removeTime)", cursor.getCount() + " rows needs to change their mean " + meanLength);

        if(deletedTimeListPos != times.size()) { // si l'on supprime le dernier temps, il n'influe sur aucun avg.
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    final DatabaseTime iteratedTime = getCursorTime(cursor);
                    final int iteratedTimePos = deletedTimeListPos + cursor.getPosition();
                    final int timeToAddToMeanPos = iteratedTimePos - (meanLength - 1);
                    int meanValue = 0;
                    if (deletedTimeListPos + cursor.getPosition() >= (meanLength - 1)) {
                        // on retire le temps supprimé et on ajoute le temps d'avant.
                        meanValue = times.get(deletedTimeListPos + cursor.getPosition())
                                .getMean(meanLength);
                        meanValue -= (deletedTime.getTime() / meanLength);
                        switch(times.get(timeToAddToMeanPos).getPenalty()){
                            case DatabaseTime.PENALTY_2 :
                                meanValue += (times.get(timeToAddToMeanPos).getTime()
                                        / meanLength) + TimerUtils.TIME_2_PENALITY_VALUE;
                                break;
                            case DatabaseTime.PENALTY_DNF : meanValue = TimerUtils.TIME_DNF; break;
                            default : meanValue += (times.get(timeToAddToMeanPos).getTime()
                                    / meanLength);
                        }
                        //meanValue += (times.get(deletedTimeListPos - 1)
                        //        .getTime() / meanLength);
                    }
                    /*
                    Log.d("DTH (meanC.Remov.)", "arrayPos(" +
                            (deletedTimeListPos + cursor.getPosition())
                            + ")iterated time (id:" + iteratedTime.getId()
                            + "|time:" + iteratedTime.getTime() +
                            ") newMo" + meanLength + " = " + meanValue
                            + " | oldMo" + meanLength + " = "
                            + iteratedTime.getMean(meanLength));
                    */
                    iteratedTime.setMean(meanLength, meanValue);

                    if(deletedTimeListPos + cursor.getPosition() < times.size()
                            && deletedTimeListPos + cursor.getPosition() > 0) {
                        times.set(deletedTimeListPos + cursor.getPosition(), iteratedTime);
                    }

                    ContentValues values = new ContentValues();
                    if(meanLength == 3){
                        values.put(Database.TIMES_MO3, meanValue);
                    }else if(meanLength == 50){
                        values.put(Database.TIMES_MO50, meanValue);
                    }else if(meanLength == 100){
                        values.put(Database.TIMES_MO100, meanValue);
                    }
                    db.update(Database.TABLE_TIMES, values, Database.TIMES_ID + " = " + iteratedTime.getId(), null);

                } while (cursor.moveToNext());

            } else {
                Log.d("DTH (removeTime)", "0 times returned by the cursor");
                close();
            }
        }
    }

    private void updateAverageConcernedByTimeRemoving(TimeList times,
                                                      DatabaseTime deletedTime,
                                                      int deletedTimeListPos,
                                                      int avgLength){

        if(!db.isOpen()){
            open();
        }

        //récupération des x temps (ou moins selon la position du temps supprimé) auxquels il faut modifier l'avgX.
        String query = "SELECT * FROM " + Database.TABLE_TIMES +
                " WHERE " + Database.TIMES_EVENT + " = " + deletedTime.getEvent().getId() +
                " AND " + Database.TIMES_ID + " > " + deletedTime.getId() +
                " LIMIT " + (avgLength - 1);
        Cursor cursor = db.rawQuery(query, null);

        Log.d("DTH (removeTime)", cursor.getCount() + " rows needs to change their avg " + avgLength);

        if(deletedTimeListPos != times.size()) { // si l'on supprime le dernier temps, il n'influe sur aucun avg.
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    DatabaseTime iteratedTime = getCursorTime(cursor);
                    int avgValue = 0;
                    if (deletedTimeListPos + cursor.getPosition() >= (avgLength - 1)) {
                        // on retire le temps supprimé et on ajoute le temps d'avant.
                        final DatabaseTime[] timesOfNewAvg = new DatabaseTime[avgLength];
                        for(int i = 0; i < avgLength; i++){
                            timesOfNewAvg[i] = times.get(deletedTimeListPos + cursor.getPosition() - i);
                        }

                        avgValue = AverageCalculator.calculateAverage(timesOfNewAvg);
                    }

                    Log.d("DTH (removeTime)", "arrayPos(" + (deletedTimeListPos + cursor.getPosition()) + ")iterated time (id:" + iteratedTime.getId() + "|time:" + iteratedTime.getTime() + ") newAvg" + avgLength + " = " + avgValue + " | oldAvg5 = " + iteratedTime.getAvg5());

                    if(avgLength == 5){
                        iteratedTime.setAvg5(avgValue);
                    }else if(avgLength == 12){
                        iteratedTime.setAvg12(avgValue);
                    }

                    if(deletedTimeListPos + cursor.getPosition() < times.size() &&
                            (deletedTimeListPos + cursor.getPosition() > 0)){
                        times.set(deletedTimeListPos + cursor.getPosition(), iteratedTime);
                    }

                    ContentValues values = new ContentValues();
                    if(avgLength == 5){
                        values.put(Database.TIMES_AVG5, avgValue);
                    }else if(avgLength == 12){
                        values.put(Database.TIMES_AVG12, avgValue);
                    }
                    db.update(Database.TABLE_TIMES, values, Database.TIMES_ID + " = " + iteratedTime.getId(), null);

                } while (cursor.moveToNext());

            } else {
                Log.d("DTH (removeTime)", "0 times returned by the cursor");
                close();
            }
        }
    }

    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    public final RecordList getRecords(int eventId){
        RecordList records = new RecordList(eventId);
        final DatabaseTime bestSingle = getBestTime(Database.TIMES_SINGLE, eventId);

        if(bestSingle != null) {
            records.setSingle(bestSingle.getTimeAccordingToPenalty());
            records.setMo3(AverageCalculator.calculateMean(
                    getBestAverage(Database.TIMES_MO3, 3, eventId)));
            records.setAvg5(AverageCalculator.calculateAverage(
                    getBestAverage(Database.TIMES_AVG5, 5, eventId)));
            records.setAvg12(AverageCalculator.calculateAverage(
                    getBestAverage(Database.TIMES_AVG12, 12, eventId)));
            records.setMo50(AverageCalculator.calculateMean(
                    getBestAverage(Database.TIMES_MO50, 50, eventId)));
            records.setMo100(AverageCalculator.calculateMean(
                    getBestAverage(Database.TIMES_MO100, 100, eventId)));

        }else{
            records.setAll(RecordList.RECORD_NO_VALUE);
        }


        return records;
    }

    public final DatabaseTime getBestTime(String columnName, int eventId) {

        open();

        String query;

        if(columnName == Database.TIMES_SINGLE){
            query = "SELECT * FROM " + Database.TABLE_TIMES +
                    " WHERE " + Database.TIMES_EVENT + " = " + eventId +
                    " AND " + columnName + " != 0" +
                    " AND " + Database.TIMES_PENALTY + " != " + DatabaseTime.PENALTY_DNF +
                    " ORDER BY " + (columnName + "+(2000*" + Database.TIMES_PENALTY) + ") ASC" +
                    " LIMIT 1";
        }else{
            query = "SELECT * FROM " + Database.TABLE_TIMES +
                    " WHERE " + Database.TIMES_EVENT + " = " + eventId +
                    " AND " + columnName + " != 0" +
                    " AND " + columnName + " != " + TimerUtils.TIME_DNF +
                    " ORDER BY " + columnName + " ASC" +
                    " LIMIT 1";
        }
        Log.d("DTH (getBTime)", "query : " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {

            cursor.moveToFirst();
            final DatabaseTime time = getCursorTime(cursor);

            Log.d("DTH (getBestTime)", "record for " + columnName + " : " + time.toString());

            close();
            return time;

        } else {
            Log.e("DTH (getBestTime)", "there is no " + columnName + " record.");
            close();
            return null;
        }

    }

    private DatabaseTime[] getBestAverage(String columnName, int nbTimes, int eventId){

        final DatabaseTime lastTimeOfBestAverage = getBestTime(columnName, eventId);

        if(lastTimeOfBestAverage == null){
            Log.e("DTH (getBestAverage)", "there is no (best) time in BDD.");
            return null;
        }

        open();

        String query = "SELECT * FROM " + Database.TABLE_TIMES + " WHERE " + Database.TIMES_EVENT + " = " + eventId + " AND " + Database.TIMES_ID + " <= " + lastTimeOfBestAverage.getId() +
                " ORDER BY " + Database.TIMES_ID + " DESC LIMIT ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(nbTimes)});

        DatabaseTime[] timesOfBestAverage = new DatabaseTime[nbTimes];

        Log.d("DTH (getBestAverage)", "there is " + cursor.getCount() + " times according to the query : " + query + "{" + nbTimes + "}");

        if (cursor.getCount() == nbTimes) {
            Log.d("DTH (getBestAverage)", "getting best avg");

            int i = 0;

            if(cursor.moveToFirst()) {
                do {
                    timesOfBestAverage[i] = getCursorTime(cursor);
                    i++;
                } while (cursor.moveToNext());
            }
            close();
            return timesOfBestAverage;

        } else {
            Log.e("DTH (getBestAverage)", "there is only " + cursor.getCount() + " times before this one, and querying " + nbTimes + " times.");
            Log.e("DTH (getBestAverage)", "lastTimeOfBestAverage.getId() = " + lastTimeOfBestAverage.getId());
            //Log.e("DTH (getBestAverage)", "there is only " + cursor.getCount() + " times before this one (id:" + lastTimeOfBestAverage.getId() + "|time:" + TimerUtils.formatTime(lastTimeOfBestAverage));
            close();
            return null;
        }

    }

    private DatabaseTime getCursorTime(Cursor cursor){
        final DatabaseTime time = new DatabaseTime();
        time.setId(cursor.getInt(cursor.getColumnIndex(Database.TIMES_ID)));
        time.setTime(cursor.getInt(cursor.getColumnIndex(Database.TIMES_SINGLE)));
        time.setTime2(cursor.getInt(cursor.getColumnIndex(Database.TIMES_SINGLE2)));
        time.setMo3(cursor.getInt(cursor.getColumnIndex(Database.TIMES_MO3)));
        time.setAvg5(cursor.getInt(cursor.getColumnIndex(Database.TIMES_AVG5)));
        time.setAvg12(cursor.getInt(cursor.getColumnIndex(Database.TIMES_AVG12)));
        time.setMo50(cursor.getInt(cursor.getColumnIndex(Database.TIMES_MO50)));
        time.setMo100(cursor.getInt(cursor.getColumnIndex(Database.TIMES_MO100)));
        time.setEvent(CubeEvents.getEvent(context,
                cursor.getInt(cursor.getColumnIndex(Database.TIMES_EVENT))));
        time.setPenalty(cursor.getInt(cursor.getColumnIndex(Database.TIMES_PENALTY)));
        time.setDate(cursor.getString(cursor.getColumnIndex(Database.TIMES_DATE)));
        time.setScramble(cursor.getString(cursor.getColumnIndex(Database.TIMES_SCRAMBLE)));
        time.setSessionId(cursor.getInt(cursor.getColumnIndex(Database.TIMES_SESSION)));
        time.setComment(cursor.getString(cursor.getColumnIndex(Database.TIMES_COMMENT)));
        return time;
    }

    public String getColumnName(int nbTimes){
        switch(nbTimes){
            case 1 : return Database.TIMES_SINGLE;
            case 3 : return Database.TIMES_MO3;
            case 5 : return Database.TIMES_AVG5;
            case 12 : return Database.TIMES_AVG12;
            case 50 : return Database.TIMES_MO50;
            case 100 : return Database.TIMES_MO100;
            default : return null;
        }
    }

    public void printDB(){
        open();

        String query = "SELECT * FROM " + Database.TABLE_TIMES;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            Log.d("DTH (printDB)", "---" + cursor.getCount() + " rows in database---");

            if(cursor.moveToFirst()) {
                do {
                    getCursorTime(cursor).print();
                } while (cursor.moveToNext());
            }
        }else{
            Log.d("DTH (printDB)", "--- Empty database---");
            close();
            return;
        }

        close();
    }
}
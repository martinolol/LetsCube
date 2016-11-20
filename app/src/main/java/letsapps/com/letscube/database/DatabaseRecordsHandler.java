package letsapps.com.letscube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.util.cube.RecordList;

public class DatabaseRecordsHandler extends DatabaseHandler {

    public DatabaseRecordsHandler(Context context) {
        super(context);
    }

    public void updateRecord(int eventId, int avgType, int newValue){

        open();

        final ContentValues values = new ContentValues();
        values.put(getRow(avgType), newValue);

        final int nbUpdate = db.update(
                Database.TABLE_RECORDS, values, database.RECORDS_EVENT + "=" + eventId, null);

        if(nbUpdate == 0){
            recomputeRecord(eventId);
            db.update(
                    Database.TABLE_RECORDS, values, database.RECORDS_EVENT + "=" + eventId, null);
        }

        close();
    }

    public RecordList recomputeRecord(int eventId){

        Log.i("DRH", "Recomputing records for event " + eventId);

        final DatabaseTimeHandler databaseTimeHandler = new DatabaseTimeHandler(context);
        final RecordList actualRecords = databaseTimeHandler.getRecords(eventId);

        if(actualRecords.getSingle() == TimerUtils.TIME_NO_VALUE)
            actualRecords.setSingle(RecordList.RECORD_NO_VALUE);
        if(actualRecords.getMo3() == TimerUtils.TIME_NO_VALUE)
            actualRecords.setMo3(RecordList.RECORD_NO_VALUE);
        if(actualRecords.getAvg5() == TimerUtils.TIME_NO_VALUE)
            actualRecords.setAvg5(RecordList.RECORD_NO_VALUE);
        if(actualRecords.getAvg12() == TimerUtils.TIME_NO_VALUE)
            actualRecords.setAvg12(RecordList.RECORD_NO_VALUE);
        if(actualRecords.getMo50() == TimerUtils.TIME_NO_VALUE)
            actualRecords.setMo50(RecordList.RECORD_NO_VALUE);
        if(actualRecords.getMo100() == TimerUtils.TIME_NO_VALUE)
            actualRecords.setMo100(RecordList.RECORD_NO_VALUE);

        open();

        db.execSQL("DELETE FROM " + Database.TABLE_RECORDS +
                " WHERE " + Database.RECORDS_EVENT + " = " + eventId);

        db.execSQL("INSERT INTO " + Database.TABLE_RECORDS + " (" +
                Database.RECORDS_EVENT + ", " +
                Database.RECORDS_SINGLE + ", " +
                Database.RECORDS_MO3 + ", " +
                Database.RECORDS_AVG5 + ", " +
                Database.RECORDS_AVG12 + ", " +
                Database.RECORDS_MO50 + ", " +
                Database.RECORDS_MO100 + ") VALUES (" +
                eventId + ", " +
                actualRecords.getSingle() + ", " +
                actualRecords.getMo3() + ", " +
                actualRecords.getAvg5() + ", " +
                actualRecords.getAvg12() + ", " +
                actualRecords.getMo50() + ", " +
                actualRecords.getMo100() + ")");

        close();

        return actualRecords;
    }

    public RecordList getRecord(int eventId){

        RecordList record = new RecordList();

        open();

        final String query = "SELECT * FROM " + Database.TABLE_RECORDS +
                " WHERE " + Database.RECORDS_EVENT + " = " + eventId;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null) {
            if(cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    record.setEventId(eventId);
                    record.setSingle(
                            cursor.getInt(cursor.getColumnIndex(Database.RECORDS_SINGLE)));
                    record.setMo3(cursor.getInt(cursor.getColumnIndex(Database.RECORDS_MO3)));
                    record.setAvg5(cursor.getInt(cursor.getColumnIndex(Database.RECORDS_AVG5)));
                    record.setAvg12(cursor.getInt(cursor.getColumnIndex(Database.RECORDS_AVG12)));
                    record.setMo50(cursor.getInt(cursor.getColumnIndex(Database.RECORDS_MO50)));
                    record.setMo100(cursor.getInt(cursor.getColumnIndex(Database.RECORDS_MO100)));
                } while (cursor.moveToNext());
                close();
            }else{
                close();
                Log.e("DRH (getRecord)", "No record for " + eventId + " in database");
                return recomputeRecord(eventId);
            }
        }else{
            close();
            Log.e("DRH (getRecord)", "No record for " + eventId + " in database");
            return recomputeRecord(eventId);
        }

        return record;
    }

    private String getRow(int avgType){
        switch(avgType){
            case CubeRecord.TYPE_SINGLE : return Database.RECORDS_SINGLE;
            case CubeRecord.TYPE_MO3 : return Database.RECORDS_MO3;
            case CubeRecord.TYPE_AVG5 : return Database.RECORDS_AVG5;
            case CubeRecord.TYPE_AVG12 : return Database.RECORDS_AVG12;
            case CubeRecord.TYPE_MO50 : return Database.RECORDS_MO50;
            case CubeRecord.TYPE_MO100 : return Database.RECORDS_MO100;
            default : return null;
        }
    }

    public void printDB(){
        open();

        final String query = "SELECT * FROM " + Database.TABLE_RECORDS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null) {
            if(cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    Log.d("DRH", "EventId : " + cursor.getInt(cursor.getColumnIndex(Database.RECORDS_EVENT)) +
                            " | Single : " + cursor.getInt(cursor.getColumnIndex(Database.RECORDS_SINGLE)) +
                            " | Mo3 : " + cursor.getInt(cursor.getColumnIndex(Database.RECORDS_MO3)) +
                            " | Avg5 : " + cursor.getInt(cursor.getColumnIndex(Database.RECORDS_AVG5)) +
                            " | Avg12 : " + cursor.getInt(cursor.getColumnIndex(Database.RECORDS_AVG12)) +
                            " | Mo50 : " + cursor.getInt(cursor.getColumnIndex(Database.RECORDS_MO50)) +
                            " | Mo100 : " + cursor.getInt(cursor.getColumnIndex(Database.RECORDS_MO100)));
                } while (cursor.moveToNext());
            }else{
                Log.e("DRH", "No record in database");
            }
        }else{
            Log.e("DRH", "No record in database");
        }
        Log.d("DRH", "------------------------------------------------------");

        close();
    }
}
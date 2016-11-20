package letsapps.com.letscube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import letsapps.com.letscube.util.cube.Event;

public class DatabaseEventHandler extends DatabaseHandler {

    public DatabaseEventHandler(Context context) {
        super(context);
    }

    public final Event addEvent(Event eventToAdd) {
        open();

        ContentValues values = new ContentValues();
        values.put(Database.EVENTS_NAME, eventToAdd.getName());
        values.put(Database.EVENTS_SCRAMBLE_TYPE, eventToAdd.getScrambleTypeId());
        values.put(Database.EVENTS_IS_OFFICIAL, booleanToInteger(eventToAdd.isOfficial()));
        eventToAdd.setId((int) db.insert(Database.TABLE_EVENTS, null, values));

        Log.d("DH (addTime)", "event added : " + eventToAdd.getName());

        close();

        return eventToAdd;
    }

    public final ArrayList<Event> getEvents(){
        final ArrayList<Event> events = new ArrayList<>();

        open();

        String query = "SELECT * FROM " + Database.TABLE_EVENTS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            Log.d("DEH (getEvents)", "there is actually " + cursor.getCount() + " added events");

            if(cursor.moveToFirst()) {
                do {
                    events.add(getCursorEvent(cursor));
                } while (cursor.moveToNext());
            }
        }else{
            Log.d("DEH (getEvents)", "there is actually no added events");
            close();
            return null;
        }

        close();
        return events;
    }

    public final Event getEvent(int eventId){
        Event event;

        open();

        String query = "SELECT * FROM " + Database.TABLE_EVENTS +
                " WHERE " + Database.EVENTS_ID + " = " + eventId;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            event = getCursorEvent(cursor);
            Log.d("DEH (getEvent)", "event with id " + eventId + " is " + event.getName());
        }else{
            Log.d("DEH (getEvent)", "this event with id " + eventId + " doesn't exist");
            close();
            return null;
        }

        close();
        return event;
    }

    public final void removeEvent(Event eventToRemove){

        DatabaseTimeHandler dh = new DatabaseTimeHandler(context);
        dh.deleteEventTimes(eventToRemove);
        open();

        db.delete(Database.TABLE_EVENTS,
                Database.EVENTS_ID + "=" + eventToRemove.getId(), null);

        close();
    }

    public final void updateEvent(Event eventToUpdate){

        open();

        ContentValues values = new ContentValues();
        values.put(Database.EVENTS_NAME, eventToUpdate.getName());
        values.put(Database.EVENTS_SCRAMBLE_TYPE, eventToUpdate.getScrambleTypeId());

        db.update(Database.TABLE_EVENTS, values,
                Database.EVENTS_ID + "=" + eventToUpdate.getId(), null);

        close();
    }

    private int booleanToInteger(boolean b){
        return b ? 1 : 0;
    }

    private boolean integerToBoolean(int i){
        return i == 0 ? false : true;
    }

    private Event getCursorEvent(Cursor cursor){
        final Event event = new Event();
        event.setId(cursor.getInt(cursor.getColumnIndex(Database.EVENTS_ID)));
        event.setName(cursor.getString(cursor.getColumnIndex(Database.EVENTS_NAME)));
        event.setScrambleTypeId(cursor.getInt(
                cursor.getColumnIndex(Database.EVENTS_SCRAMBLE_TYPE)));
        event.setIsOfficial(integerToBoolean(
                cursor.getInt(cursor.getColumnIndex(Database.EVENTS_IS_OFFICIAL))));

        //Log.d("DEH (getCEvent)", "event : " + event.getName() + " -> id = " + event.getId());

        return event;
    }

}

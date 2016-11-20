package letsapps.com.letscube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.util.Date;
import letsapps.com.letscube.util.Session;
import letsapps.com.letscube.util.cube.Event;

public class DatabaseSessionHandler extends DatabaseHandler {

    public DatabaseSessionHandler(Context context) {
        super(context);
    }

    public Session addSession(Session session){
        open();

        ContentValues values = new ContentValues();
        values.put(Database.SESSION_NAME, session.getName());
        values.put(Database.SESSION_EVENT, session.getEventId());
        values.put(Database.SESSION_COMMENT, session.getComment());
        values.put(Database.SESSION_START_DATE, session.getStartDate());
        values.put(Database.SESSION_END_DATE, session.getEndDate());
        session.setId((int) db.insert(Database.TABLE_SESSIONS, null, values));

        Log.i("DSH (addSession)", "session added for " + session.getEventId());

        close();

        return session;
    }

    public final ArrayList<Session> getSessions(){
        final ArrayList<Session> sessions = new ArrayList<>();

        open();

        String query = "SELECT * FROM " + Database.TABLE_SESSIONS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            //Log.d("DSH (getSessions)", "there is actually " + cursor.getCount() + " sessions");

            if(cursor.moveToFirst()) {
                do {
                    sessions.add(getCursorSession(cursor));
                } while (cursor.moveToNext());
            }
        }else{
            //Log.d("DSH (getSessions)", "there is actually no sessions");
            close();
            return null;
        }

        close();
        return sessions;
    }

    public final Session getLastSession(int eventId){
        Session session;

        open();

        String query = "SELECT * FROM " + Database.TABLE_SESSIONS +
                " WHERE " + Database.SESSION_EVENT + " = " + eventId +
                " ORDER BY " + Database.SESSION_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            //Log.d("DSH (getSessions)", "there is actually " + cursor.getCount() + " sessions");
            cursor.moveToFirst();
            session = getCursorSession(cursor);
        }else{
            final Session sessionToAdd = new Session();
            sessionToAdd.setName(context.getString(R.string.default_name_first_session));
            sessionToAdd.setEventId(eventId);
            sessionToAdd.setStartDate(Date.getDate());
            session = addSession(sessionToAdd);
        }

        close();
        return session;
    }

    public final Session getSession(int sessionId){
        Session session;

        open();

        String query = "SELECT * FROM " + Database.TABLE_SESSIONS +
                " WHERE " + Database.SESSION_ID + " = " + sessionId + " LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            //Log.d("DSH (getSession)", "there is a session with ID : " + sessionId);
            cursor.moveToFirst();
            session = getCursorSession(cursor);
        }else{
            //Log.d("DSH (getSession)", "there is actually no session with ID : " + sessionId);
            close();
            return null;
        }

        close();
        return session;
    }

    public final ArrayList<Session> getSessions(Event event){
        final ArrayList<Session> sessions = new ArrayList<>();

        open();

        String query = "SELECT * FROM " + Database.TABLE_SESSIONS +
                " WHERE " + Database.SESSION_EVENT + " = " + event.getId();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            //Log.d("DSH (getSessions)", "there is actually " + cursor.getCount() + " sessions");

            if(cursor.moveToFirst()) {
                do {
                    sessions.add(getCursorSession(cursor));
                } while (cursor.moveToNext());
            }
        }else{
            //Log.d("DSH (getSessions)", "there is actually no sessions");
            close();
            return null;
        }

        close();
        return sessions;
    }

    private Session getCursorSession(Cursor cursor){
        final Session session = new Session();
        session.setId(cursor.getInt(cursor.getColumnIndex(Database.SESSION_ID)));
        session.setName(cursor.getString(cursor.getColumnIndex(Database.SESSION_NAME)));
        session.setComment(cursor.getString(cursor.getColumnIndex(Database.SESSION_COMMENT)));
        session.setStartDate(cursor.getString(cursor.getColumnIndex(Database.SESSION_START_DATE)));
        session.setEndDate(cursor.getString(cursor.getColumnIndex(Database.SESSION_END_DATE)));
        session.setEventId(cursor.getInt(cursor.getColumnIndex(Database.SESSION_EVENT)));
        return session;
    }
}

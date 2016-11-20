package letsapps.com.letscube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import letsapps.com.letscube.util.User;

/**
 * Created by Martin on 23/02/2016.
 */
public class DatabaseUserHandler extends DatabaseHandler {

    public DatabaseUserHandler(Context context){
        super(context);
    }

    public void addUser(User user){
        open();

        ContentValues values = new ContentValues();
        values.put(Database.USER_MIXPANEL_ID, user.getMixpanelID());
        values.put(Database.USER_NAME, user.getName());
        values.put(Database.USER_EMAIL, user.getEmail());
        db.insert(Database.TABLE_USER, null, values);

        Log.i("DHU (addUser)", user.getName() + " with email : " + user.getEmail() + " and " +
                "MixPanelId : " + user.getMixpanelID() + " was added successfully");

        close();
    }

    public void removeUser(){
        open();
        db.delete(Database.TABLE_USER,null,null);
        close();
    }

    public User getUser(){
        open();

        final User user = new User();

        final String query = "SELECT * FROM " + Database.TABLE_USER + " LIMIT 1";

        final Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();

            user.setMixpanelID(cursor.getString(
                    cursor.getColumnIndex(Database.USER_MIXPANEL_ID)));
            user.setName(cursor.getString(
                    cursor.getColumnIndex(Database.USER_NAME)));
            user.setEmail(cursor.getString(
                    cursor.getColumnIndex(Database.USER_EMAIL)));
        }else{
            Log.d("DHU (getUser)", "no user in database");
            close();
            return null;
        }

        close();
        return user;
    }
}

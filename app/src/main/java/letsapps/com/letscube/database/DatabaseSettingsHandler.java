package letsapps.com.letscube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.util.TimerStats;

public class DatabaseSettingsHandler extends DatabaseHandler {

    public DatabaseSettingsHandler(Context context) {
        super(context);
    }

    public void updateInspection(boolean isInspection){

        open();

        final ContentValues values = new ContentValues();
        values.put(Database.SETTINGS_VALUE_INT, isInspection ? 1 : 0);

        db.update(Database.TABLE_SETTINGS, values, database.SETTINGS_SETTING_ID + "=" +
                TimerSettings.SETTINGS_INSPECTION_ID, null);

        close();
    }

    public void updateTimerUpdate(int timerUpdate){

        open();

        final ContentValues values = new ContentValues();
        values.put(Database.SETTINGS_VALUE_INT, timerUpdate);

        db.update(Database.TABLE_SETTINGS, values, database.SETTINGS_SETTING_ID + "=" +
                TimerSettings.SETTINGS_TIMER_UPDATE_ID, null);

        close();
    }

    public void updateHomeContent(int homeContentId){

        open();

        final ContentValues values = new ContentValues();
        values.put(Database.SETTINGS_VALUE_INT, homeContentId);

        db.update(Database.TABLE_SETTINGS, values, database.SETTINGS_SETTING_ID + "=" +
                TimerSettings.SETTINGS_HOME_INFO_ID, null);

        close();
    }

    public void updateTheme(Theme theme){

        open();

        final ContentValues values = new ContentValues();
        values.put(Database.SETTINGS_VALUE_INT, theme.getId());

        db.update(Database.TABLE_SETTINGS, values, database.SETTINGS_SETTING_ID + "=" +
                TimerSettings.SETTINGS_THEME_ID, null);

        close();
    }

    public void updateBackground(Theme background){

        open();

        final ContentValues values = new ContentValues();
        values.put(Database.SETTINGS_VALUE_INT, background.getId());

        db.update(Database.TABLE_SETTINGS, values, database.SETTINGS_SETTING_ID + "=" +
                TimerSettings.SETTINGS_BACKGROUND_ID, null);

        close();
    }

    public void instantiateSettings(){

        createSettingsIfNotExist(TimerSettings.SETTINGS_INSPECTION_ID, 1, null);
        createSettingsIfNotExist(
                TimerSettings.SETTINGS_TIMER_UPDATE_ID, TimerSettings.TIMER_UPDATE_MILLISECOND, null);
        createSettingsIfNotExist(
                TimerSettings.SETTINGS_HOME_INFO_ID, TimerSettings.HOME_INFO_AVERAGES_ID, null);
        createSettingsIfNotExist(
                TimerSettings.SETTINGS_THEME_ID, TimerSettings.THEMES[0].getId(), null);
        createSettingsIfNotExist(
                TimerSettings.SETTINGS_BACKGROUND_ID, TimerSettings.BACKGROUND_WHITE.getId(), null);

        open();

        String query = "SELECT * FROM " + Database.TABLE_SETTINGS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                final int settingId = cursor.getInt(cursor.getColumnIndex(Database.SETTINGS_SETTING_ID));
                final int intValue = cursor.getInt(cursor.getColumnIndex(Database.SETTINGS_VALUE_INT));
                final String strValue = cursor.getString(cursor.getColumnIndex(Database.SETTINGS_VALUE_STRING));
                TimerSettings.getInstance().setSetting(settingId, intValue, strValue);
            }while (cursor.moveToNext());
        }else{
            Log.e("DSH (getSettings)", "No settings in database");
        }

        close();
    }

    public boolean createSettingsIfNotExist(int settingId, int defaultIntValue, String defaultStrValue){

        open();

        String query = "SELECT * FROM " + Database.TABLE_SETTINGS +
                " WHERE " + Database.SETTINGS_SETTING_ID + " = " + settingId;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() == 0){
            Log.d("DSH (createSetting)", "creating setting with id : " + settingId);
            final ContentValues values = new ContentValues();
            values.put(Database.SETTINGS_SETTING_ID, settingId);
            values.put(Database.SETTINGS_VALUE_INT, defaultIntValue);
            values.put(Database.SETTINGS_VALUE_STRING, defaultStrValue);
            db.insert(Database.TABLE_SETTINGS, null, values);
            close();
            return false;
        }

        close();
        return true;
    }
}
package letsapps.com.letscube.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    /* TABLE DES TEMPS DU TIMER */
    public static final String TABLE_TIMES = "TABLE_TIMES";
    public static final String TIMES_ID = "TIMES_ID";
    public static final String TIMES_SESSION = "TIMES_SESSION";
    public static final String TIMES_SINGLE = "TIMES_SINGLE";
    public static final String TIMES_SINGLE2 = "TIMES_SINGLE2";
    public static final String TIMES_MO3 = "TIMES_MO3";
    public static final String TIMES_AVG5 = "TIMES_AVG5";
    public static final String TIMES_AVG12 = "TIMES_AVG12";
    public static final String TIMES_MO50 = "TIMES_MO50";
    public static final String TIMES_MO100 = "TIMES_MO100";
    public static final String TIMES_DATE = "TIMES_DATE";
    public static final String TIMES_SCRAMBLE = "TIMES_SCRAMBLE";
    public static final String TIMES_COMMENT = "TIMES_COMMENT";
    public static final String TIMES_PENALTY = "TIMES_PENALTY";
    public static final String TIMES_EVENT = "TIMES_EVENT";
    public static final String CREATE_TABLE_TIMES =
            "CREATE TABLE " + TABLE_TIMES + " (" +
                    TIMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TIMES_EVENT + " INTEGER, " +
                    TIMES_SESSION + " INTEGER, " +
                    TIMES_DATE + " STRING, " +
                    TIMES_SINGLE + " INTEGER, " +
                    TIMES_SINGLE2 + " INTEGER, " +
                    TIMES_MO3 + " INTEGER, " +
                    TIMES_AVG5 + " INTEGER, " +
                    TIMES_AVG12 + " INTEGER, " +
                    TIMES_MO50 + " INTEGER, " +
                    TIMES_MO100 + " INTEGER, " +
                    TIMES_PENALTY + " INTEGER, " +
                    TIMES_SCRAMBLE + " STRING, " +
                    TIMES_COMMENT + " STRING);";

    /* TABLE DES SESSIONS DU TIMER */
    public static final String TABLE_SESSIONS = "TABLE_SESSIONS";
    public static final String SESSION_ID = "SESSION_ID";
    public static final String SESSION_EVENT = "SESSION_EVENT";
    public static final String SESSION_NAME = "SESSION_NAME";
    public static final String SESSION_START_DATE = "SESSION_START_DATE";
    public static final String SESSION_END_DATE = "SESSION_END_DATE";
    public static final String SESSION_COMMENT = "SESSION_COMMENT";
    public static final String CREATE_TABLE_SESSIONS =
            "CREATE TABLE " + TABLE_SESSIONS + " (" +
                    SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SESSION_EVENT + " INTEGER, " +
                    SESSION_NAME + " STRING, " +
                    SESSION_START_DATE + " STRING, " +
                    SESSION_END_DATE + " STRING, " +
                    SESSION_COMMENT + " STRING);";

    /* TABLE DES RECORDS DU TIMER */
    public static final String TABLE_RECORDS = "TABLE_RECORDS";
    public static final String RECORDS_ID = "RECORDS_ID";
    public static final String RECORDS_SINGLE = "RECORDS_SINGLE";
    public static final String RECORDS_MO3 = "RECORDS_MO3";
    public static final String RECORDS_AVG5 = "RECORDS_AVG5";
    public static final String RECORDS_AVG12 = "RECORDS_AVG12";
    public static final String RECORDS_MO50 = "RECORDS_MO50";
    public static final String RECORDS_MO100 = "RECORDS_MO100";
    public static final String RECORDS_EVENT = "RECORDS_EVENT";
    public static final String CREATE_TABLE_RECORDS =
            "CREATE TABLE " + TABLE_RECORDS + " (" +
                    RECORDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECORDS_EVENT + " INTEGER, " +
                    RECORDS_SINGLE + " INTEGER, " +
                    RECORDS_MO3 + " INTEGER, " +
                    RECORDS_AVG5 + " INTEGER, " +
                    RECORDS_AVG12 + " INTEGER, " +
                    RECORDS_MO50 + " INTEGER, " +
                    RECORDS_MO100 + " INTEGER);";

    /* TABLE DES STATS DU TIMER */
    public static final String TABLE_STATS = "TABLE_STATS";
    public static final String STATS_ID = "STATS_ID";
    public static final String STATS_NB_TIMES = "STATS_NB_TIMES";
    public static final String STATS_AVERAGE = "STATS_AVERAGE";
    public static final String STATS_STANDARD_DEVIATION = "STATS_STANDARD_DEVIATION";
    public static final String STATS_EVENT = "STATS_EVENT";
    public static final String CREATE_TABLE_STATS =
            "CREATE TABLE " + TABLE_STATS + " (" +
                    STATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STATS_NB_TIMES + " INTEGER, " +
                    STATS_AVERAGE + " INTEGER, " +
                    STATS_STANDARD_DEVIATION + " INTEGER, " +
                    STATS_EVENT + " INTEGER);";

    /* TABLE DES EVENTS DU TIMER */
    public static final String TABLE_EVENTS = "TABLE_EVENTS";
    public static final String EVENTS_ID = "EVENTS_ID";
    public static final String EVENTS_NAME = "EVENTS_NAME";
    public static final String EVENTS_SCRAMBLE_TYPE = "EVENTS_SCRAMBLE_TYPE";
    public static final String EVENTS_IS_OFFICIAL = "EVENTS_IS_OFFICIAL";

    public static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EVENTS_NAME + " STRING, " +
                    EVENTS_SCRAMBLE_TYPE + " INTEGER, " +
                    EVENTS_IS_OFFICIAL + " INTEGER);";

    /* TABLE DES SETTINGS DU TIMER */
    public static final String TABLE_SETTINGS = "TABLE_SETTINGS";
    public static final String SETTINGS_ID = "SETTINGS_ID";
    public static final String SETTINGS_SETTING_ID = "SETTINGS_SETTING_ID";
    public static final String SETTINGS_VALUE_INT = "SETTINGS_VALUE_INT";
    public static final String SETTINGS_VALUE_STRING = "SETTINGS_VALUE_STRING";

    public static final String CREATE_TABLE_SETTINGS =
            "CREATE TABLE " + TABLE_SETTINGS + " (" +
                    SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SETTINGS_SETTING_ID + " INTEGER, " +
                    SETTINGS_VALUE_INT + " INTEGER, " +
                    SETTINGS_VALUE_STRING + " STRING);";

    /* TABLE DE L'USER DU TIMER */
    public static final String TABLE_USER = "TABLE_USER";
    public static final String USER_ID = "USER_ID";
    public static final String USER_MIXPANEL_ID = "USER_MIXPANEL_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_ONBOARDING_STEP = "USER_ONBOARDING_STEP";

    public static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_MIXPANEL_ID + " STRING, " +
                    USER_NAME + " STRING, " +
                    USER_EMAIL + " STRING, " +
                    USER_ONBOARDING_STEP + " INTEGER);";

    private static final String DATABASE_NAME = "DATABASE_LETSCUBE";
    private static final int DATABASE_VERSION = 4;

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TIMES);
        db.execSQL(CREATE_TABLE_RECORDS);
        db.execSQL(CREATE_TABLE_STATS);
        db.execSQL(CREATE_TABLE_SETTINGS);
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SESSIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        for(int version = oldVersion + 1; version < newVersion; version++) {

            if (version == 2) {
                db.execSQL(CREATE_TABLE_USER);
            }
            if (version == 3) {
                final String upgradeQuery = "ALTER TABLE " + TABLE_TIMES + " ADD COLUMN "
                        + TIMES_SESSION + " INTEGER";
                db.execSQL(upgradeQuery);
                db.execSQL(CREATE_TABLE_SESSIONS);
            }
            if (version == 4) {
                db.execSQL(CREATE_TABLE_RECORDS);
                db.execSQL(CREATE_TABLE_STATS);

            }
            if (version == 5) {
                final String upgradeQuery = "ALTER TABLE " + TABLE_USER + " ADD COLUMN "
                        + USER_ONBOARDING_STEP + " INTEGER";
                db.execSQL(upgradeQuery);
            }

        }
    }
}

package letsapps.com.letscube.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import letsapps.com.letscube.listener.RecordListener;

public abstract class DatabaseHandler {

    protected final Context context;
    protected Database database;
    protected SQLiteDatabase db;

    public DatabaseHandler(Context context){
        this.context = context;
        database = new Database(context);
    }

    public void open() throws SQLException {
        db = database.getWritableDatabase();
    }

    public void close(){
        database.close();
    }
}

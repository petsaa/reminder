package com.igor.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by igor on 17.08.16.
 */
public class DbHelper extends SQLiteOpenHelper implements BaseColumns {

    public final static String DB_NAME = "tasklist.db";
    private final static int DB_VERSION = 2;
    public final static String TASKLIST_TABLE_NAME = "taskstodo";
    public final static String TASK_FAVS_TABLE = "taskfavs";
    public final static String NAME_COLUMN = "name";
    public final static String TASK_DESCRIPTION_COLUMN = "description";
    public final static String TASK_ID = "taskid";
    public final static String TASK_YEAR = "year";
    public final static String TASK_MONTH = "month";
    public final static String TASK_DAY  = "day";
    public final static String TASK_HOUR = "hour";
    public final static String TASK_MINUTE = "minute";

    private final static String DB_CREATE_CURTASK_SCRIPT = "create table " + TASKLIST_TABLE_NAME + " ( "
            + BaseColumns._ID + " integer primary key autoincrement, " + NAME_COLUMN + " text, "
            + TASK_DESCRIPTION_COLUMN + " text not null, " + TASK_ID + " text, " + TASK_YEAR + " text, "
            + TASK_MONTH + " text, "  + TASK_DAY + " text, " + TASK_HOUR + " text, "
            + TASK_MINUTE + " text);";
    private final static String DB_CREATE_FAVS_SCRIPT = "create table " + TASK_FAVS_TABLE + " ( "
            + BaseColumns._ID + " integer primary key autoincrement, " + NAME_COLUMN + " text, "
            + TASK_DESCRIPTION_COLUMN + " text not null, " + TASK_ID + " text, " + TASK_YEAR + " text, "
            + TASK_MONTH + " text, "  + TASK_DAY + " text, " + TASK_HOUR + " text, "
            + TASK_MINUTE + " text);";

    private final static String DROP_TABLE_CURTASK = "DROP TABLE IF EXISTS " + TASKLIST_TABLE_NAME;
    private final static String DROP_TABLE_FAVS = "DROP TABLE IF EXISTS " + TASK_FAVS_TABLE;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_CURTASK_SCRIPT);
        db.execSQL(DB_CREATE_FAVS_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CURTASK);
        db.execSQL(DROP_TABLE_FAVS);
        onCreate(db);
    }
}

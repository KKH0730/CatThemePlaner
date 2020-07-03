package com.planer.catthemeplaner.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

public class CalendarDatabase extends ViewModel {
    private final static String TAG = "CalendarDatabase";

    private DatabaseHelper dbHelper;
    private static CalendarDatabase database;
    private SQLiteDatabase db;
    private Context context;

    public static final String DATABASE_NAME = "planer.db";
    public static final String TABLE_CALENDAR = "calendarTable";
    public static final String TABLE_TODO = "todoTable";
    public static final String TABLE_COMPLETE_TODO = "completeTodoTable";
    public static final String TABLE_COMPLETE_LIST = "completeListTable";
    public static final String TABLE_HOLIDAY_LIST = "holidayListTable";
    public static final String TABLE_THEME_LIST = "themeListTable";
    public static final int DATABASE_VERSION = 2;

    public CalendarDatabase(Context context) {
        this.context = context;
    }

    public static CalendarDatabase getInstance(Context context) {


        if (database == null) {
            database = new CalendarDatabase(context);
        }
        return database;
    }

    public boolean open() {
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
        Log.d(TAG, "Database Open");

        return true;
    }

    public void close() {
        db.close();
        database = null;
        Log.d(TAG, "Database Close");
    }

    public Cursor rawQuery(String SQL) {

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SQL, null);
        } catch (Exception ex) {
            Log.d(TAG, "Exception in executeQuery", ex);
        }

        return cursor;
    }

    public void execSQL(String SQL) {

        try {
            db.execSQL(SQL);
        } catch (Exception ex) {
            Log.d(TAG, "Exception in execSQL", ex);
        }
    }


    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String DROP_SQL = "drop table if exists " + TABLE_CALENDAR;
            String DROP_SQL2 = "drop table if exists " + TABLE_TODO;
            String DROP_SQL3 = "drop table if exists " + TABLE_COMPLETE_TODO;
            String DROP_SQL4 = "drop table if exists " + TABLE_COMPLETE_LIST;
            String DROP_SQL5 = "drop table if exists " + TABLE_HOLIDAY_LIST;
            String DROP_SQL6 = "drop table if exists " + TABLE_THEME_LIST;


            try {
                db.execSQL(DROP_SQL);
                db.execSQL(DROP_SQL2);
                db.execSQL(DROP_SQL3);
                db.execSQL(DROP_SQL4);
                db.execSQL(DROP_SQL5);
                db.execSQL(DROP_SQL6);

            } catch (Exception ex) {
                Log.d(TAG, "Exception in DROP_SQL", ex);
            }
            String CREATE_SQL = "create table " + TABLE_CALENDAR + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " MEMO TEXT DEFAULT '', "
                    + " POSITION TEXT DEFAULT '', "
                    + " STARTDATE TEXT DEFAULT '', "
                    + " ENDDATE TEXT DEFAULT '', "
                    + " STARTTIME TEXT DEFAULT '', "
                    + " ENDTIME TEXT DEFAULT '', "
                    + " ALARM SMALLINT DEFAULT '', "
                    + " IMPORTANCE SMALLINT DEFAULT '', "
                    + " DESCRIPTION TEXT DEFAULT '', "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + " MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";

            String CREATE_TODO = "create table " + TABLE_TODO + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " TODO TEXT DEFAULT '', "
                    + " SHARED_NUMBER INTEGER DEFAULT '', "
                    + " RANK TEXT DEFAULT '', "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + " MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";

            String CREATE_COMPLETE_TODO = "create table " + TABLE_COMPLETE_TODO + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " COMPLETE_TODO TEXT DEFAULT '', "
                    + " SHARED_NUMBER INTEGER DEFAULT '', "
                    + " RANK TEXT DEFAULT '', "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + " MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";

            String CREATE_COMPLETE_LIST = "create table " + TABLE_COMPLETE_LIST + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " TODO_COMPLETE TEXT DEFAULT '', "
                    + " SHARED_NUMBER INTEGER DEFAULT '', "
                    + " RANK TEXT DEFAULT '', "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + " MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";

            String CREATE_HOLIDAY_LIST = "create table " + TABLE_HOLIDAY_LIST + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " HOLIDAY_DATE TEXT DEFAULT '', "
                    + " HOLIDAY_NAME TEXT DEFAULT '', "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + " MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";

            String Theme_LIST = "create table " + TABLE_THEME_LIST + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " THEME_NAME TEXT DEFAULT '', "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + " MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                db.execSQL(CREATE_SQL);
                db.execSQL(CREATE_TODO);
                db.execSQL(CREATE_COMPLETE_TODO);
                db.execSQL(CREATE_COMPLETE_LIST);
                db.execSQL(CREATE_HOLIDAY_LIST);
                db.execSQL(Theme_LIST);

            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            String CREATE_INDEX_SQL = "create index " + TABLE_CALENDAR + "_IDX ON "
                    + TABLE_CALENDAR + "("
                    + "CREATE_DATE"
                    + ")";
            try {
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
            }
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            Log.d(TAG, "Opened Database [" + DATABASE_NAME + "]");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "Upgrading database from version" + oldVersion + " to " + newVersion + ".");
            try {
                if (oldVersion < 2) {
                    db.execSQL("ALTER TABLE " + TABLE_CALENDAR + " ADD COLUMN  MEMO  TEXT DEFAULT ''");
                }
            } catch (Exception ex){
                Log.d(TAG, "Exception in Upgrading database " + oldVersion + " to " + newVersion + ".");
            }
        }
    }

}
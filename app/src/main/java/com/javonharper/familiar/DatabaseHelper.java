package com.javonharper.familiar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "Familiar.db";

    private static DatabaseHelper instance = null;

    private static final java.lang.String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Habit.TABLE_NAME + " (" +
                    Habit._ID + " INTEGER PRIMARY KEY," +
                    Habit.COLUMN_NAME_NAME + " TEXT, " +
                    Habit.COLUMN_NAME_TIMES_PER_DURATION + " INTEGER, " +
                    Habit.COLUMN_NAME_DURATION + " INTEGER, " +
                    Habit.COLUMN_NAME_CURRENT_PROGRESS + " INTEGER" +
            ")";

    private static final java.lang.String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Habit.TABLE_NAME;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }

        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            addDuration(db);
        }
    }

    public void addDuration(SQLiteDatabase db) {
        final String ALTER_TBL =
                "ALTER TABLE " + Habit.TABLE_NAME +
                        " ADD COLUMN " + Habit.COLUMN_NAME_DURATION + " INTEGER;";
        db.execSQL(ALTER_TBL);
    }
}

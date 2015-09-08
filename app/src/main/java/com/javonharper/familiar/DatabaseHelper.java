package com.javonharper.familiar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Familiar.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper mInstance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HABITS_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_HABITS_TABLE_SQL);
        db.execSQL(CREATE_HABITS_TABLE_SQL);
    }

    private static final String CREATE_HABITS_TABLE_SQL =
            "CREATE TABLE " + Habit.TABLE_NAME + " (" +
                    Habit._ID + " INTEGER PRIMARY KEY," +
                    Habit.COLUMN_NAME_NAME + " TEXT, " +
                    Habit.COLUMN_NAME_TIMES_PER_WEEK + " INTEGER, " +
                    Habit.COLUMN_NAME_DURATION + " INTEGER, " +
                    Habit.COLUMN_NAME_CURRENT_PROGRESS + " INTEGER" +
                    ")";

    private static final String DROP_HABITS_TABLE_SQL =
            "DROP TABLE IF EXISTS " + Habit.TABLE_NAME;
}

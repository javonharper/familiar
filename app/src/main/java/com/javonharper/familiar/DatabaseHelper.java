package com.javonharper.familiar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Familiar.db";
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
    private static DatabaseHelper instance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }

        return instance;
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

        if (oldVersion < 19) {
            addDays(db);
        }
    }

    public void addDuration(SQLiteDatabase db) {
        final String ALTER_TBL =
                "ALTER TABLE " + Habit.TABLE_NAME +
                        " ADD COLUMN " + Habit.COLUMN_NAME_DURATION + " INTEGER;";
        db.execSQL(ALTER_TBL);
    }

    public void addDays(SQLiteDatabase db) {
        final String MONDAY_ALTER_TBL ="ALTER TABLE " + Habit.TABLE_NAME + " ADD COLUMN " + Habit.COLUMN_NAME_DO_MONDAY + " INTEGER;";
        final String TUESDAY_ALTER_TBL ="ALTER TABLE " + Habit.TABLE_NAME + " ADD COLUMN " + Habit.COLUMN_NAME_DO_TUESDAY + " INTEGER;";
        final String WEDNESDAY_ALTER_TBL ="ALTER TABLE " + Habit.TABLE_NAME + " ADD COLUMN " + Habit.COLUMN_NAME_DO_WEDNESDAY + " INTEGER;";
        final String THURSDAY_ALTER_TBL ="ALTER TABLE " + Habit.TABLE_NAME + " ADD COLUMN " + Habit.COLUMN_NAME_DO_THURSDAY + " INTEGER;";
        final String FRIDAY_ALTER_TBL ="ALTER TABLE " + Habit.TABLE_NAME + " ADD COLUMN " + Habit.COLUMN_NAME_DO_FRIDAY + " INTEGER;";
        final String SATURDAY_ALTER_TBL ="ALTER TABLE " + Habit.TABLE_NAME + " ADD COLUMN " + Habit.COLUMN_NAME_DO_SATURDAY + " INTEGER;";
        final String SUNDAY_ALTER_TBL ="ALTER TABLE " + Habit.TABLE_NAME + " ADD COLUMN " + Habit.COLUMN_NAME_DO_SUNDAY + " INTEGER;";

        db.execSQL(MONDAY_ALTER_TBL);
        db.execSQL(TUESDAY_ALTER_TBL);
        db.execSQL(WEDNESDAY_ALTER_TBL);
        db.execSQL(THURSDAY_ALTER_TBL);
        db.execSQL(FRIDAY_ALTER_TBL);
        db.execSQL(SATURDAY_ALTER_TBL);
        db.execSQL(SUNDAY_ALTER_TBL);
    }
}

package com.javonharper.familiar;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class HabitController {
    DatabaseHelper dbHelper;

    public HabitController(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public List<Habit> getAllHabits() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Habit.TABLE_NAME, null, null, null, null, null, null);
        boolean hasHabits = cursor.moveToFirst();

        List<Habit> habits = new ArrayList<Habit>();

        if(hasHabits) {
            habits.add(populateHabit(cursor));

            while (cursor.moveToNext()) {
                Habit habit = populateHabit(cursor);
                habits.add(habit);
            }
        }

        cursor.close();
        db.close();

        return habits;
    }

    public int createHabit(Habit habit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Habit.COLUMN_NAME_NAME, habit.getName());
        values.put(Habit.COLUMN_NAME_DURATION, habit.getDuration());
        values.put(Habit.COLUMN_NAME_TIMES_PER_DURATION, habit.getTimesPerDuration());
        values.put(Habit.COLUMN_NAME_CURRENT_PROGRESS, habit.getCurrentProgress());

        long id = db.insert(Habit.TABLE_NAME, null, values);

        db.close();

        return (int) (long) id;
    }


    public Habit getHabit(Integer id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = Habit._ID + "=?";
        String[] selectionArgs = { id.toString() };

        Cursor cursor = db.query(Habit.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();

        Habit habit = populateHabit(cursor);

        db.close();
        return habit;
    }

    public void updateHabit(Habit habit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Habit.COLUMN_NAME_NAME, habit.getName());
        values.put(Habit.COLUMN_NAME_DURATION, habit.getDuration());
        values.put(Habit.COLUMN_NAME_TIMES_PER_DURATION, habit.getTimesPerDuration());
        values.put(Habit.COLUMN_NAME_CURRENT_PROGRESS, habit.getCurrentProgress());

        String selection = Habit._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(habit.getId())};

        db.update(Habit.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void deleteHabit(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = Habit._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.delete(Habit.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    private Habit populateHabit(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(Habit._ID);
        int nameIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_NAME);
        int durationIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DURATION);
        int timesPerDurationIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_TIMES_PER_DURATION);
        int currentProgressIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_CURRENT_PROGRESS);

        Integer id = (int) cursor.getLong(idIndex);
        String name = cursor.getString(nameIndex);
        Integer duration = Integer.valueOf(cursor.getInt(durationIndex));
        Integer timesPerDuration = Integer.valueOf(cursor.getInt(timesPerDurationIndex));
        Integer currentProgress = Integer.valueOf(cursor.getInt(currentProgressIndex));
        return new Habit(id, name, duration,timesPerDuration    , currentProgress);
    }
}
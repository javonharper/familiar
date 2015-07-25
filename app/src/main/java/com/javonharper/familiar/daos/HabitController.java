package com.javonharper.familiar.daos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.javonharper.familiar.data.DatabaseHelper;
import com.javonharper.familiar.models.Habit;

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

        if (hasHabits) {
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

        values.put(Habit.COLUMN_NAME_DO_MONDAY, habit.getDoOnMonday());
        values.put(Habit.COLUMN_NAME_DO_TUESDAY, habit.getDoOnTuesday());
        values.put(Habit.COLUMN_NAME_DO_WEDNESDAY, habit.getDoOnWednesday());
        values.put(Habit.COLUMN_NAME_DO_THURSDAY, habit.getDoOnThursday());
        values.put(Habit.COLUMN_NAME_DO_FRIDAY, habit.getDoOnFriday());
        values.put(Habit.COLUMN_NAME_DO_SATURDAY, habit.getDoOnSaturday());
        values.put(Habit.COLUMN_NAME_DO_SUNDAY, habit.getDoOnSunday());

        long id = db.insert(Habit.TABLE_NAME, null, values);

        db.close();

        return (int) (long) id;
    }


    public Habit getHabit(Integer id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = Habit._ID + "=?";
        String[] selectionArgs = {id.toString()};

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
        values.put(Habit.COLUMN_NAME_DO_MONDAY, habit.getDoOnMonday());
        values.put(Habit.COLUMN_NAME_DO_TUESDAY, habit.getDoOnTuesday());
        values.put(Habit.COLUMN_NAME_DO_WEDNESDAY, habit.getDoOnWednesday());
        values.put(Habit.COLUMN_NAME_DO_THURSDAY, habit.getDoOnThursday());
        values.put(Habit.COLUMN_NAME_DO_FRIDAY, habit.getDoOnFriday());
        values.put(Habit.COLUMN_NAME_DO_SATURDAY, habit.getDoOnSaturday());
        values.put(Habit.COLUMN_NAME_DO_SUNDAY, habit.getDoOnSunday());

        String selection = Habit._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(habit.getId())};

        db.update(Habit.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public void deleteHabit(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = Habit._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(Habit.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    private Habit populateHabit(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(Habit._ID);
        int nameIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_NAME);
        int durationIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DURATION);
        int timesPerDurationIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_TIMES_PER_DURATION);
        int currentProgressIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_CURRENT_PROGRESS);
        int doOnMondayIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DO_MONDAY);
        int doOnTuesdayIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DO_TUESDAY);
        int doOnWednesdayIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DO_WEDNESDAY);
        int doOnThursdayIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DO_THURSDAY);
        int doOnFridayIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DO_FRIDAY);
        int doOnSaturdayIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DO_SATURDAY);
        int doOnSundayIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_DO_SUNDAY);

        Integer id = (int) cursor.getLong(idIndex);
        String name = cursor.getString(nameIndex);
        Integer duration = Integer.valueOf(cursor.getInt(durationIndex));
        Integer timesPerDuration = Integer.valueOf(cursor.getInt(timesPerDurationIndex));
        Integer currentProgress = Integer.valueOf(cursor.getInt(currentProgressIndex));

        Boolean doOnMonday = Integer.valueOf(cursor.getInt(doOnMondayIndex)) > 0;
        Boolean doOnTuesday = Integer.valueOf(cursor.getInt(doOnTuesdayIndex)) > 0;
        Boolean doOnWednesday = Integer.valueOf(cursor.getInt(doOnWednesdayIndex)) > 0;
        Boolean doOnThursday = Integer.valueOf(cursor.getInt(doOnThursdayIndex)) > 0;
        Boolean doOnFriday = Integer.valueOf(cursor.getInt(doOnFridayIndex)) > 0;
        Boolean doOSaturday = Integer.valueOf(cursor.getInt(doOnSaturdayIndex)) > 0;
        Boolean doOnSunday = Integer.valueOf(cursor.getInt(doOnSundayIndex)) > 0;

        Habit habit = new Habit(id, name, duration, timesPerDuration, currentProgress);

        habit.setDoOnMonday(doOnMonday);
        habit.setDoOnTuesday(doOnTuesday);
        habit.setDoOnWednesday(doOnWednesday);
        habit.setDoOnThursday(doOnThursday);
        habit.setDoOnFriday(doOnFriday);
        habit.setDoOnSaturday(doOSaturday);
        habit.setDoOnSunday(doOnSunday);

        return habit;
    }
}
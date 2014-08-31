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
        cursor.moveToFirst();

        List<Habit> habits = new ArrayList<Habit>();

        while (cursor.moveToNext()) {
            Habit habit = populateHabit(cursor);
            habits.add(habit);
        }

        cursor.close();
        db.close();

        return habits;
    }

    public int createHabit(Habit habit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Habit.COLUMN_NAME_NAME, habit.getName());

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

        db.close();
        return populateHabit(cursor);
    }

    public void updateHabit(Habit habit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Habit.COLUMN_NAME_NAME, habit.getName());

        String selection = Habit._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(habit.getId())};

        db.close();
        db.update(Habit.TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteHabit(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = Habit._ID + "LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.close();
        db.delete(Habit.TABLE_NAME, selection, selectionArgs);
    }

    private Habit populateHabit(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(Habit._ID);
        int nameIndex = cursor.getColumnIndex(Habit.COLUMN_NAME_NAME);

        Integer id = (int) cursor.getLong(idIndex);
        String name = cursor.getString(nameIndex);
        return new Habit(id, name);
    }
}

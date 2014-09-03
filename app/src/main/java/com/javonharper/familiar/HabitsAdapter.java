package com.javonharper.familiar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HabitsAdapter extends ArrayAdapter<Habit> {

    public HabitsAdapter(Context context, List<Habit> habits) {
        super(context, R.layout.habit_list_item, habits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Habit habit = getItem(position);

        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.habit_list_item, parent, false);
        }

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.body_font));

        TextView nameView = (TextView) convertView.findViewById(R.id.habit_name);
        nameView.setTypeface(font);
        nameView.setText(habit.getName());

        TextView timesPerDurationView = (TextView) convertView.findViewById(R.id.habit_times_per_duration);
        timesPerDurationView.setTypeface(font);
        timesPerDurationView.setText(habit.getTimesPerDuration().toString());

        return convertView;
    }
}

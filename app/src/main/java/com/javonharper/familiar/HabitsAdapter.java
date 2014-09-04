package com.javonharper.familiar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        TextView progress = (TextView) convertView.findViewById(R.id.current_progress);
        progress.setTypeface(font);
        progress.setText(habit.getCurrentProgress().toString());

        TextView timesPerDuration = (TextView) convertView.findViewById(R.id.times_per_duration);
        timesPerDuration.setTypeface(font);
        timesPerDuration.setText(habit.getTimesPerDuration().toString());

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setTypeface(font);
        name.setText(habit.getName());

        if(habit.getCurrentProgress() >= habit.getTimesPerDuration()) {
            progress.setTextColor(convertView.getResources().getColor(R.color.green));
            timesPerDuration.setTextColor(convertView.getResources().getColor(R.color.green));
            ((TextView) convertView.findViewById(R.id.seperator)).setTextColor(convertView.getResources().getColor(R.color.green));
        }

        return convertView;
    }

}



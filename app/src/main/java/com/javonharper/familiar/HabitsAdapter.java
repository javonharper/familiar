package com.javonharper.familiar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HabitsAdapter extends BaseAdapter {

    private Context context = null;
    private List<Habit> habits = null;
    private LayoutInflater inflator = null;

    public HabitsAdapter(Context context, List<Habit> habits) {
        this.context = context;
        this.habits = habits;
        this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return habits.size();
    }

    @Override
    public Object getItem(int position) {
        return habits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Habit habit = (Habit) getItem(position);

        if (convertView == null) {
           convertView = LayoutInflater.from(context).inflate(R.layout.habit_list_item, parent, false);
        }

        Typeface font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.body_font));

        TextView progress = (TextView) convertView.findViewById(R.id.current_progress);
        progress.setTypeface(font);
        progress.setText(habit.getCurrentProgress().toString());

        TextView timesPerDuration = (TextView) convertView.findViewById(R.id.times_per_duration);
        timesPerDuration.setTypeface(font);
        timesPerDuration.setText(habit.getTimesPerDuration().toString());

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setTypeface(font);
        name.setText(habit.getName());

        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        duration.setTypeface(font);
        duration.setText(habit.getDuration().toString() + "m");

        if (habit.getCurrentProgress() >= habit.getTimesPerDuration()) {
            progress.setTextColor(convertView.getResources().getColor(R.color.green));
            timesPerDuration.setTextColor(convertView.getResources().getColor(R.color.green));
            ((TextView) convertView.findViewById(R.id.seperator)).setTextColor(convertView.getResources().getColor(R.color.green));
        } else if (habit.getCurrentProgress() > 0) {
            progress.setTextColor(convertView.getResources().getColor(R.color.dark_gray));
            timesPerDuration.setTextColor(convertView.getResources().getColor(R.color.dark_gray));
            ((TextView) convertView.findViewById(R.id.seperator)).setTextColor(convertView.getResources().getColor(R.color.dark_gray));
        }

        return convertView;
    }

}



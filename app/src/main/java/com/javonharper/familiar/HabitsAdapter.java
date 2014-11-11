package com.javonharper.familiar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        convertView = inflator.inflate(R.layout.habit_list_item, parent, false);

        TextView progress = (TextView) convertView.findViewById(R.id.current_progress);
        TextView sep = (TextView) convertView.findViewById(R.id.seperator);
        TextView timesPerDuration = (TextView) convertView.findViewById(R.id.times_per_duration);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView durationView = (TextView) convertView.findViewById(R.id.duration);

        Typeface font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.body_font));

        progress.setTypeface(font);
        sep.setTypeface(font);
        timesPerDuration.setTypeface(font);
        name.setTypeface(font);
        durationView.setTypeface(font);

        progress.setText(habit.getCurrentProgress().toString());
        timesPerDuration.setText(habit.getTimesPerDuration().toString());
        name.setText(habit.getName());

        Integer duration = habit.getDuration();

        if (duration == null || duration == 0) {
            durationView.setText("");
        } else {
            durationView.setText(habit.getDuration().toString() + "m");
        }

        if (habit.getCurrentProgress() >= habit.getTimesPerDuration()) {
            progress.setTextColor(convertView.getResources().getColor(R.color.green));
            timesPerDuration.setTextColor(convertView.getResources().getColor(R.color.green));
            sep.setTextColor(convertView.getResources().getColor(R.color.green));
        } else if (habit.getCurrentProgress() > 0) {
            progress.setTextColor(convertView.getResources().getColor(R.color.dark_gray));
            timesPerDuration.setTextColor(convertView.getResources().getColor(R.color.dark_gray));
            sep.setTextColor(convertView.getResources().getColor(R.color.dark_gray));
        }

        return convertView;
    }
}



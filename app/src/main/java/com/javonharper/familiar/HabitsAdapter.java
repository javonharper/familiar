package com.javonharper.familiar;

import android.content.Context;
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
        TextView timesPerWeek = (TextView) convertView.findViewById(R.id.times_per_week);
        TextView name = (TextView) convertView.findViewById(R.id.name_label);
        TextView durationView = (TextView) convertView.findViewById(R.id.duration);

        progress.setText(habit.getCurrentProgress().toString());
        timesPerWeek.setText(habit.getTimesPerWeek().toString());
        name.setText(habit.getName());

        Integer duration = habit.getDuration();

        if (duration == null || duration == 0) {
            durationView.setText("");
        } else {
            durationView.setText(habit.getDuration().toString() + "m");
        }

        if (habit.getCurrentProgress() >= habit.getTimesPerWeek()) {
            progress.setTextColor(convertView.getResources().getColor(R.color.green));
            timesPerWeek.setTextColor(convertView.getResources().getColor(R.color.green));
            sep.setTextColor(convertView.getResources().getColor(R.color.green));
            name.setTextColor(convertView.getResources().getColor(R.color.gray));
        } else if (habit.getCurrentProgress() > 0) {
            progress.setTextColor(convertView.getResources().getColor(R.color.gray_dark));
            timesPerWeek.setTextColor(convertView.getResources().getColor(R.color.gray_dark));
            sep.setTextColor(convertView.getResources().getColor(R.color.gray_dark));
        }

        return convertView;
    }
}



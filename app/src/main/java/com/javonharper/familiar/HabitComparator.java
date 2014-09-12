package com.javonharper.familiar;

import java.util.Comparator;

class HabitComparator implements Comparator<Habit> {
    @Override
    public int compare(Habit habit1, Habit habit2) {
        if (habit1.getTimesPerDuration() > habit2.getTimesPerDuration()) {
            return -1;
        } else if (habit1.getTimesPerDuration() < habit2.getTimesPerDuration()) {
            return 1;
        } else {
            if (habit1.getCurrentProgress() > habit2.getCurrentProgress()) {
                return -1;
            } else if (habit1.getCurrentProgress() < habit2.getCurrentProgress()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

package com.javonharper.familiar;

import java.util.Comparator;

class HabitComparator implements Comparator<Habit> {
    @Override
    public int compare(Habit habit1, Habit habit2) {
        int sort = compareByCompleted(habit1, habit2);

        if (sort == 0) {
            sort = compareByDuration(habit1, habit2);

            if (sort == 0) {
                sort = compareByFrequency(habit1, habit2);
            }
        }

        return sort;
    }

    private int compareByCompleted(Habit habit1, Habit habit2) {
        if (habit1.isCompleted() && !habit2.isCompleted()) {
            return 1;
        } else if (!habit1.isCompleted() && habit2.isCompleted()) {

            return -1;
        } else {
            return 0;
        }
    }

    private int compareByFrequency(Habit habit1, Habit habit2) {
        if (habit1.getTimesPerDuration() < habit2.getTimesPerDuration()) {
            return 1;
        } else if (habit1.getTimesPerDuration() > habit2.getTimesPerDuration()) {
            return -1;
        } else {
            return 0;
        }
    }

    private int compareByDuration(Habit habit1, Habit habit2) {
        if (habit1.getDuration() < habit2.getDuration()) {
            return 1;
        } else if (habit1.getDuration() > habit2.getDuration()) {
            return -1;
        } else {
            return 0;
        }
    }
}

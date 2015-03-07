package com.javonharper.familiar;


import android.provider.BaseColumns;

public class FamiliarContract {

    public FamiliarContract() {
    }

    public static abstract class HabitContract implements BaseColumns {
        public static final String TABLE_NAME = "habit";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TIMES_PER_DURATION = "times_per_duration";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_CURRENT_PROGRESS = "current_progress";

        public static final String COLUMN_NAME_DO_MONDAY = "do_monday";
        public static final String COLUMN_NAME_DO_TUESDAY = "do_tuesday";
        public static final String COLUMN_NAME_DO_WEDNESDAY = "do_wednesday";
        public static final String COLUMN_NAME_DO_THURSDAY = "do_thursday";
        public static final String COLUMN_NAME_DO_FRIDAY = "do_friday";
        public static final String COLUMN_NAME_DO_SATURDAY = "do_saturday";
        public static final String COLUMN_NAME_DO_SUNDAY = "do_sunday";
    }
}

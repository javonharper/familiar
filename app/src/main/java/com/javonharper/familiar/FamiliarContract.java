package com.javonharper.familiar;


import android.provider.BaseColumns;

public class FamiliarContract {
    
    public FamiliarContract() {
    }

    public static abstract class HabitContract implements BaseColumns {
        public static final String TABLE_NAME = "habit";
        public static final String COLUMN_NAME_NAME = "name";
    }
}

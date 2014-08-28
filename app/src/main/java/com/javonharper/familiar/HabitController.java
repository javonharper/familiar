package com.javonharper.familiar;

import java.util.ArrayList;

public class HabitController {
    ArrayList<String> habits;

    public HabitController() {
        habits = new ArrayList<String>();
        habits.add("Exercise");
        habits.add("Read");
        habits.add("Journal");
        habits.add("Draw");
        habits.add("Practice Guitar");
    }

    public ArrayList<String> getIndex() {
        return habits;
    }
}

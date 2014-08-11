package com.javonharper.familiar;

import java.util.ArrayList;

public class HabitController {

    public HabitController() {

    }

    public ArrayList<String> getIndex() {
        ArrayList<String> habits = new ArrayList<String>();

        habits.add("Exercise");
        habits.add("Read");
        habits.add("Journal");
        habits.add("Draw");
        habits.add("Practice Guitar");

        return habits;
    }
}

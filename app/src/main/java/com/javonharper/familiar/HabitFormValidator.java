package com.javonharper.familiar;


import java.util.HashMap;
import java.util.Map;

public class HabitFormValidator {

    public static String NAME = "name";
    public static String TIMES_PER_WEEK = "timesPerWeek";
    public static String DURATION = "duration";

    private final String name;
    private final String timesPerWeek;
    private final String duration;

    private String nameError;
    private String timesPerWeekError;
    private String durationError;

    public HabitFormValidator(String name, String timesPerWeek, String duration) {
        this.name = name;
        this.timesPerWeek = timesPerWeek;
        this.duration = duration;
    }

    public void validate() {
        nameError = validateName(name);
        timesPerWeekError = validateTimesPerWeek(timesPerWeek);
        durationError = validateDuration(duration);
    }

    public boolean isValid() {
        if (nameError == null && timesPerWeekError == null && durationError == null) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, String> getErrors() {
        HashMap errors = new HashMap<String, String>();
        errors.put(NAME, nameError);
        errors.put(TIMES_PER_WEEK, timesPerWeekError);
        errors.put(DURATION, durationError);
        return errors;
    }

    private String validateName(String name) {
        if (name.equals("")) {
            return "Fill in name";
        } else {
            return null;
        }
    }

    private String validateTimesPerWeek(String timesPerWeek) {
        if (timesPerWeek.equals("")) {
            return "Fill in times per week";
        } else {
            return null;
        }
    }

    private String validateDuration(String duration) {
        if (duration.equals("")) {
            return "Fill in duration";
        } else {
            return null;
        }
    }
}

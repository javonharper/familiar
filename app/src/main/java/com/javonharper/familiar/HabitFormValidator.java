package com.javonharper.familiar;


import java.util.HashMap;
import java.util.Map;

public class HabitFormValidator {

    public static String NAME = "name";
    public static String TIMES_PER_DURATION = "timesPerDuration";
    public static String DURATION = "duration";

    private final String name;
    private final String timesPerDuration;
    private final String duration;

    private String nameError;
    private String timesPerDurationError;
    private String durationError;

    public HabitFormValidator(String name, String timesPerDuration, String duration) {
        this.name = name;
        this.timesPerDuration = timesPerDuration;
        this.duration = duration;
    }

    public void validate() {
        nameError = validateName(name);
        timesPerDurationError = validateTimesPerDuration(timesPerDuration);
        durationError = validateDuration(duration);
    }

    public boolean isValid() {
        if (nameError == null && timesPerDurationError == null && durationError == null) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, String> getErrors() {
        HashMap errors = new HashMap<String, String>();
        errors.put(NAME, nameError);
        errors.put(TIMES_PER_DURATION, timesPerDurationError);
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

    private String validateTimesPerDuration(String timesPerDuration) {
        if (timesPerDuration.equals("")) {
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

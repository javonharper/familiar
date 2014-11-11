package com.javonharper.familiar;

public class Habit extends FamiliarContract.HabitContract {
    private Integer id;
    private String name;
    private Integer timesPerDuration;
    private Integer currentProgress;
    private Integer duration;

    public Habit() {

    }

    public Habit(Integer id, String name, Integer duration, Integer timesPerDuration, Integer currentProgress) {
        this.setId(id);
        this.setName(name);
        this.setDuration(duration);
        this.setTimesPerDuration(timesPerDuration);
        this.setCurrentProgress(currentProgress);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimesPerDuration() {
        return this.timesPerDuration;
    }

    public void setTimesPerDuration(Integer timesPerDuration) {
        this.timesPerDuration = timesPerDuration;
    }

    public Integer getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Integer currentProgress) {
        this.currentProgress = currentProgress;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public boolean isCompleted() {
        return this.getCurrentProgress() >= this.getTimesPerDuration();
    }

    public String toString() {
        String id;

        if (this.getId() == null) {
            id = "NO_ID";
        } else {
            id = this.getId().toString();
        }
        return "Habit[" + id + ": " + this.getName() + " (" + this.getCurrentProgress().toString() + "/" + this.getTimesPerDuration().toString() + ")]";
    }
}

package com.javonharper.familiar;

public class Habit extends FamiliarContract.HabitContract {
    private Integer id;
    private String name;
    private Integer timesPerDuration;
    private Integer currentProgress;
    private Integer duration;
    private Boolean doOnMonday;
    private Boolean doOnTuesday;
    private Boolean doOnWednesday;
    private Boolean doOnThursday;
    private Boolean doOnFriday;
    private Boolean doOnSaturday;
    private Boolean doOnSunday;


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

    public Boolean getDoOnMonday() {
        return doOnMonday;
    }

    public void setDoOnMonday(Boolean doOnMonday) {
        this.doOnMonday = doOnMonday;
    }

    public Boolean getDoOnTuesday() {
        return doOnTuesday;
    }

    public void setDoOnTuesday(Boolean doOnTuesday) {
        this.doOnTuesday = doOnTuesday;
    }

    public Boolean getDoOnWednesday() {
        return doOnWednesday;
    }

    public void setDoOnWednesday(Boolean doOnWednesday) {
        this.doOnWednesday = doOnWednesday;
    }

    public Boolean getDoOnThursday() {
        return doOnThursday;
    }

    public void setDoOnThursday(Boolean doOnThursday) {
        this.doOnThursday = doOnThursday;
    }

    public Boolean getDoOnFriday() {
        return doOnFriday;
    }

    public void setDoOnFriday(Boolean doOnFriday) {
        this.doOnFriday = doOnFriday;
    }

    public Boolean getDoOnSaturday() {
        return doOnSaturday;
    }

    public void setDoOnSaturday(Boolean doOnSaturday) {
        this.doOnSaturday = doOnSaturday;
    }

    public Boolean getDoOnSunday() {
        return doOnSunday;
    }

    public void setDoOnSunday(Boolean doOnSunday) {
        this.doOnSunday = doOnSunday;
    }
}

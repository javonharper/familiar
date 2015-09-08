package com.javonharper.familiar;

public class Habit extends FamiliarContract.HabitContract {
    private Integer id;
    private String name;
    private Integer timesPerWeek;
    private Integer currentProgress;
    private Integer duration;


    public Habit() {

    }

    public Habit(Integer id, String name, Integer duration, Integer timesPerWeek, Integer currentProgress) {
        this.setId(id);
        this.setName(name);
        this.setDuration(duration);
        this.setTimesPerWeek(timesPerWeek);
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

    public Integer getTimesPerWeek() {
        return this.timesPerWeek;
    }

    public void setTimesPerWeek(Integer timesPerWeek) {
        this.timesPerWeek = timesPerWeek;
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
        return this.getCurrentProgress() >= this.getTimesPerWeek();
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", timesPerWeek=" + timesPerWeek +
                ", currentProgress=" + currentProgress +
                ", duration=" + duration +
                '}';
    }
}

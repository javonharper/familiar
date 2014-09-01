package com.javonharper.familiar;

public class Habit extends FamiliarContract.HabitContract {
    private Integer id = null;
    private String name = null;
    private Integer timesPerDuration = null;

    public Habit(Integer id, String name, Integer timesPerDuration) {
        this.setId(id);
        this.setName(name);
        this.setTimesPerDuration(timesPerDuration);
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

    public String toString() {
        String id;

        if (this.getId() == null) {
            id = "NO_ID";
        } else {
            id = this.getId().toString();
        }
        return "Habit(" + id + ": " + this.getName() + "x" + this.getTimesPerDuration().toString() +")";
    }
}

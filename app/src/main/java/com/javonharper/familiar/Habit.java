package com.javonharper.familiar;

public class Habit extends FamiliarContract.HabitContract {
    private Integer id;
    private String name;

    public Habit(Integer id, String name) {
        this.setId(id);
        this.setName(name);
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

    public String toString() {
        String id;

        if (this.getId() == null) {
            id = "NO_ID";
        } else {
            id = this.getId().toString();
        }
        return "Habit(" + id + ": " + this.getName() + ")";
    }
}

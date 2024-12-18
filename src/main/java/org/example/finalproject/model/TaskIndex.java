package org.example.finalproject.model;

import java.util.ArrayList;
import java.util.List;

public class TaskIndex {
    private String name; // Index Name
    private List<Task> tasks = new ArrayList<>(); // List of Tasks

    public TaskIndex(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}


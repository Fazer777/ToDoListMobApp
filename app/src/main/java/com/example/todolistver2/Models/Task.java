package com.example.todolistver2.Models;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private String name;
    //private List<SubTask> subTaskList;
    private Boolean isCompleted;
    private int color;
    private String description;
    private LocalDate date;

//    public class SubTask implements Serializable{
//        private String name;
//        private Boolean isCompleted;
//    }
    public Task(){

    }
    public Task(String name, Boolean isCompleted, int color, String description, LocalDate date){
        this.name = name;
        this.isCompleted = isCompleted;
        this.color = color;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

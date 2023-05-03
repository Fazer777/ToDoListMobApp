package com.example.todolistver2.Models;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Task implements Serializable {

    private String name;
    private LocalDateTime dateTime;
    private int colorTask;
    private LocalTime time;

    public Task(){

    }

    public Task(String name, LocalDateTime dateTime, int colorTask, LocalTime time) {
        this.name = name;
        this.dateTime = dateTime;
        this.colorTask = colorTask;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getColorTask() {
        return colorTask;
    }

    public void setColorTask(int colorTask) {
        this.colorTask = colorTask;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void addToTimeTask(long amountMillis){
        long seconds = amountMillis/1000;
        time = time.plus(seconds, ChronoUnit.SECONDS);
        //time = time.plus(Duration.ofSeconds(amount));
    }
}

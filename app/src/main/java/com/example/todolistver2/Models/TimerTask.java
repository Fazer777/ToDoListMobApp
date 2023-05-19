package com.example.todolistver2.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimerTask implements Serializable {

    private String name;
    private LocalDate date;
    private int colorTask;
    private LocalTime time;
    private int itemIndex;

    public TimerTask(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate dateTime) {
        this.date = dateTime;
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
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }
}

package com.example.todolistver2.Models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Note implements Serializable {

    private String description;
    private LocalDateTime noteDateTime;
    private String category;

    //TODO Можно добавить поле "цвет заметки", который зависит от категории
    //private int colorNote;

    Note() {

    }

    Note(String description, LocalDateTime noteDateTime, String category){
        this.description = description;
        this.noteDateTime = noteDateTime;
        this.category = category;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLocalDateTime() {
        return noteDateTime;
    }

    public void setLocalDateTime(LocalDateTime noteDateTime) {
        this.noteDateTime = noteDateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //TODO Сделать методы конвертации даты в строку и обратно
}

package com.example.todolistver2.Models;

import android.util.Log;

import com.example.todolistver2.Constants.Constants;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Note implements Serializable {

    private String description;
    private LocalDateTime noteDateTime;
    private Category category;


    public Note() {

    }

   public Note( String description, LocalDateTime noteDateTime, Category category){
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

    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


}

package com.example.todolistver2.fragments.Notes;

import android.graphics.Color;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

    public static Category[] categories = {
            new Category("Без категории", Color.parseColor("#FFFFFFFF")),
            new Category("Работа", Color.parseColor("#FD9292")),
            new Category("Фильмы", Color.parseColor("#818EFF")),
            new Category("Путешествия", Color.parseColor("#FFD75E")),
            new Category("Повседневное", Color.parseColor("#95E478")),
            new Category("Игры", Color.parseColor("#FFAD54"))
    };

    private String name;
    private int color;

    public Category(){

    }

    public Category(String name, int color){
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

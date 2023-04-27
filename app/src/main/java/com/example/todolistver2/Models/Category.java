package com.example.todolistver2.Models;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {

    public static List<Category> categories = new ArrayList<Category>(){
        {
            add(new Category("Без категории", Color.parseColor("#FFFFFF")));// Белый
            add(new Category("Работа", Color.parseColor("#FFBDB3"))); // Красный
            add(new Category("Фильмы", Color.parseColor("#C2C8FF"))); // Синий
            add(new Category("Путешествия", Color.parseColor("#FFE8A8"))); // Желтый
            add(new Category("Повседневное", Color.parseColor("#CAFFB8"))); // Зеленый
            add(new Category("Игры", Color.parseColor("#FFD79E")));// Оранженвый

        }};


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

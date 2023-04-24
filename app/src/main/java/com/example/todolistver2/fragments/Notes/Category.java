package com.example.todolistver2.fragments.Notes;

import android.content.res.Resources;
import android.graphics.Color;

import androidx.core.content.res.ResourcesCompat;

import com.example.todolistver2.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {

    public static List<Category> categories = new ArrayList<Category>(){
        {
            add(new Category("Без категории", Color.parseColor("#FFFFFF")));// Белый
            add(new Category("Работа", Color.parseColor("#FF9999"))); // Красный
            add(new Category("Фильмы", Color.parseColor("#818EFF"))); // Синий
            add(new Category("Путешествия", Color.parseColor("#FFD75E"))); // Желтый
            add(new Category("Повседневное", Color.parseColor("#95E478"))); // Зеленый
            add(new Category("Игры", Color.parseColor("#FFAD54")));// Оранженвый

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

package com.example.todolistver2.Models;
import java.io.Serializable;

public class Category implements Serializable {

    private String name;
    private int color;
    private int itemIndex;

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

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }
}

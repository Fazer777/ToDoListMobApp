package com.example.todolistver2.Database;

import android.graphics.Color;

public class TableCategories {
    public static final String TABLE_NAME = "Categories";
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String COLOR = "Color";
    public static final String ITEM_INDEX ="ItemIndex";

    public static final String CREATE_TABLE_CATEGORIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    NAME + " TEXT, " +
                    COLOR + " INTEGER, " +
                    ITEM_INDEX + " INTEGER)";

    public static final String DELETE_TABLE_CATEGORIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String INSERT_DEFAULT_CATEGORIES =
            "INSERT INTO " + TABLE_NAME + " ("+ NAME +","+ COLOR + ","+ ITEM_INDEX + ")  " +
                    "VALUES  " +
                    "(" + "'Без категории'" +", " + Color.parseColor("#FFFFFF") +", "+ 0 + "), "+
                    "(" + "'Работа'" +", " + Color.parseColor("#FFBDB3") +", "+ 1 + "), "+
                    "(" + "'Фильмы'" +", " + Color.parseColor("#C2C8FF") +", "+ 2 + "), " +
                    "(" + "'Путешествия'" +", " + Color.parseColor("#FFE8A8") +", "+ 3 + "), " +
                    "(" + "'Повседневное'" + ", " + Color.parseColor("#CAFFB8") +", "+ 4 + "), " +
                    "(" + "'Игры'" + ", " + Color.parseColor("#FFD79E") +", "+ 5 + ")";
}

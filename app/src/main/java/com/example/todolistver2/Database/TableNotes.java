package com.example.todolistver2.Database;

public class TableNotes {
    public static final String TABLE_NAME = "Notes";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_NAME = "category";
    public static final String NOTE_DATETIME = "note_dateTime";
    public static final String COLOR_CATEGORY = "color_category";
    public static final String ITEM_INDEX = "item_index";

    public static final String CREATE_TABLE_NOTES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    DESCRIPTION + " TEXT," +
                    CATEGORY_NAME + " TEXT," +
                    NOTE_DATETIME + " TEXT," +
                    COLOR_CATEGORY + " TEXT," +
                    ITEM_INDEX + " INTEGER)";

    public static final String DELETE_TABLE_NOTES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

}

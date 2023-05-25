package com.example.todolistver2.Database;

public class TableNotes {
    public static final String TABLE_NAME = "Notes";
    public static final String ID = "Id";
    public static final String DESCRIPTION = "Description";
    public static final String DATE_TIME = "DateTime";
    public static final String ITEM_INDEX = "NoteItemIndex";
    public static final String CATEGORY_ID = "CategoryId";

    public static final String CREATE_TABLE_NOTES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    DESCRIPTION + " TEXT," +
                    DATE_TIME + " TEXT," +
                    CATEGORY_ID + " INTEGER," +
                    ITEM_INDEX + " INTEGER," +
                    "FOREIGN KEY ("+ CATEGORY_ID +") REFERENCES "
                    + TableCategories.TABLE_NAME +"("+TableCategories.ID+"))";


    public static final String DELETE_TABLE_NOTES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

}

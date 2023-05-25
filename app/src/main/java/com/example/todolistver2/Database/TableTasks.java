package com.example.todolistver2.Database;

public class TableTasks {
    public static final String TABLE_NAME = "Tasks";
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String DATE = "Date";
    public static final String IS_COMPLETED = "IsCompleted";
    public static final String COMPLETION_DATE = "CompletionDate";
    public static final String COLOR = "Color";
    public static final String ITEM_INDEX = "ItemIndex";

    public static final String CREATE_TABLE_TASKS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    NAME + " TEXT," +
                    DESCRIPTION + " TEXT," +
                    DATE + " TEXT," +
                    IS_COMPLETED + " INTEGER," +
                    COMPLETION_DATE + " TEXT," +
                    COLOR + " TEXT," +
                    ITEM_INDEX + " INTEGER)";

    public static final String DELETE_TABLE_TASKS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}

package com.example.todolistver2.Database;

public class TableTasks {
    public static final String TABLE_NAME = "Tasks";
    public static final String ID = "id";
    public static final String TASK_NAME = "task_name";
    public static final String TASK_DESCRIPTION = "task_description";
    public static final String TASK_DATE = "task_date";
    public static final String TASK_COMPLETED = "task_completed";
    public static final String TASK_COLOR = "task_color";

    public static final String CREATE_TABLE_TASKS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    TASK_NAME + " TEXT," +
                    TASK_DESCRIPTION + " TEXT," +
                    TASK_DATE + " TEXT," +
                    TASK_COMPLETED + " INTEGER," +
                    TASK_COLOR + " TEXT)";

    public static final String DELETE_TABLE_TASKS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}

package com.example.todolistver2.Database;

public class TableTimerTasks {
    public static final String TABLE_NAME = "TimerTasks";
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String DATE_TIME = "DateTime";
    public static final String COLOR = "Color";
    public static final String TIME = "Time";
    public static final String ITEM_INDEX = "ItemIndex";

    public static final String CREATE_TABLE_TIMER_TASKS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    NAME + " TEXT," +
                    DATE_TIME + " TEXT," +
                    COLOR + " TEXT," +
                    TIME + " TEXT," +
                    ITEM_INDEX + " INTEGER)";

    public static final String DELETE_TABLE_TIMER_TASKS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}

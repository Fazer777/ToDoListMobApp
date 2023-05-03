package com.example.todolistver2.Database;

// Таблица TimerTasks (задач на таймере)

public class TableTimerTasks {
    public static final String TABLE_NAME = "TimerTasks";
    public static final String ID = "id";
    public static final String TIMER_TASK_NAME = "timer_task_name";
    public static final String TIMER_TASK_DATETIME = "timer_task_datetime";
    public static final String TIMER_TASK_COLOR = "timer_task_color";
    public static final String TIMER_TASK_TIME = "timer_task_timer";

    public static final String CREATE_TABLE_TIMER_TASKS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    TIMER_TASK_NAME + " TEXT," +
                    TIMER_TASK_DATETIME + " TEXT," +
                    TIMER_TASK_COLOR + " TEXT," +
                    TIMER_TASK_TIME + " TEXT)";

    public static final String DELETE_TABLE_TIMER_TASKS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}

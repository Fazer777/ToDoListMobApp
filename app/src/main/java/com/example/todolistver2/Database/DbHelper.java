package com.example.todolistver2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ToDoList.db";
    private static final int DATABASE_VERSION = 3;



    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableCategories.CREATE_TABLE_CATEGORIES);
        db.execSQL(TableNotes.CREATE_TABLE_NOTES);
        db.execSQL(TableTimerTasks.CREATE_TABLE_TIMER_TASKS);
        db.execSQL(TableTasks.CREATE_TABLE_TASKS);
        db.execSQL(TableCategories.INSERT_DEFAULT_CATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TableNotes.DELETE_TABLE_NOTES);
        db.execSQL(TableTimerTasks.DELETE_TABLE_TIMER_TASKS);
        db.execSQL(TableTasks.DELETE_TABLE_TASKS);
        db.execSQL(TableCategories.DELETE_TABLE_CATEGORIES);
        onCreate(db);
    }
}

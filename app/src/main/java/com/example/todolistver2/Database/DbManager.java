package com.example.todolistver2.Database;

import android.content.Context;

import com.example.todolistver2.Models.Note;

public class DbManager {
    private Context context;
    private DbHelper dbHelper;

    public DbManager(Context context){
        this.context = context;
        dbHelper = new DbHelper(this.context);
    }

    //-----------------------------------#TABLE NOTES#----------------------------------------------

    // Добавление заметки в базу данных
    public void addNoteDatabase(Note note){

    }

    // Обновление заметки в базе данных
    public void updateNoteDatabase(int id, Note note){

    }

    // Удаление заметки из базы данных
    public void deleteNoteDatabase(int id){

    }

    //-----------------------------------#TABLE TIMER_TASKS#----------------------------------------


    //-----------------------------------#TABLE TASKS#----------------------------------------------



}

package com.example.todolistver2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.Toast;

import com.example.todolistver2.Constants.Constants;
import com.example.todolistver2.Models.Note;
import com.example.todolistver2.Models.Category;
import com.example.todolistver2.Models.Task;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private Context context;
    private DbHelper dbHelper;

    public DbManager(Context context){
        this.context = context;
        dbHelper = new DbHelper(this.context);
    }

    //-----------------------------------#TABLE NOTES#----------------------------------------------

    // Addition new note in database
    public void addNoteDatabase(Note note){
        if (note != null){
            ContentValues cv = new ContentValues();
            cv.put(TableNotes.DESCRIPTION, note.getDescription());
            cv.put(TableNotes.CATEGORY_NAME, note.getCategory().getName());
            String hexColor = String.format("#%06X", (0xFFFFFF & note.getCategory().getColor()));
            cv.put(TableNotes.COLOR_CATEGORY, hexColor);
            cv.put(TableNotes.NOTE_DATETIME, Constants.convertLocalDateTimeToString(note.getLocalDateTime()));

            try{
                long result = dbHelper.getWritableDatabase().insert(TableNotes.TABLE_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception ex){
                Toast.makeText(context, "add_note_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                dbHelper.close();
            }
        }
        else {
            Toast.makeText(context, "Note is null", Toast.LENGTH_SHORT).show();
        }
    }

    // Update note in database
    public void updateNoteDatabase(int id, Note note){
        ContentValues cv = new ContentValues();
        cv.put(TableNotes.ID, id);
        cv.put(TableNotes.DESCRIPTION, note.getDescription());
        cv.put(TableNotes.CATEGORY_NAME, note.getCategory().getName());
        cv.put(TableNotes.NOTE_DATETIME, Constants.convertLocalDateTimeToString(note.getLocalDateTime()));
        String hexColor = String.format("#%06X", (0xFFFFFF & note.getCategory().getColor()));
        cv.put(TableNotes.COLOR_CATEGORY, hexColor);

        String selection = TableNotes.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        try{
            int count = dbHelper.getWritableDatabase().update(
                    TableNotes.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);

            if (count == -1){
                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(context, "update_note_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }

    }

    // Delete note from database
    public void deleteNoteDatabase(int id){
        String whereClause = TableNotes.ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };
        try{
            dbHelper.getWritableDatabase().delete(TableNotes.TABLE_NAME, whereClause, whereArgs);
        }
        catch (Exception ex){
            Toast.makeText(context, "delete_note_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }

    }

    public List<Note> getAllNotesDatabase(){
        List<Note> allNotes = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM " + TableNotes.TABLE_NAME;
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(selectAllQuery, null);
        while (cursor.moveToNext()){
            Note note = new Note();
            note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TableNotes.DESCRIPTION)));

            Category category = new Category();
            category.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableNotes.CATEGORY_NAME)));
            category.setColor(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableNotes.COLOR_CATEGORY))));
            note.setCategory(category);

            LocalDateTime dateTime = Constants.convertStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(TableNotes.NOTE_DATETIME)));
            note.setLocalDateTime(dateTime);
            allNotes.add(note);
        }
        cursor.close();
        dbHelper.close();

        return allNotes;
    }

    //-----------------------------------#TABLE TIMER_TASKS#----------------------------------------

    public void addTimerTaskDatabase(Task task){
        if (task != null){
            ContentValues cv = new ContentValues();
            cv.put(TableTimerTasks.TIMER_TASK_NAME, task.getName());
            cv.put(TableTimerTasks.TIMER_TASK_DATETIME, Constants.convertLocalDateTimeToString(task.getDateTime()));
            String hexColor = String.format("#%06X", (0xFFFFFF & task.getColorTask()));
            cv.put(TableTimerTasks.TIMER_TASK_COLOR, hexColor);
            cv.put(TableTimerTasks.TIMER_TASK_TIME, task.getTime().format(Constants.timeFormat_HH_mm_ss));

            try{
                long result = dbHelper.getWritableDatabase().insert(TableTimerTasks.TABLE_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception ex){
                Toast.makeText(context, "add_timer_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                dbHelper.close();
            }
        }
        else {
            Toast.makeText(context, "Timer task is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTimerTaskDatabase(int id, Task task){
        ContentValues cv = new ContentValues();
        cv.put(TableTimerTasks.TIMER_TASK_NAME, task.getName());
        cv.put(TableTimerTasks.TIMER_TASK_DATETIME, task.getDateTime().format(Constants.format_dd_MM_YYYY_HH_mm_ss));
        String hexColor = String.format("#%06X", (0xFFFFFF & task.getColorTask()));
        cv.put(TableTimerTasks.TIMER_TASK_COLOR, hexColor);

        String selection = TableTimerTasks.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        try{
            int count = dbHelper.getWritableDatabase().update(
                    TableTimerTasks.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);

            if (count == -1){
                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(context, "update_timer_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public void updateTimeTimerTaskDatabase(int id, LocalTime time){
        ContentValues cv = new ContentValues();
        cv.put(TableTimerTasks.TIMER_TASK_TIME, time.format(Constants.timeFormat_HH_mm_ss));

        String selection = TableNotes.ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        try{
            int count = dbHelper.getWritableDatabase().update(TableTimerTasks.TABLE_NAME, cv, selection, selectionArgs );

            if (count == -1){
                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(context, "update_timer_task_time_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public void deleteTimerTaskDatabase(int id){
        String whereClause = TableTimerTasks.ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };
        try{
            dbHelper.getWritableDatabase().delete(TableTimerTasks.TABLE_NAME, whereClause, whereArgs);
        }
        catch (Exception ex){
            Toast.makeText(context, "delete_timer_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    // TODO may be will write try-catch???
    public List<Task> getAllTimerTasksDatabase(){

        List<Task> allTasks = new ArrayList<>();

        String sortOrder =
                TableTimerTasks.TIMER_TASK_DATETIME + " DESC";

        Cursor cursor = dbHelper.getWritableDatabase().query(
                TableTimerTasks.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while (cursor.moveToNext()){
            Task timerTask = new Task();
            timerTask.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_NAME)));

            LocalDateTime dateTime = Constants.convertStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_DATETIME)));
            timerTask.setDateTime(dateTime);

            timerTask.setColorTask(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_COLOR))));
            timerTask.setTime(LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_TIME)), Constants.timeFormat_HH_mm_ss));

            allTasks.add(timerTask);
        }
        cursor.close();
        dbHelper.close();

        return allTasks;
    }


    //-----------------------------------#TABLE TASKS#----------------------------------------------



}

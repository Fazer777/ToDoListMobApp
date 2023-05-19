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
import com.example.todolistver2.Models.TimerTask;

import java.time.LocalDate;
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
            cv.put(TableNotes.ITEM_INDEX, note.getItemIndex());

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
    public void updateNoteDatabase(int itemIndex, Note note){
        ContentValues cv = new ContentValues();
        cv.put(TableNotes.DESCRIPTION, note.getDescription());
        cv.put(TableNotes.CATEGORY_NAME, note.getCategory().getName());
        cv.put(TableNotes.NOTE_DATETIME, Constants.convertLocalDateTimeToString(note.getLocalDateTime()));
        String hexColor = String.format("#%06X", (0xFFFFFF & note.getCategory().getColor()));
        cv.put(TableNotes.COLOR_CATEGORY, hexColor);

        String selection = TableNotes.ITEM_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

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
    public void deleteNoteDatabase(int position){
        String whereDelClause = TableNotes.ITEM_INDEX + "= ?";
        String[] whereArgs = { String.valueOf(position) };
        try{
            dbHelper.getWritableDatabase().delete(TableNotes.TABLE_NAME, whereDelClause, whereArgs);
            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableNotes.TABLE_NAME + " SET " + TableNotes.ITEM_INDEX + " = " +
                    TableNotes.ITEM_INDEX + " -1 " + " WHERE " + TableNotes.ITEM_INDEX + " > " + position + ";");

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
            note.setItemIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TableNotes.ITEM_INDEX))));
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

    //-----------------------------------#TABLE TIMER#----------------------------------------

    public void addTimerTaskDatabase(TimerTask timerTask){
        if (timerTask != null){
            ContentValues cv = new ContentValues();
            cv.put(TableTimerTasks.TIMER_TASK_NAME, timerTask.getName());
            cv.put(TableTimerTasks.TIMER_TASK_DATETIME, timerTask.getDate().format(Constants.format_dd_MM_YYYY));
            String hexColor = String.format("#%06X", (0xFFFFFF & timerTask.getColorTask()));
            cv.put(TableTimerTasks.TIMER_TASK_COLOR, hexColor);
            cv.put(TableTimerTasks.TIMER_TASK_TIME, timerTask.getTime().format(Constants.timeFormat_HH_mm_ss));
            cv.put(TableTimerTasks.TIMER_TASK_ITEM_INDEX, timerTask.getItemIndex());

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

    public void updateTimerTaskDatabase(int itemIndex, TimerTask timerTask){
        ContentValues cv = new ContentValues();
        cv.put(TableTimerTasks.TIMER_TASK_NAME, timerTask.getName());
        cv.put(TableTimerTasks.TIMER_TASK_DATETIME, timerTask.getDate().format(Constants.format_dd_MM_YYYY));
        String hexColor = String.format("#%06X", (0xFFFFFF & timerTask.getColorTask()));
        cv.put(TableTimerTasks.TIMER_TASK_COLOR, hexColor);

        String selection = TableTimerTasks.TIMER_TASK_ITEM_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

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

    public void updateTimeTimerTaskDatabase(int itemIndex, LocalTime time){
        ContentValues cv = new ContentValues();
        cv.put(TableTimerTasks.TIMER_TASK_TIME, time.format(Constants.timeFormat_HH_mm_ss));

        String selection = TableNotes.ITEM_INDEX + "= ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

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

    public void deleteTimerTaskDatabase(int itemIndex){
        String whereClause = TableTimerTasks.TIMER_TASK_ITEM_INDEX + "= ?";
        String[] whereArgs = { String.valueOf(itemIndex) };
        try{
            dbHelper.getWritableDatabase().delete(TableTimerTasks.TABLE_NAME, whereClause, whereArgs);
            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableTimerTasks.TABLE_NAME + " SET " + TableTimerTasks.TIMER_TASK_ITEM_INDEX + " = " +
                    TableTimerTasks.TIMER_TASK_ITEM_INDEX + " -1 " + " WHERE " + TableTimerTasks.TIMER_TASK_ITEM_INDEX + " > " + itemIndex + ";");
        }
        catch (Exception ex){
            Toast.makeText(context, "delete_timer_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public List<TimerTask> getAllTimerTasksDatabase(){

        List<TimerTask> allTimerTasks = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().query(
                TableTimerTasks.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order
        );

        while (cursor.moveToNext()){
            TimerTask timerTask = new TimerTask();
            timerTask.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_NAME)));

            LocalDate date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_DATETIME)), Constants.format_dd_MM_YYYY);
            timerTask.setDate(date);

            timerTask.setColorTask(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_COLOR))));
            timerTask.setTime(LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_TIME)), Constants.timeFormat_HH_mm_ss));
            timerTask.setItemIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_ITEM_INDEX))));

            allTimerTasks.add(timerTask);
        }
        cursor.close();
        dbHelper.close();

        return allTimerTasks;
    }

    public List<TimerTask> getFilteredTimerTasksByDate(String strDate){
        List<TimerTask> filteredTasks = new ArrayList<>();

        String selection = TableTimerTasks.TIMER_TASK_DATETIME + " = ?";
        String[] selectionArgs = { strDate };
        Cursor cursor = dbHelper.getReadableDatabase().query(
                TableTimerTasks.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            TimerTask timerTask = new TimerTask();
            timerTask.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_NAME)));
            timerTask.setColorTask(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_COLOR))));
            timerTask.setItemIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_ITEM_INDEX))));
            timerTask.setTime(LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_TIME)), Constants.timeFormat_HH_mm_ss));
            timerTask.setDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIMER_TASK_DATETIME)), Constants.format_dd_MM_YYYY));
            filteredTasks.add(timerTask);
        }

        cursor.close();
        dbHelper.close();
        return filteredTasks;
    }


    //-----------------------------------#TABLE TASKS#----------------------------------------------
    public void addTaskDatabase(Task task){
        if (task != null){
            ContentValues cv = new ContentValues();
            cv.put(TableTasks.TASK_NAME, task.getName());
            cv.put(TableTasks.TASK_DESCRIPTION, task.getDescription());
            cv.put(TableTasks.TASK_DATE, task.getDate().format(Constants.format_dd_MM_YYYY));
            cv.put(TableTasks.TASK_COMPLETED, booleanToInt(task.getCompleted()));
            String hexColor = String.format("#%06X", (0xFFFFFF & task.getColor()));
            cv.put(TableTasks.TASK_COLOR, hexColor);
            cv.put(TableTasks.TASK_ITEM_INDEX, task.getItemIndex());
            if (task.getDateComplete() != null){
                cv.put(TableTasks.TASK_DATE_COMPLETE, task.getDateComplete().format(Constants.format_dd_MM_YYYY));
            }


            try{
                long result = dbHelper.getWritableDatabase().insert(TableTasks.TABLE_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception ex){
                Toast.makeText(context, "add_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                dbHelper.close();
            }
        }
        else{
            Toast.makeText(context, "task is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTaskDataBase(int itemIndex, Task task){
        ContentValues cv = new ContentValues();
        cv.put(TableTasks.TASK_NAME, task.getName());
        cv.put(TableTasks.TASK_DESCRIPTION, task.getDescription());
        cv.put(TableTasks.TASK_DATE, task.getDate().format(Constants.format_dd_MM_YYYY));
        cv.put(TableTasks.TASK_COMPLETED, booleanToInt(task.getCompleted()));
        String hexColor = String.format("#%06X", (0xFFFFFF & task.getColor()));
        cv.put(TableTasks.TASK_COLOR, hexColor);
        if (task.getDateComplete() != null){
            cv.put(TableTasks.TASK_DATE_COMPLETE, task.getDateComplete().format(Constants.format_dd_MM_YYYY));
        }


        String selection = TableTasks.TASK_ITEM_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

        try{
            int count = dbHelper.getWritableDatabase().update(
                    TableTasks.TABLE_NAME,
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
            Toast.makeText(context, "update_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public void deleteTaskDatabase(int itemIndex){
        String whereClause = TableTasks.TASK_ITEM_INDEX + "= ?";
        String[] whereArgs = { String.valueOf(itemIndex) };
        try{
            dbHelper.getWritableDatabase().delete(TableTasks.TABLE_NAME, whereClause, whereArgs);
            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableTasks.TABLE_NAME + " SET " + TableTasks.TASK_ITEM_INDEX + " = " +
                    TableTasks.TASK_ITEM_INDEX + " -1 " + " WHERE " + TableTasks.TASK_ITEM_INDEX + " > " + itemIndex + ";");
        }
        catch (Exception ex){
            Toast.makeText(context, "delete_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public List<Task> getAllTasksDatabase(){
        List<Task> allTasks = new ArrayList<>();

        Cursor cursor = dbHelper.getReadableDatabase().query(
                TableTasks.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            Task task = new Task();
            task.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.TASK_NAME)));
            task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.TASK_DESCRIPTION)));
            task.setDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.TASK_DATE)), Constants.format_dd_MM_YYYY));
            task.setColor(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.TASK_COLOR))));
            int i = cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.TASK_COMPLETED));
            task.setCompleted(intToBoolean(i));
            task.setItemIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.TASK_ITEM_INDEX))));
            String strDate = cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.TASK_DATE_COMPLETE));
            if (strDate != null){
                task.setDateComplete(LocalDate.parse(strDate, Constants.format_dd_MM_YYYY));
            }

            allTasks.add(task);
        }

        cursor.close();
        dbHelper.close();
        return  allTasks;
    }

    // Other functions

    private Boolean intToBoolean(int num){
        return num > 0;
    }

    private int booleanToInt(Boolean bool){
        if (bool){
            return 1;
        }
        else {
            return 0;
        }
    }
}

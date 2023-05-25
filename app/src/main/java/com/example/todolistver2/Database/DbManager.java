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

    public void addNoteDatabase(Note note){
        if (note != null){
            String sqlQuery = "SELECT Id "
                            + "FROM Categories "
                            + "WHERE Name = ?";
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlQuery, new String[]{note.getCategory().getName()} );
            cursor.moveToFirst();
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.ID));
            cursor.close();
            dbHelper.close();

            ContentValues cv = new ContentValues();
            cv.put(TableNotes.DESCRIPTION, note.getDescription());
            cv.put(TableNotes.DATE_TIME, Constants.convertLocalDateTimeToString(note.getLocalDateTime()));
            cv.put(TableNotes.ITEM_INDEX, note.getItemIndex());
            cv.put(TableNotes.CATEGORY_ID, categoryId);
            try{
                long result = dbHelper.getWritableDatabase().insert(TableNotes.TABLE_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "Неудачное добавление", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Успешно добавлено", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception ex){
                Toast.makeText(context, "add_note_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                dbHelper.close();
            }
        }
        else{
            Toast.makeText(context, "Неудачное добавление", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNoteDatabase(int itemIndex, Note note){
        String sqlQuery =
                "SELECT Id "
                        + "FROM Categories "
                        + "WHERE Name = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlQuery, new String[]{note.getCategory().getName()} );
        cursor.moveToFirst();
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.ID));
        cursor.close();
        dbHelper.close();

        ContentValues cv = new ContentValues();
        cv.put(TableNotes.DESCRIPTION, note.getDescription());
        cv.put(TableNotes.DATE_TIME, Constants.convertLocalDateTimeToString(note.getLocalDateTime()));
        cv.put(TableNotes.CATEGORY_ID, categoryId);
        String selection = TableNotes.ITEM_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

        try{
            int count = dbHelper.getWritableDatabase().update(
                    TableNotes.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);

            if (count == -1){
                Toast.makeText(context, "Неудачное изменение", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Успешно изменено", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(context, "update_note_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public void deleteNoteDatabase(int itemIndex){
        String whereDelClause = TableNotes.ITEM_INDEX + "= ?";
        String[] whereArgs = { String.valueOf(itemIndex) };
        try{
            dbHelper.getWritableDatabase().delete(TableNotes.TABLE_NAME, whereDelClause, whereArgs);
            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableNotes.TABLE_NAME + " SET " + TableNotes.ITEM_INDEX + " = " +
                    TableNotes.ITEM_INDEX + " -1 " + " WHERE " + TableNotes.ITEM_INDEX + " > " + itemIndex + ";");

        }
        catch (Exception ex){
            Toast.makeText(context, "delete_note_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }

    }

    public List<Note> getAllNotesDatabase() {
        List<Note> allNotes = new ArrayList<>();
    String sqlQuery = "SELECT * "
                    + "FROM Notes, Categories "
                    + "WHERE Notes.CategoryId = Categories.Id";
        final String selectAll = "SELECT * FROM Notes";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlQuery, null);
        while (cursor.moveToNext()) {
            Note note = new Note();
            Category category = new Category();
            note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TableNotes.DESCRIPTION)));
            note.setItemIndex(cursor.getInt(cursor.getColumnIndexOrThrow(TableNotes.ITEM_INDEX)));
            note.setLocalDateTime(Constants.convertStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(TableNotes.DATE_TIME))));
            category.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableCategories.NAME)));
            category.setColor(cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.COLOR)));
            note.setCategory(category);
            allNotes.add(note);
        }
        cursor.close();
        dbHelper.close();
        return allNotes;
    }

    public List<Note> getFilteredNotesByCategory(String nameCategory){
        List<Note> filteredList = new ArrayList<>();
        String sqlQuery = "SELECT * "
                        + "FROM Categories "
                        + "WHERE Name = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlQuery, new String[]{nameCategory} );
        cursor.moveToFirst();
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.ID));
        Category tmpCategory = new Category();
        tmpCategory.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableCategories.NAME)));
        tmpCategory.setColor(cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.COLOR)));
        cursor.close();
        dbHelper.close();

        String whereClause = TableNotes.CATEGORY_ID + "= ?";
        String[] whereArgs = { String.valueOf(categoryId) };

        Cursor cursor1 = dbHelper.getReadableDatabase().query(
                TableNotes.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        while (cursor1.moveToNext()) {
            Note note = new Note();
            note.setDescription(cursor1.getString(cursor1.getColumnIndexOrThrow(TableNotes.DESCRIPTION)));
            note.setItemIndex(cursor1.getInt(cursor1.getColumnIndexOrThrow(TableNotes.ITEM_INDEX)));
            note.setLocalDateTime(Constants.convertStringToLocalDate(cursor1.getString(cursor1.getColumnIndexOrThrow(TableNotes.DATE_TIME))));
            note.setCategory(tmpCategory);
            filteredList.add(note);
        }
        cursor1.close();
        dbHelper.close();
        return filteredList;
    }

    //-----------------------------------#TABLE TIMER#----------------------------------------
    public void addTimerTaskDatabase(TimerTask timerTask){
        if (timerTask != null){
            ContentValues cv = new ContentValues();
            cv.put(TableTimerTasks.NAME, timerTask.getName());
            cv.put(TableTimerTasks.DATE_TIME, timerTask.getDate().format(Constants.format_dd_MM_YYYY));
            String hexColor = String.format("#%06X", (0xFFFFFF & timerTask.getColorTask()));
            cv.put(TableTimerTasks.COLOR, hexColor);
            cv.put(TableTimerTasks.TIME, timerTask.getTime().format(Constants.timeFormat_HH_mm_ss));
            cv.put(TableTimerTasks.ITEM_INDEX, timerTask.getItemIndex());

            try{
                long result = dbHelper.getWritableDatabase().insert(TableTimerTasks.TABLE_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "Неудачное добавление", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Успешно добавлено", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Неудачное добавление", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTimerTaskDatabase(int itemIndex, TimerTask timerTask){
        ContentValues cv = new ContentValues();
        cv.put(TableTimerTasks.NAME, timerTask.getName());
        cv.put(TableTimerTasks.DATE_TIME, timerTask.getDate().format(Constants.format_dd_MM_YYYY));
        String hexColor = String.format("#%06X", (0xFFFFFF & timerTask.getColorTask()));
        cv.put(TableTimerTasks.COLOR, hexColor);

        String selection = TableTimerTasks.ITEM_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

        try{
            int count = dbHelper.getWritableDatabase().update(
                    TableTimerTasks.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);

            if (count == -1){
                Toast.makeText(context, "Неудачное изменение", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Успешно изменено", Toast.LENGTH_SHORT).show();
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
        cv.put(TableTimerTasks.TIME, time.format(Constants.timeFormat_HH_mm_ss));

        String selection = TableTimerTasks.ITEM_INDEX + "= ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

        try{
            int count = dbHelper.getWritableDatabase().update(TableTimerTasks.TABLE_NAME, cv, selection, selectionArgs );

            if (count == -1){
                Toast.makeText(context, "Неудачное изменение", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Успешно изменено", Toast.LENGTH_SHORT).show();
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
        String whereClause = TableTimerTasks.ITEM_INDEX + "= ?";
        String[] whereArgs = { String.valueOf(itemIndex) };
        try{
            dbHelper.getWritableDatabase().delete(TableTimerTasks.TABLE_NAME, whereClause, whereArgs);
            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableTimerTasks.TABLE_NAME + " SET " + TableTimerTasks.ITEM_INDEX + " = " +
                    TableTimerTasks.ITEM_INDEX + " -1 " + " WHERE " + TableTimerTasks.ITEM_INDEX + " > " + itemIndex + ";");
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
            timerTask.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.NAME)));

            LocalDate date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.DATE_TIME)), Constants.format_dd_MM_YYYY);
            timerTask.setDate(date);

            timerTask.setColorTask(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.COLOR))));
            timerTask.setTime(LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIME)), Constants.timeFormat_HH_mm_ss));
            timerTask.setItemIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.ITEM_INDEX))));

            allTimerTasks.add(timerTask);
        }
        cursor.close();
        dbHelper.close();

        return allTimerTasks;
    }

    public List<TimerTask> getFilteredTimerTasksByDate(String strDate){
        List<TimerTask> filteredTasks = new ArrayList<>();

        String selection = TableTimerTasks.DATE_TIME + " = ?";
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
            timerTask.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.NAME)));
            timerTask.setColorTask(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.COLOR))));
            timerTask.setItemIndex(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.ITEM_INDEX))));
            timerTask.setTime(LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.TIME)), Constants.timeFormat_HH_mm_ss));
            timerTask.setDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTimerTasks.DATE_TIME)), Constants.format_dd_MM_YYYY));
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
            cv.put(TableTasks.NAME, task.getName());
            cv.put(TableTasks.DESCRIPTION, task.getDescription());
            cv.put(TableTasks.DATE, task.getDate().format(Constants.format_dd_MM_YYYY));
            cv.put(TableTasks.IS_COMPLETED, booleanToInt(task.getCompleted()));
            String hexColor = String.format("#%06X", (0xFFFFFF & task.getColor()));
            cv.put(TableTasks.COLOR, hexColor);
            cv.put(TableTasks.ITEM_INDEX, task.getItemIndex());
            if (task.getDateComplete() != null){
                cv.put(TableTasks.COMPLETION_DATE, task.getDateComplete().format(Constants.format_dd_MM_YYYY));
            }

            try{
                long result = dbHelper.getWritableDatabase().insert(TableTasks.TABLE_NAME, null, cv);
                if (result == -1) {
                    Toast.makeText(context, "Неудачное добавление", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Успешно добавлено", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Неудачное добавление", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTaskDataBase(int itemIndex, Task task){
        ContentValues cv = new ContentValues();
        cv.put(TableTasks.NAME, task.getName());
        cv.put(TableTasks.DESCRIPTION, task.getDescription());
        cv.put(TableTasks.DATE, task.getDate().format(Constants.format_dd_MM_YYYY));
        cv.put(TableTasks.IS_COMPLETED, booleanToInt(task.getCompleted()));
        String hexColor = String.format("#%06X", (0xFFFFFF & task.getColor()));
        cv.put(TableTasks.COLOR, hexColor);
        if (task.getDateComplete() != null){
            cv.put(TableTasks.COMPLETION_DATE, task.getDateComplete().format(Constants.format_dd_MM_YYYY));
        }


        String selection = TableTasks.ITEM_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

        try{
            int count = dbHelper.getWritableDatabase().update(
                    TableTasks.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);

            if (count == -1){
                Toast.makeText(context, "Неудачное изменение", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Успешно изменено", Toast.LENGTH_SHORT).show();
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
        String whereClause = TableTasks.ITEM_INDEX + "= ?";
        String[] whereArgs = { String.valueOf(itemIndex) };
        try{
            dbHelper.getWritableDatabase().delete(TableTasks.TABLE_NAME, whereClause, whereArgs);
            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableTasks.TABLE_NAME + " SET " + TableTasks.ITEM_INDEX + " = " +
                    TableTasks.ITEM_INDEX + " -1 " + " WHERE " + TableTasks.ITEM_INDEX + " > " + itemIndex + ";");
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
            task.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.NAME)));
            task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.DESCRIPTION)));
            task.setDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.DATE)), Constants.format_dd_MM_YYYY));
            task.setColor(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.COLOR))));
            int i = cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.IS_COMPLETED));
            task.setCompleted(intToBoolean(i));
            task.setItemIndex(cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.ITEM_INDEX)));
            String strDate = cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.COMPLETION_DATE));
            if (strDate != null){
                task.setDateComplete(LocalDate.parse(strDate, Constants.format_dd_MM_YYYY));
            }

            allTasks.add(task);
        }

        cursor.close();
        dbHelper.close();
        return  allTasks;
    }

    public List<Task> getFilteredTasksByDate(String date){
        List<Task> filteredTasks = new ArrayList<>();

        String selection = TableTasks.DATE + " = ?";
        String[] selectionArgs = { date };
        Cursor cursor = dbHelper.getReadableDatabase().query(
                TableTasks.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            Task task = new Task();
            task.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.NAME)));
            task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.DESCRIPTION)));
            task.setDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.DATE)), Constants.format_dd_MM_YYYY));
            task.setColor(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.COLOR))));
            int i = cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.IS_COMPLETED));
            task.setCompleted(intToBoolean(i));
            task.setItemIndex(cursor.getInt(cursor.getColumnIndexOrThrow(TableTasks.ITEM_INDEX)));
            String strDate = cursor.getString(cursor.getColumnIndexOrThrow(TableTasks.COMPLETION_DATE));
            if (strDate != null){
                task.setDateComplete(LocalDate.parse(strDate, Constants.format_dd_MM_YYYY));
            }
            filteredTasks.add(task);
        }

        cursor.close();
        dbHelper.close();
        return filteredTasks;
    }

    //----------------------------------#TABLE CATEGORIES#------------------------------------------

    public void addCategoryDatabase(Category category){
        ContentValues cv = new ContentValues();
        cv.put(TableCategories.NAME, category.getName());
        cv.put(TableCategories.COLOR, category.getColor());
        cv.put(TableCategories.ITEM_INDEX, category.getItemIndex());

        try{
            long result = dbHelper.getWritableDatabase().insert(TableCategories.TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Неудачное добавление", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Успешно добавлено", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(context, "add_category_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public void updateCategoryDatabase(int itemIndex, Category category){

        ContentValues cv = new ContentValues();
        cv.put(TableCategories.NAME, category.getName());
        cv.put(TableCategories.COLOR, category.getColor());

        String selection = TableCategories.ITEM_INDEX + " = ?";
        String[] selectionArgs = { String.valueOf(itemIndex) };

        try{
            int count = dbHelper.getWritableDatabase().update(
                    TableCategories.TABLE_NAME,
                    cv,
                    selection,
                    selectionArgs);

            if (count == -1){
                Toast.makeText(context, "Неудачное изменение", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Успешно изменено", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(context, "update_task_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public void deleteCategoryDatabase(int itemIndex){

        String sqlQuery = "SELECT Id "
                        + "FROM Categories "
                        + "WHERE ItemIndex = ?";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlQuery, new String[]{String.valueOf(itemIndex)} );
        cursor.moveToFirst();
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.ID));
        cursor.close();
        dbHelper.close();

        String whereClause = TableCategories.ITEM_INDEX + "= ?";
        String[] whereArgs = { String.valueOf(itemIndex) };
        try{

            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableNotes.TABLE_NAME + " SET " + TableNotes.CATEGORY_ID + " = " +
                    " 1 " + " WHERE " + TableNotes.CATEGORY_ID + " = " + categoryId + ";");

            dbHelper.getWritableDatabase().delete(TableCategories.TABLE_NAME, whereClause, whereArgs);
            dbHelper.getWritableDatabase().execSQL("UPDATE " + TableCategories.TABLE_NAME + " SET " + TableCategories.ITEM_INDEX + " = " +
                    TableCategories.ITEM_INDEX + " -1 " + " WHERE " + TableCategories.ITEM_INDEX + " > " + itemIndex + ";");


        }
        catch (Exception ex){
            Toast.makeText(context, "delete_category_database: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            dbHelper.close();
        }
    }

    public List<Category> getAllCategoriesDatabase() {
        List<Category> allCategories = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().query(
                TableCategories.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()){
            Category category = new Category();
            category.setName(cursor.getString(cursor.getColumnIndexOrThrow(TableCategories.NAME)));
            category.setColor(cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.COLOR)));
            category.setItemIndex(cursor.getInt(cursor.getColumnIndexOrThrow(TableCategories.ITEM_INDEX)));
            allCategories.add(category);
        }
        cursor.close();
        dbHelper.close();
        return allCategories;
    }

    //----------------------------------#OTHER FUNCTIONS#------------------------------------------

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

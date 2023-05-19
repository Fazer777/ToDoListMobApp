package com.example.todolistver2.Constants;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {

    public static final DateTimeFormatter format_dd_MM_YYYY_HH_mm_ss = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    //public static final DateTimeFormatter format_dd_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter format_dd_MM_YYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter timeFormat_HH_mm_ss = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final int NOTE_CREATE_ACTION = 121;
    public static final int NOTE_EDIT_ACTION = 122;

    public static final int TIMER_TASK_CREATE_ACTION = 123;
    public static final int TIMER_TASK_UPDATE_ACTION = 124;

    public static final int INSERT_POSITION =0;
    public static final int INTENT_DEFAULT_VALUE = -1;
    public static final String INTENT_INDEX_KEY = "INDEX";
    public static final String INTENT_UPDATE_NOTE_KEY = "UPDATE_NOTE";
    public static final String INTENT_CREATE_NOTE_KEY = "CREATE_NOTE";
    public static final String INTENT_CREATE_TIMER_TASK_KEY = "CREATE_TASK";
    public static final String INTENT_UPDATE_TIMER_TASK_KEY = "UPDATE_TASK";
    public static final String SHARED_PREF_KEY_NOTE = "COUNT_NOTE";
    public static final String SHARED_PREF_KEY_TIMER= "COUNT_TIMER";
    public static final String SHARED_PREF_KEY_TASK = "COUNT_TASK";


    public static String convertLocalDateTimeToString(LocalDateTime localDatetime){
        String dateTimeString="";
        try{
            dateTimeString = localDatetime.format(Constants.format_dd_MM_YYYY_HH_mm_ss);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Log.d("LDTtoStr", ex.getMessage());
            dateTimeString="";
        }
        return dateTimeString;
    }

    public static LocalDateTime convertStringToLocalDate(String strDateTime){
        LocalDateTime localDateTime = null;
        try{
            localDateTime = LocalDateTime.parse(strDateTime, Constants.format_dd_MM_YYYY_HH_mm_ss);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Log.d("StrToLDT", ex.getMessage());

        }
        return localDateTime;
    }
}

package com.example.todolistver2.Constants;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final DateTimeFormatter format_dd_MM_YYYY_HH_mm_ss = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final DateTimeFormatter format_dd_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter timeFormat_HH_mm_ss = DateTimeFormatter.ofPattern("HH:mm:ss");
    //public static final String BUNDLE_NOTE_LIST_KEY = "NOTES";
    public static final int NOTE_CREATE_ACTION = 121;
    public static final int NOTE_EDIT_ACTION = 122;

    public static final int INSERT_POSITION =0;
    public static final int INTENT_DEFAULT_VALUE = -1;
    public static final String INTENT_INDEX_KEY = "INDEX";
    public static final String INTENT_UPDATE_NOTE_KEY = "UPDATE_NOTE";
    public static final String INTENT_CREATE_NOTE_KEY = "CREATE_NOTE";
}

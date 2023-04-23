package com.example.todolistver2.Constants;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final DateTimeFormatter format_dd_MM_YYYY_HH_mm_ss = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final DateTimeFormatter format_dd_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter timeFormat_HH_mm_ss = DateTimeFormatter.ofPattern("HH:mm:ss");

}

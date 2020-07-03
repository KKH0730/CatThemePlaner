package com.planer.catthemeplaner.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy년 MM월 dd일 | a hh시 mm분");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy년 M월 dd일");
    public static SimpleDateFormat dateFormat3 = new SimpleDateFormat("a h시 m분");
    public static SimpleDateFormat dateFormat4 = new SimpleDateFormat("MM월 dd일");
    public static SimpleDateFormat dateFormat5 = new SimpleDateFormat("yyyyMMdd");


    public static SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM");
    public static SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd");
    public static SimpleDateFormat dateFormatHour = new SimpleDateFormat("kk");
    public static SimpleDateFormat dateFormatMinute = new SimpleDateFormat("mm");
    public static SimpleDateFormat dateFormatSecond = new SimpleDateFormat("ss");



    public final static String CALENDAR_HEADER_FORMAT = "yyyy-MM";
    public final static String FULL_DATE_FORMAT = "yyyy년 M월 dd일";
    public final static String YEAR_FORMAT = "yyyy";
    public final static String MONTH_FORMAT = "MM";
    public final static String DAY_FORMAT = "dd";
    public final static String HOUR_FORMAT = "HH";
    public final static String MIN_FORMAT = "mm";
    public final static String SEC_FORMAT = "ss";
    public final static String DATE_FORMAT = "yyyy 년 MM 월 dd 일 HH 시 mm 분 ss 초";

    public static String getDateFormat(long date, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        } catch (Exception e) {
            return " ";
        }
    }


    public static String getDate(long date, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date d = new Date(date);
            return formatter.format(d).toUpperCase();
        } catch (Exception e) {
            return " ";
        }
    }
}

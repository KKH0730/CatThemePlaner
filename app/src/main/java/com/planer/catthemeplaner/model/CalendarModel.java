package com.planer.catthemeplaner.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.planer.catthemeplaner.gson.BodyItem;
import com.planer.catthemeplaner.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarModel extends ViewModel {
    private static final String TAG = "CalendarModel";
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public CalendarModel(Context context) {
        this.context = context;

        pref = context.getSharedPreferences("CalendarData", context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveInfo(String memo, String position, String startDate, String endDate, String startTime, String endTime, int alarm, int importance, String description) {
        String sql = "insert into " + CalendarDatabase.TABLE_CALENDAR +
                " (MEMO, POSITION, STARTDATE, ENDDATE, STARTTIME, ENDTIME, ALARM, IMPORTANCE, DESCRIPTION) values (" +
                "'" + memo + "', " +
                "'" + position + "', " +
                "'" + startDate + "', " +
                "'" + endDate + "', " +
                "'" + startTime + "', " +
                "'" + endTime + "', " +
                "'" + alarm + "', " +
                "'" + importance + "', " +
                "'" + description + "')";

        CalendarDatabase database = CalendarDatabase.getInstance(context);
        database.execSQL(sql);

        editor.remove("startDate").commit();
        editor.remove("endDate").commit();
        editor.remove("startTime").commit();
        editor.remove("endTime").commit();
        editor.remove("importance").commit();
        editor.remove("alarm").commit();
        editor.remove("position").commit();
        editor.remove("description").commit();
    }

    public void saveHoliday(String holidayDate, String dateName) {
        Date date = null;

        try {
            date = DateUtil.dateFormat5.parse(holidayDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        String holidayD = DateUtil.dateFormat2.format(date);

        String sql = "insert into " + CalendarDatabase.TABLE_HOLIDAY_LIST +
                " (HOLIDAY_DATE, HOLIDAY_NAME) values (" +
                "'" + holidayD + "', " +
                "'" + dateName + "')";

        CalendarDatabase database = CalendarDatabase.getInstance(context);
        database.execSQL(sql);

    }

    public void updateInfo(String memo, String position, String startDate, String endDate, String startTime, String endTime, int alarm, int importance, String description, MemoListItem memoListItem) {
        String sql = "update " + CalendarDatabase.TABLE_CALENDAR +
                " set " +
                " MEMO = '" + memo + "'" +
                " ,POSITION = '" + position + "'" +
                " ,STARTDATE = '" + startDate + "'" +
                " ,ENDDATE = '" + endDate + "'" +
                " ,STARTTIME = '" + startTime + "'" +
                " ,ENDTIME = '" + endTime + "'" +
                " ,ALARM = '" + alarm + "'" +
                " ,IMPORTANCE = '" + importance + "'" +
                " ,DESCRIPTION = '" + description + "'" +
                " where " + " _id = " + memoListItem.get_id();

        CalendarDatabase database = CalendarDatabase.getInstance(context);
        database.execSQL(sql);

        Log.d(TAG, "UPDATE 실행됨.");
    }


    public void deleteInfo(MemoListItem memoListItem) {
        String sql = "delete from " + CalendarDatabase.TABLE_CALENDAR +
                " where " +
                " _id = " + memoListItem.get_id();

        CalendarDatabase database = CalendarDatabase.getInstance(context);
        database.execSQL(sql);
    }

    public  ArrayList<Holiday> loadHoliday() {
        String sql = "select * from " +  CalendarDatabase.TABLE_HOLIDAY_LIST + " order by  CREATE_DATE ASC";

        ArrayList<Holiday> items = new ArrayList<>();
        int recordCount = -1;
        CalendarDatabase database = CalendarDatabase.getInstance(context);
        if (database != null) {
            Cursor cursor = database.rawQuery(sql);
            recordCount = cursor.getCount();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();
                int _id = cursor.getInt(0);
                String holidayDate = cursor.getString(1);
                String holidayName = cursor.getString(2);


                items.add(new Holiday(_id, holidayDate, holidayName));
            }

            cursor.close();
        }
        return items;
    }
}

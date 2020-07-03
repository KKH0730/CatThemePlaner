package com.planer.catthemeplaner.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.planer.catthemeplaner.util.DateUtil;

import java.text.ParseException;
import java.util.Calendar;

public class NotificationService extends JobService {
    private static final String TAG = "NotificationService";
    private static AlarmManager alarmManager;
    private static PendingIntent pendingIntent;

    @Override
    public boolean onStartJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        Log.d(TAG, "NotificationService onStartJob() 실행됨.");


        Bundle bundle = job.getExtras();

        String startYear = null;
        String startMonth = null;
        String startDay = null;
        String startHour = null;
        String startMinute = null;
        String startSecond = null;

        try {
            startYear = DateUtil.dateFormatYear.format(DateUtil.dateFormat2.parse(bundle.getString("startDate")));
            startMonth = DateUtil.dateFormatMonth.format(DateUtil.dateFormat2.parse((bundle.getString("startDate"))));
            startDay = DateUtil.dateFormatDay.format(DateUtil.dateFormat2.parse(bundle.getString("startDate")));

            startHour = DateUtil.dateFormatHour.format(DateUtil.dateFormat3.parse(bundle.getString("startTime")));
            startMinute = DateUtil.dateFormatMinute.format(DateUtil.dateFormat3.parse(bundle.getString("startTime")));
            startSecond = DateUtil.dateFormatSecond.format(DateUtil.dateFormat3.parse(bundle.getString("startTime")));
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        int alarm = bundle.getInt("alarm");

        int addMonth = 0;
        int addHour = 0;
        int addDay = 0;

        if (alarm == 1) {
            addMonth = 10;
        } else if (alarm == 2) {
            addMonth = 30;
        } else if (alarm == 3) {
            addHour = 1;
        } else if (alarm == 4) {
            addDay = 1;
        }



        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, Integer.parseInt(startYear));
        mCalendar.set(Calendar.MONTH, (Integer.parseInt(startMonth) - 1));
        mCalendar.set(Calendar.DATE, (Integer.parseInt(startDay) - addDay));

        mCalendar.set(Calendar.HOUR_OF_DAY, (Integer.parseInt(startHour) - addHour));
        mCalendar.set(Calendar.MINUTE, (Integer.parseInt(startMinute) - addMonth));
        mCalendar.set(Calendar.SECOND, Integer.parseInt(startSecond));

        int pendingCode = Integer.parseInt("20" + startMonth + startDay + startHour + startMinute);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm", alarm);
        intent.putExtra("memo", bundle.getString("memo"));
        intent.putExtra("position", bundle.getString("position"));
        intent.putExtra("startDate", bundle.getString("startDate"));
        intent.putExtra("startDate", bundle.getString("startTime"));

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), pendingCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.d(TAG, "pendingCode : " + pendingCode);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
        }

         */

        return false;
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        return false;
    }

    public static void cancelAlarmManager(Context context, int pendingCode) {
        if (pendingIntent != null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, pendingCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            alarmManager = null;
            pendingIntent = null;
        }
    }


}

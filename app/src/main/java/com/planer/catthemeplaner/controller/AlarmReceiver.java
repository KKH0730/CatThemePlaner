package com.planer.catthemeplaner.controller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.planer.catthemeplaner.MainActivity;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.util.UtilCode;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "AlarmReceiver onReceive() 실행됨.");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(UtilCode.CHANNEL_ID) == null) {
                manager.createNotificationChannel(new NotificationChannel(
                        UtilCode.CHANNEL_ID, UtilCode.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                ));
                builder = new NotificationCompat.Builder(context, UtilCode.CHANNEL_ID);
            } else {
                builder = new NotificationCompat.Builder(context, UtilCode.CHANNEL_ID);
            }
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Log.d(TAG, "인텐트 알람값 : " + intent.getIntExtra("alarm", -100));

        String alarmDate = "";
        if (intent.getIntExtra("alarm", -1) == 1) {
            alarmDate = "10분전";
        } else if (intent.getIntExtra("alarm", -1) == 2) {
            alarmDate = "30분전";
        } else if (intent.getIntExtra("alarm", -1) == 2) {
            alarmDate = "한시간전";
        } else if (intent.getIntExtra("alarm", -1) == 2) {
            alarmDate = "하루전)";
        } else if (intent.getIntExtra("alarm", -1) == -1) {
            alarmDate = "";
        }


        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, UtilCode.BROADCAST_REQUEST, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String position = intent.getStringExtra("position");

        if(position.equals("null")){
            position = "미정";
        }

        if (builder != null) {
            builder.setContentTitle("일정 알림 (일시 : " + intent.getStringExtra("startDate") + ",  장소 : " + position + ")");
            builder.setContentText(intent.getStringExtra("memo"));
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            builder.setSmallIcon(R.drawable.cat_face);
            Notification noti = builder.build();

            manager.notify(1, noti);

        } else {
            Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
        }

    }

}


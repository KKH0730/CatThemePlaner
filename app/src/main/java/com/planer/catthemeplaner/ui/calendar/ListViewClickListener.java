package com.planer.catthemeplaner.ui.calendar;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.util.DateUtil;

import java.util.Date;
import java.util.Calendar;


public class ListViewClickListener implements View.OnClickListener {
    private final static String TAG = "ListViewClickListener";
    private int mYear, mMonth, mDay;
    private int hour, min;
    private Context context;
    private String menuName;
    private TextView dateTextView;
    private ImageView iconDescription;
    private SharedPreferences prefTheme;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int importanceNum = -1;
    private int selectedItem = -1;
    private String selectedItemName;




    public ListViewClickListener(Context context, String menuName, TextView dateTextView, ImageView iconDescription) {
        this.context = context;
        this.menuName = menuName;
        this.dateTextView = dateTextView;
        this.iconDescription = iconDescription;

        init();
    }

    public void init() {
        prefTheme = context.getSharedPreferences("ThemeData", context.MODE_PRIVATE);
        pref = context.getSharedPreferences("CalendarData", context.MODE_PRIVATE);
        editor = pref.edit();

    }

    @Override
    public void onClick(View v) {
        showDateDialog();
    }

    public void showDateDialog() {
        final Dialog dialog = new Dialog(context);

        if (menuName.equals("시작일") || (menuName.equals("종료일"))) {
            dialog.setContentView(R.layout.datetime_dialog);
/*
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
 */
            setDateTimePicker(dialog);

            Button dateConformBtn = dialog.findViewById(R.id.dateConformBtn);
            dateConformBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((menuName.equals("시작일"))) {
                        dateSet(editor, "startDate", "startTime");
                    } else {
                        dateSet(editor, "endDate", "endTime");
                    }
                    editor.commit();
                    dialog.hide();
                }
            });
        }


        if (menuName.equals("중요도")) {
            dialog.setContentView(R.layout.importance_dialog);
            ConstraintLayout importanceHeader = dialog.findViewById(R.id.importanceHeader);
            setCustomTheme(importanceHeader);

            setRadioButton(dialog);

            Button importanceConfirm = dialog.findViewById(R.id.alarmConfirmBtn);
            importanceConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(importanceNum == 1){
                        iconDescription.setImageResource(R.drawable.importance1);
                    } else if(importanceNum == 2){
                        iconDescription.setImageResource(R.drawable.importance2);
                    } else if(importanceNum == 3){
                        iconDescription.setImageResource(R.drawable.importance3);
                    }
                    editor.putInt("importance", importanceNum);
                    editor.commit();
                    dialog.hide();
                }
            });

            Button importanceCancelBtn = dialog.findViewById(R.id.importanceCancelBtn);
            importanceCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                }
            });
        }

        if (menuName.equals("알    림")) {
            dialog.setContentView(R.layout.alarm_dialog);

            final TextView selectedTime = dialog.findViewById(R.id.selectedTime);
            Button alarm_10min = dialog.findViewById(R.id.alarm_10min);
            Button alarm_30min = dialog.findViewById(R.id.alarm_30min);
            Button alarm_1hour = dialog.findViewById(R.id.alarm_1hour);
            Button alarm_1day = dialog.findViewById(R.id.alarm_1day);
            Button alarmConfirmBtn = dialog.findViewById(R.id.alarmConfirmBtn);

            checkSelectedItem(alarm_10min, alarm_30min, alarm_1hour, alarm_1day, selectedTime);

            alarmConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedItem == 1){
                        dateTextView.setText(selectedItemName);
                    } else if(selectedItem == 2) {
                        dateTextView.setText(selectedItemName);
                    } else if(selectedItem == 3) {
                        dateTextView.setText(selectedItemName);
                    } else if(selectedItem == 4) {
                        dateTextView.setText(selectedItemName);
                    }


                    editor.putInt("alarm", selectedItem);
                    editor.commit();
                    dialog.hide();
                }
            });

            Button alarmCancelBtn = dialog.findViewById(R.id.alarmCancelBtn);
            alarmCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                }
            });
        }

        dialog.show();
    }


    public void dateSet(SharedPreferences.Editor editor, String startDate, String startTime) {

        Date date = new Date(mYear - 1900, mMonth, mDay , hour , min);
        dateTextView.setText(DateUtil.dateFormat2.format(date) + "  |  " + DateUtil.dateFormat3.format(date));

        editor.putString(startDate, DateUtil.dateFormat2.format(date));
        editor.putString(startTime, DateUtil.dateFormat3.format(date));
    }


    public void setDateTimePicker(Dialog dialog) {
        DatePicker datePicker = dialog.findViewById(R.id.datePicker);

        //날짜 설정
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(mYear, mMonth, mDay, dateChangedListener);


        //시간설정
        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        //hour_of_12_hour_format = calendar.get(Calendar.HOUR_OF_DAY);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            Log.d(TAG, "hour : " + hour);
            timePicker.setHour(hour + 5 );
            min = timePicker.getMinute();
            timePicker.setHour(min);
            Log.d(TAG, "min : " + min);
        } else {
            hour = timePicker.getCurrentHour();
            timePicker.setCurrentHour(hour);
            min = timePicker.getCurrentMinute();
            timePicker.setCurrentHour(min);
        }
        timePicker.setOnTimeChangedListener(timeChangedListener);
    }

    DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
            mYear = yy;
            mMonth = mm;
            mDay = dd;
        }
    };

    TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            hour = hourOfDay;
            min = minute;

        }
    };

    public void setRadioButton(Dialog dialog) {

        final RadioButton highButton = dialog.findViewById(R.id.highButton);
        final RadioButton middleButton = dialog.findViewById(R.id.middleButton);
        final RadioButton lowButton = dialog.findViewById(R.id.lowButton);



        RadioButton.OnClickListener radioButtonListener = new RadioButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == highButton) {
                    importanceNum = 1;
                } else if (v == middleButton) {
                    importanceNum = 2;
                } else if (v == lowButton) {
                    importanceNum = 3;
                }
            }
        };
        highButton.setOnClickListener(radioButtonListener);
        middleButton.setOnClickListener(radioButtonListener);
        lowButton.setOnClickListener(radioButtonListener);

    }

    public void checkSelectedItem(Button alarm_10min, Button alarm_30min, Button alarm_1hour, Button alarm_1day, final TextView selectedTime) {
        alarm_10min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = 1;
                selectedItemName = " 시작일 10분전 ";
                selectedTime.setText(selectedItemName);
            }
        });

        alarm_30min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = 2;
                selectedItemName = " 시작일 30분전 ";
                selectedTime.setText(selectedItemName);
            }
        });

        alarm_1hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = 3;
                selectedItemName = " 시작일 1시간전 ";
                selectedTime.setText(selectedItemName);
            }
        });

        alarm_1day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = 4;
                selectedItemName = " 시작일 하루전 ";
                selectedTime.setText(selectedItemName);
            }
        });
    }

    public void setCustomTheme(ConstraintLayout importanceHeader) {
        String themeName = prefTheme.getString("currentTheme", "기본테마");

        if(!themeName.equals("noTheme")){
            if(themeName.equals("기본테마")){
                importanceHeader.setBackgroundColor(context.getResources().getColor(R.color.mainColor));
            } else if(themeName.equals("첫번째")){
                importanceHeader.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
            } else if(themeName.equals("두번째")){
                importanceHeader.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
            } else if(themeName.equals("세번째")){
                importanceHeader.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            } else if(themeName.equals("네번째")){
                importanceHeader.setBackgroundColor(context.getResources().getColor(R.color.colorPink));
            }
        }
    }




}

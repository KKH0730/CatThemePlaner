package com.planer.catthemeplaner.model;

import androidx.lifecycle.ViewModel;

import com.planer.catthemeplaner.util.DateUtil;

import java.util.Calendar;


public class Day extends ViewModel {

    private String day;
    private String fullDate;

    public Day() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    // TODO : day에 달력일값넣기
    public void setCalendar(Calendar calendar){

        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);

    }

    public void setDateCalendar(Calendar calendar){
        fullDate = DateUtil.getDateFormat(calendar.getTimeInMillis(), DateUtil.FULL_DATE_FORMAT);
    }



}
package com.planer.catthemeplaner.model;


import androidx.lifecycle.ViewModel;

import com.planer.catthemeplaner.util.DateUtil;

public class CalendarHeader extends ViewModel {

    private String header;
    private String headerYear;
    private String headerMonth;


    public CalendarHeader() {

    }


    public String getHeader() {
        return header;
    }


    public void setHeader(long time) {

        String value = DateUtil.getDate(time, DateUtil.CALENDAR_HEADER_FORMAT);
        this.header = value;
    }


    public String getHeaderYear() {
        return headerYear;
    }

    public void setHeaderYear(long time) {
        String year = DateUtil.getDate(time, DateUtil.YEAR_FORMAT);
        this.headerYear = year;
    }


    public String getHeaderMonth() {
        return headerMonth;
    }

    public void setHeaderMonth(long time) {
        String month = DateUtil.getDate(time, DateUtil.MONTH_FORMAT);
        this.headerMonth = month;
    }


    public void setHeader(String header) {

        this.header = header;

    }

}
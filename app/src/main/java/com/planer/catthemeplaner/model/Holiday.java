package com.planer.catthemeplaner.model;

public class Holiday {

    int _id;
    private String holidayDate;
    private String holidayName;


    public Holiday(int _id, String holidayDate, String holidayName) {
        this._id = _id;
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
}

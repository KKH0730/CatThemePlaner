package com.planer.catthemeplaner.controller;

import com.planer.catthemeplaner.model.MemoListItem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TestComparator implements Comparator<MemoListItem> {

    @Override
    public int compare(MemoListItem o1, MemoListItem o2) {
        SimpleDateFormat format = new SimpleDateFormat("a hh시 mm분");

        Date day1 = null;
        Date day2 = null;

        try{
            if(!o1.getStartTime().equals("") &&!o2.getStartTime().equals("")) {
                day1 = format.parse(o1.getStartTime());
                day2 = format.parse(o2.getStartTime());
            } else {
                return 0;
            }

        }catch (ParseException e){
            e.printStackTrace();
        }

        int result = day1.compareTo(day2);

        return result;
    }
}

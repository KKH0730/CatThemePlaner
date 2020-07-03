package com.planer.catthemeplaner.model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class ThemeModel {
    private Context context;

    public ThemeModel(Context context) {
        this.context = context;
    }

    public void saveThemeInfo(String themeName) {
        String sql = "insert into " + CalendarDatabase.TABLE_THEME_LIST +
                " (THEME_NAME) values ('" + themeName + "')";

        CalendarDatabase database = CalendarDatabase.getInstance(context);
        database.execSQL(sql);
    }

    public ArrayList<String> loadThemeList() {
        String sql = "select * from " + CalendarDatabase.TABLE_THEME_LIST;

        ArrayList<String> items = new ArrayList<>();
        int recordCount = -1;
        CalendarDatabase database = CalendarDatabase.getInstance(context);
        if (database != null) {
            Cursor cursor = database.rawQuery(sql);
            recordCount = cursor.getCount();

            if (recordCount != 0) {

                for (int i = 0; i < recordCount; i++) {
                    cursor.moveToNext();
                    int _id = cursor.getInt(0);
                    String themeName = cursor.getString(1);

                    items.add(themeName);
                }
            }

            cursor.close();
        }
        return items;
    }

}

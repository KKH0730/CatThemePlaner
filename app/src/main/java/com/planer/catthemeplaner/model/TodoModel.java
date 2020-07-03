package com.planer.catthemeplaner.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoModel extends ViewModel {
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public TodoModel(Context context) {
        this.context = context;

        pref = context.getSharedPreferences("TodoData", context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveTodoInfo(TodoList todoList, String columnStr, String sharedNumber, String tableName) {
        String sql = "insert into " + tableName +
                " (" + columnStr + ", " + sharedNumber + ", RANK) values (" + "'" + todoList.getContent() + "', " + "'" + todoList.getSharedNumber() + "', " + "'" + todoList.getRank() + "')";

        CalendarDatabase database = CalendarDatabase.getInstance(context);
        database.execSQL(sql);
    }

    public void saveCompleteList(TodoList todoList) {
        String sql = "insert into " + CalendarDatabase.TABLE_COMPLETE_LIST +
                " (TODO_COMPLETE, SHARED_NUMBER, RANK) values ('" + todoList.getContent() + "', " + "'" + todoList.getSharedNumber() + "', " + "'" + todoList.getRank() + "')";

        CalendarDatabase database = CalendarDatabase.getInstance(context);
        database.execSQL(sql);
    }

    public void deleteTodoInfo(TodoList todoLists, String tableName) {
        String sql = "delete from " + tableName + " where SHARED_NUMBER = " + todoLists.getSharedNumber();
        CalendarDatabase calendarDatabase = CalendarDatabase.getInstance(context);
        calendarDatabase.execSQL(sql);
    }

    public void deleteTodoInfo(String tableName) {
        String sql = "delete from " + tableName;
        CalendarDatabase calendarDatabase = CalendarDatabase.getInstance(context);
        calendarDatabase.execSQL(sql);
    }


    public ArrayList<TodoList> loadTodoList(String columnStr, String tableName) {
        String sql = "select _id, " + columnStr + ", SHARED_NUMBER, RANK from " + tableName + " order by CREATE_DATE desc";

        ArrayList<TodoList> items = new ArrayList<>();
        int recordCount = -1;
        CalendarDatabase database = CalendarDatabase.getInstance(context);
        if (database != null) {
            Cursor cursor = database.rawQuery(sql);
            recordCount = cursor.getCount();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();
                int _id = cursor.getInt(0);
                String todo = cursor.getString(1);
                int sharedNumber = cursor.getInt(2);
                String rank = cursor.getString(3);


                items.add(new TodoList(_id, todo, sharedNumber, rank));
            }

            cursor.close();
        }
        return items;
    }

    public ArrayList<TodoList> loadCompleteList(String tableName) {
        String sql = "select * from " +  tableName + " order by  CREATE_DATE ASC";

        ArrayList<TodoList> items = new ArrayList<>();
        int recordCount = -1;
        CalendarDatabase database = CalendarDatabase.getInstance(context);
        if (database != null) {
            Cursor cursor = database.rawQuery(sql);
            recordCount = cursor.getCount();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();
                int _id = cursor.getInt(0);
                String todo = cursor.getString(1);
                int sharedNumber = cursor.getInt(2);
                String rank = cursor.getString(3);


                items.add(new TodoList(_id, todo, sharedNumber, rank));
            }

            cursor.close();
        }
        return items;
    }



}

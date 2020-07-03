package com.planer.catthemeplaner.ui.calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.planer.catthemeplaner.MainActivity;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.controller.TestComparator;
import com.planer.catthemeplaner.adapter.CalendarListAdapter;
import com.planer.catthemeplaner.listener.OnPopupMenuItemClickListener;
import com.planer.catthemeplaner.model.CalendarDatabase;
import com.planer.catthemeplaner.model.MemoListItem;
import com.planer.catthemeplaner.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


import butterknife.BindView;
import butterknife.ButterKnife;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;


public class ListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "ListFragment";

    @BindView(R.id.date)
    TextView dateText;
    @BindView(R.id.listRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.frameLayout)
    ConstraintLayout headerLayout;
    @BindView(R.id.horizontalCalendarHeaderLayout)
    ConstraintLayout horizontalCalendarHeaderLayout;
    @BindView(R.id.todayBtn)
    Button todayBtn;

    private CalendarListAdapter listAdapter;
    private MemoListItem memoListItem;
    private SharedPreferences prefTheme;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String startDate;
    private String showDate;
    private int selectEnable = -1;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            startDate = bundle.getString("startDate");
        } else {
            return;
        }

         Log.d(TAG, "startDate : " + bundle.getString("startDate"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, rootView);
        init();
        setCustomTheme();
        buttonEvent();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        if (startDate.equals("")) {
            setHorizontalCalendar(rootView);
        } else {
            setHorizontalCalendar(rootView, startDate);
        }


        listAdapter.setOnPopupMenuClickListener(new OnPopupMenuItemClickListener() {
            @Override
            public void onPopupMenuClickListener(int position, View v) {

                MemoListItem memoListItem = listAdapter.getItem(position);
                setMemoListItem(memoListItem);

                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.setOnMenuItemClickListener(ListFragment.this);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());
                popup.show();

            }
        });


        return rootView;
    }

    public void init() {
        listAdapter = new CalendarListAdapter(getContext());
        prefTheme = getContext().getSharedPreferences("ThemeData", getContext().MODE_PRIVATE);
        pref = getContext().getSharedPreferences("CalendarData", getContext().MODE_PRIVATE);
        editor = pref.edit();
        startDate = pref.getString("selectedItem", "");

    }

    public static Fragment newInstance(String param1) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("startDate", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public void setCustomTheme() {
        String themeName = prefTheme.getString("currentTheme", "기본테마");

        if(!themeName.equals("noTheme")){
            if(themeName.equals("기본테마")){
                headerLayout.setBackgroundColor(getResources().getColor(R.color.mainColor));
            } else if(themeName.equals("첫번째")){
                headerLayout.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if(themeName.equals("두번째")){
                headerLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            } else if(themeName.equals("세번째")){
                headerLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            } else if(themeName.equals("네번째")){
                headerLayout.setBackgroundColor(getResources().getColor(R.color.colorPink));
            }
        }
    }

    public void setMemoListItem(MemoListItem memoListItem) {
        this.memoListItem = memoListItem;
    }

    public void buttonEvent() {
        horizontalCalendarHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = null;
                try {
                    date = DateUtil.dateFormat2.parse(dateText.getText().toString());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                DatePickerDialog dialog = new DatePickerDialog(getContext(), datePickerListener, (date.getYear() + 1900), (date.getMonth()), date.getDate());

                dialog.show();
            }
        });


        todayBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        todayBtn.setTextColor(getContext().getResources().getColor(R.color.selectorColor));
                        ListFragment listFragment = new ListFragment();
                        ((MainActivity) getActivity()).replaceFragment(listFragment);
                        return true;
                    case MotionEvent.ACTION_UP:
                        todayBtn.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                        return true;
                }

                return false;
            }
        });
    }

    public void setHorizontalCalendar(View rootView) {

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 12);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.horizontalCalendar)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("E")
                .dayNumberFormat("dd")
                .monthFormat("MM")
                .textSize(12f, 20f, 12f)
                .showDayName(true)
                .showMonthName(false)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                showDate = DateUtil.dateFormat2.format(date);

                dateText.setText(showDate);
                loadListData(showDate);

                ArrayList<MemoListItem> ascendingArray = ascendingOrder(loadListData(showDate));
                listAdapter.setItems(ascendingArray);
                listAdapter.notifyDataSetChanged();

            }
        });
    }


    public ArrayList<MemoListItem> loadListData(String compareDate) {
        String sql = "select _id, MEMO, POSITION, STARTDATE, ENDDATE, STARTTIME, ENDTIME, ALARM, IMPORTANCE, DESCRIPTION from " + CalendarDatabase.TABLE_CALENDAR + " where STARTDATE='" + compareDate + "'" + " order by STARTDATE desc";

        ArrayList<MemoListItem> items = new ArrayList<>();
        int recordCount = -1;
        CalendarDatabase database = CalendarDatabase.getInstance(getContext());
        if (database != null) {
            Cursor cursor = database.rawQuery(sql);
            recordCount = cursor.getCount();

            for (int i = 0; i < recordCount; i++) {
                cursor.moveToNext();
                int _id = cursor.getInt(0);
                String memo = cursor.getString(1);
                String position = cursor.getString(2);
                String startDate = cursor.getString(3);
                String endDate = cursor.getString(4);
                String startTime = cursor.getString(5);
                String endTime = cursor.getString(6);
                int alarm = cursor.getInt(7);
                int importance = cursor.getInt(8);
                String description = cursor.getString(9);

                items.add(new MemoListItem(_id, memo, position, startDate, endDate, startTime, endTime, alarm, importance, description));
            }

            cursor.close();
        }
        return items;
    }

    public ArrayList<MemoListItem> ascendingOrder(ArrayList<MemoListItem> items) {
        ArrayList<MemoListItem> transIntegerArray = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            transIntegerArray.add(new MemoListItem(items.get(i).get_id(), items.get(i).getMemo(), items.get(i).getPosition(),
                    items.get(i).getStartDate(), items.get(i).getEndDate(), items.get(i).getStartTime(), items.get(i).getEndTime(),
                    items.get(i).getAlarm(), items.get(i).getImportance(), items.get(i).getDescription()));
        }

        //오름차순정렬
        TestComparator tc = new TestComparator();

        Collections.sort(transIntegerArray, tc);
        for (int j = 0; j < transIntegerArray.size(); j++) {
        }
        return transIntegerArray;
    }


    public void setHorizontalCalendar(final View rootView, String startDate) {
        Date givenDate = null;
        try {
            givenDate = DateUtil.dateFormat2.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date defaultDate = givenDate;
        final Calendar sDate = Calendar.getInstance();
        sDate.add(Calendar.MONTH, -12);

        final Calendar eDate = Calendar.getInstance();
        eDate.add(Calendar.MONTH, 12);

        final HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.horizontalCalendar)
                .startDate(sDate.getTime())
                .endDate(eDate.getTime())
                .defaultSelectedDate(defaultDate)
                .datesNumberOnScreen(5)
                .dayNameFormat("E")
                .dayNumberFormat("dd")
                .monthFormat("MM")
                .textSize(12f, 20f, 12f)
                .showDayName(true)
                .showMonthName(false)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                selectEnable++;

                if (selectEnable > 1) {
                    Log.d(TAG, "selectedEnable : " + selectEnable);
                    showDate = DateUtil.dateFormat2.format(date);

                    dateText.setText(showDate);
                    loadListData(showDate);
                    Log.d(TAG, "startDate : " + showDate);

                } else if (selectEnable == 0) {
                    Log.d(TAG, "selectedEnable2 : " + selectEnable);
                    date = defaultDate;
                    showDate = DateUtil.dateFormat2.format(date);

                    dateText.setText(showDate);
                    loadListData(showDate);
                }
                ArrayList<MemoListItem> ascendingArray = ascendingOrder(loadListData(showDate));
                listAdapter.setItems(ascendingArray);
                listAdapter.notifyDataSetChanged();
            }
        });
    }



    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText(getContext(), year + "년" + (month + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            Date date = new Date((year - 1900), month, dayOfMonth);
            String startDate = DateUtil.dateFormat2.format(date);

            editor.putString("selectedItem", startDate).commit();
            ((MainActivity) getActivity()).replaceFragment(ListFragment.newInstance(startDate));

        }
    };


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modifyBtn:
                ((MainActivity) getActivity()).showModifyForm(memoListItem);
                return true;
            case R.id.cancelBtn:
                Toast.makeText(getContext(), "정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).deleteInfo(memoListItem);
        }
        return false;
    }

}






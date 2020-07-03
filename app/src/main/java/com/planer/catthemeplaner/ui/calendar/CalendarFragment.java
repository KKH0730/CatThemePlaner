package com.planer.catthemeplaner.ui.calendar;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.planer.catthemeplaner.adapter.CalendarAdapter;
import com.planer.catthemeplaner.listener.CalendarClickListener;
import com.planer.catthemeplaner.controller.ItemDecoration;
import com.planer.catthemeplaner.MainActivity;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.model.CalendarHeader;
import com.planer.catthemeplaner.util.Keys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";

    private ArrayList<Object> mCalendarList = new ArrayList<Object>();
    private Long timeMillis;

    @BindView(R.id.headerMonth)
    TextView headerM;
    @BindView(R.id.headerYear)
    TextView headerY;
    @BindView(R.id.monthENG)
    TextView monthEng;
    @BindView(R.id.calendar)
    RecyclerView recyclerView;
    @BindView(R.id.headerLayout)
    ConstraintLayout headerLayout;
    @BindView(R.id.calendarHeader)
    ConstraintLayout calendarHeader;

    private CalendarAdapter mAdapter;
    private GridLayoutManager manager;
    private SharedPreferences prefTheme;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private int pageControl = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        ButterKnife.bind(this, rootView);
        init();

        setCustomTheme();

        setCalendarList();
        setRecycler();
        buttonEvent(rootView);
        setDateData();

        mAdapter.setOnCalendarClickListener(new CalendarClickListener() {
            @Override
            public void onCalendarClickListener(String fullDate) {
                editor.putString("selectedItem", fullDate).commit();
                ((MainActivity) getActivity()).replaceFragment(ListFragment.newInstance(fullDate));
            }
        });


        return rootView;
    }

    public void init() {
        prefTheme = getContext().getSharedPreferences("ThemeData", getContext().MODE_PRIVATE);
        pref = getContext().getSharedPreferences("CalendarData", getContext().MODE_PRIVATE);
        editor = pref.edit();
    }

    public static Fragment newInstance(String param1) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString("memo", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public void setCustomTheme() {
        String themeName = prefTheme.getString("currentTheme", "기본테마");

        if(!themeName.equals("noTheme")){
            if(themeName.equals("기본테마")){
                calendarHeader.setBackgroundColor(getResources().getColor(R.color.mainColor));
            } else if(themeName.equals("첫번째")){
               calendarHeader.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if(themeName.equals("두번째")){
                calendarHeader.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            } else if(themeName.equals("세번째")){
                calendarHeader.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            } else if(themeName.equals("네번째")){
                calendarHeader.setBackgroundColor(getResources().getColor(R.color.colorPink));
            }
        }
    }


    public void setCalendarList() {
        ArrayList<Object> calendar = setDateCalendar(pageControl);
        mCalendarList = calendar;
    }

    public ArrayList<Object> setDateCalendar(int i) {
        GregorianCalendar cal = new GregorianCalendar();
        ArrayList<Object> calendarList = new ArrayList<>();

        dateSet(calendarList, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), i);

        return calendarList;
    }

    private void setRecycler() {

        manager = new GridLayoutManager(getContext(), 7);
        mAdapter = new CalendarAdapter(mCalendarList);

        recyclerView.addItemDecoration(new ItemDecoration(getContext()));
        //달력에 표시되는 날짜의 time을 millis로 adapter에 set함
        mAdapter.setTimeMillis(timeMillis);
        //달력 정보 set
        mAdapter.setCalendarList(mCalendarList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);


    }

    public void buttonEvent(View rootView) {
        Button prevButton = rootView.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //--pageControl;
                pageControl = -1;
                setPrevNextDate();
            }
        });

        Button nextButton = rootView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //++pageControl;
                pageControl = 1;
                setPrevNextDate();

            }
        });

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                Date date = new Date(Integer.parseInt(headerY.getText().toString()), Integer.parseInt(headerM.getText().toString()), calendar.get(Calendar.DAY_OF_MONTH));
                DatePickerDialog dialog = new DatePickerDialog(getContext(), datePickerListener, date.getYear(), (date.getMonth() - 1), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });


    }

    public void setDateData() {
        String[] monthArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        Long time = timeMillis;
        CalendarHeader model = new CalendarHeader();

        model.setHeaderYear(time);
        String headerYear = model.getHeaderYear();
        headerY.setText(headerYear);

        model.setHeaderMonth(time);
        String headerMonth = model.getHeaderMonth();
        headerM.setText(headerMonth);


        switch (headerMonth) {
            case "01":
                monthEng.setText(monthArray[0]);
                break;
            case "02":
                monthEng.setText(monthArray[1]);
                break;
            case "03":
                monthEng.setText(monthArray[2]);
                break;
            case "04":
                monthEng.setText(monthArray[3]);
                break;
            case "05":
                monthEng.setText(monthArray[4]);
                break;
            case "06":
                monthEng.setText(monthArray[5]);
                break;
            case "07":
                monthEng.setText(monthArray[6]);
                break;
            case "08":
                monthEng.setText(monthArray[7]);
                break;
            case "09":
                monthEng.setText(monthArray[8]);
                break;
            case "10":
                monthEng.setText(monthArray[9]);
                break;
            case "11":
                monthEng.setText(monthArray[10]);
                break;
            case "12":
                monthEng.setText(monthArray[11]);
                break;
            default:
                monthEng.setText("");
                break;
        }
    }


    public void setPrevNextDate() {
        //mCalendarList = setDateCalendar(pageControl);
        mCalendarList = setDateCalendar2(pageControl);
        setDateData();
        //달력에 표시되는 날짜의 time을 millis로 adapter에 set함
        mAdapter.setTimeMillis(timeMillis);
        mAdapter.setCalendarList(mCalendarList);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText(getContext(), year + "년" + (month + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();


            ArrayList<Object> calendarList = new ArrayList<>();
            dateSet(calendarList, year, month, 0);

            mAdapter.setTimeMillis(timeMillis);
            mAdapter.setCalendarList(calendarList);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            setDateData();
        }
    };


    public ArrayList<Object> setDateCalendar2(int i) {

        ArrayList<Object> calendarList = new ArrayList<>();
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTimeInMillis(mAdapter.getTimeMillis());
        dateSet(calendarList, aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH), i);

        return calendarList;


    }

    public void dateSet(ArrayList<Object> calendarList, int Year, int Month, int i) {
        try {

            GregorianCalendar calendar = new GregorianCalendar(Year, Month + i, 1, 0, 0, 0);

            timeMillis = calendar.getTimeInMillis();

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            // EMPTY 생성
            for (int j = 0; j < dayOfWeek; j++) {
                calendarList.add(Keys.EMPTY);
            }
            for (int j = 1; j <= max; j++) {
                calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColor(int color) {
        calendarHeader.setBackgroundColor(color);
    }


}

package com.planer.catthemeplaner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.planer.catthemeplaner.adapter.CalendarViewPagerAdapter;
import com.planer.catthemeplaner.adapter.MemoListAdapter;
import com.planer.catthemeplaner.controller.JobSchedulerStart;
import com.planer.catthemeplaner.controller.NotificationService;
import com.planer.catthemeplaner.gson.BodyItem;
import com.planer.catthemeplaner.gson.BodyItems;
import com.planer.catthemeplaner.gson.HolidayList;
import com.planer.catthemeplaner.gson.HolidayListResult;
import com.planer.catthemeplaner.listener.MenuCallback;
import com.planer.catthemeplaner.listener.OnBackPressedListener;
import com.planer.catthemeplaner.model.CalendarDatabase;
import com.planer.catthemeplaner.model.CalendarModel;
import com.planer.catthemeplaner.model.MemoItem;
import com.planer.catthemeplaner.model.MemoListItem;
import com.planer.catthemeplaner.ui.calendar.CalendarFragment;
import com.planer.catthemeplaner.ui.calendar.ListFragment;
import com.planer.catthemeplaner.ui.calendar.StoreFragment;
import com.planer.catthemeplaner.ui.calendar.TodoFragment;
import com.planer.catthemeplaner.util.DateUtil;
import com.planer.catthemeplaner.util.UtilCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MenuCallback {
    private static final String TAG = "hi";

    private CalendarFragment calendarFragment;
    private ListFragment listFragment;
    private TodoFragment todoFragment;
    private StoreFragment storeFragment;
    private CalendarViewPagerAdapter pagerAdapter;
    private CalendarModel calendarModel;
    private MemoListItem memoListItem;
    private MemoListAdapter adapter;
    private static CalendarDatabase mDatabase;


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.memoPageHeader)
    ConstraintLayout memoPageHeader;
    @BindView(R.id.floatButton)
    FloatingActionButton floatButton;
    @BindView(R.id.memoPage)
    ConstraintLayout memoPage;
    @BindView(R.id.memoInput)
    EditText memoInput;
    @BindView(R.id.memoList)
    ListView listView;
    @BindView(R.id.confirm)
    Button confirmBtn;
    @BindView(R.id.cancel)
    Button cancelBtn;

    private Animation translateTopAnim;
    private Animation translateBottomAnim;

    private boolean isPageOpen = false;
    private boolean sendDatabaseOk = true;
    private int mMode = UtilCode.MODE_SAVE;

    private SharedPreferences pref;
    private SharedPreferences prefTheme;
    private SharedPreferences.Editor editor;

    private OnBackPressedListener listener;
    private long backKeyPressedTime;

    private static RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        setCustomTheme();




        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        makeRequest();
        openDatabase();
        buttonEvent();

        viewPager.setOffscreenPageLimit(4);
        pagerAdapter.addItem(calendarFragment);
        pagerAdapter.addItem(listFragment);
        pagerAdapter.addItem(todoFragment);
        pagerAdapter.addItem(storeFragment);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(viewPagerListener);


        SlidingMemoPageAnimationListener animListener = new SlidingMemoPageAnimationListener();
        translateTopAnim.setAnimationListener(animListener);
        translateBottomAnim.setAnimationListener(animListener);
    }

    public void initView() {
        calendarFragment = new CalendarFragment();
        listFragment = new ListFragment();
        todoFragment = new TodoFragment();
        storeFragment = new StoreFragment();
        calendarModel = new CalendarModel(this);
        adapter = new MemoListAdapter(this);
        pagerAdapter = new CalendarViewPagerAdapter(getSupportFragmentManager());

        translateTopAnim = AnimationUtils.loadAnimation(this, R.anim.translate_top);
        translateBottomAnim = AnimationUtils.loadAnimation(this, R.anim.translate_bottom);

        prefTheme = this.getSharedPreferences("ThemeData", this.MODE_PRIVATE);
        pref = this.getSharedPreferences("CalendarData", this.MODE_PRIVATE);
        editor = pref.edit();

    }

    public void setCustomTheme() {
        String themeName = prefTheme.getString("currentTheme", "기본테마");

        if (!themeName.equals("noTheme")) {
            if (themeName.equals("기본테마")) {
                floatButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.mainColor)));
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.mainColor));
                getWindow().setStatusBarColor(getResources().getColor(R.color.mainColor));
                memoPageHeader.setBackgroundColor(getResources().getColor(R.color.mainColor));
            } else if (themeName.equals("첫번째")) {
                floatButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorOrange)));
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorOrange));
                memoPageHeader.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if (themeName.equals("두번째")) {
                floatButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorYellow)));
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorYellow));
                memoPageHeader.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            } else if (themeName.equals("세번째")) {
                floatButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorGreen)));
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorGreen));
                memoPageHeader.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            } else if (themeName.equals("네번째")) {
                floatButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPink)));
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorPink));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPink));
                memoPageHeader.setBackgroundColor(getResources().getColor(R.color.colorPink));
            }
        }
    }

    public void makeRequest() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        Log.d(TAG, "YEAR1 : " + year);

        if (year == pref.getInt("year", year)) {
            String url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear=" + year + "&numOfRows=30&ServiceKey=2s75s1z21wLj3ZW%2FtmxrwqyEB2tdoYVJ3ekpF2Hdvq95TKCgvY5G6eZdM03zkrxJLQNKtUnPS9jBByp0%2F2KACQ%3D%3D&_type=json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    processResponse(response, year);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "에러 -> " + error);
                        }
                    }) {

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String utf8String = new String(response.data, "UTF-8");
                        return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));

                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (Exception e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };

            request.setShouldCache(false);
            requestQueue.add(request);
        }
    }

    public void processResponse(String responseStr, int year) {
        Gson gson = new Gson();
        HolidayList holidayList = gson.fromJson(responseStr, HolidayList.class);
        year++;

        editor.putInt("year", year).commit();

        if (holidayList.response.body.items.item.size() != 0) {
            for (int i = 0; i < holidayList.response.body.items.item.size(); i++) {

                calendarModel.saveHoliday(holidayList.response.body.items.item.get(i).locdate, holidayList.response.body.items.item.get(i).dateName);

            }
        }
    }


    public void openDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
        mDatabase = CalendarDatabase.getInstance(this);

        boolean isOpen = mDatabase.open();
        if (isOpen) {
            Log.d(TAG, "database is open.");
        } else {
            Log.d(TAG, "database is not open.");
        }
    }


    @Override
    public void onMenuSelected(int position, Bundle bundle) {
        Fragment currentFragment = null;

        if (position == 0) {
            goneData();
            currentFragment = new CalendarFragment();

        } else if (position == 1) {
            goneData();
            currentFragment = new ListFragment();

        } else if (position == 2) {
            goneData();
            currentFragment = new TodoFragment();

        } else if (position == 3) {
            goneData();
            currentFragment = new StoreFragment();
        }

        viewPager.setCurrentItem(position);
    }

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (position == 0) {
                bottomNavigationView.setSelectedItemId(R.id.calendar_menu);
                //transaction.attach(listFragment).commit();
            } else if (position == 1) {
                bottomNavigationView.setSelectedItemId(R.id.list_menu);
                //transaction.attach(listFragment).commit();
            } else if (position == 2) {
                bottomNavigationView.setSelectedItemId(R.id.todo_menu);
                //transaction.attach(listFragment).commit();
            } else if (position == 3) {
                bottomNavigationView.setSelectedItemId(R.id.store_menu);
                //transaction.attach(listFragment).commit();
            }
        }
    };


    public void goneData() {
        editor.remove("memo").commit();
        editor.remove("startDate").commit();
        editor.remove("endDate").commit();
        editor.remove("startTime").commit();
        editor.remove("endTime").commit();
        editor.remove("importance").commit();
        editor.remove("alarm").commit();
        editor.remove("position").commit();
        editor.remove("description").commit();
        editor.remove("selectedItem").commit();
    }

    public void setMemoListItem(MemoListItem memoListItem) {
        this.memoListItem = memoListItem;
    }

    public void replaceFragment(final Fragment fragment) {

        final ImageView loadingCat = findViewById(R.id.loadingCat);
        GlideDrawableImageViewTarget gifLoading = new GlideDrawableImageViewTarget(loadingCat);
        Glide.with(this).load(R.raw.loadingcat).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifLoading);
        loadingCat.setVisibility(View.VISIBLE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(listFragment).attach(listFragment).commit();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(1);

                loadingCat.setVisibility(View.GONE);
                //floatButton.setVisibility(View.GONE);
            }
        }, 2000);
    }


    public void showModifyForm(MemoListItem memoListItem) {
        mMode = UtilCode.MODE_UPDATE;

        onMenuSelected(0, null);

        editor.putString("memo", memoListItem.getMemo());

        listView.setVisibility(View.VISIBLE);
        memoInput.setVisibility(View.VISIBLE);
        memoInput.setText(memoListItem.getMemo());
        memoPage.setVisibility(View.VISIBLE);
        memoPage.startAnimation(translateTopAnim);

        adapter.setSendMemoListItem(memoListItem);
        setMemoListItem(memoListItem);

        editor.putString("memo", memoListItem.getMemo());
        editor.putString("startDate", memoListItem.getStartDate()).commit();
        editor.putString("endDate", memoListItem.getEndDate()).commit();
        editor.putString("startTime", memoListItem.getStartTime()).commit();
        editor.putString("endTime", memoListItem.getEndTime()).commit();
        editor.putInt("importance", memoListItem.getImportance()).commit();
        editor.putInt("alarm", memoListItem.getAlarm()).commit();
        editor.putString("position", memoListItem.getPosition()).commit();
        editor.putString("description", memoListItem.getDescription()).commit();
    }

    public void deleteInfo(MemoListItem memoListItem) {

        //알람매니저 취소
        if (memoListItem.getAlarm() != -1) {
            String startMonth = null;
            String startDay = null;
            String startHour = null;
            String startMinute = null;

            try {
                startMonth = DateUtil.dateFormatMonth.format(DateUtil.dateFormat2.parse(memoListItem.getStartDate()));
                startDay = DateUtil.dateFormatDay.format(DateUtil.dateFormat2.parse(memoListItem.getStartDate()));

                startHour = DateUtil.dateFormatHour.format(DateUtil.dateFormat3.parse(memoListItem.getStartTime()));
                startMinute = DateUtil.dateFormatMinute.format(DateUtil.dateFormat3.parse(memoListItem.getStartTime()));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            int pendingCode = Integer.parseInt("20" + startMonth + startDay + startHour + startMinute);
            NotificationService.cancelAlarmManager(getApplicationContext(), pendingCode);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(listFragment).attach(listFragment).detach(calendarFragment).attach(calendarFragment).commit();

        viewPager.setCurrentItem(1);

        calendarModel.deleteInfo(memoListItem);
    }

    public void buttonEvent() {
        listViewSet();
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPageOpen) {
                } else {
                    confirmBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    memoInput.setVisibility(View.VISIBLE);
                    memoPage.setVisibility(View.VISIBLE);
                    memoPage.startAnimation(translateTopAnim);
                    floatButton.setVisibility(View.GONE);
                }
            }

        });


        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.calendar_menu) {
                    onMenuSelected(0, null);
                    setGonListView();

                    if (isPageOpen) {
                        memoPage.startAnimation(translateBottomAnim);
                    }
                    return true;

                } else if (item.getItemId() == R.id.list_menu) {
                    onMenuSelected(1, null);
                    setGonListView();
                    //floatButton.setVisibility(View.GONE);
                    //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    //transaction.attach(listFragment).commit();

                    if (isPageOpen) {
                        memoPage.startAnimation(translateBottomAnim);
                    }

                    return true;

                } else if (item.getItemId() == R.id.todo_menu) {
                    onMenuSelected(2, null);
                    setGonListView();
                    floatButton.setVisibility(View.GONE);

                    if (isPageOpen) {
                        memoPage.startAnimation(translateBottomAnim);
                    }

                    return true;

                } else if (item.getItemId() == R.id.store_menu) {
                    onMenuSelected(3, null);
                    setGonListView();
                    floatButton.setVisibility(View.GONE);

                    if (isPageOpen) {
                        memoPage.startAnimation(translateBottomAnim);
                    }

                    return true;
                }
                return false;
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date sDate = null;
                Date eDate = null;
                String endDate = null;
                String endTime = null;
                int importanceNum = -1;
                String position = null;
                String description = null;
                int alarm = -1;
                Bundle bundle = new Bundle();


                Calendar calendar = Calendar.getInstance();
                Long time = calendar.getTimeInMillis();
                Date date = new Date(time);
                String startDate = DateUtil.dateFormat2.format(date);
                String startTime = DateUtil.dateFormat3.format(date);


                try {
                    if (!pref.getString("startDate", " ").equals(" ")) {
                        sDate = DateUtil.dateFormat2.parse(pref.getString("startDate", " "));
                        startDate = DateUtil.dateFormat2.format(sDate);
                    } else {
                        startDate = DateUtil.dateFormat2.format(date);
                    }
                    if (!pref.getString("endDate", " ").equals(" ")) {
                        eDate = DateUtil.dateFormat2.parse(pref.getString("endDate", " "));
                        endDate = DateUtil.dateFormat2.format(eDate);
                    } else {
                        endDate = " ";
                    }
                    if (!pref.getString("startTime", " ").equals(" ")) {
                        startTime = DateUtil.dateFormat3.format(new Date(String.valueOf(DateUtil.dateFormat3.parse((pref.getString("startTime", " "))))));

                    } else {
                        startTime = DateUtil.dateFormat3.format(date);
                    }
                    if (!pref.getString("endTime", " ").equals(" ")) {
                        endTime = DateUtil.dateFormat3.format(new Date(String.valueOf(DateUtil.dateFormat3.parse((pref.getString("endTime", " "))))));

                    } else {
                        endTime = " ";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                }
                dateCompare(pref, sDate, eDate);


                //중요도 수치를 데이터베이스로 보낸다 (1:매우중요 2: 중요 3:보통 4:관심없음 -1: 선택안함)
                if (pref.getInt("importance", -1) != -1) {
                    importanceNum = pref.getInt("importance", -1);
                } else {
                    importanceNum = -1;
                }

                //위치 String 값을 받아서 데이터베이스로 보낸다
                if (!pref.getString("position", " ").equals(" ")) {
                    position = pref.getString("position", " ");

                }
                //설명 String 값을 받아서 데이터베이스로 보낸다
                if (!pref.getString("description", " ").equals(" ")) {
                    description = pref.getString("description", " ");
                }

                if (pref.getInt("alarm", -1) != -1) {
                    alarm = pref.getInt("alarm", -1);
                    Toast.makeText(getApplicationContext(), "알람이 설정되었습니다. ", Toast.LENGTH_SHORT).show();
                }

                bundle.putString("startDate", startDate);
                bundle.putString("startTime", startTime);
                bundle.putInt("alarm", alarm);
                bundle.putString("memo", memoInput.getText().toString());
                bundle.putString("position", position);


                if (mMode == UtilCode.MODE_SAVE) {
                    calendarModel.saveInfo(memoInput.getText().toString(), position, startDate, endDate, startTime, endTime, alarm, importanceNum, description);
                    if (alarm != -1) {

                        JobSchedulerStart.start(getApplicationContext(), bundle);
                    }
                } else if (mMode == UtilCode.MODE_UPDATE) {
                    String startMonth = null;
                    String startDay = null;
                    String startHour = null;
                    String startMinute = null;

                    try {
                        startMonth = DateUtil.dateFormatMonth.format(DateUtil.dateFormat2.parse(memoListItem.getStartDate()));
                        startDay = DateUtil.dateFormatDay.format(DateUtil.dateFormat2.parse(memoListItem.getStartDate()));

                        startHour = DateUtil.dateFormatHour.format(DateUtil.dateFormat3.parse(memoListItem.getStartTime()));
                        startMinute = DateUtil.dateFormatMinute.format(DateUtil.dateFormat3.parse(memoListItem.getStartTime()));
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }

                    if (alarm == -1) {
                        //알람 취소
                        int pendingCode = Integer.parseInt("20" + startMonth + startDay + startHour + startMinute);
                        NotificationService.cancelAlarmManager(getApplicationContext(), pendingCode);

                        calendarModel.updateInfo(memoInput.getText().toString(), position, startDate, endDate, startTime, endTime, alarm, importanceNum, description, memoListItem);
                    } else {
                        //알람 취소 후 변경
                        int pendingCode = Integer.parseInt("20" + startMonth + startDay + startHour + startMinute);
                        NotificationService.cancelAlarmManager(getApplicationContext(), pendingCode);

                        JobSchedulerStart.start(getApplicationContext(), bundle);

                        calendarModel.updateInfo(memoInput.getText().toString(), position, startDate, endDate, startTime, endTime, alarm, importanceNum, description, memoListItem);
                    }
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.detach(listFragment).attach(listFragment).commit();
                closeListView();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeListView();
            }
        });
    }

    public void closeListView() {
        if (sendDatabaseOk) {
            try {
                //메모 페이지에서 확인 버튼을 누르면 키패드 자동 제거
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(memoInput.getWindowToken(), 0);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isPageOpen) {
                            memoPage.startAnimation(translateBottomAnim);
                            setGonListView();

                        }
                    }
                }, 100);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.detach(calendarFragment).attach(calendarFragment).commit();

            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

    public void dateCompare(SharedPreferences pref, Date sDate, Date eDate) {

        //시작일이 종료일보다 빠른 경우
        if (!pref.getString("startDate", " ").equals(" ") && !pref.getString("endDate", " ").equals(" ")) {
            if (sDate.getYear() > eDate.getYear()) {
                Toast.makeText(this, "종료일을 시작일 이전으로 설정해주세요.", Toast.LENGTH_SHORT).show();
                sendDatabaseOk = false;
            } else if (sDate.getYear() == eDate.getYear()) {
                if (sDate.getMonth() > eDate.getMonth()) {
                    Toast.makeText(this, "종료일을 시작일 이전으로 설정해주세요.", Toast.LENGTH_SHORT).show();
                    sendDatabaseOk = false;
                } else if (sDate.getMonth() == eDate.getMonth()) {
                    if (sDate.getDate() > eDate.getDate()) {
                        Toast.makeText(this, "종료일을 시작일 이전으로 설정해주세요.", Toast.LENGTH_SHORT).show();
                        sendDatabaseOk = false;
                    } else {
                        sendDatabaseOk = true;
                    }
                } else {
                    sendDatabaseOk = true;
                }
            } else {
                sendDatabaseOk = true;
            }
        } else if (pref.getString("startDate", " ").equals(" ") && pref.getString("endDate", " ").equals(" ")) {
            sendDatabaseOk = true;
        } else {
            sendDatabaseOk = true;
        }
    }


    public void listViewSet() {
        adapter.addItem(new MemoItem(R.drawable.position, "위    치"));
        adapter.addItem(new MemoItem(R.drawable.calendar_start, "시작일"));
        adapter.addItem(new MemoItem(R.drawable.calendar_end, "종료일"));
        adapter.addItem(new MemoItem(R.drawable.bell, "알    림"));
        adapter.addItem(new MemoItem(R.drawable.important, "중요도"));
        adapter.addItem(new MemoItem(R.drawable.description, "설    명"));

        listView.setAdapter(adapter);
    }

    public void setGonListView() {
        confirmBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
        listView.removeAllViewsInLayout();
        listView.setVisibility(View.GONE);
        floatButton.setVisibility(View.VISIBLE);
        memoInput.setVisibility(View.GONE);
    }

    private class SlidingMemoPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                memoPage.setVisibility(View.GONE);
                memoInput.setText(null);
                isPageOpen = false;
                memoInput.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            } else {
                isPageOpen = true;
                memoInput.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBackPressed() {
        if (listener != null) {
            listener.onBackPressed();
        } else {
            if (isPageOpen) {
                closeListView();
            }

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
        goneData();
    }


}

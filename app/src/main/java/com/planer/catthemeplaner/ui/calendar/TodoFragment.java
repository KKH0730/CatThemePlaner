package com.planer.catthemeplaner.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.planer.catthemeplaner.CompleteListActivity;
import com.planer.catthemeplaner.InfoActivity;
import com.planer.catthemeplaner.MainActivity;
import com.planer.catthemeplaner.adapter.TodoViewPagerAdapter;
import com.planer.catthemeplaner.listener.OnBackPressedListener;
import com.planer.catthemeplaner.listener.OnDeleteListener;
import com.planer.catthemeplaner.listener.OnTodoCheckListener;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.adapter.TodoListAdapter;
import com.planer.catthemeplaner.model.CalendarDatabase;
import com.planer.catthemeplaner.model.TodoList;
import com.planer.catthemeplaner.model.TodoModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TodoFragment extends Fragment implements OnBackPressedListener {
    @BindView(R.id.todoLayout)
    ConstraintLayout todoLayout;
    @BindView(R.id.circleProgressBar)
    CircleProgressBar circleProgressBar;
    @BindView(R.id.goalNumberProgressbar)
    ProgressBar goalNumberProgressbar;
    @BindView(R.id.goalNumber)
    TextView goalNumber;
    @BindView(R.id.pointProgressBar)
    ProgressBar pointProgressBar;
    @BindView(R.id.pointNumber)
    TextView pointNumber;
    @BindView(R.id.todoInput)
    EditText todoInput;
    @BindView(R.id.todoMemoPage)
    ConstraintLayout todoMemoPage;
    @BindView(R.id.completeListBtn)
    Button completeListBtn;
    @BindView(R.id.getPointBtn)
    Button getPointBtn;
    @BindView(R.id.floatBtn)
    FloatingActionButton floatBtn;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.advertisementBtn)
    Button advertisementBtn;
    @BindView(R.id.infoBtn)
    Button infoBtn;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioRankA)
    RadioButton radioRankA;
    @BindView(R.id.radioRankB)
    RadioButton radioRankB;
    @BindView(R.id.radioRankC)
    RadioButton radioRankC;
    @BindView(R.id.radioRankD)
    RadioButton radioRankD;
    @BindView(R.id.titleTextView)
    TextView titleTextView;


    private static final String TAG = "TodoFragment";
    private TodoListAdapter todoAdapter;
    private TodoListAdapter completeAdapter;
    private TodoViewPagerAdapter pagerAdapter;

    private Animation translateTopAnim;
    private Animation translateBottomAnim;

    private boolean isPageOpen = false;
    private long backKeyPressedTime;
    private MainActivity activity;

    private SharedPreferences prefTheme;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Handler handler = new Handler();
    private TodoSomethingFragment todoSomethingFragment;
    private CompleteFragment completeFragment;

    private String rank;
    private RewardedVideoAd mRewardVideoAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
        ButterKnife.bind(this, rootView);

        init();
        adSet();
        setCustomTheme();

        viewPager.setOffscreenPageLimit(2);
        pagerAdapter = new TodoViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addItem(todoSomethingFragment);
        pagerAdapter.addItem(completeFragment);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(viewPagerListener);
        radioGroup.setOnCheckedChangeListener(radioListener);


        final TodoModel todoModel = new TodoModel(getContext());
        buttonEvent(todoModel);
        setTodoList(todoModel);
        showTodayAchievementRate(todoModel);


        SlidingMemoPageAnimationListener animListener = new SlidingMemoPageAnimationListener();
        translateTopAnim.setAnimationListener(animListener);
        translateBottomAnim.setAnimationListener(animListener);


        todoAdapter.setOnDeleteListener(new OnDeleteListener() {
            @Override
            public void onDeleteButtonListener(int position) {
                TodoList todoList = todoAdapter.getItem(position);
                todoModel.deleteTodoInfo(todoList, CalendarDatabase.TABLE_TODO);
                todoAdapter.notifyDataSetChanged();
                todoSomethingFragment.deleteItemToAdapter(todoList, position);
                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        completeAdapter.setOnDeleteListener(new OnDeleteListener() {
            @Override
            public void onDeleteButtonListener(int position) {
                TodoList completeTodoInfo = completeAdapter.getItem(position);
                todoModel.deleteTodoInfo(completeTodoInfo, CalendarDatabase.TABLE_COMPLETE_TODO);
                completeAdapter.notifyDataSetChanged();
                completeFragment.deleteItemToAdapter(completeTodoInfo, position);
                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        todoAdapter.setOnTodoCheckListener(new OnTodoCheckListener() {
            @Override
            public void onTodoCheckListener(boolean isChecked, int position) {
                TodoList item = todoAdapter.getItem(position);
                if (isChecked) {

                    completeAdapter.addItem(item);
                    todoAdapter.removeItem(item);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            todoAdapter.notifyDataSetChanged();
                            completeAdapter.notifyDataSetChanged();
                        }
                    }, 400);
                    todoModel.saveTodoInfo(item, "COMPLETE_TODO", "SHARED_NUMBER", CalendarDatabase.TABLE_COMPLETE_TODO);
                    todoModel.deleteTodoInfo(item, CalendarDatabase.TABLE_TODO);

                    editor.putInt("todoCount", todoAdapter.getItemCount()).commit();
                    editor.putInt("completeCount", completeAdapter.getItemCount()).commit();
                    showTodayAchievementRate(todoModel);
                }
            }
        });

        completeAdapter.setOnTodoCheckListener(new OnTodoCheckListener() {
            @Override
            public void onTodoCheckListener(boolean isChecked, int position) {
                TodoList item = completeAdapter.getItem(position);

                if (isChecked) {
                    todoAdapter.addItem(item);
                    completeAdapter.removeItem(item);

                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            todoAdapter.notifyDataSetChanged();
                            completeAdapter.notifyDataSetChanged();
                        }
                    }, 400);

                    todoModel.saveTodoInfo(item, "TODO", "SHARED_NUMBER", CalendarDatabase.TABLE_TODO);
                    todoModel.deleteTodoInfo(item, CalendarDatabase.TABLE_COMPLETE_TODO);

                    editor.putInt("todoCount", todoAdapter.getItemCount()).commit();
                    editor.putInt("completeCount", completeAdapter.getItemCount()).commit();
                    showTodayAchievementRate(todoModel);
                }
            }
        });

        return rootView;
    }


    public void init() {
        translateTopAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_top);
        translateBottomAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_bottom);
        activity = (MainActivity) getActivity();
        todoAdapter = new TodoListAdapter(getContext());
        completeAdapter = new TodoListAdapter(getContext());

        circleProgressBar.setMax(100);
        goalNumberProgressbar.setMax(100);
        pointProgressBar.setMax(100);

        prefTheme = getContext().getSharedPreferences("ThemeData", getContext().MODE_PRIVATE);
        pref = getContext().getSharedPreferences("TodoData", getContext().MODE_PRIVATE);
        editor = pref.edit();

        todoSomethingFragment = new TodoSomethingFragment(todoAdapter);
        completeFragment = new CompleteFragment(completeAdapter);
    }

    public void setCustomTheme() {
        String themeName = prefTheme.getString("currentTheme", "기본테마");

        if (!themeName.equals("noTheme")) {
            if(themeName.equals("기본테마")){
                todoLayout.setBackgroundColor(getResources().getColor(R.color.mainColor));
                floatBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.mainColor)));
                todoMemoPage.setBackground(getResources().getDrawable(R.drawable.layout_back_main));
            }
            else if (themeName.equals("첫번째")) {
                todoLayout.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                floatBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorOrange)));
                todoMemoPage.setBackground(getResources().getDrawable(R.drawable.layout_back_main1));
            } else if (themeName.equals("두번째")) {
                todoLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                floatBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorYellow)));
                todoMemoPage.setBackground(getResources().getDrawable(R.drawable.layout_back_main2));
            } else if (themeName.equals("세번째")) {
                todoLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                floatBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorGreen)));
                todoMemoPage.setBackground(getResources().getDrawable(R.drawable.layout_back_main3));
            } else if (themeName.equals("네번째")) {
                todoLayout.setBackgroundColor(getResources().getColor(R.color.colorPink));
                floatBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPink)));
                todoMemoPage.setBackground(getResources().getDrawable(R.drawable.layout_back_main4));
            }
        }
    }

    public void buttonEvent(final TodoModel todoModel) {
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPageOpen) {
                } else {
                    todoInput.setVisibility(View.VISIBLE);
                    todoMemoPage.setVisibility(View.VISIBLE);
                    todoMemoPage.startAnimation(translateTopAnim);
                    floatBtn.setVisibility(View.GONE);

                    radioGroup.check(R.id.radioRankA);
                }
            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sharedNumber = 0;

                sharedNumber = pref.getInt("sharedNumber", -1);


                Log.d(TAG, " todoInput.getText().toString() : " + todoInput.getText().toString());
                Log.d(TAG, " SharedNumber : " + pref.getInt("sharedNumber", -1));


                todoSomethingFragment.addItemToAdapter(new TodoList(todoInput.getText().toString(), sharedNumber, rank));

                todoModel.saveTodoInfo(new TodoList(todoInput.getText().toString(), sharedNumber, rank), "TODO", "SHARED_NUMBER", CalendarDatabase.TABLE_TODO);
                todoInput.setText(null);

                sharedNumber += 1;

                editor.putInt("sharedNumber", sharedNumber).commit();
                editor.putInt("todoCount", todoAdapter.getItemCount()).commit();
                showTodayAchievementRate(todoModel);

                closeListView();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeListView();
            }
        });

        getPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int completeCount = pref.getInt("completeCount", 0);
                int totalPoint = completeCount + pref.getInt("totalPoint", 0);

                if (totalPoint > 100) {
                    totalPoint = 100;
                    //editor.remove("totalPoint").commit();
                    editor.putInt("totalPoint", 100).commit();

                }
                editor.putInt("totalPoint", totalPoint).commit();

                if (completeCount != -1) {
                    pointNumber.setText(totalPoint + "/100");
                    pointProgressBar.setProgress(totalPoint);

                    Toast.makeText(getContext(), completeCount + " 포인트 적립이다옹", Toast.LENGTH_SHORT).show();

                    ArrayList<TodoList> completeList = todoModel.loadCompleteList(CalendarDatabase.TABLE_COMPLETE_TODO);

                    for (TodoList todoList : completeList) {
                        todoModel.saveCompleteList(todoList);
                    }

                    todoModel.deleteTodoInfo(CalendarDatabase.TABLE_COMPLETE_TODO);
                    completeAdapter.itemsClear();
                    completeAdapter.notifyDataSetChanged();

                    editor.remove("completeCount").commit();
                    showTodayAchievementRate(todoModel);
                }
                showTodayAchievementRate(todoModel);
            }
        });

        completeListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), CompleteListActivity.class);
                startActivity(intent);

            }
        });

        advertisementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAdvertisement();
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), InfoActivity.class);
                startActivity(intent);

            }
        });

    }

    public void closeListView() {
        try {
            //메모 페이지에서 확인 버튼을 누르면 키패드 자동 제거
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(todoInput.getWindowToken(), 0);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isPageOpen) {
                        todoMemoPage.startAnimation(translateBottomAnim);
                        floatBtn.setVisibility(View.VISIBLE);
                        todoInput.setVisibility(View.GONE);
                    }
                }
            }, 100);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    public void setTodoList(TodoModel todoModel) {
        todoAdapter.itemsClear();
        todoAdapter.notifyDataSetChanged();

        ArrayList<TodoList> todoLists = todoModel.loadTodoList("TODO", CalendarDatabase.TABLE_TODO);
        todoAdapter.setItems(todoLists);
        todoAdapter.notifyDataSetChanged();

        ArrayList<TodoList> completeLists = todoModel.loadTodoList("COMPLETE_TODO", CalendarDatabase.TABLE_COMPLETE_TODO);
        completeAdapter.setItems(completeLists);
        completeAdapter.notifyDataSetChanged();
    }

    public void showTodayAchievementRate(TodoModel todoModel) {

        int todoCount = pref.getInt("todoCount", 0);
        int completeCount = pref.getInt("completeCount", 0);
        Log.d(TAG, "todoCount : " + todoCount + ",  completeCount : " + completeCount);


        if ((todoCount + completeCount) == 0) {
            circleProgressBar.setProgress(0);
        } else {
            float progressRate = ((float) completeCount / (float) (todoCount + completeCount)) * 100;
            circleProgressBar.setProgress((int) progressRate);
        }

        //포인트 프로그레스바
        int totalPoint = pref.getInt("totalPoint", 0);
        if (totalPoint != 0) {
            pointProgressBar.setProgress(totalPoint);
            pointNumber.setText(totalPoint + "/100");
        } else {
            pointProgressBar.setProgress(0);
        }

        //총 달성 횟수 프로그레스바
        ArrayList<TodoList> completeList = todoModel.loadCompleteList(CalendarDatabase.TABLE_COMPLETE_LIST);
        int total = completeList.size();
        //달성횟수가 100회가 넘으면 데이터를 삭제
        if (completeList.size() > 100) {
            total = total - 100;
            todoModel.deleteTodoInfo(CalendarDatabase.TABLE_COMPLETE_LIST);

        }
        goalNumberProgressbar.setProgress(total);
        goalNumber.setText(total + "/100");
    }


    private RadioGroup.OnCheckedChangeListener radioListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (radioRankA.isChecked()) {
                rank = "A";
            } else if (radioRankB.isChecked()) {
                rank = "B";
            } else if (radioRankC.isChecked()) {
                rank = "C";
            } else if (radioRankD.isChecked()) {
                rank = "D";
            }
        }


    };

    public void adSet() {
        MobileAds.initialize(getContext(), getResources().getString(R.string.reward_ad_unit_id_for_test));
        mRewardVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());

        mRewardVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
            }

            @Override
            public void onRewardedVideoAdOpened() {
            }

            @Override
            public void onRewardedVideoStarted() {
            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                int totalPoint = pref.getInt("totalPoint", 0);
                totalPoint += 5;

                if (totalPoint > 100) {
                    totalPoint = 100;
                    //editor.remove("totalPoint").commit();
                    editor.putInt("totalPoint", 100).commit();

                }
                editor.putInt("totalPoint", totalPoint).commit();


                pointNumber.setText(totalPoint + "/100");
                pointProgressBar.setProgress(totalPoint);

                Toast.makeText(getContext(), "5 포인트 적립이다옹", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
            }

            @Override
            public void onRewardedVideoCompleted() {
                loadRewardVideoAd();
            }
        });
        loadRewardVideoAd();
    }

    private void loadRewardVideoAd() {
        mRewardVideoAd.loadAd(getResources().getString(R.string.reward_ad_unit_id_for_test), new AdRequest.Builder().build());
    }

    public void loadAdvertisement() {
        if (mRewardVideoAd.isLoaded()) {
            mRewardVideoAd.show();
        }
    }

    private class SlidingMemoPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                todoMemoPage.setVisibility(View.GONE);
                todoInput.setText(null);
                isPageOpen = false;

            } else {
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            if (position == 0) {
                titleTextView.setText("To Do Something");

            } else if (position == 1) {
                titleTextView.setText("Complete");
            }
        }


    };

    @Override
    public void onBackPressed() {
        if (isPageOpen) {
            closeListView();
        }
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setOnBackPressedListener(this);
    }

}

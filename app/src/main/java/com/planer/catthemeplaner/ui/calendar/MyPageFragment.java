package com.planer.catthemeplaner.ui.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.planer.catthemeplaner.controller.CirclePagerIndicatorDecoration;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.SelectThemeActivity;
import com.planer.catthemeplaner.adapter.MyPageAdapter;
import com.planer.catthemeplaner.listener.OnShowThemeListener;
import com.planer.catthemeplaner.model.ThemeList;
import com.planer.catthemeplaner.model.ThemeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyPageFragment extends Fragment {
    @BindView(R.id.MyPageRecyclerView)
    RecyclerView MyPageRecyclerView;
    @BindView(R.id.MyPagePointTextView)
    TextView MyPagePointTextView;
    @BindView(R.id.currentThemeName)
    TextView currentThemeName;

    private static final String TAG = "MyPageFragment";
    private MyPageAdapter myPageAdapter;
    private ThemeModel themeModel;
    private ArrayList<String> items;
    private PagerSnapHelper snapHelper;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, rootView);

        init();
        loadTheme();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals("첫번째")) {
                Log.d(TAG, "첫번째");
                myPageAdapter.addItem(new ThemeList(R.drawable.exam1_1, "첫번째"));
            } else if (items.get(i).equals("두번째")) {
                myPageAdapter.addItem(new ThemeList(R.drawable.exam2_1, "두번째"));
                Log.d(TAG, "두번째");
            } else if (items.get(i).equals("세번째")) {
                myPageAdapter.addItem(new ThemeList(R.drawable.exam3_1, "세번째"));
                Log.d(TAG, "세번째");
            } else if (items.get(i).equals("네번째")) {
                myPageAdapter.addItem(new ThemeList(R.drawable.exam4_1, "네번째"));
                Log.d(TAG, "네번째");
            }
        }
        myPageAdapter.addItem(new ThemeList(R.drawable.exam_1, "기본테마"));
        MyPageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(MyPageRecyclerView);
        MyPageRecyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());
        MyPageRecyclerView.setAdapter(myPageAdapter);
        MyPageRecyclerView.setOnScrollListener(scrollListener);

        currentThemeName.setText(myPageAdapter.getItem(0).getTitle());


        myPageAdapter.setOnShowThemeListener(new OnShowThemeListener() {
            @Override
            public void onShowTheme(int position) {
                Intent intent = new Intent(getContext(), SelectThemeActivity.class);
                intent.putExtra("themeName", myPageAdapter.getItem(position).getTitle());
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void init() {
        myPageAdapter = new MyPageAdapter();
        themeModel = new ThemeModel(getContext());
        items = new ArrayList<>();

        pref = getContext().getSharedPreferences("TodoData", getContext().MODE_PRIVATE);
        editor = pref.edit();

        int totalPoint = pref.getInt("totalPoint", -1);
        MyPagePointTextView.setText(String.valueOf(totalPoint) + " P");
    }

    public void loadTheme() {
        items = themeModel.loadThemeList();
        Log.d(TAG, "LOAD  : " + items.size());
    }


    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            View snapView = snapHelper.findSnapView(layoutManager);

            if (layoutManager.getPosition(snapView) == 0) {
                currentThemeName.setText(myPageAdapter.getItem(0).getTitle());
            } else if (layoutManager.getPosition(snapView) == 1) {
                currentThemeName.setText(myPageAdapter.getItem(1).getTitle());
            } else if (layoutManager.getPosition(snapView) == 2) {
                currentThemeName.setText(myPageAdapter.getItem(2).getTitle());
            } else if (layoutManager.getPosition(snapView) == 3) {
                currentThemeName.setText(myPageAdapter.getItem(3).getTitle());
            }


        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

        }
    };

}

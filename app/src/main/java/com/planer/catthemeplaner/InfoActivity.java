package com.planer.catthemeplaner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.planer.catthemeplaner.adapter.InfoViewPager;
import com.planer.catthemeplaner.ui.calendar.InfoFragment1;
import com.planer.catthemeplaner.ui.calendar.InfoFragment2;
import com.planer.catthemeplaner.ui.calendar.InfoFragment3;
import com.planer.catthemeplaner.ui.calendar.InfoFragment4;
import com.planer.catthemeplaner.ui.calendar.InfoFragment5;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
    @BindView(R.id.infoViewPager)
    ViewPager InfoViewPager;

    private com.planer.catthemeplaner.adapter.InfoViewPager adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ButterKnife.bind(this);
        init();

        InfoFragment1 infoFragment1 = new InfoFragment1();
        InfoFragment2 infoFragment2 = new InfoFragment2();
        InfoFragment3 infoFragment3 = new InfoFragment3();
        InfoFragment4 infoFragment4 = new InfoFragment4();
        InfoFragment5 infoFragment5 = new InfoFragment5();

        adapter.addItem(infoFragment1);
        adapter.addItem(infoFragment2);
        adapter.addItem(infoFragment3);
        adapter.addItem(infoFragment4);
        adapter.addItem(infoFragment5);
        InfoViewPager.setAdapter(adapter);
    }

    public void init() {
        adapter = new InfoViewPager(getSupportFragmentManager());
    }
}

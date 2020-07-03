package com.planer.catthemeplaner.ui.calendar;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.planer.catthemeplaner.MainActivity;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.adapter.StoreViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoreFragment extends Fragment {
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.storeViewPager)
    ViewPager storeViewPager;



    private ShoppingFragment shoppingFragment;
    private MyPageFragment myPageFragment;
    private StoreViewPager adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_store, container, false);
        ButterKnife.bind(this, rootView);

        init();
        buttonEvent();
        
        adapter.addItem(myPageFragment);
        adapter.addItem(shoppingFragment);

        storeViewPager.setOffscreenPageLimit(2);
        storeViewPager.setAdapter(adapter);
        storeViewPager.setOnPageChangeListener(viewPagerListener);


        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(storeViewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selectedFragment = null;
                if (position == 0) {
                    selectedFragment = new ShoppingFragment();
                } else if (position == 1) {
                    selectedFragment = new MyPageFragment();
                }
                storeViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootView;
    }

    public void init() {
        shoppingFragment = new ShoppingFragment();
        myPageFragment = new MyPageFragment();
        adapter = new StoreViewPager(getChildFragmentManager());
    }

    public void buttonEvent() {

    }

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            if (position == 0) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.detach(myPageFragment).attach(myPageFragment).commit();
            }
        }


    };



}

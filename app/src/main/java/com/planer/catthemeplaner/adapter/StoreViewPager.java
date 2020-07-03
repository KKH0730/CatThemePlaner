package com.planer.catthemeplaner.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class StoreViewPager extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> items = new ArrayList<>();

    public StoreViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment item){
        items.add(item);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position ==0){
            return "마이 페이지";
        } else {
            return "스토어";
        }
    }

}

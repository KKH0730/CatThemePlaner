package com.planer.catthemeplaner.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> items = new ArrayList<>();

    public CalendarViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment item){
        items.add(item);
    }
    public void addItem2(Fragment item, int position){
        items.add(position, item);
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


    public void removeItem(int position){
        items.remove(position);
    }

    @Override

    public int getItemPosition(Object object) {

        return POSITION_NONE;

    }

    public void clearItem(){
        items.clear();
    }

}

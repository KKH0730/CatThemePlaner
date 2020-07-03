package com.planer.catthemeplaner.model;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

public class MemoItem extends ViewModel {
    private int iconDrawable;
    private String menuName;


    public MemoItem(){
    }

    public MemoItem(int iconDrawable, String menuName) {
        this.iconDrawable = iconDrawable;
        this.menuName = menuName;
    }

    public int getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}

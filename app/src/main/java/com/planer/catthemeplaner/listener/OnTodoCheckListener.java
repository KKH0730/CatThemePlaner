package com.planer.catthemeplaner.listener;

import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

public interface OnTodoCheckListener {
    public void onTodoCheckListener( boolean isChecked, int position);
}

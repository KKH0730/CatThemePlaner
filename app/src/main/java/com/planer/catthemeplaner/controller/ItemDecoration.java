package com.planer.catthemeplaner.controller;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int size10;
    private int size2;

    public ItemDecoration(Context context) {
        size10 = dpToPx(context, 1);
        size2 = dpToPx(context, 0);
    }

    // dp -> pixel 단위로 변경
    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);


        outRect.top = size2;
        outRect.bottom = size2;

        outRect.left = size2;
        outRect.right = size2;
    }
}

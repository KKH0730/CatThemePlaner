package com.planer.catthemeplaner.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.planer.catthemeplaner.StoreShowExample;

public class ShoppingListViewClickListener implements View.OnClickListener {
    private Context context;
    private int preViewImage;
    private String title;
    private String textDescription;

    public ShoppingListViewClickListener(Context context, int preViewImage, String title, String textDescription) {
        this.context = context;
        this.preViewImage = preViewImage;
        this.title = title;
        this.textDescription = textDescription;
    }


    @Override
    public void onClick(View v) {
        if(title.equals("첫번째")){
            Intent intent = new Intent(context, StoreShowExample.class);
            intent.putExtra("preViewImage", preViewImage);
            intent.putExtra("title", title);
            Log.d("ShoppingListViewClickListener", "intent : " + intent.getStringExtra("title"));
            intent.putExtra("textDescription", textDescription);
            context.startActivity(intent);
        }

        if(title.equals("두번째")){
            Intent intent = new Intent(context, StoreShowExample.class);
            intent.putExtra("preViewImage", preViewImage);
            intent.putExtra("title", title);
            intent.putExtra("textDescription", textDescription);
            context.startActivity(intent);
        }

        if(title.equals("세번째")){
            Intent intent = new Intent(context, StoreShowExample.class);
            intent.putExtra("preViewImage", preViewImage);
            intent.putExtra("title", title);
            intent.putExtra("textDescription", textDescription);
            context.startActivity(intent);
        }

        if(title.equals("네번째")){
            Intent intent = new Intent(context, StoreShowExample.class);
            intent.putExtra("preViewImage", preViewImage);
            intent.putExtra("title", title);
            intent.putExtra("textDescription", textDescription);
            context.startActivity(intent);
        }

    }
}

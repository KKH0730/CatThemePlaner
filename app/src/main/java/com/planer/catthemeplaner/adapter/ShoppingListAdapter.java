package com.planer.catthemeplaner.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.model.MemoItem;
import com.planer.catthemeplaner.model.StoreList;
import com.planer.catthemeplaner.ui.calendar.ListViewClickListener;
import com.planer.catthemeplaner.ui.calendar.ShoppingListViewClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingListAdapter extends BaseAdapter {
    @BindView(R.id.previewImage)
    ImageView previewImage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.textDescription)
    TextView textDescription;
    @BindView(R.id.listItem)
    ConstraintLayout listItem;


    private Context context;
    private ArrayList<StoreList> items = new ArrayList<StoreList>();


    public ShoppingListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(StoreList item) {
        items.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.store_list, parent, false);
        ButterKnife.bind(this, rootView);
        listItem.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));

        previewImage.setImageResource(items.get(position).getImage());
        title.setText(items.get(position).getContent());
        textDescription.setText(items.get(position).getDescription());

        setListener(listItem, position);

        return rootView;
    }



    public void setListener(ConstraintLayout listItem, int position) {
        ShoppingListViewClickListener listViewClickListener = new ShoppingListViewClickListener(context, items.get(position).getImage(),items.get(position).getContent(),items.get(position).getDescription());
        listItem.setOnClickListener(listViewClickListener);
    }


}

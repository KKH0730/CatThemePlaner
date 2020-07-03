package com.planer.catthemeplaner.ui.calendar;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.adapter.ShoppingAdapter;
import com.planer.catthemeplaner.adapter.ShoppingListAdapter;
import com.planer.catthemeplaner.model.ShoppingList;
import com.planer.catthemeplaner.model.StoreList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShoppingFragment extends Fragment {
    @BindView(R.id.shoppingListView)
    ListView listView;
    @BindView(R.id.shoppingLayout)
    ConstraintLayout shoppingLayout;

    private ShoppingListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_shopping, container, false);
        ButterKnife.bind(this,rootView);
        init();


        adapter.addItem(new StoreList(R.drawable.importance1, "첫번째", "첫번째 description 입니다."));
        adapter.addItem(new StoreList(R.drawable.importance2, "두번째", "두번째 description 입니다."));
        adapter.addItem(new StoreList(R.drawable.importance3, "세번째", "세번째 description 입니다."));
        adapter.addItem(new StoreList(R.drawable.importance4, "네번째", "네번째 description 입니다."));

        listView.setAdapter(adapter);



        return rootView;
    }

    public void init(){
        adapter = new ShoppingListAdapter(getContext());

    }

}

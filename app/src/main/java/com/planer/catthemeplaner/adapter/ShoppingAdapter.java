package com.planer.catthemeplaner.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.model.ShoppingList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//storeShowExample.activity
//shoppingListViewClickListener
public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder>{
    private static final String TAG = "ShoppingAdapter";
    ArrayList<ShoppingList> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list, parent, false);


        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingList item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ShoppingList item){
        items.add(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.theme1)
        ImageView theme1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(ShoppingList item){
            Log.d(TAG, "ITEM : " + item.getImage());
            theme1.setImageResource(item.getImage());


        }
    }
}

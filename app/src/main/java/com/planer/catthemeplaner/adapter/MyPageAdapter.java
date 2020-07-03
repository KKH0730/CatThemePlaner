package com.planer.catthemeplaner.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.listener.OnShowThemeListener;
import com.planer.catthemeplaner.model.ThemeList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyPageAdapter extends RecyclerView.Adapter<MyPageAdapter.ViewHolder> {
    private ArrayList<ThemeList> items = new ArrayList<>();
    private OnShowThemeListener onShowThemeListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_theme, parent, false);

        return new ViewHolder(rootView, onShowThemeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThemeList item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ThemeList item) {
        items.add(item);
    }

    public ThemeList getItem(int position){
        return items.get(position);
    }

    public void setOnShowThemeListener(OnShowThemeListener onShowThemeListener) {
        this.onShowThemeListener = onShowThemeListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.theme)
        ImageView theme;



        public ViewHolder(@NonNull View itemView, final OnShowThemeListener onShowThemeListener) {
            super(itemView);


            ButterKnife.bind(this, itemView);
            theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onShowThemeListener.onShowTheme(position);
                }
            });

        }

        public void bind(ThemeList item) {
            Log.d("MyPageFragment", "item.getImage()  : " + item.getImage());
            theme.setImageResource(item.getImage());

        }

    }
}

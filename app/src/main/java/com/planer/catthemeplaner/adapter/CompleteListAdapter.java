package com.planer.catthemeplaner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.model.TodoList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteListAdapter extends RecyclerView.Adapter<CompleteListAdapter.ViewHolder> {
    ArrayList<TodoList> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_list_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteListAdapter.ViewHolder holder, int position) {
        TodoList item = items.get(position);
        holder.bind(item, position);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<TodoList> items) {
        this.items = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.order)
        TextView order;
        @BindView(R.id.completeListRank)
        TextView completeListRank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(TodoList item, int position) {
            order.setText(String.valueOf((position + 1)));
            completeListRank.setText(item.getRank());
            content.setText(item.getContent());

        }
    }
}
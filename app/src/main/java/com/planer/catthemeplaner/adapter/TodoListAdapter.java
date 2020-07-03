package com.planer.catthemeplaner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.planer.catthemeplaner.listener.OnDeleteListener;
import com.planer.catthemeplaner.listener.OnTodoCheckListener;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.model.TodoList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {
    private ArrayList<TodoList> items = new ArrayList<>();
    private Context context;
    private OnDeleteListener deleteListener;
    private OnTodoCheckListener checkListener;


    public TodoListAdapter(Context context) {
        this.context = this.context;
    }

    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_row, parent, false);
        return new TodoListViewHolder(rootView, deleteListener, checkListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        TodoList item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<TodoList> getItems() {
        return items;
    }

    public void setItems(ArrayList<TodoList> items) {
        this.items = items;
    }

    public TodoList getItem(int position) {
        return items.get(position);
    }

    public void itemsClear(){
        items.clear();
    }

    public void addItem(TodoList item) {
        items.add(item);
    }

    public void removeItem(int position){
        items.remove(position);
    }

    public void addPosition(int position, TodoList todoList){
        items.add(position, todoList);
    }


    public void removeItem(TodoList todoList){
        items.remove(todoList);
    }

    public void setOnDeleteListener(OnDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setOnTodoCheckListener(OnTodoCheckListener checkListener) {
        this.checkListener = checkListener;
    }


    public class TodoListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contentTextView)
        TextView contentTextView;
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.deleteTodo)
        ImageView deleteTodo;
        @BindView(R.id.layout)
        ConstraintLayout layout;
        @BindView(R.id.rankStr)
        TextView rankStr;


        public TodoListViewHolder(@NonNull View itemView, OnDeleteListener deleteListener, OnTodoCheckListener checkListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TodoList item, int position) {
            if(item.getRank().equals("A")){
                rankStr.setTextColor(Color.RED);
                rankStr.setPaintFlags(rankStr.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
            } else if (item.getRank().equals("B")){
                rankStr.setTextColor(Color.BLACK);
            } else if (item.getRank().equals("C")){
                rankStr.setTextColor(Color.BLACK);
            } else if (item.getRank().equals("D")){
                rankStr.setTextColor(Color.BLACK);
            }
            rankStr.setText("  |  " + item.getRank() + "  |  ");

            contentTextView.setText(item.getContent());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                        checkListener.onTodoCheckListener(isChecked, position);

                }
            });

            deleteTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    deleteListener.onDeleteButtonListener(position);
                }
            });

            checkBox.setChecked(false);
        }
    }

}

package com.planer.catthemeplaner.ui.calendar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.adapter.TodoListAdapter;
import com.planer.catthemeplaner.model.TodoList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TodoSomethingFragment extends Fragment {
    private static final String TAG = "TodoSomethingFragment";
    @BindView(R.id.todoListRecyclerView)
    RecyclerView todoListRecyclerView;


    private TodoListAdapter todoListAdapter;

    public TodoSomethingFragment(TodoListAdapter todoListAdapter) {
        this.todoListAdapter = todoListAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_todo_something, container, false);
        ButterKnife.bind(this, rootView);

        todoListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todoListRecyclerView.setAdapter(todoListAdapter);


        return rootView;
    }


    public void addItemToAdapter(TodoList item) {
        if (item != null) {
            Log.d(TAG, "item.getContent() " + item.getContent() + ",  shw" + item.getSharedNumber());
            todoListAdapter.addItem(item);
            todoListAdapter.notifyDataSetChanged();
        } else {

        }
    }

    public void deleteItemToAdapter(TodoList item, int position) {
        if (item != null) {

            todoListAdapter.removeItem(position);
            todoListAdapter.notifyDataSetChanged();
        } else {

        }
    }


}

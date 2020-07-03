package com.planer.catthemeplaner.ui.calendar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.adapter.TodoListAdapter;
import com.planer.catthemeplaner.model.TodoList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CompleteFragment extends Fragment {
    @BindView(R.id.completeListRecyclerView)
    RecyclerView completeListRecyclerView;

    private TodoListAdapter completeAdapter;

    public CompleteFragment(TodoListAdapter completeAdapter) {
        this.completeAdapter = completeAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_complete, container, false);
        ButterKnife.bind(this, rootView);


        completeListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        completeListRecyclerView.setAdapter(completeAdapter);

        return rootView;
    }
    public void deleteItemToAdapter(TodoList item, int position) {
        if (item != null) {

            completeAdapter.removeItem(position);
            completeAdapter.notifyDataSetChanged();
        } else {

        }
    }

}

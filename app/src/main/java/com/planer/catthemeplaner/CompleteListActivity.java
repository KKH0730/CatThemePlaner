package com.planer.catthemeplaner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.planer.catthemeplaner.adapter.CompleteListAdapter;
import com.planer.catthemeplaner.model.CalendarDatabase;
import com.planer.catthemeplaner.model.TodoList;
import com.planer.catthemeplaner.model.TodoModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteListActivity extends AppCompatActivity {
    private static final String TAG = "CompleteList";

    @BindView(R.id.completeListRecyclerView)
    RecyclerView completeListRecyclerView;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.completeListLayout)
    ConstraintLayout completeListLayout;

    private SharedPreferences prefTheme;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_list);

        ButterKnife.bind(this);

        prefTheme = this.getSharedPreferences("ThemeData", this.MODE_PRIVATE);
        editor = prefTheme.edit();

        setCustomTheme();

        completeListRecyclerView.addItemDecoration(new DividerItemDecoration(this, new LinearLayoutManager(this).getOrientation()));
        completeListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        TodoModel todoModel = new TodoModel(this);
        ArrayList<TodoList> item = todoModel.loadCompleteList(CalendarDatabase.TABLE_COMPLETE_LIST);

        CompleteListAdapter adapter = new CompleteListAdapter();
        adapter.setItems(item);
        completeListRecyclerView.setAdapter(adapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setCustomTheme() {
        String themeName = prefTheme.getString("currentTheme", "기본테마");


        if (themeName.equals("기본테마")) {
            completeListLayout.setBackgroundColor(getResources().getColor(R.color.mainColor));
        } else if (themeName.equals("첫번째")) {
            completeListLayout.setBackgroundColor(getResources().getColor(R.color.colorOrange));
        } else if (themeName.equals("두번째")) {
            completeListLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));
        } else if (themeName.equals("세번째")) {
            completeListLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        } else if (themeName.equals("네번째")) {
            completeListLayout.setBackgroundColor(getResources().getColor(R.color.colorPink));
        }
    }


}

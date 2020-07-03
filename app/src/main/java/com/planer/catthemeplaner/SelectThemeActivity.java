package com.planer.catthemeplaner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectThemeActivity extends AppCompatActivity {
    @BindView(R.id.selectThemeLayout)
    ConstraintLayout selectThemeLayout;
    @BindView(R.id.themeTitle)
    TextView themeTitle;
    @BindView(R.id.themeConfirmBtn)
    Button themeConfirmBtn;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_theme);

        ButterKnife.bind(this);
        init();
        buttonEvent();

        Intent intent = getIntent();
        if(intent.getStringExtra("themeName").equals("기본테마")){
            selectThemeLayout.setBackgroundColor(getResources().getColor(R.color.mainColor));
            themeTitle.setText(intent.getStringExtra("themeName"));
        } else if (intent.getStringExtra("themeName").equals("첫번째")) {
            selectThemeLayout.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            themeTitle.setText(intent.getStringExtra("themeName"));
        } else if (intent.getStringExtra("themeName").equals("두번째")) {
            selectThemeLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            themeTitle.setText(intent.getStringExtra("themeName"));
        } else if (intent.getStringExtra("themeName").equals("세번째")) {
            selectThemeLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            themeTitle.setText(intent.getStringExtra("themeName"));
        } else if (intent.getStringExtra("themeName").equals("네번째")) {
            selectThemeLayout.setBackgroundColor(getResources().getColor(R.color.colorPink));
            themeTitle.setText(intent.getStringExtra("themeName"));
        }
    }

    public void init() {
        pref = this.getSharedPreferences("ThemeData", this.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void buttonEvent() {
        themeConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(themeTitle.getText().toString().equals("기본테마")){
                    editor.putString("currentTheme", themeTitle.getText().toString()).commit();
                } else if(themeTitle.getText().toString().equals("첫번째")){
                    editor.putString("currentTheme", themeTitle.getText().toString()).commit();
                } else if(themeTitle.getText().toString().equals("두번째")){
                    editor.putString("currentTheme", themeTitle.getText().toString()).commit();
                } else if(themeTitle.getText().toString().equals("세번째")){
                    editor.putString("currentTheme", themeTitle.getText().toString()).commit();
                } else if(themeTitle.getText().toString().equals("네번째")){
                    editor.putString("currentTheme", themeTitle.getText().toString()).commit();
                }
                Toast.makeText(getApplicationContext(), "currentTheme : " + pref.getString("currentTheme", "-1"), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

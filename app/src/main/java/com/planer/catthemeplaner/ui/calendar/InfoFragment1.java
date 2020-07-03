package com.planer.catthemeplaner.ui.calendar;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planer.catthemeplaner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoFragment1 extends Fragment {
    @BindView(R.id.informationLayout1)
    ConstraintLayout informationLayout1;

    private SharedPreferences prefTheme;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_info1, container, false);

        ButterKnife.bind(this, rootView);

        prefTheme = getContext().getSharedPreferences("ThemeData", getContext().MODE_PRIVATE);
        editor = prefTheme.edit();

        setCustomTheme();


        return rootView;
    }

    public void setCustomTheme() {
        String themeName = prefTheme.getString("currentTheme", "기본테마");

        if (!themeName.equals("noTheme")) {
            if (themeName.equals("기본테마")) {
                informationLayout1.setBackgroundColor(getResources().getColor(R.color.mainColor));
            } else if (themeName.equals("첫번째")) {
                informationLayout1.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if (themeName.equals("두번째")) {
                informationLayout1.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            } else if (themeName.equals("세번째")) {
                informationLayout1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            } else if (themeName.equals("네번째")) {
                informationLayout1.setBackgroundColor(getResources().getColor(R.color.colorPink));
            }
        }
    }
}

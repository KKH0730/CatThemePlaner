package com.planer.catthemeplaner.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.model.MemoItem;
import com.planer.catthemeplaner.ui.calendar.ListViewClickListener;
import com.planer.catthemeplaner.model.MemoListItem;
import com.planer.catthemeplaner.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MemoListAdapter extends BaseAdapter {
    private final static String TAG = "MemoListAdapter";

    @BindView(R.id.menuName)
    TextView menuName;
    @BindView(R.id.description)
    EditText editDescription;
    @BindView(R.id.listItem)
    ConstraintLayout listItem;
    @BindView(R.id.iconDrawable)
    ImageView iconDrawable;
    @BindView(R.id.textDescription)
    TextView textDescription;
    @BindView(R.id.iconDialog)
    ImageView iconDialog;
    @BindView(R.id.iconDescription)
    ImageView iconDescription;

    private MemoListItem memoListItem;
    private ArrayList<MemoItem> items = new ArrayList<MemoItem>();
    private Context context;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public MemoListAdapter(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("CalendarData", context.MODE_PRIVATE);
        editor = pref.edit();

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.list_item, parent, false);

        ButterKnife.bind(this, rootView);

        iconDrawable.setImageResource(items.get(position).getIconDrawable());
        menuName.setText(items.get(position).getMenuName());

        //시작일 기본으로 오늘날짜를 설정
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        if (menuName.getText().equals("시작일")) {
            textDescription.setText(DateUtil.dateFormat1.format(date));
        }

        if (memoListItem != null) {
            setListItemInfo(memoListItem, textDescription, editDescription, iconDescription);
        }

        setListener(listItem, menuName, textDescription, editDescription, iconDialog, iconDescription);


        return rootView;
    }


    public void addItem(MemoItem item) {
        items.add(item);
    }

    public void setSendMemoListItem(MemoListItem memoListItem) {
        this.memoListItem = memoListItem;

    }


    public void setListener(ConstraintLayout listItem, TextView menuName, TextView textDescription, EditText description, ImageView iconDialog, ImageView iconDescription) {
        ListViewClickListener listViewClickListener = new ListViewClickListener(context, menuName.getText().toString(), textDescription, iconDescription);

        if (menuName.getText().equals("시작일") || menuName.getText().equals("종료일") || menuName.getText().equals("알    림") || menuName.getText().equals("중요도") || menuName.getText().equals("기념일")) {
            description.setVisibility(View.GONE);
            listItem.setOnClickListener(listViewClickListener);
        } else {
            iconDialog.setVisibility(View.GONE);
        }
        if (menuName.getText().equals("위    치")) {
            editDescription.addTextChangedListener(positionWatcher);
            editDescription.setHint("위치를 입력하세요.");
        }
        if (menuName.getText().equals("설    명")) {
            editDescription.addTextChangedListener(descriptionWatcher);
            editDescription.setHint("추가 설명을 입력하세요.");
        }

    }

    TextWatcher positionWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editor.putString("position", s.toString()).commit();
        }
    };

    TextWatcher descriptionWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editor.putString("description", s.toString()).commit();
        }
    };

    public void setListItemInfo(MemoListItem memoListItem, TextView textDescription, EditText editDescription, ImageView iconDescription) {
        Log.d(TAG, "Name : " + menuName.getText());
        Log.d(TAG, memoListItem.getStartDate() + " | " + memoListItem.getStartTime());
        if (menuName.getText().equals("시작일") || menuName.getText().equals("종료일")) {
            Log.d(TAG, "시작일 혹은 종료일");
            if ((menuName.getText().equals("시작일"))) {
                textDescription.setText(memoListItem.getStartDate() + "  |  " + memoListItem.getStartTime());
            } else {
                if (textDescription.getText().equals("null") ) {
                    textDescription.setText("");
                } else if(!memoListItem.getEndTime().equals("null")){
                    textDescription.setText(memoListItem.getEndDate() + "  |  " + memoListItem.getEndTime());
                }


            }
        }
        if (menuName.getText().equals("중요도")) {
            Log.d(TAG, "중요도동작");
            if (memoListItem.getImportance() == 1) {
                iconDescription.setImageResource(R.drawable.importance1);
            } else if (memoListItem.getImportance() == 2) {
                iconDescription.setImageResource(R.drawable.importance2);
            } else if (memoListItem.getImportance() == 3) {
                iconDescription.setImageResource(R.drawable.importance3);
            } else if (memoListItem.getImportance() == 4) {
                iconDescription.setImageResource(R.drawable.importance4);
            }
        }

        if (menuName.getText().equals("알    림")) {

            if (memoListItem.getAlarm() == 1) {
                textDescription.setText(" 시작시간 10분전 ");
            } else if (memoListItem.getAlarm() == 2) {
                textDescription.setText(" 시작시간 30분전 ");
            } else if (memoListItem.getAlarm() == 3) {
                textDescription.setText(" 시작시간 1시간전 ");
            } else if (memoListItem.getAlarm() == 4) {
                textDescription.setText(" 시작시간 하루전 ");
            }
        }

        if (menuName.getText().equals("위    치")) {
            if (memoListItem.getPosition().equals("null")) {
                editDescription.setText("");
            } else {
                editDescription.setText(memoListItem.getPosition());
            }
        }

        if (menuName.getText().equals("설    명")) {

            if (memoListItem.getDescription().equals("null")) {
                editDescription.setText("");
            } else {
                editDescription.setText(memoListItem.getDescription());
            }
        }
    }

}





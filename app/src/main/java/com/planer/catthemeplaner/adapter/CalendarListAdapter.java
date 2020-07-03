package com.planer.catthemeplaner.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.listener.OnPopupMenuItemClickListener;
import com.planer.catthemeplaner.listener.OnViewHolderItemClickListener;
import com.planer.catthemeplaner.model.MemoListItem;
import com.planer.catthemeplaner.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarListAdapter extends RecyclerView.Adapter<CalendarListAdapter.viewHolder> {
    private static final String TAG = "CalendarListAdapter";
    private ArrayList<MemoListItem> items = new ArrayList<>();
    private OnPopupMenuItemClickListener onPopupMenuItemClickListener;
    private Context context;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;


    public CalendarListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.memo_list, parent, false);

        return new viewHolder(rootView, onPopupMenuItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        MemoListItem item = items.get(position);
        holder.bind(item, position, selectedItems);
        holder.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {
                if (selectedItems.get(position)) {
                    selectedItems.delete(position);
                } else {
                    selectedItems.delete(prePosition);
                    selectedItems.put(position, true);
                }
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                prePosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public MemoListItem getItem(int position) {
        return items.get(position);
    }

    public void setItems(ArrayList<MemoListItem> items) {
        this.items = items;
    }

    public ArrayList<MemoListItem> getItems() {
        return items;
    }



    public void setOnPopupMenuClickListener(OnPopupMenuItemClickListener onPopupMenuItemClickListener) {
        this.onPopupMenuItemClickListener = onPopupMenuItemClickListener;
    }



    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;
        @BindView(R.id.frontPage)
        ConstraintLayout frontPage;
        @BindView(R.id.behindPage)
        ConstraintLayout behindPage;
        @BindView(R.id.startDateText)
        TextView startDateText;
        @BindView(R.id.startTimeText2)
        TextView startDateText2;
        @BindView(R.id.startTimeText)
        TextView startTimeText;
        @BindView(R.id.endDateText)
        TextView endDateText;
        @BindView(R.id.endTimeText)
        TextView endTimeText;

        @BindView(R.id.contentText)
        TextView contentText;
        @BindView(R.id.positionText)
        TextView positionText;
        @BindView(R.id.importanceImage)
        ImageView importanceImage;
        @BindView(R.id.alarmImage)
        ImageView alarmImage;
        @BindView(R.id.descriptionText)
        TextView descriptionText;
        @BindView(R.id.popupBtn)
        Button popupBtn;
        @BindView(R.id.alarmTextView)
        TextView alarmTextView;

        private OnViewHolderItemClickListener onViewHolderItemClickListener;
        private SharedPreferences prefTheme;

        public viewHolder(@NonNull View itemView, final OnPopupMenuItemClickListener onPopupMenuItemClickListener) {

            super(itemView);
            prefTheme = context.getSharedPreferences("ThemeData", context.MODE_PRIVATE);
            ButterKnife.bind(this, itemView);

            setCustomTheme();

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewHolderItemClickListener.onViewHolderItemClick();
                }
            });

            popupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onPopupMenuItemClickListener != null) {
                        onPopupMenuItemClickListener.onPopupMenuClickListener(position, v);
                    }
                }
            });

        }


        public void bind(MemoListItem item, int position, SparseBooleanArray selectedItems) {
            //startDate
            setDateFormat(startDateText, item.getStartDate());
            //endDate
            setDateFormat(endDateText, item.getEndDate());

            contentText.setText(item.getMemo());
            //startTime
            startTimeText.setText(item.getStartTime());
            startDateText2.setText(item.getStartTime());
            //endTime
            if (item.getEndTime().equals("") || item.getEndTime().equals("null")) {
                endTimeText.setText("");
            } else {
                endTimeText.setText(item.getEndTime());
            }

            if (!item.getPosition().equals("null")) {
                positionText.setText(item.getPosition());
            } else {
                positionText.setText("");
            }
            if (!item.getDescription().equals("null")) {
                descriptionText.setText(item.getDescription());
            } else {
                positionText.setText("");
            }
            if (item.getAlarm() == -1) {
                alarmImage.setImageResource(R.drawable.alarm_off);
            } else {
                alarmImage.setImageResource(R.drawable.alarm_on);
            }

            Log.d(TAG, "ALARM : " + item.getAlarm());
            if(item.getAlarm() == 1){
                alarmTextView.setText("시작시간 10분전");
            } else if(item.getAlarm() == 2){
                alarmTextView.setText("시작시간 30분전");
            } else if(item.getAlarm() == 3){
                alarmTextView.setText("시작시간 1시간전");
            } else if(item.getAlarm() == 4){
                alarmTextView.setText("시작시간 하루전");
            }


            if (item.getImportance() == -1) {
                importanceImage.setImageResource(R.drawable.blank);
            } else if (item.getImportance() == 1) {
                importanceImage.setImageResource(R.drawable.importance_circle);
            } else if (item.getImportance() == 2) {
                importanceImage.setImageResource(R.drawable.importance_circle2);
            } else if (item.getImportance() == 3) {
                importanceImage.setImageResource(R.drawable.importance_circle3);
            }

            String themeName = prefTheme.getString("currentTheme", "기본테마");

            if(!themeName.equals("noTheme")){
                if(themeName.equals("기본테마")){
                    frontPage.setBackgroundColor(selectedItems.get(position) ? context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.mainColor));
                } else if(themeName.equals("첫번째")){
                    frontPage.setBackgroundColor(selectedItems.get(position) ? context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.colorOrange));
                } else if(themeName.equals("두번째")){
                    frontPage.setBackgroundColor(selectedItems.get(position) ? context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.colorYellow));
                } else if(themeName.equals("세번째")){
                    frontPage.setBackgroundColor(selectedItems.get(position) ? context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.colorGreen));
                } else if(themeName.equals("네번째")){
                    frontPage.setBackgroundColor(selectedItems.get(position) ? context.getResources().getColor(R.color.colorBlack) : context.getResources().getColor(R.color.colorPink));
                }
            }

            changeVisibility(selectedItems.get(position));
        }

        private void changeVisibility(final boolean isExpanded) {
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(100, 700) : ValueAnimator.ofInt(700, 100);
            va.setDuration(500);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    behindPage.getLayoutParams().height = (int) animation.getAnimatedValue();
                    behindPage.requestLayout();

                    behindPage.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            va.start();
        }


        public void setDateFormat(TextView textView, String str) {
            if (str.equals("") || str.equals("null")) {
                textView.setText("");
            } else {
                try {
                    Date date = DateUtil.dateFormat2.parse(str);
                    textView.setText(DateUtil.dateFormat2.format(date));
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }

        public void setCustomTheme() {
            String themeName = prefTheme.getString("currentTheme", "noTheme");

            if(!themeName.equals("noTheme")){
                if(themeName.equals("기본테마")){
                    frontPage.setBackgroundColor(context.getResources().getColor(R.color.mainColor));
                } else if(themeName.equals("첫번째")){
                    frontPage.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
                } else if(themeName.equals("두번째")){
                    frontPage.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
                } else if(themeName.equals("세번째")){
                    frontPage.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                } else if(themeName.equals("네번째")){
                    frontPage.setBackgroundColor(context.getResources().getColor(R.color.colorPink));
                }
            }
        }

        public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
            this.onViewHolderItemClickListener = onViewHolderItemClickListener;
        }

    }

}

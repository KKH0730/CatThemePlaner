package com.planer.catthemeplaner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planer.catthemeplaner.listener.CalendarClickListener;
import com.planer.catthemeplaner.R;
import com.planer.catthemeplaner.model.CalendarHeader;
import com.planer.catthemeplaner.model.CalendarModel;
import com.planer.catthemeplaner.model.Day;
import com.planer.catthemeplaner.model.EmptyDay;
import com.planer.catthemeplaner.model.Holiday;
import com.planer.catthemeplaner.model.MemoListItem;
import com.planer.catthemeplaner.ui.calendar.ListFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CalendarAdapter extends RecyclerView.Adapter {
    private final static String TAG = "CalendarAdapter";
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private CalendarClickListener calendarClickListener;

    private List<Object> mCalendarList;
    private ArrayList<MemoListItem> memoListItems;
    private Context context;
    private Long timeMillis;


    public CalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }

    public CalendarAdapter(ArrayList<MemoListItem> items) {
        memoListItems = items;
    }

    public void setCalendarList(List<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }


    public List<Object> getCalendarList() {
        return mCalendarList;
    }


    public void setTimeMillis(Long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public Long getTimeMillis() {
        return timeMillis;
    }

    public void setOnCalendarClickListener(CalendarClickListener calendarClickListener) {
        this.calendarClickListener = calendarClickListener;
    }

    @Override
    public int getItemViewType(int position) { //뷰타입 나누기
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; //날짜 타입
        } else if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE; // 일자 타입

        }

    }


    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();


        //비어있는 일자 타입
        if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, parent, false));

        }
        // 일자 타입
        else {
            View rootView = inflater.inflate(R.layout.item_day, parent, false);
            return new DayViewHolder(rootView, calendarClickListener);
        }

    }

    // 데이터 넣어서 완성시키는것
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        /** 비어있는 날짜 타입 꾸미기 */
        /** EX : empty */
        if (viewType == EMPTY_TYPE) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);
        }
        /** 일자 타입 꾸미기 */
        /** EX : 22 */
        else if (viewType == DAY_TYPE) {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {

                // Model에 Calendar값을 넣어서 몇일인지 데이터 넣기
                model.setCalendar((Calendar) item);
                model.setDateCalendar((Calendar) item);
            }
            // Model의 데이터를 View에 표현하기

            holder.bind(model, position);
        }
    }


    // 개수구하기
    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }


    class EmptyViewHolder extends RecyclerView.ViewHolder { // 비어있는 요일 타입 ViewHolder


        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        public void initView(View v) {
        }

        public void bind(EmptyDay model) {
        }

    }

    // TODO : item_day와 매칭
    class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder

        @BindView(R.id.item_day)
        TextView itemDay;
        @BindView(R.id.extra)
        TextView extra;
        @BindView(R.id.circle)
        TextView circle;
        @BindView(R.id.layout_memo)
        LinearLayout layout_memo;
        @BindView(R.id.holidayTextView)
        TextView holidayTextView;

        private String today;
        private String todayDate;
        private String fullDate;


        public DayViewHolder(@NonNull View itemView, CalendarClickListener calendarClickListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }


        public void bind(Day model, int position) {
            // 일자 값 가져오기
            String day = ((Day) model).getDay();

            // 일자 값 View에 보이게하기
            itemDay.setText(day);

            if (position == 0 || position == 6 || position == 7 || position == 13 || position == 14 || position == 20 || position == 21 || position == 27 || position == 28 || position == 34 || position == 35) {
                itemDay.setTextColor(context.getResources().getColor(R.color.colorRed));
            }


            //오늘 날짜를 default로 표시
            todayDate();
            String checkDate = checkDate();

            //오늘 날짜에 동그라미 표시
            if (todayDate.equals(checkDate)) {
                if (today.equals(day)) {
                    circle.setVisibility(View.VISIBLE);
                }
            }

            fullDate = ((Day) model).getFullDate();
            ListFragment listFragment = new ListFragment();
            ArrayList<MemoListItem> memoListItems = listFragment.loadListData(fullDate);

            if (memoListItems.size() < 4) {
                for (int i = 0; i < memoListItems.size(); i++) {
                    if (fullDate.equals(memoListItems.get(i).getStartDate())) {
                        setTextView(memoListItems.get(i).getMemo(), i, memoListItems.get(i).getImportance());
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (fullDate.equals(memoListItems.get(i).getStartDate())) {
                        setTextView(memoListItems.get(i).getMemo(), i, memoListItems.get(i).getImportance());

                        if (memoListItems.size() - 4 == 0) {

                        } else {
                            extra.setText("+" + (memoListItems.size() - 4));
                            extra.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            //공휴일 설정
            CalendarModel calendarModel = new CalendarModel(context);
            ArrayList<Holiday> holidays = calendarModel.loadHoliday();
            for (int i = 0; i < holidays.size(); i++) {

                if (holidays.get(i).getHolidayDate().equals(fullDate)) {
                    if (holidays.get(i).getHolidayName().equals("1월1일")) {
                        holidayTextView.setText("신정");
                    } else if (holidays.get(i).getHolidayName().equals("기독탄신일")) {
                        holidayTextView.setText("크리스마스");
                    } else {
                        holidayTextView.setText(holidays.get(i).getHolidayName());
                    }
                    holidayTextView.setTextColor(context.getResources().getColor(R.color.colorRed));
                    itemDay.setTextColor(context.getResources().getColor(R.color.colorRed));
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarClickListener.onCalendarClickListener(fullDate);
                }
            });

        }

        //오늘이 몇년도 몇월, 그리고 몇일인지 표시
        public void todayDate() {
            Calendar calendar = Calendar.getInstance();

            Day dayModel = new Day();
            dayModel.setCalendar(calendar);
            //오늘 몇일인지
            String today = dayModel.getDay();
            this.today = today;

            //오늘 몇년도 몇월인지
            CalendarHeader headerModel = new CalendarHeader();
            headerModel.setHeader(calendar.getTimeInMillis());
            String todayDate = headerModel.getHeader();
            this.todayDate = todayDate;
        }


        //처음 날짜가 보여질때 혹은 날짜를 변경할때의 시간을 몇년도 몇월인지 비교를 위해 나타내는 구문
        public String checkDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeMillis);

            CalendarHeader checkHeaderModel = new CalendarHeader();
            checkHeaderModel.setHeader(calendar.getTimeInMillis());
            String checkDate = checkHeaderModel.getHeader();

            return checkDate;
        }

        public void setTextView(String a, int i, int importance) {
            //TextView 생성
            TextView view1 = new TextView(context);
            view1.setText(a);
            view1.setTextSize(10);
            view1.setTextColor(Color.BLACK);

            if (importance == -1) {
                if (i == 0) {
                    view1.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));
                    view1.setTextColor(Color.WHITE);
                } else if (i == 1) {
                    view1.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));
                    view1.setTextColor(Color.WHITE);
                } else if (i == 2) {
                    view1.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));
                    view1.setTextColor(Color.WHITE);
                } else if (i == 3) {
                    view1.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));
                    view1.setTextColor(Color.WHITE);
                }
            } else {
                if (i == 0) {
                    if (importance == 1) {
                        Log.d(TAG, "1동작됨");
                        view1.setBackground(context.getResources().getDrawable(R.drawable.very_important));
                    } else if (importance == 2) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.important_));
                    }else if (importance == 3) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.neutral));
                    }
                } else if (i == 1) {
                    if (importance == 1) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.very_important));
                    } else if (importance == 2) {
                        Log.d(TAG, "2동작됨");
                        view1.setBackground(context.getResources().getDrawable(R.drawable.important_));
                    }else if (importance == 3) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.neutral));
                    }

                } else if (i == 2) {
                    if (importance == 1) {
                        Log.d(TAG, "3동작됨");
                        view1.setBackground(context.getResources().getDrawable(R.drawable.very_important));
                    } else if (importance == 2) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.important_));
                    }else if (importance == 3) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.neutral));
                    }

                } else if (i == 3) {
                    if (importance == 1) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.very_important));
                    } else if (importance == 2) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.important_));
                    }else if (importance == 3) {
                        view1.setBackground(context.getResources().getDrawable(R.drawable.neutral));
                    }
                }
                view1.setVisibility(View.VISIBLE);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.LEFT;
            view1.setLayoutParams(lp);

            layout_memo.addView(view1);
        }


    }


}


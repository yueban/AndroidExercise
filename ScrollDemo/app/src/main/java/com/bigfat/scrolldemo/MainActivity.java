package com.bigfat.scrolldemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    //颜色值
    public static int Calendar_WeekBgColor;
    public static int Calendar_DayBgColor;
    public static int isHoliday_BgColor;
    public static int unPresentMonth_FontColor;
    public static int isPresentMonth_FontColor;
    public static int isToday_BgColor;
    public static int special_Reminder;
    public static int common_Reminder;
    public static int Calendar_WeekFontColor;
    private static int firstDayOfWeek = Calendar.SUNDAY;
    //测量值
    int calendarCurrentWeekHeight;//日历当前周那一行距离顶部的高度
    int screenWidth;//屏幕宽度
    int cellWidth;//日历单元格宽度
    //标识值
    private int currentMonth;
    private int currentYear;
    private boolean isSelectedWeekInCalendar;
    //容器类
    private ScrollView scrollView;
    private LinearLayout llCalendarTop;
    private LinearLayout llCalendarContent;
    private LinearLayout llCalendarContentFloat;
    //控件
    private TextView tvCurrentDate;
    private ImageButton btnPre;
    private ImageButton btnNext;
    private ListView listView;
    private ArrayList<DateWidgetDayCell> days = new ArrayList<>();
    private ArrayList<DateWidgetDayCell> daysFloat = new ArrayList<>();
    //数据
    private ArrayList<String> data;//日程列表
    private Calendar calStartDate = Calendar.getInstance();//日历起始日期
    private Calendar calToday = Calendar.getInstance();//今天
    private Calendar calSelected = Calendar.getInstance();//当前选中日期
    // 点击日历，触发事件
    private DateWidgetDayCell.OnItemClick onDayCellClickEvent = new DateWidgetDayCell.OnItemClick() {
        public void OnClick(DateWidgetDayCell item) {
            setCalSelected(item.getDate());
        }
    };

    public static Calendar GetTodayDate() {
        Calendar cal_Today = Calendar.getInstance();
        cal_Today.set(Calendar.HOUR_OF_DAY, 0);
        cal_Today.set(Calendar.MINUTE, 0);
        cal_Today.set(Calendar.SECOND, 0);
        cal_Today.setFirstDayOfWeek(firstDayOfWeek);
        return cal_Today;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMeasure();
        initColor();
        initData();
        initView();
        initCalendarView();
        initListView();
        initEvent();
    }

    private void initMeasure() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        cellWidth = screenWidth / 7 + 1;
    }

    private void initColor() {
        Calendar_WeekBgColor = this.getResources().getColor(
                R.color.Calendar_WeekBgColor);
        Calendar_DayBgColor = this.getResources().getColor(
                R.color.Calendar_DayBgColor);
        isHoliday_BgColor = this.getResources().getColor(
                R.color.isHoliday_BgColor);
        unPresentMonth_FontColor = this.getResources().getColor(
                R.color.unPresentMonth_FontColor);
        isPresentMonth_FontColor = this.getResources().getColor(
                R.color.isPresentMonth_FontColor);
        isToday_BgColor = this.getResources().getColor(R.color.isToday_BgColor);
        special_Reminder = this.getResources().getColor(R.color.specialReminder);
        common_Reminder = this.getResources().getColor(R.color.commonReminder);
        Calendar_WeekFontColor = this.getResources().getColor(
                R.color.Calendar_WeekFontColor);
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("item_" + i);
        }
    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scv_frag_calendar);
        llCalendarTop = (LinearLayout) findViewById(R.id.ll_frag_calendar_top);
        llCalendarContent = (LinearLayout) findViewById(R.id.ll_frag_calendar_content);
        llCalendarContentFloat = (LinearLayout) findViewById(R.id.ll_frag_calendar_content_float);
        tvCurrentDate = (TextView) findViewById(R.id.tv_frag_calendar_current_date);
        btnPre = (ImageButton) findViewById(R.id.btn_frag_calendar_pre);
        btnNext = (ImageButton) findViewById(R.id.btn_frag_calendar_next);
        listView = (ListView) findViewById(R.id.lv_frag_calendar);
    }

    private void initCalendarView() {
        Log.i(TAG, "calStartDate--1->" + calStartDate.get(Calendar.YEAR) + " " + calStartDate.get(Calendar.MONTH) + " " + calStartDate.get(Calendar.DAY_OF_MONTH));
        calStartDate.setTimeInMillis(System.currentTimeMillis());
        Log.i(TAG, "calStartDate--2->" + calStartDate.get(Calendar.YEAR) + " " + calStartDate.get(Calendar.MONTH) + " " + calStartDate.get(Calendar.DAY_OF_MONTH));
        calStartDate.setFirstDayOfWeek(firstDayOfWeek);
        Log.i(TAG, "calStartDate--3->" + calStartDate.get(Calendar.YEAR) + " " + calStartDate.get(Calendar.MONTH) + " " + calStartDate.get(Calendar.DAY_OF_MONTH));
        calToday = GetTodayDate();
        calSelected.setTimeInMillis(calToday.getTimeInMillis());
        //绘制日历空白模板
        llCalendarTop.addView(generateCalendarTop());
        llCalendarContent.addView(generateCalendarContent());
        llCalendarContentFloat.addView(generateCalendarContentFloat());
        // 计算本月日历中的第一天(一般是上月的某天)，并更新日历
        updateCalendarStartDate();
    }

    private void initListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_item_tv, data);
        listView.setAdapter(adapter);

        int itemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        int lvHeight = itemHeight * data.size() + listView.getDividerHeight() * (data.size() - 1);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listView.getLayoutParams();
        lp.height = lvHeight;
        listView.setLayoutParams(lp);
    }

    private void initEvent() {
        btnPre.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                executeScvScrollEvent();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_frag_calendar_pre://上个月
                Log.i(TAG, "btn_frag_calendar_pre");
//                calSelected.setTimeInMillis(0);
                currentMonth--;
                if (currentMonth == -1) {
                    currentMonth = 11;
                    currentYear--;
                }

                calStartDate.set(Calendar.MONTH, currentMonth);
                calStartDate.set(Calendar.YEAR, currentYear);

                updateCalendarStartDate();
                break;

            case R.id.btn_frag_calendar_next://下个月
                Log.i(TAG, "btn_frag_calendar_next");
                currentMonth++;
                if (currentMonth == 12) {
                    currentMonth = 0;
                    currentYear++;
                }

                calStartDate.set(Calendar.MONTH, currentMonth);
                calStartDate.set(Calendar.YEAR, currentYear);

                updateCalendarStartDate();
                break;
        }
    }

    /**
     * 处理界面滚动事件
     */
    private void executeScvScrollEvent() {
//                Log.i(TAG, "scrollView.getY()--->" + scrollView.getScrollY());
//                Log.i(TAG, "calendarCurrentWeekHeight--->" + calendarCurrentWeekHeight);
        if (isSelectedWeekInCalendar && scrollView.getScrollY() > calendarCurrentWeekHeight) {
            llCalendarContentFloat.setVisibility(View.VISIBLE);
        } else {
            llCalendarContentFloat.setVisibility(View.GONE);
        }
    }

    /**
     * 设置当前选中日期
     */
    private void setCalSelected(Calendar calendar) {
        calSelected.setTimeInMillis(calendar.getTimeInMillis());
        updateCalendarSelectedWeekHeight();

//            if (adapter != null)
//                adapter.jump2today(item.getDate().getTime());

        updateCalendar();
    }

    /**
     * 更新当天日期和被选中日期
     */
    private void updateCalendarStartDate() {
        currentMonth = calStartDate.get(Calendar.MONTH);
        currentYear = calStartDate.get(Calendar.YEAR);

        calStartDate.set(Calendar.DAY_OF_MONTH, 1);
        calStartDate.set(Calendar.HOUR_OF_DAY, 0);
        calStartDate.set(Calendar.MINUTE, 0);
        calStartDate.set(Calendar.SECOND, 0);
        //默认选择当前月第一天
        calSelected.setTimeInMillis(calStartDate.getTimeInMillis());

        //计算当前月日历视图中真正的起始天
        int iDay = 0;
        int iStartDay = firstDayOfWeek;
        if (iStartDay == Calendar.MONDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
            if (iDay < 0)
                iDay = 6;
        }
        if (iStartDay == Calendar.SUNDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            if (iDay < 0)
                iDay = 6;
        }
        calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
        //根据当前选择更新视图
        setCalSelected(calSelected);
        //刷新顶部当前月文本
        updateCurrentMonthDisplay();
        //判断当前选择周（天）是否在日历列表中，作为顶部周视图是否显示的判断
        long startMills = calStartDate.getTimeInMillis();
        long selectedMills = calSelected.getTimeInMillis();
        Log.i(TAG, "startMills--->" + startMills);
        Log.i(TAG, "selectedMills--->" + selectedMills);
        isSelectedWeekInCalendar = selectedMills >= startMills && selectedMills <= startMills + 41 * 24 * 60 * 60 * (long) 1000;
    }

    /**
     * 更新日历标题上显示的年月
     */
    private void updateCurrentMonthDisplay() {
        String date = calStartDate.get(Calendar.YEAR) + "年" + (currentMonth + 1) + "月";
        tvCurrentDate.setText(date);
    }

    private View generateCalendarTop() {
        LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
        layRow.setBackgroundColor(Color.parseColor("#f3f3f3"));
        layRow.setPadding(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));

        DateWidgetDayHeader day;
        int iWeekDay;
        for (int iDay = 0; iDay < 7; iDay++) {
            day = new DateWidgetDayHeader(this, cellWidth);
            day.setBackgroundColor(Color.parseColor("#f3f3f3"));
            iWeekDay = DayStyle.getWeekDay(iDay, firstDayOfWeek);
            day.setData(iWeekDay);
            layRow.addView(day);
        }

        return layRow;
    }

    private View generateCalendarContent() {
        LinearLayout layContentDays = createLayout(LinearLayout.VERTICAL);
        days.clear();
        for (int iRow = 0; iRow < 6; iRow++) {
            layContentDays.addView(generateCalendarRow());
        }
//        layContentDays.setBackgroundResource(R.drawable.main_bg0top0bottom0left0right);
        return layContentDays;
    }

    private View generateCalendarContentFloat() {
        LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);

        for (int iDay = 0; iDay < 7; iDay++) {
            DateWidgetDayCell dayCell = new DateWidgetDayCell(this, cellWidth, cellWidth * 4 / 5);
            dayCell.setItemClick(onDayCellClickEvent);
            daysFloat.add(dayCell);
            layRow.addView(dayCell);
        }

        return layRow;
    }

    /**
     * 生成日历中的一行，仅生成View，没有数据
     */
    private View generateCalendarRow() {
        LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);

        for (int iDay = 0; iDay < 7; iDay++) {
            DateWidgetDayCell dayCell = new DateWidgetDayCell(this, cellWidth, cellWidth * 4 / 5);
            dayCell.setItemClick(onDayCellClickEvent);
            days.add(dayCell);
            layRow.addView(dayCell);
        }

        return layRow;
    }

    private void updateCalendar() {
        updateCalendarSelectedWeekHeight();
        // 更新日历
        Calendar calCalendar = Calendar.getInstance();
        DateWidgetDayCell daySelected = null;
        final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
        final int iSelectedYear = calSelected.get(Calendar.YEAR);
        final int iSelectedMonth = calSelected.get(Calendar.MONTH);
        final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
        calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
        //当前选中周起始索引
        long currentWeekStartDayIndex = (calSelected.getTimeInMillis() - calStartDate.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 7 * 7;
        Log.i(TAG, "updateCalendar");
        Log.i(TAG, "currentMonth--->" + currentMonth);
        for (int i = 0; i < days.size(); i++) {
            final int iYear = calCalendar.get(Calendar.YEAR);
            final int iMonth = calCalendar.get(Calendar.MONTH);
            final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
            final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
            DateWidgetDayCell dayCell = days.get(i);

            // 判断是否当天
            boolean bToday = false;
            if (calToday.get(Calendar.YEAR) == iYear) {
                if (calToday.get(Calendar.MONTH) == iMonth) {
                    if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
                        bToday = true;
                    }
                }
            }

            // 是否是周末
            boolean bHoliday = false;
            if ((iDayOfWeek == Calendar.SATURDAY) || (iDayOfWeek == Calendar.SUNDAY))
                bHoliday = true;

            // 是否被选中
            boolean bSelected = false;
            if (bIsSelection)
                if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
                        && (iSelectedYear == iYear)) {
                    bSelected = true;
                }
            if (bSelected)
                daySelected = dayCell;

            // 是否有日程
            boolean hasRecord = false;
//            if (data != null) {
//                hasRecord = data.containsKey(DateUtil.getStr(
//                        calCalendar.getTime(),
//                        DateUtil.DAY));
//            }

            //设置日期Cell状态
            dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday, currentMonth, hasRecord, bSelected);
            dayCell.invalidate();
            //如果是当前周，则顶部当前周数据也要设置
            if (i >= currentWeekStartDayIndex && i < currentWeekStartDayIndex + 7) {
                DateWidgetDayCell dayCellFloat = daysFloat.get(i - (int)currentWeekStartDayIndex);
                dayCellFloat.setData(iYear, iMonth, iDay, bToday, bHoliday, currentMonth, hasRecord, bSelected);
                dayCellFloat.invalidate();
            }
            //下一天
            calCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        llCalendarContent.invalidate();
        llCalendarContentFloat.invalidate();
    }

    /**
     * 计算当前周这一行之前日历的高度
     */
    private void updateCalendarSelectedWeekHeight() {
        llCalendarContentFloat.post(new Runnable() {
            @Override
            public void run() {
                //计算当前周之前一共有几行
                long rows = (calSelected.getTimeInMillis() - calStartDate.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 7;
                calendarCurrentWeekHeight = llCalendarContentFloat.getHeight() * (int)rows;
                executeScvScrollEvent();
            }
        });
    }

    // 生成布局
    private LinearLayout createLayout(int iOrientation) {
        LinearLayout lay = new LinearLayout(this);
        lay.setLayoutParams(new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        lay.setOrientation(iOrientation);
        lay.setBackgroundColor(Color.WHITE);

        return lay;
    }
}
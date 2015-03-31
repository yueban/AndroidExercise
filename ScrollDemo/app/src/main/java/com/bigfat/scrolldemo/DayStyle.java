package com.bigfat.scrolldemo;

import java.util.Calendar;

/**
 * 日历控件样式绘制类
 *
 * @Description: 日历控件样式绘制类
 * @FileName: DayStyle.java
 */
public class DayStyle {
    private final static String[] vecStrWeekDayNames = getWeekDayNames();

    private static String[] getWeekDayNames() {
        String[] vec = new String[8];

        vec[Calendar.SUNDAY] = Util.getStringRes(App.getContext(), R.string.sunday);
        vec[Calendar.MONDAY] = Util.getStringRes(App.getContext(), R.string.monday);
        vec[Calendar.TUESDAY] = Util.getStringRes(App.getContext(), R.string.tuesday);
        vec[Calendar.WEDNESDAY] = Util.getStringRes(App.getContext(), R.string.thursday);
        vec[Calendar.THURSDAY] = Util.getStringRes(App.getContext(), R.string.wednesday);
        vec[Calendar.FRIDAY] = Util.getStringRes(App.getContext(), R.string.friday);
        vec[Calendar.SATURDAY] = Util.getStringRes(App.getContext(), R.string.saturday);

        return vec;
    }

    public static String getWeekDayName(int iDay) {
        return vecStrWeekDayNames[iDay];
    }

    public static int getWeekDay(int index, int iFirstDayOfWeek) {
        int iWeekDay = -1;

        if (iFirstDayOfWeek == Calendar.MONDAY) {
            iWeekDay = index + Calendar.MONDAY;

            if (iWeekDay > Calendar.SATURDAY)
                iWeekDay = Calendar.SUNDAY;
        }

        if (iFirstDayOfWeek == Calendar.SUNDAY) {
            iWeekDay = index + Calendar.SUNDAY;
        }

        return iWeekDay;
    }
}

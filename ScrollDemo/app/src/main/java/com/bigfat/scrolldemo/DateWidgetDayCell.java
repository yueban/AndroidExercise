package com.bigfat.scrolldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import java.util.Calendar;

/**
 * 日历控件单元格绘制类
 *
 * @Description: 日历控件单元格绘制类
 * @FileName: DateWidgetDayCell.java
 */
public class DateWidgetDayCell extends View {
    // 基本元素
    private OnItemClick itemClick = null;
    private Paint ptBg = new Paint();//画背景
    private Paint ptNumber = new Paint();//话数字
    private Paint ptBottomText = new Paint();//画底部文本
    private RectF rect = new RectF();
    private String sDate;
    private int textSizeNumber;//数字文本大小
    private int textSizeBottom;//底部文本大小
    // 当前日期
    private int iDateYear;
    private int iDateMonth;
    private int iDateDay;
    // 布尔变量
    private boolean bSelected;
    private boolean bIsActiveMonth;
    private boolean bToday;
    private boolean bTouchedDown;
    private boolean bHoliday;
    private boolean hasRecord;
    //农历
    private Lunar lunar;
    private int radius;//背景圆的半径

    // 构造函数
    public DateWidgetDayCell(Context context, int iWidth, int iHeight) {
        super(context);
        setFocusable(true);
        setLayoutParams(new LayoutParams(iWidth, iHeight));
        textSizeNumber = (int) this.getResources().getDimension(R.dimen.sch_text_size);
        textSizeBottom = (int) this.getResources().getDimension(R.dimen.sch_text_size_bottom);

        ptBg.setAntiAlias(true);

        ptNumber.setTextSize(textSizeNumber);
        ptNumber.setTypeface(null);
        ptNumber.setAntiAlias(true);
        ptNumber.setShader(null);
        ptNumber.setUnderlineText(false);

        ptBottomText.setTextSize(textSizeBottom);
        ptBottomText.setTypeface(null);
        ptBottomText.setAntiAlias(true);
        ptBottomText.setShader(null);
        ptBottomText.setUnderlineText(false);
    }

    public boolean isbToday() {
        return bToday;
    }

    public boolean isbSelected() {
        return bSelected;
    }

    // 取变量值
    public Calendar getDate() {
        Calendar calDate = Calendar.getInstance();
        calDate.clear();
        calDate.set(Calendar.YEAR, iDateYear);
        calDate.set(Calendar.MONTH, iDateMonth);
        calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
        return calDate;
    }

    // 设置变量值
    public void setData(int iYear, int iMonth, int iDay, Boolean bToday, Boolean bHoliday, int iActiveMonth, boolean hasRecord, boolean bSelected) {
        this.iDateYear = iYear;
        this.iDateMonth = iMonth;
        this.iDateDay = iDay;
        this.lunar = new Lunar(iYear, iMonth, iDay);
        this.sDate = Integer.toString(iDateDay);
        this.bIsActiveMonth = (iDateMonth == iActiveMonth);
        this.bToday = bToday;
        this.bHoliday = bHoliday;
        this.hasRecord = hasRecord;
        this.bSelected = bSelected;

        ptNumber.measureText(sDate);
        ptBottomText.measureText(lunar.toString());
    }

    // 重载绘制方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(0, 0, this.getWidth(), this.getHeight());
        rect.inset(1, 1);

        drawDayView(canvas, IsViewFocused());
        drawDayNumber(canvas);
        drawDaBottomText(canvas);
    }

    public boolean IsViewFocused() {
        return (this.isFocused() || bTouchedDown);
    }

    /**
     * 绘制日历方格
     */
    private void drawDayView(Canvas canvas, boolean bFocused) {
        final int iPosX = this.getWidth() / 2;
        final int iPosY = this.getHeight() / 2 - getHeight() / 6;
        radius = getTextHeight() / 2 + getHeight() / 30;

        ptBg.setColor(0x00ffffff);
        //画背景圆
        if (bSelected || bFocused) {
            //选中状态
            if (bSelected) {
                ptBg.setColor(Color.parseColor("#FFFF2D2D"));
            }
        } else if (bToday) {
            ptBg.setColor(MainActivity.isToday_BgColor);
        }
        canvas.drawCircle(iPosX, iPosY, radius, ptBg);

        //画底部的线
        ptBg.setColor(getContext().getResources().getColor(R.color.darkdarkgray));  //#525252
        ptBg.setStrokeWidth(1.5f);
        float[] pts = new float[4];
        pts[0] = rect.left - 1;
        pts[1] = rect.bottom + 1;
        pts[2] = rect.right + 1;
        pts[3] = rect.bottom + 1;
        canvas.drawLines(pts, ptBg);

        //画日程小点
        ptBg.setStrokeWidth(0);
        if (hasRecord) {
            CreateReminder(canvas, MainActivity.special_Reminder);
        }
    }

    /**
     * 绘制日历中的数字
     */
    public void drawDayNumber(Canvas canvas) {
        final int iPosX = (int) rect.left + ((int) rect.width() >> 1) - ((int) ptNumber.measureText(sDate) >> 1);
        final int iPosY = (int) (this.getHeight() - (this.getHeight() - getTextHeight()) / 2 - ptNumber.getFontMetrics().bottom) - getHeight() / 6;

        ptNumber.setColor(MainActivity.isPresentMonth_FontColor);

        //如果是周末
        if (bHoliday) {
            ptNumber.setColor(Color.parseColor("#999999"));
        }
        //如果非本月
        if (!bIsActiveMonth) {
            ptNumber.setColor(MainActivity.unPresentMonth_FontColor);
        }
        //如果是选中态下，是白色
        if (bSelected) {
            ptNumber.setColor(0xffffffff);
        }

        canvas.drawText(sDate, iPosX, iPosY, ptNumber);
    }

    /**
     * 绘制底部文本（节日，节气，农历）
     */
    private void drawDaBottomText(Canvas canvas) {
        final int iPosX = (int) rect.left + ((int) rect.width() >> 1) - ((int) ptBottomText.measureText(lunar.toString()) >> 1);
        final int iPosY = (int) (this.getHeight() - ptBottomText.getFontMetrics().bottom) - getHeight() / 10;

        ptBottomText.setColor(MainActivity.isPresentMonth_FontColor);

        //如果是周末
        if (bHoliday) {
            ptBottomText.setColor(Color.parseColor("#999999"));
        }
        //如果非本月
        if (!bIsActiveMonth) {
            ptBottomText.setColor(MainActivity.unPresentMonth_FontColor);
        }

        canvas.drawText(lunar.toString(), iPosX, iPosY, ptBottomText);
    }

    /**
     * 得到数字文本高度
     */
    private int getTextHeight() {
        return (int) (-ptNumber.ascent() + ptNumber.descent());
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void doItemClick() {
        if (itemClick != null)
            itemClick.OnClick(this);
    }

    // 点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean bHandled = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bHandled = true;
            bTouchedDown = true;
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            bHandled = true;
            bTouchedDown = false;
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            bHandled = true;
            bTouchedDown = false;
            invalidate();
            doItemClick();
        }
        return bHandled;
    }

    // 点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bResult = super.onKeyDown(keyCode, event);
        if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
                || (keyCode == KeyEvent.KEYCODE_ENTER)) {
            doItemClick();
        }
        return bResult;
    }

    public void CreateReminder(Canvas canvas, int Color) {
        ptBg.setStyle(Paint.Style.FILL_AND_STROKE);
        ptBg.setColor(Color);
        canvas.drawCircle(rect.right - rect.width() / 2, rect.height() - rect.height() / 6.5f, rect.height() / 15, ptBg);// 绘制圆
    }

    public interface OnItemClick {
        void OnClick(DateWidgetDayCell item);
    }
}

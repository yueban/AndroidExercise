package com.bigfat.scrolldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;

import java.util.Calendar;

/**
 * 日历控件单元格绘制类
 *
 * @Description: 日历控件单元格绘制类
 * @FileName: DateWidgetDayCell.java
 */
public class DateWidgetDayCell extends View {
    // 字体大小
    //private static final int fTextSize = 24;

    public static int ANIM_ALPHA_DURATION = 100;
    // 基本元素
    private OnItemClick itemClick = null;
    private Paint pt = new Paint();
    private RectF rect = new RectF();
    private String sDate = "";
    // 当前日期
    private int iDateYear = 0;
    private int iDateMonth = 0;
    private int iDateDay = 0;
    // 布尔变量
    private boolean bSelected = false;
    private boolean bIsActiveMonth = false;
    private boolean bToday = false;
    private boolean bTouchedDown = false;
    private boolean bHoliday = false;
    private boolean hasRecord = false;

    // 构造函数
    public DateWidgetDayCell(Context context, int iWidth, int iHeight) {
        super(context);
        setFocusable(true);
        setLayoutParams(new LayoutParams(iWidth, iHeight));
        int sizeOfText = (int) this.getResources().getDimension(R.dimen.sch_text_size);
        pt.setTextSize(sizeOfText);
    }

    // 根据条件返回不同颜色值
    public static int getColorBkg(boolean bHoliday, boolean bToday) {
        if (bToday)
            return MainActivity.isToday_BgColor;
        // if (bHoliday) //如需周末有特殊背景色，可去掉注释
        // return Calendar_TestActivity.isHoliday_BgColor;
        return MainActivity.Calendar_DayBgColor;
    }

    // 不透明度渐变
    public static void startAlphaAnimIn(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
        anim.setDuration(ANIM_ALPHA_DURATION);
        anim.startNow();
        view.startAnimation(anim);
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
    public void setData(int iYear, int iMonth, int iDay, Boolean bToday,
                        Boolean bHoliday, int iActiveMonth, boolean hasRecord) {
        iDateYear = iYear;
        iDateMonth = iMonth;
        iDateDay = iDay;

        this.sDate = Integer.toString(iDateDay);
        this.bIsActiveMonth = (iDateMonth == iActiveMonth);
        this.bToday = bToday;
        this.bHoliday = bHoliday;
        this.hasRecord = hasRecord;
    }

    // 重载绘制方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(0, 0, this.getWidth(), this.getHeight());
        rect.inset(1, 1);

        final boolean bFocused = IsViewFocused();

        drawDayView(canvas, bFocused);
        drawDayNumber(canvas);
    }

    public boolean IsViewFocused() {
        return (this.isFocused() || bTouchedDown);
    }

    // 绘制日历方格
    private void drawDayView(Canvas canvas, boolean bFocused) {
        final int iPosX = this.getWidth() / 2;
//        final int iPosY = this.getHeight() / 2 - (int)pt.getFontMetrics().bottom;
        final int iPosY = this.getHeight() / 2;

        pt.setAntiAlias(true);

        if (bSelected || bFocused) {
            LinearGradient lGradBkg = null;

            if (bFocused) {
//				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0, 0xffffffff, 0xffffffff, Shader.TileMode.CLAMP);
                pt.setColor(Color.parseColor("#ffffffff"));
            }
            //选中状态
            if (bSelected) {
//				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0, 0xff3696f8, 0xff3696f8, Shader.TileMode.CLAMP);
//              pt.setShader(lGradBkg);
                pt.setColor(Color.parseColor("#FF2D2D"));
//                canvas.drawCircle(iPosX-1, iPosY-1, getTextHeight()/2+5, pt);
                canvas.drawCircle(iPosX, iPosY, getTextHeight() / 2 + 6, pt);
            }

            pt.setShader(null);

        } else if (bToday) {
//			pt.setColor(MainActivity.isToday_BgColor);
//			canvas.drawRect(rect, pt);
//			pt.setStrokeWidth(1);
//			canvas.drawRect(rect, pt);
//			canvas.clipRect(rect.left+1, rect.top, rect.right-1, rect.bottom);
            pt.setColor(getResources().getColor(R.color.unPresentMonth_FontColor));
            canvas.drawCircle(iPosX, iPosY - 4, getTextHeight() / 2 + 6, pt);
        }

        pt.setColor(getContext().getResources().getColor(R.color.darkdarkgray));  //#525252
        pt.setStrokeWidth(1.5f);
        float[] pts = new float[4];
        pts[0] = rect.left - 1;
        pts[1] = rect.bottom + 1;
        pts[2] = rect.right + 1;
        pts[3] = rect.bottom + 1;
        canvas.drawLines(pts, pt);

        pt.setStrokeWidth(0);
        if (hasRecord) {
            CreateReminder(canvas, MainActivity.special_Reminder);
        }
        // else if (!hasRecord && !bToday && !bSelected) {
        // CreateReminder(canvas, Calendar_TestActivity.Calendar_DayBgColor);
        // }
    }

    // 绘制日历中的数字
    public void drawDayNumber(Canvas canvas) {
        // draw day number
        pt.setTypeface(null);
        pt.setAntiAlias(true);
        pt.setShader(null);
//		pt.setFakeBoldText(true);

        pt.setColor(MainActivity.isPresentMonth_FontColor);
        pt.setUnderlineText(false);

        //如果是周末
        if (bHoliday) {
            pt.setColor(Color.parseColor("#999999"));
        }
        //如果非本月
        if (!bIsActiveMonth) {
            pt.setColor(MainActivity.unPresentMonth_FontColor);
//			pt.setFakeBoldText(false);
        }

        //如果是选中态下，是白色
        if (bSelected) {
            pt.setColor(0xffffffff);
        }

        //如果是今天 就加下划线，我们就不加了
//		if (bToday)
//			pt.setUnderlineText(true);

        final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
                - ((int) pt.measureText(sDate) >> 1);
//
//        //文字居中
        final int iPosY = (int) (this.getHeight() - (this.getHeight() - getTextHeight()) / 2 - pt.getFontMetrics().bottom);

//        //文字偏上
//        final int iPosY = (int) (this.getHeight() / 2);

        canvas.drawText(sDate, iPosX, iPosY, pt);
        pt.setUnderlineText(false);
    }

    // 得到字体高度
    private int getTextHeight() {
        return (int) (-pt.ascent() + pt.descent());
    }

    // 设置是否被选中
    @Override
    public void setSelected(boolean bEnable) {
        if (this.bSelected != bEnable) {
            this.bSelected = bEnable;
            this.invalidate();
        }
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
            startAlphaAnimIn(DateWidgetDayCell.this);
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
        pt.setStyle(Paint.Style.FILL_AND_STROKE);
        pt.setColor(Color);
//		Path path = new Path();
//		path.moveTo(rect.right - rect.width() / 4, rect.top);
//		path.lineTo(rect.right, rect.top);
//		path.lineTo(rect.right, rect.top + rect.width() / 4);
//		path.lineTo(rect.right - rect.width() / 4, rect.top);
//		path.close();
        canvas.drawCircle(rect.right - rect.width() / 2, rect.height() - rect.height() / 6.5f, rect.height() / 15, pt);// 绘制圆
        //canvas.drawPath(path, pt);
    }

    public interface OnItemClick {
        public void OnClick(DateWidgetDayCell item);
    }
}

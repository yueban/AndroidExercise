package com.bigfat.lockpattern.model;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/3
 */
public class Point {
    public enum Status {
        NORMAL, PRESSED, ERROR
    }

    private float x;
    private float y;
    private int index = 0;
    private Status state = Status.NORMAL;

    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Status getState() {
        return state;
    }

    public void setState(Status state) {
        this.state = state;
    }

    /**
     * 返回当前点与另一点间距离
     *
     * @param point 另一点
     * @return 两点间距离
     */
    public double distance(Point point) {
        return Math.sqrt(Math.pow(x - point.getX(), 2) + Math.pow(y - point.getY(), 2));
    }

    /**
     * 判断移动（触摸）位置是否与点重合
     *
     * @param r       点的半径
     * @param movingX 移动点的x坐标
     * @param movingY 移动点的y坐标
     * @return true：移动点与当前点重合；false：不重合
     */
    public boolean with(float r, float movingX, float movingY) {
        return Math.sqrt(Math.pow(x - movingX, 2) + Math.pow(y - movingY, 2)) < r;
    }
}

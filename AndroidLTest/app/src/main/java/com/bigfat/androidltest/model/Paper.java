package com.bigfat.androidltest.model;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/12
 */
public class Paper {
    private String name;
    private int pic;
    private String work;
    private int[] picGroup;

    public Paper(String name, int pic, String work, int[] picGroup) {
        this.name = name;
        this.pic = pic;
        this.work = work;
        this.picGroup = picGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public int[] getPicGroup() {
        return picGroup;
    }

    public void setPicGroup(int[] picGroup) {
        this.picGroup = picGroup;
    }
}

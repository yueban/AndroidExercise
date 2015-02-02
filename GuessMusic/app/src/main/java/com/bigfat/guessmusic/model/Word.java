package com.bigfat.guessmusic.model;

/**
 * 文字（按钮）
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/31
 */
public class Word {

    private int index;
    private boolean isVisiable;
    private String text;

    public Word() {
        isVisiable = true;
        text = "";
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisiable() {
        return isVisiable;
    }

    public void setVisiable(boolean isVisiable) {
        this.isVisiable = isVisiable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

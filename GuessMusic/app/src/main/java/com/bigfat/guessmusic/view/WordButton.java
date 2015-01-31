package com.bigfat.guessmusic.view;

import android.widget.Button;

/**
 * 文字按钮
 *
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/31
 */
public class WordButton {

    public int mIndex;
    public boolean mIsVisiable;
    public String mWordText;
    public Button mViewButton;

    public WordButton() {
        mIsVisiable = true;
        mWordText = "";
    }
}

package com.bigfat.weixin60ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/15
 */
public class TabFragment extends Fragment {

    private String mTitle = "Default";

    public static final String TITLE = "title";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
        TextView tv = new TextView(getActivity());
        tv.setTextSize(20);
        tv.setBackgroundColor(0xffffffff);
        tv.setText(mTitle);
        tv.setTextColor(0xff000000);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}


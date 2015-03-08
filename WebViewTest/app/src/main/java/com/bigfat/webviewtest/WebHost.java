package com.bigfat.webviewtest;

import android.content.Context;
import android.widget.Toast;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/8
 */
public class WebHost {

    private Context mContext;

    public WebHost(Context context) {
        this.mContext = mContext;
    }

    public void callJs(){
        Toast.makeText(mContext,"call from js",Toast.LENGTH_LONG).show();
    }
}

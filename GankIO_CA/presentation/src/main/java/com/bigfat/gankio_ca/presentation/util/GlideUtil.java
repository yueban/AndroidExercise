package com.bigfat.gankio_ca.presentation.util;

import android.content.Context;
import com.bigfat.gankio_ca.presentation.R;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

/**
 * Created by yueban on 22:30 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class GlideUtil {
    public static DrawableRequestBuilder<String> url(Context context) {
        return Glide.with(context)
            .fromString();
            //.placeholder(R.mipmap.ic_launcher)
            //.error(R.mipmap.ic_launcher);
    }
}

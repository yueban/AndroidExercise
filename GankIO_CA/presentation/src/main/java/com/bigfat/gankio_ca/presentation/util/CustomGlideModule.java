package com.bigfat.gankio_ca.presentation.util;

import android.content.Context;
import com.bigfat.gankio_ca.presentation.common.di.components.DaggerExecutorComponent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import java.util.concurrent.ExecutorService;

/**
 * Created by yueban on 22:01 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class CustomGlideModule implements GlideModule {
    private final ExecutorService mExecutorService;

    public CustomGlideModule() {
        mExecutorService = DaggerExecutorComponent.builder().build().executorService();
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder
            .setResizeService(mExecutorService)
            .setDiskCacheService(mExecutorService);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}

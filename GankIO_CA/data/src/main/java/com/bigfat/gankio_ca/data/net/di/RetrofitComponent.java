package com.bigfat.gankio_ca.data.net.di;

import dagger.Component;
import javax.inject.Singleton;
import retrofit2.Retrofit;

/**
 * Created by yueban on 15:03 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
@Component(modules = RetrofitModule.class)
public interface RetrofitComponent {
    Retrofit retrofit();
}

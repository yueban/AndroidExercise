package com.bigfat.gankio_ca.data.net.di;

import com.bigfat.gankio_ca.data.net.GankApi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yueban on 14:51 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class RetrofitModule {
    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();

        return new Retrofit.Builder().baseUrl(GankApi.URL_BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    }
}

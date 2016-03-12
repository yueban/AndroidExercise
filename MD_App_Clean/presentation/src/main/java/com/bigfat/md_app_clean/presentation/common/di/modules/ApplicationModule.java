package com.bigfat.md_app_clean.presentation.common.di.modules;

import android.content.Context;
import com.bigfat.md_app_clean.data.cache.LoginCache;
import com.bigfat.md_app_clean.data.cache.LoginCacheImpl;
import com.bigfat.md_app_clean.data.datastore.LoginDataStore;
import com.bigfat.md_app_clean.data.datastore.LoginDataStoreImpl;
import com.bigfat.md_app_clean.data.net.NetConstant;
import com.bigfat.md_app_clean.data.net.converter.GsonConverterFactory;
import com.bigfat.md_app_clean.presentation.BuildConfig;
import com.bigfat.md_app_clean.presentation.common.App;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by yueban on 15:08 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class ApplicationModule {
    private final App mApp;

    public ApplicationModule(App app) {
        mApp = app;
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return mApp;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    //Request request = chain.request();
                    //HttpUrl url = request.url();
                    //String api = url.scheme() + "://" + url.host();
                    //if (api.equals(NetConstant.API)) {
                    //    Request.Builder builder = request.newBuilder()
                    //        .header("format", "json");
                    //    request = builder.build();
                    //}
                    //Response response =chain.proceed(request);
                    //response.body().string();
                    //return response;

                    //TODO 替换access_token参数(loginAccountEntity !=null && url.startsWith(NetConstant.API))
                    //LoginAccountEntity loginAccountEntity = mApp.getLoginAccountEntity();
                    return chain.proceed(chain.request());
                }
            });
        //stetho网络监控
        if (BuildConfig.DEBUG) {
            clientBuilder.addNetworkInterceptor(new StethoInterceptor());
        }

        return new Retrofit.Builder()
            .baseUrl(NetConstant.API)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    }

    @Singleton
    @Provides
    LoginDataStore provideLoginDataStore(LoginDataStoreImpl loginDataStore) {
        return loginDataStore;
    }

    @Singleton
    @Provides
    LoginCache provideLoginCache(LoginCacheImpl loginCache) {
        return loginCache;
    }
}

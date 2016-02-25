package com.bigfat.dagger2demo.app;

import android.content.Context;
import com.bigfat.dagger2demo.App;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by yueban on 14:09 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class ApplicationModule {
    private final App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    @Singleton
    @Provides
    Context provideApplication() {
        return app;
    }
}

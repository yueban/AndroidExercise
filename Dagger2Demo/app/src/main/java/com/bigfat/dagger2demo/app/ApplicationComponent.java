package com.bigfat.dagger2demo.app;

import android.content.Context;
import com.bigfat.dagger2demo.MainActivity;
import com.bigfat.dagger2demo.coffee.DripCoffeeModule;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by yueban on 14:08 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
@Component(modules = { ApplicationModule.class, DripCoffeeModule.class })
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);

    Context context();
}

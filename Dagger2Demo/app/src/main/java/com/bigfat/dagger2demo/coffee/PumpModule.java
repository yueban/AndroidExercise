package com.bigfat.dagger2demo.coffee;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yueban on 10:30 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class PumpModule {
    @Provides
    Pump providePump(Thermosiphon pump) {
        return pump;
    }
}

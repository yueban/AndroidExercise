package com.bigfat.dagger2demo.coffee;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by yueban on 10:33 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module(includes = PumpModule.class)
public class DripCoffeeModule {
    @Singleton
    @Provides
    Heater provideHeater() {
        return new ElectricHeater();
    }

    @Singleton
    @Provides
    CoffeeMaker provideCoffeeMaker(Lazy<Heater> heater, Pump pump) {
        return new CoffeeMaker(heater, pump);
    }
}

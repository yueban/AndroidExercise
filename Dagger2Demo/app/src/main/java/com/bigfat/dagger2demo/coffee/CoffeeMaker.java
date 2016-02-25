package com.bigfat.dagger2demo.coffee;

import dagger.Lazy;
import javax.inject.Inject;

/**
 * Created by yueban on 10:35 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class CoffeeMaker {
    private final Lazy<Heater> heater;
    private final Pump pump;

    @Inject
    public CoffeeMaker(Lazy<Heater> heater, Pump pump) {
        this.heater = heater;
        this.pump = pump;
    }

    public void brew() {
        heater.get().on();
        pump.pump();
        System.out.println(" [_]P coffee! [_]P ");
        heater.get().off();
    }
}

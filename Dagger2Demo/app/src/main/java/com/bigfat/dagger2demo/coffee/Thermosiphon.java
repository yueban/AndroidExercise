package com.bigfat.dagger2demo.coffee;

import javax.inject.Inject;

/**
 * Created by yueban on 10:27 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class Thermosiphon implements Pump {
    private final Heater heater;

    @Inject
    public Thermosiphon(Heater heater) {
        this.heater = heater;
    }

    @Override
    public void pump() {
        if (heater.isHot()) {
            System.out.println("=> => pumping => =>");
        }
    }
}

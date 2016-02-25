package com.bigfat.dagger2demo.coffee;

import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by yueban on 10:37 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
@Component(modules = { DripCoffeeModule.class })
public interface Coffee {
    CoffeeMaker maker();
}

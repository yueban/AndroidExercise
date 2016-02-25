package com.bigfat.dagger2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.bigfat.dagger2demo.app.ApplicationComponent;
import com.bigfat.dagger2demo.coffee.Coffee;
import com.bigfat.dagger2demo.coffee.CoffeeMaker;
import com.bigfat.dagger2demo.coffee.DaggerCoffee;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    CoffeeMaker coffeeMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test1();
        test2();
    }

    private void test1() {
        Coffee coffee = DaggerCoffee.create();
        coffee.maker().brew();
    }

    private void test2() {
        ApplicationComponent applicationComponent = ((App) getApplication()).getApplicationComponent();
        applicationComponent.inject(this);
        Log.d("test2", "applicationComponent.context()--->" + applicationComponent.context().toString());
        coffeeMaker.brew();
    }
}

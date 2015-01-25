package com.bigfat.qq_50_slidingmenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.bigfat.qq_50_slidingmenu.view.SlidingMenu;


public class MainActivity extends ActionBarActivity {

    private SlidingMenu slidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
    }

    public void toggleMenu(View view) {
        slidingMenu.toggle();
    }
}

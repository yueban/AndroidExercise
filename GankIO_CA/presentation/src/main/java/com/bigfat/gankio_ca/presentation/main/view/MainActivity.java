package com.bigfat.gankio_ca.presentation.main.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.bigfat.gankio_ca.domain.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.R;
import com.bigfat.gankio_ca.presentation.common.ui.BaseActivity;
import com.bigfat.gankio_ca.presentation.common.di.HasComponent;
import com.bigfat.gankio_ca.presentation.main.di.DaggerGankMainComponent;
import com.bigfat.gankio_ca.presentation.main.di.GankMainComponent;
import com.bigfat.gankio_ca.presentation.main.di.GankMainModule;

public class MainActivity extends BaseActivity implements HasComponent<GankMainComponent>, MainActivityFragment.Listener {

    private GankMainComponent mGankMainComponent;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeInjector();
    }

    private void initializeInjector() {
        mGankMainComponent = DaggerGankMainComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
            .gankMainModule(new GankMainModule("福利", 1))
            .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public GankMainComponent getComponent() {
        return mGankMainComponent;
    }

    @Override
    public void onGankEntityClick(GankEntity gankEntity) {

    }
}

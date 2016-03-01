package com.bigfat.gankio_ca.presentation.ui.meizhi.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.R;
import com.bigfat.gankio_ca.presentation.common.di.HasComponent;
import com.bigfat.gankio_ca.presentation.common.di.components.DaggerUseCaseComponent;
import com.bigfat.gankio_ca.presentation.common.di.components.UseCaseComponent;
import com.bigfat.gankio_ca.presentation.common.ui.BaseActivity;

public class MeizhiActivity extends BaseActivity implements HasComponent<UseCaseComponent>, MeizhiFragment.Listener {

    private UseCaseComponent mUseCaseComponent;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MeizhiActivity.class);
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
        mUseCaseComponent = DaggerUseCaseComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
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
    public UseCaseComponent getComponent() {
        return mUseCaseComponent;
    }

    @Override
    public void onGankEntityClick(GankEntity gankEntity) {
        Toast.makeText(this, gankEntity.toString(), Toast.LENGTH_SHORT).show();
    }
}

package com.bigfat.appcompattest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bigfat.appcompattest.util.LogUtil;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tb_main);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LogUtil.i("action_settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

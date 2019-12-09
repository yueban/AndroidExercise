package com.juphoon.cloud.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.juphoon.cloud.JCClient;
import com.juphoon.cloud.JCMediaChannel;
import com.juphoon.cloud.sample.JCWrapper.JCCallUtils;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCLoginEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.pgyersdk.update.PgyUpdateManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Toolbar mToolbar;

    private TabLayout mTabLayout;

    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getTitle() + "-" + BuildConfig.VERSION_NAME);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        EventBus.getDefault().register(this);
        requestPermission();

        if (!JCManager.getInstance().loginIfLastLogined()) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        updateTitle();

        mFloatingActionButton.setVisibility(isMediaRunning() ? View.VISIBLE : View.INVISIBLE);
        mViewPager.setOffscreenPageLimit(getResources().getStringArray(R.array.tab_titles).length);

        PgyUpdateManager.setIsForced(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            JCManager.getInstance().client.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                System.exit(1);
            }
        }
        PgyUpdateManager.register(this);
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.LOGOUT) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (event.getEventType() == JCEvent.EventType.LOGIN) {
            JCLoginEvent login = (JCLoginEvent) event;
            if (!login.result) {
                startActivity(new Intent(this, LoginActivity.class));
            }
        } else if (event.getEventType() == JCEvent.EventType.CLIENT_STATE_CHANGE) {
            updateTitle();
        } else if (event.getEventType() == JCEvent.EventType.CALL_ADD) {
            startActivity(new Intent(this, CallActivity.class));
            mFloatingActionButton.setVisibility(isMediaRunning() ? View.VISIBLE : View.INVISIBLE);
        } else if (event.getEventType() == JCEvent.EventType.CALL_REMOVE) {
            mFloatingActionButton.setVisibility(isMediaRunning() ? View.VISIBLE : View.INVISIBLE);
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_JOIN) {
            mFloatingActionButton.setVisibility(isMediaRunning() ? View.VISIBLE : View.INVISIBLE);
        } else if (event.getEventType() == JCEvent.EventType.CONFERENCE_LEAVE) {
            mFloatingActionButton.setVisibility(isMediaRunning() ? View.VISIBLE : View.INVISIBLE);
        } else if (event.getEventType() == JCEvent.EventType.Exit) {
            finish();
        }
    }

    public void onFloatButton(View view) {
        if (JCManager.getInstance().call.getCallItems().size() > 0) {
            startActivity(new Intent(this, CallActivity.class));
        } else if (JCManager.getInstance().mediaChannel.getState() != JCMediaChannel.STATE_IDLE) {
            startActivity(new Intent(this, ConferenceActivity.class));
        }
    }

    private boolean isMediaRunning() {
        return !JCCallUtils.isIdle()
                || JCManager.getInstance().mediaChannel.getState() != JCMediaChannel.STATE_IDLE;
    }

    private void updateTitle() {
        int state = JCManager.getInstance().client.getState();
        if (state == JCClient.STATE_LOGINED) {
            mToolbar.setSubtitle(JCManager.getInstance().client.getUserId());
        } else if (state == JCClient.STATE_IDLE) {
            mToolbar.setSubtitle(R.string.not_login);
        } else if (state == JCClient.STATE_LOGINING) {
            mToolbar.setSubtitle(R.string.logining);
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1000);
            } else {
                PgyUpdateManager.register(this);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new CallFragment();
            } else if (position == 1) {
                return new ConferenceFragment();
            } else if (position == 2) {
                return new MessageFragment();
            } else if (position == 3) {
                return new StorageFragment();
            } else if (position == 4) {
                return new GroupFragment();
            } else {
                return new AccountFragment();
            }
        }

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.tab_titles).length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tab_titles)[position];
        }
    }
}

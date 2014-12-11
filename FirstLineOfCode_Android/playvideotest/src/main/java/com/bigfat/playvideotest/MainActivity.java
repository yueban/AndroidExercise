package com.bigfat.playvideotest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import java.io.File;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private VideoView videoView;
    private Button play;
    private Button pause;
    private Button replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = (VideoView) findViewById(R.id.video_view);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        replay = (Button) findViewById(R.id.replay);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);

        initVideoPath();
    }

    private void initVideoPath() {
        File file = new File(Environment.getExternalStorageDirectory(), "梦幻西游华丽3D宣传CG.mkv");
        Log.d(TAG, file.getPath());
        if (file.exists()) {
            videoView.setVideoPath(file.getPath());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!videoView.isPlaying()) {
                    videoView.start();
                }
                break;

            case R.id.pause:
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
                break;

            case R.id.replay:
                if (videoView.isPlaying()) {
                    videoView.resume();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
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
}

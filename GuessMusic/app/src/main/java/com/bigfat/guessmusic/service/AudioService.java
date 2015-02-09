package com.bigfat.guessmusic.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.bigfat.guessmusic.constant.Constant;
import com.bigfat.guessmusic.util.Utils;

import java.io.IOException;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/8
 */
public class AudioService extends Service {

    public static final String TAG = "AudioService";

    private MediaPlayer mediaPlayer;//歌曲播放器
    private MediaPlayer[] toneMediaPlayer = new MediaPlayer[Constant.TONE_INFO.length];//音效播放器

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getIntExtra(Constant.EXTRA_COMMAND, -1)) {
            case Constant.COMMAND_SONG_PLAY:
                playSong(intent.getStringExtra(Constant.EXTRA_SONG_NAME));
                break;

            case Constant.COMMAND_SONG_STOP:
                stopSong();
                break;

            case Constant.COMMAND_TONE_PLAY:
                playTone(intent.getIntExtra(Constant.EXTRA_TONE_INDEX, -1));
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 播放歌曲
     *
     * @param fileName 歌曲文件名
     */
    private void playSong(String fileName) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        AssetFileDescriptor assetFileDescriptor = Utils.getAssetFileDescriptor(AudioService.this, fileName);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放歌曲
     */
    private void stopSong() {
        mediaPlayer.stop();
    }

    /**
     * 播放音效
     */
    private void playTone(int toneIndex) {
        if (toneMediaPlayer[toneIndex] == null) {
            toneMediaPlayer[toneIndex] = new MediaPlayer();
            toneMediaPlayer[toneIndex].reset();
            AssetFileDescriptor assetFileDescriptor = Utils.getAssetFileDescriptor(AudioService.this, Constant.TONE_INFO[toneIndex]);
            try {
                toneMediaPlayer[toneIndex].setDataSource(assetFileDescriptor.getFileDescriptor(),
                        assetFileDescriptor.getStartOffset(),
                        assetFileDescriptor.getLength());
                toneMediaPlayer[toneIndex].prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        toneMediaPlayer[toneIndex].start();
    }
}
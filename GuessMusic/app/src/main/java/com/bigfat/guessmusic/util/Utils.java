package com.bigfat.guessmusic.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigfat.guessmusic.R;
import com.bigfat.guessmusic.constant.Constant;
import com.bigfat.guessmusic.listener.IAlertDialogButtonListener;
import com.bigfat.guessmusic.service.AudioService;
import com.bigfat.guessmusic.ui.MyApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/2
 */
public class Utils {

    /**
     * 随机获得一个汉字
     */
    public static String getRandomHanzi() {
        String str = "";
        Random rand = new Random();
        int highPos = 176 + rand.nextInt(39);
        int lowPos = 161 + rand.nextInt(93);
        byte[] b = new byte[2];
        b[0] = Integer.valueOf(highPos).byteValue();
        b[1] = Integer.valueOf(lowPos).byteValue();
        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 从资源文件中读取删除文字需要的金币数
     */
    public static int getDeleteWordCoins() {
        return MyApplication.getContext().getResources().getInteger(R.integer.pay_delete_word);
    }

    /**
     * 从资源文件中读取提示需要的金币数
     */
    public static int getTipAnswerCoins() {
        return MyApplication.getContext().getResources().getInteger(R.integer.pay_tip_answer);
    }

    /**
     * 显示自定义Dialog
     *
     * @param context  上下文对象
     * @param message  内容
     * @param callback 回调
     */
    public static void showCustomeDialog(final Context context, String message, final IAlertDialogButtonListener callback) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_view, null);
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.Theme_Transparent).setView(dialogView).create();

        //初始化自定义DialogView
        TextView tVMessage = (TextView) dialogView.findViewById(R.id.text_dialog_message);
        ImageButton btnOk = (ImageButton) dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancel = (ImageButton) dialogView.findViewById(R.id.btn_dialog_cancel);

        tVMessage.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null) {
                    callback.onOkButtonClick();
                }
                playTone(context, Constant.TONE_ENTER_INDEX);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                playTone(context, Constant.TONE_CANCEL_INDEX);
            }
        });

        //显示Dialog
        dialog.show();
    }

    /**
     * 播放歌曲
     */
    public static void playSong(Context context, String fileName) {
        Intent intent = new Intent(context, AudioService.class);
        intent.putExtra(Constant.EXTRA_COMMAND, Constant.COMMAND_SONG_PLAY);
        intent.putExtra(Constant.EXTRA_SONG_NAME, fileName);
        context.startService(intent);
    }

    /**
     * 停止播放歌曲
     */
    public static void stopSong(Context context) {
        Intent intent = new Intent(context, AudioService.class);
        intent.putExtra(Constant.EXTRA_COMMAND, Constant.COMMAND_SONG_STOP);
        context.startService(intent);
    }

    /**
     * 得到assets文件的AssetFileDescriptor对象
     *
     * @param fileName assets文件名
     */
    public static AssetFileDescriptor getAssetFileDescriptor(Context context, String fileName) {
        try {
            return context.getAssets().openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 播放音效
     *
     * @param toneIndex 音效索引
     */
    public static void playTone(Context context, int toneIndex) {
        Intent intent = new Intent(context, AudioService.class);
        intent.putExtra(Constant.EXTRA_COMMAND, Constant.COMMAND_TONE_PLAY);
        intent.putExtra(Constant.EXTRA_TONE_INDEX, toneIndex);
        context.startService(intent);
    }
}

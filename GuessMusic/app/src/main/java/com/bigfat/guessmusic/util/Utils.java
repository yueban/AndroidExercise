package com.bigfat.guessmusic.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigfat.guessmusic.R;
import com.bigfat.guessmusic.observer.IAlertDialogButtonListener;
import com.bigfat.guessmusic.ui.MyApplication;

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
    public static void showCustomeDialog(Context context, String message, final IAlertDialogButtonListener callback) {
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
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        //显示Dialog
        dialog.show();
    }
}

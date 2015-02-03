package com.bigfat.guessmusic.util;

import com.bigfat.guessmusic.R;
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
}

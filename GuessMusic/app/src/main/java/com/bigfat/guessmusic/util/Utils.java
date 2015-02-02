package com.bigfat.guessmusic.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/2
 */
public class Utils {
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
//        Log.i("myInfo", str);
        return str;
    }
}

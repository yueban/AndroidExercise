package com.bigfat.game_pintu.util;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/17
 */
public class Utils {
    /**
     * 得到一组整数中的最小值
     */
    public static int min(int... params) {
        int min = params[0];
        for (int param : params) {
            if (param < min) {
                min = param;
            }
        }
        return min;
    }

    /**
     * 根据tag获取ImageId
     */
    public static int getImageIdByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

    /**
     * 根据tag获取Index
     */
    public static int getIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }
}

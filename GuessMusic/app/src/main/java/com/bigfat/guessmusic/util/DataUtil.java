package com.bigfat.guessmusic.util;

import android.content.Context;

import com.bigfat.guessmusic.constant.Constant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/9
 */
public class DataUtil {

    public static final String TAG = "DataUtil";

    /**
     * 存储游戏信息
     *
     * @param context    上下文对象
     * @param stageIndex 当前关卡索引，因为初始化关卡时会将关卡索引+1，所以存储时要先-1
     * @param coins      金币数
     */
    public static void saveData(Context context, int stageIndex, int coins) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Constant.FILE_NAME_SAVE_DATA, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);

            dos.writeInt(stageIndex);
            dos.writeInt(coins);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取游戏数据
     *
     * @param context 上下文对象
     * @return 游戏数据
     */
    public static int[] loadData(Context context) {
        int[] data = {-1, Constant.TOTAL_COINS};//游戏数据默认值
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(Constant.FILE_NAME_SAVE_DATA);
            DataInputStream dis = new DataInputStream(fis);

            data[Constant.INDEX_SAVE_DATA_STAGE] = dis.readInt();
            data[Constant.INDEX_SAVE_DATA_COIN] = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}

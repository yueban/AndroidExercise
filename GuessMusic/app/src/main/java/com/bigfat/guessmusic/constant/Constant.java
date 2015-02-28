package com.bigfat.guessmusic.constant;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/2
 */
public class Constant {
    /**
     * 待选文字总数
     */
    public static final int WORD_COUNT = 24;

    /**
     * 答案错误闪烁次数
     */
    public static final int SPLASH_TIMES = 6;

    /**
     * 初始金币总数
     */
    public static final int TOTAL_COINS = 9000;

    /**
     * 每关金币奖励
     */
    public static final int REWARD_COINS = 50;

    /**
     * 歌曲数组中文件名索引
     */
    public static final int INDEX_SONG_INFO_FILE_NAME = 0;

    /**
     * 歌曲数组中歌曲名索引
     */
    public static final int INDEX_SONG_INFO_NAME = 1;

    /**
     * 歌曲信息
     */
    public static final String[][] SONG_INFO = {
            {"__00000.m4a", "征服"},
            {"__00001.m4a", "童话"},
            {"__00002.m4a", "同桌的你"},
            {"__00003.m4a", "七里香"},
            {"__00004.m4a", "传奇"},
            {"__00005.m4a", "大海"},
            {"__00006.m4a", "后来"},
            {"__00007.m4a", "你的背包"},
            {"__00008.m4a", "再见"},
            {"__00009.m4a", "老男孩"},
            {"__00010.m4a", "龙的传人"},
    };

    public static final int INDEX_TONE_CANCEL = 0;
    public static final int INDEX_TONE_COIN = 1;
    public static final int INDEX_TONE_ENTER = 2;

    /**
     * 音效信息
     */
    public static final String[] TONE_INFO = {
            "cancel.mp3",
            "coin.mp3",
            "enter.mp3"
    };

    /**
     * AudioService命令的键
     */
    public static final String EXTRA_COMMAND = "extra_command";

    /**
     * 播放歌曲
     */
    public static final int COMMAND_SONG_PLAY = 1001;

    /**
     * 停止歌曲
     */
    public static final int COMMAND_SONG_STOP = 1002;

    /**
     * 播放音效
     */
    public static final int COMMAND_TONE_PLAY = 1003;

    /**
     * Intent传参歌曲文件名的键
     */
    public static final String EXTRA_SONG_NAME = "extra_song_name";

    /**
     * Intent传参音效的键
     */
    public static final String EXTRA_TONE_INDEX = "extra_tone_index";

    /**
     * 存储数据文件名
     */
    public static final String FILE_NAME_SAVE_DATA = "single";

    /**
     * 存储文件中关卡数的索引
     */
    public static final int INDEX_SAVE_DATA_STAGE = 0;

    /**
     * 存储文件中金币数的索引
     */
    public static final int INDEX_SAVE_DATA_COIN = 1;
}
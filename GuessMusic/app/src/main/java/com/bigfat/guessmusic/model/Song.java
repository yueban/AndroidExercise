package com.bigfat.guessmusic.model;

/**
 * 歌曲类
 *
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/2
 */
public class Song {
    private String name;//歌曲名
    private String fileName;//歌曲文件名

    public char[] getNameCharacters() {
        return name.toCharArray();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

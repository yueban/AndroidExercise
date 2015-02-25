package com.bigfat.game_pintu.util;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/25
 */
public interface OnGamePintuListener {
    void nextLevel(int nextLevel);

    void timeChanged(int currentTime);

    void gameOver();
}

package com.bigfat.guessmusic.observer;

import android.widget.Button;

import com.bigfat.guessmusic.model.Word;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/2
 */
public interface WordClickListener {
    void onWordClick(Button wordButton, Word word);
}

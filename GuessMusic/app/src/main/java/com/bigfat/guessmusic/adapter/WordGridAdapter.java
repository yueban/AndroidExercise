package com.bigfat.guessmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.bigfat.guessmusic.R;
import com.bigfat.guessmusic.model.Word;
import com.bigfat.guessmusic.observer.WordClickListener;

import java.util.ArrayList;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/2
 */
public class WordGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Word> data;
    private WordClickListener wordClickListener;//文字点击回调

    public WordGridAdapter(Context context, ArrayList<Word> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Word getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Button wordButton;
        final Word word = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.word_grid_view_item, parent, false);
            wordButton = (Button) convertView.findViewById(R.id.word_grid_view_item_btn);
            convertView.setTag(wordButton);
        } else {
            wordButton = (Button) convertView.getTag();
        }
        word.setIndex(position);
        wordButton.setText(word.getText());
        wordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordClickListener.onWordClick(wordButton, word);
            }
        });
        return convertView;
    }

    public void setWordClickListener(WordClickListener wordClickListener) {
        this.wordClickListener = wordClickListener;
    }
}
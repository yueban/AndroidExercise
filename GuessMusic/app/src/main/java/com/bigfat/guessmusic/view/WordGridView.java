package com.bigfat.guessmusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.bigfat.guessmusic.R;

import java.util.ArrayList;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/31
 */
public class WordGridView extends GridView {
    private ArrayList<WordButton> mArrayList = new ArrayList<>();
    private WordGridAdapter mAdatper;
    private Context mContext;

    public WordGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAdatper = new WordGridAdapter();
        this.setAdapter(mAdatper);
    }

    class WordGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final WordButton holder;
            if (convertView == null) {
                holder = new WordButton();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.word_grid_view_item, parent, false);
                holder.mIndex = position;
                holder.mViewButton = (Button) convertView.findViewById(R.id.word_grid_view_item_btn);
                convertView.setTag(holder);
            } else {
                holder = (WordButton) convertView.getTag();
            }
            holder.mViewButton.setText(holder.mWordText);
            return convertView;
        }
    }
}

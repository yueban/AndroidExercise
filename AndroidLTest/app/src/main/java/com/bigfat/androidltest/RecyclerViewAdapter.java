package com.bigfat.androidltest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigfat.androidltest.model.Paper;

import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/11
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Paper> paperList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<Paper> paperList) {
        this.mContext = context;
        this.paperList = paperList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Paper paper = paperList.get(i);
        viewHolder.mContext = mContext;
        viewHolder.pic.setImageResource(paper.getPic());
        viewHolder.name.setText(paper.getName());
    }

    @Override
    public int getItemCount() {
        return paperList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ImageView pic;
        private TextView name;

        public ViewHolder(View v) {
            super(v);
            pic = (ImageView) v.findViewById(R.id.img_list_item_pic);
            name = (TextView) v.findViewById(R.id.tv_list_item_name);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).startActivity(v, getPosition());
                }
            });
        }
    }
}

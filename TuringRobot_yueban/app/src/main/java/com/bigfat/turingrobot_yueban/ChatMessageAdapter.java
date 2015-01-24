package com.bigfat.turingrobot_yueban;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigfat.turingrobot_yueban.bin.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/23
 */
public class ChatMessageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ChatMessage> datas;

    public ChatMessageAdapter(Context context, List<ChatMessage> datas) {
        this.mInflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType() == ChatMessage.Type.INCOMING ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = datas.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (getItemViewType(position)) {
                case 0:
                    convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
                    holder.date = (TextView) convertView.findViewById(R.id.from_msg_date);
                    holder.msg = (TextView) convertView.findViewById(R.id.from_msg_msg);
                    convertView.setTag(holder);
                    break;

                case 1:
                    convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
                    holder.date = (TextView) convertView.findViewById(R.id.to_msg_date);
                    holder.msg = (TextView) convertView.findViewById(R.id.to_msg_msg);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.date.setText(sdf.format(chatMessage.getDate()));
        holder.msg.setText(chatMessage.getMsg());
        return convertView;
    }

    private final class ViewHolder {
        TextView date;
        TextView msg;
    }
}

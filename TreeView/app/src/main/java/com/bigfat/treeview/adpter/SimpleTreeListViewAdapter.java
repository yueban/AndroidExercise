package com.bigfat.treeview.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigfat.treeview.R;
import com.bigfat.treeview.utils.Node;
import com.bigfat.treeview.utils.adapter.TreeListViewAdapter;

import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/12
 */
public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter {

    public SimpleTreeListViewAdapter(Context context, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        super(context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_tree_node, parent, false);
            holder.icon = (ImageView) convertView.findViewById(R.id.id_item_icon);
            holder.text = (TextView) convertView.findViewById(R.id.id_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (node.getIcon() == -1) {
            holder.icon.setVisibility(View.INVISIBLE);
        } else {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(node.isExpand() ? R.mipmap.tree_ex : R.mipmap.tree_ec);
        }
        holder.text.setText(node.getName());
        return convertView;
    }

    private final class ViewHolder {
        ImageView icon;
        TextView text;
    }
}

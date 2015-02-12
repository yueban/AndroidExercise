package com.bigfat.treeview.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.bigfat.treeview.utils.Node;
import com.bigfat.treeview.utils.TreeHelper;
import com.bigfat.treeview.utils.listener.OnTreeListViewItemClickListener;

import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/12
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter implements AdapterView.OnItemClickListener {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<Node> nodes;
    protected List<Node> visibleNodes;
    protected int defaultExpandLevel;
    //供外部调用处理Item点击事件
    private OnTreeListViewItemClickListener onTreeListViewItemClickListener;

    public TreeListViewAdapter(Context context, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.nodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        this.visibleNodes = TreeHelper.filterVisibleNodes(nodes);
        this.defaultExpandLevel = defaultExpandLevel;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        expandOrCollapse(position);
        if (onTreeListViewItemClickListener != null) {
            onTreeListViewItemClickListener.onClick(getItem(position), position);
        }
    }

    /**
     * 点击展开或收缩
     *
     * @param position 点击位置
     */
    private void expandOrCollapse(int position) {
        Node n = getItem(position);
        if (n != null) {
            if (n.isLeaf()) {//点击叶节点
                return;
            }
            //设置节点展开或收缩
            n.setExpand(!n.isExpand());
            //重新过滤要显示的节点
            visibleNodes = TreeHelper.filterVisibleNodes(nodes);
            //刷新View
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return visibleNodes.size();
    }

    @Override
    public Node getItem(int position) {
        return visibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = getItem(position);
        convertView = getConvertView(node, position, convertView, parent);
        //设置节点缩进
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);

        return convertView;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);
}

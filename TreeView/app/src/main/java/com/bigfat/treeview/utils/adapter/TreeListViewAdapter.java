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
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<Node> mAllNodes;
    protected List<Node> mVisibleNodes;
    protected int mDefaultExpandLevel;
    //供外部调用处理Item点击事件
    private OnTreeListViewItemClickListener mOnTreeListViewItemClickListener;

    public TreeListViewAdapter(Context context, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        this.mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        this.mDefaultExpandLevel = defaultExpandLevel;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        expandOrCollapse(position);
        if (mOnTreeListViewItemClickListener != null) {
            mOnTreeListViewItemClickListener.onClick(getItem(position), position);
        }
    }

    /**
     * 供以外部处理Item点击事件
     */
    public void setOnTreeListViewItemClickListener(OnTreeListViewItemClickListener onTreeListViewItemClickListener) {
        this.mOnTreeListViewItemClickListener = onTreeListViewItemClickListener;
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
            //刷新View
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Node getItem(int position) {
        return mVisibleNodes.get(position);
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

    @Override
    public void notifyDataSetChanged() {
        //重新过滤要显示的节点
        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        super.notifyDataSetChanged();
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);

    /**
     * 动态插入节点
     *
     * @param position 插入节点的位置
     * @param s        插入节点的文本
     */
    public void addExtraNode(int position, String s) {
        //获取插入节点位置
        Node node = getItem(position);
        int indexOf = mAllNodes.indexOf(node);
        //生成新插入的节点
        Node extraNode = new Node(-1, node.getId(), s);
        extraNode.setParent(node);
        //设置关联
        node.getChildren().add(extraNode);
        //插入新节点
        mAllNodes.add(indexOf + 1, extraNode);
        //设置展开
        node.setExpand(true);
        //刷新View
        notifyDataSetChanged();
    }
}

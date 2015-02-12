package com.bigfat.treeview.utils;

import com.bigfat.treeview.R;
import com.bigfat.treeview.annotation.TreeNodeId;
import com.bigfat.treeview.annotation.TreeNodeLabel;
import com.bigfat.treeview.annotation.TreeNodePId;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/10
 */
public class TreeHelper {

    /**
     * 将用户数据转换为序列化的树形数据
     *
     * @param datas              用户数据
     * @param defaultExpandLevel 默认展开层级
     * @return 序列换的树形数据
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();//排序后的结果
        List<Node> nodes = convertDatas2Nodes(datas);//转换的树形数据
        //获得树的根节点
        List<Node> rootNodes = getRootNodes(nodes);
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 过滤出需要显示的节点
     *
     * @param nodes 序列化树形数据
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        for (Node n : nodes) {
            if (n.isRoot() || n.isParentExpand()) {
                setNodeIcon(n);
                result.add(n);
            }
        }
        return result;
    }

    /**
     * 遍历根节点像序列化树形数据中添加节点
     *
     * @param result             序列化树形数据
     * @param node               根节点
     * @param defaultExpandLevel 默认展开层级
     * @param currentLevel       当前添加节点层级
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        //加入当前节点
        result.add(node);
        //判断当前节点是否展开
        if (defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        }
        //如果是叶子节点则表示当前分支遍历完毕
        if (node.isLeaf()) {
            return;
        }
        //不是叶子节点则继续遍历
        for (Node n : node.getChildren()) {
            addNode(result, n, defaultExpandLevel, currentLevel + 1);
        }
    }

    /**
     * 获得树的根节点
     *
     * @param nodes 树形数据
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<>();
        for (Node n : nodes) {
            if (n.isRoot()) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }

    /**
     * 将用户的数据转换为树形数据
     *
     * @param datas 用户数据
     * @param <T>   用户数据类型
     * @return 转换后的树形数据
     */
    private static <T> List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        Node node = null;
        for (T t : datas) {
            int id = -1;
            int pId = -1;
            String label = null;
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);
                } else if (field.getAnnotation(TreeNodePId.class) != null) {
                    field.setAccessible(true);
                    pId = field.getInt(t);
                } else if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }
            }
            node = new Node(id, pId, label);
            nodes.add(node);
        }
        //设置父子关系
        for (int i = 0; i < nodes.size(); i++) {
            Node n1 = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node n2 = nodes.get(j);
                if (n1.getpId() == n2.getId()) {//n2是n1的父亲
                    n2.getChildren().add(n1);
                    n1.setParent(n2);
                } else if (n1.getId() == n2.getpId()) {//n2是n1的孩子
                    n2.setParent(n1);
                    n1.getChildren().add(n2);
                }
            }
        }
        //设置节点展开状态图片
        for (Node n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    /**
     * 设置节点展开状态图片
     */
    private static void setNodeIcon(Node node) {
        if (node.getChildren().size() <= 0) {//没有孩子
            node.setIcon(-1);
        } else if (node.isExpand()) {//有孩子且展开
            node.setIcon(R.mipmap.tree_ex);
        } else {//有孩子未展开
            node.setIcon(R.mipmap.tree_ec);
        }
    }
}

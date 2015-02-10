package com.bigfat.treeview.utils;

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
     * 将用户的数据转换为树形数据
     *
     * @param datas 用户数据
     * @param <T>   用户数据类型
     * @return 转换后的树形数据
     */
    public static <T> List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException {
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
        return nodes;
    }
}

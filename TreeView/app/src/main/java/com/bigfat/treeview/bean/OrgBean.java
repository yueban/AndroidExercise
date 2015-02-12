package com.bigfat.treeview.bean;

import com.bigfat.treeview.annotation.TreeNodeId;
import com.bigfat.treeview.annotation.TreeNodeLabel;
import com.bigfat.treeview.annotation.TreeNodePId;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/10
 */
public class OrgBean {
    @TreeNodeId
    private int _id;
    @TreeNodePId
    private int parentId;
    @TreeNodeLabel
    private String name;

    public OrgBean(int _id, int parentId, String name) {
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

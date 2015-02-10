package com.bigfat.treeview.bean;

import com.bigfat.treeview.annotation.TreeNodeId;
import com.bigfat.treeview.annotation.TreeNodeLabel;
import com.bigfat.treeview.annotation.TreeNodePId;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/10
 */
public class FileBean {
    @TreeNodeId
    private int id;
    @TreeNodePId
    private int pId;
    @TreeNodeLabel
    private String label;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

package com.bigfat.scrollviewedittexttest;

import java.util.List;

/**
 * Created by yueban on 26/7/15.
 */
public class Page implements com.bigfat.draggedviewpager.model.Page<Item> {
    private List<Item> data;

    public Page(List<Item> data) {
        this.data = data;
    }

    @Override
    public List<Item> getData() {
        return data;
    }

    @Override
    public void setData(List<Item> data) {
        this.data = data;
    }
}

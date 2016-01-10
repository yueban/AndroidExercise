package com.bigfat.listviewdragdemo;

import java.util.ArrayList;

/**
 * 阶段Model
 * Created by yueban on 13/7/15.
 */
public class Section {
    private String title;
    private ArrayList<String> data;

    public Section() {
    }

    public Section(String title, ArrayList<String> data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Section{" +
                "title='" + title + '\'' +
                ", data=" + data +
                '}';
    }
}

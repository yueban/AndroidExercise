package com.bigfat.android50test;

import java.util.ArrayList;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/21
 */
public class DemoApp {

    /**
     * 获取要显示的数据（初始化数据）
     *
     * @param size 数据长度（数量）
     */
    public static ArrayList<SampleModel> getSampleData(int size) {
        ArrayList<SampleModel> sampleData = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            sampleData.add(new SampleModel("新的列表项<" + i + ">"));
        }
        return sampleData;
    }
}

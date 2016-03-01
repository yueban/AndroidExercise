package com.bigfat.gankio_ca.data.entity;

import java.util.List;

/**
 * 每日数据
 * Created by yueban on 18:05 23/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DayEntity extends BaseEntity {
    private DayResultsEntity results;
    private List<String> category;

    public DayResultsEntity getResults() {
        return results;
    }

    public void setResults(DayResultsEntity results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "DayEntity{" +
            "results=" + results +
            ", category=" + category +
            '}';
    }
}

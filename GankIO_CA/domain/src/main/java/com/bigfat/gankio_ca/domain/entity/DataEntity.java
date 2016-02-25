package com.bigfat.gankio_ca.domain.entity;

import java.util.List;

/**
 * 数据列表
 * Created by yueban on 18:02 23/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DataEntity extends BaseEntity {
    private List<GankEntity> results;

    public List<GankEntity> getResults() {
        return results;
    }

    public void setResults(List<GankEntity> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
            "results=" + results +
            '}';
    }
}

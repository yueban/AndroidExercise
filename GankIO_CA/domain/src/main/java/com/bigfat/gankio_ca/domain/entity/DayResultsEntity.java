package com.bigfat.gankio_ca.domain.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 每日数据内容
 * Created by yueban on 22:25 23/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DayResultsEntity {
    @SerializedName("iOS")
    private List<GankEntity> iosList;
    @SerializedName("Android")
    private List<GankEntity> androidList;
    @SerializedName("瞎推荐")
    private List<GankEntity> recommendList;
    @SerializedName("拓展资源")
    private List<GankEntity> extendList;
    @SerializedName("福利")
    private List<GankEntity> benefitList;
    @SerializedName("休息视频")
    private List<GankEntity> restVideoList;

    public List<GankEntity> getIosList() {
        return iosList;
    }

    public void setIosList(List<GankEntity> iosList) {
        this.iosList = iosList;
    }

    public List<GankEntity> getAndroidList() {
        return androidList;
    }

    public void setAndroidList(List<GankEntity> androidList) {
        this.androidList = androidList;
    }

    public List<GankEntity> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<GankEntity> recommendList) {
        this.recommendList = recommendList;
    }

    public List<GankEntity> getExtendList() {
        return extendList;
    }

    public void setExtendList(List<GankEntity> extendList) {
        this.extendList = extendList;
    }

    public List<GankEntity> getBenefitList() {
        return benefitList;
    }

    public void setBenefitList(List<GankEntity> benefitList) {
        this.benefitList = benefitList;
    }

    public List<GankEntity> getRestVideoList() {
        return restVideoList;
    }

    public void setRestVideoList(List<GankEntity> restVideoList) {
        this.restVideoList = restVideoList;
    }

    @Override
    public String toString() {
        return "DayResultsEntity{" +
            "iosList=" + iosList +
            ", androidList=" + androidList +
            ", recommendList=" + recommendList +
            ", extendList=" + extendList +
            ", benefitList=" + benefitList +
            ", restVideoList=" + restVideoList +
            '}';
    }
}

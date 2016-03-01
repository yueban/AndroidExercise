package com.bigfat.gankio_ca.data.entity;

import com.bigfat.gankio_ca.data.database.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by yueban on 17:55 23/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Table(database = AppDataBase.class)
public class GankEntity extends BaseModel {
    /**
     * who : 张涵宇
     * publishedAt : 2016-02-23T05:08:46.837Z
     * desc : 2.23
     * type : 福利
     * url : http://ww4.sinaimg.cn/large/7a8aed7bjw1f19241kkpwj20f00hfabt.jpg
     * used : true
     * objectId : 56cbc2d37db2a20051a7227e
     * createdAt : 2016-02-23T02:24:19.518Z
     * updatedAt : 2016-02-23T05:08:48.046Z
     */
    @Column
    private String who;
    @Column
    private String publishedAt;
    @Column
    private String desc;
    @Column
    private String type;
    @Column
    private String url;
    @Column
    private boolean used;
    @PrimaryKey
    private String objectId;
    @Column
    private String createdAt;
    @Column
    private String updatedAt;
    /**
     * 为了瀑布流,记录图片宽高
     */
    @Column
    private int width;
    @Column
    private int height;

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "GankEntity{" +
            "who='" + who + '\'' +
            ", publishedAt='" + publishedAt + '\'' +
            ", desc='" + desc + '\'' +
            ", type='" + type + '\'' +
            ", url='" + url + '\'' +
            ", used=" + used +
            ", objectId='" + objectId + '\'' +
            ", createdAt='" + createdAt + '\'' +
            ", updatedAt='" + updatedAt + '\'' +
            ", width=" + width +
            ", height=" + height +
            '}';
    }
}

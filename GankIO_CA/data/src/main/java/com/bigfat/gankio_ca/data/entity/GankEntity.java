package com.bigfat.gankio_ca.data.entity;

import com.bigfat.gankio_ca.data.database.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import org.parceler.Parcel;

/**
 * Created by yueban on 17:55 23/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Parcel
@Table(database = AppDataBase.class)
public class GankEntity extends BaseModel {
    /**
     * _id : 56d8eb3a67765967efcbd69c
     * _ns : ganhuo
     * createdAt : 2016-03-04T09:56:10.964Z
     * desc : 3.4
     * publishedAt : 2016-03-04T12:44:51.926Z
     * source : chrome
     * type : 福利
     * url : http://ww4.sinaimg.cn/large/7a8aed7bjw1f1klhuc8w5j20d30h9gn8.jpg
     * used : true
     * who : 张涵宇
     */
    @PrimaryKey
    protected String _id;
    @Column
    protected String _ns;
    @Column
    protected String createdAt;
    @Column
    protected String desc;
    @Column
    protected String publishedAt;
    @Column
    protected String source;
    @Column
    protected String type;
    @Column
    protected String url;
    @Column
    protected boolean used;
    @Column
    protected String who;
    /**
     * 为了瀑布流,记录图片宽高
     */
    @Column
    protected int width;
    @Column
    protected int height;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_ns() {
        return _ns;
    }

    public void set_ns(String _ns) {
        this._ns = _ns;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
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
            "_id='" + _id + '\'' +
            ", _ns='" + _ns + '\'' +
            ", createdAt='" + createdAt + '\'' +
            ", desc='" + desc + '\'' +
            ", publishedAt='" + publishedAt + '\'' +
            ", source='" + source + '\'' +
            ", type='" + type + '\'' +
            ", url='" + url + '\'' +
            ", used=" + used +
            ", who='" + who + '\'' +
            ", width=" + width +
            ", height=" + height +
            '}';
    }
}

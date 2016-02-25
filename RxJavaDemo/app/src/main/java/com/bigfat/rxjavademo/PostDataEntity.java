package com.bigfat.rxjavademo;

import java.util.List;

/**
 * Created by david on 15/12/1.
 */
public class PostDataEntity {

    /**
     * posts : []
     * more : 1
     * sincepostid : 948200
     */

    private String more;
    private String sincepostid;
    private List<PostData> posts;

    public void setMore(String more) {
        this.more = more;
    }

    public void setSincepostid(String sincepostid) {
        this.sincepostid = sincepostid;
    }

    public void setPosts(List<PostData> posts) {
        this.posts = posts;
    }

    public String getMore() {
        return more;
    }

    public String getSincepostid() {
        return sincepostid;
    }

    public List<PostData> getPosts() {
        return posts;
    }
}

package com.bigfat.rxjavademo;

import java.util.List;

/**
 * Created by david on 15/12/1.
 */
public class PostData {

    /**
     * autoid : 948613
     * p_id : fc3a7f9b-db4f-41b7-8c98-eb70ed602240
     * id : 948613
     * guid : fc3a7f9b-db4f-41b7-8c98-eb70ed602240
     * text : $$$195934f7-5789-4ed8-b80d-2593246edeb1|bug$$$ 任务打印出现乱码。
     * text_attribute : {}
     * tags : [{"id":"f90e8a8c-5649-45a8-9c54-1d47793607b0","name":"已建任务跟进"}]
     * create_time : 2015-11-30 18:14:33
     * source : 明道iPhone客户端
     * reply_count : 2
     * like_count : 0
     * reshared_count : 0
     * favorite_count : 0
     * favorite : 0
     * like : 0
     * type : 2
     * share_type : 1
     * details : [{"id":"bd5e417d-b1d8-4560-887d-df1d08208b03","file_type":1,"original_filename":"2015-11-30-18-14-24-321.jpg","filesize":182446,"thumbnail_pic":"https://dn-mdpic.qbox.me/ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d/pic/201511/30/2015-11-30-18-14-24-321.jpg?imageView2/2/w/130/h/90/format/jpg","middle_pic":"https://dn-mdpic.qbox.me/ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d/pic/201511/30/2015-11-30-18-14-24-321.jpg?imageView2/2/w/470/h/312/format/jpg","original_pic":"https://dn-mdpic.qbox.me/ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d/pic/201511/30/2015-11-30-18-14-24-321.jpg"}]
     * groups : [{"id":"2c46009c-4d0c-45d5-a273-3fa6c67ad048","name":"明道研发部","avstar":"https://dn-mdpic.qbox.me/GroupAvatar/default16.png?imageView2/1/w/24/h/24/q/100","avatar":"https://dn-mdpic.qbox.me/GroupAvatar/default16.png?imageView2/1/w/24/h/24/q/100"}]
     * user : {"id":"ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d","name":"许维","avstar":"https://dn-mdpic.qbox.me/UserAvatar/ca0fdf92-0346-437a-abc9-4314a93f658a.jpg?imageView2/1/w/48/h/48/q/90","avatar":"https://dn-mdpic.qbox.me/UserAvatar/ca0fdf92-0346-437a-abc9-4314a93f658a.jpg?imageView2/1/w/48/h/48/q/90"}
     */

    private String autoid;
    private String p_id;
    private String id;
    private String guid;
    private String text;
    private String create_time;
    private String source;
    private String reply_count;
    private String like_count;
    private String reshared_count;
    private String favorite_count;
    private String favorite;
    private String like;
    private String type;
    private String share_type;
    /**
     * id : ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d
     * name : 许维
     * avstar : https://dn-mdpic.qbox.me/UserAvatar/ca0fdf92-0346-437a-abc9-4314a93f658a.jpg?imageView2/1/w/48/h/48/q/90
     * avatar : https://dn-mdpic.qbox.me/UserAvatar/ca0fdf92-0346-437a-abc9-4314a93f658a.jpg?imageView2/1/w/48/h/48/q/90
     */

    private UserEntity user;
    /**
     * id : bd5e417d-b1d8-4560-887d-df1d08208b03
     * file_type : 1
     * original_filename : 2015-11-30-18-14-24-321.jpg
     * filesize : 182446
     * thumbnail_pic : https://dn-mdpic.qbox.me/ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d/pic/201511/30/2015-11-30-18-14-24-321.jpg?imageView2/2/w/130/h/90/format/jpg
     * middle_pic : https://dn-mdpic.qbox.me/ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d/pic/201511/30/2015-11-30-18-14-24-321.jpg?imageView2/2/w/470/h/312/format/jpg
     * original_pic : https://dn-mdpic.qbox.me/ffbe06cf-4d43-4eeb-a39d-c6c984d70d3d/pic/201511/30/2015-11-30-18-14-24-321.jpg
     */

    private List<DetailsEntity> details;

    public void setAutoid(String autoid) {
        this.autoid = autoid;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public void setReshared_count(String reshared_count) {
        this.reshared_count = reshared_count;
    }

    public void setFavorite_count(String favorite_count) {
        this.favorite_count = favorite_count;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setShare_type(String share_type) {
        this.share_type = share_type;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setDetails(List<DetailsEntity> details) {
        this.details = details;
    }

    public String getAutoid() {
        return autoid;
    }

    public String getP_id() {
        return p_id;
    }

    public String getId() {
        return id;
    }

    public String getGuid() {
        return guid;
    }

    public String getText() {
        return text;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getSource() {
        return source;
    }

    public String getReply_count() {
        return reply_count;
    }

    public String getLike_count() {
        return like_count;
    }

    public String getReshared_count() {
        return reshared_count;
    }

    public String getFavorite_count() {
        return favorite_count;
    }

    public String getFavorite() {
        return favorite;
    }

    public String getLike() {
        return like;
    }

    public String getType() {
        return type;
    }

    public String getShare_type() {
        return share_type;
    }

    public UserEntity getUser() {
        return user;
    }

    public List<DetailsEntity> getDetails() {
        return details;
    }

    public static class UserEntity {
        private String id;
        private String name;
        private String avstar;
        private String avatar;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAvstar(String avstar) {
            this.avstar = avstar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAvstar() {
            return avstar;
        }

        public String getAvatar() {
            return avatar;
        }
    }

    public static class DetailsEntity {
        private String id;
        private int file_type;
        private String original_filename;
        private int filesize;
        private String thumbnail_pic;
        private String middle_pic;
        private String original_pic;

        public void setId(String id) {
            this.id = id;
        }

        public void setFile_type(int file_type) {
            this.file_type = file_type;
        }

        public void setOriginal_filename(String original_filename) {
            this.original_filename = original_filename;
        }

        public void setFilesize(int filesize) {
            this.filesize = filesize;
        }

        public void setThumbnail_pic(String thumbnail_pic) {
            this.thumbnail_pic = thumbnail_pic;
        }

        public void setMiddle_pic(String middle_pic) {
            this.middle_pic = middle_pic;
        }

        public void setOriginal_pic(String original_pic) {
            this.original_pic = original_pic;
        }

        public String getId() {
            return id;
        }

        public int getFile_type() {
            return file_type;
        }

        public String getOriginal_filename() {
            return original_filename;
        }

        public int getFilesize() {
            return filesize;
        }

        public String getThumbnail_pic() {
            return thumbnail_pic;
        }

        public String getMiddle_pic() {
            return middle_pic;
        }

        public String getOriginal_pic() {
            return original_pic;
        }
    }
}

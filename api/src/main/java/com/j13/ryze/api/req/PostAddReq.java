package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostAddReq {
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "")
    private int anonymous;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private String imgList;
    @Parameter(desc = "")
    private String topicIdList;

    public String getTopicIdList() {
        return topicIdList;
    }

    public void setTopicIdList(String topicIdList) {
        this.topicIdList = topicIdList;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class NoticeDetailResp {
    @Parameter(desc = "")
    private int noticeId;
    @Parameter(desc = "")
    private int fromUserId;
    @Parameter(desc = "")
    private String fromUserNickName;
    @Parameter(desc = "")
    private String fromUserAvatarImgUrl;
    @Parameter(desc = "")
    private int toUserId;
    @Parameter(desc = "")
    private Object content;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private int status;

    public String getFromUserAvatarImgUrl() {
        return fromUserAvatarImgUrl;
    }

    public void setFromUserAvatarImgUrl(String fromUserAvatarImgUrl) {
        this.fromUserAvatarImgUrl = fromUserAvatarImgUrl;
    }

    public String getFromUserNickName() {
        return fromUserNickName;
    }

    public void setFromUserNickName(String fromUserNickName) {
        this.fromUserNickName = fromUserNickName;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

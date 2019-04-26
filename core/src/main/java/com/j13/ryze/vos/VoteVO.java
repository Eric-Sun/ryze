package com.j13.ryze.vos;

import com.j13.ryze.destiny.VoteEvidence;

public class VoteVO {
    private int id;
    private int userId;
    private String userName;
    private String userAvatarUrl;
    private int resourceId;
    private int type;
    private String evidence;
    private int agreeCount;
    private int disagreeCount;
    private int result;
    private long createtime;
    private int status;
    private VoteEvidence evidenceObject;
    private long triggertime;

    public long getTriggertime() {
        return triggertime;
    }

    public void setTriggertime(long triggertime) {
        this.triggertime = triggertime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public VoteEvidence getEvidenceObject() {
        return evidenceObject;
    }

    public void setEvidenceObject(VoteEvidence evidenceObject) {
        this.evidenceObject = evidenceObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public int getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(int agreeCount) {
        this.agreeCount = agreeCount;
    }

    public int getDisagreeCount() {
        return disagreeCount;
    }

    public void setDisagreeCount(int disagreeCount) {
        this.disagreeCount = disagreeCount;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}

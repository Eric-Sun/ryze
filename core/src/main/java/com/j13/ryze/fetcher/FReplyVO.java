package com.j13.ryze.fetcher;

public class FReplyVO {

    private int id;
    private int fPostId;
    private int lastFReplyId;
    private int sourceReplyId;
    private String content;
    private int isAuhtor;

    public int getIsAuhtor() {
        return isAuhtor;
    }

    public void setIsAuhtor(int isAuhtor) {
        this.isAuhtor = isAuhtor;
    }

    public int getLastFReplyId() {
        return lastFReplyId;
    }

    public void setLastFReplyId(int lastFReplyId) {
        this.lastFReplyId = lastFReplyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getfPostId() {
        return fPostId;
    }

    public void setfPostId(int fPostId) {
        this.fPostId = fPostId;
    }


    public int getSourceReplyId() {
        return sourceReplyId;
    }

    public void setSourceReplyId(int sourceReplyId) {
        this.sourceReplyId = sourceReplyId;
    }

    public String getContent() {
        return content.replaceAll("\t", "").replace("  ","");
    }

    public void setContent(String content) {
        this.content = content;
    }
}

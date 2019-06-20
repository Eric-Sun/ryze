package com.j13.ryze.vos;

import java.util.Date;

public class CollectionVO {
    private int id;
    private int userId;
    private int type;
    private int resourceId;
    private long createtime;
    private Object resourceObject;

    public Object getResourceObject() {
        return resourceObject;
    }

    public void setResourceObject(Object resourceObject) {
        this.resourceObject = resourceObject;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}

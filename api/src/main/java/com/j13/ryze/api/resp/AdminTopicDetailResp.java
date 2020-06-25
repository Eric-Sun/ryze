package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminTopicDetailResp {
    @Parameter(desc = "")
    private int id;
    @Parameter(desc = "")
    private String name;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int isDefault;

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}

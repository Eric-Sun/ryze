package com.j13.ryze.vos;

import com.j13.poppy.anno.Parameter;

public class BarVO {
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private String name;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

/**
 * 用于在顶部显示层级关系
 */
public class AdminLevelInfoResp {
    @Parameter(desc = "0:bar,1:post,2:reply")
    private int type;
    @Parameter(desc = "对应不同类型的id")
    private int id;
    @Parameter(desc = "只用于reply的level")
    private int level;
    @Parameter(desc = "显示在上面的内容，bar和post为title，reply为content")
    private String brief;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class ImgGetResp {

    @Parameter(desc = "")
    private int id;
    @Parameter(desc = "")
    private String name;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

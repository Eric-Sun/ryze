package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class PainterAddResp {
    @Parameter(desc="账号id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

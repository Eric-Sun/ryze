package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class UserModifyNameAndAvatarReq {
    @Parameter(desc = "")
    private String newName;
    @Parameter(desc = "")
    private int newImgId;

    public int getNewImgId() {
        return newImgId;
    }

    public void setNewImgId(int newImgId) {
        this.newImgId = newImgId;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}

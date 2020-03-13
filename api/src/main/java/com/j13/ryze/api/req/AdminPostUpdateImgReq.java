package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminPostUpdateImgReq {

    @Parameter(desc = "")
    private int postId;
    @Parameter
    private String imgIdListStr;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getImgIdListStr() {
        return imgIdListStr;
    }

    public void setImgIdListStr(String imgIdListStr) {
        this.imgIdListStr = imgIdListStr;
    }
}

package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;
import org.apache.commons.fileupload.FileItem;

public class OrderUploadImgReq {
    @Parameter(desc="")
    private FileItem img;

    public FileItem getImg() {
        return img;
    }

    public void setImg(FileItem img) {
        this.img = img;
    }
}

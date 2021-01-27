package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;
import org.apache.commons.fileupload.FileItem;

public class AdminImgUploadReq {

    @Parameter(desc = "")
    private FileItem file;
    @Parameter(desc = "")
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FileItem getFile() {
        return file;
    }

    public void setFile(FileItem file) {
        this.file = file;
    }
}

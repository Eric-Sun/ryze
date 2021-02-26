package com.j13.ryze.services;

import com.j13.ryze.daos.VideoDAO;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.VideoVO;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    @Autowired
    OSSClientService ossClientService;

    @Autowired
    VideoDAO videoDAO;

    /**
     * 通过文件上传的流保存到oss上
     * @param item
     * @param type
     * @return
     */
    public ImgVO save(FileItem item, int type) {
        String fileName = ossClientService.saveFile(item, type);
        ImgVO img = new ImgVO();
        int imgId = videoDAO.add(fileName, type);
        img.setId(imgId);
        img.setName(fileName);
        img.setType(type);
        String url = ossClientService.getFileUrl(img.getName(), img.getType());
        img.setUrl(url);
        return img;
    }


    /**
     * 通过videoId获得对应的播放地址
     *
     * @param videoId
     * @return
     */
    public String getUrl(int videoId) {
        VideoVO vo = videoDAO.get(videoId);
        String url = ossClientService.getFileUrl(vo.getName(), vo.getType());
        return url;
    }

}

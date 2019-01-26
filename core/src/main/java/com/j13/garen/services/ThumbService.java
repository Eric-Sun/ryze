package com.j13.garen.services;

import com.j13.garen.core.Constants;
import com.j13.garen.daos.ImgDAO;
import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.poppy.exceptions.ServerException;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

@Service
public class ThumbService {

    private static Logger LOG = LoggerFactory.getLogger(ThumbService.class);

    Random ran = new Random();
    @Autowired
    ImgDAO imgDAO;

    public String uploadThumb(FileItem item) {

        int i = ran.nextInt(1000);
        String fileName = System.currentTimeMillis() + i + ".jpg";
        String localFile = getThumbSaveFile(fileName);
        try {
            InputStream in = item.getInputStream();
            FileOutputStream out = new FileOutputStream(localFile);
            byte buffer[] = new byte[1024];
            //判断输入流中的数据是否已经读完的标识
            int len = 0;
            //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
            while ((len = in.read(buffer)) > 0) {
                //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                out.write(buffer, 0, len);
            }
            //关闭输入流
            in.close();
            //关闭输出流
            out.close();
            //删除处理文件上传时生成的临时文件
            item.delete();
        } catch (Exception e) {
            throw new ServerException("upload file .", e);
        }

        return fileName;

    }


    private String getThumbSaveFile(String fileName) {
        return PropertiesConfiguration.getInstance().getStringValue("local.img.dir") + File.separator + fileName;
    }


    public int insertOrderImg(String fileName){
        return imgDAO.insert(fileName, Constants.IMG_TYPE.ORDER);
    }


}

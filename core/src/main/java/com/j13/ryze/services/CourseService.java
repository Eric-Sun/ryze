package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.j13.ryze.daos.CourseDAO;
import com.j13.ryze.vos.course.CourseInfo;
import com.j13.ryze.vos.course.CourseVideoStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseDAO courseDAO;
    @Autowired
    VideoService videoService;

    /**
     * 获得课程信息
     */
    public CourseInfo getCourse(int id) {
        CourseInfo ci = courseDAO.get(id);
        List<String> videoIdList = JSON.parseArray(ci.getData(),String.class);
        for(String videoIdStr : videoIdList){
            int videoId = new Integer(videoIdStr);
            String videoUrl = videoService.getUrl(videoId);
            CourseVideoStep s = new CourseVideoStep();
            s.setUrl(videoUrl);
            ci.getStepList().add(s);
        }
        return ci;
    }


}

package com.j13.ryze.facades;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.ryze.api.req.CourseDetailReq;
import com.j13.ryze.api.req.CourseListReq;
import com.j13.ryze.api.resp.CourseDetailResp;
import com.j13.ryze.services.CourseService;
import com.j13.ryze.vos.course.CourseInfo;
import com.j13.ryze.vos.course.CourseStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseFacade {

    @Autowired
    CourseService courseService;


    @Action(name = "course.get")
    public CourseDetailResp get(CommandContext ctxt, CourseDetailReq req) {
        CourseDetailResp resp = new CourseDetailResp();
        CourseInfo ci = courseService.getCourse(req.getId());
        resp.setId(ci.getId());
        resp.setName(ci.getName());
        resp.setType(ci.getType());
        resp.setTips1(ci.getTips1());
        resp.setTips2(ci.getTips2());
        resp.setPrice(ci.getPrice());
        resp.setDiscountedPrice(ci.getDiscountedPrice());
        for(CourseStep cs : ci.getStepList()){
            resp.getData().add(cs);
        }
        return resp;
    }

}

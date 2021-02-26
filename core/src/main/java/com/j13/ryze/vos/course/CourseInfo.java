package com.j13.ryze.vos.course;

import com.google.common.collect.Lists;

import java.util.List;

public class CourseInfo {
    private int id;
    private String name;
    private int type;
    private int count;
    private String data;
    private int status;
    private List<CourseStep> stepList = Lists.newLinkedList();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public List<CourseStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<CourseStep> stepList) {
        this.stepList = stepList;
    }
}

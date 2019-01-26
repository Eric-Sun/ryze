package com.j13.garen.vos;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 14-10-9
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class AuthorityVO {
    private int id;
    private String name;
    private List<Integer> resourceIdList;

    public List<Integer> getResourceIdList() {
        return resourceIdList;
    }

    public void setResourceIdList(List<Integer> resourceIdList) {
        this.resourceIdList = resourceIdList;
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
}

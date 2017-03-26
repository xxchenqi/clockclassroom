package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/11/16 11:29
 * ----------------------------------------
 */
public class TagBean implements Serializable{
    private String id;
    private String name;
    private String type;
    private String sort;
    private String create_time;
    private String org_id;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSort() {
        return sort;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getOrg_id() {
        return org_id;
    }
}

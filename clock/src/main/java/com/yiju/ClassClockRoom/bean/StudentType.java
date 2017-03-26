package com.yiju.ClassClockRoom.bean;

/**
 * StudentType
 * Created by wh on 2016/5/13.
 */
public class StudentType {

    /**
     * id : 1
     * type_name : 幼儿
     * type_desc : 0-5岁
     * sort : 1
     * create_time : 0
     * is_default : 0
     */

    private String id;
    private String type_name;
    private String type_desc;
    private String sort;
    private String create_time;
    private String is_default;

    public void setId(String id) {
        this.id = id;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getId() {
        return id;
    }

    public String getType_name() {
        return type_name;
    }

    public String getType_desc() {
        return type_desc;
    }

    public String getSort() {
        return sort;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getIs_default() {
        return is_default;
    }
}

package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/11/16 11:28
 * ----------------------------------------
 */
public class MienBean implements Serializable {
    private String id;
    private String pic;
    private String type;
    private String create_time;
    private String relation_id;
    private String is_delete;

    public void setId(String id) {
        this.id = id;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getId() {
        return id;
    }

    public String getPic() {
        return pic;
    }

    public String getType() {
        return type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public String getIs_delete() {
        return is_delete;
    }
}

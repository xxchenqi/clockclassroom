package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/11/16 11:27
 * ----------------------------------------
 */
public class MemberDetailData implements Serializable {
    private String id = "";
    private String real_name = "";
    private String first_name = "";
    private String last_name = "";
    private String show_name = "";
    private String identity_no = "";
    private String identity_pic = "";
    private String identity_picback = "";
    private String avatar = "";
    private String sex = "";
    private String info = "";
    private String create_time = "";
    private String is_auth = "";
    private String auth_remark = "";
    private String allow_edit = "";
    private String tags = "";
    private String org_id = "";
    private List<MienBean> mien = new ArrayList<>();


    public void setId(String id) {
        this.id = id;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public void setIdentity_no(String identity_no) {
        this.identity_no = identity_no;
    }

    public void setIdentity_pic(String identity_pic) {
        this.identity_pic = identity_pic;
    }

    public void setIdentity_picback(String identity_picback) {
        this.identity_picback = identity_picback;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setIs_auth(String is_auth) {
        this.is_auth = is_auth;
    }

    public void setAuth_remark(String auth_remark) {
        this.auth_remark = auth_remark;
    }

    public void setAllow_edit(String allow_edit) {
        this.allow_edit = allow_edit;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public void setMien(List<MienBean> mien) {
        this.mien = mien;
    }

    public String getId() {
        return id;
    }

    public String getReal_name() {
        return real_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getShow_name() {
        return show_name;
    }

    public String getIdentity_no() {
        return identity_no;
    }

    public String getIdentity_pic() {
        return identity_pic;
    }

    public String getIdentity_picback() {
        return identity_picback;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getSex() {
        return sex;
    }

    public String getInfo() {
        return info;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getIs_auth() {
        return is_auth;
    }

    public String getAuth_remark() {
        return auth_remark;
    }

    public String getAllow_edit() {
        return allow_edit;
    }

    public String getTags() {
        return tags;
    }

    public String getOrg_id() {
        return org_id;
    }

    public List<MienBean> getMien() {
        return mien;
    }
}

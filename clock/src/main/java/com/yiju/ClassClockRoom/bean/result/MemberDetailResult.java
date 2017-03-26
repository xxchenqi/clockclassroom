package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.MemberDetailData;
import com.yiju.ClassClockRoom.bean.TagBean;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/11/16 11:23
 * ----------------------------------------
 */
public class MemberDetailResult extends BaseEntity implements Serializable {
    private MemberDetailData data;
    private String show_teacher;
    private String avatar;
    private String org_dname;
    private String org_auth;
    private String is_pay;//标记是否完整下过订单
    private List<TagBean> tags;
    private int fullteacherinfo;//资料是否齐全
    private String mobile;

    public MemberDetailData getData() {
        if (data != null) {
            return data;
        } else {
            data = new MemberDetailData();
            return data;
        }
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getOrg_auth() {
        return org_auth;
    }

    public void setOrg_auth(String org_auth) {
        this.org_auth = org_auth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShow_teacher() {
        return show_teacher;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getOrg_dname() {
        return org_dname;
    }

    public List<TagBean> getTags() {
        return tags;
    }

    public void setData(MemberDetailData data) {
        this.data = data;
    }

    public void setShow_teacher(String show_teacher) {
        this.show_teacher = show_teacher;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setOrg_dname(String org_dname) {
        this.org_dname = org_dname;
    }

    public void setTags(List<TagBean> tags) {
        this.tags = tags;
    }

    public int getFullteacherinfo() {
        return fullteacherinfo;
    }

    public void setFullteacherinfo(int fullteacherinfo) {
        this.fullteacherinfo = fullteacherinfo;
    }
}

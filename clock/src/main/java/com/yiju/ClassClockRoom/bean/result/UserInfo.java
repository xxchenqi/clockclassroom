package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;

public class UserInfo extends BaseEntity{
    private Data data;
    private Alert_order alert_order;
    private String org_black_count;

    public String getOrg_black_count() {
        return org_black_count;
    }

    public Data getData() {
        return data;
    }

    public Alert_order getAlert_order() {
        return alert_order;
    }

    public class Data {
        private String id = "";
        private String name = "";
        private String password = "";
        private String sex = "";
        private String age = "";
        private String mobile = "";
        private String email = "";
        private String nickname = "";
        private String avatar = "";
        private String praise = "";
        private String credit = "";
        private String create_time = "";
        private String login_time = "";
        private String is_teacher = "";
        private String third_source = "";
        private String third_id = "";
        private String remerber = "";
        private String is_remerber = "";// 陪读提醒
        private String is_sys_remerber = "";// 系统提醒
        private String is_order_remerber = "";// 订单提醒
        private String is_reserve_remerber = "";
        private String is_pay_remerber = "";
        private String trouble_btime = "";
        private String trouble_etime = "";
        private String badge = "";
        private String cnum = "";
        private String third_qq = "";
        private String third_wechat = "";
        private String third_weibo = "";
        private String real_name;
        private String show_teacher;
        private String org_id;
        private String org_name;
        private String is_auth;
        private String org_auth;                //身份信息  1-普通成员 2-管理员
        private String teacher_id;
        private String teacher_info;
        private String is_pay;
        private String fullteacherinfo;// 资料是否齐全  1：齐全  0：不全

        public String getIs_pay() {
            return is_pay;
        }

        public String getTeacher_info() {
            return teacher_info;
        }

        public void setThird_qq(String third_qq) {
            this.third_qq = third_qq;
        }

        public void setThird_wechat(String third_wechat) {
            this.third_wechat = third_wechat;
        }

        public void setThird_weibo(String third_weibo) {
            this.third_weibo = third_weibo;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getIs_sys_remerber() {
            return is_sys_remerber;
        }

        public void setIs_sys_remerber(String is_sys_remerber) {
            this.is_sys_remerber = is_sys_remerber;
        }

        public String getIs_order_remerber() {
            return is_order_remerber;
        }

        public void setIs_order_remerber(String is_order_remerber) {
            this.is_order_remerber = is_order_remerber;
        }

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getOrg_auth() {
            return org_auth;
        }

        public void setOrg_auth(String org_auth) {
            this.org_auth = org_auth;
        }

        public String getShow_teacher() {
            return show_teacher;
        }

        public void setShow_teacher(String show_teacher) {
            this.show_teacher = show_teacher;
        }

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public String getOrg_name() {
            return org_name;
        }

        public void setOrg_name(String org_name) {
            this.org_name = org_name;
        }

        public String getIs_auth() {
            return is_auth;
        }

        public void setIs_auth(String is_auth) {
            this.is_auth = is_auth;
        }

        public String getThird_qq() {
            return third_qq;
        }

        public String getThird_wechat() {
            return third_wechat;
        }

        public String getThird_weibo() {
            return third_weibo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getIs_teacher() {
            return is_teacher;
        }

        public void setIs_teacher(String is_teacher) {
            this.is_teacher = is_teacher;
        }

        public String getThird_source() {
            return third_source;
        }

        public void setThird_source(String third_source) {
            this.third_source = third_source;
        }

        public String getThird_id() {
            return third_id;
        }

        public void setThird_id(String third_id) {
            this.third_id = third_id;
        }

        public String getRemerber() {
            return remerber;
        }

        public void setRemerber(String remerber) {
            this.remerber = remerber;
        }

        public String getIs_remerber() {
            return is_remerber;
        }

        public void setIs_remerber(String is_remerber) {
            this.is_remerber = is_remerber;
        }

        public String getIs_reserve_remerber() {
            return is_reserve_remerber;
        }

        public void setIs_reserve_remerber(String is_reserve_remerber) {
            this.is_reserve_remerber = is_reserve_remerber;
        }

        public String getIs_pay_remerber() {
            return is_pay_remerber;
        }

        public void setIs_pay_remerber(String is_pay_remerber) {
            this.is_pay_remerber = is_pay_remerber;
        }

        public String getTrouble_btime() {
            return trouble_btime;
        }

        public void setTrouble_btime(String trouble_btime) {
            this.trouble_btime = trouble_btime;
        }

        public String getTrouble_etime() {
            return trouble_etime;
        }

        public void setTrouble_etime(String trouble_etime) {
            this.trouble_etime = trouble_etime;
        }

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        public String getCnum() {
            return cnum;
        }

        public void setCnum(String cnum) {
            this.cnum = cnum;
        }

        public String getFullteacherinfo() {
            return fullteacherinfo;
        }

        public void setFullteacherinfo(String fullteacherinfo) {
            this.fullteacherinfo = fullteacherinfo;
        }
    }

    public class Alert_order {
        private String count;
        private String expire_time;
        private String nowtime;

        public String getCount() {
            return count;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public String getNowtime() {
            return nowtime;
        }

    }

}
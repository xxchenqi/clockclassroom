package com.yiju.ClassClockRoom.bean;

import java.util.List;

public class ClassRoomData {

    private List<CourseInfo> course_recommend;//推荐课程
    private List<TeacherInfoBean> teacher_recommend;//推荐老师
    private ClassRoom school_list;
    private CartStatus cart_status;
    private IndexAds index_ads;
//    private MessageBoxNoRead message_box_noread;

//    public MessageBoxNoRead getMessage_box_noread() {
//        return message_box_noread;
//    }
//
//    public void setMessage_box_noread(MessageBoxNoRead message_box_noread) {
//        this.message_box_noread = message_box_noread;
//    }

    public ClassRoom getSchool_list() {
        return school_list;
    }

    public void setSchool_list(ClassRoom school_list) {
        this.school_list = school_list;
    }

    public CartStatus getCart_status() {
        return cart_status;
    }

    public void setCart_status(CartStatus cart_status) {
        this.cart_status = cart_status;
    }

    public IndexAds getIndex_ads() {
        return index_ads;
    }

    public void setIndex_ads(IndexAds index_ads) {
        this.index_ads = index_ads;
    }

    public List<CourseInfo> getCourse_recommend() {
        return course_recommend;
    }

    public void setCourse_recommend(List<CourseInfo> course_recommend) {
        this.course_recommend = course_recommend;
    }

    public List<TeacherInfoBean> getTeacher_recommend() {
        return teacher_recommend;
    }

    public void setTeacher_recommend(List<TeacherInfoBean> teacher_recommend) {
        this.teacher_recommend = teacher_recommend;
    }
}

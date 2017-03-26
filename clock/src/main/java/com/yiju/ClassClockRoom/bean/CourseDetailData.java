package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/6/17/0017.
 */
public class CourseDetailData {

    private String id;
    private String name;
    private String single_price;
    private String remain_count;
    private String total_count;
    private String desc;
    private String is_interest;     //是否关注 0-未关注    1-已关注
    /**
     * time_list : [{"status":3,"time":"2016.06.16 9:00-10:00"},{"status":3,"time":"2016.06.16 12:00-13:00"}]
     * all_course_hour : 2
     * active_course_hour : 0
     * date_section : 2016.06.16~06.16
     */

    private CourseSchedule schedule;
    private int is_enroll;
    /**
     * id : 30
     * real_name : aaaa
     * org_name : 陈奇奇
     * show_teacher : 1
     * tags : 语文,能力,全科,太可怜了,门口了,all 里
     * course_info : test2
     * pics : ["http://get.file.dc.cric.com/SZJS00425f290b95426709858a63af3eaa69_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5e886a31f39dc8542fdf51e380e669e3_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5266a8de4047ccbfdb6864553cd0ddc8_320X240_0_0_0.jpg"]
     */

    private CourseTeacher teacher;
    /**
     * id : 8
     * name : 测试徐汇虹梅商务大厦旗舰店
     * address : 徐汇区沪闵路8075号8楼
     * lng : 121.42033397575
     * lat : 31.14827526199
     * lng_g : 121.41399311220
     * lat_g : 31.14218370720
     */

    private CourseSchool school;
    private List<String> pics;

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

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public String getRemain_count() {
        return remain_count;
    }

    public void setRemain_count(String remain_count) {
        this.remain_count = remain_count;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CourseSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(CourseSchedule schedule) {
        this.schedule = schedule;
    }

    public int getIs_enroll() {
        return is_enroll;
    }

    public void setIs_enroll(int is_enroll) {
        this.is_enroll = is_enroll;
    }

    public CourseTeacher getTeacher() {
        return teacher;
    }

    public void setTeacher(CourseTeacher teacher) {
        this.teacher = teacher;
    }

    public CourseSchool getSchool() {
        return school;
    }

    public void setSchool(CourseSchool school) {
        this.school = school;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getIs_interest() {
        return is_interest;
    }

    public void setIs_interest(String is_interest) {
        this.is_interest = is_interest;
    }
}

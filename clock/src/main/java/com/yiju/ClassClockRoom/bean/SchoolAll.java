package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * Created by Sandy on 2016/9/5/0005.
 */
public class SchoolAll {

    /**
     * code : 1
     * msg : ok
     * data : [{"id":"-1","dist_name":"全部区域","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"720","dist_name":"黄浦区","school_list":[{"sid":"-1","school_name":"全部门店"},{"sid":"46","school_name":"人广测试店0818"}]},{"id":"721","dist_name":"卢湾区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"722","dist_name":"徐汇区","school_list":[{"sid":"-1","school_name":"全部门店"},{"sid":"8","school_name":"徐汇虹梅中心"}]},{"id":"723","dist_name":"长宁区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"724","dist_name":"静安区","school_list":[{"sid":"-1","school_name":"全部门店"},{"sid":"36","school_name":"时钟教室静安临汾中心测试"}]},{"id":"725","dist_name":"普陀区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"726","dist_name":"闸北区","school_list":[{"sid":"-1","school_name":"全部门店"},{"sid":"11","school_name":"闸北达人湾中心"},{"sid":"18","school_name":"技术中心测试门店"},{"sid":"47","school_name":"Miratest"}]},{"id":"727","dist_name":"虹口区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"728","dist_name":"杨浦区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"729","dist_name":"闵行区","school_list":[{"sid":"-1","school_name":"全部门店"},{"sid":"34","school_name":"美华少儿英语莘庄中心"}]},{"id":"730","dist_name":"宝山区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"731","dist_name":"嘉定区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"732","dist_name":"浦东新区","school_list":[{"sid":"-1","school_name":"全部门店"},{"sid":"16","school_name":"浦东龙阳中心"}]},{"id":"733","dist_name":"金山区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"734","dist_name":"松江区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"735","dist_name":"青浦区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"736","dist_name":"南汇区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"737","dist_name":"奉贤区","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"738","dist_name":"崇明县","school_list":[{"sid":"-1","school_name":"全部门店"}]},{"id":"3110","dist_name":"其他","school_list":[{"sid":"-1","school_name":"全部门店"}]}]
     */

    private String code;
    private String msg;
    /**
     * id : -1
     * dist_name : 全部区域
     * school_list : [{"sid":"-1","school_name":"全部门店"}]
     */

    private List<SchoolLeft> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SchoolLeft> getData() {
        return data;
    }

    public void setData(List<SchoolLeft> data) {
        this.data = data;
    }

}

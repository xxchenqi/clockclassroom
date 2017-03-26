package com.yiju.ClassClockRoom.bean.result;

import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.CourseDetailData;

/**
 * Created by Sandy on 2016/6/17/0017.
 */
public class CourseDetail extends BaseEntity{
    /**
     * id : 14
     * name : 语文1
     * single_price : 50.00
     * remain_count : 7
     * total_count : 11
     * desc : 我的语文课
     * pics : ["http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg","http://s.cimg.163.com/catchpic/A/A2/A286F279104F4A50B20DEE4FFA6A04A1.jpg.234x166.jpg","aa","bb","cc"]
     * schedule : {"time_list":[{"status":3,"time":"2016.06.16 9:00-10:00"},{"status":3,"time":"2016.06.16 12:00-13:00"}],"all_course_hour":2,"active_course_hour":0,"date_section":"2016.06.16~06.16"}
     * is_enroll : 3
     * teacher : {"id":"30","real_name":"aaaa","org_name":"陈奇奇","show_teacher":"1","tags":"语文,能力,全科,太可怜了,门口了,all 里","course_info":"test2","pics":["http://get.file.dc.cric.com/SZJS00425f290b95426709858a63af3eaa69_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5e886a31f39dc8542fdf51e380e669e3_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5266a8de4047ccbfdb6864553cd0ddc8_320X240_0_0_0.jpg"]}
     * school : {"id":"8","name":"测试徐汇虹梅商务大厦旗舰店","address":"徐汇区沪闵路8075号8楼","lng":"121.42033397575","lat":"31.14827526199","lng_g":"121.41399311220","lat_g":"31.14218370720"}
     */

    private CourseDetailData data;

    public CourseDetailData getData() {
        return data;
    }

    public void setData(CourseDetailData data) {
        this.data = data;
    }


    /**
     * code : 1
     * msg : ok
     * data : {"id":"18","name":"test2","single_price":"50.00","remain_count":"0","total_count":"0","desc":"","pics":[],"schedule":{"time_list":[{"status":3,"time":"2016.06.16 12:00-13:00"}],"all_course_hour":1,"active_course_hour":0,"date_section":"2016.06.16~06.16"},"is_enroll":3,"teacher":{"id":"30","real_name":"","org_name":"陈奇奇","show_teacher":"0","tags":"语文,能力,全科,太可怜了,门口了,all 里","course_info":"test2","pics":["http://get.file.dc.cric.com/SZJS00425f290b95426709858a63af3eaa69_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5e886a31f39dc8542fdf51e380e669e3_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5266a8de4047ccbfdb6864553cd0ddc8_320X240_0_0_0.jpg"]},"school":{"id":"8","name":"测试徐汇虹梅商务大厦旗舰店","address":"徐汇区沪闵路8075号8楼","lng":"121.42033397575","lat":"31.14827526199","lng_g":"121.41399311220","lat_g":"31.14218370720"}}
     */

//    private Integer code;
//    private String msg;
//    /**
//     * id : 18
//     * name : test2
//     * single_price : 50.00
//     * remain_count : 0
//     * total_count : 0
//     * desc :
//     * pics : []
//     * schedule : {"time_list":[{"status":3,"time":"2016.06.16 12:00-13:00"}],"all_course_hour":1,"active_course_hour":0,"date_section":"2016.06.16~06.16"}
//     * is_enroll : 3
//     * teacher : {"id":"30","real_name":"","org_name":"陈奇奇","show_teacher":"0","tags":"语文,能力,全科,太可怜了,门口了,all 里","course_info":"test2","pics":["http://get.file.dc.cric.com/SZJS00425f290b95426709858a63af3eaa69_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5e886a31f39dc8542fdf51e380e669e3_320X240_0_0_0.jpg","http://get.file.dc.cric.com/SZJS5266a8de4047ccbfdb6864553cd0ddc8_320X240_0_0_0.jpg"]}
//     * school : {"id":"8","name":"测试徐汇虹梅商务大厦旗舰店","address":"徐汇区沪闵路8075号8楼","lng":"121.42033397575","lat":"31.14827526199","lng_g":"121.41399311220","lat_g":"31.14218370720"}
//     */
//
//    private CourseDetailData data;
//
//    public Integer getCode() {
//        return code;
//    }
//
//    public void setCode(Integer code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public CourseDetailData getData() {
//        return data;
//    }
//
//    public void setData(CourseDetailData data) {
//        this.data = data;
//    }


}

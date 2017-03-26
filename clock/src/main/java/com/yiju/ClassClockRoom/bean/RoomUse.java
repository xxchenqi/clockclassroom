package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 房间类型2015/12/14/0014.
 */
public class RoomUse implements Serializable {

    /**
     * code : 1
     * msg : ok
     * data : [{"use_id":"31","use_name":"全科",
     * "pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=
     * SZJS384f6873e70677f45c97005578b7375a_265X265_0_0_3.jpg&key=
     * b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003",
     * "puse_id":"26","puse_name":"文化教学",
     * "room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},
     * {"use_id":"43","use_name":"语文",
     * "pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?
     * file=SZJS252bc88408c53447beea0ec5a315c1c2_265X265_0_0_3.jpg&
     * key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"26","puse_name":"文化教学",
     * "room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"44","use_name":"数学","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS3676aa381f3b96cd418f0158dbfea31f_265X265_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"26","puse_name":"文化教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"45","use_name":"外语","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSbe0f6ce966a019719531706a757090d1_265X265_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"26","puse_name":"文化教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"46","use_name":"物理","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS74093e29a8306880de9129890dead259_265X265_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"26","puse_name":"文化教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"47","use_name":"化学","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS1e4f4355ac5dbabc4140f77a258e20fd_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"26","puse_name":"文化教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"49","use_name":"文化教学-其他","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS1d78d76b474fd666896fb40ad4f5ac20_265X265_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"26","puse_name":"文化教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"50","use_name":"乐器","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS6eb4643cfe0e07caaa2012028d6a42a8_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"27","puse_name":"文艺教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"51","use_name":"棋类","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS340065a62c2d8252ab8e499925849b52_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"27","puse_name":"文艺教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"52","use_name":"绘画","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSb2f25b513f5bc21d905f35d94e689bf6_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"27","puse_name":"文艺教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"53","use_name":"书法","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS1f151f8c6c7bff8b88396e6db018795d_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"27","puse_name":"文艺教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"54","use_name":"演讲","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSa69fd5e399deb027f53131f7550a182a_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"27","puse_name":"文艺教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"55","use_name":"表演","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS9b4187e291a59f0552bdb96dea909f78_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"27","puse_name":"文艺教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"57","use_name":"文艺教学-其他","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS1d78d76b474fd666896fb40ad4f5ac20_265X265_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"27","puse_name":"文艺教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"103"}]},{"use_id":"59","use_name":"瑜伽","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSea21205821b67d50923bda2ce37d0bb1_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"28","puse_name":"形体教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"62","use_name":"跆拳道","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSb0390297aa76cdd103d8ad8637993719_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"28","puse_name":"形体教学","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"64","use_name":"证书培训","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS1f66cbd699cf7634cc32ed8a64e45aff_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"29","puse_name":"职业培训","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"65","use_name":"企业培训","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSf88acc46ca008b6b4d18206ed5325d37_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"29","puse_name":"职业培训","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"66","use_name":"企业会议","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS96e2583b4d73b1683f2a587e0eccd7a7_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"29","puse_name":"职业培训","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"67","use_name":"技能培训","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS2f952df52e6299081466fa14d0fd6c0d_100X100_0_0_0.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"29","puse_name":"职业培训","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]},{"use_id":"68","use_name":"职业培训-其他","pic_url":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS1d78d76b474fd666896fb40ad4f5ac20_265X265_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","puse_id":"29","puse_name":"职业培训","room":[{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]}]
     */

    private Integer code;
    private String msg;
    /**
     * use_id : 31
     * use_name : 全科
     * pic_url : http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS384f6873e70677f45c97005578b7375a_265X265_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003
     * puse_id : 26
     * puse_name : 文化教学
     * room : [{"id":"87"},{"id":"88"},{"id":"89"},{"id":"93"},{"id":"102"},{"id":"103"}]
     */

    private List<RightData> data;
    private List<StudentType> student_type;
    private List<RoomDevice> device;

    public List<RoomDevice> getDevice() {
        return device;
    }

    public void setDevice(List<RoomDevice> device) {
        this.device = device;
    }

    public List<StudentType> getStudent_type() {
        return student_type;
    }

    public void setStudent_type(List<StudentType> student_type) {
        this.student_type = student_type;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<RightData> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<RightData> getData() {
        return data;
    }

    public static class RightData implements Serializable {
        private String use_id;
        private String use_name;
        private String pic_url;
        private String puse_id;
        private String puse_name;
        private boolean isCheck;
        /**
         * id : 87
         */

        private List<RoomEntity> room;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

        public void setUse_id(String use_id) {
            this.use_id = use_id;
        }

        public void setUse_name(String use_name) {
            this.use_name = use_name;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public void setPuse_id(String puse_id) {
            this.puse_id = puse_id;
        }

        public void setPuse_name(String puse_name) {
            this.puse_name = puse_name;
        }

        public void setRoom(List<RoomEntity> room) {
            this.room = room;
        }

        public String getUse_id() {
            return use_id;
        }

        public String getUse_name() {
            return use_name;
        }

        public String getPic_url() {
            return pic_url;
        }

        public String getPuse_id() {
            return puse_id;
        }

        public String getPuse_name() {
            return puse_name;
        }

        public List<RoomEntity> getRoom() {
            return room;
        }

        public static class RoomEntity implements Serializable {
            private String id;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }
        }
    }

//    public static class StudentType implements Serializable {
//        private String id;
//        private String type_name;
//        private String type_desc;
//        private String sort;
//        private String create_time;
//        private String is_default;
//
//        public String getIs_default() {
//            return is_default;
//        }
//
//        public void setIs_default(String is_default) {
//            this.is_default = is_default;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getType_name() {
//            return type_name;
//        }
//
//        public void setType_name(String type_name) {
//            this.type_name = type_name;
//        }
//
//        public String getType_desc() {
//            return type_desc;
//        }
//
//        public void setType_desc(String type_desc) {
//            this.type_desc = type_desc;
//        }
//
//        public String getSort() {
//            return sort;
//        }
//
//        public void setSort(String sort) {
//            this.sort = sort;
//        }
//
//        public String getCreate_time() {
//            return create_time;
//        }
//
//        public void setCreate_time(String create_time) {
//            this.create_time = create_time;
//        }
//    }
}

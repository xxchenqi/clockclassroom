package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:更多门店列表
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/9/5 10:56
 * ----------------------------------------
 */
public class MoreStoreBean {

    /**
     * code : 0
     * msg : 查询成功
     * obj : [{"address":"人民广场XXX","addressgeo":"","area":"黄浦区","available_table":"","contract_code":"","create_time":0,"detail":"","dist_id":0,"end_time":0,"end_time_afternoon":1800,"end_time_day":0,"end_time_evening":2100,"end_time_morning":0,"id":46,"ip":"","is_available":0,"lat":0,"lat_g":31.198819,"lng":0,"lng_g":121.454819,"name":"人广测试店0818","phone":"","pic_big":"http://get.file.dc.cric.com/SZJSa1ec2fc60b36a2ea6aa2a9bbcfe151d8_639X360_0_0_3.jpg","pic_small":"http://get.file.dc.cric.com/SZJSa1ec2fc60b36a2ea6aa2a9bbcfe151d8_314X236_0_0_3.jpg","praise":0,"rate_device":0,"rate_room":0,"start_time":0,"start_time_afternoon":1200,"start_time_day":0,"start_time_evening":1800,"start_time_morning":0,"tags":"","traffic":"","type_desc":"","update_time":0,"use":"","video_ip":"","video_password":"","video_port":"","video_username":""}]
     */

    private int code;
    private String msg;
    /**
     * address : 人民广场XXX
     * addressgeo :
     * area : 黄浦区
     * available_table :
     * contract_code :
     * create_time : 0
     * detail :
     * dist_id : 0
     * end_time : 0
     * end_time_afternoon : 1800
     * end_time_day : 0
     * end_time_evening : 2100
     * end_time_morning : 0
     * id : 46
     * ip :
     * is_available : 0
     * lat : 0
     * lat_g : 31.198819
     * lng : 0
     * lng_g : 121.454819
     * name : 人广测试店0818
     * phone :
     * pic_big : http://get.file.dc.cric.com/SZJSa1ec2fc60b36a2ea6aa2a9bbcfe151d8_639X360_0_0_3.jpg
     * pic_small : http://get.file.dc.cric.com/SZJSa1ec2fc60b36a2ea6aa2a9bbcfe151d8_314X236_0_0_3.jpg
     * praise : 0
     * rate_device : 0
     * rate_room : 0
     * start_time : 0
     * start_time_afternoon : 1200
     * start_time_day : 0
     * start_time_evening : 1800
     * start_time_morning : 0
     * tags :
     * traffic :
     * type_desc :
     * update_time : 0
     * use :
     * video_ip :
     * video_password :
     * video_port :
     * video_username :
     */

    private List<MoreStoreEntity> obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MoreStoreEntity> getObj() {
        return obj;
    }

    public void setObj(List<MoreStoreEntity> obj) {
        this.obj = obj;
    }

    public class MoreStoreEntity {
        /**
         * address : 上海市静安区共和新路2395弄宝华商业街35号
         * addressgeo :
         * area : 静安区
         * available_table :
         * can_schedule : 0
         * contract_code :
         * create_time : 0
         * detail :
         * dist_id : 0
         * distances : 0.36
         * end_time : 0
         * end_time_afternoon : 1800
         * end_time_day : 0
         * end_time_evening : 2100
         * end_time_morning : 0
         * id : 299
         * ip :
         * is_available : 0
         * lat : 31.28634546508
         * lat_g : 31.280147
         * lng : 121.45991762838
         * lng_g : 121.453891
         * name : 秦汉胡同国学大宁分院
         * phone :
         * pic_big : http://get.file.dc.cric.com/SZJSc9570a49e974cac0ebc0b0c2be4f7045_639X446_0_0_1.jpg
         * pic_small : http://get.file.dc.cric.com/SZJSc9570a49e974cac0ebc0b0c2be4f7045_314X236_0_0_1.jpg
         * praise : 0
         * rate_device : 0
         * rate_room : 0
         * school_type : 2
         * start_time : 0
         * start_time_afternoon : 1200
         * start_time_day : 0
         * start_time_evening : 1800
         * start_time_morning : 0
         * tags : 国学 茶道 琴棋书画 交通便利
         * traffic :
         * type_desc :
         * update_time : 0
         * use : 国学 茶道 琴棋书画 交通便利
         * video_ip :
         * video_password :
         * video_port :
         * video_username :
         */

        private String address;
        private String addressgeo;
        private String area;
        private String available_table;
        private String can_schedule;
        private String contract_code;
        private int create_time;
        private String detail;
        private int dist_id;
        private double distances;
        private int end_time;
        private int end_time_afternoon;
        private int end_time_day;
        private int end_time_evening;
        private int end_time_morning;
        private int id;
        private String ip;
        private int is_available;
        private double lat;
        private double lat_g;
        private double lng;
        private double lng_g;
        private String name;
        private String phone;
        private String pic_big;
        private String pic_small;
        private int praise;
        private int rate_device;
        private int rate_room;
        private String school_type;
        private int start_time;
        private int start_time_afternoon;
        private int start_time_day;
        private int start_time_evening;
        private int start_time_morning;
        private String tags;
        private String traffic;
        private String type_desc;
        private int update_time;
        private String use;
        private String video_ip;
        private String video_password;
        private String video_port;
        private String video_username;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressgeo() {
            return addressgeo;
        }

        public void setAddressgeo(String addressgeo) {
            this.addressgeo = addressgeo;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAvailable_table() {
            return available_table;
        }

        public void setAvailable_table(String available_table) {
            this.available_table = available_table;
        }

        public String getCan_schedule() {
            return can_schedule;
        }

        public void setCan_schedule(String can_schedule) {
            this.can_schedule = can_schedule;
        }

        public String getContract_code() {
            return contract_code;
        }

        public void setContract_code(String contract_code) {
            this.contract_code = contract_code;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getDist_id() {
            return dist_id;
        }

        public void setDist_id(int dist_id) {
            this.dist_id = dist_id;
        }

        public double getDistances() {
            return distances;
        }

        public void setDistances(double distances) {
            this.distances = distances;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public int getEnd_time_afternoon() {
            return end_time_afternoon;
        }

        public void setEnd_time_afternoon(int end_time_afternoon) {
            this.end_time_afternoon = end_time_afternoon;
        }

        public int getEnd_time_day() {
            return end_time_day;
        }

        public void setEnd_time_day(int end_time_day) {
            this.end_time_day = end_time_day;
        }

        public int getEnd_time_evening() {
            return end_time_evening;
        }

        public void setEnd_time_evening(int end_time_evening) {
            this.end_time_evening = end_time_evening;
        }

        public int getEnd_time_morning() {
            return end_time_morning;
        }

        public void setEnd_time_morning(int end_time_morning) {
            this.end_time_morning = end_time_morning;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getIs_available() {
            return is_available;
        }

        public void setIs_available(int is_available) {
            this.is_available = is_available;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLat_g() {
            return lat_g;
        }

        public void setLat_g(double lat_g) {
            this.lat_g = lat_g;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLng_g() {
            return lng_g;
        }

        public void setLng_g(double lng_g) {
            this.lng_g = lng_g;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPic_big() {
            return pic_big;
        }

        public void setPic_big(String pic_big) {
            this.pic_big = pic_big;
        }

        public String getPic_small() {
            return pic_small;
        }

        public void setPic_small(String pic_small) {
            this.pic_small = pic_small;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public int getRate_device() {
            return rate_device;
        }

        public void setRate_device(int rate_device) {
            this.rate_device = rate_device;
        }

        public int getRate_room() {
            return rate_room;
        }

        public void setRate_room(int rate_room) {
            this.rate_room = rate_room;
        }

        public String getSchool_type() {
            return school_type;
        }

        public void setSchool_type(String school_type) {
            this.school_type = school_type;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getStart_time_afternoon() {
            return start_time_afternoon;
        }

        public void setStart_time_afternoon(int start_time_afternoon) {
            this.start_time_afternoon = start_time_afternoon;
        }

        public int getStart_time_day() {
            return start_time_day;
        }

        public void setStart_time_day(int start_time_day) {
            this.start_time_day = start_time_day;
        }

        public int getStart_time_evening() {
            return start_time_evening;
        }

        public void setStart_time_evening(int start_time_evening) {
            this.start_time_evening = start_time_evening;
        }

        public int getStart_time_morning() {
            return start_time_morning;
        }

        public void setStart_time_morning(int start_time_morning) {
            this.start_time_morning = start_time_morning;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getTraffic() {
            return traffic;
        }

        public void setTraffic(String traffic) {
            this.traffic = traffic;
        }

        public String getType_desc() {
            return type_desc;
        }

        public void setType_desc(String type_desc) {
            this.type_desc = type_desc;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }

        public String getVideo_ip() {
            return video_ip;
        }

        public void setVideo_ip(String video_ip) {
            this.video_ip = video_ip;
        }

        public String getVideo_password() {
            return video_password;
        }

        public void setVideo_password(String video_password) {
            this.video_password = video_password;
        }

        public String getVideo_port() {
            return video_port;
        }

        public void setVideo_port(String video_port) {
            this.video_port = video_port;
        }

        public String getVideo_username() {
            return video_username;
        }

        public void setVideo_username(String video_username) {
            this.video_username = video_username;
        }

    }
}

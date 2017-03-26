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
 * 时间: on 2016/5/18 10:36
 * ----------------------------------------
 */
public class Order2 implements Serializable {
    public Order2() {
    }

    public Order2(String id, String uid, String pid, String sid, String saddress, String sname, String fee, String real_fee, String refund_fee, String time_info, String extra, String create_time, String update_time, String expire_time, String seat_type, String status, String is_refund, String use_desc, String type_id, String type_desc, String room_count, String start_date, String end_date, String repeat, String start_time, String end_time, String use_id, String total_hour, String commission, String coupon_id, String is_send, String school_remark, String student_type, String student_count, String is_admin) {
        this.id = id;
        this.uid = uid;
        this.pid = pid;
        this.sid = sid;
        this.saddress = saddress;
        this.sname = sname;
        this.fee = fee;
        this.real_fee = real_fee;
        this.refund_fee = refund_fee;
        this.time_info = time_info;
        this.extra = extra;
        this.create_time = create_time;
        this.update_time = update_time;
        this.expire_time = expire_time;
        this.seat_type = seat_type;
        this.status = status;
        this.is_refund = is_refund;
        this.use_desc = use_desc;
        this.type_id = type_id;
        this.type_desc = type_desc;
        this.room_count = room_count;
        this.start_date = start_date;
        this.end_date = end_date;
        this.repeat = repeat;
        this.start_time = start_time;
        this.end_time = end_time;
        this.use_id = use_id;
        this.total_hour = total_hour;
        this.commission = commission;
        this.coupon_id = coupon_id;
        this.is_send = is_send;
        this.school_remark = school_remark;
        this.student_type = student_type;
        this.student_count = student_count;
        this.is_admin = is_admin;
    }

    public Order2(
            String id,
            String pid,
            String sid,
            String s_address,
            String sname,
            String use_desc,
            String type_id,
            String type_desc,
            String room_count,
            String start_date,
            String end_date,
            String repeat,
            String start_time,
            String end_time,
            String pic_url,
            String student_type,
            List<DeviceEntity> device_nofree,
            String use_id,
            RoomAdjustEntity room_adjust) {
        super();
        this.id = id;
        this.pid = pid;
        this.sid = sid;
        this.saddress = s_address;
        this.sname = sname;
        this.use_desc = use_desc;
        this.type_id = type_id;
        this.type_desc = type_desc;
        this.room_count = room_count;
        this.start_date = start_date;
        this.end_date = end_date;
        this.repeat = repeat;
        this.start_time = start_time;
        this.end_time = end_time;
        this.pic_url = pic_url;
        this.student_type = student_type;
        this.device_nofree = device_nofree;
        this.use_id = use_id;
        this.room_adjust = room_adjust;
    }

    private String id;
    private String uid;
    private String pid;
    private String sid;
    private String saddress;
    private String sname;
    private String fee;
    private String real_fee;
    private String refund_fee;
    private String time_info;
    private String extra;
    private String create_time;
    private String update_time;
    private String expire_time;
    private String seat_type;
    private String status;
    private String is_refund;
    private String use_desc;
    private String type_id;
    private String type_desc;
    private String room_count;
    private String start_date;
    private String end_date;
    private String repeat;
    private String start_time;
    private String end_time;
    private String use_id;
    private String total_hour;
    private String commission;
    private String commission_alipay;
    private String commission_diff;
    private String coupon_id;
    private String coupon_no;
    private String is_send;
    private String school_remark;
    private String student_type;
    private String student_count;
    private String is_admin;
    private String time_type;
    private String device_mode;
    private String room_info;
    private String period_mode;
    private String period_mode_d;
    private String period_count;
    private String pic_url;
    private String type_name;//学生类型名
    private String student_desc;//年龄段
    private String lng;
    private String lat;
    private String lng_g;
    private String lat_g;
    private String max_member;
    private String address;
    private String tags;//标签
    private String refund;
    private ArrayList<Order3> order3;
    private List<DeviceEntity> device_nofree;
    private List<DeviceEntity> device_free;
    private String deposit;
    private RoomAdjustEntity room_adjust;
    private ArrayList<DeviceEntity> device;
    private boolean isOpen;//打开或关闭—— 个性化调整（个别日期调整）
    private boolean isHave;//是否有个性化数据源
    private String single_fee;
    private String is_valid;
    private boolean isCheck;
    private String device_str;
    private String course_id;
    private String school_type;
    private String device_fee;
    private String school_phone;
    private String school_tags;
    private String confirm_type;

    public String getConfirm_type() {
        return confirm_type;
    }

    public void setConfirm_type(String confirm_type) {
        this.confirm_type = confirm_type;
    }

    private List<TimeGroup> time_group;
    private List<AvailablePrice> available_price;

    public List<TimeGroup> getTime_group() {
        return time_group;
    }

    public List<AvailablePrice> getAvailable_price() {
        return available_price;
    }

    public String getSchool_tags() {
        return school_tags;
    }

    public String getSchool_phone() {
        return school_phone;
    }

    public String getDevice_fee() {
        return device_fee;
    }

    public String getSchool_type() {
        return school_type;
    }

    public void setSchool_type(String school_type) {
        this.school_type = school_type;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getStudent_desc() {
        return student_desc;
    }

    public void setStudent_desc(String student_desc) {
        this.student_desc = student_desc;
    }

    public String getDevice_str() {
        return device_str;
    }

    public void setDevice_str(String device_str) {
        this.device_str = device_str;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSaddress() {
        return saddress;
    }

    public void setSaddress(String saddress) {
        this.saddress = saddress;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getReal_fee() {
        return real_fee;
    }

    public void setReal_fee(String real_fee) {
        this.real_fee = real_fee;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getTime_info() {
        return time_info;
    }

    public void setTime_info(String time_info) {
        this.time_info = time_info;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getSeat_type() {
        return seat_type;
    }

    public void setSeat_type(String seat_type) {
        this.seat_type = seat_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_refund() {
        return is_refund;
    }

    public void setIs_refund(String is_refund) {
        this.is_refund = is_refund;
    }

    public String getUse_desc() {
        return use_desc;
    }

    public void setUse_desc(String use_desc) {
        this.use_desc = use_desc;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public String getRoom_count() {
        return room_count;
    }

    public void setRoom_count(String room_count) {
        this.room_count = room_count;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUse_id() {
        return use_id;
    }

    public void setUse_id(String use_id) {
        this.use_id = use_id;
    }

    public String getTotal_hour() {
        return total_hour;
    }

    public void setTotal_hour(String total_hour) {
        this.total_hour = total_hour;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getIs_send() {
        return is_send;
    }

    public void setIs_send(String is_send) {
        this.is_send = is_send;
    }

    public String getSchool_remark() {
        return school_remark;
    }

    public void setSchool_remark(String school_remark) {
        this.school_remark = school_remark;
    }

    public String getStudent_type() {
        return student_type;
    }

    public void setStudent_type(String student_type) {
        this.student_type = student_type;
    }

    public String getStudent_count() {
        return student_count;
    }

    public void setStudent_count(String student_count) {
        this.student_count = student_count;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public ArrayList<Order3> getOrder3() {
        return order3;
    }

    public void setOrder3(ArrayList<Order3> order3) {
        this.order3 = order3;
    }

    public ArrayList<DeviceEntity> getDevice() {
        return device;
    }

    public void setDevice(ArrayList<DeviceEntity> device) {
        this.device = device;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public List<DeviceEntity> getDevice_nofree() {
        return device_nofree;
    }

    public void setDevice_nofree(List<DeviceEntity> device_nofree) {
        this.device_nofree = device_nofree;
    }

    public List<DeviceEntity> getDevice_free() {
        return device_free;
    }

    public void setDevice_free(List<DeviceEntity> device_free) {
        this.device_free = device_free;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isHave() {
        return isHave;
    }

    public void setIsHave(boolean isHave) {
        this.isHave = isHave;
    }

    public RoomAdjustEntity getRoom_adjust() {
        return room_adjust;
    }

    public void setRoom_adjust(RoomAdjustEntity room_adjust) {
        this.room_adjust = room_adjust;
    }

    public String getMax_member() {
        return max_member;
    }

    public void setMax_member(String max_member) {
        this.max_member = max_member;
    }

    public String getSingle_fee() {
        return single_fee;
    }

    public void setSingle_fee(String single_fee) {
        this.single_fee = single_fee;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public boolean getCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getCommission_alipay() {
        return commission_alipay;
    }

    public void setCommission_alipay(String commission_alipay) {
        this.commission_alipay = commission_alipay;
    }

    public String getCommission_diff() {
        return commission_diff;
    }

    public void setCommission_diff(String commission_diff) {
        this.commission_diff = commission_diff;
    }

    public String getCoupon_no() {
        return coupon_no;
    }

    public void setCoupon_no(String coupon_no) {
        this.coupon_no = coupon_no;
    }

    public String getTime_type() {
        return time_type;
    }

    public void setTime_type(String time_type) {
        this.time_type = time_type;
    }

    public String getDevice_mode() {
        return device_mode;
    }

    public void setDevice_mode(String device_mode) {
        this.device_mode = device_mode;
    }

    public String getRoom_info() {
        return room_info;
    }

    public void setRoom_info(String room_info) {
        this.room_info = room_info;
    }

    public String getPeriod_mode() {
        return period_mode;
    }

    public void setPeriod_mode(String period_mode) {
        this.period_mode = period_mode;
    }

    public String getPeriod_mode_d() {
        return period_mode_d;
    }

    public void setPeriod_mode_d(String period_mode_d) {
        this.period_mode_d = period_mode_d;
    }

    public String getPeriod_count() {
        return period_count;
    }

    public void setPeriod_count(String period_count) {
        this.period_count = period_count;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getLng_g() {
        return lng_g;
    }

    public void setLng_g(String lng_g) {
        this.lng_g = lng_g;
    }

    public String getLat_g() {
        return lat_g;
    }

    public void setLat_g(String lat_g) {
        this.lat_g = lat_g;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
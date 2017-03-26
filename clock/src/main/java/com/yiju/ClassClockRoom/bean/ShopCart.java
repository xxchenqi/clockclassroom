package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车的数据对象 2015/12/8/0008.
 */
public class ShopCart implements Serializable {

    /**
     * code : 1
     * data : [{"commission":"","coupon_id":"0","create_time":"1451961123","device":[{"device_id":"5","device_name":"白板（含白板笔）","fee":"0.00","id":"373","is_refund":"0","num":"20","oid":"389","poid":"0","single_fee":"0.00"}],"end_date":"2016-01-05","end_time":"1215","expire_time":"1451962323","extra":"","fee":"80.00","id":"3023","is_refund":"0","is_send":"0","is_valid":"0","pic_url":"http://get.file.dc.cric.com/SZJSbe0f6ce966a019719531706a757090d1_265X265_0_0_3.jpg","pid":"","real_fee":"","refund_fee":"0.00","repeat":"","room_count":"1","saddress":"中山北路756号达人湾创智广场2楼","school_remark":"","seat_type":"1","sid":"11","single_fee":"80","sname":"闸北达人湾中心店","start_date":"2016-01-05","start_time":"1115","status":"3","time_info":"","total_hour":"1","type_desc":"中间课室","type_id":"65","uid":"78","update_time":"","use_desc":"外语","use_id":"45"},{"commission":"","coupon_id":"0","create_time":"1451957769","device":[],"end_date":"2016-01-05","end_time":"1115","expire_time":"1451958969","extra":"","fee":"45.00","id":"3012","is_refund":"0","is_send":"0","is_valid":"0","pic_url":"http://get.file.dc.cric.com/SZJS3676aa381f3b96cd418f0158dbfea31f_265X265_0_0_3.jpg","pid":"","real_fee":"","refund_fee":"0.00","repeat":"","room_count":"1","saddress":"中山北路756号达人湾创智广场2楼","school_remark":"","seat_type":"1","sid":"11","single_fee":"45","sname":"闸北达人湾中心店","start_date":"2016-01-05","start_time":"1015","status":"3","time_info":"","total_hour":"1","type_desc":"小间课室","type_id":"64","uid":"78","update_time":"","use_desc":"数学","use_id":"44"},{"commission":"","coupon_id":"0","create_time":"1451902553","device":[],"end_date":"2016-01-06","end_time":"1000","expire_time":"1451903755","extra":"","fee":"180.00","id":"2988","is_refund":"0","is_send":"1","is_valid":"0","pic_url":"http://get.file.dc.cric.com/SZJS3676aa381f3b96cd418f0158dbfea31f_265X265_0_0_3.jpg","pid":"","real_fee":"","refund_fee":"0.00","repeat":"","room_count":"2","saddress":"中山北路756号达人湾创智广场2楼","school_remark":"","seat_type":"1","sid":"11","single_fee":"90","sname":"闸北达人湾中心店","start_date":"2016-01-05","start_time":"900","status":"3","time_info":"","total_hour":"2","type_desc":"小间课室","type_id":"64","uid":"78","update_time":"","use_desc":"数学","use_id":"44"},{"commission":"","coupon_id":"0","create_time":"1451902471","device":[],"end_date":"2016-01-06","end_time":"1115","expire_time":"1451903673","extra":"","fee":"90.00","id":"2987","is_refund":"0","is_send":"1","is_valid":"0","pic_url":"http://get.file.dc.cric.com/SZJS74093e29a8306880de9129890dead259_265X265_0_0_3.jpg","pid":"","real_fee":"","refund_fee":"0.00","repeat":"","room_count":"1","saddress":"中山北路756号达人湾创智广场2楼","school_remark":"","seat_type":"1","sid":"11","single_fee":"90","sname":"闸北达人湾中心店","start_date":"2016-01-05","start_time":"1015","status":"3","time_info":"","total_hour":"2","type_desc":"小间课室","type_id":"64","uid":"78","update_time":"","use_desc":"物理","use_id":"46"},{"commission":"","coupon_id":"0","create_time":"1451896479","device":[],"end_date":"2016-01-06","end_time":"1115","expire_time":"1451897681","extra":"","fee":"70.00","id":"2960","is_refund":"0","is_send":"1","is_valid":"0","pic_url":"http://get.file.dc.cric.com/SZJSbe0f6ce966a019719531706a757090d1_265X265_0_0_3.jpg","pid":"","real_fee":"","refund_fee":"0.00","repeat":"","room_count":"1","saddress":"中山北路756号达人湾创智广场2楼","school_remark":"","seat_type":"1","sid":"11","single_fee":"70","sname":"闸北达人湾中心店","start_date":"2016-01-05","start_time":"1015","status":"3","time_info":"","total_hour":"2","type_desc":"迷你间课室","type_id":"63","uid":"78","update_time":"","use_desc":"外语","use_id":"45"},{"commission":"","coupon_id":"0","create_time":"1451896466","device":[],"end_date":"2016-01-06","end_time":"1000","expire_time":"1451897668","extra":"","fee":"70.00","id":"2959","is_refund":"0","is_send":"1","is_valid":"0","pic_url":"http://get.file.dc.cric.com/SZJSbe0f6ce966a019719531706a757090d1_265X265_0_0_3.jpg","pid":"","real_fee":"","refund_fee":"0.00","repeat":"","room_count":"1","saddress":"中山北路756号达人湾创智广场2楼","school_remark":"","seat_type":"1","sid":"11","single_fee":"70","sname":"闸北达人湾中心店","start_date":"2016-01-05","start_time":"900","status":"3","time_info":"","total_hour":"2","type_desc":"迷你间课室","type_id":"63","uid":"78","update_time":"","use_desc":"外语","use_id":"45"}]
     * msg : ok
     * nowtime : 1451964437
     */

    private String code;
    private String msg;
    private String nowtime;
    private ContactTelName contact;
    private String invoice_xmmc;
    private String last_invoice_type;

    public String getLast_invoice_type() {
        return last_invoice_type;
    }

    /**
     * commission :
     * coupon_id : 0
     * create_time : 1451961123
     * device : [{"device_id":"5","device_name":"白板（含白板笔）","fee":"0.00","id":"373","is_refund":"0","num":"20","oid":"389","poid":"0","single_fee":"0.00"}]
     * end_date : 2016-01-05
     * end_time : 1215
     * expire_time : 1451962323
     * extra :
     * fee : 80.00
     * id : 3023
     * is_refund : 0
     * is_send : 0
     * is_valid : 0
     * pic_url : http://get.file.dc.cric.com/SZJSbe0f6ce966a019719531706a757090d1_265X265_0_0_3.jpg
     * pid :
     * real_fee :
     * refund_fee : 0.00
     * repeat :
     * room_count : 1
     * saddress : 中山北路756号达人湾创智广场2楼
     * school_remark :
     * seat_type : 1
     * sid : 11
     * single_fee : 80
     * sname : 闸北达人湾中心店
     * start_date : 2016-01-05
     * start_time : 1115
     * status : 3
     * time_info :
     * total_hour : 1
     * type_desc : 中间课室
     * type_id : 65
     * uid : 78
     * update_time :
     * use_desc : 外语
     * use_id : 45
     */

    private List<Order2> data;
    private ArrayList<InvoiceContacts> invoice_contacts;

    public String getInvoice_xmmc() {
        return invoice_xmmc;
    }

    public ArrayList<InvoiceContacts> getInvoiceContactses() {
        return invoice_contacts;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public void setData(List<Order2> data) {
        this.data = data;
    }


    public String getMsg() {
        return msg;
    }


    public List<Order2> getData() {
        return data;
    }

    public ContactTelName getContact() {
        return contact;
    }

    public void setContact(ContactTelName contact) {
        this.contact = contact;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNowtime() {
        return nowtime;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }
}

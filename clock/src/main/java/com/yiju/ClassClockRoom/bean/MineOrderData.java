package com.yiju.ClassClockRoom.bean;

import com.yiju.ClassClockRoom.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/5/18 10:29
 * ----------------------------------------
 */
public class MineOrderData implements Serializable {
    private String id;
    private String status;
    private String remark;
    private String contact_name;
    private String contact_mobile;
    private String batch_name;
    private String school_phone;
    private String fee;
    private String real_fee;
    private String expire_time;
    private String is_refund;
    private String create_time;
    private String trade_id;
    private ArrayList<Order2> order2;

    private String coupon_id;
    private String refund_fee_total;
    private String confirm_type;
    private String invoice_type;
    private String mc;
    private String invoice_xmmc;

    public String getInvoice_rule() {
        return invoice_rule;
    }

    private String invoice_rule;
    private String pdf_url;

    public String getPay_method() {
        return pay_method;
    }

    private String pay_method;//1：支付宝 6：在线支付 5:余额   其他(其他支付方式)
    private int time;
    private boolean isChoose = false;
    private boolean cbChoose = false;
    private boolean statusFlag = false;

    public void setReal_fee(String real_fee) {
        this.real_fee = real_fee;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public String getMc() {
        return mc;
    }

    public String getInvoice_xmmc() {
        return invoice_xmmc;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public String getConfirm_type() {
        return confirm_type;
    }

    public boolean isStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(boolean statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public String getSchool_phone() {
        return school_phone;
    }

    public void setSchool_phone(String school_phone) {
        this.school_phone = school_phone;
    }

    public boolean isCbChoose() {
        return cbChoose;
    }

    public void setCbChoose(boolean cbChoose) {
        this.cbChoose = cbChoose;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefund_fee_total() {
        return StringUtils.isNullString(refund_fee_total) ? "" : refund_fee_total;
    }

    public String getIs_refund() {
        return StringUtils.isNullString(is_refund) ? "0" : is_refund;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getId() {
        return StringUtils.isNullString(id) ? "" : id;
    }

    public String getStatus() {
        return StringUtils.isNullString(status) ? "" : status;
    }

    public String getRemark() {
        return StringUtils.isNullString(remark) ? "" : remark;
    }

    public String getContact_name() {
        return StringUtils.isNullString(contact_name) ? "" : contact_name;
    }

    public String getContact_mobile() {
        return StringUtils.isNullString(contact_mobile) ? "" : contact_mobile;
    }

    public String getFee() {
        return StringUtils.isNullString(fee) ? "" : fee;
    }

    public String getReal_fee() {
        return StringUtils.isNullString(real_fee) ? "" : real_fee;
    }

    public String getExpire_time() {
        return StringUtils.isNullString(expire_time) ? "" : expire_time;
    }

    public ArrayList<Order2> getOrder2() {
        return order2;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }
}
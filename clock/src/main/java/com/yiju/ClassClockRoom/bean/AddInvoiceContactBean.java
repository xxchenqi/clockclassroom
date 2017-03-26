package com.yiju.ClassClockRoom.bean;

/**
 * ----------------------------------------
 * 注释:新增发票联系人BEAN
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/10/20 11:20
 * ----------------------------------------
 */
public class AddInvoiceContactBean {

    /**
     * code : 1
     * msg : 新增发票信息成功
     * invoice_contact_id : 50
     */

    private String code;
    private String msg;
    private int invoice_contact_id;

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

    public int getInvoice_contact_id() {
        return invoice_contact_id;
    }

    public void setInvoice_contact_id(int invoice_contact_id) {
        this.invoice_contact_id = invoice_contact_id;
    }
}

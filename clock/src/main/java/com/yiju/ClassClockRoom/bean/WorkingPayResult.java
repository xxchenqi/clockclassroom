package com.yiju.ClassClockRoom.bean;

/**
 * 获取支付工作密钥bean
 * Created by wh on 2016/7/28.
 */
public class WorkingPayResult {

    /**
     * code : 1
     * msg : ok
     * signatureKey : 99D3TYX73UQ9XZHUILO41CNJIOGIIZ75
     * cipherKey : GVDSOPSR6BEBDS3XID6KLYY9NHH5DFG5
     * pay_uid : 1000026
     * partner_id : 100002
     */

    private String code;
    private String msg;
    private String signatureKey;
    private String cipherKey;
    private String pay_uid;
    private int partner_id;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public void setCipherKey(String cipherKey) {
        this.cipherKey = cipherKey;
    }

    public void setPay_uid(String pay_uid) {
        this.pay_uid = pay_uid;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public String getCipherKey() {
        return cipherKey;
    }

    public String getPay_uid() {
        return pay_uid;
    }

    public int getPartner_id() {
        return partner_id;
    }
}

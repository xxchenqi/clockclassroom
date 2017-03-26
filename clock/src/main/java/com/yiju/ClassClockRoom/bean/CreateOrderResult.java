package com.yiju.ClassClockRoom.bean;

/**
 * 易支付调取支付接口时所需的报文体与签名resultBean
 * Created by wh on 2016/7/29.
 */
public class CreateOrderResult {

    /**
     * code : 1
     * url : http://10.0.57.44:8138/access-pos/access/cashier
     * partner : 100002
     * version : v1.1
     * sign : mY6QM9yfYntwQbI8D6SdegJ1cKszwW7MxeSbliUuzkbN6PipOBQt2kH3hDV9pxBi8HhYHbqtc3hxOsUpbgEcXr7B6w+SMf7LKposlnRiIOWxqURNdLiRdzez4I7q4Z4MPre2fA43gLce+Z8t08EQ3pKMQ08ahoRw+Rx21UOtV6Q=
     * body : {"service":"acquirer","requestTime":1469778159000,"traceNO":"201607291123190802R","payer":"1000050","amount":"35.00","describe":"","notifyUrl":"http:\/\/172.28.70.47\/api_v5\/callback\/ejupay_callback.php","returnUrl":"","terminalType":"SDK"}
     */

    private String code;
    private String url;
    private int partner;
    private String version;
    private String sign;
    private String body;

    public void setCode(String code) {
        this.code = code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPartner(int partner) {
        this.partner = partner;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public int getPartner() {
        return partner;
    }

    public String getVersion() {
        return version;
    }

    public String getSign() {
        return sign;
    }

    public String getBody() {
        return body;
    }
}

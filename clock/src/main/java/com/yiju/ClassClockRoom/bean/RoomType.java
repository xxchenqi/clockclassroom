package com.yiju.ClassClockRoom.bean;


import java.util.List;

/**
 * 房间类型 2015/12/14/0014.
 */
class RoomType {

    /**
     * code : 1
     * data : [{"area":"-1","desc":"小间","desc_app":"文化教学1-2人 形体教学3-4人","id":"41","pic_small":"http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSbdf3bf1da3405725be763540d6601144_314X236_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003","price_weekday":"36.00","price_weekend":"68.00"},{"area":"-1","desc":"中间","desc_app":"文化教学1-2人 形体教学3-4人","id":"42","pic_small":"","price_weekday":"56.00","price_weekend":"108.00"},{"area":"-1","desc":"大间","desc_app":"文化教学1-2人 形体教学3-4人","id":"43","pic_small":"","price_weekday":"72.00","price_weekend":"136.00"},{"area":"-1","desc":"大套间","desc_app":"文化教学1-2人 形体教学3-4人","id":"44","pic_small":"","price_weekday":"144.00","price_weekend":"272.00"},{"area":"1~100","attr":"面积：1~100 有窗 地板材质：地毯 迷你间 ","desc":"高级相仿test","desc_app":"文化教学1-2人 形体教学3-4人","id":"59","pic_small":"","price_weekday":"99.00","price_weekend":"100.00"}]
     * msg : ok
     */

    private Integer code;
    private String msg;
    /**
     * area : -1
     * desc : 小间
     * desc_app : 文化教学1-2人 形体教学3-4人
     * id : 41
     * pic_small : http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJSbdf3bf1da3405725be763540d6601144_314X236_0_0_3.jpg&key=b97d8b9dc9a22a4f8d8273e50317c884&permit_code=A003
     * price_weekday : 36.00
     * price_weekend : 68.00
     */

    private List<RoomTypeInfo> data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<RoomTypeInfo> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<RoomTypeInfo> getData() {
        return data;
    }

}

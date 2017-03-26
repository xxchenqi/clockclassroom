package com.yiju.ClassClockRoom.bean;

import java.util.List;

/**
 * 首页数据 2015/11/24.
 */
public class ClassRoom {

    /**
     * code : 1 msg : ok data :
     * [{"id":"8","tags":"全科辅导 交通便利 图书阅览 商务洽谈","name":"徐汇旗舰店"
     * ,"address":"徐汇区沪闵路8075号8楼"
     * ,"lng":"121.42014200786","lat":"31.14826073643","pic_big":
     * "http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS166eb100184a2f3e9735daa0b49555a3_800X600_0_0_0.jpg&key=733b4369963c24d85eb2d6f2a9da3471&permit_code=A003"
     * ,"cate":null},{"id":"9","tags":"多校环绕 特色教学 生源充足 小黄人主题","name":"普陀中心店",
     * "address"
     * :"大渡河路1517号友通数码港","lng":"121.40401033992","lat":"31.25167061337",
     * "pic_big":
     * "http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS166eb100184a2f3e9735daa0b49555a3_800X600_0_0_0.jpg&key=733b4369963c24d85eb2d6f2a9da3471&permit_code=A003"
     * ,"cate":null},{"id":"10","tags":"双语教学 韩国文化 台湾教育 兴趣天地","name":"闵行中心店",
     * "address":"合川路2928号A幢新乐坊","lng":"121.38848402479","lat":"31.17923264688",
     * "pic_big":
     * "http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS166eb100184a2f3e9735daa0b49555a3_800X600_0_0_0.jpg&key=733b4369963c24d85eb2d6f2a9da3471&permit_code=A003"
     * ,"cate":null},{"id":"11","tags":"名师汇集 交通便利 核心商圈 绘画展示","name":"闸北中心店",
     * "address"
     * :"中山北路756号达人弯创智广场","lng":"121.46627229546","lat":"31.26623501404"
     * ,"pic_big":
     * "http://i.get.file.dc.cric.com/FileReaderInner.php?file=SZJS166eb100184a2f3e9735daa0b49555a3_800X600_0_0_0.jpg&key=733b4369963c24d85eb2d6f2a9da3471&permit_code=A003"
     * ,"cate":null}]
     */

    private String code;
    private String msg;
    /**
     * id : 8 tags : 全科辅导 交通便利 图书阅览 商务洽谈 name : 徐汇旗舰店 address : 徐汇区沪闵路8075号8楼
     * lng : 121.42014200786 lat : 31.14826073643 pic_big :
     * http://i.get.file.dc.cric.com/FileReaderInner.php?file=
     * SZJS166eb100184a2f3e9735daa0b49555a3_800X600_0_0_0
     * .jpg&key=733b4369963c24d85eb2d6f2a9da3471&permit_code=A003 cate : null
     */

    private List<Room> data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<Room> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<Room> getData() {
        return data;
    }

}

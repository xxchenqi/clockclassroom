package com.yiju.ClassClockRoom.bean;

import java.io.Serializable;

/**
 * Created by Sandy on 2016/11/9/0009.
 */
public class StoreShareBean implements Serializable{

    /**
     * sms : {"content":"我向您推荐了浦东龙阳中心， 快来体验吧！http://172.28.70.47/h5_v9/roomtypelist.html?sid=16","picurl":"http://get.file.dc.cric.com/SZJSe8ff1003bad7ceaf243994df52e77900_200X200_0_0_3.jpg","title":"【时钟教室】","url":"http://172.28.70.47/h5_v9/roomtypelist.html?sid=16"}
     * weixin : {"content":"我向您推荐了浦东龙阳中心， 快来体验吧！","picurl":"http://get.file.dc.cric.com/SZJSe8ff1003bad7ceaf243994df52e77900_200X200_0_0_3.jpg","title":"【时钟教室】","url":"http://172.28.70.47/h5_v9/roomtypelist.html?sid=16"}
     * weixin_friend : {"content":"我向您推荐了浦东龙阳中心， 快来体验吧！","picurl":"http://get.file.dc.cric.com/SZJSe8ff1003bad7ceaf243994df52e77900_200X200_0_0_3.jpg","title":"【时钟教室】","url":"http://172.28.70.47/h5_v9/roomtypelist.html?sid=16"}
     */


    private ShareBean sms;
    /**
     * content : 我向您推荐了浦东龙阳中心， 快来体验吧！
     * picurl : http://get.file.dc.cric.com/SZJSe8ff1003bad7ceaf243994df52e77900_200X200_0_0_3.jpg
     * title : 【时钟教室】
     * url : http://172.28.70.47/h5_v9/roomtypelist.html?sid=16
     */

    private ShareBean weixin;
    /**
     * content : 我向您推荐了浦东龙阳中心， 快来体验吧！
     * picurl : http://get.file.dc.cric.com/SZJSe8ff1003bad7ceaf243994df52e77900_200X200_0_0_3.jpg
     * title : 【时钟教室】
     * url : http://172.28.70.47/h5_v9/roomtypelist.html?sid=16
     */

    private ShareBean weixin_friend;

    private ShareBean qq;

    public ShareBean getQq() {
        return qq;
    }

    public void setQq(ShareBean qq) {
        this.qq = qq;
    }

    public ShareBean getWeibo() {
        return weibo;
    }

    public void setWeibo(ShareBean weibo) {
        this.weibo = weibo;
    }

    private ShareBean weibo;

    public ShareBean getSms() {
        return sms;
    }

    public void setSms(ShareBean sms) {
        this.sms = sms;
    }

    public ShareBean getWeixin() {
        return weixin;
    }

    public void setWeixin(ShareBean weixin) {
        this.weixin = weixin;
    }

    public ShareBean getWeixin_friend() {
        return weixin_friend;
    }

    public void setWeixin_friend(ShareBean weixin_friend) {
        this.weixin_friend = weixin_friend;
    }

}

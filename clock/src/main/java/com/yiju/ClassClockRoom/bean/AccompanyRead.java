package com.yiju.ClassClockRoom.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.StringUtils;

/**
 * 陪读返回信息
 *
 * @author geliping
 */
public class AccompanyRead implements Parcelable {
    private int id;
    private String date;
    private String room_no;
    private String start_time;
    private String end_time;
    private String video_url;
    private String video_id;
    private String name;
    private String address;
    private String video_ip;
    private String video_port;
    private String video_username;
    private String video_password;
    // 0-已取消 1-已加入
    private int hasRemember;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
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

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_id() {
        return StringUtils.isNullString(video_id) ? "" : video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return StringUtils.isNullString(address) ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVideo_ip() {
        return StringUtils.isNullString(video_ip) ? "" : video_ip;
    }

    public void setVideo_ip(String video_ip) {
        this.video_ip = video_ip;
    }

    public String getVideo_port() {
        return StringUtils.isNullString(video_port) ? "9999" : video_port;
    }

    public void setVideo_port(String video_port) {
        this.video_port = video_port;
    }

    public String getVideo_username() {
        return StringUtils.isNullString(video_username) ? ""
                : video_username;
    }

    public void setVideo_username(String video_username) {
        this.video_username = video_username;
    }

    public String getVideo_password() {
        return StringUtils.isNullString(video_password) ? ""
                : video_password;
    }

    public void setVideo_password(String video_password) {
        this.video_password = video_password;
    }

    public int getHasRemember() {
        return hasRemember;
    }

    public void setHasRemember(int hasRemember) {
        this.hasRemember = hasRemember;
    }

    //转义后的start_time
    public String getStartTimeStr() {
        String time = start_time;
        time = time.substring(0, time.length() - 2) + ":"
                + time.substring(time.length() - 2, time.length());
        return time;
    }

    //转义后的end_time
    public String getEndTimeStr() {
        String time = end_time;
        time = time.substring(0, time.length() - 2) + ":"
                + time.substring(time.length() - 2, time.length());
        return time;
    }

    // 获得开始日期时间
    public String getStart_DateTime_change() {
        return DateUtil.StringPattern(date + " " + getStartTimeStr(),
                "yyyy-MM-dd HH:mm", "yyyy年MM月dd日HH:mm");
    }

    // 获得结束日期时间
    public String getEnd_DateTime_change() {
        return DateUtil.StringPattern(date + " " + getEndTimeStr(),
                "yyyy-MM-dd HH:mm", "yyyy年MM月dd日HH:mm");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(room_no);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeString(video_url);
        dest.writeString(video_id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(video_ip);
        dest.writeString(video_port);
        dest.writeString(video_username);
        dest.writeString(video_password);
        dest.writeInt(hasRemember);
    }

    public static final Parcelable.Creator<AccompanyRead> CREATOR = new Creator<AccompanyRead>() {

        @Override
        public AccompanyRead[] newArray(int size) {
            return new AccompanyRead[size];
        }

        @Override
        public AccompanyRead createFromParcel(Parcel source) {
            AccompanyRead bean = new AccompanyRead();
            bean.id = source.readInt();
            bean.date = source.readString();
            bean.room_no = source.readString();
            bean.start_time = source.readString();
            bean.end_time = source.readString();
            bean.video_url = source.readString();
            bean.video_id = source.readString();
            bean.name = source.readString();
            bean.address = source.readString();
            bean.video_ip = source.readString();
            bean.video_port = source.readString();
            bean.video_username = source.readString();
            bean.video_password = source.readString();
            bean.hasRemember = source.readInt();
            return bean;
        }
    };

}

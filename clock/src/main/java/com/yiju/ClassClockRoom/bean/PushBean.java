package com.yiju.ClassClockRoom.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yiju.ClassClockRoom.util.StringUtils;

/**
 * 推送数据
 *
 * @author geliping
 */
public class PushBean implements Parcelable {

    private String action;
    private String oid;
    private String order_type;
    private String course_id;
    private String title;
    private String content;
    private String url;
    private String teacherID;
    private String detail_id;
    private String type;
    private String state;
    private String imageUrl;
    private String createDate;
    private int mid;
    private int code;//自定义的code(随机数)
    private String special_id;

    public PushBean() {

    }

    public PushBean(String action, String oid, String title, String content, String url, String teacherID, String type, String state, String imageUrl, String createDate, int mid) {
        this.action = action;
        this.oid = oid;
        this.title = title;
        this.content = content;
        this.url = url;
        this.teacherID = teacherID;
        this.type = type;
        this.state = state;
        this.imageUrl = imageUrl;
        this.createDate = createDate;
        this.mid = mid;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOid() {
        return StringUtils.formatNullString(oid);
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getTitle() {
        return StringUtils.isNotNullString(title) ? title : "时钟教室";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return StringUtils.formatNullString(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTeacherID() {
        return StringUtils.formatNullString(teacherID);
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSpecial_id() {
        return special_id;
    }

    public void setSpecial_id(String special_id) {
        this.special_id = special_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(oid);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(teacherID);
        dest.writeString(type);
        dest.writeString(state);
        dest.writeString(imageUrl);
        dest.writeString(createDate);
        dest.writeInt(mid);
    }

    public static final Parcelable.Creator<PushBean> CREATOR = new Creator<PushBean>() {

        @Override
        public PushBean[] newArray(int size) {
            return new PushBean[size];
        }

        @Override
        public PushBean createFromParcel(Parcel source) {
            PushBean bean = new PushBean();
            bean.action = source.readString();
            bean.oid = source.readString();
            bean.title = source.readString();
            bean.content = source.readString();
            bean.url = source.readString();
            bean.teacherID = source.readString();
            bean.type = source.readString();
            bean.state = source.readString();
            bean.imageUrl = source.readString();
            bean.createDate = source.readString();
            bean.mid = source.readInt();
            return bean;
        }
    };

}

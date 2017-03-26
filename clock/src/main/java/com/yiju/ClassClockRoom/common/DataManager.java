package com.yiju.ClassClockRoom.common;

import com.yiju.ClassClockRoom.bean.AccompanyRead;
import com.yiju.ClassClockRoom.bean.HotSearch;
import com.yiju.ClassClockRoom.bean.PushBean;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.util.net.ClassEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 葛立平
 * 2016/2/29 18:03
 * 数据管理类
 */
public class DataManager {
    private static DataManager _instance;


    public static DataManager getInstance() {
        synchronized (DataManager.class) {
            if (_instance == null) {
                _instance = new DataManager();
            }
            return _instance;
        }
    }

    /**
     * 基本数据分发
     *
     * @param bean      数据源
     * @param eventCode 返回码
     */
    public void baseEvent(BaseEntity bean, int eventCode) {
        EventBus.getDefault().post(new ClassEvent<>(eventCode, bean));
    }


    //视频陪读类型
    public static final int Video_Data = 0;
    private AccompanyRead _accompanyRead;

    public AccompanyRead getAccompanyRead() {
        return _accompanyRead;
    }

    public void updateAccompanyRead(int code, AccompanyRead accompanyRead) {
        _accompanyRead = accompanyRead;
        EventBus.getDefault()
                .post(new ClassEvent<>(Video_Data, code, _accompanyRead));
    }

    public void updateAccompanyRead(AccompanyRead accompanyRead) {
        _accompanyRead = accompanyRead;
        EventBus.getDefault()
                .post(new ClassEvent<>(Video_Data, _accompanyRead));
    }


    //热搜关键字
    public static final int Hot_Search_Data = 1;
    public static final int Number_limit = 10;//限制数据条数
    private List<HotSearch> _hotSearchList;

    public List<HotSearch> getHotSearchList() {
        return _hotSearchList;
    }

    public void updateHotSearchList(List<HotSearch> hotSearchList) {
        if (hotSearchList.size() > Number_limit) {//判断是否超出限制
            if (_hotSearchList == null) {
                _hotSearchList = new ArrayList<>();
            } else {
                _hotSearchList.clear();
            }
            for (int i = 0; i < Number_limit; i++) {
                _hotSearchList.add(hotSearchList.get(i));
            }
        } else {
            _hotSearchList = hotSearchList;
        }
        EventBus.getDefault().post(new ClassEvent<>(Hot_Search_Data, _hotSearchList));
    }


    //推送通知栏消息
    public static final int Notification_Data = 2;
    private List<PushBean> _pushBeans = new ArrayList<>();

    public List<PushBean> get_pushBeans() {
        return _pushBeans;
    }

    /**
     * 更新推送数据
     */
    public void updatePushBeans(PushBean bean) {
        if (!_pushBeans.contains(bean)) {
            _pushBeans.add(bean);
        }
    }

    //根据ID删除通知消息列表数据
    public void deletePushById(int id) {
        if (_pushBeans == null) {
            return;
        }
        for (PushBean bean : _pushBeans) {
            if (bean.getMid() == id) {
                _pushBeans.remove(bean);
                break;
            }
        }
    }

    /**
     * 根据ID获得推送对象
     *
     * @param id id
     */
    public PushBean getPushBeanById(int id) {
        if (_pushBeans == null || _pushBeans.size() == 0) {
            return null;
        }
        for (PushBean bean : _pushBeans) {
            if (bean.getMid() != 0) {
                if (bean.getMid() == id) {
                    return bean;
                }
            } else {
                if (bean.getCode() == id) {
                    return bean;
                }
            }
        }
        return null;
    }

    //首页未读消息数量
    public static final int Index_No_Read_Data = 3;
    public int _noReadCount = 0;

    public int getNoReadCount() {
        return _noReadCount;
    }

    /**
     * 更新未读消息数据
     */
    public void updateNoReadCount(int noReadCount) {
        this._noReadCount = noReadCount;
        EventBus.getDefault().post(new ClassEvent<>(Index_No_Read_Data, _noReadCount));
    }

    /**
     * 增加未读消息数
     */
    public void addNoReadCount(int add) {
        this._noReadCount = _noReadCount + add;
        EventBus.getDefault().post(new ClassEvent<>(Index_No_Read_Data, _noReadCount));
    }

    //上传的图片信息
//    public static final int UPLOAD_PHOTO_DATA = 4;
//    private PictureWrite _pictureWrite;


    //存储图片链接
    public static final int SAVE_PHOTO_URL = 5;
    private MineOrder _mineOrder;

    public void saveUploadPhotoUrl(MineOrder mineOrder) {
        this._mineOrder = mineOrder;
        EventBus.getDefault().post(new ClassEvent<>(SAVE_PHOTO_URL, _mineOrder));
    }

    //更改是否展示我的老师信息
    public static final int SWITCH_TEACHER_INFO = 6;

    public void switchTeacherInfo(BaseEntity bean) {
        EventBus.getDefault().post(new ClassEvent<>(SWITCH_TEACHER_INFO, bean));
    }

    //更改是否展示我的老师信息
    public static final int LOGOUT_DATA = 7;

    public void logout(BaseEntity bean) {
        EventBus.getDefault().post(new ClassEvent<>(LOGOUT_DATA, bean));
    }

    //关注老师
    public static final int ATTENTION_TEACHER = 8;
    //关注课程
    public static final int ATTENTION_COURSE = 9;
    //取消关注老师
    public static final int ATTENTION_CANCEL_TEACHER = 10;
    //取消关注课程
    public static final int ATTENTION_CANCEL_COURSE = 11;
    //获得课程详情
    public static final int GET_COURSE_DETAIL = 12;
    //报名课程
    public static final int APPLY_COURSE_DATA = 13;
    //获得老师详情
    public static final int GET_TEACHER_DETAIL = 14;
    //登录
    public static final int LOGIN_USER_DATA = 15;

    public static boolean isRequest;//标志第一次进入app请求首页数据


    public static final int MODIFY_MEMBER_DATA_INFO = 16;
    public static final int MODIFY_MEMBER_DATA_OTHER = 17;

    //关注供应商
    public static final int ATTENTION_SP = 18;
    //取消供应商
    public static final int ATTENTION_CANCEL_SP = 19;

    public void modifyMemberInfo(MemberDetailResult bean, boolean flag) {
        if (flag) {
            EventBus.getDefault().post(new ClassEvent<>(MODIFY_MEMBER_DATA_INFO, bean));
        } else {
            EventBus.getDefault().post(new ClassEvent<>(MODIFY_MEMBER_DATA_OTHER, bean));
        }
    }

}

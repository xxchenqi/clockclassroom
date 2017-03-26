package com.yiju.ClassClockRoom.util.net.api;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.bean.result.MessageBoxNoReadResult;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.util.net.HttpApiParam;
import com.yiju.ClassClockRoom.util.net.HttpManage;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;

/**
 * 公共API
 * Created by geliping on 2016/8/11.
 */
public class HttpCommonApi extends BaseSingleton {

    public static HttpCommonApi getInstance() {
        return getSingleton(HttpCommonApi.class);
    }

    /**
     * 关注操作/取消关注
     *
     * @param action    路由：   关注-interest   取消关注-unInterest
     * @param uid       用户id
     * @param detail_id 关注对象ID
     * @param type      关注类型:   老师-1    课程-2
     */
    public static void attentionAction(String action, String uid, String detail_id,
                                       String type) {
        attentionAction(action, uid, detail_id, type, null);
    }

    /**
     * 关注操作/取消关注
     *
     * @param action        路由：   关注-interest   取消关注-unInterest
     * @param uid           用户id
     * @param detail_id     关注对象ID
     * @param type          关注类型:   老师-1    课程-2
     * @param resultCallImp 回调自己处理
     */
    public static void attentionAction(final String action, String uid, String detail_id,
                                       final String type, final ResultCallImpl<BaseEntity> resultCallImp) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .baseCommonApi(HttpApiParam.attentionAction(action, uid, detail_id, type)),
                new ResultCallImpl<BaseEntity>() {
                    @Override
                    public void onNext(BaseEntity bean) {
                        if (resultCallImp != null) {
                            resultCallImp.onNext(bean);
                        } else {
                            if ("interest".equals(action)) {          //关注
                                if ("1".equals(type)) {                       //关注老师
                                    DataManager.getInstance()
                                            .baseEvent(bean, DataManager.ATTENTION_TEACHER);
                                } else {                                      //关注课程
                                    DataManager.getInstance()
                                            .baseEvent(bean, DataManager.ATTENTION_COURSE);
                                }
                            } else {                                  //取消关注
                                if ("1".equals(type)) {                       //取消老师关注
                                    DataManager.getInstance()
                                            .baseEvent(bean, DataManager.ATTENTION_CANCEL_TEACHER);
                                } else {                                      //取消课程关注
                                    DataManager.getInstance()
                                            .baseEvent(bean, DataManager.ATTENTION_CANCEL_COURSE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        if ("interest".equals(action)) {          //关注
                            UIUtils.showToastSafe(R.string.label_attention_fail);
                        } else {                                  //取消关注
                            UIUtils.showToastSafe(R.string.label_cancel_attention_fail);
                        }
                    }
                });
    }

    /**
     * 阅读消息
     * @param mid 消息编号
     */
    public static void readMessage(final int mid) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .readMessage(HttpApiParam.readMessage(mid)),false,
                new ResultCallImpl<MessageBoxNoReadResult>() {
                    @Override
                    public void onNext(MessageBoxNoReadResult bean) {
                        if (bean == null || bean.getData() == null) {
                            return;
                        }
                        if ("1".equals(bean.getData().getCode())) {
                            DataManager.getInstance()
                                    .updateNoReadCount(Integer.parseInt(
                                            bean.getData().getNoread_count()));
                        }
                        DataManager.getInstance().deletePushById(mid);
                    }
                });
    }
}

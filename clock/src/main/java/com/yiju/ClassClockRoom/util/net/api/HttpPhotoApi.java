package com.yiju.ClassClockRoom.util.net.api;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.base.BaseSingleton;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.bean.PictureWrite;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.util.net.HttpApiParam;
import com.yiju.ClassClockRoom.util.net.HttpManage;
import com.yiju.ClassClockRoom.util.net.HttpUrl;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;

import java.io.File;

/**
 * 图片相关API
 * Created by geliping on 2016/8/11.
 */
public class HttpPhotoApi extends BaseSingleton {

    public static HttpPhotoApi getInstance() {
        return getSingleton(HttpPhotoApi.class);
    }

    /**
     * 图片上传
     *
     * @param file 图片文件
     */
    public void uploadPhoto(File file) {
        HttpManage.getInstance().getObject(HttpManage.getInstance()
                        .getApiService(HttpUrl.BASE_PIC_WIRTE)
                        .uploadPhoto(HttpApiParam.uploadPhoto(file)),false,
                new ResultCallImpl<PictureWrite>() {
                    @Override
                    public void onNext(PictureWrite bean) {
                        DataManager.getInstance().uploadPhoto(bean);
                    }
                });
    }

    /**
     * 保存上传图片地址
     *
     * @param uid 用户id
     * @param url 图片地址
     */
    public void saveUploadPhotoUrl(String uid, String username, String password, String third_source, String url) {
        HttpManage.getInstance().getBaseEntity(HttpManage.getInstance().getApiService()
                        .saveUploadPhotoUrl(HttpApiParam.saveUploadPhotoUrl(
                                uid,username,password,third_source, url)),
                new ResultCallImpl<MineOrder>() {
                    @Override
                    public void onNext(MineOrder bean) {
                        DataManager.getInstance().saveUploadPhotoUrl(bean);
                    }

                    @Override
                    public void onError(BaseEntity bean) {
                        super.onError(bean);
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }
                });
    }
}

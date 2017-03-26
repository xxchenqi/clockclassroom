package com.yiju.ClassClockRoom.control;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ShareBean;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

public class ShareControl {

    /**
     * 启动分享
     *
     * @param mActivity ac
     * @param share_MEDIA 分享类型
     * @param bean        分享数据
     */
    public static void StartShare(Activity mActivity,
                                  final SHARE_MEDIA share_MEDIA, ShareBean bean) {
        if (bean != null) {
            if (share_MEDIA == SHARE_MEDIA.WEIXIN) {
                // 微信分享
                ShareMethod(mActivity, SHARE_MEDIA.WEIXIN, bean);
            } else if (share_MEDIA == SHARE_MEDIA.WEIXIN_CIRCLE) {
                // 朋友圈分享
                ShareMethod(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE, bean);
            } else if (share_MEDIA == SHARE_MEDIA.QQ) {
                // QQ分享
                ShareMethod(mActivity, SHARE_MEDIA.QQ, bean);
            } else if (share_MEDIA == SHARE_MEDIA.SINA) {
                // 新浪分享
                ShareMethod(mActivity, SHARE_MEDIA.SINA, bean);
            } else if (share_MEDIA == SHARE_MEDIA.SMS) {
                // 短信分享
                ShareMethod(mActivity, SHARE_MEDIA.SMS, bean);
            }
        }
    }

    private static void ShareMethod(Activity mActivity,
                                    final SHARE_MEDIA share_MEDIA, final ShareBean bean) {
        ShareAction shareAction = new ShareAction(mActivity);
        shareAction.setPlatform(share_MEDIA)
                .withText(bean.getContent())
                .setCallback(new UMShareListener() {

                    @Override
                    public void onResult(SHARE_MEDIA arg0) {
                        if (share_MEDIA != SHARE_MEDIA.SMS) {
                            UIUtils.showToastSafe(R.string.share_success);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA arg0, Throwable arg1) {
                        UIUtils.showToastSafe(R.string.share_fail);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA arg0) {
                        if (!"#时钟教室意见反馈##时钟教室|分时出租#".equals(bean.getContent())) {
                            UIUtils.showToastSafe(R.string.share_cancel);
                        }
                    }
                });
        if (StringUtils.isNotNullString(bean.getUrl())) {
            shareAction.withTargetUrl(bean.getUrl());
        }
        if (StringUtils.isNotNullString(bean.getPicurl())) {
            shareAction.withMedia(new UMImage(mActivity, bean.getPicurl()));
        } else if (bean.getPicicon() != null) {
            shareAction.withMedia(new UMImage(mActivity, bean.getPicicon()));
        }
        if (share_MEDIA != SHARE_MEDIA.SINA) {
            shareAction.withTitle(bean.getTitle());
        }
        shareAction.share();
    }
}

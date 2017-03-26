package com.yiju.ClassClockRoom.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.UserVerifyInfo;
import com.yiju.ClassClockRoom.control.OtherLoginControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * ----------------------------------------
 * 注释: 绑定三方账号
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/1/20 13:33
 * ----------------------------------------
 */
public class PersonalCenter_BindingThreeWayAccountActivity extends BaseActivity implements
        OnClickListener {
    /**
     * 退出按钮
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    /**
     * qq绑定布局
     */
    @ViewInject(R.id.rl_qqbind)
    private RelativeLayout rl_qqbind;
    /**
     * 微信绑定布局
     */
    @ViewInject(R.id.rl_wxbind)
    private RelativeLayout rl_wxbind;
    /**
     * 新浪绑定布局
     */
    @ViewInject(R.id.rl_sinabind)
    private RelativeLayout rl_sinabind;
    /**
     * qq图标
     */
    @ViewInject(R.id.iv_qq)
    private ImageView iv_qq;
    /**
     * 微信图标
     */
    @ViewInject(R.id.iv_wx)
    private ImageView iv_wx;
    /**
     * 新浪图标
     */
    @ViewInject(R.id.iv_sina)
    private ImageView iv_sina;
    /**
     * qq绑定文字
     */
    @ViewInject(R.id.tv_qq)
    private TextView tv_qq;
    /**
     * 微信绑定文字
     */
    @ViewInject(R.id.tv_wx)
    private TextView tv_wx;
    /**
     * 新浪绑定文字
     */
    @ViewInject(R.id.tv_sina)
    private TextView tv_sina;

    private UMShareAPI mShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareAPI = UMShareAPI.get(this);

    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_bindingthreeway;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.label_binding));
        head_back_relative.setOnClickListener(this);


    }

    @Override
    public void initData() {
        //设置第三方账号绑定图标和文字
        String shared_third_qq = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_qq), "");
        String shared_third_wechat = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_wechat), "");
        String shared_third_weibo = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_weibo), "");

        if ("".equals(shared_third_qq)) {
            iv_qq.setImageResource(R.drawable.qq_icon);
            tv_qq.setText(getResources().getString(R.string.label_go_binding));
            tv_qq.setTextColor(getResources().getColor(R.color.app_theme_color));
            rl_qqbind.setOnClickListener(this);
        } else {
            iv_qq.setImageResource(R.drawable.qq_share_icon);
            tv_qq.setText(getResources().getString(R.string.label_has_binding));
            tv_qq.setTextColor(getResources().getColor(R.color.color_gay_99));
        }

        if ("".equals(shared_third_wechat)) {
            iv_wx.setImageResource(R.drawable.wechat_icon_gray);
            tv_wx.setText(getResources().getString(R.string.label_go_binding));
            tv_wx.setTextColor(getResources().getColor(R.color.app_theme_color));
            rl_wxbind.setOnClickListener(this);
        } else {
            iv_wx.setImageResource(R.drawable.wechat_share_icon);
            tv_wx.setText(getResources().getString(R.string.label_has_binding));
            tv_wx.setTextColor(getResources().getColor(R.color.color_gay_99));
        }

        if ("".equals(shared_third_weibo)) {
            iv_sina.setImageResource(R.drawable.sina_icon);
            tv_sina.setText(getResources().getString(R.string.label_go_binding));
            tv_sina.setTextColor(getResources().getColor(R.color.app_theme_color));
            rl_sinabind.setOnClickListener(this);
        } else {
            iv_sina.setImageResource(R.drawable.weibo_share_icon);
            tv_sina.setText(getResources().getString(R.string.label_has_binding));
            tv_sina.setTextColor(getResources().getColor(R.color.color_gay_99));
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_076");
                this.setResult(3);
                this.finish();
                break;

            case R.id.rl_qqbind://绑定qq
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_077");
                OtherLoginControl.binding(PersonalCenter_BindingThreeWayAccountActivity.this, mShareAPI,
                        SHARE_MEDIA.QQ);
                break;
            case R.id.rl_wxbind://绑定微信
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_078");
                OtherLoginControl.binding(PersonalCenter_BindingThreeWayAccountActivity.this, mShareAPI,
                        SHARE_MEDIA.WEIXIN);
                break;
            case R.id.rl_sinabind://绑定新浪
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_079");
                OtherLoginControl.binding(PersonalCenter_BindingThreeWayAccountActivity.this, mShareAPI,
                        SHARE_MEDIA.SINA);
                break;
            default:
                break;
        }
    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_binding_threeway_account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);

        //登录成功后，请求并且刷新界面
//        if (requestCode == 11101 && resultCode == RESULT_OK) {
//            getHttpUtils(uid, OtherLoginControl.third_source + "", OtherLoginControl.THIRD_ID, this);
//        } else if (requestCode == 32973 && resultCode == RESULT_OK) {
//            getHttpUtils(uid, OtherLoginControl.third_source + "", OtherLoginControl.THIRD_ID, this);
//        }
    }


    /**
     * 第三方账号绑定请求
     *
     * @param uid          用户id
     * @param third_source 第三方来源 1=QQ 2=微信 3=微博
     * @param third_id     三方账号id
     */
    private void getHttpUtils(String uid, final String third_source, String third_id, final Activity mActivity) {


        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "user_bind_third");
        params.addBodyParameter("old_third_source", StringUtils.getThirdSource());
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", third_source);
        params.addBodyParameter("third_id", third_id);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result, third_source, mActivity);
                    }
                });
    }

    private void processData(String result, String third_source, final Activity mActivity) {
        UserVerifyInfo userVerifyInfo = GsonTools.changeGsonToBean(result,
                UserVerifyInfo.class);
        if (userVerifyInfo == null) {
            return;
        }
        if (!"1".equals(userVerifyInfo.getCode())) {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
        } else {
            UIUtils.showToastSafe(userVerifyInfo.getMsg());
            //第三方来源 1=QQ 2=微信 3=微博
            //存储到轻量级数据库里
            //刷新界面
            if ("1".equals(third_source)) {
                SharedPreferencesUtils.saveString(mActivity,
                        mActivity.getResources().getString(R.string.shared_third_qq),
                        "qq");
                iv_qq.setImageResource(R.drawable.qq_share_icon);
                tv_qq.setText(getResources().getString(R.string.label_has_binding));
                tv_qq.setTextColor(getResources().getColor(R.color.app_theme_color));
                rl_qqbind.setClickable(false);
            } else if ("2".equals(third_source)) {
                SharedPreferencesUtils.saveString(mActivity,
                        mActivity.getResources().getString(R.string.shared_third_wechat),
                        "wx");
                iv_wx.setImageResource(R.drawable.wechat_share_icon);
                tv_wx.setText(getResources().getString(R.string.label_has_binding));
                tv_wx.setTextColor(getResources().getColor(R.color.app_theme_color));
                rl_wxbind.setClickable(false);
            } else if ("3".equals(third_source)) {
                SharedPreferencesUtils.saveString(mActivity,
                        mActivity.getResources().getString(R.string.shared_third_weibo),
                        "wb");
                iv_sina.setImageResource(R.drawable.weibo_share_icon);
                tv_sina.setText(getResources().getString(R.string.label_has_binding));
                tv_sina.setTextColor(getResources().getColor(R.color.app_theme_color));
                rl_sinabind.setClickable(false);
            }
        }
    }

    public void refresh() {
        if ("-1".equals(StringUtils.getUid())) {
            return;
        }
        getHttpUtils(StringUtils.getUid(), OtherLoginControl.third_source + "", OtherLoginControl.THIRD_ID, this);
    }


}

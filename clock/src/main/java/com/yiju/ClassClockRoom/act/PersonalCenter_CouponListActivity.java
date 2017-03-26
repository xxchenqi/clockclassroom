package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.common.NetWebViewClient;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * ----------------------------------------
 * 注释: 个人中心优惠券
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/7 17:33
 * ----------------------------------------
 */
public class PersonalCenter_CouponListActivity extends BaseActivity implements
        OnClickListener {
    /**
     * 返回按钮
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    /**
     * 帮助按钮
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * 帮助图片设置
     */
    @ViewInject(R.id.head_right_image)
    private ImageView head_right_image;
    /**
     * webview
     */
    @ViewInject(R.id.webview)
    private WebView webview;
    /**
     * 无WIFI显示界面
     */
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    /**
     * 刷新
     */
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;

    private WebSettings setting;

    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_couponlist;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.use_coupon));
        head_right_image.setImageResource(R.drawable.coupon_ask_icon);

        setting = webview.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        NetWebViewClient netWebViewClient = new NetWebViewClient();
        webview.setWebViewClient(netWebViewClient);

        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            webview.setVisibility(View.VISIBLE);
            String uid = "-1".equals(StringUtils.getUid()) ? "" : StringUtils.getUid();
            webview.loadUrl(UrlUtils.SERVER_WEB_COUPONLIST + "uid=" + uid +"&username="+StringUtils.getUsername()+"&password="+StringUtils.getPassword()+"&third_source="+StringUtils.getThirdSource());

        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_168");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                onBackPressed();
                break;
            case R.id.head_right_relative://优惠券说明
                Intent intent = new Intent(UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                intent.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.WEB_Int_Coupon_Page);
                startActivity(intent);
                break;
            case R.id.btn_no_wifi_refresh://刷新
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    webview.setVisibility(View.VISIBLE);
                    String uid = "-1".equals(StringUtils.getUid()) ? "" : StringUtils.getUid();
                    webview.loadUrl(UrlUtils.SERVER_WEB_COUPONLIST + "uid=" + uid);

                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    webview.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_couponlist);
    }
}

package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.common.NetWebViewClient;
import com.yiju.ClassClockRoom.control.map.NavigationUtils;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.fragment.NewIndexFragment;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * 首页跳转到的详情页
 *
 * @author len
 */
public class IndexDetailActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.ly_broken_fail)
    private RelativeLayout ly_broken_fail;
    @ViewInject(R.id.btn_broken_refresh)
    private Button btn_broken_refresh;
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
     * 分享按钮
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * webview
     */
    @ViewInject(R.id.wv_room_detail)
    private WebView wv_room_detail;
    /**
     * 购物车
     */
    @ViewInject(R.id.iv_root_detail_cart)
    private ImageView iv_root_detail_cart;
    /**
     * 预订按钮
     */
    @ViewInject(R.id.btn_room_detail_reserve)
    private Button btn_room_detail_reserve;
    /**
     * 标题布局1(其他webview)
     */
    @ViewInject(R.id.ll_room_detail_title_1)
    private LinearLayout ll_room_detail_title_1;

    /**
     * 标题布局2(类型详情进来的标题)
     */
    @ViewInject(R.id.ll_room_detail_title_2)
    private LinearLayout ll_room_detail_title_2;
    /**
     * 标题布局2(返回图标)
     */
    @ViewInject(R.id.head_gradual_back)
    private ImageView head_gradual_back;

    /**
     * 返回键2
     */
    @ViewInject(R.id.head_gradual_back_relative)
    private RelativeLayout head_gradual_back_relative;
    /**
     * 标题2
     */
    @ViewInject(R.id.head_gradual_title)
    private TextView head_gradual_title;
    @ViewInject(R.id.head_right_image)
    private ImageView head_right_image;

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

    /**
     * webview 设置
     */
    private WebSettings setting;


    private String lng;
    private String lat;

    private String sid;
    private String type_desc;
    private String address;
    private String tags;
    private String url;
    private String typeid;
    private String sname;
    //是否是门店页面
    private boolean isStorePage;

    private String room_start_time;
    private String room_end_time;
    private String room_name;
    private String school_type;
    private String instruction;
    private String confirm_type;
    private String can_schedule;

    @Override
    public int setContentViewId() {
        return R.layout.activity_room_detail;
    }

    @Override
    public void initView() {
        head_right_image.setImageResource(R.drawable.share_btn_icon);
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        iv_root_detail_cart.setOnClickListener(this);
        head_gradual_back_relative.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        btn_broken_refresh.setOnClickListener(this);

        lng = UIUtils.getLng();
        lat = UIUtils.getLat();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initData() {
        setting = wv_room_detail.getSettings();
        //sname: "闸北达人湾中心店",标题显示
        //type_desc: "迷你间课室",webview显示

        // 接收从首页传递过来的学校id
        Intent intent = getIntent();
        String from_order_detail = intent.getStringExtra("from_order_detail");//标记是否从订单详情过来
        type_desc = intent.getStringExtra("type_desc");
        school_type = intent.getStringExtra("school_type");//标记是否是旗舰店
        if (StringUtils.isNotNullString(type_desc)) {
            // 课室类型详情
            instruction = intent.getStringExtra("instruction");
            sid = intent.getStringExtra("sid");
            typeid = intent.getStringExtra("typeid");
            sname = intent.getStringExtra("sname");
            room_start_time = intent.getStringExtra("room_start_time");
            room_end_time = intent.getStringExtra("room_end_time");
            room_name = intent.getStringExtra("room_name");
            confirm_type = intent.getStringExtra("confirm_type");
            can_schedule = intent.getStringExtra("can_schedule");//标记是否可预订
            url = UrlUtils.TYPE_DESC + "typeid=" + typeid
                    + "&pictype=pictype&type=2&sid=" + sid + "&title=" + type_desc;

            //隐藏标题1布局
            ll_room_detail_title_1.setVisibility(View.GONE);
            //显示标题2布局
            ll_room_detail_title_2.setVisibility(View.VISIBLE);
            head_gradual_back.setImageResource(R.drawable.back_circle);
            //设置标题2
            head_gradual_title.setText(sname);
            if ("1".equals(from_order_detail)) {
                btn_room_detail_reserve.setVisibility(View.GONE);
            } else {
                //显示预订按钮
                btn_room_detail_reserve.setVisibility(View.VISIBLE);
                if ("1".equals(can_schedule)) {
                    btn_room_detail_reserve.setText(R.string.reserve_space);
                    btn_room_detail_reserve.setOnClickListener(this);
                } else {
                    btn_room_detail_reserve.setText(R.string.coming_reserve_space);
                    btn_room_detail_reserve.setBackgroundColor(UIUtils.getColor(R.color.gray_cccccc));
                }
            }
            isStorePage = false;
        } else {
            setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//只有专题页做缓存
            // 门店详情
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                sid = bundle.getString("sid");
                type_desc = bundle.getString("name");
                Common_Show_WebPage_Activity.currentTitle = type_desc;
                address = bundle.getString("address");
                tags = bundle.getString("tags");

                head_title.setText(type_desc);
                boolean isHaveBaidu = NavigationUtils.isInstallByread("com.baidu.BaiduMap");
                boolean isHaveGaoDe = NavigationUtils.isInstallByread("com.autonavi.minimap");
                String haveMap;
                if (!isHaveBaidu && !isHaveGaoDe) {
                    haveMap = "0";
                } else {
                    haveMap = "1";
                }
                if ("1".equals(school_type)) {
                    url = UrlUtils.BASE_URL + "/static/school/" + sid + ".html?" + "lng=" + lng + "&lat=" + lat + "&havemap=" + haveMap;
                } else {
                    url = UrlUtils.SERVER_WEB_CLASSDETAIL + "out=out&sid=" + sid
                            + "&lng=" + lng + "&lat=" + lat + "&havemap=" + haveMap;
                }
                isStorePage = true;
            }
        }


        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        NetWebViewClient netWebViewClient = new NetWebViewClient();
        wv_room_detail.setWebViewClient(netWebViewClient);
        wv_room_detail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("404")) {
                    view.stopLoading();
                    ly_broken_fail.setVisibility(View.VISIBLE);
                }
            }
        });
        netWebViewClient.setData(wv_room_detail);
//        wv_room_detail.loadUrl(url);

        String flag = intent.getStringExtra("flag");
        if (flag != null) {
            head_right_relative.setVisibility(View.INVISIBLE);
        }

        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            wv_room_detail.setVisibility(View.VISIBLE);
            wv_room_detail.loadUrl(url);
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            wv_room_detail.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_gradual_back_relative:
                finish();
                break;
            case R.id.btn_room_detail_reserve:
                //预订
                Intent intent = new Intent(this, ReservationActivity.class);
                intent.putExtra("sid", sid);
                intent.putExtra("name", sname);//name: "测试徐汇虹梅商务大厦旗舰店",
                intent.putExtra("type_id", typeid);
                intent.putExtra("room_start_time", room_start_time);
                intent.putExtra("room_end_time", room_end_time);
                intent.putExtra("room_name", room_name);//desc: "大间课室(有窗)",
                intent.putExtra("instruction", instruction);
                intent.putExtra("confirm_type", confirm_type);
                startActivity(intent);
                break;
            case R.id.head_right_relative:
                ShareDialog.getInstance()
                        .setCurrent_Type(ShareDialog.Type_Share_ClassRoom_Info)
                        .setAddress(address).setSchool_name(sname).setSid(sid)
                        .setTags(tags).showDialog();
                break;

            case R.id.iv_root_detail_cart:
                String uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                        "id", null);
                if (null != uid) {
                    Intent intentShopCart = new Intent(UIUtils.getContext(),
                            ShopCartActivity.class);
                    intentShopCart.putExtra("uid", uid);
                    BaseApplication context = (BaseApplication) UIUtils
                            .getContext().getApplicationContext();
                    context.setCheck(true);
                    UIUtils.startActivity(intentShopCart);
                } else {
                    Intent intentLogin = new Intent(UIUtils.getContext(),
                            LoginActivity.class);
                    UIUtils.startActivity(intentLogin);
                }

                break;
            case R.id.btn_no_wifi_refresh:
            case R.id.btn_broken_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    wv_room_detail.setVisibility(View.VISIBLE);
                    wv_room_detail.loadUrl(url);
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    wv_room_detail.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // 购物车是否显示
        if (NewIndexFragment.SHOP_CART_FLAG) {
            iv_root_detail_cart.setVisibility(View.VISIBLE);
        } else {
            iv_root_detail_cart.setVisibility(View.GONE);
        }

    }

    @Override
    public String getPageName() {
        if (!isStorePage) {
            return getString(R.string.title_act_common_show_webpage_for_type_detail);
        } else {
            return getString(R.string.title_act_place_detail_Page);
        }

    }

}
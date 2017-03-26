package com.yiju.ClassClockRoom.act.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.WifiBean;
import com.yiju.ClassClockRoom.common.NetWebViewClient;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

public class Common_Show_WebPage_Activity extends BaseActivity implements
        OnClickListener {
    //标题参数
    public static final String Param_String_Title = "param_string_title";
    //404错误页面
    @ViewInject(R.id.ly_broken_fail)
    private RelativeLayout ly_broken_fail;
    //刷新
    @ViewInject(R.id.btn_broken_refresh)
    private Button btn_broken_refresh;
    //webview
    @ViewInject(R.id.webview)
    private WebView webview;
    //标题布局
    @ViewInject(R.id.ll_webpage)
    private LinearLayout ll_webpage;
    //返回按钮
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //分享按钮
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //无WIFI显示界面
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    //刷新
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
    //预订布局
    @ViewInject(R.id.lr_webpage2)
    private LinearLayout lr_webpage2;
    //分享按钮
    @ViewInject(R.id.head_right_image)
    private ImageView head_right_image;
    //是否登录
    private boolean isLogin;
    //链接
    private String url;
    //page
    private int page_num;
    //老师id
    private String tid;
    //专题id
    private String special_id;
    //标题
    private String title_content = "";
    //当前标题
    public static String currentTitle = "";
    //web设置
    private WebSettings setting;
    public static boolean isFirst = true;

    @Override
    public int setContentViewId() {
        return R.layout.activity_common_show_webpage;
    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        page_num = intent.getIntExtra(
                UIUtils.getString(R.string.get_page_name), 0);
        url = intent.getStringExtra(UIUtils
                .getString(R.string.redirect_open_url));
        title_content = intent.getStringExtra(Param_String_Title);
        tid = intent.getStringExtra(UIUtils.getString(R.string.redirect_tid));
        special_id = intent.getStringExtra("special_id");
    }


    @Override
    public void initView() {
        head_right_image.setBackgroundResource(R.drawable.share_btn_icon);
        //默认隐藏分享按钮
        head_right_relative.setVisibility(View.INVISIBLE);

        isLogin = SharedPreferencesUtils.getBoolean(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_isLogin), false);

        switch (page_num) {
            case WebConstant.WEB_Int_TeachInfo_Page:
                isLogin = SharedPreferencesUtils.getBoolean(UIUtils.getContext(),
                        UIUtils.getString(R.string.shared_isLogin), false);
                if (isLogin) {
                    String uid = "-1".equals(StringUtils.getUid()) ? "" : StringUtils.getUid();
                    url = String.format(UrlUtils.SERVER_WEB_TEACHERDETAIL,
                            StringUtils.formatNullString(tid), uid);
                } else {
                    url = String.format(UrlUtils.SERVER_WEB_TEACHERDETAIL,
                            StringUtils.formatNullString(tid), "");
                }
                break;
            case WebConstant.WEB_Int_UserAgreement_Page:
                title_content = UIUtils.getString(R.string.user_agreement);
                url = UrlUtils.SERVER_WEB_USERAGENT;
                break;
            case WebConstant.WEB_Int_Coupon_Page:
                title_content = UIUtils.getString(R.string.coupon_explain);
                url = UrlUtils.SERVER_WEB_COUPONDESCRIPTION;
                break;
            case WebConstant.WEB_Int_Action_Page:
                if (StringUtils.isNullString(title_content)) {
                    title_content = UIUtils.getString(R.string.action_page);
                }
                break;
            case WebConstant.Order_Log_Page:
                title_content = UIUtils.getString(R.string.order_log);
                break;
            case WebConstant.Classroom_Page:
                title_content = UIUtils.getString(R.string.classroom_price);
                break;
            case WebConstant.WiFi_Page:
                //WIFI页面,请求url的结果
                title_content = UIUtils.getString(R.string.wifi);
                getHttpUtilsWifi();
                break;
            case WebConstant.Organization_authentication_Page:
                url = UrlUtils.H5_ORGANIZATION_AUTHENTICATION;
                break;
            case WebConstant.Draw_up_invoices_Page:
                //如何开具发票
                title_content = UIUtils.getString(R.string.tax_policy);
                url = UrlUtils.H5_DRAW_UP_INVOICES;
                break;
            case WebConstant.Added_value_tax_Page:
                title_content = UIUtils.getString(R.string.added_value_tax);
                url = UrlUtils.H5_ADDED_VALUE_TAX;
                head_right_image.setBackgroundResource(R.drawable.save_btn_icon);
                head_right_relative.setVisibility(View.VISIBLE);
                break;
            case WebConstant.Classroom_value_tax_Page:
                title_content = UIUtils.getString(R.string.classroom_value_tax);
                url = UrlUtils.H5_CLASSROOM_VALUE_TAX;
                head_right_relative.setVisibility(View.INVISIBLE);
                break;
            case WebConstant.WEB_value_special_teacher_Page://老师专题
                title_content = UIUtils.getString(R.string.course_zhuan);
                head_right_relative.setVisibility(View.VISIBLE);
                break;
            case WebConstant.WEB_value_special_course_Page://课程专题
                title_content = UIUtils.getString(R.string.course_zhuan);
                head_right_relative.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        head_title.setText(title_content);

        webview = (WebView) findViewById(R.id.webview);
        setting = webview.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("GBK");
        if (page_num == WebConstant.WEB_value_special_teacher_Page||
                page_num == WebConstant.WEB_value_special_course_Page) {
            setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//只有专题页做缓存
        } else {
            setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        NetWebViewClient netWebViewClient = new NetWebViewClient();
        netWebViewClient.setHead_title(head_title, title_content);
        netWebViewClient.setData(webview);
        webview.setWebViewClient(netWebViewClient);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("404")) {
                    view.stopLoading();
                    ly_broken_fail.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void initData() {
        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            webview.setVisibility(View.VISIBLE);
            if (page_num != WebConstant.WiFi_Page) {
                //不是认证WIFI才加载,认证WIFI页面要去请求完结果在下载
                webview.loadUrl(url);
            }
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        btn_broken_refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            case R.id.head_right_relative:
                if (url.contains(WebConstant.varinvoice)) {//发票图片本地保存
                    StatFs sf = new StatFs("/mnt/sdcard");
                    long blockSize = sf.getBlockSize();
                    long availableBlocks = sf.getAvailableBlocks();
                    long l = blockSize * availableBlocks / 1024 / 1024;//mb
                    if (l < 10) {
                        UIUtils.showToastSafe(getString(R.string.toast_run_out_of_memory));
                        return;
                    }
                    head_right_relative.setClickable(false);
                    ImageView iv = new ImageView(this);
                    BitmapUtils bitmap = new BitmapUtils(this);
                    bitmap.display(iv, UrlUtils.H5_ADDED_VALUE_TAX_IMG, new BitmapLoadCallBack<ImageView>() {
                        @Override
                        public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                            String imageName = System.currentTimeMillis() + ".jpg";
                            MediaStore.Images.Media.insertImage(
                                    Common_Show_WebPage_Activity.this.getApplicationContext().getContentResolver(),
                                    bitmap,
                                    imageName,
                                    "开具发票");
                            head_right_relative.setClickable(true);
                            Toast.makeText(Common_Show_WebPage_Activity.this, "已保存到系统相册", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                            head_right_relative.setClickable(true);
                            Toast.makeText(Common_Show_WebPage_Activity.this, "保存失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (page_num == WebConstant.WEB_value_special_teacher_Page ||
                        page_num == WebConstant.WEB_value_special_course_Page) {
                    //专题分享
                    ShareDialog.getInstance()
                            .setCurrent_Type(ShareDialog.Type_Share_Special)
                            .setSpecial_id(special_id)
                            .showDialog();
                }
                break;
            case R.id.btn_no_wifi_refresh:
            case R.id.btn_broken_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    webview.setVisibility(View.VISIBLE);
                    webview.loadUrl(url);
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
        this.setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void getHttpUtilsWifi() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "switch_wifi");


        httpUtils.send(HttpRequest.HttpMethod.POST, "http://check.51shizhong.com/common_api.php?", params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataWifi(arg0.result);
                    }
                });
    }

    private void processDataWifi(String result) {
        WifiBean wifiBean = GsonTools.changeGsonToBean(result,
                WifiBean.class);
        if (wifiBean == null) {
            return;
        }

        if ("1".equals(wifiBean.getCode())) {
            WifiBean.DataEntity data = wifiBean.getData();
            String id = data.getId();
            if ("1".equals(id)) {
//                手机号验证码认证方式：id=1,
// url=http://api.51shizhong.com/h5/wifi_check.html?tel=
// {手机号码，能获取到值：号码，获取不到值：111}&nw={判断是否在网关下面，是：wifi,否：no}
                String mobile = SharedPreferencesUtils.getString(this,
                        getResources().getString(R.string.shared_mobile), "111");
                //判断wifi是否可用
                if (NetWorkUtils.isWiFiActive(this)) {
                    url = UrlUtils.WIFI + mobile + "&nw=wifi";
                } else {
                    url = UrlUtils.WIFI + mobile + "&nw=no";
                }

            } else if ("2".equals(id)) {
//                免认证方式：id=2,url=http://check.51shizhong.com/wx_check.html
                url = data.getUrl();

            }

            webview.loadUrl(url);


        } else {
            UIUtils.showToastSafe(wifiBean.getMsg());
        }

    }


    @Override
    public String getPageName() {
        switch (page_num) {

            case WebConstant.WEB_Int_TeachInfo_Page:
                return getString(R.string.title_act_common_show_webpage_for_teacher);

            case WebConstant.WEB_Int_UserAgreement_Page:
                return getString(R.string.title_act_common_show_webpage_for_register);

            case WebConstant.WEB_Int_Coupon_Page:// 优惠券使用说明
                return getString(R.string.title_act_common_show_webpage_for_coupon);

            case WebConstant.WEB_Int_Action_Page:
                return getString(R.string.title_act_common_show_webpage_for_ad);

            case WebConstant.WEB_Int_Map_Page:
                return getString(R.string.title_act_common_show_webpage_for_around_map);

            case WebConstant.Order_Log_Page:
                return "";

            case WebConstant.Classroom_Page:
                return "";

            case WebConstant.WiFi_Page:
                return getString(R.string.title_act_common_show_webpage_for_wifi);

            case WebConstant.WEB_Int_Class_Page:
                return "";

            case WebConstant.Comment_Page:
                // 评论页
                return "";

            case WebConstant.Picclass_Page:// 课室_类型详情
                return getString(R.string.title_act_common_show_webpage_for_type_detail);

            case WebConstant.Picdes_Page:// 照片列表页
                return getString(R.string.title_act_common_show_webpage_for_imagelist);

            case WebConstant.Around_Page:// 周边环境页
                return getString(R.string.title_act_common_show_webpage_for_around);

            case WebConstant.Attention_Page:// 我的关注
                return getString(R.string.title_act_common_show_webpage_for_attention);

            case WebConstant.Organization_authentication_Page://机构认证
                return getString(R.string.title_act_org_auth);
            case WebConstant.Draw_up_invoices_Page://如何开具发票页
                return getString(R.string.title_act_Draw_up_invoices_Page);
            case WebConstant.Added_value_tax_Page://增值税专用发票信息采集表页
                return getString(R.string.title_act_Added_value_tax_Page);
            case WebConstant.WEB_value_special_teacher_Page://老师专题
                return getString(R.string.title_act_special_Page);
            case WebConstant.WEB_value_special_course_Page://课程专题
                return getString(R.string.title_act_special_Page);
            default:
                return title_content;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
            UIUtils.showToastSafe(getString(R.string.toast_permission_call_phone));
        }
    }
}

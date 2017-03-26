package com.yiju.ClassClockRoom.common;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.CourseDetailActivity;
import com.yiju.ClassClockRoom.act.CourseMoreActivity;
import com.yiju.ClassClockRoom.act.LoginActivity;
import com.yiju.ClassClockRoom.act.MainActivity;
import com.yiju.ClassClockRoom.act.StoreDetailActivity;
import com.yiju.ClassClockRoom.act.TeacherDetailActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.control.map.NavigationUtils;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;
import com.yiju.ClassClockRoom.widget.windows.NavigationWindow;

import java.util.HashMap;
import java.util.Map;

public class NetWebViewClient extends WebViewClient {
    private TextView head_title;
    private String defaultTitle;
    private WebView webview;
    private NavigationUtils navigationUtils = null;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return processUrl(view, url) || super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
//        handler.proceed();
        super.onReceivedSslError(view, handler, error);
        UIUtils.showToastSafe(UIUtils.getString(R.string.toast_show_webview_error));
        ProgressDialog.getInstance().dismiss();
    }

    private boolean processUrl(WebView view, final String url) {
        if (url.startsWith("tel:")) {
            String tel = url.split("tel:")[1];
            // 弹出电话呼叫窗口
            CustomDialog customDialog = new CustomDialog(
                    BaseApplication.getmForegroundActivity(),
                    UIUtils.getString(R.string.confirm),
                    UIUtils.getString(R.string.label_cancel),
                    tel);
            customDialog.setOnClickListener(new IOnClickListener() {
                @Override
                public void oncClick(boolean isOk) {
                    if (isOk) {
                        if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                            // 跳转系统电话
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                    .parse(url));
                            if (ActivityCompat.checkSelfPermission(
                                    UIUtils.getContext(),
                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                return;
                            }
                            BaseApplication.getmForegroundActivity().startActivity(intent);
                        } else {
                            PermissionsChecker.requestPermissions(
                                    BaseApplication.getmForegroundActivity(),
                                    PermissionsChecker.CALL_PHONE_PERMISSIONS
                            );
                        }
                    }
                }
            });
            return true;
        }

        Uri parseUri = Uri.parse(url);
        if (parseUri == null) {
            return false;
        }
        // 设置web头部标题
        String title = parseUri
                .getQueryParameter(WebConstant.bannertitle_Param);

        if (url.contains(WebConstant.reStartLogin_Param)) {
            // 登陆
            view.stopLoading();
            Intent intent = new Intent();
            intent.putExtra(UIUtils.getString(R.string.login_pre_page),
                    UIUtils.getString(R.string.from_page_find));
            intent.setClass(UIUtils.getContext(), LoginActivity.class);
            BaseApplication.getmForegroundActivity().startActivityForResult(
                    intent, RequestCodeConstant.Web_Skip_Login);
            return true;
        } else if (url.contains(WebConstant.teacherDetail_Param)) {
            // 跳转老师详情
            view.stopLoading();
            Intent intent = new Intent();
            intent.putExtra(UIUtils.getString(R.string.redirect_tid),
                    parseUri.getQueryParameter(WebConstant.teacher_Id));
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.WEB_Int_TeachInfo_Page);
            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);

            title = parseUri.getQueryParameter(WebConstant.title_Param);
            if (title != null) {
                intent.putExtra(
                        Common_Show_WebPage_Activity.Param_String_Title, title);
            } else {
                intent.putExtra(
                        Common_Show_WebPage_Activity.Param_String_Title, "老师详情");
            }

            BaseApplication.getmForegroundActivity().startActivityForResult(
                    intent, RequestCodeConstant.Web_Finish_Refresh);

            return true;
        } else if (url.contains(WebConstant.action_Param)) {
            // 活动和良师
            if (title == null) {
                parseUri = Uri.parse(url.replace("#", ""));
                title = parseUri
                        .getQueryParameter(WebConstant.bannertitle_Param);
            }

            view.stopLoading();
            Intent intent = new Intent();
            intent.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
            intent.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                    title);
            intent.putExtra(UIUtils.getString(R.string.redirect_owner),
                    parseUri.getQueryParameter(WebConstant.owner_Id));
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.WEB_Int_Action_Page);

            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);

        } else if (url.contains(WebConstant.trapmap)) {
            //地图页面
            view.stopLoading();
            title = parseUri.getQueryParameter(WebConstant.title_Param);
            Intent intent = new Intent();
            // url
            intent.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
            // title
            intent.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                    title);
            // page
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.WEB_Int_Map_Page);
            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);

        } else if (url.contains(WebConstant.classroomdetail)) {
            if (url.contains("singlestore=singlestore")) {//跳转到门店课室类型列表
                view.stopLoading();
                String sid = Uri.parse(url).getQueryParameter("sid");
                String store_name = Uri.parse(url).getQueryParameter("title");
                String school_type = Uri.parse(url).getQueryParameter("school_type");

                Intent intent = new Intent(UIUtils.getContext(), StoreDetailActivity.class);
                intent.putExtra(ExtraControl.EXTRA_STORE_ID, sid);
//                intent.putExtra("school_type", school_type);
//                intent.putExtra("store_name", store_name);
                UIUtils.startActivity(intent);
                return false;
            }
            // 课室详情
            view.stopLoading();
            title = parseUri.getQueryParameter(WebConstant.title_Param);
            String address = parseUri.getQueryParameter("address");
            String tags = parseUri.getQueryParameter("tags");
            // 进预订时的title
            Common_Show_WebPage_Activity.currentTitle = title;
            Intent intent = new Intent();
            // url
            intent.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
            // title
            intent.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                    title);
            // page
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.WEB_Int_Class_Page);
            intent.putExtra("address", address);
            intent.putExtra("tags", tags);
            // sid
            String sid = parseUri.getQueryParameter("sid");
            intent.putExtra("sid", sid);
            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);

        } else if (url.contains("hotteacher=hotteacher")) {
            //??
            if (Common_Show_WebPage_Activity.isFirst) {
                Common_Show_WebPage_Activity.isFirst = false;
            } else {
                view.stopLoading();
                Common_Show_WebPage_Activity.isFirst = true;
                title = parseUri.getQueryParameter(WebConstant.title_Param);
                Intent intent = new Intent();
                // url
                intent.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        url);
                // title
                intent.putExtra(
                        Common_Show_WebPage_Activity.Param_String_Title, title);
                // page
                intent.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.Comment_Page);
                intent.setClass(UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                UIUtils.startActivity(intent);
            }

        } else if (url.contains("picclass")) {
            // 课室类型详情
            view.stopLoading();
            title = parseUri.getQueryParameter(WebConstant.title_Param);
            Intent intent = new Intent();
            // url
            intent.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
            intent.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                    title);
            // page
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.Picclass_Page);
            String sid = parseUri.getQueryParameter("sid");
            intent.putExtra("sid", sid);
            String typeid = parseUri.getQueryParameter("typeid");
            intent.putExtra("typeid", typeid);
            String type = parseUri.getQueryParameter("type");
            intent.putExtra("type", type);
            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);
        } else if (url.contains(WebConstant.teacherlist)) {
            // 没有任何关注后点击返回到良师页面
            Intent intent = new Intent(
                    BaseApplication.getmForegroundActivity(),
                    MainActivity.class);
            intent.putExtra("find", "find");
            BaseApplication.getmForegroundActivity().startActivity(intent);
            // BaseApplication.getmForegroundActivity().setResult(4);
            // BaseApplication.getmForegroundActivity().finish();
        } else if (url.contains(WebConstant.checkloginpage)) {
            // 没有任何关注后点击返回到良师页面
            Intent intent = new Intent(BaseApplication.getmForegroundActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            BaseApplication.getmForegroundActivity().startActivity(intent);
            BaseApplication.getmForegroundActivity().finish();
            BaseApplication.getmForegroundActivity().overridePendingTransition(R.anim.anim_yv, R.anim.anim_act_push);
        } else if (url.contains(WebConstant.picdes)) {
            // 图集详情
            view.stopLoading();
            Intent intent = new Intent();
            // url
            intent.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
            // title
            intent.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                    Common_Show_WebPage_Activity.currentTitle + "场地照片");
            // page
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.Picdes_Page);

            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);

        } else if (url.contains(WebConstant.around)) {
            // 周边环境
            view.stopLoading();
            Intent intent = new Intent();
            // url
            intent.putExtra(UIUtils.getString(R.string.redirect_open_url), url);
            // title
            intent.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                    "周边环境");
            // page
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.Around_Page);
            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);
        } else if (url.contains(WebConstant.varinvoice)) {
            view.stopLoading();
            Intent intent = new Intent();
            // page
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.Added_value_tax_Page);
            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);
        } else if (url.contains(WebConstant.room_price)) {
            view.stopLoading();
            Intent intent = new Intent();
            // page
            intent.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.Classroom_value_tax_Page);
            intent.setClass(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            UIUtils.startActivity(intent);
        }
        // 防止要改回调用系统的浏览器
        // else if (url.contains("out=out&hotteacher=hotteacher")) {
        // Intent intent = new Intent();
        // intent.setAction("android.intent.action.VIEW");
        // Uri content_url = Uri.parse(url);
        // intent.setData(content_url);
        // UIUtils.startActivity(intent);
        // return true;
        // }
        else {
            if (StringUtils.isNotNullString(title)
                    || StringUtils.isNotNullString(defaultTitle)) {
                if (head_title != null) {
                    if (StringUtils.isNotNullString(title)) {
                        head_title.setText(title);
                    } else {
                        head_title.setText(defaultTitle);
                    }
                }
            }
        }
        return false;
    }

    public void setHead_title(TextView head_title, String defualtTitle) {
        this.head_title = head_title;
        this.defaultTitle = defualtTitle;
    }

    public void setData(WebView webview) {
        this.webview = webview;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        ProgressDialog.getInstance().show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        ProgressDialog.getInstance().dismiss();
        if (url.contains("#clickbtn=clickbtn")) {
            String to = Uri.parse(url).getQueryParameter("to");
            String to_g = Uri.parse(url).getQueryParameter("to_g");
            String address = Uri.parse(url).getQueryParameter("address");
            if (StringUtils.isNullString(to) || StringUtils.isNullString(to_g)) {
                return;
            }
            String[] ss_to = to.split(",");
            String lng = ss_to[0];
            String lat = ss_to[1];
            String[] ss_to_g = to_g.split(",");
            String lng_g = ss_to_g[0];
            String lat_g = ss_to_g[1];
            //导航点击
            if (PermissionsChecker.checkPermission(PermissionsChecker.LOCATION_PERMISSIONS)) {
                //缺少权限
                showDialog();
            } else {
                boolean isHaveBaidu;
                boolean isHaveGaode;
                if (navigationUtils == null) {
                    navigationUtils = new NavigationUtils(
                            lat,
                            lng,
                            lat_g,
                            lng_g,
                            address
                    );
                } else {
                    navigationUtils.setEndLat(lat);
                    navigationUtils.setEndLng(lng);
                    navigationUtils.setEndLat_g(lat_g);
                    navigationUtils.setEndLng_g(lng_g);
                    navigationUtils.setEndAddress(address);
                }

                isHaveBaidu = NavigationUtils.isInstallByread("com.baidu.BaiduMap");
                isHaveGaode = NavigationUtils.isInstallByread("com.autonavi.minimap");
                if (isHaveBaidu || isHaveGaode) {
                    //高德跟百度都存在
                    NavigationWindow navigationWindow = new NavigationWindow(
                            isHaveBaidu,
                            isHaveGaode,
                            new ListItemClickHelp() {
                                @Override
                                public void onClickItem(int position) {
                                    navigationUtils.navigationHandle(position);
                                }
                            });
                    navigationWindow.showAtLocation(webview, Gravity.BOTTOM, 0, 0);
                } else {
                    //都不存在
                    UIUtils.showToastSafe("请先安装百度或高德地图客户端！");
                }
            }
        } else if (url.contains("#subject_t=")) {//老师信息跳转
            skipTeacherInfo(url, "subject_t");
        } else if (url.contains("#subject_c=")) {//课程信息跳转
            skipCourseInfo(url, "subject_c");
        } else if (url.contains("#tid=")) {//老师信息跳转
            skipTeacherInfo(url, "tid");
        } else if (url.contains("#cid=")) {//课程信息跳转
            skipCourseInfo(url, "cid");
        } else if (url.contains("#morecourse_sid=")) {//更多课程跳转
            skipCourseMore(url, "morecourse_sid");
        }
    }

    /**
     * 跳转老师详情
     */
    public void skipTeacherInfo(String url, String value) {
        String anchorId = getAnchorId(url, value);
        if (anchorId != null) {
            Intent intent = new Intent(UIUtils.getContext(), TeacherDetailActivity.class);
            intent.putExtra("id", anchorId);
            UIUtils.startActivity(intent);
        }
    }

    /**
     * 跳转课程详情
     */
    public void skipCourseInfo(String url, String value) {
        String anchorId = getAnchorId(url, value);
        if (anchorId != null) {
            Intent intent = new Intent(UIUtils.getContext(), CourseDetailActivity.class);
            intent.putExtra("COURSE_ID", anchorId);
            UIUtils.startActivity(intent);
        }
    }

    /**
     * 跳转更多课程
     */
    public void skipCourseMore(String url, String value) {
        String anchorId = getAnchorId(url, value);
        if (anchorId != null) {
            Intent intent = new Intent(UIUtils.getContext(), CourseMoreActivity.class);
            intent.putExtra("SCHOOL_ID", anchorId);
            UIUtils.startActivity(intent);
        }
    }


    /**
     * 获取锚点字符串
     *
     * @param url 链接
     */
    public String getAnchorId(String url, String value) {
        String anchor = Uri.parse(url).getFragment();
        Map<String, String> map = new HashMap<>();
        if (anchor != null) {
            String[] split = anchor.split("&");
            if (split.length != 0) {
                for (int i = 0; i < split.length; i++) {
                    String[] split2 = split[i].split("=");
                    if (split2.length != 0) {
                        map.put(split2[0], split2[1]);
                    }
                }
                return map.get(value);
            }
        }
        return null;
    }

    private void showDialog() {
        CustomDialog customDialog = new CustomDialog(
                BaseApplication.getmForegroundActivity(),
                UIUtils.getString(R.string.location_open),
                UIUtils.getString(R.string.txt_cancel),
                UIUtils.getString(R.string.location_message));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    BaseApplication.mForegroundActivity.startActivity(intent);
                }
            }
        });
    }

}

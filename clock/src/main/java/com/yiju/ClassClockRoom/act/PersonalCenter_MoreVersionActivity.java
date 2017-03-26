package com.yiju.ClassClockRoom.act;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.ShareBean;
import com.yiju.ClassClockRoom.bean.VersionBean;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.VersionUpdateDialog;

import java.io.File;

/**
 * ----------------------------------------
 * 注释: 关于时钟教室
 * <p>
 * 作者: cq
 * <p>
 * 时间: 2016/6/7 17:39
 * ----------------------------------------
 */
public class PersonalCenter_MoreVersionActivity extends BaseActivity implements
        OnClickListener, VersionUpdateDialog.DownloadApkListener {

    //退出按钮
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //意见反馈
    @ViewInject(R.id.rl_app_feedback)
    private TextView rl_app_feedback;
    //版本检测
    @ViewInject(R.id.rl_app_version_check)
    private TextView rl_app_version_check;
    //版本号
    private String id;
    //对话框
    private ProgressDialog progressDialog;
    //下载app的handler
    private HttpHandler<File> download;
    //版本文案
    @ViewInject(R.id.tv_version_code)
    private TextView tv_version_code;

    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_more_versions;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.about_clock_classroom));

        // 创建ProgressDialog对象
        progressDialog = new ProgressDialog(this);
        // 设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        progressDialog.setTitle(R.string.dialog_title_downloading);
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);


        head_back_relative.setOnClickListener(this);
        rl_app_feedback.setOnClickListener(this);
        rl_app_version_check.setOnClickListener(this);
        id = getVersion();
        tv_version_code.setText(String.format(UIUtils.getString(R.string.clock_id), id));
    }

    /**
     * 获取版本号
     */
    private String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                this.finish();
                break;
            case R.id.rl_app_feedback:// 用户意见反馈窗口
                ShareBean bean = new ShareBean();
                bean.setTitle("");
                bean.setContent(getString(R.string.content_feed_back));
                bean.setUrl("http://wap.51shizhong.com/");
                bean.setPicicon(BitmapFactory.decodeResource(getResources(), R.drawable.weibo_link));
                ShareDialog.getInstance().shareSina(bean);
                break;
            case R.id.rl_app_version_check:
                // 检测APP版本
                getHttpUtils();
                break;
            default:
                break;
        }
    }

    /**
     * 获取新版本
     */
    private void getHttpUtils() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_newest_version");
        params.addBodyParameter("system_id", "2");
        params.addBodyParameter("version", id);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                });
    }

    private void processData(String result) {
        final VersionBean versionBean = GsonTools.changeGsonToBean(result,
                VersionBean.class);
        if (versionBean == null) {
            return;
        }
        if ("1".equals(versionBean.getCode())) {
            int update = versionBean.getUpdate();
            switch (update) {
                case -1:
                    // 没有最新版本信息
                    UIUtils.showToastSafe(getString(R.string.toast_no_new_version));
                    break;
                case 0:
                    // 静默升级
                    UIUtils.showToastSafe(getString(R.string.toast_no_new_version));
                    break;
                case 1:
                    // 强制升级
                    new VersionUpdateDialog(this, versionBean, this).createView();
                    break;
                case 2:
                    // 提示升级
                    new VersionUpdateDialog(this, versionBean, this).createView();
                    break;
                default:
                    break;
            }

        } else {
            UIUtils.showToastSafe(versionBean.getMsg());
        }

    }

    /**
     * 下载apk
     * @param url 地址
     */
    private void downloadApk(String url) {
        String target = Environment.getExternalStorageDirectory()
                + "/clock/CameraCache/clock.apk";

        File file = new File(target);
        if (file.exists()) {
            file.delete();
        }

        HttpUtils http = new HttpUtils();
        if (url.endsWith(".apk")) {

            download = http.download(url, target, true,
                    new RequestCallBack<File>() {

                        @Override
                        public void onStart() {
                            // 让ProgressDialog显示
                            progressDialog.show();
                        }

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {

                            if (progressDialog.isShowing()) {
                                progressDialog.setMax((int) total);
                                progressDialog.setProgress((int) current);

                                String current_result;
                                if (current > 1048576) {
                                    current_result = String.format("%.2f", current / 1024.0 / 1024.0) + "MB";
                                } else {
                                    current_result = String.format("%.2f", current / 1024.0) + "KB";
                                }
                                String total_result = String.format("%.2f", total / 1024.0 / 1024.0) + "MB";
                                progressDialog.setProgressNumberFormat(current_result + "/" + total_result);
                            } else {
                                download.cancel();
                            }
                        }

                        @Override
                        public void onSuccess(ResponseInfo<File> arg0) {
                            // progressDialog.dismiss();
                            File result = arg0.result;
                            if (!result.exists()) {
                                return;
                            }
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(
                                    Uri.parse("file://" + result.toString()),
                                    "application/vnd.android.package-archive");
                            startActivity(i);

                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            if (progressDialog != null
                                    && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(
                                    PersonalCenter_MoreVersionActivity.this,
                                    arg0.getExceptionCode() + ":" + arg1,
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_more_version);
    }

    @Override
    public void download(String url) {
        downloadApk(url);
    }
}

package com.yiju.ClassClockRoom.act;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseFragmentActivity;
import com.yiju.ClassClockRoom.bean.VersionBean;
import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.fragment.ExperienceClassFragment;
import com.yiju.ClassClockRoom.fragment.IndexFragment;
import com.yiju.ClassClockRoom.fragment.ThemeTemplateFragment;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.VersionUpdateDialog;

import java.io.File;
import java.util.Locale;


public class MainActivity extends BaseFragmentActivity implements
        OnCheckedChangeListener, VersionUpdateDialog.DownloadApkListener {
    //参数
    public static final String Param_Start_Fragment = "param_start_fragment";
    //初始tag
    private int start_fragment = FragmentFactory.TAB_THEME;
    //当前选中的fragment
    private BaseFragment currentFragment;
    //选择按钮
    private RadioGroup rbs;
    //下载
    private HttpHandler<File> download;
    //对话框
    private ProgressDialog progressDialog;
    //主题
    private BaseFragment fragment_theme;
    //订课室
    private BaseFragment fragment_index;
    //体验课
    private BaseFragment fragment_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initIntent();
        rbs = (RadioGroup) findViewById(R.id.rbs_fragment);
        initFragments();
        initProgressDialog();
        getHttpUtils();
//        switch (BaseApplication.FORMAL_ENVIRONMENT) {
//            case 1:
//            case 2:
//                PgyUpdateManager.register(this);
//                break;
//            case 3:
//                break;
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        switch (BaseApplication.FORMAL_ENVIRONMENT) {
//            case 1:
//            case 2:
//                PgyUpdateManager.unregister();
//                break;
//            case 3:
//                break;
//        }
    }

    private void initIntent() {
        start_fragment = getIntent().getIntExtra(Param_Start_Fragment,
                FragmentFactory.TAB_THEME);
        if (start_fragment >= 3) {
            //防止崩溃
            start_fragment = 1;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        start_fragment = intent.getIntExtra(Param_Start_Fragment,
                FragmentFactory.TAB_THEME);
        if (start_fragment >= 3) {
            //防止崩溃
            start_fragment = 1;
        }
        ((RadioButton) rbs.getChildAt(start_fragment)).setChecked(true);
    }

    // 初始化主页fragment界面
    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        ((RadioButton) rbs.getChildAt(start_fragment)).setChecked(true);
        rbs.setOnCheckedChangeListener(this);
        switch (start_fragment) {
            case FragmentFactory.TAB_INDEX:
                fragment_index = new IndexFragment();
                currentFragment = fragment_index;
                break;
            case FragmentFactory.TAB_THEME:
                fragment_theme = new ThemeTemplateFragment();
                currentFragment = fragment_theme;
                break;
            case FragmentFactory.TAB_EXPERIENCE:
                fragment_class = new ExperienceClassFragment();
                currentFragment = fragment_class;
                break;
        }
        beginTransaction.add(R.id.fl_container, currentFragment);
        beginTransaction.show(currentFragment);
        beginTransaction.commit();
    }

    //初始化对话框
    private void initProgressDialog() {
        // 创建ProgressDialog对象
        progressDialog = new ProgressDialog(this);
        // 设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        progressDialog.setTitle(getString(R.string.dialog_title_downloading));
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_index:
                changeFragment(FragmentFactory.TAB_INDEX);
                break;
            case R.id.rb_course:
                changeFragment(FragmentFactory.TAB_THEME);
                break;
            case R.id.rb_find:
                changeFragment(FragmentFactory.TAB_EXPERIENCE);
                break;
        }
    }

    private void changeFragment(int fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        switch (fragment) {
            case FragmentFactory.TAB_INDEX:
                if (fragment_index == null) {
                    fragment_index = new IndexFragment();
                    beginTransaction.add(R.id.fl_container, fragment_index);
                }
                beginTransaction.hide(currentFragment);
                currentFragment = fragment_index;
                beginTransaction.show(currentFragment);
                break;
            case FragmentFactory.TAB_THEME:
                if (fragment_theme == null) {
                    fragment_theme = new ThemeTemplateFragment();
                    beginTransaction.add(R.id.fl_container, fragment_theme);
                }
                beginTransaction.hide(currentFragment);
                currentFragment = fragment_theme;
                beginTransaction.show(currentFragment);
                break;
            case FragmentFactory.TAB_EXPERIENCE:
                if (fragment_class == null) {
                    fragment_class = new ExperienceClassFragment();
                    beginTransaction.add(R.id.fl_container, fragment_class);
                }
                beginTransaction.hide(currentFragment);
                currentFragment = fragment_class;
                beginTransaction.show(currentFragment);
                break;
        }
        beginTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 返回首页的选择
        if (resultCode == 2) {
            ((RadioButton) rbs.getChildAt(0)).setChecked(true);
        } else if (resultCode == 4) {
            ((RadioButton) rbs.getChildAt(2)).setChecked(true);
        }
        if (requestCode == RequestCodeConstant.Web_Skip_Login && resultCode == -1) {//主题页登录回调
            ((ThemeTemplateFragment) fragment_theme).refreshWebView();
        } else if (resultCode == 1000) {//登录状态变化
            boolean flag = data.getBooleanExtra(ExtraControl.EXTRA_IS_LOGIN, false);
            if (flag) {
                ((ThemeTemplateFragment) fragment_theme).refreshWebView();
            }
        }
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_newest_version");
        params.addBodyParameter("system_id", "2");
        params.addBodyParameter("version", UIUtils.getVersion());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
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
                    // UIUtils.showToastSafe("没有最新版本");
                    break;
                case 0:
                    // 静默升级
                    // UIUtils.showToastSafe("没有最新版本");
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
        }
    }

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
                                    current_result = String.format(Locale.CHINA, "%.2f", current / 1024.0 / 1024.0) + "MB";
                                } else {
                                    current_result = String.format(Locale.CHINA, "%.2f", current / 1024.0) + "KB";
                                }
                                String total_result = String.format(Locale.CHINA, "%.2f", total / 1024.0 / 1024.0) + "MB";

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
                            //下载完成后关闭
                            finish();

                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            if (progressDialog != null
                                    && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(MainActivity.this,
                                    arg0.getExceptionCode() + ":" + arg1,
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    @Override
    public void download(String url) {
        downloadApk(url);
    }

    @Override
    protected void onDestroy() {
        //关掉首页时要把所有页面全部销毁
        super.onDestroy();
        ActivityControlManager.getInstance().finishAllActivity();
    }
}

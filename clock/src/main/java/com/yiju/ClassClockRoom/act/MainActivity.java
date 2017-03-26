package com.yiju.ClassClockRoom.act;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseFragmentActivity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.VersionBean;
import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.fragment.IndexFragment;
import com.yiju.ClassClockRoom.fragment.PersonalCenterFragment;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.VersionUpdateDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements
        OnCheckedChangeListener, VersionUpdateDialog.DownloadApkListener {
    public static final String Param_Start_Fragment = "param_start_fragment";

    private int start_fragment = FragmentFactory.TAB_INDEX;

    private List<BaseFragment> fragments;

    private BaseFragment currentFragment;

    private RadioGroup rbs;

    private HttpHandler<File> download;

    private ProgressDialog progressDialog;
    private boolean isFragmentStatok;

    public RadioGroup getRadioGroup() {
        return rbs;
    }

    private Order2 info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initIntent();
        rbs = (RadioGroup) findViewById(R.id.rbs_fragment);
        initFragments();
        rbs.setOnCheckedChangeListener(this);
        ((RadioButton) rbs.getChildAt(start_fragment)).setChecked(true);
        initProgressDialog();
        getHttpUtils();
    }

    private void initIntent() {
        start_fragment = getIntent().getIntExtra(Param_Start_Fragment,
                FragmentFactory.TAB_INDEX);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra("backhome") != null) {
            info = (Order2) intent.getSerializableExtra("info");
            Bundle bundle = new Bundle();
            if (info != null) {
                bundle.putSerializable("info", info);//从OrderEditDetailActivity传入
                bundle.putBoolean("isBackHome", true);
                IndexFragment indexFragment = new IndexFragment();
                changeFragment(indexFragment, bundle);
            }
            start_fragment = FragmentFactory.TAB_INDEX;
        } else if (intent.getStringExtra("read") != null) {
            start_fragment = FragmentFactory.TAB_VIDEO;
        } else if (intent.getStringExtra("find") != null) {
            start_fragment = FragmentFactory.TAB_TEACHER;
        } else if (intent.getStringExtra("person") != null) {
            start_fragment = FragmentFactory.TAB_MY;
        } else if (intent.getStringExtra("backhome_jump_shopcart") != null) {
            if (!"-1".equals(StringUtils.getUid())) {
                Intent intentShopCart = new Intent(UIUtils.getContext(),
                        ShopCartActivity.class);
                intentShopCart.putExtra("uid", StringUtils.getUid());
                intentShopCart.putExtra("newIndex", 0);
                BaseApplication context = (BaseApplication) UIUtils
                        .getContext().getApplicationContext();
                context.setCheck(true);
                UIUtils.startActivity(intentShopCart);
            } else {
                Intent intentLogin = new Intent(UIUtils.getContext(),
                        LoginActivity.class);
                UIUtils.startActivity(intentLogin);
            }
        }

        ((RadioButton) rbs.getChildAt(start_fragment)).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 初始化主页fragment界面
    private void initFragments() {
        fragments = new ArrayList<>();
        FragmentFactory factory = new FragmentFactory();
        fragments.add(factory.createFragment(FragmentFactory.TAB_INDEX));
        fragments.add(factory.createFragment(FragmentFactory.TAB_COURSE));
        fragments.add(factory.createFragment(FragmentFactory.TAB_TEACHER));
        fragments.add(factory.createFragment(FragmentFactory.TAB_MY));

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_index:
                changeFragment(FragmentFactory.TAB_INDEX);
                break;
            case R.id.rb_course:
                changeFragment(FragmentFactory.TAB_COURSE);
                break;
            case R.id.rb_find:
                changeFragment(FragmentFactory.TAB_TEACHER);
                break;
            case R.id.rb_my:
                changeFragment(FragmentFactory.TAB_MY);
                break;
        }
    }

    private void changeFragment(int i) {
        isFragmentStatok = false;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        BaseFragment fragment = fragments.get(i);
        currentFragment = fragment;
        start_fragment = i;
        beginTransaction.replace(R.id.fl_container, fragment);
        beginTransaction.commit();
        isFragmentStatok = true;
    }

    private void changeFragment(BaseFragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        currentFragment = fragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fm.beginTransaction();
        beginTransaction.replace(R.id.fl_container, fragment);
        beginTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // web页数据返回
        /*if (requestCode == RequestCodeConstant.Web_Skip_Login
                && resultCode == RESULT_OK) {
            if (currentFragment instanceof TeacherFragment) {
                ((TeacherFragment) currentFragment).RefreshWeb();
            }
        }*/
        // 提醒页数据返回
        if (requestCode == RequestCodeConstant.PersonalCenter_Skip_RemindSet
                && resultCode == RESULT_OK) {
            if (currentFragment instanceof PersonalCenterFragment) {
                ((PersonalCenterFragment) currentFragment).RefreshRemindText();
            }
        }

        // 点赞后返回刷新
        /*if (requestCode == RequestCodeConstant.Web_Finish_Refresh
                && resultCode == RESULT_OK) {
            if (currentFragment instanceof TeacherFragment) {
                ((TeacherFragment) currentFragment).RefreshWeb();
            }
        }*/

        // 返回首页的选择
        if (resultCode == 2) {
            changeFragment(0);
            ((RadioButton) rbs.getChildAt(0)).setChecked(true);
        } else if (resultCode == 3) {
            changeFragment(3);
            ((RadioButton) rbs.getChildAt(3)).setChecked(true);
        } else if (resultCode == 4) {
            changeFragment(2);
            ((RadioButton) rbs.getChildAt(2)).setChecked(true);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isFragmentStatok) {
                return true;
            }
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!isFragmentStatok) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

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

    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_newest_version");
        params.addBodyParameter("system_id", "2");
        params.addBodyParameter("version", getVersion());

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
    public void download(String url) {
        downloadApk(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关掉首页时要把所有页面全部销毁
        ActivityControlManager.getInstance().finishAllActivity();
    }
}

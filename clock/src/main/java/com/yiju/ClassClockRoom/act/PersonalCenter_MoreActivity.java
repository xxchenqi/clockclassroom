package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

/**
 * ----------------------------------------
 * 注释: 更多
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/7 17:37
 * ----------------------------------------
 */
public class PersonalCenter_MoreActivity extends BaseActivity implements
        OnClickListener {
    //退出按钮
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //服务邮箱
    @ViewInject(R.id.rl_call_email)
    private RelativeLayout rl_call_email;
    //服务热线
    @ViewInject(R.id.rl_call_telephone)
    private RelativeLayout rl_call_telephone;
    //关于
    @ViewInject(R.id.rl_about_app)
    private RelativeLayout rl_about_app;
    //用户协议
    @ViewInject(R.id.rl_user_contract)
    private RelativeLayout rl_user_contract;
    //税率政策
    @ViewInject(R.id.rl_tax_policy)
    private RelativeLayout rl_tax_policy;
    //intent
    private Intent intent;

    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_more;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.label_more));

        head_back_relative.setOnClickListener(this);
        rl_call_email.setOnClickListener(this);
        rl_call_telephone.setOnClickListener(this);
        rl_about_app.setOnClickListener(this);
        rl_user_contract.setOnClickListener(this);
        rl_tax_policy.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                onBackPressed();
                break;
            case R.id.rl_call_email:
                // 唤起邮件客户端
                toExecuteSave();
                break;
            case R.id.rl_call_telephone:
                // 弹出电话呼叫窗口
                CustomDialog customDialog = new CustomDialog(
                        PersonalCenter_MoreActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        UIUtils.getString(R.string.txt_phone_number));
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                                // 跳转系统电话
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                        .parse("tel:" + UIUtils.getString(R.string.txt_phone_number_)));//400-608-2626
                                startActivity(intent);
                            } else {
                                PermissionsChecker.requestPermissions(
                                        PersonalCenter_MoreActivity.this,
                                        PermissionsChecker.CALL_PHONE_PERMISSIONS
                                );
                            }
                        }
                    }
                });
                break;
            case R.id.rl_about_app://关于
                intent = new Intent(UIUtils.getContext(),
                        PersonalCenter_MoreVersionActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_user_contract://用户协议
                intent = new Intent(UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                intent.putExtra(UIUtils.getString(R.string.get_page_name), WebConstant.WEB_Int_UserAgreement_Page);
                startActivity(intent);
                break;
            case R.id.rl_tax_policy://税率政策
                intent = new Intent(UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                intent.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.Draw_up_invoices_Page);
                startActivity(intent);
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
        return getString(R.string.title_act_personal_more);
    }

    /**
     * 唤起邮箱客户端
     */
    private void toExecuteSave() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        String[] tos = {"ejstaff@ehousechina.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, tos);
        intent.putExtra(Intent.EXTRA_TEXT, "请输入邮件内容");
        intent.putExtra(Intent.EXTRA_SUBJECT, "时钟教室用户反馈邮件");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose Email Client"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
//        } else {
            UIUtils.showToastSafe(getString(R.string.toast_permission_call_phone));
        }
    }
}

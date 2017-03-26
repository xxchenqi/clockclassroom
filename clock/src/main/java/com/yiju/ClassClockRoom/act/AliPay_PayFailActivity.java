package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

public class AliPay_PayFailActivity extends BaseActivity implements
        OnClickListener {

    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;


    @ViewInject(R.id.tv_fail_name)
    private TextView tv_fail_name;
    @ViewInject(R.id.tv_fail_type)
    private TextView tv_fail_type;
    @ViewInject(R.id.tv_fail_roomcount)
    private TextView tv_fail_roomcount;
    @ViewInject(R.id.tv_fail_tel)
    private TextView tv_fail_tel;
    @ViewInject(R.id.bt_backindex)
    private Button bt_backindex;
    @ViewInject(R.id.bt_again_choose)
    private Button bt_again_choose;
    private CartCommit info;

    @Override
    public int setContentViewId() {
        return R.layout.activity_alipayfail;
    }

    @Override
    public void initView() {
        head_back_relative.setOnClickListener(this);

        tv_fail_tel.setOnClickListener(this);
        bt_backindex.setOnClickListener(this);
        bt_again_choose.setOnClickListener(this);
    }

    @Override
    public void initData() {
        head_title.setText(UIUtils.getString(R.string.pay_result));

        // 加载数据
        Intent intent = getIntent();
        if (null != intent) {
            info = (CartCommit) intent.getSerializableExtra("fail");
            if (null != info) {
                tv_fail_name.setText(info.getType_desc());
                if (!"".equals(info.getUse_desc())) {
                    tv_fail_type.setText(String.format(UIUtils.getString(R.string.use_desc_content), info.getUse_desc()));
                }
                tv_fail_roomcount.setText(String.format(UIUtils.getString(R.string.sum_room_count), info.getRoom_count()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        // 处理点击事件
        switch (v.getId()) {
            case R.id.head_back_relative:// 返回
                onBackPressed();
                break;
            case R.id.tv_fail_tel://电话
                // 弹出电话呼叫窗口
                CustomDialog customDialog = new CustomDialog(
                        AliPay_PayFailActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        UIUtils.getString(R.string.txt_phone_number));
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + UIUtils.getString(R.string.txt_phone_number)));
                                startActivity(intent);
                            } else {
                                PermissionsChecker.requestPermissions(
                                        AliPay_PayFailActivity.this,
                                        PermissionsChecker.CALL_PHONE_PERMISSIONS
                                );
                            }
                        }
                    }
                });
                break;
            case R.id.bt_backindex://重新预订
                jumpIndex();
                break;
            case R.id.bt_again_choose://查看订单
                Intent intentOrder = new Intent(this, OrderDetailActivity.class);
                intentOrder.putExtra("oid", info.getOrder1_id() + "");
                intentOrder.putExtra("status", "1");
                startActivity(intentOrder);
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishAllAndOpenHome(this, 0);
    }

    private void jumpIndex() {
        ActivityControlManager.getInstance().finishAllAndOpenHome(this, 0);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_pay_fail);
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

package com.yiju.ClassClockRoom.act;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

/**
 * --------------------------------------
 * <p/>
 * 注释:退单失败界面
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-18 下午2:39:11
 * <p/>
 * --------------------------------------
 */
public class BackOrderFailActivity extends BaseActivity implements
        OnClickListener {
    /**
     * 科目名字
     */
    @ViewInject(R.id.tv_backorder_fail_name)
    private TextView tv_backorder_fail_name;
    /**
     * room
     */
    @ViewInject(R.id.tv_backorder_fail_type)
    private TextView tv_backorder_fail_type;
    /**
     * 地名
     */
    @ViewInject(R.id.tv_backorder_fail_room)
    private TextView tv_backorder_fail_room;
    /**
     * 订单编号
     */
    @ViewInject(R.id.tv_backorder_fail_oid)
    private TextView tv_backorder_fail_oid;
    /**
     * 联系人
     */
    @ViewInject(R.id.lr_backorder_fail_phone)
    private LinearLayout lr_backorder_fail_phone;
    /**
     * 返回首页
     */
    @ViewInject(R.id.bt_backindex)
    private Button btn_backorder_fail_backhome;
    /**
     * 查看订单
     */
    @ViewInject(R.id.bt_again_choose)
    private Button btn_backorder_fail_checkorder;
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
     * 传进来的数据
     */
    private Order2 order2;

    @Override
    public int setContentViewId() {
        return R.layout.activity_backorder_fail;
    }

    @Override
    public void initView() {
        lr_backorder_fail_phone.setOnClickListener(this);
        btn_backorder_fail_backhome.setOnClickListener(this);
        btn_backorder_fail_checkorder.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
        head_title.setText(getResources().getString(R.string.back_order_result));

        order2 = (Order2) getIntent().getSerializableExtra("order2");

        tv_backorder_fail_name.setText(order2.getUse_desc());
        tv_backorder_fail_type.setText(String.format(UIUtils.getString(R.string.use_desc_content), order2.getType_desc()));
        tv_backorder_fail_room.setText(order2.getSname());
        tv_backorder_fail_oid.setText(String.format(UIUtils.getString(R.string.order_num), order2.getPid()));

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lr_backorder_fail_phone://联系电话
                CustomDialog customDialog = new CustomDialog(
                        BackOrderFailActivity.this,
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
                                        .parse("tel:" + UIUtils.getString(R.string.txt_phone_number)));
                                if (ActivityCompat.checkSelfPermission(UIUtils.getContext(),
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                    return;
                                }
                                startActivity(intent);
                            } else {
                                PermissionsChecker.requestPermissions(
                                        BackOrderFailActivity.this,
                                        PermissionsChecker.CALL_PHONE_PERMISSIONS
                                );
                            }
                        }
                    }
                });
                break;
            case R.id.bt_backindex://返回首页
                Intent intent2 = new Intent(this, MainActivity.class);
                intent2.putExtra(MainActivity.Param_Start_Fragment, 0);
                startActivity(intent2);

                break;
            case R.id.bt_again_choose://查看订单
                Intent intent3 = new Intent(this, OrderDetailActivity.class);
                intent3.putExtra("oid", order2.getPid());
                intent3.putExtra("status", order2.getStatus());
                startActivity(intent3);

                break;
            case R.id.head_back_relative://返回
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_back_order_result);
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

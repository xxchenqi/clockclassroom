package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * ----------------------------------------
 * 注释:提交支付结果页
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/9/22 11:23
 * ----------------------------------------
 */
public class NewPayResultActivity extends BaseActivity implements View.OnClickListener {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //支付结果图标
    @ViewInject(R.id.iv_result)
    private ImageView iv_result;
    //支付结果文案
    @ViewInject(R.id.tv_result)
    private TextView tv_result;
    //支付结果内容
    @ViewInject(R.id.tv_result_content)
    private TextView tv_result_content;
    //提示信息
    @ViewInject(R.id.tv_result_gray)
    private TextView tv_result_gray;
    //返回首页
    @ViewInject(R.id.btn_back_home)
    private Button btn_back_home;
    //查看订单
    @ViewInject(R.id.btn_check_order)
    private Button btn_check_order;
    // type: 1 提交成功 2.支付成功 3.支付失败
    private int type;
    //数据
    private CartCommit cartCommit;

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        cartCommit = (CartCommit) intent.getSerializableExtra("conmmitInfo");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        switch (type) {
            case 1:
                //提交成功
                iv_result.setImageResource(R.drawable.order_sucess_icon);
                tv_result.setText(R.string.txt_order_submitted_successfully);
                tv_result_gray.setText(R.string.txt_will_be_confirm);
                head_title.setText(R.string.title_submit_the_results);
                break;
            case 2:
                //支付成功
                iv_result.setImageResource(R.drawable.order_sucess_icon);
                tv_result.setText(R.string.txt_order_payment_success);
                tv_result_gray.setText(R.string.txt_will_be_confirm);
                head_title.setText(R.string.pay_result);
                break;
            case 3:
                //支付失败
                iv_result.setImageResource(R.drawable.order_error_icon);
                tv_result.setText(R.string.txt_order_payment_failure);
                tv_result_gray.setText(R.string.txt_resubmit);
                head_title.setText(R.string.pay_result);
                btn_check_order.setVisibility(View.GONE);
                break;
        }
        tv_result_content.setText(cartCommit.getType_desc() + "等共" + cartCommit.getRoom_count() + "间课室");
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        btn_back_home.setOnClickListener(this);
        btn_check_order.setOnClickListener(this);
    }

    @Override
    public String getPageName() {
        switch (type) {
            case 1:
                return UIUtils.getString(R.string.title_act_commit_result_Page);
            case 2:
            case 3:
                return UIUtils.getString(R.string.title_act_pay_result_Page);
            default:
                return "";
        }
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_new_pay_result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            case R.id.btn_back_home:
                onBackPressed();
                break;
            case R.id.btn_check_order:
                Intent intentOrder = new Intent(this, OrderDetailActivity.class);
                intentOrder.putExtra("oid", cartCommit.getOrder1_id() + "");
                intentOrder.putExtra("status", "1");
                startActivity(intentOrder);
                break;
        }
    }

    private void jumpHome() {
        Intent intentIndex = new Intent(this, MainActivity.class);
        intentIndex.putExtra(MainActivity.Param_Start_Fragment, 0);
        startActivity(intentIndex);
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishAllAndOpenHome(this, 0);
    }
}

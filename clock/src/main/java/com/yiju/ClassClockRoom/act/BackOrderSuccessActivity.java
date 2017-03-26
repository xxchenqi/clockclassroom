package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.text.DecimalFormat;

/**
 * --------------------------------------
 * <p>
 * 注释:退单成功界面
 * <p>
 * <p>
 * <p>
 * 作者: cq
 * <p>
 * <p>
 * <p>
 * 时间: 2015-12-18 下午2:39:11
 * <p>
 * --------------------------------------
 */
public class BackOrderSuccessActivity extends BaseActivity implements
        OnClickListener {

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
     * 学校名字
     */
    @ViewInject(R.id.tv_sucess_name)
    private TextView tv_sucess_name;
    /**
     * 课室
     */
    @ViewInject(R.id.tv_sucess_classroom)
    private TextView tv_sucess_classroom;
    /**
     * 费用
     */
    @ViewInject(R.id.tv_sucess_fee)
    private TextView tv_sucess_fee;
    /**
     * 返回主页
     */
    @ViewInject(R.id.btn_sucess_backhome)
    private Button btn_sucess_backhome;
    /**
     * 查看订单
     */
    @ViewInject(R.id.btn_sucess_checkorder)
    private Button btn_sucess_checkorder;
    /**
     * 传进来的参数
     */
    private Order2 order2;
    /**
     * 当前费用
     */
    private String currentFee;
    /**
     * 选择天数
     */
    private String currentLength;
    /**
     * 精确两位小数
     */
    private DecimalFormat df;

    private int commission_rate;

    @Override
    public int setContentViewId() {
        return R.layout.activity_backorder_success;
    }

    @Override
    public void initView() {
        head_back_relative.setOnClickListener(this);
        btn_sucess_backhome.setOnClickListener(this);
        btn_sucess_checkorder.setOnClickListener(this);
        head_title.setText(getResources().getString(R.string.back_order_result));
        Intent intent = getIntent();
        order2 = (Order2) intent.getSerializableExtra("order2");
        currentFee = intent.getStringExtra("currentFee");
        currentLength = intent.getStringExtra("currentLength");
        commission_rate = intent.getIntExtra("commission_rate", 0);
        // 保留两位小数
        df = new DecimalFormat("#0.00");

    }

    @Override
    public void initData() {
        tv_sucess_name.setText(String.format(UIUtils.getString(R.string.desc_and_type_content), order2.getUse_desc(), order2.getType_desc()));
        tv_sucess_classroom.setText(String.format(UIUtils.getString(R.string.sum_room_classroom), order2.getRoom_count(), currentLength));
        tv_sucess_fee.setText(
                String.format(UIUtils.getString(R.string.sum_room_money), currentFee,
                        df.format(commission_rate)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sucess_backhome://返回首页
                Intent intent2 = new Intent(this, MainActivity.class);
                intent2.putExtra("backhome", "backhome");
                startActivity(intent2);
                break;
            case R.id.btn_sucess_checkorder://查看订单
                Intent intent3 = new Intent(this, OrderDetailActivity.class);
                intent3.putExtra("oid", order2.getPid());
                // intent3.putExtra("status", order2.getStatus());
                // intent3.putExtra("is_refund", currentFee);
                startActivity(intent3);
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_back_order_result);
    }

}

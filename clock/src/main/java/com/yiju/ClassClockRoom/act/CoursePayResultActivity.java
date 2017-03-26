package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.CreateOrderCourseResult;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * 课程订单结果页
 * Created by wh on 2016/6/23.
 */
public class CoursePayResultActivity extends BaseActivity implements
        OnClickListener {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    @ViewInject(R.id.iv_result_icon)
    private ImageView iv_result_icon;
    @ViewInject(R.id.tv_result)
    private TextView tv_result;
    @ViewInject(R.id.tv_desc)
    private TextView tv_desc;
    @ViewInject(R.id.ll_reason)
    private LinearLayout ll_reason;
    //查看订单
    @ViewInject(R.id.btn_check_order)
    private Button btn_check_order;
    //继续支付/订课
    @ViewInject(R.id.btn_goon)
    private Button btn_goon;
    //返回首页
    @ViewInject(R.id.tv_back_home)
    private TextView tv_back_home;

    private CreateOrderCourseResult courseResult;
    private String course_desc;

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_pay_result;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent != null) {
            courseResult = (CreateOrderCourseResult) intent.getSerializableExtra("courseResult");
            course_desc = intent.getStringExtra("course_desc");
        }
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        head_title.setText(UIUtils.getString(R.string.pay_result));
        if (courseResult == null) {
            return;
        }
        if ("1".equals(courseResult.getCode())) {
            iv_result_icon.setBackgroundResource(R.drawable.order_sucess_icon);
            tv_result.setText(R.string.reserve_ok);
            btn_goon.setText(R.string.txt_continue_to_book_class);
        } else {
            iv_result_icon.setBackgroundResource(R.drawable.failed);
            tv_result.setText(R.string.reserve_fail);
            btn_goon.setText(R.string.txt_continue_to_pay);
        }
        tv_desc.setText(course_desc);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        btn_check_order.setOnClickListener(this);
        btn_goon.setOnClickListener(this);
        tv_back_home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 处理点击事件
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                backPress();
                break;
            case R.id.btn_check_order://查看订单
                // 跳转到课程订单详情页
                break;
            case R.id.btn_goon://继续
                if (courseResult == null) {
                    return;
                }
                /*if ("1".equals(courseResult.getCode())) {
                    //继续订课
                } else {
                    //继续支付
                }*/
                break;
            case R.id.tv_back_home://返回首页
                jumpIndex();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void jumpIndex() {
        Intent intentIndex = new Intent(this, MainActivity.class);
        intentIndex.putExtra("backhome", "0");
        startActivity(intentIndex);
    }

    @Override
    public String getPageName() {
        return null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            backPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backPress() {
        Intent intentPerson = new Intent(this, MainActivity.class);
        intentPerson.putExtra("read", "1");
        startActivity(intentPerson);
    }
}

package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.CreateOrderCourseResult;
import com.yiju.ClassClockRoom.bean.result.CourseApplyResult;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

/**
 * 课程订单_立即支付
 * Created by wh on 2016/6/23.
 */
public class OrderCourseActivity extends BaseActivity implements View.OnClickListener {
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
     * 图片
     */
    @ViewInject(R.id.iv_course_pic)
    private ImageView iv_course_pic;
    /**
     * 课程名
     */
    @ViewInject(R.id.tv_course_name)
    private TextView tv_course_name;
    /**
     * 课程价格
     */
    @ViewInject(R.id.tv_course_price)
    private TextView tv_course_price;
    /**
     * 2次课
     */
    @ViewInject(R.id.tv_course_desc)
    private TextView tv_course_desc;
    /**
     * 数量
     */
    @ViewInject(R.id.tv_course_count)
    private TextView tv_course_count;
    /**
     * 老师名
     */
    @ViewInject(R.id.tv_course_teacher_name)
    private TextView tv_course_teacher_name;
    /**
     * 地址
     */
    @ViewInject(R.id.tv_course_address)
    private TextView tv_course_address;
    /**
     * 总计金额
     */
    @ViewInject(R.id.tv_course_total)
    private TextView tv_course_total;
    @ViewInject(R.id.rl_contact)
    private RelativeLayout rl_contact;
    /**
     * 联系人姓名
     */
    @ViewInject(R.id.tv_contact_name)
    private TextView tv_contact_name;
    /**
     * 联系人电话
     */
    @ViewInject(R.id.tv_contact_phone)
    private TextView tv_contact_phone;
    /**
     * 立即支付
     */
    @ViewInject(R.id.tv_pay_now)
    private TextView tv_pay_now;

    private CourseApplyResult courseApplyResult;
    private String contactId;

    @Override
    public void initIntent() {
        super.initIntent();
        courseApplyResult = (CourseApplyResult) getIntent().getSerializableExtra("ORDERCOURSE");
    }

    @Override
    protected void initView() {
        head_title.setText(R.string.order_affirm);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        rl_contact.setOnClickListener(this);
        tv_pay_now.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (courseApplyResult == null) {
            return;
        }
        Glide.with(mContext).load(courseApplyResult.getCourse_pic()).into(iv_course_pic);
        tv_course_name.setText(courseApplyResult.getCourse_name());
        tv_course_price.setText(
                String.format(
                        UIUtils.getString(R.string.how_much_money),
                        courseApplyResult.getSingle_price())
        );
        tv_course_desc.setText(
                String.format(
                        UIUtils.getString(R.string.course_how_much),
                        courseApplyResult.getTotal_hours() + "")
        );
        tv_course_count.setText(
                String.format(
                        UIUtils.getString(R.string.multiply),
                        courseApplyResult.getTotal_hours())
        );
        tv_course_teacher_name.setText(courseApplyResult.getTeacher_name());
        tv_course_address.setText(courseApplyResult.getSname());
        tv_course_total.setText(
                String.format(
                        UIUtils.getString(R.string.how_much_money),
                        courseApplyResult.getTotal_fee() + "")
        );
        if (courseApplyResult.getContact() == null) {
            tv_contact_name.setText(R.string.txt_select_contact);
        } else {
            contactId = courseApplyResult.getContact().getId();
            tv_contact_name.setText(courseApplyResult.getContact().getName());
            tv_contact_phone.setText(courseApplyResult.getContact().getMobile());
        }
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_order_course;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.rl_contact://选择联系人
                Intent intent = new Intent(this, ContactShopCartActivity.class);
                intent.putExtra("id", contactId);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_pay_now://立即支付
                getHttpUtils();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            contactId = data.getStringExtra("id");
            if (contactId != null) {
                String name = data.getStringExtra("name");
                String tel = data.getStringExtra("tel");
                tv_contact_name.setText(name);
                tv_contact_phone.setText(tel);
            }
        }
    }

    private void getHttpUtils() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "create_order1");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order2_id", courseApplyResult.getOrder2_id() + "");
        if (StringUtils.isNotNullString(tv_contact_phone.getText().toString())) {
            params.addBodyParameter("contact_name", tv_contact_name.getText().toString().trim());
            params.addBodyParameter("contact_mobile", tv_contact_phone.getText().toString().trim());
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        ProgressDialog.getInstance().dismiss();
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {
        CreateOrderCourseResult courseResult = GsonTools.changeGsonToBean(result,
                CreateOrderCourseResult.class);

        if (courseResult == null) {
            return;
        }
//        if ("1".equals(courseResult.getCode())){
//            int order1_id = courseResult.getOrder1_id();
//        }
        Intent intent = new Intent(this, CoursePayResultActivity.class);
        intent.putExtra("courseResult", courseResult);
        intent.putExtra("course_desc", courseApplyResult.getCourse_name() + ",共" + courseApplyResult.getTotal_hours() + "次课");
        startActivity(intent);
    }
}

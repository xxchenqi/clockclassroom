package com.yiju.ClassClockRoom.act;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.CourseOrderDetailBean;
import com.yiju.ClassClockRoom.bean.OrderCourseResult;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.ObservableScrollView;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:课程订单详情
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/24 13:55
 * ----------------------------------------
 */
public class CourseOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //标题右边文字
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //标题右边布局
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //关闭图片
    @ViewInject(R.id.iv_delete)
    private ImageView iv_delete;
    //订单超时提示布局
    @ViewInject(R.id.ll_order_overtime)
    private LinearLayout ll_order_overtime;
    //sv
    @ViewInject(R.id.sv_order_detail)
    private ObservableScrollView sv_order_detail;
    //订单超时文案
    @ViewInject(R.id.tv_course_order_content)
    private TextView tv_course_order_content;
    //展开布局
    @ViewInject(R.id.ll_unfold)
    private LinearLayout ll_unfold;
    //展开文案
    @ViewInject(R.id.tv_unfold)
    private TextView tv_unfold;
    //展开图片
    @ViewInject(R.id.iv_unfold)
    private ImageView iv_unfold;
    //明细
    @ViewInject(R.id.ll_check_classroom_detail)
    private LinearLayout ll_check_classroom_detail;
    //订单编号
    @ViewInject(R.id.tv_order_detail_id)
    private TextView tv_order_detail_id;
    //订单状态
    @ViewInject(R.id.tv_order_detail_status)
    private TextView tv_order_detail_status;
    //下单时间
    @ViewInject(R.id.tv_order_time)
    private TextView tv_order_time;
    //课程
    @ViewInject(R.id.tv_item_detail_use_desc)
    private TextView tv_item_detail_use_desc;
    //共多少次课程
    @ViewInject(R.id.tv_item_detail_room_count)
    private TextView tv_item_detail_room_count;
    //门店名字
    @ViewInject(R.id.tv_item_detail_sname)
    private TextView tv_item_detail_sname;
    //课程安排时间
    @ViewInject(R.id.ll_cancel_classroom)
    private LinearLayout ll_cancel_classroom;
    //订单小计金额
    @ViewInject(R.id.tv_item_detail_fee)
    private TextView tv_item_detail_fee;
    //联系人名字
    @ViewInject(R.id.tv_order_detail_name)
    private TextView tv_order_detail_name;
    //联系人手机
    @ViewInject(R.id.tv_order_detail_mobile)
    private TextView tv_order_detail_mobile;
    //支付方式布局
    @ViewInject(R.id.rl_detail_pay)
    private RelativeLayout rl_detail_pay;
    //订单总计布局
    @ViewInject(R.id.ll_order_total)
    private LinearLayout ll_order_total;
    //课程费用
    @ViewInject(R.id.tv_order_detail_money)
    private TextView tv_order_detail_money;
    //退款金额
    @ViewInject(R.id.tv_rl_reimburse_prices)
    private TextView tv_rl_reimburse_prices;
    //退款布局
    @ViewInject(R.id.rl_reimburse)
    private RelativeLayout rl_reimburse;
    //实付金额
    @ViewInject(R.id.tv_ought_pay)
    private TextView tv_ought_pay;
    //立即支付布局
    @ViewInject(R.id.rl_order_detail_pay)
    private RelativeLayout rl_order_detail_pay;
    //计时器时间
    @ViewInject(R.id.tv_count_down)
    private TextView tv_count_down;
    //课程安排布局
    @ViewInject(R.id.ll_up_down)
    private LinearLayout ll_up_down;
    //明细
    @ViewInject(R.id.tv_check_classroom_detail)
    private TextView tv_check_classroom_detail;
    //订单id
    private String oid;

    //是否展开，默认不展开
    private boolean is_unfold = false;
    //字体调整
    private SpannableStringBuilder builder;
    //字体调整颜色
    private ForegroundColorSpan greenSpan;
    //订单id
    private String order_id;
    // 服务器时间
    private int serverTime;
    //延期时间
    private int expireTime;
    //倒计时时间 ( time = expireTime - serverTime;)
    private int time;
    //总共数量
    private String sumCount;
    //学校名字
    private String sname;
    //课程id
    private String course_id;

    private String uid;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    //每秒的倒计时时间-1
                    time--;
                    /**
                     * 当倒计时结束时:
                     * 状态：待支付-->已关闭
                     * 按钮文字：关闭订单-->删除订单
                     */
                    if (time >= 0) {
                        handler.sendEmptyMessageDelayed(0, 1000);
                        tv_count_down.setText("[" + time / (60) + ":" + time % 60 + "]");
                    } else {
                        //计算后的结果小于0，强制改成已取消状态
                        rl_order_detail_pay.setVisibility(View.GONE);
                        tv_order_detail_status.setText(UIUtils
                                .getString(R.string.status_close));
                        tv_order_detail_status.setTextColor(getResources().getColor(R.color.color_blue_1e));
                        head_right_text.setText(UIUtils.getString(R.string.order_delete));
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void initIntent() {
        super.initIntent();
        oid = getIntent().getStringExtra("oid");
    }

    @Override
    protected void initView() {
        greenSpan = new ForegroundColorSpan(UIUtils.getColor(R.color.color_green_1e));
    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.order_detail));
        uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                "id", null);
        getHttpUtils();
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_delete.setOnClickListener(this);
        tv_course_order_content.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        ll_unfold.setOnClickListener(this);
        tv_order_detail_status.setOnClickListener(this);
        rl_order_detail_pay.setOnClickListener(this);
        tv_item_detail_use_desc.setOnClickListener(this);
        sv_order_detail.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
                ll_order_overtime.setVisibility(View.GONE);
            }
        });

    }


    /**
     * 课程订单详情请求
     */
    private void getHttpUtils() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_order_course_detail");
        params.addBodyParameter("id", oid);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
        CourseOrderDetailBean courseOrderDetailBean = GsonTools.changeGsonToBean(result,
                CourseOrderDetailBean.class);
        if (courseOrderDetailBean == null) {
            return;
        }
        if ("1".equals(courseOrderDetailBean.getCode())) {
            sv_order_detail.setVisibility(View.VISIBLE);
            CourseOrderDetailBean.DataEntity data = courseOrderDetailBean.getData();
            order_id = data.getOrder_id();
            course_id = data.getCourse_id();
            tv_order_detail_id.setText(String.format(UIUtils.getString(R.string.order_num), order_id));
            switch (Integer.valueOf(data.getStatus())) {
                case 0://待支付
                    tv_order_detail_status.setText(UIUtils.getString(R.string.label_wait_pay));
                    head_right_text.setText(UIUtils.getString(R.string.order_close));
                    ll_order_total.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    rl_order_detail_pay.setVisibility(View.VISIBLE);
                    rl_detail_pay.setVisibility(View.GONE);
                    // 未支付状态要改成时间
                    tv_order_detail_status.setText(UIUtils
                            .getString(R.string.status_zero));
                    tv_order_detail_status.setTextColor(getResources().getColor(
                            R.color.color_blue_1e));

                    if (data.getExpire_time() != null) {
                        expireTime = Integer.valueOf(data.getExpire_time());
                    }
                    if (data.getServer_time() != null) {
                        serverTime = Integer.valueOf(data.getServer_time());
                    }

                    // 开启倒计时
                    time = expireTime - serverTime;
                    if (time >= 0) {
                        tv_count_down.setText(
                                "[" + time / (60) + ":" + time % 60 + "]");
                        handler.sendEmptyMessage(0);
                    } else {
                        rl_order_detail_pay.setVisibility(View.GONE);
                        tv_order_detail_status.setText(UIUtils
                                .getString(R.string.status_close));
                        tv_order_detail_status.setTextColor(getResources().getColor(R.color.color_blue_1e));
                        head_right_text.setText(UIUtils.getString(R.string.order_delete));
                    }
                    break;
                case 1://进行中
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_ing));

                    tv_course_order_content.setText(UIUtils.getString(R.string.order_status_content_one));
                    builder = new SpannableStringBuilder(tv_course_order_content.getText().toString());
                    builder.setSpan(greenSpan, 22, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_course_order_content.setText(builder);
                    tv_course_order_content.setSelected(true);

                    ll_check_classroom_detail.setOnClickListener(this);
                    tv_check_classroom_detail.setVisibility(View.VISIBLE);
                    ll_order_overtime.setVisibility(View.VISIBLE);
                    break;
                case 2://已关闭
                case 4:
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_close));
                    head_right_text.setText(UIUtils.getString(R.string.order_delete));
                    ll_order_total.setVisibility(View.GONE);
                    ll_order_overtime.setVisibility(View.GONE);
                    rl_detail_pay.setVisibility(View.GONE);
                    break;
                case 6://订单超市
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_overtime));

                    tv_course_order_content.setText(UIUtils.getString(R.string.order_status_content_two));
                    builder = new SpannableStringBuilder(tv_course_order_content.getText().toString());
                    builder.setSpan(greenSpan, 13, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_course_order_content.setText(builder);
                    tv_course_order_content.setSelected(true);

                    ll_order_overtime.setVisibility(View.VISIBLE);
                    break;
                case 100://学生已取消
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_cancel));

                    tv_course_order_content.setText(UIUtils.getString(R.string.order_status_content_three));
                    builder = new SpannableStringBuilder(tv_course_order_content.getText().toString());
                    builder.setSpan(greenSpan, 26, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_course_order_content.setText(builder);
                    tv_course_order_content.setSelected(true);

                    tv_check_classroom_detail.setVisibility(View.VISIBLE);
                    ll_check_classroom_detail.setOnClickListener(this);
                    ll_order_overtime.setVisibility(View.VISIBLE);
                    break;
                case 101://已完成
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_finish));

                    tv_course_order_content.setText(UIUtils.getString(R.string.order_status_content_one));
                    builder = new SpannableStringBuilder(tv_course_order_content.getText().toString());
                    builder.setSpan(greenSpan, 22, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_course_order_content.setText(builder);
                    tv_course_order_content.setSelected(true);

                    ll_check_classroom_detail.setOnClickListener(this);
                    tv_check_classroom_detail.setVisibility(View.VISIBLE);
                    ll_order_overtime.setVisibility(View.VISIBLE);
                    break;
                case 110://老师已取消
                    tv_order_detail_status.setText(UIUtils.getString(R.string.status_cancel));

                    tv_course_order_content.setText(UIUtils.getString(R.string.order_status_content_four));
                    builder = new SpannableStringBuilder(tv_course_order_content.getText().toString());
                    builder.setSpan(greenSpan, 34, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_course_order_content.setText(builder);
                    tv_course_order_content.setSelected(true);
                    tv_check_classroom_detail.setVisibility(View.VISIBLE);
                    ll_check_classroom_detail.setOnClickListener(this);
                    ll_order_overtime.setVisibility(View.VISIBLE);
                    break;
            }

            tv_order_time.setText(DateUtil.second2Date(data.getCreate_time()));
            tv_item_detail_use_desc.setText(data.getCourse_name());
            sname = data.getSchool_name();
            tv_item_detail_sname.setText(data.getSchool_name());
            tv_item_detail_fee.setText(String.format(
                    UIUtils.getString(R.string.how_much_money), data.getSingle_fee()));
            tv_order_detail_name.setText(data.getContact_name());
            tv_order_detail_mobile.setText(data.getMobile());
            tv_order_detail_money.setText(String.format(
                    UIUtils.getString(R.string.how_much_money), data.getSingle_fee()));
            tv_ought_pay.setText(String.format(
                    UIUtils.getString(R.string.how_much_money), data.getSingle_fee()));

            List<CourseOrderDetailBean.DataEntity.ClassListEntity> classList =
                    data.getClasslist();

            if (classList.size() < 4) {
                ll_unfold.setVisibility(View.GONE);
            }
            if (classList.size() != 0) {
                handleClassRoomCancel(ll_cancel_classroom, classList);
            } else {
                ll_up_down.setVisibility(View.GONE);
            }
            if (!"0.00".equals(data.getRefund_fee())) {
                rl_reimburse.setVisibility(View.VISIBLE);
                tv_rl_reimburse_prices.setText(String.format(
                        UIUtils.getString(R.string.how_much_money), data.getRefund_fee()));
            }
            sumCount = data.getCountclasstime();
            tv_item_detail_room_count.setText(String.format(
                    UIUtils.getString(R.string.order_times), data.getCountclasstime()));
        } else {
            UIUtils.showToastSafe(courseOrderDetailBean.getMsg());
        }
    }

    /**
     * 课室安排
     */
    private void handleClassRoomCancel(LinearLayout ll_parent,
                                       List<CourseOrderDetailBean.DataEntity.ClassListEntity> classList) {
        for (int i = 0; i < classList.size(); i++) {
            LinearLayout cancel_layout = (LinearLayout) LayoutInflater.from(
                    UIUtils.getContext()).inflate(
                    R.layout.item_course_arrange, null);
            TextView tv_item_course = (TextView)
                    cancel_layout.findViewById(R.id.tv_item_course);
            TextView tv_item_time = (TextView)
                    cancel_layout.findViewById(R.id.tv_item_time);
            TextView tv_item_status = (TextView)
                    cancel_layout.findViewById(R.id.tv_item_status);
            CourseOrderDetailBean.DataEntity.ClassListEntity entity = classList.get(i);
            tv_item_course.setText(i + 1 + "/" + classList.size());
            tv_item_time.setText(entity.getDate());
            tv_item_status.setText(StringUtils.changeTime(entity.getStart_time()) + "-" +
                    StringUtils.changeTime(entity.getEnd_time()) + entity.getClassstatus());

            if (i > 2) {
                cancel_layout.setVisibility(View.GONE);
            }
            ll_parent.addView(cancel_layout, i);
        }
    }


    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_order_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                finish();
                break;
            case R.id.head_right_relative://关闭取消订单
                String s = head_right_text.getText().toString();
                if (UIUtils.getString(R.string.order_close).equals(s)) {
                    cancelOrder();
                } else if (UIUtils.getString(R.string.order_delete).equals(s)) {
                    deleteOrder();
                }
                break;
            case R.id.iv_delete://隐藏
                ll_order_overtime.setVisibility(View.GONE);
                break;
            case R.id.tv_course_order_content://电话
                CustomDialog customDialog = new CustomDialog(
                        CourseOrderDetailActivity.this,
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
                                if (ActivityCompat.checkSelfPermission(
                                        UIUtils.getContext(),
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                    return;
                                }
                                startActivity(intent);
                            } else {
                                PermissionsChecker.requestPermissions(
                                        CourseOrderDetailActivity.this,
                                        PermissionsChecker.CALL_PHONE_PERMISSIONS
                                );
                            }
                        }
                    }
                });
                break;
            case R.id.ll_unfold://展开收起
                if (is_unfold) {
                    //收起
                    tv_unfold.setText(UIUtils.getString(R.string.txt_unfold));
                    iv_unfold.setImageResource(R.drawable.arrow_down_icon);
                    is_unfold = false;

                    for (int i = 0; i < ll_cancel_classroom.getChildCount(); i++) {
                        if (i > 2) {
                            ll_cancel_classroom.getChildAt(i).setVisibility(View.GONE);
                        }
                    }

                } else {
                    //展开
                    tv_unfold.setText(UIUtils.getString(R.string.txt_shrink));
                    iv_unfold.setImageResource(R.drawable.arrow_up_icon);
                    is_unfold = true;
                    for (int i = 0; i < ll_cancel_classroom.getChildCount(); i++) {
                        ll_cancel_classroom.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.ll_check_classroom_detail://明细
                Intent intent_detail = new Intent(this, CourseParticularsActivity.class);
                intent_detail.putExtra("id", order_id);
                intent_detail.putExtra("sname", sname);
                intent_detail.putExtra("sumCount", sumCount);
                startActivity(intent_detail);
                break;
            case R.id.tv_order_detail_status://订单状态
                Intent intent_status = new Intent(this, Common_Show_WebPage_Activity.class);
                intent_status.putExtra(UIUtils.getString(R.string.redirect_open_url),
                        UrlUtils.ORDER_LOG + "oid=" + order_id);
                intent_status.putExtra(UIUtils.getString(R.string.get_page_name),
                        WebConstant.Order_Log_Page);
                startActivity(intent_status);
                break;
            case R.id.rl_order_detail_pay://立即支付
                UIUtils.showToastSafe(R.string.order_immediate_pay);
                break;
            case R.id.tv_item_detail_use_desc://跳转课程
                Intent intent = new Intent(UIUtils.getContext(), CourseDetailActivity.class);
                intent.putExtra("COURSE_ID", course_id);
                UIUtils.startActivity(intent);
                break;
        }
    }


    /**
     * 取消订单
     */
    private void cancelOrder() {
        CustomDialog customDialog = new CustomDialog(
                CourseOrderDetailActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_close_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    getHttpUtils2();
                }
            }
        });
    }

    // 请求关闭课程订单
    private void getHttpUtils2() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "close_course_order");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", order_id);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData2(arg0.result);

                    }
                });
    }

    private void processData2(String result) {
        OrderCourseResult mineOrder = GsonTools.changeGsonToBean(result,
                OrderCourseResult.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            //刷新
            // 修改状态
            tv_order_detail_status.setText(UIUtils.getString(R.string.status_close));
            // 隐藏取消订单
            head_right_relative.setVisibility(View.INVISIBLE);

            CustomDialog customDialog = new CustomDialog(
                    CourseOrderDetailActivity.this,
                    mineOrder.getMsg());
            customDialog.setOnClickListener(new IOnClickListener() {
                @Override
                public void oncClick(boolean isOk) {
                    if (isOk) {
                        CourseOrderDetailActivity.this.setResult(RESULT_OK);
                        finish();
                    }
                }
            });

        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }


    /**
     * 删除订单
     */
    private void deleteOrder() {
        CustomDialog customDialog = new CustomDialog(
                CourseOrderDetailActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_delete_order));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    // 请求删除
                    getHttpUtils3();
                }
            }
        });
    }

    /**
     * 删除请求
     */
    private void getHttpUtils3() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "delete_course_order");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("order1_id", order_id);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData3(arg0.result);

                    }
                });
    }

    private void processData3(String result) {
        OrderCourseResult mineOrder = GsonTools.changeGsonToBean(result,
                OrderCourseResult.class);
        if (mineOrder == null) {
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            //刷新
            CustomDialog customDialog = new CustomDialog(
                    CourseOrderDetailActivity.this,
                    mineOrder.getMsg());
            customDialog.setOnClickListener(new IOnClickListener() {
                @Override
                public void oncClick(boolean isOk) {
                    if (isOk) {
                        CourseOrderDetailActivity.this.setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }
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

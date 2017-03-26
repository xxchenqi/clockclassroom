package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.AdjustmentData;
import com.yiju.ClassClockRoom.bean.DeviceEntity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.OrderCancel;
import com.yiju.ClassClockRoom.bean.RoomAdjustEntity;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import java.util.List;

/**
 * --------------------------------------
 * <p>
 * 注释:订单编辑详情
 * <p>
 * <p>
 * <p>
 * 作者: cq
 * <p>
 * <p>
 * <p>
 * 时间: 2016-1-4 下午1:54:31
 * <p>
 * --------------------------------------
 */
@SuppressLint("SimpleDateFormat")
public class OrderEditDetailActivity extends BaseActivity implements
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
     * 删除按钮
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * 删除文案
     */
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    /**
     * 图片
     */
    @ViewInject(R.id.iv_order_edit_pic)
    private ImageView iv_order_edit_pic;
    /**
     * 订单desc
     */
    @ViewInject(R.id.tv_order_edit_use_desc)
    private TextView tv_order_edit_use_desc;
    /**
     * 订单type
     */
    @ViewInject(R.id.tv_order_edit_use_type)
    private TextView tv_order_edit_use_type;
    /**
     * 订单店名
     */
    @ViewInject(R.id.tv_order_edit_room)
    private TextView tv_order_edit_room;
    /**
     * 订单日期
     */
    @ViewInject(R.id.tv_order_edit_date)
    private TextView tv_order_edit_date;
    /**
     * 订单时间
     */
    @ViewInject(R.id.tv_order_edit_time)
    private TextView tv_order_edit_time;
    /**
     * 订单提醒
     */
    @ViewInject(R.id.tv_order_edit_repeat)
    private TextView tv_order_edit_repeat;
    /**
     * 编辑
     */
    @ViewInject(R.id.tv_cart_change)
    private TextView tv_cart_change;
    /**
     * 传值数据
     */
    private Order2 order2;

    private int position;
    /**
     * 设备
     */
    @ViewInject(R.id.tv_order_edit_device)
    private TextView tv_order_edit_device;
    //个性化调整
    @ViewInject(R.id.ll_up_down)
    private LinearLayout ll_up_down;
    //个性化调整区域块
    @ViewInject(R.id.ll_edit_individuation)
    private LinearLayout ll_edit_individuation;
    //取消课室
    @ViewInject(R.id.tv_cancel_classroom)
    private TextView tv_cancel_classroom;
    // 增加课室
    @ViewInject(R.id.tv_add_classroom)
    private TextView tv_add_classroom;
    //取消课室列表
    @ViewInject(R.id.ll_cancel_classroom)
    private LinearLayout ll_cancel_classroom;
    //增加课室列表
    @ViewInject(R.id.ll_add_classroom)
    private LinearLayout ll_add_classroom;
    //个别日期调整箭头
    @ViewInject(R.id.iv_up_down)
    private ImageView iv_up_down;
    //个别日期调整显示flag，默认为开
    private boolean more_flag = true;

    @Override
    public int setContentViewId() {
        return R.layout.activity_order_edit_detail;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        position = getIntent().getIntExtra("position", Integer.MAX_VALUE);
        order2 = (Order2) getIntent().getSerializableExtra("order2");
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.reserve_detail));
        head_right_relative.setVisibility(View.INVISIBLE);
        head_right_text.setText(getResources().getString(R.string.delete));

    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        tv_cart_change.setOnClickListener(this);
        ll_up_down.setOnClickListener(this);
    }

    @Override
    public void initData() {

        head_right_relative.setVisibility(View.VISIBLE);


        //设置图片等其他信息
        Glide.with(this).load(order2.getPic_url()).into(iv_order_edit_pic);
        tv_order_edit_use_desc.setText(order2.getType_desc());
        if (!"".equals(order2.getUse_desc())) {
            tv_order_edit_use_type.setText(String.format(UIUtils.getString(R.string.use_desc_content), order2.getUse_desc()));
        } else {
            tv_order_edit_use_type.setText("");
        }
        tv_order_edit_room.setText(order2.getSname());
        tv_order_edit_date.setText(String.format(UIUtils.getString(R.string.to_symbol), order2.getStart_date(), order2.getEnd_date()));
        tv_order_edit_time.setText(String.format(UIUtils.getString(R.string.to_symbol), StringUtils.changeTime(order2
                .getStart_time()), StringUtils.changeTime(order2.getEnd_time())));
        tv_order_edit_repeat.setText(StringUtils.getRepeatWeek(order2
                .getRepeat()));

        //设置收费设备
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.txt_device_no_free));
        if (order2.getDevice_nofree().size() != 0) {
            for (int i = 0; i < order2.getDevice_nofree().size(); i++) {
                DeviceEntity device_no_free = order2.getDevice_nofree().get(i);
                if (i == order2.getDevice_nofree().size() - 1) {
//                    sb.append(device_nofree.getDevice_name()).append(":").append(device_nofree.getFee()).append("元/").append(device_nofree.getUnit()).append("*小时").append("X").append(device_nofree.getNum());
                    sb.append(device_no_free.getDevice_name()).append("X").append(device_no_free.getNum());
                } else {
//                    sb.append(device_nofree.getDevice_name()).append(":").append(device_nofree.getFee()).append("元/").append(device_nofree.getUnit()).append("*小时").append("X").append(device_nofree.getNum()).append("\n");
                    sb.append(device_no_free.getDevice_name()).append("X").append(device_no_free.getNum()).append(";");
                }
            }
            tv_order_edit_device.setText(sb.toString());
        }
        //设置个别日期调整
        RoomAdjustEntity room_adjust = order2.getRoom_adjust();
        List<AdjustmentData> add_date = room_adjust.getAdd_date();
        List<AdjustmentData> cancel_date = room_adjust.getCancel_date();
        if (cancel_date != null && cancel_date.size() != 0) {
            //设置
            handleClassRoomCancel(ll_cancel_classroom, cancel_date);
        } else {
            //隐藏
            tv_cancel_classroom.setVisibility(View.GONE);
        }
        if (add_date != null && add_date.size() != 0) {
            //设置
            handleClassRoomCancel(ll_add_classroom, add_date);
        } else {
            //隐藏
            tv_add_classroom.setVisibility(View.GONE);
        }

        //如果增加和取消都没有都隐藏
        if (cancel_date != null && cancel_date.size() == 0 && add_date != null && add_date.size() == 0) {
            ll_up_down.setVisibility(View.GONE);
            ll_edit_individuation.setVisibility(View.GONE);
        }

    }

    /**
     * 取消课室
     *
     * @param ll_parent   动态添加布局的父控件
     * @param cancel_date 取消课室的日期数据源
     */
    private void handleClassRoomCancel(LinearLayout ll_parent, List<AdjustmentData> cancel_date) {
        if (cancel_date != null && cancel_date.size() != 0) {
            for (int i = 0; i < cancel_date.size(); i++) {
                LinearLayout cancel_layout = (LinearLayout) LayoutInflater.from(
                        UIUtils.getContext()).inflate(
                        R.layout.item_adjustment_date, null);
                TextView tv_adjustment_date = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_date);
                TextView tv_adjustment_time = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_time);
                TextView tv_adjustment_count = (TextView)
                        cancel_layout.findViewById(R.id.tv_adjustment_count);
                AdjustmentData adjustmentData = cancel_date.get(i);
                tv_adjustment_date.setText(adjustmentData.getDate());
                tv_adjustment_time.setText(
                        changeTime(adjustmentData.getStart_time())
                                + " - " + changeTime(adjustmentData.getEnd_time())
                );
                tv_adjustment_count.setText(
                        String.format(
                                getString(R.string.multiply),
                                adjustmentData.getRoom_count()
                        ));
                ll_parent.addView(cancel_layout, i);
            }
        }
    }

    /*
         *时间转换
 */
    private String changeTime(String start_time) {
        String h;
        String m;
        if (start_time.length() < 4) {
            h = "0" + start_time.substring(0, 1);
            m = start_time.substring(1);
        } else {
            h = start_time.substring(0, 2);
            m = start_time.substring(2);
        }
        return h + ":" + m;
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_order_edite_detail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_relative:
                //删除
                CustomDialog customDialog = new CustomDialog(
                        OrderEditDetailActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        "确认要删除该课室吗？");
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {// 携带数据跳转页面
                            Intent intent = new Intent();
                            intent.putExtra("position", position);
                            setResult(3, intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.tv_cart_change:
                //编辑
                CustomDialog customDialog2 = new CustomDialog(
                        OrderEditDetailActivity.this,
                        UIUtils.getString(R.string.confirm),
                        "想想再说",
                        "编辑教室将会从购物车中移除该教室，是否确认？");
                customDialog2.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {// 携带数据跳转页面
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra("backhome", "backhome");
                            intent.putExtra("info", order2);
                            UIUtils.startActivity(intent);
                            cancelOrder();
                        }
                    }
                });
                break;
            case R.id.ll_up_down:
                if (!more_flag) {
                    more_flag = true;
                    //显示
                    ll_edit_individuation.setVisibility(View.VISIBLE);
                    iv_up_down.setImageDrawable(UIUtils.getDrawable(R.drawable.more_btn_up));
                } else {
                    more_flag = false;
                    //隐藏
                    ll_edit_individuation.setVisibility(View.GONE);
                    iv_up_down.setImageDrawable(UIUtils.getDrawable(R.drawable.more_btn_down));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
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

    private void cancelOrder() {
        // 取消订单
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "cart_cancel");
        params.addBodyParameter("oid", order2.getId());
        params.addBodyParameter("sid", order2.getSid());
        params.addBodyParameter("uid", order2.getUid());
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_RESERVATION,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        OrderCancel info = GsonTools.changeGsonToBean(
                                arg0.result, OrderCancel.class);
                        if (null != info) {
                            if (info.getCode() == 1
                                    && info.getMsg().equals(getString(R.string.toast_show_cancle_course))) {
                                UIUtils.showToastSafe(getString(R.string.toast_order_have_been_deleted));
                            }
                        }
                    }
                });
    }
}

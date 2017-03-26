package com.yiju.ClassClockRoom.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.ClassroomAdapter;
import com.yiju.ClassClockRoom.bean.ClassroomItem;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Order3;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.MyListView;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

/**
 * --------------------------------------
 * <p/>
 * 注释:课室详情
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-15 下午6:36:32
 * <p/>
 * --------------------------------------
 */
public class ClassroomDetailActivity extends BaseActivity implements
        OnClickListener {

    /**
     * 整个内容区域
     */
    @ViewInject(R.id.sv_classroom)
    private ScrollView sv_classroom;
    /**
     * 未完成布局
     */
    @ViewInject(R.id.lr_classroom1)
    private LinearLayout lr_classroom1;
    /**
     * 已使用布局
     */
    @ViewInject(R.id.lr_classroom2)
    private LinearLayout lr_classroom2;
    /**
     * 已退单布局
     */
    @ViewInject(R.id.lr_classroom3)
    private LinearLayout lr_classroom3;
    /**
     * 地点
     */
    @ViewInject(R.id.tv_classromm_use_desc)
    private TextView tv_classromm_use_desc;
    /**
     * 房间
     */
    @ViewInject(R.id.tv_classromm_type_desc)
    private TextView tv_classromm_type_desc;
    /**
     * 房间数量
     */
    @ViewInject(R.id.tv_classromm_room_count)
    private TextView tv_classromm_room_count;
    /**
     * 店名
     */
    @ViewInject(R.id.tv_classromm_sname)
    private TextView tv_classromm_sname;
    /**
     * 退单按钮
     */
    @ViewInject(R.id.btn_classroom_back_order)
    private TextView btn_classroom_back_order;
    /**
     * 未完成lv
     */
    @ViewInject(R.id.lv_classroom1)
    private MyListView lv_classroom1;
    /**
     * 已使用lv
     */
    @ViewInject(R.id.lv_classroom2)
    private MyListView lv_classroom2;
    /**
     * 已退单lv
     */
    @ViewInject(R.id.lv_classroom3)
    private MyListView lv_classroom3;
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

    /**
     * 未完成数据源
     */
    private List<Order3> datas_no_finish;
    /**
     * 已使用数据源
     */
    private List<Order3> datas_has_use;
    /**
     * 已退单数据源
     */
    private List<Order3> datas_back_order;

    /**
     * 未完成不同日期
     */
    private TreeSet<String> no_finish_date;
    /**
     * 已使用不同日期
     */
    private TreeSet<String> has_use_date;
    /**
     * 已退单不同日期
     */
    private TreeSet<String> back_order_date;

    /**
     * 未完成相同日期里的所有内容
     */
    private List<ClassroomItem> no_finish_list;
    /**
     * 已使用相同日期里的所有内容
     */
    private List<ClassroomItem> has_use_list;
    /**
     * 已退单相同日期里的所有内容
     */
    private List<ClassroomItem> back_order_list;

    private ClassroomAdapter adapter1;
    private ClassroomAdapter adapter2;
    private ClassroomAdapter adapter3;

    /**
     * 使用优惠券的内容
     */
    @ViewInject(R.id.tv_coupon_content)
    private TextView tv_coupon_content;

    /**
     * 未完成相同日期里的所有内容 (去除订单的起始日期-24小时也能退款的order3)
     */
    private List<ClassroomItem> no_finish_hour_list;
    private String course_id;
    private String coupon_id;

    @Override
    public int setContentViewId() {
        return R.layout.activity_classroom_detail;
    }

    @Override
    public void initView() {
        btn_classroom_back_order.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
        order2 = (Order2) getIntent().getSerializableExtra("order2");
        coupon_id = getIntent().getStringExtra("coupon_id");
        head_title.setText(getResources().getString(R.string.classroom_detail));
        datas_no_finish = new ArrayList<>();
        datas_has_use = new ArrayList<>();
        datas_back_order = new ArrayList<>();

        no_finish_date = new TreeSet<>();
        has_use_date = new TreeSet<>();
        back_order_date = new TreeSet<>();

        no_finish_list = new ArrayList<>();
        has_use_list = new ArrayList<>();
        back_order_list = new ArrayList<>();
        no_finish_hour_list = new ArrayList<>();
    }

    @Override
    public void initData() {
        if (order2 == null) {
            return;
        }
        tv_classromm_use_desc.setText(order2.getType_desc());
        if (StringUtils.isNotNullString(order2.getUse_desc())) {
            tv_classromm_type_desc.setText(
                    String.format(
                            UIUtils.getString(R.string.max_member_text),
                            order2.getMax_member()
                    ));
        }
        tv_classromm_sname.setText(order2.getSname());
        tv_classromm_room_count.setText(
                String.format(
                        UIUtils.getString(R.string.total_hour_short_text),
                        order2.getTotal_hour()
                ));

        // 分解order2
        ArrayList<Order3> order3 = order2.getOrder3();
        // is_refund > 0 已退单
        // 结束时间-现在时间>=0 未完成
        // 结束时间-现在时间<0 已使用
        for (int i = 0; i < order3.size(); i++) {
            Order3 o3 = order3.get(i);
            if (Integer.valueOf(o3.getIs_refund()) > 0) {
                // 已退单 is_refund > 0
                datas_back_order.add(o3);
            } else {
                String end_time = o3.getDate() + " " + o3.getEnd_time();
                if (date2millisecond(end_time) >= 0) {
                    // 未完成

//                    //是否绑定课程
//                    boolean binding_flag = false;
//                    List<ClassCourseSchedule> course_schedule = o3.getCourse_schedule();
//                    if (course_schedule != null) {
//                        for (int j = 0; j < course_schedule.size(); j++) {
//                            String course_id = course_schedule.get(j).getCourse_id();
//                            if ("0".equals(course_id)) {
//                                binding_flag = false;
//                            } else {
//                                //其中1个不等于0就绑定
//                                binding_flag = true;
//                                break;
//                            }
//                        }
//                        o3.setCourse_flag(binding_flag);
//                    }

                    datas_no_finish.add(o3);
                } else {
                    // 已使用

                    //是否绑定课程
//                    boolean binding_flag = false;
//                    List<ClassCourseSchedule> course_schedule = o3.getCourse_schedule();
//                    if (course_schedule != null) {
//                        for (int j = 0; j < course_schedule.size(); j++) {
//                            String course_id = course_schedule.get(j).getCourse_id();
//                            if ("0".equals(course_id)) {
//                                binding_flag = false;
//                            } else {
//                                //其中1个不等于0就绑定
//                                binding_flag = true;
//                                break;
//                            }
//                        }
//                        o3.setCourse_flag(binding_flag);
//                    }
                    datas_has_use.add(o3);
                }
            }
        }

        // 添加未完成的不同日期到Set
        for (int i = 0; i < datas_no_finish.size(); i++) {
            Order3 o3 = datas_no_finish.get(i);
            String isSame = o3.getDate() + " " + o3.getStart_time() + "-"
                    + o3.getEnd_time();
            no_finish_date.add(isSame);
        }
        // 添加已使用的不同日期到Set
        for (int i = 0; i < datas_has_use.size(); i++) {
            Order3 o3 = datas_has_use.get(i);
            String isSame = o3.getDate() + " " + o3.getStart_time() + "-"
                    + o3.getEnd_time();
            has_use_date.add(isSame);
        }
        // 添加已退单的不同日期到Set
        for (int i = 0; i < datas_back_order.size(); i++) {
            Order3 o3 = datas_back_order.get(i);
            String isSame = o3.getDate() + " " + o3.getStart_time() + "-"
                    + o3.getEnd_time();
            back_order_date.add(isSame);
        }

        // 相同日期里的内容添加到list<相同日期的order3>,并创建ClassroomItem,添加到总的集合中
        for (String date : no_finish_date) {
            List<Order3> list = new ArrayList<>();
            for (int i = 0; i < datas_no_finish.size(); i++) {
                Order3 o3 = datas_no_finish.get(i);
                String isSame = o3.getDate() + " " + o3.getStart_time() + "-"
                        + o3.getEnd_time();
                if (date.equals(isSame)) {
                    list.add(o3);
                }
            }
            no_finish_list.add(new ClassroomItem(date, list, "no_finish"));
        }

        for (String date : has_use_date) {
            List<Order3> list = new ArrayList<>();
            for (int i = 0; i < datas_has_use.size(); i++) {
                Order3 o3 = datas_has_use.get(i);
                String isSame = o3.getDate() + " " + o3.getStart_time() + "-"
                        + o3.getEnd_time();
                if (date.equals(isSame)) {
                    list.add(o3);
                }
            }
            has_use_list.add(new ClassroomItem(date, list, "has_use"));
        }

        for (String date : back_order_date) {
            List<Order3> list = new ArrayList<>();
            for (int i = 0; i < datas_back_order.size(); i++) {
                Order3 o3 = datas_back_order.get(i);
                String isSame = o3.getDate() + " " + o3.getStart_time() + "-"
                        + o3.getEnd_time();
                if (date.equals(isSame)) {
                    list.add(o3);
                }
            }
            back_order_list.add(new ClassroomItem(date, list, "back_order"));
        }

        if (no_finish_list.size() != 0) {
            adapter1 = new ClassroomAdapter(this, no_finish_list,
                    R.layout.item_classroom1, order2);
            lv_classroom1.setAdapter(adapter1);
            lr_classroom1.setVisibility(View.VISIBLE);
        }
        if (has_use_list.size() != 0) {
            adapter2 = new ClassroomAdapter(this, has_use_list,
                    R.layout.item_classroom1, order2);
            lv_classroom2.setAdapter(adapter2);
            lr_classroom2.setVisibility(View.VISIBLE);
        }
        if (back_order_list.size() != 0) {
            adapter3 = new ClassroomAdapter(this, back_order_list,
                    R.layout.item_classroom1, order2);
            lv_classroom3.setAdapter(adapter3);
            lr_classroom3.setVisibility(View.VISIBLE);
        }

        // 设置用过优惠券的未完成不能点击
        if (coupon_id != null && !"0".equals(coupon_id)) {
            // 显示字体
            tv_coupon_content.setVisibility(View.VISIBLE);

            // 按钮字体变色，无法点击
            btn_classroom_back_order.setClickable(false);
            btn_classroom_back_order.setTextColor(getResources().getColor(
                    R.color.color_gay_8f));
        }

        for (int i = 0; i < no_finish_list.size(); i++) {
            Order3 order3s = no_finish_list.get(i).getList().get(0);
            // 起始时间-24h < 现在时间 就不能退款
            String time = order3s.getDate() + " "
                    + changeTime(order3s.getStart_time());
            if (date2startmillisecond(time)) {
                //course_schedule里的course_id 全部等于0 才能退款
//                course_id= 0 未绑定课程 可以退
//                course_id!=0 绑定课程 	不可退(要取消)
                //是否能退标志
//                boolean refund_flag = false;
//                List<ClassCourseSchedule> course_schedule = order3s.getCourse_schedule();
//                if (course_schedule != null) {
//                    for (int j = 0; j < course_schedule.size(); j++) {
//                        String course_id = course_schedule.get(j).getCourse_id();
//                        if ("0".equals(course_id)) {
//                            refund_flag = true;
//                        } else {
//                            //其中1个不等于0就不能退
//                            refund_flag = false;
//                            break;
//                        }
//                    }
//                }
//                if (refund_flag) {
//                }
                no_finish_hour_list.add(no_finish_list.get(i));

            }
        }

//        course_id = order2.getCourse_id();
//        if (!"0".equals(course_id)) {
//            //不能退
//            btn_classroom_back_order.setTextColor(getResources().getColor(
//                    R.color.color_gay_8f));
//            tv_classromm_room_count.setText("已关联课程");
//        }
        sv_classroom.smoothScrollTo(0, 20);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_classroom_back_order://退单
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_114");
//                if (!"0".equals(course_id)) {
//                    UIUtils.showToastSafe("已关联课程的课室不可进行退单，需要取消课程发布方可进行退单");
//                }else{
                if (order2 == null) {
                    return;
                }
                final String tel;
                if (StringUtils.isNullString(order2.getSchool_phone())) {
                    tel = UIUtils.getString(R.string.txt_phone_number);
                } else {
                    tel = order2.getSchool_phone();
                }
                CustomDialog dialog = new CustomDialog(
                        this,
                        UIUtils.getString(R.string.dialog_confirm_call_service),
                        UIUtils.getString(R.string.label_cancel),
                        UIUtils.getString(R.string.dialog_message_call_service)
                );
                dialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            if (!PermissionsChecker.checkPermission(PermissionsChecker.CALL_PHONE_PERMISSIONS)) {
                                // 跳转系统电话
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                        .parse("tel:" + tel));// 默认400-608-2626
                                startActivity(intent);
                            } else {
                                PermissionsChecker.requestPermissions(
                                        ClassroomDetailActivity.this,
                                        PermissionsChecker.CALL_PHONE_PERMISSIONS
                                );
                            }
                        }
                    }
                });
               /* Intent intent = new Intent(this, BackOrderActivity.class);
                intent.putExtra("list", (Serializable) no_finish_hour_list);
                intent.putExtra("order2", order2);
                startActivity(intent);*/
//                }
                break;
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_113");
                finish();
                break;
            default:
                break;
        }

    }

    /*
     * 起始时间(日期转毫秒)-当前时间
     */
    @SuppressLint("SimpleDateFormat")
    private boolean date2startmillisecond(String start_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            // 起始时间 - 24h
            long millionSeconds = sdf.parse(start_time).getTime() - 86400000;
            // 当前时间
            long now_time = new Date().getTime();

            // 如果起始时间小于当前时间,不可以退款
            return millionSeconds >= now_time;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * 时间转换
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

    /*
     * 结束时间(日期转毫秒)-当前时间
     */
    @SuppressLint("SimpleDateFormat")
    private long date2millisecond(String end_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm");
        try {
            // 结束时间
            long millionSeconds = sdf.parse(end_time).getTime();
            // 当前时间
            long now_time = new Date().getTime();

            return millionSeconds - now_time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_classroom_detail);
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

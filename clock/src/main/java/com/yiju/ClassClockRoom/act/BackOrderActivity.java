package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.BackOrderAdapter;
import com.yiju.ClassClockRoom.bean.ClassroomItem;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Order3;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * --------------------------------------
 * <p/>
 * 注释:退单
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-17 下午6:43:00
 * <p/>
 * --------------------------------------
 */
public class BackOrderActivity extends BaseActivity implements OnClickListener,
        OnCheckedChangeListener, OnItemClickListener {
    /**
     * 退单lv
     */
    @ViewInject(R.id.lv_backorder)
    private ListView lv_backorder;
    /**
     * 全选按钮
     */
    @ViewInject(R.id.cb_backorder_all)
    private CheckBox cb_backorder_all;
    /**
     * 退单金额
     */
    @ViewInject(R.id.tv_backorder_total_price)
    private TextView tv_backorder_total_price;
    /**
     * 确认退单按钮
     */
    @ViewInject(R.id.btn_backorder_affirm)
    private Button btn_backorder_affirm;
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
     * 退单数据源
     */
    private List<ClassroomItem> list;
    /**
     * 退单适配器
     */
    private BackOrderAdapter adapter;
    /**
     * 总费用
     */
    private double totalFee = 0;
    /**
     * 当前费用
     */
    private double currentFee = 0;
    /**
     * 精确两位小数
     */
    private DecimalFormat df;
    /**
     * 选择item的cb
     */
    private boolean cb_status = false;
    /**
     * 选择订单的id
     */
    private List<String> ids;
    /**
     * order2 bean
     */
    private Order2 order2;
    /**
     * 有退单显示的布局
     */
    @ViewInject(R.id.rl_backorder_show1)
    private RelativeLayout rl_backorder_show1;
    /**
     * 无退单显示的布局
     */
    @ViewInject(R.id.rl_backorder_show2)
    private RelativeLayout rl_backorder_show2;

    @Override
    public int setContentViewId() {
        return R.layout.activity_backorder;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        list = (List<ClassroomItem>) getIntent().getSerializableExtra("list");
        order2 = (Order2) getIntent().getSerializableExtra("order2");
    }

    @Override
    public void initView() {
        adapter = new BackOrderAdapter(this, list, R.layout.item_backorder);
        // 保留两位小数
        df = new DecimalFormat("#0.00");
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_backorder_affirm.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
        cb_backorder_all.setOnCheckedChangeListener(this);
        lv_backorder.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        head_title.setText(getResources().getString(R.string.back_order));

        lv_backorder.setAdapter(adapter);
        //长度=0，显示无退单界面
        if (list.size() == 0) {
            rl_backorder_show1.setVisibility(View.GONE);
            rl_backorder_show2.setVisibility(View.VISIBLE);
        } else {
            rl_backorder_show1.setVisibility(View.VISIBLE);
            rl_backorder_show2.setVisibility(View.GONE);
        }

        // 计算总金额
        for (int i = 0; i < list.size(); i++) {
            ClassroomItem classroomItem = list.get(i);
            // list2 表示相同日期的多个order3
            List<Order3> list2 = classroomItem.getList();
            // 相同日期的order3只加1次Device_refund(设备退款金额)
            if (list2.get(0).getDevice_refund() != null) {
                totalFee += Double.valueOf(list2.get(0).getDevice_refund());
            } else {
                btn_backorder_affirm.setClickable(false);
            }
            //循环遍历order3里的退单金额
            for (int j = 0; j < list2.size(); j++) {
                Order3 order3 = list2.get(j);
                totalFee += Double.valueOf(order3.getFee());
            }
        }
        currentFee = totalFee;
        tv_backorder_total_price.setText(String.format(UIUtils.getString(R.string.how_much_money), df.format(currentFee)));
    }

    /**
     * 退单请求
     *
     * @param oid 订单id
     */
    private void getHttpUtils(String oid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "refund");
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("level", "3");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }

        if ("1".equals(mineOrder.getCode())) {

            int currentLength = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCb_status()) {
                    currentLength++;
                }
            }
            Intent intent = new Intent(this, BackOrderSuccessActivity.class);
            intent.putExtra("order2", order2);//二级订单bean
            intent.putExtra("currentFee", df.format(currentFee));//当前费用
            intent.putExtra("currentLength", currentLength + "");//当前长度
            intent.putExtra("commission_rate", mineOrder.getCommission_rate());//手续费
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, BackOrderFailActivity.class);
            intent.putExtra("order2", order2);//二级订单bean
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backorder_affirm://确认退单
                ids = new ArrayList<>();
                // 遍历选择订单
                for (int i = 0; i < list.size(); i++) {
                    ClassroomItem classroomItem = list.get(i);
                    if (classroomItem.isCb_status()) {
                        List<Order3> list2 = classroomItem.getList();
                        for (int j = 0; j < list2.size(); j++) {
                            ids.add(list2.get(j).getId());
                        }
                    }
                }

                StringBuilder sb = new StringBuilder();
                // 请求选择订单
                for (int i = 0; i < ids.size(); i++) {
                    if (i == ids.size() - 1) {
                        sb.append(ids.get(i));
                    } else {
                        sb.append(ids.get(i)).append(getResources().getString(R.string.douhao));
                    }
                }
                getHttpUtils(sb.toString());

                break;
            case R.id.head_back_relative://返回
                finish();
                break;
            default:
                break;
        }
    }

    //全选费全选计算金额
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!cb_status) {
            if (isChecked) {
                // item里所有的item全部选中,价格未总金额
                currentFee = totalFee;
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCb_status(true);
                }
                btn_backorder_affirm
                        .setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
                btn_backorder_affirm.setClickable(true);

            } else {
                // item里所有的item全部不选,价格为0
                currentFee = 0;
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCb_status(false);
                }
                btn_backorder_affirm
                        .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
                btn_backorder_affirm.setClickable(false);
            }
            // 设置当前金额
            tv_backorder_total_price.setText(String.format(UIUtils.getString(R.string.how_much_money), df.format(currentFee)));
            adapter.notifyDataSetChanged();
        }

    }

    //listview的item选择、非选择进行计算金额
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        CheckBox cb = (CheckBox) ((RelativeLayout) view).getChildAt(0);
        cb.setChecked(!cb.isChecked());
        // 获取选择的item的退单金额
        List<Order3> list2 = list.get(position).getList();
        // 重新设置cb的状态
        list.get(position).setCb_status(cb.isChecked());
        // 选择日期的金额
        double itemFee = 0;
        // 相同日期的order3只加1次Device_refund
        if (list2.get(0).getDevice_refund() != null) {
            itemFee += Double.valueOf(list2.get(0).getDevice_refund());
        } else {
            btn_backorder_affirm.setClickable(false);
        }
        for (int i = 0; i < list2.size(); i++) {
            itemFee += Double.valueOf(list2.get(i).getFee());
        }

        if (cb.isChecked()) {
            // 选择
            currentFee += itemFee;
        } else {
            // 取消
            currentFee -= itemFee;
        }
        tv_backorder_total_price.setText(
                String.format(UIUtils.getString(R.string.how_much_money),
                        df.format(currentFee)));

        // 是否选择全选按钮

        int size = list.size();
        int s = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCb_status()) {
                s++;
            }
        }
        cb_status = true;
        if (s == size) {
            // 全选
            cb_backorder_all.setChecked(true);
        } else {

            cb_backorder_all.setChecked(false);
        }
        cb_status = false;

        if (s == 0) {
            // 没一个选择，按钮不能点击
            btn_backorder_affirm
                    .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
            btn_backorder_affirm.setClickable(false);
        } else {
            btn_backorder_affirm
                    .setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
            btn_backorder_affirm.setClickable(true);
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_back_order);
    }
}

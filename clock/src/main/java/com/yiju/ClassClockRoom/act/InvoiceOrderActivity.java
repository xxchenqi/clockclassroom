package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.yiju.ClassClockRoom.adapter.InvoiceOrderAdapter;
import com.yiju.ClassClockRoom.adapter.InvoiceOrderAdapter.CheckChangeListener;
import com.yiju.ClassClockRoom.bean.MineOrderData;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * --------------------------------------
 * <p/>
 * 注释:可开发票订单
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-21 下午12:55:36
 * <p/>
 * --------------------------------------
 */
public class InvoiceOrderActivity extends BaseActivity implements
        OnClickListener, OnCheckedChangeListener, CheckChangeListener {
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
     * 列表
     */
    @ViewInject(R.id.lv_inovice)
    private ListView lv_inovice;
    /**
     * 无订单布局
     */
    @ViewInject(R.id.rl_mine_order3)
    private RelativeLayout rl_mine_order3;
    /**
     * 随便逛逛按钮
     */
    @ViewInject(R.id.btn_mineorder_stroll)
    private Button btn_mineorder_stroll;
    /**
     * 全选按钮
     */
    @ViewInject(R.id.cb_invoice_choose_all)
    private CheckBox cb_invoice_choose_all;
    /**
     * ListView布局
     */
    @ViewInject(R.id.rl_invoice_all)
    private RelativeLayout rl_invoice_all;
    /**
     * 下一步按钮
     */
    @ViewInject(R.id.btn_invoice_next)
    private Button btn_invoice_next;
    /**
     * 总价格tv
     */
    @ViewInject(R.id.tv_invoice_price)
    private TextView tv_invoice_price;
    /**
     * 无WIFI显示界面
     */
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    /**
     * 刷新
     */
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
    /**
     * listview数据源
     */
    private ArrayList<MineOrderData> data;
    /**
     * 适配器
     */
    private InvoiceOrderAdapter adapter;
    /**
     * 参数limit
     */
    private String limit = "0,999";
    /**
     * 参数invoice_flag
     */
    private String invoice_flag = "1";
    /**
     * 成功状态码
     */
    private String successfulCode = "1";
    /**
     * 总费用
     */
    private double totalFee = 0;
    /**
     * 当前费用
     */
    private double currentFee = 0;
    /**
     * 保留2位小数
     */
    private DecimalFormat df;
    /**
     * 列表总长度(用来判断是否全选)
     */
    private int size = 0;
    /**
     * 判断是否是item点击
     */
    private boolean cb_status = false;


    @Override
    public int setContentViewId() {
        return R.layout.activity_invoice;
    }

    @Override
    public void initView() {
        data = new ArrayList<>();
        df = new DecimalFormat("#0.00");
        adapter = new InvoiceOrderAdapter(this, data,
                R.layout.item_invoiceorder, this);
    }

    @Override
    public void initData() {
        head_title.setText(getResources().getString(R.string.invoice_title));
        btn_invoice_next.setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);

        lv_inovice.setAdapter(adapter);

        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            getHttpUtils();
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        btn_mineorder_stroll.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
        btn_invoice_next.setOnClickListener(this);
        cb_invoice_choose_all.setOnCheckedChangeListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        btn_invoice_next.setClickable(false);

    }

    private void getHttpUtils() {
        ProgressDialog.getInstance().dismiss();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_list");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("limit", limit);
        params.addBodyParameter("invoice_flag", invoice_flag);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();

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
        if (successfulCode.equals(mineOrder.getCode())) {
            data.clear();
            totalFee = 0;
            ArrayList<MineOrderData> data2 = mineOrder.getData();
            if (data2.size() > 0) {
                // 显示ListView和全选
                rl_invoice_all.setVisibility(View.VISIBLE);
                rl_mine_order3.setVisibility(View.GONE);
                // 计算出总金额
                for (int i = 0; i < data2.size(); i++) {
                    totalFee += Double.valueOf(data2.get(i).getReal_fee());
                }

                this.data.addAll(data2);
                adapter.notifyDataSetChanged();
            } else {
                // 显示无订单布局
                rl_invoice_all.setVisibility(View.GONE);
                rl_mine_order3.setVisibility(View.VISIBLE);
            }

        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }
        ProgressDialog.getInstance().dismiss();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.btn_mineorder_stroll:
                // 随便逛逛
                this.setResult(2);
                finish();
                break;
            case R.id.btn_invoice_next:
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < data.size(); i++) {
                    MineOrderData d = data.get(i);
                    if (d.isChoose()) {
                        sb.append(d.getId()).append(",");
                    }
                    if (i == data.size() - 1) {
                        // 去除逗号
                        sb.delete(sb.length() - 1, sb.length());
                    }

                }
                Intent intent = new Intent(this, InvoiceInformationActivity.class);
                intent.putExtra("oid", sb.toString());
                startActivityForResult(intent, 0);

                break;

            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    getHttpUtils();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!cb_status) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setChoose(isChecked);
            }
            if (isChecked) {
                size = data.size();
                currentFee = totalFee;
                tv_invoice_price.setText(String.format(UIUtils.getString(R.string.how_much_money),
                                df.format(currentFee))
                );
            } else {
                size = 0;
                currentFee = 0;
                tv_invoice_price.setText(String.format(UIUtils.getString(R.string.how_much_money),
                        df.format(currentFee)));
            }
            adapter.notifyDataSetChanged();

            if (size == 0) {
                // 没一个选择，按钮不能点击
                btn_invoice_next
                        .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
                btn_invoice_next.setClickable(false);
            } else {
                btn_invoice_next.setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
                btn_invoice_next.setClickable(true);
            }

        }

    }

    // item内的cb点击
    @Override
    public void check(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            size++;
            currentFee += Double.valueOf(data.get(position).getReal_fee());
        } else {
            size--;
            currentFee -= Double.valueOf(data.get(position).getReal_fee());
        }
        data.get(position).setChoose(((CheckBox) view).isChecked());

        // 设置总价格
        tv_invoice_price.setText(String.format(UIUtils.getString(R.string.how_much_money), df.format(currentFee)));

        // 判断是否要全选
        cb_status = true;
        if (data.size() == size) {
            // 全选
            cb_invoice_choose_all.setChecked(true);
        } else {
            cb_invoice_choose_all.setChecked(false);
        }
        cb_status = false;

        // 设置按钮是否能点击
        if (size == 0) {
            // 没一个选择，按钮不能点击
            btn_invoice_next
                    .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
            btn_invoice_next.setClickable(false);
        } else {
            btn_invoice_next.setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
            btn_invoice_next.setClickable(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getHttpUtils();
            btn_invoice_next
                    .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
            btn_invoice_next.setClickable(false);
            size = 0;
            currentFee = 0;
            tv_invoice_price.setText(
                    String.format(UIUtils.getString(R.string.how_much_money),
                            df.format(currentFee)));
            cb_status = true;
            cb_invoice_choose_all.setChecked(false);
            cb_status = false;
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_invoice_order);
    }

}

package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
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
import com.yiju.ClassClockRoom.adapter.OrderDetailListAdapter;
import com.yiju.ClassClockRoom.bean.MineOrderData;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import java.util.ArrayList;

/**
 * 课室选择列表页(订单详情列表页)
 * 支付成功页点击课室布置跳转过来
 * Created by wh on 2016/5/13.
 */
public class OrderDetailListActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回按钮
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    /**
     * 保存按钮
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * 右上角文案
     */
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    @ViewInject(R.id.lv_order_detail_list)
    private ListView lv_order_detail_list;

    private OrderDetailListAdapter adapter;

    private String oid = "";
    private ArrayList<Order2> order2;


    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            oid = intent.getStringExtra("oid");
//            order2 = (ArrayList<Order2>) intent.getSerializableExtra("order2");
        }
        head_title.setText(R.string.title_classroom_choose);
        head_right_text.setText(R.string.order_detail);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
    }

    @Override
    protected void initData() {

//        if (order2 == null) {
//            return;
//        }
//        if (adapter == null) {
//            adapter = new OrderDetailListAdapter(order2);
//            lv_order_detail_list.setAdapter(adapter);
//        } else {
//            adapter.setOrder2s(order2);
//            adapter.notifyDataSetChanged();
//        }

        if (NetWorkUtils.getNetworkStatus(this)) {
            getHttpUtils();
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_classroom_choose);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_order_detail_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_relative://订单详情
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("oid", oid);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ClassroomArrangementActivity.RESULT_CODE_FROM_CLASSROOM_ARRANGEMENT_ACT)
            if (NetWorkUtils.getNetworkStatus(this)) {
                getHttpUtils();
            }
    }

    private void getHttpUtils() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_detail");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("level", "1");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ProgressDialog.getInstance().dismiss();
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {
        // <!-- 0未支付 1已支付 2过期未支付 3已支付退款 4已取消 5支付失败 status是状态 -->
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }
        if (mineOrder.getData() == null) {
            return;
        }
        MineOrderData data = mineOrder.getData().get(0);
        if (data == null) {
            return;
        }
        ArrayList<Order2> order2s = data.getOrder2();
        if (order2s == null) {
            return;
        }
        if (adapter == null) {
            adapter = new OrderDetailListAdapter(order2s);
            lv_order_detail_list.setAdapter(adapter);
        } else {
            adapter.setOrder2s(order2s);
            adapter.notifyDataSetChanged();
        }
    }

}

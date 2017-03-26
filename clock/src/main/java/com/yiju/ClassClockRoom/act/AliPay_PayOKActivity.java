package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.yiju.ClassClockRoom.bean.CartCommit;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;

public class AliPay_PayOKActivity extends BaseActivity implements
        OnClickListener {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //布置课室
    @ViewInject(R.id.btn_arrangement_classroom)
    private Button btn_arrangement_classroom;
    //发布课程
    @ViewInject(R.id.btn_continue_reserve)
    private Button btn_continue_reserve;
    //返回首页
    @ViewInject(R.id.tv_back_home)
    private TextView tv_back_home;
    private CartCommit info;
    private ArrayList<Order2> order2;


    @Override
    public int setContentViewId() {
        return R.layout.activity_alipayok;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        order2 = (ArrayList<Order2>) getIntent().getSerializableExtra("order2");
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        head_title.setText(UIUtils.getString(R.string.pay_result));
        // 加载数据
        Intent intent = getIntent();
        if (null != intent) {
            info = (CartCommit) intent
                    .getSerializableExtra("sucess");
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        btn_arrangement_classroom.setOnClickListener(this);
        btn_continue_reserve.setOnClickListener(this);
        tv_back_home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 处理点击事件
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                onBackPressed();
                break;
            case R.id.btn_arrangement_classroom://布置课室
                if (order2.size() > 1) {
                    //课室选择
                    Intent intentOrder = new Intent(this, OrderDetailListActivity.class);
                    intentOrder.putExtra("oid", info.getOrder1_id() + "");
                    startActivity(intentOrder);
                } else {
                    //课室布置
                    Intent intentOrder = new Intent(this, ClassroomArrangementActivity.class);
                    intentOrder.putExtra("order2", order2.get(0));
                    startActivityForResult(intentOrder, 1);
                }
                break;
            case R.id.tv_back_home://返回首页
                jumpIndex();
                break;
            case R.id.btn_continue_reserve://发布课程
                Intent intent_CourseReservation = new Intent(UIUtils.getContext(), PublishActivity.class);
                UIUtils.startActivity(intent_CourseReservation);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //课室布置返回要重新请求数据
        if (resultCode == ClassroomArrangementActivity.RESULT_CODE_FROM_CLASSROOM_ARRANGEMENT_ACT) {
            btn_arrangement_classroom.setClickable(false);
            getHttpUtils();
        }
    }


    /**
     * 订单详情接口请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "order_detail");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("oid", info.getOrder1_id() + "");
        params.addBodyParameter("level", "1");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        btn_arrangement_classroom.setClickable(true);
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
            ArrayList<Order2> o2 = mineOrder.getData().get(0).getOrder2();
            order2.clear();
            order2.addAll(o2);
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }
        btn_arrangement_classroom.setClickable(true);

    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishAllAndOpenHome(this, 3);
    }


    private void jumpIndex() {
        ActivityControlManager.getInstance().finishAllAndOpenHome(this, 0);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_pay_success);
    }

}

package com.yiju.ClassClockRoom.act;

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
import com.yiju.ClassClockRoom.adapter.PledgeAdapter;
import com.yiju.ClassClockRoom.bean.PledgeBean;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释: 押金详情页
 * <p>
 * 作者: cq
 * <p>
 * 时间: 2016/1/25 14:30
 * ----------------------------------------
 */
public class Pledge_Activity extends BaseActivity implements View.OnClickListener {

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
     * ListView
     */
    @ViewInject(R.id.lv_pledge)
    private ListView lv_pledge;

    /**
     * 全部学校id
     */
//    private String sids;

    /**
     * 适配器
     */
    private PledgeAdapter adapter;

    /**
     * 数据源
     */
    private List<PledgeBean.Data> datas;

    @Override
    public int setContentViewId() {
        return R.layout.activity_pledge;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.pledge_detail));
        //多个sid用逗号隔开(例：12,23,34)
//        sids = getIntent().getStringExtra("sid");
        head_back_relative.setOnClickListener(this);
        datas = new ArrayList<>();
        adapter = new PledgeAdapter(this, datas, R.layout.item_pledge_title);
        lv_pledge.setAdapter(adapter);
    }

    @Override
    public void initData() {
        getHttpUtils();
    }


    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "deposit_detail");
//        params.addBodyParameter("sid", sids);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
        PledgeBean pledgeBean = GsonTools.changeGsonToBean(result,
                PledgeBean.class);
        if (pledgeBean == null){
            return;
        }
        if ("1".equals(pledgeBean.getCode())) {
            datas.addAll(pledgeBean.getData());
            adapter.notifyDataSetChanged();
        } else {
            UIUtils.showToastSafe(pledgeBean.getMsg());
        }
    }


    @Override
    public String getPageName() {
        return getString(R.string.title_act_pledge);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
        }
    }
}

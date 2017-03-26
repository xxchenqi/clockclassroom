package com.yiju.ClassClockRoom.act;

import android.view.View;
import android.widget.Button;
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
import com.yiju.ClassClockRoom.adapter.AvailableWifiStoreAdapter;
import com.yiju.ClassClockRoom.bean.WifiStoreResult;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import java.util.List;

/**
 * 可用wifi门店列表页
 * Created by wh on 2016/11/9.
 */
public class AvailableWifiStoreActivity extends BaseActivity implements View.OnClickListener {
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //lv
    @ViewInject(R.id.lv_wifi_store)
    private ListView lv_wifi_store;
    //无WIFI显示界面
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    //刷新
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;


    @Override
    protected void initView() {
        head_title.setText(R.string.title_available_wifi_store_list);
    }

    @Override
    public void initListener() {
        btn_no_wifi_refresh.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (NetWorkUtils.getNetworkStatus(this)) {
            lv_wifi_store.setVisibility(View.VISIBLE);
            ly_wifi.setVisibility(View.GONE);
            getHttpRequest();
        } else {
            lv_wifi_store.setVisibility(View.GONE);
            ly_wifi.setVisibility(View.VISIBLE);
        }
    }

    private void getHttpRequest() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "wifi_school");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
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
                        ProgressDialog.getInstance().dismiss();
                    }


                }
        );
    }

    private void processData(String result) {
        WifiStoreResult wifiStoreResult = GsonTools.changeGsonToBean(result, WifiStoreResult.class);
        if (wifiStoreResult == null) {
            return;
        }

        if ("1".equals(wifiStoreResult.getCode())) {
            List<WifiStoreResult.DataEntity> dataEntities = wifiStoreResult.getData();
            AvailableWifiStoreAdapter adapter = new AvailableWifiStoreAdapter(
                    AvailableWifiStoreActivity.this,
                    dataEntities,
                    R.layout.item_available_wifi_store
            );
            lv_wifi_store.setAdapter(adapter);
        }

    }

    @Override
    public String getPageName() {
        return getString(R.string.page_name_available_wifi_store);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_available_wifi_store;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_wifi_store.setVisibility(View.VISIBLE);
                    getHttpRequest();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_wifi_store.setVisibility(View.GONE);
                }
                break;
        }
    }

}

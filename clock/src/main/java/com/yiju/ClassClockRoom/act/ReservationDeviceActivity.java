package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.DeviceTypeAdapter;
import com.yiju.ClassClockRoom.bean.ReservationBean.ReservationDevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReservationDeviceActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.tv_device_price)
    private TextView tv_device_price;

    @ViewInject(R.id.reservation_list)
    private ListView reservation_list;

    private DeviceTypeAdapter deviceTypeAdapter;
    private List<ReservationDevice> mDevices = new ArrayList<>();

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        head_title.setText(getString(R.string.reservation_device));
        tv_device_price.setText(getString(R.string.reservation_device_price_0));
        Intent mReservationIntent = getIntent();
        if (null != mReservationIntent) {
            List<ReservationDevice>  devices = (List<ReservationDevice>) mReservationIntent.getSerializableExtra("reservationDevices");
            List<ReservationDevice> haveDevices = (List<ReservationDevice>) mReservationIntent.getSerializableExtra("haveDevices");
            if (null != devices && devices.size()>0) {
                mDevices.clear();
                mDevices.addAll(devices);
            }

            if (null != haveDevices && haveDevices.size()>0) {
                mDevices.clear();
                mDevices.addAll(haveDevices);
            }
        }
        showDevice();
        changePrice();
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation_device);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation_device;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
        }
    }

    /**
     * 显示设备列表
     */
    private void showDevice() {
        if (null != mDevices && mDevices.size() > 0) {
            if (deviceTypeAdapter == null) {
                deviceTypeAdapter = new DeviceTypeAdapter(this, mDevices, tv_device_price);
                reservation_list.setAdapter(deviceTypeAdapter);
            } else {
                deviceTypeAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 返回数据
     */
    private void backReservation() {
        int DEVICE = 3;
        Intent intent = new Intent();
        if (null != mDevices && mDevices.size()>0) {
            intent.putExtra("haveDevices", (Serializable) mDevices);
        }
        setResult(DEVICE, intent);
        finish();
    }

    private void changePrice() {
        int price = 0;
        for (ReservationDevice info : mDevices) {
            price += (info.getCount() * Double.valueOf(info.getFee()));
        }
        tv_device_price.setText("￥" + price);
    }

    @Override
    public void onBackPressed() {
        backReservation();
        super.onBackPressed();
    }
}

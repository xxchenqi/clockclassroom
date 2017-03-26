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
import com.yiju.ClassClockRoom.bean.DeviceEntity;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.ReservationBean;

import java.util.List;

public class ReservationDeviceActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    @ViewInject(R.id.reservation_list)
    private ListView reservation_list;

    private DeviceTypeAdapter deviceTypeAdapter;
    private List<ReservationBean.ReservationDevice> devices;
    private Order2 info;
    private ReservationBean reservationBean;

    /**
     * 初始化页面
     */
    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        head_title.setText(getString(R.string.reservation_device));
        head_right_text.setText(getString(R.string.label_save));
        Intent mReservationIntent = getIntent();
        if (null != mReservationIntent) {
            info = (Order2) mReservationIntent.getSerializableExtra("info");
        }else{
            return;
        }
        reservationBean = (ReservationBean) mReservationIntent.getSerializableExtra("reservationBean");
        ReservationBean reservationHadBean = (ReservationBean) mReservationIntent.getSerializableExtra("reservationHadBean");
        if (null != reservationBean && null == reservationHadBean) {
            devices = reservationBean.getDevice();
        } else if (null != reservationBean && null != info) {
            devices = reservationBean.getDevice();
            List<DeviceEntity> infoDevices = info.getDevice_nofree();
            for (int i = 0; i < devices.size(); i++) {
                devices.get(i).setCount(Integer.valueOf(infoDevices.get(i).getNum()));
            }
        } else if(null != reservationBean){
            devices = reservationHadBean.getDevice();
        }
        showDevice();
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
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.head_right_text:
                backReservation();
                break;
        }
    }
    /**
     * 显示设备列表
     */
    private void showDevice() {
        if (null != devices && devices.size() > 0) {
            if (deviceTypeAdapter == null) {
                deviceTypeAdapter = new DeviceTypeAdapter(this, devices);
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
        if(null != reservationBean){
            reservationBean.setDevice(devices);
            intent.putExtra("reservationHaveBean", reservationBean);
        }
        setResult(DEVICE, intent);
        finish();
    }
}

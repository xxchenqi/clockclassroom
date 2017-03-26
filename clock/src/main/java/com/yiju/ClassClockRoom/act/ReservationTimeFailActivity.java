package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.TimeFailAdapter;
import com.yiju.ClassClockRoom.bean.DataAlone;
import com.yiju.ClassClockRoom.bean.ReservationFailTimeData;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;

import java.util.ArrayList;

public class ReservationTimeFailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;

    @ViewInject(R.id.tv_time_fail_title)
    private TextView tv_time_fail_title;

    @ViewInject(R.id.time_fail_list)
    private ListView time_fail_list;
    private ArrayList<DataAlone> mLists;
    private TimeFailAdapter timeFailAdapter;
    private int mPosition = Integer.MAX_VALUE;

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

        head_title.setText(getString(R.string.reservation_time_fail));
        head_right_text.setText(getString(R.string.label_save));
        Intent intent = getIntent();
        String result = intent.getStringExtra("TIMEFAIL");
        String title = intent.getStringExtra("title");
        if (null == result) {
            return;
        }
        String[] titles = title.split("-");
        tv_time_fail_title.setText(titles[0] + "年" + titles[1] + "月" + titles[2] + "日");
        mLists = new ArrayList<>();
        ReservationFailTimeData reservationFailTimeData = GsonTools.changeGsonToBean(result, ReservationFailTimeData.class);
        if (null != reservationFailTimeData) {
            if (reservationFailTimeData.getCode() == 1) {
                ReservationFailTimeData.DataPreEntity data_pre = reservationFailTimeData.getData_pre();
                ReservationFailTimeData.DataAfterEntity data_after = reservationFailTimeData.getData_after();
                if (null != data_pre && null != data_after) {
                    mLists.clear();
                    if (data_pre.getFlag() == 1) {
                        DataAlone dataAlone = new DataAlone();
                        String startTime = StringUtils.changeTime(data_pre.getStart_time().trim());
                        String endTime = StringUtils.changeTime(data_pre.getEnd_time().trim());
                        String time = String.format(getString(R.string.to_symbol), startTime, endTime);
                        dataAlone.setListdata(time);
                        mLists.add(dataAlone);
                    }
                    if (data_after.getFlag() == 1) {
                        DataAlone dataAlone = new DataAlone();
                        String startTime = StringUtils.changeTime(data_after.getStart_time().trim());
                        String endTime = StringUtils.changeTime(data_after.getEnd_time().trim());
                        String time = String.format(getString(R.string.to_symbol), startTime, endTime);
                        dataAlone.setListdata(time);
                        mLists.add(dataAlone);
                    }
                }
            }
        }
        if (null != mLists && mLists.size() > 0) {
            if (null != timeFailAdapter) {
                timeFailAdapter.notifyDataSetChanged();
            } else {
                timeFailAdapter = new TimeFailAdapter(mLists);
                time_fail_list.setAdapter(timeFailAdapter);
            }
        }
        time_fail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                timeFailAdapter.setPosition(position);
                timeFailAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_reservation_time_fail);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_reservation_time_fail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.head_right_text:
                String chooseTime = null;
                int TIMEFAIL = 1;
                if (mPosition != Integer.MAX_VALUE) {
                    chooseTime = mLists.get(mPosition).getListdata();
                }
                Intent intent = new Intent();
                intent.putExtra("chooseTime", chooseTime);
                setResult(TIMEFAIL, intent);
                finish();
                break;
            default:
                break;
        }
    }
}

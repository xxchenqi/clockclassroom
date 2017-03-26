package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseFragmentActivity;
import com.yiju.ClassClockRoom.adapter.ClassRoomTypePagerAdapter;
import com.yiju.ClassClockRoom.bean.ClassroomArrangeData;
import com.yiju.ClassClockRoom.bean.result.ClassroomArrangeResult;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;
import com.yiju.ClassClockRoom.fragment.ClassRoomUseNameFragment;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 课室用途选择页_大类(ex:文化教学)
 * Created by wh on 2016/5/16.
 */
public class ClassRoomTypeActivity extends BaseFragmentActivity {

    private RelativeLayout head_back_relative;
    private TextView head_title;
    private TabLayout tl_tabs;
    private ViewPager vp_viewpager;

    private ArrayList<Fragment> fragments;
    private ArrayList<ClassroomArrangeData> classroom_type;
    private String use_name = "";
    private String order2_id = "";
    private int puse_id;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_classroomtype);
        Intent intent = getIntent();
        if (intent != null) {
            classroom_type = (ArrayList<ClassroomArrangeData>) intent.getSerializableExtra("classroom_type");
            use_name = intent.getStringExtra("use_name");
            puse_id = intent.getIntExtra("puse_id", 0);
            order2_id = intent.getStringExtra("order2_id");
        }

        initView();
        initData();
    }

    protected void initView() {
        head_back_relative = (RelativeLayout) findViewById(R.id.head_back_relative);
        head_title = (TextView) findViewById(R.id.head_title);
        tl_tabs = (TabLayout) findViewById(R.id.tl_tabs);
        vp_viewpager = (ViewPager) findViewById(R.id.vp_viewpager);

        //返回按钮
        head_back_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initData() {
        head_title.setText(R.string.title_class_room_type);
        if (classroom_type == null || classroom_type.size() == 0) {
            //重新请求
            getRequestClassroomArrange();
            return;
        }
        handleData();
    }

    public void handleData() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        } else {
            fragments.clear();
        }
        int currentPos = 0;
        for (int i = 0; i < classroom_type.size(); i++) {
            ClassroomArrangeData classroomArrangeData = classroom_type.get(i);
            if (classroomArrangeData.getPuse_id() == puse_id) {
                currentPos = i;
            }
            Fragment fragment = new ClassRoomUseNameFragment();
            Bundle bundle = new Bundle();
            bundle.putString("use_name", use_name);
            bundle.putInt("puse_id", classroomArrangeData.getPuse_id());
            bundle.putSerializable("use", (Serializable) classroomArrangeData.getUse());
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        vp_viewpager.setAdapter(
                new ClassRoomTypePagerAdapter(
                        getSupportFragmentManager(),
                        fragments,
                        classroom_type
                ));

        vp_viewpager.setCurrentItem(currentPos);
        tl_tabs.setupWithViewPager(vp_viewpager);
        tl_tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 请求初始课室布置
     */
    public void getRequestClassroomArrange() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "classroom_arrange");
        params.addBodyParameter("order2_id", order2_id);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_CLASSROOM_ARRANGE, params,
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
                }
        );
    }

    private void processData(String result) {
        ClassroomArrangeResult classroomArrangeResult = GsonTools.changeGsonToBean(
                result,
                ClassroomArrangeResult.class
        );
        if (classroomArrangeResult == null) {
            return;
        }

        if ("1".equals(classroomArrangeResult.getCode())) {
            classroom_type = classroomArrangeResult.getData();//课室用途
            handleData();
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_room_type);
    }
}

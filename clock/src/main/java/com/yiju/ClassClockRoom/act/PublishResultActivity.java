package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * ----------------------------------------
 * 注释:发布结果页
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/7/6 09:47
 * ----------------------------------------
 */
public class PublishResultActivity extends BaseActivity implements View.OnClickListener {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.publish_course_result));
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_publish_result_Page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_publish_result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intentIndex = new Intent(this, MainActivity.class);
//        intentIndex.putExtra("person", "3");
        startActivity(intentIndex);
    }
}

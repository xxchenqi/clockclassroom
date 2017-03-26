package com.yiju.ClassClockRoom.act;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/22 17:02
 * ----------------------------------------
 */
public class TeacherInformationFailActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    @ViewInject(R.id.head_title)
    private TextView head_title;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.teacher_detail));
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_teacher_detail_fail);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_teacher_information_fail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
        }
    }
}

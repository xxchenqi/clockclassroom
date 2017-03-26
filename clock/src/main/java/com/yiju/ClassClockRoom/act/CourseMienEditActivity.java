package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.ZoomImageView;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

/**
 * 课程风采查看和删除界面
 * Created by wh on 2016/7/6.
 */
public class CourseMienEditActivity extends BaseActivity implements View.OnClickListener {
    //后退
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //分割线
    @ViewInject(R.id.head_divider)
    private View head_divider;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //删除
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //图片
    @ViewInject(R.id.iv_zoom_image)
    private ZoomImageView iv_zoom_image;
    //删除按钮
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //标题中间
    @ViewInject(R.id.head_center_relative)
    private RelativeLayout head_center_relative;
    //course_url
    private String course_url;
    //位置
    private int pos;
    //图片拼接
    private StringBuilder imgs;
    //风采照片删除
    public static int RESULT_CODE_FROM_COURSE_MIEN_EDIT_DELETE_ACT = 1003;

    @Override
    public void initIntent() {
        super.initIntent();
        course_url = getIntent().getStringExtra("course_url");
        pos = getIntent().getIntExtra("pos", -1);
    }

    @Override
    protected void initView() {
        head_title.setText(R.string.txt_course_mine);
        head_right_text.setText(UIUtils.getString(R.string.delete));
        head_divider.setVisibility(View.GONE);
        head_back_relative.setBackgroundColor(Color.BLACK);
        head_right_relative.setBackgroundColor(Color.BLACK);
        head_center_relative.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (StringUtils.isNotNullString(course_url)) {
            Glide.with(mContext).load(course_url).into(iv_zoom_image);
        }
    }

    @Override
    public String getPageName() {
        return UIUtils.getString(R.string.txt_course_mine);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_mien_edit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_text://删除
                CustomDialog customDialog = new CustomDialog(
                        CourseMienEditActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        UIUtils.getString(R.string.dialog_msg_delete_course_mine));
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            //删除
                            Intent intent = new Intent();
                            intent.putExtra("pos", pos);
                            setResult(RESULT_CODE_FROM_COURSE_MIEN_EDIT_DELETE_ACT, intent);
                            finish();
                        }
                    }
                });
                break;
        }
    }


}

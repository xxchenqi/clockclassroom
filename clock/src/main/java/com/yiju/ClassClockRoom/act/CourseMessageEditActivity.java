package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.widget.edittext.AmountEditText;
import com.yiju.ClassClockRoom.widget.edittext.ContainsEmojiEditText;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * 课程信息编辑页
 * Created by wh on 2016/7/6.
 */
public class CourseMessageEditActivity extends BaseActivity implements View.OnClickListener {
    public static final String RESULT_DATA_CONTEXT = "context";
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
     * 保存
     */
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    /**
     * 保存布局
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * 课程名称
     */
    @ViewInject(R.id.et_course_name)
    private ContainsEmojiEditText et_course_name;
    /**
     * 课程单价_布局
     */
    @ViewInject(R.id.ll_course_price)
    private LinearLayout ll_course_price;
    /**
     * 课程单价
     */
    @ViewInject(R.id.et_course_price)
    private AmountEditText et_course_price;
    /**
     * 上课人数_布局
     */
    @ViewInject(R.id.ll_course_people_num)
    private LinearLayout ll_course_people_num;
    /**
     * 剩余人数
     */
    @ViewInject(R.id.et_course_people_remain_num)
    private EditText et_course_people_remain_num;
    /**
     * 总共人数
     */
    @ViewInject(R.id.et_course_people_total_num)
    private EditText et_course_people_total_num;
    /**
     * 课程描述_布局
     */
    @ViewInject(R.id.rl_course_desc)
    private RelativeLayout rl_course_desc;
    /**
     * 课程描述
     */
    @ViewInject(R.id.et_course_desc)
    private ContainsEmojiEditText et_course_desc;
    /**
     * 课程描述字数变化
     */
    @ViewInject(R.id.tv_txt_count)
    private TextView tv_txt_count;


    private String course_tag;
    private String show_content;

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent != null) {
            course_tag = intent.getStringExtra(PublishCourseSecondActivity.COURSE_TAG);
            show_content = intent.getStringExtra("show_content");
        }
    }

    @Override
    protected void initView() {
        head_right_text.setText(UIUtils.getString(R.string.label_save));
        if (StringUtils.isNullString(course_tag)) {
            return;
        }
        if (PublishCourseSecondActivity.COURSE_TAG_NAME.equals(course_tag)) {
            head_title.setText(R.string.txt_course_name);
            if (StringUtils.isNotNullString(show_content)) {
                et_course_name.setText(show_content);
                et_course_name.setSelection(show_content.length());
            }
            et_course_name.setVisibility(View.VISIBLE);
            ll_course_price.setVisibility(View.GONE);
            ll_course_people_num.setVisibility(View.GONE);
            rl_course_desc.setVisibility(View.GONE);
        } else if (PublishCourseSecondActivity.COURSE_TAG_PRICE.equals(course_tag)) {
            head_title.setText(R.string.txt_course_price);
            if (StringUtils.isNotNullString(show_content)) {
                et_course_price.setText(show_content.replaceAll("¥", "").replaceAll("/小时", ""));
                et_course_price.setSelection(et_course_price.getText().toString().length());
            }
            et_course_name.setVisibility(View.GONE);
            ll_course_price.setVisibility(View.VISIBLE);
            ll_course_people_num.setVisibility(View.GONE);
            rl_course_desc.setVisibility(View.GONE);
        } else if (PublishCourseSecondActivity.COURSE_TAG_NUM.equals(course_tag)) {
            head_title.setText(R.string.txt_course_people_num);
            if (StringUtils.isNotNullString(show_content)) {
                if (show_content.split("/").length == 2) {
                    if (StringUtils.isNotNullString(show_content.split("/")[0])) {
                        et_course_people_remain_num.setText(show_content.split("/")[0]);
                        et_course_people_remain_num.setSelection(show_content.split("/")[0].length());
                    }
                    if (StringUtils.isNotNullString(show_content.split("/")[1])) {
                        et_course_people_total_num.setText(show_content.split("/")[1]);
                        et_course_people_total_num.setSelection(show_content.split("/")[1].length());
                    }
                }
            }
            et_course_name.setVisibility(View.GONE);
            ll_course_price.setVisibility(View.GONE);
            ll_course_people_num.setVisibility(View.VISIBLE);
            rl_course_desc.setVisibility(View.GONE);
        } else if (PublishCourseSecondActivity.COURSE_TAG_DESC.equals(course_tag)) {
            head_title.setText(R.string.txt_course_desc);
            if (StringUtils.isNotNullString(show_content)) {
                et_course_desc.setText(show_content);
                et_course_desc.setSelection(show_content.length());
            }
            et_course_name.setVisibility(View.GONE);
            ll_course_price.setVisibility(View.GONE);
            ll_course_people_num.setVisibility(View.GONE);
            rl_course_desc.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isNotNullString(et_course_desc.getText().toString())) {
            tv_txt_count.setText(1000 - et_course_desc.getText().toString().trim().length() + "");
        } else {
            tv_txt_count.setText(1000 + "");
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        /*InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    int destLen = dest.toString().getBytes("GB18030").length;
                    int sourceLen = source.toString().getBytes("GB18030").length;
                    if (destLen + sourceLen > 40) {
                        UIUtils.showToastSafe(getString(R.string.toast_course_name_length));
                        return "";
                    }
                    if (source.length() < 1 && (dend - dstart >= 1)) {
                        return dest.subSequence(dstart, dend - 1);
                    }
                    return source;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
        et_course_name.setFilters(new InputFilter[]{inputFilter});*/

        et_course_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() <= 1000) {
                    tv_txt_count.setText(1000 - s.toString().length() + "");
                } else {
                    UIUtils.showToastSafe(getString(R.string.toast_show_cannot_exceed_maximum_length_limit));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public String getPageName() {
        if (PublishCourseSecondActivity.COURSE_TAG_NAME.equals(course_tag)) {
            //课程名称
            return getString(R.string.title_act_publish_second_course_name_Page);
        } else if (PublishCourseSecondActivity.COURSE_TAG_PRICE.equals(course_tag)) {
            //价格
            return getString(R.string.title_act_publish_second_course_price_Page);
        } else if (PublishCourseSecondActivity.COURSE_TAG_NUM.equals(course_tag)) {
            //人数
            return getString(R.string.title_act_publish_second_course_number_Page);
        } else if (PublishCourseSecondActivity.COURSE_TAG_DESC.equals(course_tag)) {
            //课程描述
            return getString(R.string.title_act_publish_second_course_info_Page);
        }
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_message_edit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_relative://保存
                String context = "";
                if (PublishCourseSecondActivity.COURSE_TAG_NAME.equals(course_tag)) {
                    if (StringUtils.isNullString(et_course_name.getText().toString())) {
                        UIUtils.showToastSafe(getString(R.string.toast_course_name_null));
                        return;
                    }
                    context = et_course_name.getText().toString();
                } else if (PublishCourseSecondActivity.COURSE_TAG_PRICE.equals(course_tag)) {
                    if (StringUtils.isNullString(et_course_price.getText().toString())) {
                        UIUtils.showToastSafe(getString(R.string.toast_course_price_null));
                        return;
                    }
                    context = et_course_price.getText().toString();
                } else if (PublishCourseSecondActivity.COURSE_TAG_NUM.equals(course_tag)) {
                    if (StringUtils.isNullString(et_course_people_remain_num.getText().toString())
                            || StringUtils.isNullString(et_course_people_total_num.getText().toString())) {
                        UIUtils.showToastSafe(getString(R.string.toast_course_people_num_null));
                        return;
                    }
                    if ("0".equals(et_course_people_total_num.getText().toString())) {
                        UIUtils.showToastSafe(getString(R.string.toast_course_people_total_num_zero));
                        return;
                    }
                    int remain_num = Integer.parseInt(et_course_people_remain_num.getText().toString().trim());
                    int total_num = Integer.parseInt(et_course_people_total_num.getText().toString().trim());
                    if (remain_num > total_num) {
                        UIUtils.showToastSafe(getString(R.string.toast_course_people_num_compare));
                        return;
                    }
                    context = et_course_people_remain_num.getText().toString()
                            + "/" + et_course_people_total_num.getText().toString();
                } else if (PublishCourseSecondActivity.COURSE_TAG_DESC.equals(course_tag)) {
                    if (StringUtils.isNullString(et_course_desc.getText().toString())) {
                        UIUtils.showToastSafe(getString(R.string.toast_course_desc_null));
                        return;
                    }
                    context = et_course_desc.getText().toString();
                }
                Intent intent = new Intent();
                intent.putExtra(PublishCourseSecondActivity.COURSE_TAG, course_tag);
                intent.putExtra(RESULT_DATA_CONTEXT, context);
                setResult(1, intent);
                finish();
                break;
        }
    }
}

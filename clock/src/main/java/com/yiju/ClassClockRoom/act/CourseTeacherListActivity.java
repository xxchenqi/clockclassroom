package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.yiju.ClassClockRoom.adapter.CourseTeacherAdapter;
import com.yiju.ClassClockRoom.bean.SchoolTeacherListResult;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.List;

/*
 * 发布课程_授课老师列表
 * Created by wh on 2016/7/6.
 */
public class CourseTeacherListActivity extends BaseActivity
        implements View.OnClickListener {
    //后退按钮
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //不选择老师
    @ViewInject(R.id.rl_no_teacher)
    private RelativeLayout rl_no_teacher;
    @ViewInject(R.id.iv_no_teacher)
    private ImageView iv_no_teacher;
    //列表
    @ViewInject(R.id.lv_teacher_list)
    private ListView lv_teacher_list;
    //适配器
    private CourseTeacherAdapter adapter;
    //数据
    private List<SchoolTeacherListResult.SchoolTeacherBean.ListEntity> datas;
    //选择位置

    //    private String teacherName = "";
    private String teacher_uid = "";
    private boolean no_choose_teacher;


    @Override
    public void initIntent() {
        super.initIntent();
//        datas = (List<SchoolTeacherListResult.SchoolTeacherBean.ListEntity>) getIntent().getSerializableExtra("teacher_list");
//        teacherName = getIntent().getStringExtra("teacher_name");
        teacher_uid = getIntent().getStringExtra("teacher_uid");
        no_choose_teacher = getIntent().getBooleanExtra("no_choose_teacher", false);
    }

    @Override
    protected void initView() {
        head_title.setText(R.string.title_the_instructor);
        if (no_choose_teacher) {
            iv_no_teacher.setBackgroundResource(R.drawable.check);
        } else {
            iv_no_teacher.setBackgroundResource(R.drawable.uncheck);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        rl_no_teacher.setOnClickListener(this);

        lv_teacher_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datas == null) {
                    return;
                }
                if (!"2".equals(datas.get(position).getIs_verify())) {
                    UIUtils.showToastSafe(getString(R.string.txt_course_teacher_explain));
                    return;
                }
                if ("0".equals(datas.get(position).getFullteacherinfo())) {
                    UIUtils.showToastSafe(getString(R.string.txt_course_teacher_explain));
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("teacher_name", datas.get(position).getReal_name());
                intent.putExtra("teacher_uid", datas.get(position).getUid());
                setResult(2, intent);
                finish();

            }
        });
    }

    @Override
    protected void initData() {
        getHttpForMembers();
    }

    /**
     * 成员列表请求
     */
    private void getHttpForMembers() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_school_teaching");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForMembers(arg0.result);
                    }
                });
    }

    private void processDataForMembers(String result) {
        SchoolTeacherListResult memberBean = GsonTools.changeGsonToBean(result,
                SchoolTeacherListResult.class);
        if (memberBean == null) {
            return;
        }
        if ("1".equals(memberBean.getCode())) {
            datas = memberBean.getData().getList();
            if (datas == null) {
                return;
            }
            for (int i = 0; i < datas.size(); i++) {
                if (StringUtils.isNotNullString(teacher_uid)) {
                    if (teacher_uid.equals(datas.get(i).getUid())) {
                        datas.get(i).setCheck(true);
                    } else {
                        datas.get(i).setCheck(false);
                    }
                }
            }
            adapter = new CourseTeacherAdapter(
                    this, datas, R.layout.item_course_teacher);
            lv_teacher_list.setAdapter(adapter);
        } else {
            UIUtils.showToastSafe(memberBean.getMsg());
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_publish_second_course_teacher_choose_Page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_teacher_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.rl_no_teacher://不选择老师按钮
                Intent intent = new Intent();
                intent.putExtra("no_choose_teacher", true);
                setResult(2, intent);
                finish();
                break;

        }
    }
}

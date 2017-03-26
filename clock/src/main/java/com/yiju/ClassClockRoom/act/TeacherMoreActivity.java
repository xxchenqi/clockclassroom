package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.TeacherAdapter;
import com.yiju.ClassClockRoom.bean.TeacherInfoBean;
import com.yiju.ClassClockRoom.bean.TeacherMoreData;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;


public class TeacherMoreActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static String ORGANIZATION_TYPE = "1";
    public static String PERSONAL_TYPE = "2";
    public static final String ACTION_TYPE = "type";
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //lv
    @ViewInject(R.id.lv_teacher_more)
    private PullToRefreshListView lv_teacher_more;
    //适配器
    private TeacherAdapter teacherAdapter;
    //数据源
    private List<Object> mTeachers = new ArrayList<>();
    //类型
    private String type;
    //下拉刷新标志，默认为下来刷新
    private boolean flag_down = true;

    private int limitStart = 0;
    private int limitEnd = 5;

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        type = intent.getStringExtra(ACTION_TYPE);
    }

    @Override
    protected void initView() {
        teacherAdapter = new TeacherAdapter(mTeachers);
        lv_teacher_more.setAdapter(teacherAdapter);
        lv_teacher_more.setMode(PullToRefreshBase.Mode.BOTH);
        lv_teacher_more.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = true;
                limitStart = 0;
                limitEnd = 5;
                getData();
                lv_teacher_more.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = false;
                limitStart += 5;
                limitEnd = 5;
                getData();
            }
        });

    }

    @Override
    protected void initData() {
        if (ORGANIZATION_TYPE.equals(type)) {
            head_title.setText(getString(R.string.more_teacher_orangan));
        } else {
            head_title.setText(getString(R.string.more_teacher_person));
        }
        getData();
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        lv_teacher_more.setOnItemClickListener(this);
    }

    /**
     * 获取数据
     */
    private void getData() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
//        params.addBodyParameter("action", "get_teacher_list");
        params.addBodyParameter("type", type);
        params.addBodyParameter("limit", limitStart + "," + limitEnd);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_TEACHER_MORE_JAVA, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                });
    }

    /**
     * 处理返回的数据
     *
     * @param result 结果集
     */
    private void processData(String result) {
        if (null != result) {
            TeacherMoreData teacherMoredata = GsonTools.changeGsonToBean(result, TeacherMoreData.class);
            if (null != teacherMoredata) {
                if (teacherMoredata.getCode() == 0) {
                    List<TeacherInfoBean> teachers = teacherMoredata.getObj();
                    if (null != teachers && teachers.size() > 0) {
                        if (flag_down) {
                            mTeachers.clear();
                        }
                        mTeachers.addAll(teachers);
                        teacherAdapter.notifyDataSetChanged();
                        lv_teacher_more.onRefreshComplete();
                    } else {
                        if (!flag_down) {
                            lv_teacher_more.onRefreshComplete();
                            lv_teacher_more.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }
                }
            }
        }
    }


    @Override
    public String getPageName() {
        if (ORGANIZATION_TYPE.equals(type)) {
            //机构
            return getString(R.string.title_act_teacher_more_organization_Page);
        } else {
            //个人
            return getString(R.string.title_act_teacher_more_person_Page);
        }
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_teacher_more;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = mTeachers.get(position - 1);
        if (o instanceof TeacherInfoBean) {
            Intent intent = new Intent(this, TeacherDetailActivity.class);
            intent.putExtra("id", ((TeacherInfoBean) o).getId());
            startActivity(intent);
        }
    }
}

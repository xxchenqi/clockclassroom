package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.yiju.ClassClockRoom.adapter.PeopleApplyAdapter;
import com.yiju.ClassClockRoom.bean.Course_Person_People_Apply;
import com.yiju.ClassClockRoom.bean.People_Apply_Courselist;
import com.yiju.ClassClockRoom.bean.People_Apply_Data;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandy on 2016/6/27/0027.
 */
public class PersonMineCoursePeopleCountActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView headBack;

    @ViewInject(R.id.head_title)
    private TextView headTitle;

    @ViewInject(R.id.list_person_mine_course_people_apply)
    private PullToRefreshListView listPersonMineCoursePeopleApply;
    private String course_id;
    private int limitStart = 0;
    private int limitEnd = 5;
    private int limitCount = 5;
    private boolean flag_down = true;
    private List<People_Apply_Courselist> mLists = new ArrayList<>();
    private PeopleApplyAdapter peopleApplyAdapter;

    @Override
    protected void initView() {
        headBack.setOnClickListener(this);
        listPersonMineCoursePeopleApply.setMode(PullToRefreshBase.Mode.BOTH);
        listPersonMineCoursePeopleApply.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = true;
                limitStart = 0;
                limitEnd = 5;
                getData(limitStart, limitEnd);
                listPersonMineCoursePeopleApply.setMode(PullToRefreshBase.Mode.BOTH);
                peopleApplyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = false;
                limitStart += limitCount;
                limitEnd = limitCount;
                getData(limitStart, limitEnd);
                peopleApplyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if (null != intent) {
            course_id = intent.getStringExtra("COURSE_ID");
            if (null != course_id && !"".equals(course_id)) {
                getData(limitStart, limitEnd);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据
     *
     * @param limitStart 开始位置
     * @param limitEnd   分页条数
     */
    public void getData(int limitStart, int limitEnd) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_course_signup_list");
        params.addBodyParameter("id", course_id);
        params.addBodyParameter("limit", limitStart + "," + limitEnd);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
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
            Course_Person_People_Apply course_person_people_apply = GsonTools.changeGsonToBean(result, Course_Person_People_Apply.class);
            if (null != course_person_people_apply) {
                if (course_person_people_apply.getCode() == 1) {
                    People_Apply_Data data = course_person_people_apply.getData();
                    if(null != data){
                        String have_enroll = data.getHave_enroll();
                        String remain_count = data.getRemain_count();
                        if (null != have_enroll && !"".equals(have_enroll)&&
                        null != remain_count && !"".equals(remain_count)) {
                            headTitle.setText("报名(" + have_enroll+"/"+(
                                    Integer.valueOf(have_enroll)+Integer.valueOf(remain_count)
                                    ) + ")");
                        }
                        List<People_Apply_Courselist> courselist = data.getCourselist();
                        if (null != courselist && courselist.size() > 0) {
                            if (flag_down) {
                                mLists.clear();
                            }
                            mLists.addAll(courselist);
                            if (courselist.size() < limitCount) {
                                listPersonMineCoursePeopleApply.onRefreshComplete();
                                listPersonMineCoursePeopleApply.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                            showCoursePeopleApplyList();
                        } else {
                            if (!flag_down) {
                                listPersonMineCoursePeopleApply.onRefreshComplete();
                                listPersonMineCoursePeopleApply.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 展示列表
     */
    private void showCoursePeopleApplyList() {

        if(null != peopleApplyAdapter){
            peopleApplyAdapter.notifyDataSetChanged();
        }else {
            peopleApplyAdapter = new PeopleApplyAdapter(mLists);
            listPersonMineCoursePeopleApply.setAdapter(peopleApplyAdapter);
        }
        listPersonMineCoursePeopleApply.onRefreshComplete();

    }

    @Override
    public String getPageName() {
        return getString(R.string.person_mine_course_people_apply_count_page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_person_course_people_apply;
    }

}

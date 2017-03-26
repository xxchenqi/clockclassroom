package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.yiju.ClassClockRoom.adapter.CourseMineAdapter;
import com.yiju.ClassClockRoom.bean.Course_DataInfo;
import com.yiju.ClassClockRoom.bean.Course_Edit_Info;
import com.yiju.ClassClockRoom.bean.Course_Person;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.callback.ListItemClickListener;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * 我发布的课程列表页
 * Created by Sandy on 2016/6/16/0016.
 */
public class PersonMineCourseActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)
    private ImageView head_back;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.wifi)
    private RelativeLayout wifi;

    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;

    @ViewInject(R.id.list_person_mine_course)
    private PullToRefreshListView list_person_mine_course;
    //空课程界面
    @ViewInject(R.id.iv_empty_course)
    private ImageView iv_empty_course;

    private final int COURSE_DELETE = 1;
    private final int COURSE_EDIT = 2;
    private final int COURSE_FINISH = 3;
    private final int COURSE_CANCEL = 4;
    private final int COURSE_BACK = 100;
    private final String CANCEL = "3";
    private final String APPLY_FINISH = "6";
    private int limitStart = 0;
    private int limitEnd = 5;
    private int limitCount = 5;
    private boolean flag_down = true;
    private String uid;
    private CourseMineAdapter courseMineAdapter;
    private List<Course_DataInfo> mLists = new ArrayList<>();
    private Course_Person coursePerson;

    @Override
    protected void initView() {

        head_back.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        list_person_mine_course.setMode(PullToRefreshBase.Mode.BOTH);
        list_person_mine_course.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = true;
                limitStart = 0;
                limitEnd = 5;
                getData(limitStart, limitEnd);
                list_person_mine_course.setMode(PullToRefreshBase.Mode.BOTH);
                refreshList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = false;
                limitStart += limitCount;
                limitEnd = limitCount;
                getData(limitStart, limitEnd);
                refreshList();
            }
        });
        list_person_mine_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                jumpPersonMineCourseDetial(position - 1);
            }
        });
    }

    private void refreshList() {
        if (null != courseMineAdapter) {
            courseMineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {
        uid = SharedPreferencesUtils.getString(this,
                getResources().getString(R.string.shared_id), null);
        head_title.setText(getString(R.string.person_mine_course));
        if (NetWorkUtils.getNetworkStatus(this)) {
            wifi.setVisibility(View.GONE);
            getData(limitStart, limitEnd);
        } else {
            wifi.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取数据
     *
     * @param limitStart 开始位置
     * @param limitEnd   分页条数
     */
    private void getData(int limitStart, int limitEnd) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_my_course_list");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("limit", limitStart + "," + limitEnd);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
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
            coursePerson = GsonTools.changeGsonToBean(result, Course_Person.class);
            if (null != coursePerson) {
                if (coursePerson.getCode() == 1) {
                    List<Course_DataInfo> courseDataInfos = coursePerson.getData();
                    if (null != courseDataInfos && courseDataInfos.size() > 0) {
                        if (flag_down) {
                            mLists.clear();
                        }
                        mLists.addAll(courseDataInfos);
                        if (courseDataInfos.size() < limitCount) {
                            list_person_mine_course.onRefreshComplete();
                            list_person_mine_course.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        showCourseMineList();
                    } else {
                        if (flag_down) {
                            iv_empty_course.setVisibility(View.VISIBLE);
                        }
                        if (!flag_down) {
                            list_person_mine_course.onRefreshComplete();
                            list_person_mine_course.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }
                }
            }
        }
    }

    /**
     * 展示课程列表
     */
    private void showCourseMineList() {
        if (null != courseMineAdapter) {
            courseMineAdapter.notifyDataSetChanged();
        } else {
            courseMineAdapter = new CourseMineAdapter(UIUtils.getContext(), mLists, new ListItemClickListener() {
                @Override
                public void onClickItem(int position, int type) {
                    parserOnClickEvent(position, type);
                }
            });
            list_person_mine_course.setAdapter(courseMineAdapter);
        }
        list_person_mine_course.onRefreshComplete();
    }

    /**
     * 跳往详情页面
     *
     * @param position 点击的位置
     */
    private void jumpPersonMineCourseDetial(int position) {
        Intent intent = new Intent(this, PersonMineCourseDetailActivity.class);
        String course_id = mLists.get(position).getId();
        String course_status = mLists.get(position).getCourse_status();
        intent.putExtra("position", position);
        intent.putExtra("course_id", course_id);
        intent.putExtra("course_status", course_status);
        startActivityForResult(intent, 0);
    }

    /**
     * 处理课程按钮的点击事件
     *
     * @param position 课程索引
     * @param type     事件类型
     */
    private void parserOnClickEvent(final int position, int type) {
        switch (type) {
            case COURSE_DELETE:
                CustomDialog dialog = new CustomDialog(
                        this,
                        getString(R.string.confirm),
                        getString(R.string.label_cancel),
                        getString(R.string.dialog_show_delete_course));
                dialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            getData("delete_course", position);
                        }
                    }
                });
                break;
            case COURSE_EDIT:
                jumpEditCourse(position);
                break;
            /*case COURSE_FINISH:
                getData("course_finish_enroll", position);
                break;*/
            default:
                break;
        }
    }

    /**
     * 跳转到发布课程页面
     *
     * @param position p
     */
    private void jumpEditCourse(int position) {
        Intent intent_CourseReservation = new Intent(this, PublishActivity.class);
        Course_Edit_Info course_edit_info = new Course_Edit_Info();
        course_edit_info.setSid(mLists.get(position).getSchool_id());
        course_edit_info.setAddress(mLists.get(position).getSchool_name());
        course_edit_info.setStart_date(mLists.get(position).getCourse_info().getStart_date());
        course_edit_info.setEnd_date(mLists.get(position).getCourse_info().getEnd_date());
        course_edit_info.setStart_time(mLists.get(position).getCourse_info().getStart_time());
        course_edit_info.setEnd_time(mLists.get(position).getCourse_info().getEnd_time());
        course_edit_info.setRepeat(mLists.get(position).getCourse_info().getRepeat());
        course_edit_info.setSchool_start_time(mLists.get(position).getSchool_start_time());
        course_edit_info.setSchool_end_time(mLists.get(position).getSchool_end_time());
        course_edit_info.setName(mLists.get(position).getName());
        course_edit_info.setSingle_price(mLists.get(position).getSingle_price());
        course_edit_info.setRemain_count(mLists.get(position).getRemain_count());
        if (StringUtils.isNotNullString(mLists.get(position).getHave_enroll())
                && StringUtils.isNotNullString(mLists.get(position).getRemain_count())) {
            course_edit_info.setTotal_count(Integer.parseInt(mLists.get(position).getHave_enroll())
                    + Integer.parseInt(mLists.get(position).getRemain_count()) + "");
        }
        course_edit_info.setTeacher_name(mLists.get(position).getReal_name());
        course_edit_info.setTeacher_uid(mLists.get(position).getTeacher_id());
        course_edit_info.setDesc(mLists.get(position).getDesc());
        course_edit_info.setMien_pics(mLists.get(position).getPics());
        intent_CourseReservation.putExtra("COURSE_DETAIL", course_edit_info);
        intent_CourseReservation.putExtra("COURSE_LIST_DATA", mLists.get(position));
        UIUtils.startActivity(intent_CourseReservation);
    }

    private void getData(final String action, final int position) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("course_id", mLists.get(position).getId());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        JSONTokener jsonParser = new JSONTokener(arg0.result);
                        JSONObject json;
                        try {
                            json = (JSONObject) jsonParser.nextValue();
                            int code = json.getInt("code");
                            if (code == 1) {
                                if (action.equals("course_finish_enroll")) {
                                    mLists.get(position).setCourse_status(APPLY_FINISH);
                                } else if (action.equals("delete_course")) {
                                    mLists.remove(position);
                                }
                                courseMineAdapter.notifyDataSetChanged();
                            } else {
                                jumpFailTip(code);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void jumpFailTip(int code) {

        switch (code) {
            case 70001:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_parameter_is_not_complete));
                break;
            case 70005:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_course_no_exist));
                break;
            case 70007:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_course_cannot_delete));
                break;
            case 70011:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_approval_to_complete_registration));
                break;
            case 70012:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_course_registration_has_been_completed));
                break;
            case 8888:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_network_busy));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null != data) {
            int position = data.getIntExtra("position", 0);
            switch (resultCode) {
                case COURSE_DELETE:
                    mLists.remove(position);
                    courseMineAdapter.notifyDataSetChanged();
                    break;
                case COURSE_CANCEL:
                    mLists.get(position).setCourse_status(CANCEL);
                    courseMineAdapter.notifyDataSetChanged();
                    break;
                case COURSE_BACK:
                    String course_status = data.getStringExtra("course_status");
                    mLists.get(position).setCourse_status(course_status);
                    courseMineAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public String getPageName() {
        return getString(R.string.person_mine_course_page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_person_course;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    wifi.setVisibility(View.GONE);
                    getData(limitStart, limitEnd);
                } else {
                    wifi.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.Param_Start_Fragment, FragmentFactory.TAB_MY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            ActivityControlManager.getInstance().finishCurrentAndOpenHome(this,3);
        }
    }
}

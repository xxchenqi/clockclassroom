package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.PerCouTimeAdapter;
import com.yiju.ClassClockRoom.adapter.PersonMineCourseDetailAdapter;
import com.yiju.ClassClockRoom.bean.Course_Edit_Info;
import com.yiju.ClassClockRoom.bean.Course_Person_Detail;
import com.yiju.ClassClockRoom.bean.Course_Person_Detail_Data;
import com.yiju.ClassClockRoom.bean.Course_Person_Detail_RoomInfo;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.control.FragmentFactory;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.CircleImageView;
import com.yiju.ClassClockRoom.widget.ListViewForScrollView;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 我发布的课程详情页
 * Created by Sandy on 2016/6/16/0016.
 */
public class PersonMineCourseDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back)//返回键
    private ImageView head_back;

    @ViewInject(R.id.sv_per_cou)
    private ScrollView sv_per_cou;

    @ViewInject(R.id.rl_per_cour_status)//限定条件显示(取消时为灰色)
    private RelativeLayout rl_per_cour_status;

    @ViewInject(R.id.tv_per_cour_detail_status)//课程状态
    private TextView tv_per_cour_detail_status;

    @ViewInject(R.id.tv_per_cour_detail_date)//课程日期时间展示
    private TextView tv_per_cour_detail_date;

    @ViewInject(R.id.rl_per_cour_detail_reason)//限定条件显示 未通过原因
    private RelativeLayout rl_per_cour_detail_reason;

    @ViewInject(R.id.tv_person_mine_course_detial_reason)//未通过原因
    private TextView tv_person_mine_course_detial_reason;

    @ViewInject(R.id.rl_per_cour_detail_apply)//限定条件显示 含点击事件
    private RelativeLayout rl_per_cour_detail_apply;

    @ViewInject(R.id.tv_per_cour_detail_apply)//报名(5/10)
    private TextView tv_per_cour_detail_apply;

    @ViewInject(R.id.civ_per_cour_detail_one)//头像一
    private CircleImageView civ_per_cour_detail_one;

    @ViewInject(R.id.civ_per_cour_detail_two)//头像二
    private CircleImageView civ_per_cour_detail_two;

    @ViewInject(R.id.civ_per_cour_detail_three)//头像三
    private CircleImageView civ_per_cour_detail_three;

    @ViewInject(R.id.tv_per_cour_detail_class_name)//课程名称
    private TextView tv_per_cour_detail_class_name;

    @ViewInject(R.id.tv_per_cour_detail_class_date)//课程日期
    private TextView tv_per_cour_detail_class_date;

    @ViewInject(R.id.tv_per_cour_detail_class_time)//课程时间
    private TextView tv_per_cour_detail_class_time;

    @ViewInject(R.id.tv_per_cour_detail_adress)//课程地址
    private TextView tv_per_cour_detail_adress;

    @ViewInject(R.id.tv_per_cour_detail_type)//课程类型
    private TextView tv_per_cour_detail_type;

    @ViewInject(R.id.lv_per_cour_detail)//课程列表
    private ListViewForScrollView lv_per_cour_detail;

    @ViewInject(R.id.rl_time_list_item_more)//更多时间
    private RelativeLayout rl_time_list_item_more;

    @ViewInject(R.id.rl_per_cour_detail_teacher)//仅点击事件
    private RelativeLayout rl_per_cour_detail_teacher;

    @ViewInject(R.id.tv_person_mine_course_detial_teacher_name)//授课老师
    private TextView tv_person_mine_course_detial_teacher_name;

    //授课老师向右箭头
    @ViewInject(R.id.iv_course_detail_arrow)
    private ImageView iv_course_detail_arrow;

    @ViewInject(R.id.tv_per_cour_detail_people_remain)//可报名人数(12人)
    private TextView tv_per_cour_detail_people_remain;

    @ViewInject(R.id.tv_per_cour_detail_people_all)//报名总人数(班课总 22人)
    private TextView tv_per_cour_detail_people_all;

    @ViewInject(R.id.tv_per_cour_detail_price)//现价
    private TextView tv_per_cour_detail_price;

    @ViewInject(R.id.tv_per_cour_detail_content)//简介
    private TextView tv_per_cour_detail_content;

    @ViewInject(R.id.tv_test_ing)//取消按钮边文字
    private TextView tv_test_ing;

    @ViewInject(R.id.gv_per_cour_detail)//课程图片
    private GridView gv_per_cour_detail;

    @ViewInject(R.id.rl_person_button)//按钮
    private RelativeLayout rl_person_button;

    @ViewInject(R.id.ll_person_button)//按钮
    private LinearLayout ll_person_button;

    @ViewInject(R.id.tv_person_course_cancel)//取消课程按钮
    private TextView tv_person_course_cancel;

    @ViewInject(R.id.tv_person_course_edit)//编辑按钮
    private TextView tv_person_course_edit;

    @ViewInject(R.id.tv_person_course_delete)//删除按钮
    private TextView tv_person_course_delete;

    @ViewInject(R.id.iv_empty_mess)//消息箱过来空页面
    private ImageView iv_empty_mess;

    private String course_id;
    private Course_Person_Detail_Data coursePersonDetailData;
    private final int WAIT_CHECK = 1;
    private final int FAIL_CHECK = 2;
    private final int CANCEL = 3;
    private final int FINISH = 4;
    private final int APPLYING = 5;
    private final int APPLY_FINISH = 6;
    private final int COURSE_DELETE = 1;
    private final int COURSE_EDIT = 2;
    private final int COURSE_FINISH = 3;
    private final int COURSE_CANCEL = 4;
    private final int COURSE_BACK = 100;
    private final int SHOW_TIME_LIST = 3;
    private PersonMineCourseDetailAdapter personMineCourseDetailAdapter;
    private String uid;
    private int position;
    private String course_status;
    private boolean backFlag = false;
    private List<Course_Person_Detail_RoomInfo> mList = new ArrayList<>();
    private PerCouTimeAdapter perCouTimeAdapter;
    private Course_Person_Detail coursePerson;

    @Override
    protected void initView() {
        head_back.setOnClickListener(this);
        rl_per_cour_detail_teacher.setOnClickListener(this);
        tv_person_course_cancel.setOnClickListener(this);
        tv_person_course_edit.setOnClickListener(this);
        tv_person_course_delete.setOnClickListener(this);
        rl_per_cour_detail_apply.setOnClickListener(this);
        rl_time_list_item_more.setOnClickListener(this);
        sv_per_cou.smoothScrollTo(0, 20);
    }

    @Override
    protected void initData() {

        uid = SharedPreferencesUtils.getString(this, "id", null);
        Intent intent = getIntent();
        if (null != intent) {
            course_status = intent.getStringExtra("course_status");
            if (null != course_status && !"".equals(course_status)) {
                showTitle(course_status);
            }
            course_id = intent.getStringExtra("course_id");
            if (null != course_id && !"".equals(course_id)) {
                getData();
            }
            position = intent.getIntExtra("position", Integer.MAX_VALUE);
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_my_course_detail");
        params.addBodyParameter("id", course_id);

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
            coursePerson = GsonTools.changeGsonToBean(result, Course_Person_Detail.class);
            if (null != coursePerson) {
                if (coursePerson.getCode() == 1) {
                    coursePersonDetailData = coursePerson.getData();
                    if (null != coursePersonDetailData) {
                        course_status = coursePersonDetailData.getCourse_status();
                        if (StringUtils.isNotNullString(course_status)) {
                            sv_per_cou.setVisibility(View.VISIBLE);
                            iv_empty_mess.setVisibility(View.GONE);
                            showTitle(course_status);
                            showPage();
                        } else {
                            sv_per_cou.setVisibility(View.GONE);
                            iv_empty_mess.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        }
    }

    /**
     * 根据状态显示标题
     *
     * @param course_status 课程状态
     */
    private void showTitle(String course_status) {

        Integer status = Integer.valueOf(course_status);
        switch (status) {
            case WAIT_CHECK:
                //待审核
                tv_per_cour_detail_status.setText(getString(R.string.person_course_status_wait_check));
                rl_per_cour_detail_reason.setVisibility(View.GONE);
//                rl_per_cour_detail_apply.setVisibility(View.GONE);
                rl_person_button.setVisibility(View.GONE);
                tv_test_ing.setVisibility(View.GONE);
                break;
            case FAIL_CHECK:
                //审核未通过
                tv_per_cour_detail_status.setText(getString(R.string.person_course_status_fail_check));
//                rl_per_cour_detail_apply.setVisibility(View.GONE);
                rl_person_button.setVisibility(View.VISIBLE);
                ll_person_button.setVisibility(View.VISIBLE);
                tv_person_course_cancel.setVisibility(View.GONE);
                tv_test_ing.setVisibility(View.GONE);
                break;
            case CANCEL:
                //已取消
                tv_per_cour_detail_status.setText(getString(R.string.person_course_status_cancel));
                rl_per_cour_status.setBackgroundColor(UIUtils.getColor(R.color.color_gay_99));
                rl_per_cour_detail_reason.setVisibility(View.GONE);
                rl_person_button.setVisibility(View.GONE);
                tv_test_ing.setVisibility(View.GONE);
                break;
            case FINISH:
                //已完成
                tv_per_cour_detail_status.setText(getString(R.string.person_course_status_finish));
                rl_per_cour_status.setBackgroundColor(UIUtils.getColor(R.color.app_theme_color));
                rl_per_cour_detail_reason.setVisibility(View.GONE);
                rl_person_button.setVisibility(View.GONE);
                tv_test_ing.setVisibility(View.GONE);
                break;
            case APPLYING:
                //报名中
                tv_per_cour_detail_status.setText(getString(R.string.person_course_status_applying_single));
                tv_person_course_edit.setVisibility(View.GONE);
                rl_per_cour_detail_reason.setVisibility(View.GONE);
                rl_person_button.setVisibility(View.VISIBLE);
                ll_person_button.setVisibility(View.GONE);
                tv_person_course_cancel.setVisibility(View.VISIBLE);
                tv_test_ing.setVisibility(View.VISIBLE);
                break;
            /*case APPLY_FINISH:
                //报名完成
                tv_per_cour_detail_status.setText(getString(R.string.person_course_status_apply_finish));
                rl_person_button.setVisibility(View.GONE);
                rl_per_cour_detail_reason.setVisibility(View.GONE);
                break;*/
            default:
                break;
        }
    }

    /**
     * 展示页面
     */
    private void showPage() {
        if (StringUtils.isNotNullString(coursePersonDetailData.getAuth_reason())) {
            tv_person_mine_course_detial_reason.setText(coursePersonDetailData.getAuth_reason());
        }
        String update_time = coursePersonDetailData.getUpdate_time();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(update_time + "000"));
        String upDateTime = calendar.get(Calendar.YEAR) + "-"
                + (String.valueOf(calendar.get(Calendar.MONTH) + 1).length() == 1 ?
                ("0" + String.valueOf(calendar.get(Calendar.MONTH) + 1)) : String.valueOf(calendar.get(Calendar.MONTH) + 1)) + "-"
                + (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length() == 1 ?
                ("0" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))) + " "
                + (String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ?
                ("0" + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) + ":"
                + (String.valueOf(calendar.get(Calendar.MINUTE)).length() == 1 ?
                ("0" + String.valueOf(calendar.get(Calendar.MINUTE))) : String.valueOf(calendar.get(Calendar.MINUTE)));

        tv_per_cour_detail_date.setText(upDateTime);
        String remain_count = coursePersonDetailData.getRemain_count();
//        String total_count = coursePersonDetailData.getTotal_count();
        String have_enroll = coursePersonDetailData.getHave_enroll();
        int all_count = Integer.valueOf(have_enroll) + Integer.valueOf(remain_count);
        tv_per_cour_detail_apply.setText(have_enroll + "/" + all_count);
        tv_per_cour_detail_class_name.setText(coursePersonDetailData.getName());
        tv_per_cour_detail_class_date.setText(coursePersonDetailData.getSchedule().getDate_section());
        tv_per_cour_detail_class_time.setText(
                String.format(
                        getString(R.string.total_hour_short_text),
                        coursePersonDetailData.getSchedule().getAll_course_hour() + ""
                ));
        if ("0".equals(coursePersonDetailData.getTeacher().getId())) {
            iv_course_detail_arrow.setVisibility(View.GONE);
            tv_person_mine_course_detial_teacher_name.setText("暂无授课老师");
        } else {
            iv_course_detail_arrow.setVisibility(View.VISIBLE);
            tv_person_mine_course_detial_teacher_name.setText(coursePersonDetailData.getTeacher().getReal_name());
        }
        tv_per_cour_detail_people_remain.setText(
                String.format(
                        getString(R.string.count_about_people),
                        coursePersonDetailData.getRemain_count()
                ));
        tv_per_cour_detail_people_all.setText(
                String.format(
                        getString(R.string.course_total_count),
                        coursePersonDetailData.getTotal_count()
                ));
        tv_per_cour_detail_price.setText(
                String.format(
                        getString(R.string.course_single_price),
                        coursePersonDetailData.getSingle_price()
                ));
        tv_per_cour_detail_content.setText(coursePersonDetailData.getDesc());
        tv_per_cour_detail_adress.setText(coursePersonDetailData.getSchool().getName());
        tv_per_cour_detail_type.setText(coursePersonDetailData.getSchedule().getType_desc());
        List<String> pics = coursePersonDetailData.getPics();
        if (null != pics && pics.size() > 0) {
            showImageView(pics, "COURSE");
        }
        List<String> avatar = coursePersonDetailData.getAvatar();
        if (null != avatar && avatar.size() > 0) {
            showImageView(avatar, "HEAD");
        }
        List<Course_Person_Detail_RoomInfo> room_info = coursePersonDetailData.getSchedule().getRoom_info();
        if (null != room_info && room_info.size() > 0) {
            mList.clear();
            mList.addAll(room_info);
            showCourseList();
        }
    }

    /**
     * 展示课程时间列表
     */
    private void showCourseList() {
        if (mList.size() > SHOW_TIME_LIST) {
            rl_time_list_item_more.setVisibility(View.VISIBLE);
            rl_time_list_item_more.setEnabled(true);
        } else {
            rl_time_list_item_more.setVisibility(View.GONE);
            rl_time_list_item_more.setEnabled(false);
        }
        if (null != perCouTimeAdapter) {
            perCouTimeAdapter.notifyDataSetChanged();
        } else {
            perCouTimeAdapter = new PerCouTimeAdapter(mList);
            lv_per_cour_detail.setAdapter(perCouTimeAdapter);
        }


    }

    /**
     * 展示用户头像
     *
     * @param urls   图片地址
     * @param course 课程/头像
     */
    private void showImageView(List<String> urls, String course) {
        switch (course) {
            case "COURSE":
                if (null != personMineCourseDetailAdapter) {
                    personMineCourseDetailAdapter.notifyDataSetChanged();
                } else {
                    personMineCourseDetailAdapter = new PersonMineCourseDetailAdapter(urls);
                    gv_per_cour_detail.setAdapter(personMineCourseDetailAdapter);
                }
                break;
            case "HEAD":
                int civCountOne = 1;
                int civCountTwo = 2;
                if (urls.size() == civCountOne) {
                    civ_per_cour_detail_two.setVisibility(View.GONE);
                    civ_per_cour_detail_three.setVisibility(View.GONE);
                    Glide.with(this).load(urls.get(0)).into(civ_per_cour_detail_one);
                } else if (urls.size() == civCountTwo) {
                    civ_per_cour_detail_three.setVisibility(View.GONE);
                    Glide.with(this).load(urls.get(0)).into(civ_per_cour_detail_one);
                    Glide.with(this).load(urls.get(1)).into(civ_per_cour_detail_two);
                } else {
                    Glide.with(this).load(urls.get(0)).into(civ_per_cour_detail_one);
                    Glide.with(this).load(urls.get(1)).into(civ_per_cour_detail_two);
                    Glide.with(this).load(urls.get(2)).into(civ_per_cour_detail_three);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.rl_per_cour_detail_teacher:
                jumpTeacherPage();
                break;
            /*case R.id.tv_person_course_finish:
                getData("course_finish_enroll");
                break;*/
            case R.id.tv_person_course_cancel:
//                showCancelDialog();
                getData("cancel_course");
                break;
            case R.id.tv_person_course_edit:
                jumpEditCourse();
                break;
            case R.id.tv_person_course_delete:
                CustomDialog dialog = new CustomDialog(
                        this,
                        getString(R.string.confirm),
                        getString(R.string.label_cancel),
                        getString(R.string.dialog_show_delete_course));
                dialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            getData("delete_course");
                        }
                    }
                });
                break;
            case R.id.rl_per_cour_detail_apply:
                jumpPeopleApplyCount();
                break;
            case R.id.rl_time_list_item_more:
                rl_time_list_item_more.setVisibility(View.GONE);
                perCouTimeAdapter.setFlag(true);
                perCouTimeAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到发布课程页面
     */
    private void jumpEditCourse() {
        Intent intent_CourseReservation = new Intent(this, PublishActivity.class);
        Course_Edit_Info course_edit_info = new Course_Edit_Info();
        course_edit_info.setSid(coursePersonDetailData.getSchool().getId());
        course_edit_info.setAddress(coursePersonDetailData.getSchool().getName());
        course_edit_info.setStart_date(coursePersonDetailData.getCourse_info().getStart_date());
        course_edit_info.setEnd_date(coursePersonDetailData.getCourse_info().getEnd_date());
        course_edit_info.setStart_time(coursePersonDetailData.getCourse_info().getStart_time());
        course_edit_info.setEnd_time(coursePersonDetailData.getCourse_info().getEnd_time());
        course_edit_info.setRepeat(coursePersonDetailData.getCourse_info().getRepeat());
        course_edit_info.setSchool_start_time(coursePersonDetailData.getSchool().getSchool_start_time());
        course_edit_info.setSchool_end_time(coursePersonDetailData.getSchool().getSchool_end_time());
        course_edit_info.setName(coursePersonDetailData.getName());
        course_edit_info.setSingle_price(coursePersonDetailData.getSingle_price());
        course_edit_info.setRemain_count(coursePersonDetailData.getRemain_count());
        course_edit_info.setTotal_count(coursePersonDetailData.getTotal_count());
        course_edit_info.setTeacher_name(coursePersonDetailData.getTeacher().getReal_name());
        course_edit_info.setTeacher_uid(coursePersonDetailData.getTeacher().getId());
        course_edit_info.setDesc(coursePersonDetailData.getDesc());
        course_edit_info.setMien_pics(coursePersonDetailData.getPics());
        intent_CourseReservation.putExtra("COURSE_DETAIL", course_edit_info);
        intent_CourseReservation.putExtra("COURSE_DETAIL_DATA", coursePerson);
        UIUtils.startActivity(intent_CourseReservation);
    }


    /**
     * 跳转报名人数页面
     */
    private void jumpPeopleApplyCount() {
        Intent intent_PeopleApplyCount = new Intent(this, PersonMineCoursePeopleCountActivity.class);
        intent_PeopleApplyCount.putExtra("COURSE_ID", course_id);
        UIUtils.startActivity(intent_PeopleApplyCount);
    }

    /**
     * 跳转老师资料页面
     */
    private void jumpTeacherPage() {
        if (coursePersonDetailData == null || coursePersonDetailData.getTeacher() == null) {
            return;
        }
        String teacherId = coursePersonDetailData.getTeacher().getId();//授课老师id
        if (StringUtils.isNullString(teacherId) || "0".equals(teacherId)) {
            return;
        }
        Intent intent_teacher = new Intent(this, MemberDetailActivity.class);
        intent_teacher.putExtra(MemberDetailActivity.ACTION_UID, coursePersonDetailData.getTeacher().getId());
        intent_teacher.putExtra(MemberDetailActivity.ACTION_SHOW_TEACHER, coursePersonDetailData.getTeacher().getShow_teacher());
        intent_teacher.putExtra(MemberDetailActivity.ACTION_ORG_AUTH, coursePersonDetailData.getTeacher().getOrg_auth());
        intent_teacher.putExtra(MemberDetailActivity.ACTION_title, UIUtils.getString(R.string.member_detail));
        intent_teacher.putExtra(MemberDetailActivity.ACTION_MOBILE, coursePersonDetailData.getTeacher().getMobile());
        intent_teacher.putExtra(MemberDetailActivity.ACTION_COURSE_FLAG, true);
        UIUtils.startActivity(intent_teacher);
    }

    /**
     * 取消课程提示对话框
     */
    private void showCancelDialog() {

        CustomDialog customDialog = new CustomDialog(
                PersonMineCourseDetailActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                getString(R.string.dialog_show_cancle_course));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    getData("cancel_course");
                }
            }
        });
    }

    /**
     * 获取数据
     *
     * @param action 请求action
     */
    private void getData(final String action) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("course_id", course_id);

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
                                switch (action) {
                                    /*case "course_finish_enroll":
                                        backFlag = true;
                                        course_status = APPLY_FINISH+"";
                                        tv_per_cour_detail_status.setText(getString(R.string.person_course_status_apply_finish));
                                        rl_person_button.setVisibility(View.GONE);
                                        rl_per_cour_detail_reason.setVisibility(View.GONE);
                                        break;*/
                                    case "delete_course":
                                        setBackData(COURSE_DELETE);
                                        break;
                                    case "cancel_course":
                                        backFlag = true;
                                        course_status = CANCEL + "";
                                        tv_per_cour_detail_status.setText(getString(R.string.person_course_status_cancel));
                                        rl_per_cour_status.setBackgroundColor(UIUtils.getColor(R.color.color_gay_99));
                                        rl_per_cour_detail_reason.setVisibility(View.GONE);
                                        rl_person_button.setVisibility(View.GONE);
                                        UIUtils.showToastSafe(getString(R.string.toast_show_cancle_course));
                                        break;
                                }
                            } else {
                                jumpFailTip(code);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setBackData(int i) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(i, intent);
        finish();
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
            case 70013:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_approved_to_cancel_course));
                break;
            case 70014:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_less_than_many_hours_to_cancle_course));
                break;
            case 8888:
                UIUtils.showLongToastSafe(getString(R.string.toast_show_network_busy));
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
            super.onBackPressed();
            if (backFlag) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("course_status", course_status);
                setResult(COURSE_BACK, intent);
                finish();
            } else {
                finish();
            }
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.person_mine_course_detial_page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_person_course_detial;
    }
}

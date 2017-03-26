package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.yiju.ClassClockRoom.adapter.CourseStyleAdapter;
import com.yiju.ClassClockRoom.bean.Course_DataInfo;
import com.yiju.ClassClockRoom.bean.Course_Need_Info;
import com.yiju.ClassClockRoom.bean.Course_Person_Detail;
import com.yiju.ClassClockRoom.bean.Course_Person_Detail_Data;
import com.yiju.ClassClockRoom.bean.PictureWrite;
import com.yiju.ClassClockRoom.bean.result.CodeKey;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.control.camera.CameraDialog;
import com.yiju.ClassClockRoom.control.camera.CameraImage;
import com.yiju.ClassClockRoom.control.camera.ResultCameraHandler;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.GridViewForScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释: 发布课程第二步
 * <p/>
 * 作者: wh
 * <p/>
 * 时间: on 2016/7/5 09:32
 * ----------------------------------------
 */
public class PublishCourseSecondActivity extends BaseActivity
        implements View.OnClickListener, CourseStyleAdapter.AddClickListener {
    public static final String COURSE_TAG = "course_tag";
    public static final String COURSE_TAG_NAME = "name";
    public static final String COURSE_TAG_PRICE = "price";
    public static final String COURSE_TAG_NUM = "num";
    public static final String COURSE_TAG_DESC = "desc";

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
     * 课程名称
     */
    @ViewInject(R.id.rl_course_name)
    private RelativeLayout rl_course_name;
    /**
     * 课程名_无值
     */
    @ViewInject(R.id.tv_course_name_none)
    private TextView tv_course_name_none;
    /**
     * 课程名称_有值布局
     */
    @ViewInject(R.id.ll_course_name)
    private LinearLayout ll_course_name;
    /**
     * 课程名
     */
    @ViewInject(R.id.tv_course_name)
    private TextView tv_course_name;
    /**
     * 课程单价
     */
    @ViewInject(R.id.rl_course_price)
    private RelativeLayout rl_course_price;
    /**
     * 课程单价_无值
     */
    @ViewInject(R.id.tv_course_price_none)
    private TextView tv_course_price_none;
    /**
     * 课程单价_有值布局
     */
    @ViewInject(R.id.ll_course_price)
    private LinearLayout ll_course_price;
    /**
     * 课程单价
     */
    @ViewInject(R.id.tv_course_price)
    private TextView tv_course_price;
    /**
     * 招生人数
     */
    @ViewInject(R.id.rl_people_num)
    private RelativeLayout rl_people_num;
    /**
     * 人数_无值
     */
    @ViewInject(R.id.tv_people_num_none)
    private TextView tv_people_num_none;
    /**
     * 招生人数_有值布局
     */
    @ViewInject(R.id.ll_people_num)
    private LinearLayout ll_people_num;
    /**
     * 人数
     */
    @ViewInject(R.id.tv_course_people_num)
    private TextView tv_course_people_num;
    /**
     * 授课老师
     */
    @ViewInject(R.id.rl_course_teacher)
    private RelativeLayout rl_course_teacher;
    /**
     * 授课老师_无值
     */
    @ViewInject(R.id.tv_course_teacher_none)
    private TextView tv_course_teacher_none;
    /**
     * 授课老师_有值布局
     */
    @ViewInject(R.id.ll_course_teacher)
    private LinearLayout ll_course_teacher;
    /**
     * 授课老师
     */
    @ViewInject(R.id.tv_course_teacher)
    private TextView tv_course_teacher;
    /**
     * 课程描述
     */
    @ViewInject(R.id.rl_course_desc)
    private RelativeLayout rl_course_desc;
    /**
     * 课程描述_无值
     */
    @ViewInject(R.id.tv_course_desc_none)
    private TextView tv_course_desc_none;
    /**
     * 课程描述_有值布局
     */
    @ViewInject(R.id.ll_course_desc)
    private LinearLayout ll_course_desc;
    /**
     * 课程描述
     */
    @ViewInject(R.id.tv_course_desc)
    private TextView tv_course_desc;
    /**
     * 课程风采
     */
    @ViewInject(R.id.gv_course_mien)
    private GridViewForScrollView gv_course_mien;
    /**
     * 发布课程按钮
     */
    @ViewInject(R.id.btn_publish_course)
    private Button btn_publish_course;

    @ViewInject(R.id.sv_course)
    private ScrollView sv_course;

    //照相image
    private CameraImage cameraImage;
    private List<String> urls = new ArrayList<>();
    private CourseStyleAdapter adapter;
    private int mScreenWidth;
    private String sid;
    private String teacher_uid;
    private Course_Need_Info course_need_info = new Course_Need_Info();
    private Course_DataInfo course_person;
    private Course_Person_Detail course_person_detail;
    private boolean no_choose_teacher;


    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        sid = intent.getStringExtra(ChooseStoreActivity.ACTION_SID);
        course_person = (Course_DataInfo) intent.getSerializableExtra("COURSE_LIST_DATA");
        course_person_detail = (Course_Person_Detail) intent.getSerializableExtra("COURSE_DETAIL_DATA");
        String data = intent.getStringExtra("DATE");
        String week = intent.getStringExtra("WEEK");
        String time = intent.getStringExtra("TIME");
        List<String> repeats = new ArrayList<>();
        if (week.contains(UIUtils.getString(R.string.reservation_mon))) {
            repeats.add("1");
        }
        if (week.contains(UIUtils.getString(R.string.reservation_tue))) {
            repeats.add("2");
        }
        if (week.contains(UIUtils.getString(R.string.reservation_wed))) {
            repeats.add("3");
        }
        if (week.contains(UIUtils.getString(R.string.reservation_thu))) {
            repeats.add("4");
        }
        if (week.contains(UIUtils.getString(R.string.reservation_fri))) {
            repeats.add("5");
        }
        if (week.contains(UIUtils.getString(R.string.reservation_sat))) {
            repeats.add("6");
        }
        if (week.contains(UIUtils.getString(R.string.reservation_sun))) {
            repeats.add("7");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeats.size(); i++) {
            if (i == repeats.size() - 1) {
                sb.append(repeats.get(i));
            } else {
                sb.append(repeats.get(i)).append(",");
            }
        }
        if (StringUtils.isNotNullString(data)) {
            course_need_info.setStart_date(data.split("~")[0]);
            course_need_info.setEnd_date(data.split("~")[1]);
        }
        course_need_info.setRepeat(sb.toString());
        course_need_info.setStart_time(Integer.parseInt(time.split("~")[0].replaceAll(":", "").replaceAll(" ", "")));
        course_need_info.setEnd_time(Integer.parseInt(time.split("~")[1].replaceAll(":", "").replaceAll(" ", "")));
    }

    @Override
    protected void initView() {
        sv_course.smoothScrollTo(0, 20);
        mScreenWidth = CommonUtil.getScreenWidth(this);
        cameraImage = new CameraImage(PublishCourseSecondActivity.this);
        head_title.setText(UIUtils.getString(R.string.course_reservation_title));
        ll_course_name.setVisibility(View.GONE);
        ll_course_price.setVisibility(View.GONE);
        ll_people_num.setVisibility(View.GONE);
        ll_course_teacher.setVisibility(View.GONE);
        ll_course_desc.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        rl_course_name.setOnClickListener(this);
        rl_course_price.setOnClickListener(this);
        rl_people_num.setOnClickListener(this);
        rl_course_teacher.setOnClickListener(this);
        rl_course_desc.setOnClickListener(this);
        btn_publish_course.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (course_person != null) {//从我的课程列表编辑过来
            tv_course_name_none.setVisibility(View.GONE);
            ll_course_name.setVisibility(View.VISIBLE);
            tv_course_price_none.setVisibility(View.GONE);
            ll_course_price.setVisibility(View.VISIBLE);
            tv_people_num_none.setVisibility(View.GONE);
            ll_people_num.setVisibility(View.VISIBLE);
            tv_course_desc_none.setVisibility(View.GONE);
            ll_course_desc.setVisibility(View.VISIBLE);
            tv_course_teacher_none.setVisibility(View.GONE);
            ll_course_teacher.setVisibility(View.VISIBLE);

            tv_course_name.setText(course_person.getName());
            tv_course_price.setText(
                    String.format(
                            getString(R.string.string_format_money_hour),
                            course_person.getSingle_price()
                    ));
            if (StringUtils.isNotNullString(course_person.getHave_enroll())
                    && StringUtils.isNotNullString(course_person.getRemain_count())) {
                tv_course_people_num.setText(
                        String.format(
                                "%s/%d",
                                course_person.getRemain_count(),
                                Integer.parseInt(course_person.getHave_enroll())
                                        + Integer.parseInt(course_person.getRemain_count())
                        )
                );
            }
            tv_course_teacher.setText(course_person.getReal_name());
            tv_course_desc.setText(course_person.getDesc());
            teacher_uid = course_person.getTeacher_id();
            urls = course_person.getPics();
        }
        if (course_person_detail != null) {//从我的课程详情编辑过来
            Course_Person_Detail_Data course_person_detail_data = course_person_detail.getData();
            if (course_person_detail_data == null) {
                return;
            }
            tv_course_name_none.setVisibility(View.GONE);
            ll_course_name.setVisibility(View.VISIBLE);
            tv_course_price_none.setVisibility(View.GONE);
            ll_course_price.setVisibility(View.VISIBLE);
            tv_people_num_none.setVisibility(View.GONE);
            ll_people_num.setVisibility(View.VISIBLE);
            tv_course_desc_none.setVisibility(View.GONE);
            ll_course_desc.setVisibility(View.VISIBLE);
            tv_course_teacher_none.setVisibility(View.GONE);
            ll_course_teacher.setVisibility(View.VISIBLE);

            tv_course_name.setText(course_person_detail_data.getName());
            tv_course_price.setText(
                    String.format(
                            getString(R.string.string_format_money_hour),
                            course_person_detail_data.getSingle_price()
                    ));
            tv_course_people_num.setText(
                    String.format(
                            "%s/%s",
                            course_person_detail_data.getRemain_count(),
                            course_person_detail_data.getTotal_count()
                    )
            );
            tv_course_teacher.setText(course_person_detail_data.getTeacher().getReal_name());
            tv_course_desc.setText(course_person_detail_data.getDesc());
            teacher_uid = course_person_detail_data.getTeacher().getId();
            urls = course_person_detail_data.getPics();
        }

        if (urls == null) {
            return;
        }
        if (urls.size() == 0) {
            urls.add(urls.size(), "add");
        }

        updateGridView(urls);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_publish_second_Page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_publish_course_second;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.rl_course_name://课程名称
                intent = new Intent(this, CourseMessageEditActivity.class);
                intent.putExtra(COURSE_TAG, COURSE_TAG_NAME);
                intent.putExtra("show_content", tv_course_name.getText().toString());
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_course_price://课程单价
                intent = new Intent(this, CourseMessageEditActivity.class);
                intent.putExtra(COURSE_TAG, COURSE_TAG_PRICE);
                intent.putExtra("show_content", tv_course_price.getText().toString());
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_people_num://招生人数
                intent = new Intent(this, CourseMessageEditActivity.class);
                intent.putExtra(COURSE_TAG, COURSE_TAG_NUM);
                intent.putExtra("show_content", tv_course_people_num.getText().toString());
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_course_teacher://授课老师
                //请求机构成员列表
//                getHttpForMembers();
                Intent it = new Intent(this, CourseTeacherListActivity.class);
//                it.putExtra("teacher_list", (Serializable) memberBean.getData().getList());
                if (no_choose_teacher) {
                    it.putExtra("no_choose_teacher", true);
                } else {
//                    it.putExtra("teacher_name", tv_course_teacher.getText().toString());
                    it.putExtra("teacher_uid", teacher_uid);
                }
                startActivityForResult(it, 1);
                break;
            case R.id.rl_course_desc://课程描述
                intent = new Intent(this, CourseMessageEditActivity.class);
                intent.putExtra(COURSE_TAG, COURSE_TAG_DESC);
                intent.putExtra("show_content", tv_course_desc.getText().toString());
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_publish_course://发布课程按钮
                if (StringUtils.isNullString(tv_course_name.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_course_name));
                    return;
                }
                if (StringUtils.isNullString(tv_course_price.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_course_price));
                    return;
                }
                if (StringUtils.isNullString(tv_course_people_num.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_course_people_num));
                    return;
                }
                /*if (StringUtils.isNullString(tv_course_teacher.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_course_teacher));
                    return;
                }*/
                if (StringUtils.isNullString(tv_course_desc.getText().toString())) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_course_desc));
                    return;
                }
                if (urls == null || urls.size() == 1) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_course_mine));
                    return;
                }
                getHttpForPublishCourse();
                break;
        }
    }

    /**
     * 发布课程接口请求
     */
    private void getHttpForPublishCourse() {
        if ("-1".equals(StringUtils.getUid())) {
            return;
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "publish_course");//必传
        params.addBodyParameter("uid", StringUtils.getUid());//必传
        params.addBodyParameter("sid", sid);//门店id 必传
        params.addBodyParameter("name", tv_course_name.getText().toString());//课程名  必传
        String remain = tv_course_people_num.getText().toString().split("/")[0];
        String total = tv_course_people_num.getText().toString().split("/")[1];
        params.addBodyParameter("remain_count", remain);//招生剩余人数  必传
        params.addBodyParameter("total_count", total);//招生剩余人数  必传
        String price = tv_course_price.getText().toString().replaceAll("¥", "").replaceAll("/小时", "");
        params.addBodyParameter("single_price", price);//课程单价  必传
//        params.addBodyParameter("old_price", "");//课程原价  可不传
        params.addBodyParameter("desc", tv_course_desc.getText().toString());//课程简介  可不传
        urls.remove("add");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urls.size(); i++) {
            if (i == urls.size() - 1) {
                sb.append(urls.get(i));
            } else {
                sb.append(urls.get(i)).append(",");
            }
        }
        if (StringUtils.isNotNullString(sb.toString())) {
            params.addBodyParameter("mien_pic", sb.toString());//课程图片   可不传
        }
        params.addBodyParameter("teacher_uid", teacher_uid);//上课人id  必传
//        params.addBodyParameter("order2_id", "");//门店订单id  可不传
//        params.addBodyParameter("status", "");//状态  可不传
        params.addBodyParameter("course_info", GsonTools.createGsonString(course_need_info));//时间参数  可不传
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        updateGridView(urls);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForPublishCourse(arg0.result);

                    }
                });
    }

    private void processDataForPublishCourse(String result) {
        CommonResultBean memberBean = GsonTools.changeGsonToBean(result,
                CommonResultBean.class);
        if (memberBean == null) {
            return;
        }
        if ("1".equals(memberBean.getCode())) {
            //跳转至发布成功页面
            Intent intent = new Intent(this, PublishResultActivity.class);
            startActivity(intent);
        } else {
            UIUtils.showToastSafe(memberBean.getMsg());
        }
    }


    @Override
    public void addClick() {
        if (!PermissionsChecker.checkPermission(
                PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS)) {
            new CameraDialog(this, cameraImage).creatView();
        } else {
            PermissionsChecker.requestPermissions(
                    PublishCourseSecondActivity.this,
                    PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS
            );
        }
    }

    @Override
    public void checkClick(int pos) {
        Intent intent = new Intent(this, CourseMienEditActivity.class);
        intent.putExtra("pos", pos);
        intent.putExtra("course_url", urls.get(pos));
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String tag = data.getStringExtra(PublishCourseSecondActivity.COURSE_TAG);
            String context = data.getStringExtra(CourseMessageEditActivity.RESULT_DATA_CONTEXT);
            if (PublishCourseSecondActivity.COURSE_TAG_NAME.equals(tag)) {
                tv_course_name_none.setVisibility(View.GONE);
                ll_course_name.setVisibility(View.VISIBLE);
                tv_course_name.setText(context);
            } else if (PublishCourseSecondActivity.COURSE_TAG_PRICE.equals(tag)) {
                tv_course_price_none.setVisibility(View.GONE);
                ll_course_price.setVisibility(View.VISIBLE);
                tv_course_price.setText(String.format(getString(R.string.string_format_money_hour), context));
            } else if (PublishCourseSecondActivity.COURSE_TAG_NUM.equals(tag)) {
                tv_people_num_none.setVisibility(View.GONE);
                ll_people_num.setVisibility(View.VISIBLE);
                tv_course_people_num.setText(context);
            } else if (PublishCourseSecondActivity.COURSE_TAG_DESC.equals(tag)) {
                if (StringUtils.isNullString(context)) {
                    tv_course_desc_none.setVisibility(View.VISIBLE);
                    ll_course_desc.setVisibility(View.GONE);
                } else {
                    tv_course_desc_none.setVisibility(View.GONE);
                    ll_course_desc.setVisibility(View.VISIBLE);
                    tv_course_desc.setText(context);
                }
            }
        } else if (resultCode == 2) {
            //选择授课老师
            String teacher_name = data.getStringExtra("teacher_name");
            teacher_uid = data.getStringExtra("teacher_uid");
            no_choose_teacher = data.getBooleanExtra("no_choose_teacher", false);
            if (!no_choose_teacher) {
                tv_course_teacher_none.setVisibility(View.GONE);
                ll_course_teacher.setVisibility(View.VISIBLE);
                tv_course_teacher.setText(teacher_name);
            } else {
                tv_course_teacher_none.setVisibility(View.VISIBLE);
                ll_course_teacher.setVisibility(View.GONE);
                tv_course_teacher.setText("");
            }
        } else if (requestCode == CameraImage.CAMERA_WITH_DATA
                || requestCode == CameraImage.PHOTO_REQUEST_CUT
                || requestCode == CameraImage.PHOTO_REQUEST_GALLERY) {
            ResultCameraHandler
                    .getInstance()
                    .setCrop(true)
                    .getPhotoFile(this,
                            data, requestCode, resultCode, cameraImage,
                            new ResultCameraHandler.CameraResult() {
                                @Override
                                public void result(File imageUri) {
                                    uploadingHeadImage(imageUri);
                                }
                            });
        } else if (resultCode == CourseMienEditActivity.RESULT_CODE_FROM_COURSE_MIEN_EDIT_DELETE_ACT) {
            //课程风采删除
            int pos = data.getIntExtra("pos", -1);
            urls.remove(pos);
            updateGridView(urls);
        }
    }

    private void updateGridView(List<String> urls) {
        if (urls.size() > 8) {
            urls.remove("add");
        } else {
            if (!urls.contains("add")) {
                urls.add(urls.size(), "add");
            }
        }
        if (adapter == null) {
            adapter = new CourseStyleAdapter(
                    urls,
                    mScreenWidth,
                    this
            );
            gv_course_mien.setAdapter(adapter);
        } else {
            adapter.setData(urls);
        }
    }

    /**
     * 上传
     *
     * @param imageUri 图片文件流
     */
    private void uploadingHeadImage(File imageUri) {
        // 3次请求,获取授权码及key,请求内部写接口和上传头像
        getHttpUtils2(imageUri);
    }

    /**
     * 获取key请求
     *
     * @param imageUri 图片文件流
     */
    private void getHttpUtils2(final File imageUri) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "getcodekey");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData2(arg0.result, imageUri);

                    }
                });

    }

    /**
     * 解析 key
     *
     * @param result   s
     * @param imageUri f
     */
    private void processData2(String result, File imageUri) {
        CodeKey codeKey = GsonTools.changeGsonToBean(result, CodeKey.class);
        if (codeKey == null) {
            return;
        }
        if (codeKey.getCode().equals("1")) {
            CodeKey.Data data = codeKey.getData();
            // 请求上传头像
            getHttpUtils3(imageUri, data.getPermit_code(), data.getFile_category(), data.getKey());

        } else {
            UIUtils.showToastSafe(R.string.toast_request_fail);
        }
    }

    /**
     * 上传头像
     *
     * @param imageUri f
     */
    private void getHttpUtils3(File imageUri, String permit_code, String file_category, String key) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("permit_code", permit_code);
        params.addBodyParameter("file_category", file_category);
        params.addBodyParameter("key", key);
        params.addBodyParameter("pfile", imageUri);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.PIC_WIRTE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData3(arg0.result);

                    }
                });

    }

    private void processData3(String result) {
        PictureWrite pictureWrite = GsonTools.changeGsonToBean(result,
                PictureWrite.class);
        if (pictureWrite == null) {
            UIUtils.showToastSafe(R.string.toast_request_fail);
            return;
        }

        if (pictureWrite.isFlag()) {
            StringBuilder sb = new StringBuilder(pictureWrite.getResult()
                    .getPic_id());
            String headUrl = "http://get.file.dc.cric.com/"
                    + sb.insert(sb.length() - 4, "_350X350_0_0_0").toString();
            urls.add(urls.size() - 1, headUrl);
            updateGridView(urls);
        } else {
            UIUtils.showToastSafe(R.string.toast_request_fail);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
//        } else {
            UIUtils.showToastSafe(getString(R.string.toast_permission_read_write_sdcard));
        }
    }
}

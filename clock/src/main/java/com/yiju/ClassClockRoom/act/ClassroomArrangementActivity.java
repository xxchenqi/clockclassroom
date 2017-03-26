package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
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
import com.yiju.ClassClockRoom.adapter.DeviceFreeAdapter;
import com.yiju.ClassClockRoom.bean.ClassroomArrangeData;
import com.yiju.ClassClockRoom.bean.CommonMsgResult;
import com.yiju.ClassClockRoom.bean.EditDeviceListNewResult;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.StudentType;
import com.yiju.ClassClockRoom.bean.result.ClassroomArrangeResult;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.fragment.ClassRoomUseNameFragment;
import com.yiju.ClassClockRoom.util.DateUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;
import com.yiju.ClassClockRoom.widget.dialog.StudentTypeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的课室布置_编辑页面
 * 1、订单详情页面_进行中状态_点击课室布置修改跳转过来
 * 2、课室选择页(订单详情列表页)_点击课室布置跳转过来
 * Created by wh on 2016/5/13.
 */
public class ClassroomArrangementActivity extends BaseActivity
        implements View.OnClickListener, StudentTypeDialog.IStudentTypeOnItemClick {

    public static int RESULT_CODE_FROM_CLASSROOM_ARRANGEMENT_ACT = 2;
    /**
     * 返回按钮
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    /**
     * 保存按钮
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * 右上角文案
     */
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    /**
     * 选择课室用途
     */
    @ViewInject(R.id.rl_select_classroom_purpose)
    private RelativeLayout rl_select_classroom_purpose;
    /**
     * 课室用途名
     */
    @ViewInject(R.id.tv_use_type)
    private TextView tv_use_type;
    /**
     * 选择学生类型
     */
    @ViewInject(R.id.rl_select_student_type)
    private RelativeLayout rl_select_student_type;
    /**
     * 学生类型名
     */
    @ViewInject(R.id.tv_student_type)
    private TextView tv_student_type;
    /**
     * 免费设备列表
     */
    @ViewInject(R.id.lv_device_free)
    private ListView lv_device_free;

    private ArrayList<ClassroomArrangeData> classroom_type_list = new ArrayList<>();//课室用途
    private List<StudentType> student_type_list = new ArrayList<>();//学生类型
    private List<EditDeviceListNewResult.DataEntity> dataEntities = new ArrayList<>();//可编辑免费设备列表

    private Order2 order2;

    private String current_use_id = "";//当前use_id
    private String current_student_type = "";//当前student_type
    private int current_puse_id;//当前课室用途大类id
    private String end_date;//课室预订周期的最后一天日期

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            order2 = (Order2) intent.getSerializableExtra("order2");
        }

        head_title.setText(UIUtils.getString(R.string.order_classroom_arrangement));
        head_right_text.setText(UIUtils.getString(R.string.label_save));
        if (order2 != null) {
            end_date = StringUtils.isNotNullString(order2.getEnd_date()) ? order2.getEnd_date() : "";
            //获取课室用途及学生类型列表
            getRequestClassroomArrange();
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        rl_select_classroom_purpose.setOnClickListener(this);
        rl_select_student_type.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (order2 == null) {
            return;
        }
        if (StringUtils.isNotNullString(order2.getUse_desc())) {
            tv_use_type.setText(order2.getUse_desc());//课室类型
        }
        if (StringUtils.isNotNullString(order2.getStudent_desc())) {
            tv_student_type.setText(order2.getStudent_desc());//学生类型_年龄段
        }

        current_use_id = order2.getUse_id();
        current_student_type = order2.getStudent_type();

    }

    /**
     * 请求初始课室布置
     */
    public void getRequestClassroomArrange() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "classroom_arrange");
        params.addBodyParameter("order2_id", order2.getId());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_CLASSROOM_ARRANGE, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);
                    }
                }
        );
    }

    private void processData(String result) {
        ClassroomArrangeResult classroomArrangeResult = GsonTools.changeGsonToBean(
                result,
                ClassroomArrangeResult.class
        );
        if (classroomArrangeResult == null) {
            return;
        }

        if ("1".equals(classroomArrangeResult.getCode())) {
            classroom_type_list = classroomArrangeResult.getData();//课室用途
            student_type_list = classroomArrangeResult.getStudent_type();//学生类型
            int is_meeting = classroomArrangeResult.getIs_meeting();
            if (is_meeting == 1) {
                //是会议室类型,so隐藏选择学生类型
                rl_select_student_type.setVisibility(View.GONE);
            } else {
                rl_select_student_type.setVisibility(View.VISIBLE);
            }

            /**
             * Device_free无值 : 即第一次布置
             */
            if (order2.getDevice_free() == null || order2.getDevice_free().size() == 0) {
                if (!"0".equals(order2.getUse_id()) && !"0".equals(order2.getStudent_type())) {
                    getRequestEditDeviceListNew(false, order2.getUse_id(), order2.getStudent_type());
                }
            } else {
                //Device_free有值 :即布置过，就不传use_id、student_type
                getRequestEditDeviceListNew(true, null, null);
            }
        } else {
            UIUtils.showToastSafe(classroomArrangeResult.getMsg());
        }

    }

    /**
     * 编辑免费设备请求
     *
     * @param haveDeviceFree true 不传use_id、student_type
     *                       false 若use_id与student_type不为0,就传
     */
    public void getRequestEditDeviceListNew(final boolean haveDeviceFree, String use_id, String student_type) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "edit_device_list_new");
        params.addBodyParameter("order2_id", order2.getId());
        if (!haveDeviceFree) {//没有免费设备,传use_id与student_type,前提是这俩值不是0
            params.addBodyParameter("use_id", use_id);
            params.addBodyParameter("student_type", student_type);
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_CLASSROOM_ARRANGE, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processEditDeviceData(arg0.result, haveDeviceFree);
                    }
                }
        );
    }

    /**
     * 编辑免费设备请求返回值处理
     *
     * @param result 返回值
     */
    private void processEditDeviceData(String result, boolean haveDeviceFree) {
        EditDeviceListNewResult newResult = GsonTools.fromJson(result, EditDeviceListNewResult.class);
        if ("1".equals(newResult.getCode())) {
            if (dataEntities != null) {
                dataEntities.clear();
            }
            dataEntities = newResult.getData();
            if (dataEntities == null) {
                return;
            }
            for (int i = 0; i < dataEntities.size(); i++) {
                EditDeviceListNewResult.DataEntity dataEntity = dataEntities.get(i);
                handleDeviceLocalMax(dataEntity);
                // haveDeviceFree :true 就布置过, false 第一次布置
                if (haveDeviceFree) {
                    if (StringUtils.isNotNullString(dataEntity.getNum())) {
                        dataEntity.setCurrentCount(Integer.valueOf(dataEntity.getNum()));
                    }
                } else {
                    if (StringUtils.isNotNullString(dataEntity.getRecommend())) {
                        dataEntity.setCurrentCount(Integer.valueOf(dataEntity.getRecommend()));
                    }
                }
            }

            lv_device_free.setAdapter(new DeviceFreeAdapter(dataEntities));
        } else {
            UIUtils.showToastSafe(newResult.getMsg());
        }
    }

    /**
     * 课室布置保存接口
     */
    private void getRequestEditDeviceFinishNew() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "edit_device_finish_new");
        params.addBodyParameter("order2_id", order2.getId());
        HashMap<String, Integer> hashMap = new HashMap<>();
        if (dataEntities != null && dataEntities.size() > 0) {
            for (int i = 0; i < dataEntities.size(); i++) {
                EditDeviceListNewResult.DataEntity entity = dataEntities.get(i);
                hashMap.put(entity.getDevice_id(), entity.getCurrentCount());
            }
            String device_str = GsonTools.createGsonString(hashMap);
            if (StringUtils.isNullString(device_str)) {
                UIUtils.showToastSafe(UIUtils.getString(R.string.toast_select_device));
                head_right_relative.setClickable(true);
                return;
            }
            params.addBodyParameter("device_str", device_str);
        } else {
            UIUtils.showToastSafe(UIUtils.getString(R.string.toast_select_device));
            head_right_relative.setClickable(true);
            return;
        }
        params.addBodyParameter("use_id", current_use_id);
        params.addBodyParameter("student_type", current_student_type);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_CLASSROOM_ARRANGE, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                        ProgressDialog.getInstance().dismiss();
                        head_right_relative.setClickable(true);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processEditDeviceFinishNew(arg0.result);
                        head_right_relative.setClickable(true);
                    }

                }
        );
    }

    /**
     * 课室布置保存接口返回值处理
     *
     * @param result 返回值
     */
    private void processEditDeviceFinishNew(String result) {

        CommonResultBean common = GsonTools.fromJson(result, CommonResultBean.class);
        if ("1".equals(common.getCode())) {
            UIUtils.showToastSafe(UIUtils.getString(R.string.toast_save_success));
            setResult(RESULT_CODE_FROM_CLASSROOM_ARRANGEMENT_ACT);//标记返回课室选择页或订单详情页
            finish();
        } else {
            UIUtils.showToastSafe(common.getMsg());
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_classroom_arrangement);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_classroom_arrangement;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_relative://保存按钮
                head_right_relative.setClickable(false);
                //先判断当前日期是否是课室预订周期的最后一天,若是，则提示不可更改课室布置
                getSystemTimeRequest(end_date);

                break;
            case R.id.rl_select_classroom_purpose://选择课室用途
                Intent intent = new Intent(this, ClassRoomTypeActivity.class);
                if (classroom_type_list != null && classroom_type_list.size() > 0) {
                    intent.putExtra("classroom_type", classroom_type_list);
                }
                intent.putExtra("order2_id", order2.getId());
                if (current_puse_id != 0) {
                    intent.putExtra("puse_id", current_puse_id);
                }
                intent.putExtra("use_name", tv_use_type.getText().toString().trim());
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_select_student_type://选择学生类型
                if (student_type_list == null || student_type_list.size() == 0) {
                    return;
                }
                new StudentTypeDialog(this, student_type_list, this, current_student_type).creatView();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ClassRoomUseNameFragment.RESULT_CODE_FROM_CLASS_ROOM_USE_NAME_FRG) {
            ClassroomArrangeData.UseEntity use_entity = (ClassroomArrangeData.UseEntity)
                    data.getSerializableExtra("use_entity");
            current_puse_id = data.getIntExtra("puse_id", 0);
            tv_use_type.setText(use_entity.getUse_name());
            current_use_id = use_entity.getUse_id();
            getRequestEditDeviceListNew(false, current_use_id, current_student_type);
        }
    }

    @Override
    public void OnItemClick(StudentType studentType) {
        current_student_type = studentType.getId();
        tv_student_type.setText(studentType.getType_desc());
        getRequestEditDeviceListNew(false, current_use_id, current_student_type);
    }

    /**
     * 本地计算最大值
     * recommend 推荐值
     * currentCount 当前值
     * people_relation 0无关,1有关
     *
     * @param dataEntity 设备信息model
     */
    public void handleDeviceLocalMax(EditDeviceListNewResult.DataEntity dataEntity) {
        Integer recommend = Integer.valueOf(dataEntity.getRecommend());
        //设置当前值(推荐值)
        dataEntity.setCurrentCount(recommend);

        //设置最大值,判断是否与人数有关
        Integer people_relation = Integer.valueOf(dataEntity.getPeople_relation());
        if (people_relation == 0) {
            //人数无关
            int max = recommend + 3;
            dataEntity.setLocalMax(max);
        } else {
            /**人数有关
             * 人数区间:[1-10]-->最大值 = 后台给的推荐数*1.5(向上取整)
             * 人数区间:[11-50]-->最大值 = 后台给的推荐数*1.2(向上取整)
             * 人数区间:>50-->最大值 = 后台给的推荐数*1.1(向上取整)
             */
            if (recommend >= 1 && recommend <= 10) {
                // 最大值 = 后台给的推荐数*1.5(向上取整)
                dataEntity.setLocalMax((int) Math.ceil(recommend * 1.5));
            } else if (recommend >= 11 && recommend <= 50) {
                // 后台给的推荐数*1.2(向上取整)
                dataEntity.setLocalMax((int) Math.ceil(recommend * 1.2));
            } else if (recommend > 50) {
                //后台给的推荐数*1.1(向上取整)
                dataEntity.setLocalMax((int) Math.ceil(recommend * 1.1));
            }
        }
    }

    /**
     * 获取服务端当前时间请求
     *
     * @param end_date 课室预订周期的最后一天日期
     */
    public void getSystemTimeRequest(final String end_date) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_system_time");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(UIUtils.getString(R.string.fail_network_request));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result, end_date);
                    }
                }
        );
    }

    /**
     * 获取服务器系统时间返回处理
     *
     * @param result   返回值
     * @param end_date 2016-06-20
     */
    private void processData(String result, String end_date) {
        CommonMsgResult commonMsgResult = GsonTools.fromJson(result, CommonMsgResult.class);
        if ("1".equals(commonMsgResult.getCode())) {
            String sys_time = commonMsgResult.getData();//2015-10-15 15:21:36
            int compare_result = DateUtil.compareDate(sys_time, end_date);
            if (compare_result >= 0) {
                //限定  不可点
                UIUtils.showToastSafe(UIUtils.getString(R.string.toast_edit_classroom));
                head_right_relative.setClickable(true);
            } else {
                //可点 调取保存接口
                getRequestEditDeviceFinishNew();
            }
        }
    }
}

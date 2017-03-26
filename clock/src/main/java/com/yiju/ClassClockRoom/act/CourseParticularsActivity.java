package com.yiju.ClassClockRoom.act;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.yiju.ClassClockRoom.bean.CourseParticularsBean;
import com.yiju.ClassClockRoom.bean.CourseParticularsData;
import com.yiju.ClassClockRoom.bean.CourseParticularsInfo;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:课程明细
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/29 09:58
 * ----------------------------------------
 */
public class CourseParticularsActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //未完成
    @ViewInject(R.id.ll_no_finish)
    private LinearLayout ll_no_finish;
    //已使用
    @ViewInject(R.id.ll_has_use)
    private LinearLayout ll_has_use;
    //已退单
    @ViewInject(R.id.ll_remove)
    private LinearLayout ll_remove;
    //未完成
    @ViewInject(R.id.lr_classroom1)
    private LinearLayout lr_classroom1;
    //已使用
    @ViewInject(R.id.lr_classroom2)
    private LinearLayout lr_classroom2;
    //已退单
    @ViewInject(R.id.lr_classroom3)
    private LinearLayout lr_classroom3;
    //课程
    @ViewInject(R.id.tv_classromm_use_desc)
    private TextView tv_classromm_use_desc;
    //店名
    @ViewInject(R.id.tv_classromm_sname)
    private TextView tv_classromm_sname;
    //总共数量
    @ViewInject(R.id.tv_classromm_room_count)
    private TextView tv_classromm_room_count;
    //sv
    @ViewInject(R.id.sv_classroom)
    private ScrollView sv_classroom;

    //订单id
    private String id;
    private String sname;
    private String sumCount;
    private String uid;

    @Override
    public void initIntent() {
        super.initIntent();
        id = getIntent().getStringExtra("id");
        sname = getIntent().getStringExtra("sname");
        sumCount = getIntent().getStringExtra("sumCount");
    }

    @Override
    protected void initView() {

        uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                "id", null);
    }

    @Override
    protected void initData() {
        head_title.setText(R.string.title_course_particulars);
        getHttpUtils();
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
    }

    /**
     * 课程明细请求
     */
    private void getHttpUtils() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_course_schedule_detail");
        params.addBodyParameter("id", id);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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

    private void processData(String result) {
        CourseParticularsBean courseOrderDetailBean = GsonTools.changeGsonToBean(result,
                CourseParticularsBean.class);
        if (courseOrderDetailBean == null) {
            return;
        }
        if ("1".equals(courseOrderDetailBean.getCode())) {
            sv_classroom.setVisibility(View.VISIBLE);
            CourseParticularsData data = courseOrderDetailBean.getData();
            tv_classromm_use_desc.setText(data.getCourse_name());
            tv_classromm_sname.setText(sname);
            tv_classromm_room_count.setText(
                    String.format(
                            getString(R.string.order_times),
                            sumCount
                    ));
            List<CourseParticularsInfo> noComplete = data.getNocomplete();
            List<CourseParticularsInfo> complete = data.getComplete();
            List<CourseParticularsInfo> remove = data.getRemove();
            if (noComplete.size() != 0) {
                handleDeviceNoFree(ll_no_finish, noComplete, getString(R.string.txt_no_complete));
            } else {
                lr_classroom1.setVisibility(View.GONE);
            }
            if (complete.size() != 0) {
                handleDeviceNoFree(ll_has_use, complete, getString(R.string.txt_has_been_used));
            } else {
                lr_classroom2.setVisibility(View.GONE);
            }
            if (remove.size() != 0) {
                handleDeviceNoFree(ll_remove, remove, getString(R.string.txt_retired_single));
            } else {
                lr_classroom3.setVisibility(View.GONE);
            }
        }
    }


    private void handleDeviceNoFree(LinearLayout ll_parent, List<CourseParticularsInfo> data, String status) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                CourseParticularsInfo courseParticularsInfo = data.get(i);
                LinearLayout layout = (LinearLayout) LayoutInflater.from(
                        UIUtils.getContext()).inflate(
                        R.layout.item_classroom1, null);
                TextView tv_item_classroom_date = (TextView) layout.findViewById(R.id.tv_item_classroom_date);
                TextView tv_item_classroom_time = (TextView) layout.findViewById(R.id.tv_item_classroom_time);
                View v_item_classroom = layout.findViewById(R.id.v_item_classroom);
                v_item_classroom.setVisibility(View.VISIBLE);
                tv_item_classroom_date.setText(courseParticularsInfo.getDate());
                tv_item_classroom_time.setText(StringUtils.changeTime(courseParticularsInfo.getStart_time()) + "-" +
                        StringUtils.changeTime(courseParticularsInfo.getEnd_time()));

                if (getString(R.string.txt_no_complete).equals(status) || getString(R.string.txt_has_been_used).equals(status)) {
                    LinearLayout ll_item_class_content = (LinearLayout) layout.findViewById(R.id.ll_item_class_content);
                    RelativeLayout layout_inner = (RelativeLayout) LayoutInflater.from(
                            UIUtils.getContext()).inflate(
                            R.layout.item_classroom1_inner, null);
                    TextView tv_item_classroom_class = (TextView) layout_inner.findViewById(R.id.tv_item_classroom_class);
                    ImageView iv_item_classroom_share = (ImageView) layout_inner.findViewById(R.id.iv_item_classroom_share);
                    View v_item_classroom_inner = layout_inner.findViewById(R.id.v_item_classroom_inner);
                    if (data.size() != 1 && i != data.size() - 1) {
                        v_item_classroom_inner.setVisibility(View.VISIBLE);
                    }
                    tv_item_classroom_class.setText(String.format(getString(R.string.txt_classroom), courseParticularsInfo.getNo()));
                    ll_item_class_content.addView(layout_inner);
                    if (getString(R.string.txt_no_complete).equals(status)) {
                        iv_item_classroom_share.setVisibility(View.VISIBLE);
                        iv_item_classroom_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                UIUtils.showToastSafe("分享");
                            }
                        });
                    } else if (getString(R.string.txt_has_been_used).equals(status)) {
                        iv_item_classroom_share.setVisibility(View.GONE);

                    }
                }

                ll_parent.addView(layout, i);
            }
        }
    }


    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_particulars;
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

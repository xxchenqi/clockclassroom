package com.yiju.ClassClockRoom.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.CourseDetailActivity;
import com.yiju.ClassClockRoom.act.CourseMoreActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.adapter.CourseAdapter;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.CourseData;
import com.yiju.ClassClockRoom.bean.SpecialInfo;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释: 课程首页
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/6/17 9:54
 * ----------------------------------------
 */
public class CourseFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    //lv
    private ListView lv_course;
    //适配器
    private CourseAdapter courseAdapter;
    //数据源
    private List<Object> objectList = new ArrayList<>();
    //标题
    private TextView head_title_txt;
    //loading动画
    private AnimationDrawable drawable;
    //动画布局
    private LinearLayout ll_index;
    //动画图片
    private ImageView loadingImageView;
    //无WIFI显示界面
    private RelativeLayout ly_wifi;
    //刷新
    private Button btn_no_wifi_refresh;
    //无wifi图片
    private ImageView iv_no_wifi;
    //无wifi内容1
    private TextView tv_no_wifi_content1;
    //无wifi内容2
    private TextView tv_no_wifi_content2;
    //是否是第一次加载
    private boolean isFirst = true;
    //专题数据
    private List<SpecialInfo> specials;
    //缓存数据
    private String cache_course_data;

    @Override
    protected void initView() {
        cache_course_data = SharedPreferencesUtils.getString(getActivity(), SharedPreferencesConstant.Shared_Course_Data, null);
        lv_course = (ListView) currentView.findViewById(R.id.lv_course);
        head_title_txt = (TextView) currentView.findViewById(R.id.head_title_txt);
        ll_index = (LinearLayout) currentView.findViewById(R.id.ll_index);
        loadingImageView = (ImageView) currentView.findViewById(R.id.loadingImageView);
        ly_wifi = (RelativeLayout) currentView.findViewById(R.id.ly_wifi);
        btn_no_wifi_refresh = (Button) currentView.findViewById(R.id.btn_no_wifi_refresh);
        iv_no_wifi = (ImageView) currentView.findViewById(R.id.iv_no_wifi);
        tv_no_wifi_content1 = (TextView) currentView.findViewById(R.id.tv_no_wifi_content1);
        tv_no_wifi_content2 = (TextView) currentView.findViewById(R.id.tv_no_wifi_content2);
        drawable = (AnimationDrawable) loadingImageView.getDrawable();
    }

    @Override
    protected void initData() {
        head_title_txt.setText(R.string.course);
        courseAdapter = new CourseAdapter(objectList);
        lv_course.setAdapter(courseAdapter);
        if (cache_course_data != null) {
            if (isFirst) {
                processData(cache_course_data);
                getData();
            }
        } else {
            if (NetWorkUtils.getNetworkStatus(getActivity())) {
                ly_wifi.setVisibility(View.GONE);
                lv_course.setVisibility(View.VISIBLE);
                if (isFirst) {
                    getData();
                }
            } else {
                ly_wifi.setVisibility(View.VISIBLE);
                lv_course.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        lv_course.setOnItemClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
    }

    /**
     * 获取本页数据
     */
    private void getData() {
        if (isFirst) {
            ll_index.setVisibility(View.VISIBLE);
            drawable.start();
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_course_index_list");
        if (!"".equals(NewIndexFragment.sid)) {
            params.addBodyParameter("sid", NewIndexFragment.sid);
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        drawable.stop();
                        ll_index.setVisibility(View.GONE);
                        lv_course.setVisibility(View.GONE);
                        UIUtils.checkWifiAndModifyWifi(ly_wifi, iv_no_wifi, tv_no_wifi_content1, tv_no_wifi_content2);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        SharedPreferencesUtils.saveString(getActivity(), SharedPreferencesConstant.Shared_Course_Data, arg0.result);
                        processData(arg0.result);
                    }
                }
        );
    }

    /**
     * 处理返回的数据
     *
     * @param result 结果集
     */
    private void processData(String result) {
        lv_course.setVisibility(View.VISIBLE);
        drawable.stop();
        ll_index.setVisibility(View.GONE);
        if (null != result) {
            CourseData coursedata = GsonTools.changeGsonToBean(result, CourseData.class);
            if (null != coursedata) {
                if (coursedata.getCode() == 1) {
                    isFirst = false;
                    objectList.clear();
                    CourseData.Special_Course data = coursedata.getData();
                    specials = data.getSpecial();//专题
                    List<CourseInfo> courses = data.getCourse();//课程
                    if (null != specials && specials.size() > 0) {
                        objectList.add(UIUtils.getString(R.string.label_course_special));
                        objectList.addAll(specials);
                    }
                    if (null != courses && courses.size() > 0) {
                        objectList.add(UIUtils.getString(R.string.course));
                        //首页最多只显示4个
//                        if (courses.size() > 4) {
//                            for (int i = 0; i < 4; i++) {
//                                objectList.add(courses.get(i));
//                            }
//                        } else {
//                            objectList.addAll(courses);
//                        }
                        objectList.addAll(courses);
                    }
                    courseAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public int setContentViewId() {
        return R.layout.fragment_course;
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_fragment_course);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = objectList.get(position);
        if (o instanceof String) {
            //更多
            if (String.valueOf(o).equals("课程")) {
                Intent intent = new Intent(UIUtils.getContext(), CourseMoreActivity.class);
                UIUtils.startActivity(intent);
            }
        } else if (o instanceof SpecialInfo) {
            //专题
            SpecialInfo specialInfo = (SpecialInfo) o;
            Intent i = new Intent(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            i.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.WEB_value_special_course_Page);
            i.putExtra(UIUtils.getString(R.string.redirect_open_url), specialInfo.getUrl());
            i.putExtra("special_id", specialInfo.getId());
            startActivity(i);
        } else if (o instanceof CourseInfo) {
            CourseInfo courseInfo = (CourseInfo) o;
            Intent intent = new Intent(UIUtils.getContext(), CourseDetailActivity.class);
            intent.putExtra("COURSE_ID", courseInfo.getId());
            UIUtils.startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(getActivity())) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_course.setVisibility(View.VISIBLE);
                    getData();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_course.setVisibility(View.GONE);
                }
                break;
        }
    }
}

package com.yiju.ClassClockRoom.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.yiju.ClassClockRoom.act.TeacherDetailActivity;
import com.yiju.ClassClockRoom.act.TeacherMoreActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.adapter.TeacherAdapter;
import com.yiju.ClassClockRoom.bean.SpecialTeacherInfo;
import com.yiju.ClassClockRoom.bean.TeacherData;
import com.yiju.ClassClockRoom.bean.TeacherInfoBean;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class TeacherFragment extends BaseFragment implements OnClickListener, AdapterView.OnItemClickListener {
    //搜索
//    private ImageView iv_teacher_search;
    //lv
    private ListView lv_teacher;
    //适配器
    private TeacherAdapter teacherAdapter;
    //数据源
    private List<Object> objectList = new ArrayList<>();
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
    //老师缓存数据
    private String cache_teacher_data;

    @Override
    public void initView() {
        cache_teacher_data = SharedPreferencesUtils.getString(getActivity(), SharedPreferencesConstant.Shared_Teacher_Data, null);
//        iv_teacher_search = (ImageView) currentView.findViewById(R.id.iv_teacher_search);
        lv_teacher = (ListView) currentView.findViewById(R.id.lv_teacher);
        ll_index = (LinearLayout) currentView.findViewById(R.id.ll_index);
        loadingImageView = (ImageView) currentView.findViewById(R.id.loadingImageView);
        ly_wifi = (RelativeLayout) currentView.findViewById(R.id.ly_wifi);
        btn_no_wifi_refresh = (Button) currentView.findViewById(R.id.btn_no_wifi_refresh);
        iv_no_wifi = (ImageView) currentView.findViewById(R.id.iv_no_wifi);
        tv_no_wifi_content1 = (TextView) currentView.findViewById(R.id.tv_no_wifi_content1);
        tv_no_wifi_content2 = (TextView) currentView.findViewById(R.id.tv_no_wifi_content2);
        drawable = (AnimationDrawable) loadingImageView.getDrawable();

        teacherAdapter = new TeacherAdapter(objectList);
        lv_teacher.setAdapter(teacherAdapter);
    }

    @Override
    protected void initData() {
        if (cache_teacher_data != null) {
            if (isFirst) {
                processData(cache_teacher_data);
                getData();
            }
        } else {
            if (NetWorkUtils.getNetworkStatus(getActivity())) {
                ly_wifi.setVisibility(View.GONE);
                lv_teacher.setVisibility(View.VISIBLE);
                if (isFirst) {
                    getData();
                }
            } else {
                ly_wifi.setVisibility(View.VISIBLE);
                lv_teacher.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        lv_teacher.setOnItemClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
//        iv_teacher_search.setOnClickListener(this);
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
//        params.addBodyParameter("action", "get_teacher_index_list");
        params.addBodyParameter("version", UrlUtils.API_VERSION);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_TEACHER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        drawable.stop();
                        ll_index.setVisibility(View.GONE);
                        lv_teacher.setVisibility(View.GONE);
                        UIUtils.checkWifiAndModifyWifi(ly_wifi,iv_no_wifi,tv_no_wifi_content1,tv_no_wifi_content2);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        SharedPreferencesUtils.saveString(getActivity(), SharedPreferencesConstant.Shared_Teacher_Data, arg0.result);
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
        lv_teacher.setVisibility(View.VISIBLE);
        if (isFirst) {
            drawable.stop();
            ll_index.setVisibility(View.GONE);
        }
        if (null != result) {
            TeacherData teacherdata = GsonTools.changeGsonToBean(result, TeacherData.class);
            if (null != teacherdata) {
                if ("1".equals(teacherdata.getCode())) {
                    isFirst = false;
                    objectList.clear();
                    TeacherData.Teacher_Special data = teacherdata.getData();
                    List<SpecialTeacherInfo> specials = data.getSpecial();
                    List<TeacherInfoBean> organizations = data.getTeacher().getOrganization();
                    List<TeacherInfoBean> personals = data.getTeacher().getPersonal();

                    if (null != specials && specials.size() > 0) {
                        objectList.add(UIUtils.getString(R.string.label_teacher_special));
                        objectList.addAll(specials);
                    }
                    if (null != organizations && organizations.size() > 0) {
                        objectList.add(UIUtils.getString(R.string.teacher_orangan));
                        objectList.addAll(organizations);
                    }

                    if (null != personals && personals.size() > 0) {
                        objectList.add(UIUtils.getString(R.string.teacher_person));
                        objectList.addAll(personals);
                    }
                    teacherAdapter.notifyDataSetChanged();
                } else {
                    UIUtils.showToastSafe(teacherdata.getMsg());
                }
            }
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_fragment_teacher);
    }

    @Override
    public int setContentViewId() {
        return R.layout.fragment_teacher;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_teacher_search://搜索
//                Intent intent = new Intent(UIUtils.getContext(),
//                        SearchActivity.class);
//                startActivity(intent);
//                break;
            case R.id.btn_no_wifi_refresh://刷新
                if (NetWorkUtils.getNetworkStatus(getActivity())) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_teacher.setVisibility(View.VISIBLE);
                    getData();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_teacher.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = objectList.get(position);
        if (o instanceof String) {
            String s = String.valueOf(o);
            //机构老师，更多
            if (UIUtils.getString(R.string.teacher_orangan).equals(s)) {
                Intent intent = new Intent(UIUtils.getContext(), TeacherMoreActivity.class);
                intent.putExtra(TeacherMoreActivity.ACTION_TYPE, TeacherMoreActivity.ORGANIZATION_TYPE);
                startActivity(intent);
            } else if (UIUtils.getString(R.string.teacher_person).equals(s)) {
                //个人老师更多
                Intent intent = new Intent(UIUtils.getContext(), TeacherMoreActivity.class);
                intent.putExtra(TeacherMoreActivity.ACTION_TYPE, TeacherMoreActivity.PERSONAL_TYPE);
                startActivity(intent);
            }
        } else if (o instanceof SpecialTeacherInfo) {
            //专题
            SpecialTeacherInfo specialTecherInfo = (SpecialTeacherInfo) o;
            Intent i = new Intent(UIUtils.getContext(),
                    Common_Show_WebPage_Activity.class);
            i.putExtra(UIUtils.getString(R.string.get_page_name),
                    WebConstant.WEB_value_special_teacher_Page);
            i.putExtra(UIUtils.getString(R.string.redirect_open_url), specialTecherInfo.getUrl());
            i.putExtra("special_id", specialTecherInfo.getId());
            startActivity(i);
        } else if (o instanceof TeacherInfoBean) {
            Intent intent = new Intent(getActivity(), TeacherDetailActivity.class);
            intent.putExtra("id", ((TeacherInfoBean) o).getId());
            startActivity(intent);
        }
    }
}

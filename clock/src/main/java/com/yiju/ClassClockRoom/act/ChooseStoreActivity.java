package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yiju.ClassClockRoom.adapter.ChooseStoreAdapter;
import com.yiju.ClassClockRoom.bean.ChooseStoreBean;
import com.yiju.ClassClockRoom.bean.SchoolAll;
import com.yiju.ClassClockRoom.bean.SchoolLeft;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.windows.MoreStorePopUpWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:选择门店
 * <p/>
 * 作者: sandy
 * <p/>
 * 时间: on 2016/7/5 11:26
 * ----------------------------------------
 */
public class ChooseStoreActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ListItemClickHelp {
    //传参
    public static final String ACTION_SID = "sid";
    @ViewInject(R.id.ll_title)
    private LinearLayout ll_title;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //lv
    @ViewInject(R.id.lv_choose_store)
    private PullToRefreshListView lv_choose_store;
    //筛选
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //筛选文字
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //图片
    @ViewInject(R.id.head_right_image)
    private ImageView head_right_image;
    //空图片
    @ViewInject(R.id.iv_empty_stores)
    private ImageView iv_empty_stores;
    //无WIFI显示界面
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    //刷新
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
    //数据源
    private List<ChooseStoreBean.DataEntity> data;
    //适配器
    private ChooseStoreAdapter adapter;
    //sid
    private String sid;
    //筛选数据
    private List<SchoolLeft> datas_filtrate;
    //区号
    private String dist_id;
    //经度
    private Double lng_g;
    //纬度
    private Double lat_g;
    //当前页
    private int start = 0;
    //每页记录数
    private int limit = 10;
    //刷新的标记为，默认为下拉刷新
    private boolean is_down_refresh = true;
    //pop
    private MoreStorePopUpWindow popUpWindow;
    private boolean from_prepaid;
    private View footView;

    @Override
    public void initIntent() {
        super.initIntent();
        sid = getIntent().getStringExtra(ACTION_SID);
        from_prepaid = getIntent().getBooleanExtra("from_prepaid", false);
    }

    @Override
    protected void initView() {
        lng_g = LocationSingle.getInstance().getLongitude();
        lat_g = LocationSingle.getInstance().getLatitude();
        footView = View.inflate(UIUtils.getContext(), R.layout.include_no_more, null);
        footView.setVisibility(View.GONE);
        ListView refreshableView = lv_choose_store.getRefreshableView();
        FrameLayout footerLayoutHolder = new FrameLayout(UIUtils.getContext());
        footerLayoutHolder.addView(footView);
        refreshableView.addFooterView(footerLayoutHolder);
    }

    @Override
    protected void initData() {

        if (from_prepaid) {
            head_title.setText(UIUtils.getString(R.string.txt_consumption_stores));
            head_right_relative.setVisibility(View.INVISIBLE);
        } else {
            head_title.setText(UIUtils.getString(R.string.choose_store));
            head_right_relative.setVisibility(View.VISIBLE);
        }
        data = new ArrayList<>();
        datas_filtrate = new ArrayList<>();
        adapter = new ChooseStoreAdapter(this, data, R.layout.item_choose_store);
        lv_choose_store.setAdapter(adapter);
        showHaveStore();

        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            lv_choose_store.setVisibility(View.VISIBLE);
            getHttpUtilsFiltrate();
            getHttpUtils();
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            lv_choose_store.setVisibility(View.GONE);
        }

        //刷新设置
        lv_choose_store.setMode(PullToRefreshBase.Mode.BOTH);
        lv_choose_store.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                is_down_refresh = true;
                start = 0;
                getHttpUtils();
                //如果数据全部已请求完后，lv设置只能下拉刷新，重新下拉刷新后再去打开上拉加载
                lv_choose_store.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                is_down_refresh = false;
                start += 10;
                getHttpUtils();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        lv_choose_store.setOnItemClickListener(this);
        head_right_relative.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
    }


    /**
     * 获取门店请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_school");
        if (dist_id != null && !"-1".equals(dist_id)) {
            params.addBodyParameter("dist_id", dist_id);
        }
        if ((lng_g != null && lng_g != 0.0) && (lat_g != null && lat_g != 0.0)) {
            params.addBodyParameter("lng_g", lng_g + "");
            params.addBodyParameter("lat_g", lat_g + "");
        }
//        params.addBodyParameter("is_test", "1");//是否出现测试门店
        params.addBodyParameter("limit", start + "," + limit);
        if (from_prepaid) {
            params.addBodyParameter("school_type", "1");//门店类型 1=直营 2=合作
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_COURSE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        lv_choose_store.onRefreshComplete();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        lv_choose_store.onRefreshComplete();
                        processData(arg0.result);
                    }
                });
    }

    private void processData(String result) {
        //请求前停止handler
        ChooseStoreBean chooseStoreBean = GsonTools.changeGsonToBean(result,
                ChooseStoreBean.class);
        if (chooseStoreBean == null) {
            return;
        }

        if ("1".equals(chooseStoreBean.getCode())) {
            List<ChooseStoreBean.DataEntity> choose_data = chooseStoreBean.getData();

            if (null != choose_data && choose_data.size() > 0) {
                //如果是下拉刷新，数据全部清空
                if (is_down_refresh) {
                    data.clear();
                }
                data.addAll(choose_data);
                showHaveStore();
                adapter.notifyDataSetChanged();
                lv_choose_store.onRefreshComplete();
                if (choose_data.size() < 10){
                    lv_choose_store.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    footView.setVisibility(View.VISIBLE);
                }
            } else {
                if (!is_down_refresh) {
                    lv_choose_store.onRefreshComplete();
                    lv_choose_store.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    footView.setVisibility(View.VISIBLE);
                } else {
                    data.clear();
                    adapter.notifyDataSetChanged();
                }
            }
            is_down_refresh = true;
        } else {
            UIUtils.showToastSafe(chooseStoreBean.getMsg());
        }
    }

    private void showHaveStore() {
        if (sid != null) {
            for (int i = 0; i < data.size(); i++) {
                if (sid.equals(data.get(i).getSid())) {
                    data.get(i).setFlag(true);
                    break;
                }
            }
        }
    }

    /**
     * 地址筛选请求
     */
    private void getHttpUtilsFiltrate() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "area_school_list");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData2(arg0.result);
                    }
                }
        );
    }

    private void processData2(String result) {
        SchoolAll schoolAll = GsonTools.changeGsonToBean(result, SchoolAll.class);
        if (schoolAll == null) {
            return;
        }
        if ("1".equals(schoolAll.getCode())) {
            datas_filtrate.addAll(schoolAll.getData());
            datas_filtrate.get(0).setFlag(true);
            popUpWindow = new MoreStorePopUpWindow(this, datas_filtrate, this, head_right_image);
        }

    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_publish_first_choose_store_Page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_choose_store;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_relative:
                if (popUpWindow != null) {
                    head_right_image.setImageResource(R.drawable.arrow_up);
                    popUpWindow.showAsDropDown(ll_title);
                }
                break;
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_choose_store.setVisibility(View.VISIBLE);
                    getHttpUtilsFiltrate();
                    getHttpUtils();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_choose_store.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(ACTION_SID, data.get(position - 1));
        setResult(4, intent);
        finish();
    }

    @Override
    public void onClickItem(int position) {
        lv_choose_store.setMode(PullToRefreshBase.Mode.BOTH);
        data.clear();
        adapter.notifyDataSetChanged();
        dist_id = datas_filtrate.get(position).getId();
        head_right_text.setText(datas_filtrate.get(position).getDist_name());
        is_down_refresh = true;
        start = 0;
        getHttpUtils();
    }
}

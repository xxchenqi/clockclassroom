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
import com.yiju.ClassClockRoom.adapter.MoreStoreAdapter;
import com.yiju.ClassClockRoom.bean.MoreStoreBean;
import com.yiju.ClassClockRoom.bean.MoreStoreBean.MoreStoreEntity;
import com.yiju.ClassClockRoom.bean.SchoolAll;
import com.yiju.ClassClockRoom.bean.SchoolLeft;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.fragment.IndexFragment;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;
import com.yiju.ClassClockRoom.widget.windows.MoreStorePopUpWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释:更多门店
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/9/2 17:04
 * ----------------------------------------
 */
public class MoreStoreActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ListItemClickHelp {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //筛选
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //图片
    @ViewInject(R.id.head_right_image)
    private ImageView head_right_image;
    //筛选文字
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //lv
    @ViewInject(R.id.lv_more_store)
    private PullToRefreshListView lv_more_store;
    //view
    @ViewInject(R.id.v_store_divider)
    private View v_store_divider;
    //空图片
    @ViewInject(R.id.tv_empty_stores)
    private TextView tv_empty_stores;
    //无WIFI显示界面
    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;
    //刷新
    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;
    //数据
    private List<MoreStoreEntity> datas;
    //筛选数据
    private List<SchoolLeft> datas_filtrate;
    //适配器
    private MoreStoreAdapter adapter;
    //区号
    private String dist_id;
    /*//经度
    private Double lng_g;
    //纬度
    private Double lat_g;*/
    //当前页
    private int page = 1;
    //每页记录数
    private String rows = "10";
    //刷新的标记为，默认为下拉刷新
    private boolean is_down_refresh = true;
    //pop
    private MoreStorePopUpWindow popUpWindow;

    @Override
    protected void initView() {
        /*lng_g = LocationSingle.getInstance().getLongitude();
        lat_g = LocationSingle.getInstance().getLatitude();*/
    }

    @Override
    protected void initData() {
        datas = new ArrayList<>();
        datas_filtrate = new ArrayList<>();
        adapter = new MoreStoreAdapter(this, datas, R.layout.item_more_store_list);
        lv_more_store.setAdapter(adapter);
        //请求
        if (NetWorkUtils.getNetworkStatus(this)) {
            ly_wifi.setVisibility(View.GONE);
            lv_more_store.setVisibility(View.VISIBLE);
            getHttpUtilsFiltrate();
            getHttpUtils();
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            lv_more_store.setVisibility(View.GONE);
        }


        //刷新设置
        lv_more_store.setMode(PullToRefreshBase.Mode.BOTH);
        lv_more_store.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                is_down_refresh = true;
                getHttpUtils();
                //如果数据全部已请求完后，lv设置只能下拉刷新，重新下拉刷新后再去打开上拉加载
                lv_more_store.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                is_down_refresh = false;
                page++;
                getHttpUtils();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        lv_more_store.setOnItemClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_store_list_Page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_mores_tore;
    }

    /**
     * 更多门店请求
     */
    private void getHttpUtils() {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        if (dist_id != null && !"-1".equals(dist_id)) {
            params.addBodyParameter("dist_id", dist_id);
        }
        if (StringUtils.isNotNullString(IndexFragment.lat) && StringUtils.isNotNullString(IndexFragment.lng)) {
            params.addBodyParameter("lng_g", IndexFragment.lng);
            params.addBodyParameter("lat_g", IndexFragment.lat);
        }
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("rows", rows);
        params.addBodyParameter("newpic", "1");

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.JAVA_MORE_STORE, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ProgressDialog.getInstance().dismiss();
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        lv_more_store.onRefreshComplete();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ProgressDialog.getInstance().dismiss();
                        // 处理返回的数据
                        lv_more_store.onRefreshComplete();
                        processData(arg0.result);
                    }
                }
        );
    }

    private void processData(String result) {
        MoreStoreBean moreStoreBean = GsonTools.changeGsonToBean(result,
                MoreStoreBean.class);
        if (moreStoreBean == null) {
            return;
        }
        if (moreStoreBean.getCode() == 0) {
            if (is_down_refresh) {
                //是下拉刷新就清除数据
                datas.clear();
            }
            if (moreStoreBean.getObj().size() != 0) {
                datas.addAll(moreStoreBean.getObj());
                adapter.notifyDataSetChanged();
            } else {
                lv_more_store.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            lv_more_store.setVisibility(View.VISIBLE);
            tv_empty_stores.setVisibility(View.GONE);
        } else {
            //没有查到
            if (is_down_refresh) {
                lv_more_store.setVisibility(View.GONE);
                tv_empty_stores.setVisibility(View.VISIBLE);
            } else {
                lv_more_store.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                UIUtils.showLongToastSafe(moreStoreBean.getMsg());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            case R.id.head_right_relative:
                if (popUpWindow != null) {
                    head_right_image.setImageResource(R.drawable.arrow_up);
                    popUpWindow.showAsDropDown(v_store_divider);
                }
                break;
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(this)) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_more_store.setVisibility(View.VISIBLE);
                    getHttpUtilsFiltrate();
                    getHttpUtils();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_more_store.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (datas == null) {
            return;
        }
        //点击去门店详情
        int sid = datas.get(position - 1).getId();
        Intent intent = new Intent(this, StoreDetailActivity.class);
        intent.putExtra(ExtraControl.EXTRA_STORE_ID, sid + "");
        startActivity(intent);
    }


    @Override
    public void onClickItem(int position) {
        lv_more_store.setMode(PullToRefreshBase.Mode.BOTH);
        datas.clear();
        adapter.notifyDataSetChanged();
        dist_id = datas_filtrate.get(position).getId();
        head_right_text.setText(datas_filtrate.get(position).getDist_name());
        is_down_refresh = true;
        page = 1;
        getHttpUtils();
    }
}

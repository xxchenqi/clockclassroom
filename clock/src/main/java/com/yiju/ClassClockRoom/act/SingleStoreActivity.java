package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.adapter.SingleStoreAdapter;
import com.yiju.ClassClockRoom.bean.ClassRoom;
import com.yiju.ClassClockRoom.bean.Room;
import com.yiju.ClassClockRoom.bean.StoreShareBean;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.map.NavigationUtils;
import com.yiju.ClassClockRoom.control.share.ShareDialog;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.StoreImageView;

import java.util.List;

/**
 * 单一门店课室列表页
 * Created by wh on 2016/9/5.
 */
public class SingleStoreActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;

//    @ViewInject(R.id.head_right_relative)
//    private RelativeLayout head_right_relative;

    @ViewInject(R.id.head_title)
    private TextView head_title;

    @ViewInject(R.id.lv_single_store)
    private ListView lv_single_store;

    @ViewInject(R.id.ly_wifi)
    private RelativeLayout ly_wifi;

    @ViewInject(R.id.iv_no_wifi)
    private ImageView iv_no_wifi;

    @ViewInject(R.id.tv_no_wifi_content1)
    private TextView tv_no_wifi_content1;

    @ViewInject(R.id.tv_no_wifi_content2)
    private TextView tv_no_wifi_content2;

    @ViewInject(R.id.btn_no_wifi_refresh)
    private Button btn_no_wifi_refresh;

    @ViewInject(R.id.head_share)
    private ImageView head_share;

    private String sid;
    private String school_type = "";
    private String store_name;
    private Room room;
    private StoreShareBean share;

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        school_type = intent.getStringExtra("school_type");
        store_name = intent.getStringExtra("store_name");
    }

    @Override
    protected void initView() {
        head_title.setText(store_name);
        if (!TextUtils.isEmpty(school_type) && "1".equals(school_type)) {
            Drawable drawable = UIUtils.getDrawable(R.drawable.ziying);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                head_title.setCompoundDrawables(drawable, null, null, null);
                head_title.setCompoundDrawablePadding(10);
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
//        head_right_relative.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        head_share.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (NetWorkUtils.getNetworkStatus(UIUtils.getContext())) {
            ly_wifi.setVisibility(View.GONE);
            getHttpRequest();
        } else {
            iv_no_wifi.setImageResource(R.drawable.none_wifi);
            tv_no_wifi_content1.setText(UIUtils.getString(R.string.nowifi_content1));
            tv_no_wifi_content2.setText(UIUtils.getString(R.string.nowifi_content2));
            ly_wifi.setVisibility(View.VISIBLE);
        }
    }

    private void getHttpRequest() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "school_single");// action=school_list
        params.addBodyParameter("newpic", "1");//新版读图新加参/*数
        params.addBodyParameter("is_ios", "1");//传1代表会返回高德坐标*/
        params.addBodyParameter("sid", sid);//门店id
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_SCHOOL_DETAIL, params,
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
                }
        );
    }

    private void processData(String result) {
        ClassRoom classRoom = GsonTools.changeGsonToBean(result, ClassRoom.class);
        if (classRoom == null) {
            return;
        }
        if ("1".equals(classRoom.getCode())) {
            List<Room> mLists = classRoom.getData();
            if (mLists == null || mLists.size() == 0) {
                return;
            }
            room = mLists.get(0);
            initHeadView(mLists.get(0));
            share = mLists.get(0).getShare();
            SharedPreferencesUtils.saveString(this, getString(R.string.shared_data), GsonTools.createGsonString(share));
        }
    }

    public void initHeadView(final Room room) {
        if (room == null) {
            return;
        }
        View headView = View.inflate(this, R.layout.item_single_store_image_layout, null);
        StoreImageView iv_single_store_image = (StoreImageView) headView.findViewById(R.id.iv_single_store_image);
        LinearLayout ll_single_store_imgs = (LinearLayout) headView.findViewById(R.id.ll_single_store_imgs);
        TextView tv_single_store_tags = (TextView) headView.findViewById(R.id.tv_single_store_tags);
        TextView tv_single_store_address = (TextView) headView.findViewById(R.id.tv_single_store_address);
        TextView tv_store_detial = (TextView) headView.findViewById(R.id.tv_store_detial);
        iv_single_store_image.setImgResource(room.getPic_big());
        tv_single_store_tags.setText(room.getTags());
        tv_single_store_address.setText(room.getAddress());
        iv_single_store_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIUtils.getContext(),
                        IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sid", room.getId());
                bundle.putString("name", room.getName());
                bundle.putString("address", room.getAddress());
                bundle.putString("tags", room.getTags());
                bundle.putString("school_type", room.getSchool_type());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ll_single_store_imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (room.getRoom_type() == null || room.getRoom_type().size() <= 0) {
                    return;
                }
                String url_1 = UrlUtils.SERVER_WEB_PICDES
                        + "sid=" + room.getId()
                        + "&typeid=" + room.getRoom_type().get(0).getId()
                        + "&piclist=piclist";
                String title_1 = room.getName() + UIUtils.getString(R.string.pic);
                int pageValue_1 = WebConstant.Picdes_Page;
                gotoWebPage(url_1, title_1, pageValue_1, "");
            }
        });
        tv_single_store_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isHaveBaidu = NavigationUtils.isInstallByread("com.baidu.BaiduMap");
                boolean isHaveGaoDe = NavigationUtils.isInstallByread("com.autonavi.minimap");
                String haveMap;
                if (!isHaveBaidu && !isHaveGaoDe) {
                    haveMap = "0";
                } else {
                    haveMap = "1";
                }
                String url = UrlUtils.SERVER_WEB_TO_CLASSDETAIL
                        + "trapmap=trapmap&title=" + room.getName()
                        + "&to=" + room.getLng_bd() + "," + room.getLat_bd()
                        + "&to_g=" + room.getLng() + "," + room.getLat()
                        + "&address=" + room.getAddress() + "&havemap=" + haveMap;
                String title = room.getName();
                int pageValue = WebConstant.WEB_Int_Map_Page;

                gotoWebPage(url, title, pageValue, "");
            }
        });
        tv_store_detial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (room == null) {
                    return;
                }
                //门店概况
                Intent intent = new Intent(UIUtils.getContext(),
                        IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sid", room.getId());
                bundle.putString("name", room.getName());
                bundle.putString("address", room.getAddress());
                bundle.putString("tags", room.getTags());
                bundle.putString("school_type", room.getSchool_type());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lv_single_store.addHeaderView(headView, null, false);
        lv_single_store.setAdapter(new SingleStoreAdapter(room));
    }

    /**
     * 跳转到导航页h5
     */
    private void gotoWebPage(String url, String title, int pageValue,
                             String tid) {
        Intent intent = new Intent(
                UIUtils.getContext(),
                Common_Show_WebPage_Activity.class
        );
        intent.putExtra(
                Common_Show_WebPage_Activity.Param_String_Title,
                title
        );
        intent.putExtra(
                UIUtils.getString(R.string.redirect_open_url),
                url
        );
        intent.putExtra(
                UIUtils.getString(R.string.get_page_name),
                pageValue
        );
        intent.putExtra(
                UIUtils.getString(R.string.redirect_tid),
                tid
        );
        startActivity(intent);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_index_detail);
    }


    @Override
    public int setContentViewId() {
        return R.layout.activity_single_store;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            case R.id.btn_no_wifi_refresh://刷新
                initData();
            case R.id.head_share://分享
                ShareDialog
                        .getInstance()
                        .setCurrent_Type(ShareDialog.Type_Share_Store_Detail)
                        .showDialog();
                break;
        }
    }
}

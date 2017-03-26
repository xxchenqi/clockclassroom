package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.adapter.CouponAdapter;
import com.yiju.ClassClockRoom.bean.Coupon;
import com.yiju.ClassClockRoom.bean.Coupon.CouponDataEntity;
import com.yiju.ClassClockRoom.bean.CouponPrice;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.LogUtil;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class CouponActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "coupon";
    // 页面返回键
    @ViewInject(R.id.iv_back_coupon)
    private ImageView iv_back_coupon;

    // 优惠券使用说明（跳转h5页面）
    @ViewInject(R.id.iv_coupon_desc)
    private ImageView iv_coupon_desc;

    // 优惠券列表
    @ViewInject(R.id.list_coupon)
    private ListView list_coupon;

    // 添加（点击事件）
    @ViewInject(R.id.iv_coupon_add)
    private ImageView iv_coupon_add;

    // 背景
    @ViewInject(R.id.coupon_back)
    private LinearLayout coupon_back;
    @ViewInject(R.id.iv_coupon)
    private ImageView iv_coupon;
    @ViewInject(R.id.tv_coupon_content)
    private TextView tv_coupon_content;
    private String uid;
    private String order2_id;
    private List<CouponDataEntity> mLists = new ArrayList<>();
    private CouponAdapter adapter;

    @Override
    public int setContentViewId() {
        return R.layout.activity_coupon;
    }

    /**
     * 初始化页面
     */
    @Override
    public void initView() {
        iv_back_coupon.setOnClickListener(this);
        iv_coupon_desc.setOnClickListener(this);
        iv_coupon_add.setOnClickListener(this);
        list_coupon.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 使用优惠券
                mLists.get(arg2).setCheck(true);
                adapter.notifyDataSetChanged();
                useCoupon(mLists.get(arg2));
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        uid = getIntent().getStringExtra("uid");
        order2_id = getIntent().getStringExtra("order2_id");
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "user_coupon");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("order2_id", order2_id);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_COUPON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        LogUtil.d(TAG, "coupon+++++++++++++++++++++"
                                + arg0.result);
                        // 处理返回的数据
                        processData(arg0.result);
                    }

                });
    }

    /**
     * 展示优惠券列表
     *
     * @param result 服务器返回的结果集
     */
    private void processData(String result) {
        Coupon data = GsonTools.changeGsonToBean(result, Coupon.class);
        if (null != data) {
            if (data.getCode() == 1 && data.getMsg().equals("ok")) {
                List<CouponDataEntity> datas = data.getData();
                mLists.clear();
                mLists.addAll(datas);
                if (mLists.size() != 0) {
                    iv_coupon.setVisibility(View.GONE);
                    list_coupon.setVisibility(View.VISIBLE);
                    tv_coupon_content.setVisibility(View.VISIBLE);
                } else {
                    tv_coupon_content.setVisibility(View.GONE);
                    iv_coupon.setVisibility(View.VISIBLE);
                    list_coupon.setVisibility(View.GONE);
                }
                if (adapter == null) {
                    adapter = new CouponAdapter(this, mLists);
                } else {
                    adapter.notifyDataSetChanged();
                }
                list_coupon.setAdapter(adapter);
            }
        }

    }

    /**
     * 使用优惠券
     *
     * @param info 当前选择的优惠券
     */
    private void useCoupon(final CouponDataEntity info) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "coupon_fee");
        params.addBodyParameter("coupon_id", info.getId());
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("order2_id", order2_id);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_COUPON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 请求网络失败
                        UIUtils.showToastSafe(R.string.fail_network_request);

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        CouponPrice price = GsonTools.changeGsonToBean(
                                arg0.result, CouponPrice.class);
                        if (null != price) {
                            if (price.getCode() == 1
                                    && price.getMsg().equals("ok")) {
                                String couponPrice = price.getData();
                                Intent intent = new Intent();
                                intent.putExtra("couponPrice", couponPrice);
                                intent.putExtra("coupon_id", info.getId());
                                intent.putExtra("batch_name", info.getBatch_name());
                                setResult(2, intent);
                                finish();
                            }
                        }
                    }

                });
    }

    @Override
    public void onClick(View v) {
        // 处理点击事件
        switch (v.getId()) {
            case R.id.iv_back_coupon:
                Intent backIntent = new Intent();
                backIntent.putExtra("couponPrice", "0.00");
                setResult(2, backIntent);
                finish();
                break;
            case R.id.iv_coupon_desc:
                Intent intent = new Intent(
                        UIUtils.getContext(),
                        Common_Show_WebPage_Activity.class);
                intent.putExtra(
                        UIUtils.getString(R.string.get_page_name),
                        WebConstant.WEB_Int_Coupon_Page
                );
                startActivity(intent);
                break;
            case R.id.iv_coupon_add:
                // 跳转到个人中心的优惠券界面
                Intent i = new Intent(
                        UIUtils.getContext(),
                        PersonalCenter_CouponListActivity.class
                );
                startActivity(i);
                break;

            default:
                break;
        }
    }

//    private void requesrData(String uid, String coupon) {
//        // 绑定优惠券
//        HttpUtils httpUtils = new HttpUtils();
//
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("action", "coupon_bind");
//        params.addBodyParameter("uid", uid);
//        params.addBodyParameter("coupon_no", coupon);
//        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
//                new RequestCallBack<String>() {
//
//                    @Override
//                    public void onFailure(HttpException arg0, String arg1) {
//                        // 请求网络失败
//                        UIUtils.showToastSafe(R.string.fail_network_request);
//
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> arg0) {
//                        // 处理数据
//                        LogUtil.d(TAG, "优惠券绑定绑定绑定绑定绑定绑定绑定绑定" + arg0.result);
//                        if (arg0.result.equals("-1")) {
//                            UIUtils.showToastSafe("请输入正确的优惠券号码");
//                        } else if (arg0.result.equals("-4")) {
//                            UIUtils.showToastSafe("该优惠券已被绑定");
//                        }
//                        JSONObject jsonObject;
//                        try {
//                            jsonObject = new JSONObject(arg0.result);
//                            if (jsonObject.getInt("code") == 1) {
//                                adapter.notifyDataSetChanged();
//                                initData();
//                            } else {
//                                UIUtils.showToastSafe("优惠券号码已作废");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 监听返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intentback = new Intent();
            intentback.putExtra("couponPrice", "0.00");
            setResult(2, intentback);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_coupon);
    }
}

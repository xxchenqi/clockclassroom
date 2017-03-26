package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
import com.yiju.ClassClockRoom.adapter.AreasWheelAdapter;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

/**
 * --------------------------------------
 * <p/>
 * 注释:发票信息
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-21 下午4:39:34
 * <p/>
 * --------------------------------------
 */
public class InvoiceInformationActivity extends BaseActivity implements
        OnClickListener, TextWatcher, OnWheelChangedListener {

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
     * 公司信息
     */
    @ViewInject(R.id.et_invoice_information_company)
    private EditText et_invoice_information_company;
    /**
     * 详细地址
     */
    @ViewInject(R.id.et_invoice_detailed_address)
    private EditText et_invoice_detailed_address;
    /**
     * 联系方式
     */
    @ViewInject(R.id.et_invoice_contact_information)
    private EditText et_invoice_contact_information;
    /**
     * 确定
     */
    @ViewInject(R.id.btn_invoice_affirm)
    private Button btn_invoice_affirm;
    /**
     * 省市区et
     */
    @ViewInject(R.id.et_invoice_province)
    private EditText et_invoice_province;

    /**
     * 发票抬头
     */
    private String information_company;
    /**
     * 省、市、区
     */
    private String invoice_province;
    /**
     * 详细地址
     */
    private String detailed_address;
    /**
     * 联系信息
     */
    private String contact_information;

    /**
     * pop
     */
    private PopupWindow popupWindow;
    /**
     * 省的WheelView控件
     */
    private WheelView wv_province;
    /**
     * 市的WheelView控件
     */
    private WheelView wv_city;
    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, String[]> mCitiesDatasMap = new HashMap<>();
    /**
     * 当前省的名称
     */
    private String mCurrentProvinceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * popview
     */
    private View view_popview;
    /**
     * pop取消
     */
    private TextView tv_pop_cancel;
    /**
     * pop确定
     */
    private TextView tv_pop_affirm;
    /**
     * 多个订单id
     */
    private String oid;

    private AreasWheelAdapter areasWheelAdapter;

    private AreasWheelAdapter provinceWheelAdapter;

    private int pos = 0;

    @Override
    public int setContentViewId() {
        return R.layout.activity_invoice_information;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.invoice_title));
        boolean is_invoice = SharedPreferencesUtils.getBoolean(
                getApplicationContext(), "is_invoice", false);
        // 读取缓存,判断是否有写过缓存
        if (is_invoice) {
            information_company = SharedPreferencesUtils.getString(
                    getApplicationContext(), "invoice_head", "");
            invoice_province = SharedPreferencesUtils.getString(
                    getApplicationContext(), "invoice_province", "");
            detailed_address = SharedPreferencesUtils.getString(
                    getApplicationContext(), "invoice_address", "");
            contact_information = SharedPreferencesUtils.getString(
                    getApplicationContext(), "invoice_mobile", "");
            et_invoice_information_company.setText(information_company);
            et_invoice_province.setText(invoice_province);
            et_invoice_detailed_address.setText(detailed_address);
            et_invoice_contact_information.setText(contact_information);

            // 确定按钮设置可按
            btn_invoice_affirm.setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
            btn_invoice_affirm.setClickable(true);

        } else {
            // 确定按钮设置不可按
            btn_invoice_affirm
                    .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
            btn_invoice_affirm.setClickable(false);

        }

        head_back_relative.setOnClickListener(this);
        btn_invoice_affirm.setOnClickListener(this);
        et_invoice_province.setOnClickListener(this);

        et_invoice_information_company.addTextChangedListener(this);
        et_invoice_detailed_address.addTextChangedListener(this);
        et_invoice_contact_information.addTextChangedListener(this);

        // 初始化popWindow
        View contentView = getLayoutInflater().inflate(R.layout.pop_address,
                null);
        popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true);
        // 初始化popWindow里的控件
        wv_province = (WheelView) contentView.findViewById(R.id.wv_province);
        wv_city = (WheelView) contentView.findViewById(R.id.wv_city);

        // 初始化wv样式
        wv_province.setWheelBackground(R.drawable.wheel_bg);
        wv_province.setWheelForeground(R.drawable.wheel_val);
        wv_province.setShadowColor(0xFFFFFFFF, 0x80FFFFFF, 0x00FFFFFF);

        wv_city.setWheelBackground(R.drawable.wheel_bg);
        wv_city.setWheelForeground(R.drawable.wheel_val);
        wv_city.setShadowColor(0xFFFFFFFF, 0x80FFFFFF, 0x00FFFFFF);

        view_popview = contentView.findViewById(R.id.view_popview);
        tv_pop_cancel = (TextView) contentView.findViewById(R.id.tv_pop_cancel);
        tv_pop_affirm = (TextView) contentView.findViewById(R.id.tv_pop_affirm);
        view_popview.setOnClickListener(this);
        tv_pop_cancel.setOnClickListener(this);
        tv_pop_affirm.setOnClickListener(this);
        // 设置滚轮监听
        // 添加change事件
        wv_province.addChangingListener(this);
        // 添加change事件
        wv_city.addChangingListener(this);

    }

    @Override
    public void initData() {
        oid = getIntent().getStringExtra("oid");

        // 初始化滚轮数据
        mProvinceDatas = new String[]{"上海"};
        String[] mCitiesDatas = new String[]{"静安区", "闸北区", "虹口区", "杨浦区",
                "宝山区", "闵行区", "嘉定区", "浦东新区", "青浦区", "松江区", "金山区", "奉贤区",
                "普陀区", "黄浦区", "崇明县", "徐汇区", "长宁区"};
        mCitiesDatasMap.put(mProvinceDatas[0], mCitiesDatas);

        // 设置滚轮数据
        // wv_province.setViewAdapter(new ArrayWheelAdapter<String>(this,
        // mProvinceDatas));
        provinceWheelAdapter = new AreasWheelAdapter(this, mProvinceDatas);
        provinceWheelAdapter.setCurrentIndex(0);
        wv_province.setViewAdapter(provinceWheelAdapter);
        // 设置最大可见度的数量
        wv_province.setVisibleItems(5);
        wv_city.setVisibleItems(5);

        // 设置二级滚轮
        updateCities();
        updateAreas();

    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = wv_city.getCurrentItem();
        areasWheelAdapter.setCurrentIndex(pCurrent);
        mCurrentCityName = mCitiesDatasMap.get(mCurrentProvinceName)[pCurrent];
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = wv_province.getCurrentItem();
        mCurrentProvinceName = mProvinceDatas[pCurrent];
        String[] cities = mCitiesDatasMap.get(mCurrentProvinceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        // wv_city.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        areasWheelAdapter = new AreasWheelAdapter(this, cities);
        wv_city.setViewAdapter(areasWheelAdapter);
        // 设置当前的item位置
        wv_city.setCurrentItem(0);
    }

    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "invoice_add");
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("content", getString(R.string.invoice_detail));
        params.addBodyParameter("head", information_company);
        params.addBodyParameter("address", invoice_province + detailed_address);
        params.addBodyParameter("mobile", contact_information);
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null) {
            return;
        }

        CustomDialog customDialog = new CustomDialog(
                InvoiceInformationActivity.this,
                mineOrder.getMsg());
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    InvoiceInformationActivity.this.setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;

            case R.id.btn_invoice_affirm:
                // 确定
                getHttpUtils();
                // 写入缓存 invoice 抬头,省市区,地址,手机,is_invoice

                SharedPreferencesUtils.saveString(getApplicationContext(),
                        "invoice_head", information_company);
                SharedPreferencesUtils.saveString(getApplicationContext(),
                        "invoice_province", invoice_province);
                SharedPreferencesUtils.saveString(getApplicationContext(),
                        "invoice_address", detailed_address);
                SharedPreferencesUtils.saveString(getApplicationContext(),
                        "invoice_mobile", contact_information);
                SharedPreferencesUtils.saveBoolean(getApplicationContext(),
                        "is_invoice", true);

                break;

            case R.id.et_invoice_province:
                // 跳转popWindow
                popupWindow.showAtLocation(et_invoice_province, Gravity.CENTER, 0,
                        0);
                break;
            case R.id.view_popview:
                popupWindow.dismiss();
                // 没有设置恢复之前设置过的位置
                areasWheelAdapter.setCurrentIndex(pos);
                wv_city.setCurrentItem(pos);
                break;
            case R.id.tv_pop_cancel:
                popupWindow.dismiss();
                // 没有设置恢复之前设置过的位置
                areasWheelAdapter.setCurrentIndex(pos);
                wv_city.setCurrentItem(pos);
                break;

            case R.id.tv_pop_affirm:
                popupWindow.dismiss();
                String sum_name = mCurrentProvinceName + mCurrentCityName;
                et_invoice_province.setText(sum_name);
                pos = wv_city.getCurrentItem();
                if (et_invoice_information_company.length() != 0
                        && et_invoice_detailed_address.length() != 0
                        && et_invoice_contact_information.length() != 0
                        && et_invoice_province.length() != 0) {
                    btn_invoice_affirm.setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
                    btn_invoice_affirm.setClickable(true);
                } else {
                    btn_invoice_affirm
                            .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
                    btn_invoice_affirm.setClickable(false);
                }

                invoice_province = et_invoice_province.getText().toString();

                break;

            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        information_company = et_invoice_information_company.getText()
                .toString();
        detailed_address = et_invoice_detailed_address.getText().toString();
        contact_information = et_invoice_contact_information.getText()
                .toString();

        if (et_invoice_information_company.length() != 0
                && et_invoice_detailed_address.length() != 0
                && et_invoice_contact_information.length() != 0
                && et_invoice_province.length() != 0) {
            btn_invoice_affirm.setBackgroundResource(R.drawable.background_green_1eb482_radius_5);
            btn_invoice_affirm.setClickable(true);
        } else {
            btn_invoice_affirm
                    .setBackgroundResource(R.drawable.background_green_1eb482_radius_5_noclick);
            btn_invoice_affirm.setClickable(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wv_province) {
            updateCities();
        } else if (wheel == wv_city) {
            updateAreas();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_invoice_order_detail);
    }

}

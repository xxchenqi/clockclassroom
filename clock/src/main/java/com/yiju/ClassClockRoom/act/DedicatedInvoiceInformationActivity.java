package com.yiju.ClassClockRoom.act;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * ----------------------------------------
 * 注释:专用发票资质信息页
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/10/18 15:48
 * ----------------------------------------
 */
public class DedicatedInvoiceInformationActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //保存
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //保存文案
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //公司名称
    @ViewInject(R.id.et_company_name)
    private EditText et_company_name;
    //纳税人识别号
    @ViewInject(R.id.et_nsrsbh)
    private EditText et_nsrsbh;
    //注册地址
    @ViewInject(R.id.et_register_address)
    private EditText et_register_address;
    //注册电话
    @ViewInject(R.id.et_register_mobile)
    private EditText et_register_mobile;
    //开户银行
    @ViewInject(R.id.et_open_bank)
    private EditText et_open_bank;
    //开户账号
    @ViewInject(R.id.et_open_account)
    private EditText et_open_account;
    //删除按钮控制
    @ViewInject(R.id.iv_delete_one)
    private ImageView iv_delete_one;
    @ViewInject(R.id.iv_delete_two)
    private ImageView iv_delete_two;
    @ViewInject(R.id.iv_delete_three)
    private ImageView iv_delete_three;
    @ViewInject(R.id.iv_delete_four)
    private ImageView iv_delete_four;
    @ViewInject(R.id.iv_delete_five)
    private ImageView iv_delete_five;
    @ViewInject(R.id.iv_delete_six)
    private ImageView iv_delete_six;

    //传参数据
    public static String EXTRA_COMPANY_NAME = "company_name";
    public static String EXTRA_NSRSBH = "nsrsbh";
    public static String EXTRA_REGISTER_ADDRESS = "register_address";
    public static String EXTRA_REGISTER_MOBILE = "register_mobile";
    public static String EXTRA_OPEN_BANK = "open_bank";
    public static String EXTRA_OPEN_ACCOUNT = "open_account";

    private String company_name;
    private String nsrsbh;
    private String register_address;
    private String register_mobile;
    private String open_bank;
    private String open_account;

    @Override
    public void initIntent() {
        super.initIntent();
        Intent data = getIntent();
        company_name = data.getStringExtra(EXTRA_COMPANY_NAME);
        nsrsbh = data.getStringExtra(EXTRA_NSRSBH);
        register_address = data.getStringExtra(EXTRA_REGISTER_ADDRESS);
        register_mobile = data.getStringExtra(EXTRA_REGISTER_MOBILE);
        open_bank = data.getStringExtra(EXTRA_OPEN_BANK);
        open_account = data.getStringExtra(EXTRA_OPEN_ACCOUNT);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        head_title.setText(R.string.title_special_invoice_qualification_information);
        head_right_text.setText(UIUtils.getString(R.string.label_save));

        head_right_relative.setOnClickListener(this);
        if (company_name != null) {
            //可点击保存
            et_company_name.setText(company_name);
            et_nsrsbh.setText(nsrsbh);
            et_register_address.setText(register_address);
            et_register_mobile.setText(register_mobile);
            et_open_bank.setText(open_bank);
            et_open_account.setText(open_account);
            head_right_text.setTextColor(UIUtils.getColor(R.color.black66));
            head_right_relative.setClickable(true);
            iv_delete_one.setVisibility(View.VISIBLE);
            iv_delete_two.setVisibility(View.VISIBLE);
            iv_delete_three.setVisibility(View.VISIBLE);
            iv_delete_four.setVisibility(View.VISIBLE);
            iv_delete_five.setVisibility(View.VISIBLE);
            iv_delete_six.setVisibility(View.VISIBLE);
        } else {
            //不可点击
            head_right_text.setTextColor(UIUtils.getColor(R.color.color_gay_99));
            head_right_relative.setClickable(false);
            iv_delete_one.setVisibility(View.GONE);
            iv_delete_two.setVisibility(View.GONE);
            iv_delete_three.setVisibility(View.GONE);
            iv_delete_four.setVisibility(View.GONE);
            iv_delete_five.setVisibility(View.GONE);
            iv_delete_six.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        et_company_name.addTextChangedListener(this);
        et_nsrsbh.addTextChangedListener(this);
        et_register_address.addTextChangedListener(this);
        et_register_mobile.addTextChangedListener(this);
        et_open_bank.addTextChangedListener(this);
        et_open_account.addTextChangedListener(this);
        iv_delete_one.setOnClickListener(this);
        iv_delete_two.setOnClickListener(this);
        iv_delete_three.setOnClickListener(this);
        iv_delete_four.setOnClickListener(this);
        iv_delete_five.setOnClickListener(this);
        iv_delete_six.setOnClickListener(this);

    }

    @Override
    public String getPageName() {
        return UIUtils.getString(R.string.title_act_dedicated_invoice_Page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_dedicated_invoice_information;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_relative:
                Intent intent = new Intent();
                intent.putExtra(EXTRA_COMPANY_NAME, et_company_name.getText().toString());
                intent.putExtra(EXTRA_NSRSBH, et_nsrsbh.getText().toString());
                intent.putExtra(EXTRA_REGISTER_ADDRESS, et_register_address.getText().toString());
                intent.putExtra(EXTRA_REGISTER_MOBILE, et_register_mobile.getText().toString());
                intent.putExtra(EXTRA_OPEN_BANK, et_open_bank.getText().toString());
                intent.putExtra(EXTRA_OPEN_ACCOUNT, et_open_account.getText().toString());
                setResult(1000, intent);
                finish();
                break;
            case R.id.iv_delete_one:
                et_company_name.setText("");
                break;
            case R.id.iv_delete_two:
                et_nsrsbh.setText("");
                break;
            case R.id.iv_delete_three:
                et_register_address.setText("");
                break;
            case R.id.iv_delete_four:
                et_register_mobile.setText("");
                break;
            case R.id.iv_delete_five:
                et_open_bank.setText("");
                break;
            case R.id.iv_delete_six:
                et_open_account.setText("");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (et_company_name.length() > 0) {
            iv_delete_one.setVisibility(View.VISIBLE);
        } else {
            iv_delete_one.setVisibility(View.GONE);
        }
        if (et_nsrsbh.length() > 0) {
            iv_delete_two.setVisibility(View.VISIBLE);
        } else {
            iv_delete_two.setVisibility(View.GONE);
        }
        if (et_register_address.length() > 0) {
            iv_delete_three.setVisibility(View.VISIBLE);
        } else {
            iv_delete_three.setVisibility(View.GONE);
        }
        if (et_register_mobile.length() > 0) {
            iv_delete_four.setVisibility(View.VISIBLE);
        } else {
            iv_delete_four.setVisibility(View.GONE);
        }
        if (et_open_bank.length() > 0) {
            iv_delete_five.setVisibility(View.VISIBLE);
        } else {
            iv_delete_five.setVisibility(View.GONE);
        }
        if (et_open_account.length() > 0) {
            iv_delete_six.setVisibility(View.VISIBLE);
        } else {
            iv_delete_six.setVisibility(View.GONE);
        }

        if (et_company_name.length() > 0 && et_nsrsbh.length() > 0
                && et_register_address.length() > 0 && et_register_mobile.length() > 0
                && et_open_bank.length() > 0 && et_open_account.length() > 0) {
            head_right_relative.setClickable(true);
            head_right_text.setTextColor(UIUtils.getColor(R.color.black66));
        } else {
            head_right_relative.setClickable(false);
            head_right_text.setTextColor(UIUtils.getColor(R.color.color_gay_99));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
}

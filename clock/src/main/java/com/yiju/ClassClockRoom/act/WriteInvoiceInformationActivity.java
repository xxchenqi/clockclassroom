package com.yiju.ClassClockRoom.act;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.AddInvoiceContactBean;
import com.yiju.ClassClockRoom.bean.InvoiceContacts;
import com.yiju.ClassClockRoom.common.callback.IViewOnclickListener;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.InvoiceHeadDialog;
import com.yiju.ClassClockRoom.widget.dialog.InvoiceTypeDialog;

import java.util.ArrayList;

/**
 * ----------------------------------------
 * 注释:填写发票信息页
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/10/18 10:03
 * ----------------------------------------
 */
public class WriteInvoiceInformationActivity extends BaseActivity implements View.OnClickListener, IViewOnclickListener {
    public static String EXTRA_INVOICE_INFO = "invoice";
    public static String EXTRA_INVOICE_TYPE = "type";
    public static String EXTRA_INVOICE_HEAD = "head";
    public static String EXTRA_INVOICE_ID = "id";
    public static String EXTRA_INVOICE_LAST_TYPE = "last_invoice_type";
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
    //专用发票未填写显示的内容
    @ViewInject(R.id.ll_invoice_special_no)
    private LinearLayout ll_invoice_special_no;
    //专用发票填写过显示的内容
    @ViewInject(R.id.ll_invoice_special)
    private LinearLayout ll_invoice_special;
    //填写专用发票信息
    @ViewInject(R.id.tv_write_invoice)
    private TextView tv_write_invoice;
    //填写发票信息
    @ViewInject(R.id.et_write_company)
    private EditText et_write_company;
    //公司名字
    @ViewInject(R.id.tv_invoice_company_name)
    private TextView tv_invoice_company_name;
    //纳税人识别号
    @ViewInject(R.id.tv_invoice_nsrsbh)
    private TextView tv_invoice_nsrsbh;
    //注册名字
    @ViewInject(R.id.tv_invoice_register_name)
    private TextView tv_invoice_register_name;
    //注册电话
    @ViewInject(R.id.tv_invoice_register_mobile)
    private TextView tv_invoice_register_mobile;
    //开户银行
    @ViewInject(R.id.tv_invoice_open_bank)
    private TextView tv_invoice_open_bank;
    //开户账号
    @ViewInject(R.id.tv_invoice_open_account)
    private TextView tv_invoice_open_account;
    //修改专用发票
    @ViewInject(R.id.ll_invoice_modify)
    private LinearLayout ll_invoice_modify;
    //发票信息
    @ViewInject(R.id.tv_invoice_info)
    private TextView tv_invoice_info;

    //发票类型
    @ViewInject(R.id.ll_invoice_type)
    private LinearLayout ll_invoice_type;
    //发票抬头
    @ViewInject(R.id.ll_invoice_head)
    private LinearLayout ll_invoice_head;
    //公司名称布局
    @ViewInject(R.id.ll_company_name)
    private LinearLayout ll_company_name;
    //发票信息布局
    @ViewInject(R.id.fl_invoice_info)
    private FrameLayout fl_invoice_info;
    //发票类型描述
    @ViewInject(R.id.tv_type_desc)
    private TextView tv_type_desc;
    //发票抬头描述
    @ViewInject(R.id.tv_head_desc)
    private TextView tv_head_desc;


    //发票信息
    private ArrayList<InvoiceContacts> invoiceContactses;
    //是否显示已填写好的专用发票
    private boolean isShow = false;
    //uid
    private String uid;
    //类型ID 1=普通发票 2=电子发票 3=专用发票(最后一次发票选择类型)
    private int typeid = 0;
    //抬头/公司名称
    private String mc;
    //纳税人识别号
    private String nsrsbh;
    //注册地址
    private String dz;
    //注册电话
    private String dh;
    //开户银行
    private String khh;
    //开户账号
    private String yhzh;
    //发票抬头
    private String invoice_head = "";
    //公司名称
    private String companyName;
    //普通电子发票 是否是第一次请求
    private boolean isFirst = true;
    //专用发票 是否是第一次请求
    private boolean isFirst_special = true;
    //普通发票id
    private String id;
    //专用发票id
    private String id_special;
    //点击次数
    private int click = 0;

    private InvoiceHeadDialog headDialog;
    private InvoiceTypeDialog typeDialog;

    @Override
    public void initIntent() {
        super.initIntent();
        invoiceContactses = (ArrayList<InvoiceContacts>) getIntent().getSerializableExtra(EXTRA_INVOICE_INFO);
        typeid = Integer.valueOf(getIntent().getStringExtra(EXTRA_INVOICE_LAST_TYPE));
    }

    @Override
    protected void initView() {
        uid = StringUtils.getUid();
        headDialog = new InvoiceHeadDialog(this, this);
        typeDialog = new InvoiceTypeDialog(this, this);
    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.invoice_head_title));
        head_right_text.setText(UIUtils.getString(R.string.label_save));
        if (invoiceContactses != null && invoiceContactses.size() > 0) {
            //获取发票信息的时候 1专用 2普通/电子
            for (int i = 0; i < invoiceContactses.size(); i++) {
                InvoiceContacts invoiceContacts = invoiceContactses.get(i);

                if ("1".equals(invoiceContacts.getTypeid())) {//专用
                    isFirst_special = false;
                    isShow = true;
                    //设置专用发票信息
                    mc = invoiceContacts.getMc();
                    nsrsbh = invoiceContacts.getNsrsbh();
                    dz = invoiceContacts.getDz();
                    dh = invoiceContacts.getDh();
                    khh = invoiceContacts.getKhh();
                    yhzh = invoiceContacts.getYhzh();
                    tv_invoice_company_name.setText(String.format(getString(R.string.txt_company_name), mc));
                    tv_invoice_nsrsbh.setText(String.format(getString(R.string.txt_taxpayer_registration_number), nsrsbh));
                    tv_invoice_register_name.setText(String.format(getString(R.string.txt_register_address), dz));
                    tv_invoice_register_mobile.setText(String.format(getString(R.string.txt_register_mobile), dh));
                    tv_invoice_open_bank.setText(String.format(getString(R.string.txt_open_bank), khh));
                    tv_invoice_open_account.setText(String.format(getString(R.string.txt_open_account), yhzh));
                    id_special = invoiceContacts.getId();
                } else if ("2".equals(invoiceContacts.getTypeid())) {//电子和普通
                    isFirst = false;
                    id = invoiceContacts.getId();
                    //如果不是个人那么et上设置公司名字
                    if (!"个人".equals(invoiceContacts.getMc())) {
                        invoice_head = "公司";
                        companyName = invoiceContacts.getMc();
                        ll_company_name.setVisibility(View.VISIBLE);
                        et_write_company.setText(companyName);
                        et_write_company.setSelection(companyName.length());
                        tv_head_desc.setText("公司");
                    } else {
                        invoice_head = "个人";
                        companyName = "个人";
                        tv_head_desc.setText("个人");
                        ll_company_name.setVisibility(View.GONE);
                    }
                }
            }
            //数据设置完之后，设置选中,不能再循环中进行此操作
            switch (typeid) {
                case 1://纸质
                    //选中
                    tv_invoice_info.setText(UIUtils.getString(R.string.invoice_content_info));//改变备注信息
                    tv_type_desc.setText("纸质普通发票");
                    break;
                case 2://电子
                    //选中
                    tv_invoice_info.setText(UIUtils.getString(R.string.invoice_content_info));//改变备注信息
                    tv_type_desc.setText("电子普通发票");
                    break;
                case 3://专用
                    //专用选中
                    tv_type_desc.setText("专用发票");
                    tv_invoice_info.setText(UIUtils.getString(R.string.invoice_content_info));//备注信息
                    ll_invoice_special.setVisibility(View.VISIBLE);//专用布局显示
                    ll_invoice_head.setVisibility(View.GONE);//发票抬头隐藏
                    ll_company_name.setVisibility(View.GONE);//公司名称隐藏
                    break;
            }
        } else {
            //第一次，获取普通发票id
            id = getIntent().getStringExtra(EXTRA_INVOICE_ID);
            isFirst = false;//默认电子发票是开过的
            isFirst_special = true;
            //直接展示电子个人
            tv_type_desc.setText("电子普通发票");
            tv_head_desc.setText("个人");
            ll_company_name.setVisibility(View.GONE);//隐藏公司名称
            //初始化数据
            typeid = 2;
            invoice_head = "个人";
            companyName = "个人";
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        tv_write_invoice.setOnClickListener(this);
        ll_invoice_modify.setOnClickListener(this);
        ll_invoice_type.setOnClickListener(this);
        ll_invoice_head.setOnClickListener(this);
        et_write_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                companyName = s.toString();
                if (!"".equals(s.toString())) {
                    //可点击
                    changeStatus(true);
                } else {
                    //不可点击
                    changeStatus(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public String getPageName() {
        return UIUtils.getString(R.string.title_act_write_invoices_Page);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_write_invoice_information;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_155");
                finish();
                break;
            case R.id.head_right_relative://保存
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_156");
                // 保存发票类型
                SharedPreferencesUtils.saveString(this,
                        getResources().getString(R.string.shared_invoice_type) + uid,
                        typeid + "");
                //判断发票类型
                switch (typeid) {
                    case 1://普通
                    case 2://电子
                        if (isFirst) {
                            //新增请求
                            getHttpUtils();
                        } else {
                            //修改请求
                            getHttpUtils_modify();
                        }
                        break;
                    case 3://专用
                        if (isFirst_special) {
                            //新增请求
                            getHttpUtils();
                        } else {
                            //修改请求
                            getHttpUtils_modify();
                        }
                        break;
                }
                break;
            case R.id.tv_write_invoice://填写发票信息
                Intent intent = new Intent(this, DedicatedInvoiceInformationActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.ll_invoice_modify://专用发票修改
                Intent intent_modify = new Intent(this, DedicatedInvoiceInformationActivity.class);
                intent_modify.putExtra(DedicatedInvoiceInformationActivity.EXTRA_COMPANY_NAME, mc);
                intent_modify.putExtra(DedicatedInvoiceInformationActivity.EXTRA_NSRSBH, nsrsbh);
                intent_modify.putExtra(DedicatedInvoiceInformationActivity.EXTRA_REGISTER_ADDRESS, dz);
                intent_modify.putExtra(DedicatedInvoiceInformationActivity.EXTRA_REGISTER_MOBILE, dh);
                intent_modify.putExtra(DedicatedInvoiceInformationActivity.EXTRA_OPEN_BANK, khh);
                intent_modify.putExtra(DedicatedInvoiceInformationActivity.EXTRA_OPEN_ACCOUNT, yhzh);
                startActivityForResult(intent_modify, 0);
                break;
            case R.id.ll_invoice_type://发票类型
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_157");
                typeDialog.createView();
                break;
            case R.id.ll_invoice_head://发票抬头
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_158");
                headDialog.createView();
                break;

        }
    }

    @Override
    public void viewClick(View view, AlertDialog dialog) {
        dialog.dismiss();
        switch (view.getId()) {
            case R.id.tv_personal://个人
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_163");
                invoice_head = "个人";
                companyName = "个人";
                ll_company_name.setVisibility(View.GONE);
                tv_head_desc.setText("个人");
                //可点击
                changeStatus(true);
                break;
            case R.id.tv_company://公司
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_164");
                invoice_head = "公司";
                ll_company_name.setVisibility(View.VISIBLE);
                tv_head_desc.setText("公司");
                companyName = et_write_company.getText().toString();
                if (StringUtils.isNotNullString(companyName)) {
                    //可点击
                    changeStatus(true);
                } else {
                    //不可点击
                    changeStatus(false);
                }
                break;
            case R.id.tv_paper://纸质
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_159");
                typeid = 1;
                //发票领取方式
                tv_invoice_info.setText(UIUtils.getString(R.string.invoice_content_info));
                //显示发票抬头
                ll_invoice_head.setVisibility(View.VISIBLE);
                //隐藏专用发票
                ll_invoice_special_no.setVisibility(View.GONE);
                ll_invoice_special.setVisibility(View.GONE);
                tv_type_desc.setText("纸质普通发票");
                if (getString(R.string.invoice_personal).equals(invoice_head)) {
                    //可点击
                    changeStatus(true);
                } else if (getString(R.string.company).equals(invoice_head)) {
                    //判断是否有公司名字
                    ll_company_name.setVisibility(View.VISIBLE);
                    companyName = et_write_company.getText().toString();
                    if (StringUtils.isNotNullString(companyName)) {
                        //可点击
                        changeStatus(true);
                    } else {
                        //不可点击
                        changeStatus(false);
                    }
                } else {
                    //不可点击
                    changeStatus(false);
                }

                break;
            case R.id.tv_electron://电子
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_160");
                typeid = 2;
                //发票领取方式
                tv_invoice_info.setText(UIUtils.getString(R.string.invoice_content_info));
                //显示发票抬头
                ll_invoice_head.setVisibility(View.VISIBLE);
                //隐藏专用发票
                ll_invoice_special_no.setVisibility(View.GONE);
                ll_invoice_special.setVisibility(View.GONE);
                tv_type_desc.setText("电子普通发票");
                if (getString(R.string.invoice_personal).equals(invoice_head)) {
                    //可点击
                    changeStatus(true);
                } else if (getString(R.string.company).equals(invoice_head)) {
                    //判断是否有公司名字
                    companyName = et_write_company.getText().toString();
                    ll_company_name.setVisibility(View.VISIBLE);
                    if (StringUtils.isNotNullString(companyName)) {
                        //可点击
                        changeStatus(true);
                    } else {
                        //不可点击
                        changeStatus(false);
                    }
                } else {
                    //不可点击
                    changeStatus(false);
                }
                break;
            case R.id.tv_special://专用
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_161");
                if (click == 0) {
                    UIUtils.showLongToastSafe(getString(R.string.toast_special_invoice));
                    click++;
                }
                typeid = 3;
                //发票领取方式
                tv_invoice_info.setText(UIUtils.getString(R.string.invoice_content_info));
                ll_invoice_head.setVisibility(View.GONE);
                ll_company_name.setVisibility(View.GONE);
                tv_type_desc.setText("专用发票");
                if (isShow) {
                    ll_invoice_special_no.setVisibility(View.GONE);
                    ll_invoice_special.setVisibility(View.VISIBLE);
                } else {
                    ll_invoice_special_no.setVisibility(View.VISIBLE);
                    ll_invoice_special.setVisibility(View.GONE);
                }
                if (isShow) {
                    //可点击保存
                    changeStatus(true);
                } else {
                    //不可点击保存
                    changeStatus(false);
                }
                break;
            case R.id.tv_head_cancel://抬头取消
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_165");
                break;
            case R.id.tv_cancel://类型取消
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_162");
                break;

        }
    }

    /**
     * 保存发票请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "add_invoice_contact");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("typeid", typeid + "");
        if (typeid == 3) {
            //专用发票
            params.addBodyParameter("mc", mc);
            params.addBodyParameter("nsrsbh", nsrsbh);
            params.addBodyParameter("dz", dz);
            params.addBodyParameter("dh", dh);
            params.addBodyParameter("khh", khh);
            params.addBodyParameter("yhzh", yhzh);
        } else {
            params.addBodyParameter("mc", companyName);
        }

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
        AddInvoiceContactBean addInvoiceContactBean = GsonTools.changeGsonToBean(result,
                AddInvoiceContactBean.class);
        if (addInvoiceContactBean == null) {
            return;
        }
        if ("1".equals(addInvoiceContactBean.getCode())) {
            Intent intent = new Intent();
            //新增发票没id
            String new_id = addInvoiceContactBean.getInvoice_contact_id() + "";
            switch (typeid) {
                case 1://普通
                    intent.putExtra(EXTRA_INVOICE_TYPE, 1);
                    intent.putExtra(EXTRA_INVOICE_HEAD, companyName);
                    intent.putExtra(EXTRA_INVOICE_ID, new_id);//不要去之前传进来的id，因为一次id是空的
                    break;
                case 2://电子
                    intent.putExtra(EXTRA_INVOICE_TYPE, 2);
                    intent.putExtra(EXTRA_INVOICE_HEAD, companyName);
                    intent.putExtra(EXTRA_INVOICE_ID, new_id);
                    break;
                case 3://专用
                    intent.putExtra(EXTRA_INVOICE_TYPE, 3);
                    intent.putExtra(EXTRA_INVOICE_HEAD, mc);
                    intent.putExtra(EXTRA_INVOICE_ID, new_id);
                    break;
            }
            setResult(3, intent);
            finish();
        } else {
            UIUtils.showLongToastSafe(addInvoiceContactBean.getMsg());
        }
    }

    /**
     * 修改发票请求
     */
    private void getHttpUtils_modify() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "edit_invoice_contact");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("typeid", typeid + "");
        if (typeid == 3) {
            //专用发票
            params.addBodyParameter("mc", mc);
            params.addBodyParameter("nsrsbh", nsrsbh);
            params.addBodyParameter("dz", dz);
            params.addBodyParameter("dh", dh);
            params.addBodyParameter("khh", khh);
            params.addBodyParameter("yhzh", yhzh);
        } else {
            params.addBodyParameter("mc", companyName);
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData_modify(arg0.result);
                    }
                });
    }

    private void processData_modify(String result) {
        AddInvoiceContactBean addInvoiceContactBean = GsonTools.changeGsonToBean(result,
                AddInvoiceContactBean.class);
        if (addInvoiceContactBean == null) {
            return;
        }
        if ("1".equals(addInvoiceContactBean.getCode())) {
            Intent intent = new Intent();
            switch (typeid) {
                case 1://普通
                    intent.putExtra(EXTRA_INVOICE_TYPE, 1);
                    intent.putExtra(EXTRA_INVOICE_HEAD, companyName);
                    intent.putExtra(EXTRA_INVOICE_ID, id);
                    break;
                case 2://电子
                    intent.putExtra(EXTRA_INVOICE_TYPE, 2);
                    intent.putExtra(EXTRA_INVOICE_HEAD, companyName);
                    intent.putExtra(EXTRA_INVOICE_ID, id);
                    break;
                case 3://专用
                    intent.putExtra(EXTRA_INVOICE_TYPE, 3);
                    intent.putExtra(EXTRA_INVOICE_HEAD, mc);
                    intent.putExtra(EXTRA_INVOICE_ID, id_special);
                    break;
            }
            setResult(3, intent);
            finish();
        } else {
            UIUtils.showLongToastSafe(addInvoiceContactBean.getMsg());
        }


    }

    private void changeStatus(boolean status) {
        if (status) {
            head_right_relative.setClickable(true);
            head_right_text.setTextColor(UIUtils.getColor(R.color.black66));
        } else {
            head_right_relative.setClickable(false);
            head_right_text.setTextColor(UIUtils.getColor(R.color.color_gay_99));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000) {
            isShow = true;
            changeStatus(true);
            ll_invoice_special_no.setVisibility(View.GONE);
            ll_invoice_special.setVisibility(View.VISIBLE);
            mc = data.getStringExtra(DedicatedInvoiceInformationActivity.EXTRA_COMPANY_NAME);
            nsrsbh = data.getStringExtra(DedicatedInvoiceInformationActivity.EXTRA_NSRSBH);
            dz = data.getStringExtra(DedicatedInvoiceInformationActivity.EXTRA_REGISTER_ADDRESS);
            dh = data.getStringExtra(DedicatedInvoiceInformationActivity.EXTRA_REGISTER_MOBILE);
            khh = data.getStringExtra(DedicatedInvoiceInformationActivity.EXTRA_OPEN_BANK);
            yhzh = data.getStringExtra(DedicatedInvoiceInformationActivity.EXTRA_OPEN_ACCOUNT);
            tv_invoice_company_name.setText(String.format(getString(R.string.txt_company_name), mc));
            tv_invoice_nsrsbh.setText(String.format(getString(R.string.txt_taxpayer_registration_number), nsrsbh));
            tv_invoice_register_name.setText(String.format(getString(R.string.txt_register_address), dz));
            tv_invoice_register_mobile.setText(String.format(getString(R.string.txt_register_mobile), dh));
            tv_invoice_open_bank.setText(String.format(getString(R.string.txt_open_bank), khh));
            tv_invoice_open_account.setText(String.format(getString(R.string.txt_open_account), yhzh));
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

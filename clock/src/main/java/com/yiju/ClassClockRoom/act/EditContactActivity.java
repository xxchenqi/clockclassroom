package com.yiju.ClassClockRoom.act;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.ContactBean.Data;
import com.yiju.ClassClockRoom.bean.NewContactBean;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.InputValidate;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

/**
 * --------------------------------------
 * <p/>
 * 注释:编辑联系人
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-10 下午2:12:16
 * <p/>
 * --------------------------------------
 */
public class EditContactActivity extends BaseActivity implements
        OnClickListener {
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
     * 确认按钮
     */
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    /**
     * 确认文案
     */
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    /**
     * 联系人姓名输入框
     */
    @ViewInject(R.id.et_name_editcontact)
    private EditText et_name_editcontact;
    /**
     * 手机输入框
     */
    @ViewInject(R.id.et_phone_editcontact)
    private EditText et_phone_editcontact;
    /**
     * 默认联系人选择
     */
    @ViewInject(R.id.cb_default_contact)
    private SwitchCompat cb_default_contact;
    /**
     * 删除联系人按钮
     */
    @ViewInject(R.id.btn_contact_delete)
    private TextView btn_contact_delete;
    //联系人id
    private String id;
    //标记
    private String flag;

    @Override
    public int setContentViewId() {
        return R.layout.activity_edit_contact;
    }

    @Override
    public void initView() {
        head_back_relative.setOnClickListener(this);
        head_right_relative.setOnClickListener(this);
        btn_contact_delete.setOnClickListener(this);

        flag = getIntent().getStringExtra("flag");

        head_right_text.setText(getResources().getString(R.string.confirm));

    }

    @Override
    public void initData() {
        if ("new".equals(flag)) {
            //新建联系人
            head_title.setText(getResources().getString(R.string.new_contact));
            btn_contact_delete.setVisibility(View.GONE);
        } else if ("edit".equals(flag)) {
            //编辑联系人
            Data data = (Data) getIntent().getSerializableExtra("data");
            head_title.setText(getResources().getString(R.string.edit_contact));
            et_name_editcontact.setText(data.getName());
            et_phone_editcontact.setText(data.getMobile());
            if ("1".equals(data.getIsdefault())) {
                cb_default_contact.setChecked(true);
            } else {
                cb_default_contact.setChecked(false);
            }
            id = data.getId();
        }
        cb_default_contact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_233");
                }else{
                    MobclickAgent.onEvent(UIUtils.getContext(), "v3200_234");
                }
            }
        });


    }

    /**
     * 编辑联系人
     *
     * @param name       名称
     * @param mobile     手机
     * @param is_default s
     */
    private void getHttpUtils(String name, String mobile, String is_default) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "edit_mail");
        params.addBodyParameter("id", id);
        params.addBodyParameter("name", name);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("is_default", is_default);
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
        NewContactBean contactBean = GsonTools.changeGsonToBean(result,
                NewContactBean.class);
        if (contactBean == null) {
            return;
        }
        if ("1".equals(contactBean.getCode()) && contactBean.isData()) {
            setResult(RESULT_OK);
            finish();
        } else {
            UIUtils.showToastSafe(contactBean.getMsg());
        }
    }

    /**
     * 删除联系人
     */
    private void getHttpUtils2() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "delete_mail");
        params.addBodyParameter("id", id);
        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
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
                });
    }

    private void processData2(String result) {
        NewContactBean contactBean = GsonTools.changeGsonToBean(result,
                NewContactBean.class);
        if (contactBean == null) {
            return;
        }
        if ("1".equals(contactBean.getCode()) && contactBean.isData()) {
            setResult(RESULT_OK);
            finish();
        } else {
            UIUtils.showToastSafe(contactBean.getMsg());
        }
    }

    /**
     * 新增联系人
     *
     * @param name       姓名
     * @param mobile     手机号
     * @param is_default 默认联系人
     */
    private void getHttpUtils3(String name, String mobile, String is_default) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "insert_mail");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("name", name);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("is_default", is_default);

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_MINE_ORDER, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData3(arg0.result);
                    }
                });
    }

    private void processData3(String result) {
        NewContactBean contactBean = GsonTools.changeGsonToBean(result,
                NewContactBean.class);
        if (contactBean == null) {
            return;
        }
        if ("1".equals(contactBean.getCode()) && contactBean.isData()) {
            setResult(RESULT_OK);
            finish();
        } else {
            UIUtils.showToastSafe(contactBean.getMsg());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative://返回
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_231");
                finish();
                break;
            case R.id.head_right_relative: // 编辑联系人请求
                MobclickAgent.onEvent(UIUtils.getContext(), "v3200_232");
                String name = et_name_editcontact.getText().toString();
                String mobile = et_phone_editcontact.getText().toString();
                if ("".equals(name)) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_contact_name));
                } else if ("".equals(mobile)) {
                    UIUtils.showToastSafe(getString(R.string.toast_input_contact_phone));
                } else if (!InputValidate.checkedIsTelephone(mobile)) {
                    UIUtils.showToastSafe(getString(R.string.toast_contact_phone_number_format_error));
                } else {
                    // 请求刷新
                    if (cb_default_contact.isChecked()) {
                        if ("new".equals(flag)) {
                            getHttpUtils3(name, mobile, "1");
                        } else if ("edit".equals(flag)) {
                            getHttpUtils(name, mobile, "1");
                        }
                    } else {
                        if ("new".equals(flag)) {
                            getHttpUtils3(name, mobile, "0");
                        } else if ("edit".equals(flag)) {
                            getHttpUtils(name, mobile, "0");
                        }
                    }
                }

                break;
            case R.id.btn_contact_delete:// 删除联系人请求
                dialog();
                break;

            default:
                break;
        }
    }

    /**
     * 删除对话框
     */
    private void dialog() {
        CustomDialog customDialog = new CustomDialog(
                EditContactActivity.this,
                UIUtils.getString(R.string.confirm),
                UIUtils.getString(R.string.label_cancel),
                UIUtils.getString(R.string.dialog_show_delete_contact));
        customDialog.setOnClickListener(new IOnClickListener() {
            @Override
            public void oncClick(boolean isOk) {
                if (isOk) {
                    getHttpUtils2();
                }
            }
        });
    }

    @Override
    public String getPageName() {
        if ("new".equals(flag)) {
            return getString(R.string.title_act_personal_center_contact_new);
        } else {
            return getString(R.string.title_act_personal_center_contact_detail);
        }
    }

}

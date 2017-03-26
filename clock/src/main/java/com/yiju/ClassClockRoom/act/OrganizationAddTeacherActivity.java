package com.yiju.ClassClockRoom.act;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.common.constant.RequestCodeConstant;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

/**
 * 作者： 葛立平
 * 2016/3/18 15:21
 */
public class OrganizationAddTeacherActivity extends BaseActivity implements View.OnClickListener {

    //返回
    @ViewInject(R.id.head_back)
    private ImageView head_back;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //保存
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //编辑名称
    @ViewInject(R.id.et_member_name)
    private EditText et_member_name;
    //通讯录调用
    @ViewInject(R.id.iv_member_tel_address)
    private TextView iv_member_tel_address;
    //编辑手机号码
    @ViewInject(R.id.et_member_tel)
    private EditText et_member_tel;
    private String uid ;

    //返回码
    public static int RESULT_CODE_FROM_ORGANIZATION_ADD_TEACHER_ACT = 1003;


    @Override
    protected void initView() {
        head_title.setText(getText(R.string.add_menber));
        head_right_text.setText(getText(R.string.label_save));
    }

    @Override
    protected void initData() {
        uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                getResources().getString(R.string.shared_id), null);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
        iv_member_tel_address.setOnClickListener(this);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_org_add_teacher);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_add_teacher;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.head_right_text:
                if (!"".equals(et_member_name.getText().toString()) && !"".equals(et_member_tel.getText().toString())) {
                    getHttpUtils();
                } else {
                    UIUtils.showToastSafe(getString(R.string.toast_user_name_phone_not_empty));
                }
                break;
            case R.id.iv_member_tel_address:
                if (!PermissionsChecker.checkPermission(PermissionsChecker.READ_CONTACTS_PERMISSIONS)) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, RequestCodeConstant.CONTACT_REQUEST);
                } else {
                    PermissionsChecker.requestPermissions(this, PermissionsChecker.READ_CONTACTS_PERMISSIONS);
                }
                break;
        }
    }

    /**
     * 添加成员请求
     */
    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "organization_add_member");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("org_uid", StringUtils.getUid());
        }
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("mobile", et_member_tel.getText().toString().replace(" ", ""));
        params.addBodyParameter("org_dname", et_member_name.getText().toString());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
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
        CommonResultBean bean = GsonTools.changeGsonToBean(result,
                CommonResultBean.class);
        if (bean == null) {
            return;
        }
        if ("1".equals(bean.getCode())) {
            this.setResult(RESULT_CODE_FROM_ORGANIZATION_ADD_TEACHER_ACT);
            finish();
        } else {
            UIUtils.showToastSafe(bean.getMsg());
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodeConstant.CONTACT_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            setContactInfo(data);
        }
    }

    /**
     * 设置联系人信息
     */
    private void setContactInfo(Intent data) {
        String contact = null;
        String phoneNumber = null;
        String contactId = null;
        Uri uri = data.getData();
        // 得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        // 取得电话本中开始一项的游标
        Cursor cursor = cr.query(uri, null, null, null, null);
        // 向下移动光标
        assert cursor != null;
        while (cursor.moveToNext()) {
            // 取得联系人名字
            int nameFieldColumnIndex = cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact = cursor.getString(nameFieldColumnIndex);
            int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            int phoneNum = cursor.getInt(phoneColumn);
            if (phoneNum > 0) {
                int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                contactId = cursor.getString(idColumn);
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (contactId != null) {
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            if (phones != null && phones.moveToFirst()) {
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    phoneNumber = phones.getString(index);
                }
            }
            if (phones != null && !phones.isClosed()) {
                phones.close();
            }
        }
        et_member_name.setText(contact == null ? "" : contact);
        et_member_tel.setText(phoneNumber == null ? "" : phoneNumber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
//        } else {
            UIUtils.showToastSafe(getString(R.string.toast_permission_read_contacts));
        }
    }
}

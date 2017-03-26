package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.yiju.ClassClockRoom.adapter.ContactAdapter;
import com.yiju.ClassClockRoom.bean.ContactBean;
import com.yiju.ClassClockRoom.bean.ContactBean.Data;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * --------------------------------------
 * <p/>
 * 注释:联系人列表
 * <p/>
 * <p/>
 * <p/>
 * 作者: cq
 * <p/>
 * <p/>
 * <p/>
 * 时间: 2015-12-10 上午9:19:55
 * <p/>
 * --------------------------------------
 */
public class ContactInformationActivity extends BaseActivity implements
        OnClickListener, OnItemClickListener {
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
     * listview
     */
    @ViewInject(R.id.lv_contact)
    private ListView lv_contact;
    /**
     * 新建联系人
     */
    @ViewInject(R.id.rl_contact)
    private TextView rl_contact;
    /**
     * 适配器
     */
    private ContactAdapter contactAdapter;
    /**
     * 数据源
     */
    private List<Data> datas;

    @Override
    public int setContentViewId() {
        return R.layout.activity_contact_information;
    }

    @Override
    public void initView() {
        rl_contact.setOnClickListener(this);
        head_back_relative.setOnClickListener(this);
        lv_contact.setOnItemClickListener(this);

        head_title.setText(getResources().getString(R.string.contact_list));

        datas = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, datas, R.layout.item_contact);
    }

    @Override
    public void initData() {
        lv_contact.setAdapter(contactAdapter);
        getHttpUtils();
    }

    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_mail");
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
        ContactBean contactBean = GsonTools.changeGsonToBean(result,
                ContactBean.class);
        if (contactBean == null) {
            return;
        }
        if ("1".equals(contactBean.getCode())) {
            datas.clear();
            datas.addAll(contactBean.getData());
            contactAdapter.notifyDataSetChanged();
        } else {
            UIUtils.showToastSafe(contactBean.getMsg());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.rl_contact:
                Intent intent = new Intent(this, EditContactActivity.class);
                intent.putExtra("flag", "new");
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                getHttpUtils();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Data data = datas.get(position);
        Intent intent = new Intent(this, EditContactActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("flag", "edit");
        startActivityForResult(intent, 0);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_contact_list);
    }

}

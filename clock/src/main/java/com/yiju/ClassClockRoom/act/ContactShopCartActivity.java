package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.KeyEvent;
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
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.ShopcartContactAdapter;
import com.yiju.ClassClockRoom.bean.ContactBean;
import com.yiju.ClassClockRoom.bean.ContactBean.Data;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactShopCartActivity extends BaseActivity implements
        OnClickListener {

    /**
     * 退出按钮
     */
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    private TextView head_title;
    private ListView lv_contact;
    private TextView rl_contact;
    private String uid;
    private List<Data> mLists = new ArrayList<>();
    private ShopcartContactAdapter adapter;
    private String id;

    @Override
    public int setContentViewId() {
        return R.layout.activity_contact_information;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void initView() {
        head_back_relative = (RelativeLayout) findViewById(R.id.head_back_relative);
        head_title = (TextView) findViewById(R.id.head_title);
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        rl_contact = (TextView) findViewById(R.id.rl_contact);
        head_back_relative.setOnClickListener(this);
        rl_contact.setOnClickListener(this);
        //设置标题
        head_title.setText(getResources().getString(R.string.contact_list));

        if (null != adapter) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ShopcartContactAdapter(this, mLists);
        }
        lv_contact.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 选中联系人
                Intent intent = new Intent();

                intent.putExtra("name", mLists.get(arg2).getName());
                intent.putExtra("tel", mLists.get(arg2).getMobile());
                intent.putExtra("id", mLists.get(arg2).getId());
                setResult(1, intent);
                finish();

            }
        });
    }

    @Override
    public void initData() {
        uid = SharedPreferencesUtils.getString(this, "id", "");
        if (null != adapter) {
            lv_contact.setAdapter(adapter);
        }
        getHttpUtils();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                backPress();
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

    private void backPress() {
        Intent intentBack = new Intent();
        for (int i = 0; i < mLists.size(); i++) {
            if (mLists.get(i).isCheck()) {
                intentBack.putExtra("name", mLists.get(i).getName());
                intentBack.putExtra("tel",  mLists.get(i).getMobile());
                intentBack.putExtra("id", mLists.get(i).getId());
                break;
            }
        }

        setResult(1, intentBack);
        finish();
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

    private void getHttpUtils() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_mail");
        params.addBodyParameter("uid", uid);
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
        ContactBean info = GsonTools.changeGsonToBean(result,
                ContactBean.class);
        if (null != info) {
            if (info.getCode().equals("1")) {
                List<Data> data = info.getData();
                mLists.clear();
                mLists.addAll(data);
                if(id!=null){
                    for (int i = 0; i < mLists.size(); i++) {
                        if (id.equals(mLists.get(i).getId())) {
                            //如果带进来的id等于解析出来的id，表示选中
                            mLists.get(i).setCheck(true);
                            break;
                        }
                    }

                }

                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_shopcar_contact_list);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            backPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

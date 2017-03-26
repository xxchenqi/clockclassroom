package com.yiju.ClassClockRoom.act;

import android.view.KeyEvent;
import android.view.View;
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
import com.yiju.ClassClockRoom.adapter.OrganizationBlacklistManagementAdapter;
import com.yiju.ClassClockRoom.bean.OrganizationBlacklistBean;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------
 * 注释: 机构黑名单管理
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/14 10:24
 * ----------------------------------------
 */
public class OrganizationBlacklistManagementActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回
     */
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    /**
     * 标题
     */
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //黑名单列表
    @ViewInject(R.id.lv_organization_blacklist)
    private ListView lv_organization_blacklist;
    //适配器
    private OrganizationBlacklistManagementAdapter adapter;
    //数据源
    private List<OrganizationBlacklistBean.DataEntity> datas;


    @Override
    protected void initView() {
        datas = new ArrayList<>();
        adapter = new OrganizationBlacklistManagementAdapter(
                this,
                datas,
                R.layout.item_organization_blacklist,
                new ListItemClickHelp() {
                    @Override
                    public void onClickItem(final int position) {
                        CustomDialog customDialog = new CustomDialog(
                                OrganizationBlacklistManagementActivity.this,
                                UIUtils.getString(R.string.confirm),
                                UIUtils.getString(R.string.label_cancel),
                                UIUtils.getString(R.string.dialog_msg_delete_organization));
                        customDialog.setOnClickListener(new IOnClickListener() {
                            @Override
                            public void oncClick(boolean isOk) {
                                if (isOk) {
                                    getHttpUtilsForRemove(datas.get(position).getId(), position);
                                }
                            }
                        });
                    }
                }
        );
    }

    @Override
    protected void initData() {
        head_title.setText(UIUtils.getString(R.string.organization_blacklist));
        lv_organization_blacklist.setAdapter(adapter);
        getHttpUtils();
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_info_black_list);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_blacklist_management;
    }


    private void getHttpUtils() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "organization_black_list");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

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
        OrganizationBlacklistBean organizationBlacklistBean = GsonTools.changeGsonToBean(result,
                OrganizationBlacklistBean.class);
        if (organizationBlacklistBean == null) {
            return;
        }
        if ("1".equals(organizationBlacklistBean.getCode())) {
            List<OrganizationBlacklistBean.DataEntity> data = organizationBlacklistBean.getData();
            if (data != null && data.size() > 0) {
                datas.clear();
                datas.addAll(data);
                adapter.notifyDataSetChanged();
            } else {
                UIUtils.showToastSafe(getString(R.string.toast_no_black_list));
            }
        }
    }

    /**
     * 移除黑名单请求
     *
     * @param org_id 要移除的机构ID
     */
    private void getHttpUtilsForRemove(String org_id, final int pos) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "remove_organization_black");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("org_id", org_id);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForRemove(arg0.result, pos);
                    }
                });
    }

    private void processDataForRemove(String result, int pos) {
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        if ("1".equals(resultData.getCode())) {
            if (datas != null && datas.size() > 0) {
                datas.remove(pos);
                adapter.notifyDataSetChanged();
            }
            SharedPreferencesUtils.saveString(UIUtils.getContext(),
                    UIUtils.getString(R.string.shared_black_count), datas.size() + "");
        }
        UIUtils.showToastSafe(resultData.getMsg());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                setResult(5);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(5);
        return super.onKeyDown(keyCode, event);
    }
}

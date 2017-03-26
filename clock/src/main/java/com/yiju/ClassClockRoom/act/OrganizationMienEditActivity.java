package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.MienBean;
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.bean.result.CommonResultBean;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;
import com.yiju.ClassClockRoom.widget.ZoomImageView;
import com.yiju.ClassClockRoom.widget.dialog.CustomDialog;

import java.util.List;

/**
 * 机构风采查看和删除界面
 * Created by wh on 2016/3/24.
 */
public class OrganizationMienEditActivity extends BaseActivity implements View.OnClickListener {
    //后退
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //分割线
    @ViewInject(R.id.head_divider)
    private View head_divider;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //删除
    @ViewInject(R.id.head_right_text)
    private TextView head_right_text;
    //图片
    @ViewInject(R.id.iv_zoom_image)
    private ZoomImageView iv_zoom_image;
    //删除按钮
    @ViewInject(R.id.head_right_relative)
    private RelativeLayout head_right_relative;
    //标题中间
    @ViewInject(R.id.head_center_relative)
    private RelativeLayout head_center_relative;
    //bean
    private MineOrganizationBean bean;
    //bean
    private MemberDetailResult teacher_bean;
    //位置
    private int pos;
    //标题
    private String title;
    //标记
    private String flag;
    //机构id
    private String org_uid;
    //图片拼接
    private StringBuilder imgs;
    //风采照片删除
    public static int RESULT_CODE_FROM_ORGANIZATION_MIEN_EDIT_DELETE_ACT = 1003;
    //是否能编辑
    private boolean edit_flag;
    private String uid;

    @Override
    public void initIntent() {
        super.initIntent();
        bean = (MineOrganizationBean) getIntent().getSerializableExtra("bean");
        pos = getIntent().getIntExtra("pos", -1);
        title = getIntent().getStringExtra("title");
        flag = getIntent().getStringExtra("flag");
        org_uid = getIntent().getStringExtra("org_uid");
        teacher_bean = (MemberDetailResult) getIntent().getSerializableExtra("teacher_bean");
        edit_flag = getIntent().getBooleanExtra("edit_flag", false);
    }

    @Override
    protected void initView() {
        head_title.setText(title);
        head_right_text.setText(UIUtils.getString(R.string.delete));
        head_divider.setVisibility(View.GONE);
        head_back_relative.setBackgroundColor(Color.BLACK);
        head_right_relative.setBackgroundColor(Color.BLACK);
        head_center_relative.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        head_right_text.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        uid = SharedPreferencesUtils.getString(UIUtils.getContext(),
                getResources().getString(R.string.shared_id), null);
        if (edit_flag) {
            //是管理员
            head_right_text.setVisibility(View.VISIBLE);
        } else {
            head_right_text.setVisibility(View.GONE);
        }

        if (getString(R.string.title_institutional_presence).equals(title)) {
            if (bean == null) {
                return;
            }
            if (bean.getData() == null) {
                return;
            }
            if (bean.getData().getMien() == null) {
                return;
            }
            if (StringUtils.isNotNullString(bean.getData().getMien().get(pos).getPic())) {
                Glide.with(mContext).load(bean.getData().getMien().get(pos).getPic()).into(iv_zoom_image);
            }
        } else {
            if (teacher_bean == null) {
                return;
            }
            if (teacher_bean.getData() == null) {
                return;
            }
            if (teacher_bean.getData().getMien() == null) {
                return;
            }
            if (StringUtils.isNotNullString(teacher_bean.getData().getMien().get(pos).getPic())) {
                Glide.with(mContext).load(teacher_bean.getData().getMien().get(pos).getPic()).into(iv_zoom_image);
            }
        }
    }

    @Override
    public String getPageName() {
        if (getString(R.string.title_institutional_presence).equals(title)) {
            return getString(R.string.title_act_my_org_mine_delete);
        } else if (getString(R.string.teacher_mien).equals(title)) {
            return getString(R.string.title_act_my_teacher_mine_delete);
        }
        return null;
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_mien_edit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
            case R.id.head_right_text://删除
                String msg;
                if (getString(R.string.title_institutional_presence).equals(title)) {
                    msg = UIUtils.getString(R.string.dialog_msg_delete_mine);
                } else {
                    msg = UIUtils.getString(R.string.dialog_msg_delete_teacher);
                }
                CustomDialog customDialog = new CustomDialog(
                        OrganizationMienEditActivity.this,
                        UIUtils.getString(R.string.confirm),
                        UIUtils.getString(R.string.label_cancel),
                        msg);
                customDialog.setOnClickListener(new IOnClickListener() {
                    @Override
                    public void oncClick(boolean isOk) {
                        if (isOk) {
                            if (getString(R.string.title_institutional_presence).equals(title)) {
                                getHttpUtils();
                            } else {
//                                StringBuilder sb = new StringBuilder();
//                                if (mien.size() != 1) {
//                                    for (int i = 0; i < mien.size(); i++) {
//                                        //如果不是当前的位置，就不增加进去
//                                        if (i != pos) {
//                                            sb.append(mien.get(i).getPic()).append(",");
//                                        }
//                                    }
//                                    //删除最后的逗号
//                                    sb.deleteCharAt(sb.length() - 1);
//                                }

                                List<MienBean> mien = teacher_bean.getData().getMien();
                                mien.remove(pos);
                                HttpClassRoomApi.getInstance().askModifyMemberInfo(title, org_uid, teacher_bean, true,false);
                            }
                        }
                    }
                });
                break;
        }
    }

    private void getHttpUtils() {
        imgs = new StringBuilder();
        List<MineOrganizationBean.DataEntity.MienEntity> mien = bean.getData().getMien();
        if (mien.size() != 2) {
            for (int i = 0; i < mien.size() - 1; i++) {
                //如果不是当前的位置，就不增加进去
                if (i != pos) {
                    imgs.append(mien.get(i).getPic()).append(",");
                }
            }
            //删除最后的逗号
            imgs.deleteCharAt(imgs.length() - 1);
        }

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "edit_organization_info");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("org_uid", StringUtils.getUid());
        }
        params.addBodyParameter("info", bean.getData().getInfo());
        params.addBodyParameter("tags", bean.getData().getTags());
        params.addBodyParameter("mien_pic", imgs.toString());
        params.addBodyParameter("uid", uid);
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
                }
        );
    }

    private void processData(String result) {
        CommonResultBean resultData = GsonTools.fromJson(result, CommonResultBean.class);
        UIUtils.showToastSafe(resultData.getMsg());
        if ("1".equals(resultData.getCode())) {
            MineOrganizationActivity.organization_mien_flag = true;
            Intent intent = new Intent();
            intent.putExtra("imgs", imgs.toString());
            this.setResult(1000, intent);
            finish();
        }
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        if (event.getType() == DataManager.MODIFY_MEMBER_DATA_OTHER) {
            MemberDetailResult bean = (MemberDetailResult) event.getData();
            if (bean == null) {
                return;
            }
            if ("1".equals(bean.getCode())) {
                UIUtils.showLongToastSafe(UIUtils.getString(R.string.toast_edit_success));
                this.setResult(RESULT_CODE_FROM_ORGANIZATION_MIEN_EDIT_DELETE_ACT);
                finish();
            } else {
                UIUtils.showToastSafe(bean.getMsg());
            }
        }
    }

}

package com.yiju.ClassClockRoom.act;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.MineOrganizationBean;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.CircleImageView;
import com.yiju.ClassClockRoom.widget.FrameImageView;

/**
 * ----------------------------------------
 * 注释:认证信息
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/15 17:54
 * ----------------------------------------
 */
public class OrganizationInformationActivity extends BaseActivity implements View.OnClickListener {
    //返回
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //头像
    @ViewInject(R.id.cv_user_avatar)
    private CircleImageView cv_user_avatar;
    //用户名
    @ViewInject(R.id.tv_user_name)
    private TextView tv_user_name;
    //机构名
    @ViewInject(R.id.tv_organization_name)
    private TextView tv_organization_name;
    //申请人手机
    @ViewInject(R.id.tv_proposer_mobile)
    private TextView tv_proposer_mobile;
    //申请人地址
    @ViewInject(R.id.tv_proposer_address)
    private TextView tv_proposer_address;
    //营业执照
    @ViewInject(R.id.iv_business_license)
    private FrameImageView iv_business_license;
    //营业执照号
    @ViewInject(R.id.tv_business_license_no)
    private TextView tv_business_license_no;
    //法人照
    @ViewInject(R.id.iv_legal_person)
    private FrameImageView iv_legal_person;
    //法人名
    @ViewInject(R.id.tv_legal_person_name)
    private TextView tv_legal_person_name;
    //法人编号
    @ViewInject(R.id.tv_legal_person_no)
    private TextView tv_legal_person_no;
    //申请人图片
    @ViewInject(R.id.iv_proposer)
    private FrameImageView iv_proposer;
    //申请人名字
    @ViewInject(R.id.tv_proposer_name)
    private TextView tv_proposer_name;
    //申请人编号
    @ViewInject(R.id.tv_proposer_no)
    private TextView tv_proposer_no;
    //beam
    private MineOrganizationBean bean;
    //用户名
    private String user_name;
    //用户头像
    private String user_avatar;


    @Override
    public void initIntent() {
        super.initIntent();
        bean = (MineOrganizationBean) getIntent().getSerializableExtra("bean");
    }

    @Override
    protected void initView() {
        user_name = SharedPreferencesUtils.getString(
                UIUtils.getContext(),
                getResources().getString(R.string.shared_nickname),
                "");
        user_avatar = SharedPreferencesUtils.getString(
                UIUtils.getContext(),
                getResources().getString(R.string.shared_avatar), "");
        iv_business_license.setColour(UIUtils.getColor(R.color.color_gay_8f));
        iv_business_license.setBorderWidth(UIUtils.dip2px(1));
        iv_legal_person.setColour(UIUtils.getColor(R.color.color_gay_8f));
        iv_legal_person.setBorderWidth(UIUtils.dip2px(1));
        iv_proposer.setColour(UIUtils.getColor(R.color.color_gay_8f));
        iv_proposer.setBorderWidth(UIUtils.dip2px(1));
    }

    @Override
    protected void initData() {
        MineOrganizationBean.DataEntity data = bean.getData();
        head_title.setText(R.string.title_authentication_information);
        Glide.with(UIUtils.getContext()).load(data.getLogo()).into(cv_user_avatar);
        tv_user_name.setText(data.getShort_name());
        tv_organization_name.setText(StringUtils.isNotNullString(data.getName()) ? data.getName() : "");
        tv_proposer_mobile.setText(data.getContact_mobile());
        tv_proposer_address.setText(data.getContact_address());
        Glide.with(UIUtils.getContext()).load(data.getRegister_pic()).into(iv_business_license);
        tv_business_license_no.setText(data.getRegister_no());
        Glide.with(UIUtils.getContext()).load(data.getCorporation_pic()).into(iv_legal_person);
        tv_legal_person_name.setText(data.getCorporation());
        tv_legal_person_no.setText(data.getCorporation_no());
        Glide.with(UIUtils.getContext()).load(data.getContact_pic()).into(iv_proposer);
        tv_proposer_name.setText(data.getContact_name());
        tv_proposer_no.setText(data.getContact_no());
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_my_org_info);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_organization_information;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                finish();
                break;
        }
    }
}

package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ejupay.sdk.EjuPayManager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.bean.PictureWrite;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.common.constant.ParamConstant;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.CountControl;
import com.yiju.ClassClockRoom.control.EjuPaySDKUtil;
import com.yiju.ClassClockRoom.control.camera.CameraDialog;
import com.yiju.ClassClockRoom.control.camera.CameraImage;
import com.yiju.ClassClockRoom.control.camera.ResultCameraHandler;
import com.yiju.ClassClockRoom.control.camera.ResultCameraHandler.CameraResult;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.api.HttpPhotoApi;
import com.yiju.ClassClockRoom.util.net.api.HttpUserApi;
import com.yiju.ClassClockRoom.widget.CircleImageView;
import com.yiju.ClassClockRoom.widget.windows.SexPopUpWindow;

import java.io.File;

/**
 * ----------------------------------------
 * 注释: 个人中心信息
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: 2016/1/20 11:00
 * ----------------------------------------
 */
public class PersonalCenter_InformationActivity extends BaseActivity implements
        OnClickListener, OnTouchListener, ListItemClickHelp {

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
     * 头像
     */
    @ViewInject(R.id.iv_avatar_info)
    private CircleImageView iv_avatar_info;
    /**
     * 昵称
     */
    @ViewInject(R.id.tv_nickname)
    private TextView tv_nickname;
    /**
     * 性别
     */
    @ViewInject(R.id.tv_sex_setting)
    private TextView tv_sex_setting;
    /**
     * 手机
     */
    @ViewInject(R.id.tv_mobile)
    private TextView tv_mobile;
    /**
     * 邮箱
     */
    @ViewInject(R.id.tv_email)
    private TextView tv_email;
    /**
     * 退出登录按钮
     */
    @ViewInject(R.id.btn_quit)
    private Button btn_quit;
    /**
     * 选择头像布局
     */
    @ViewInject(R.id.rl_layout_avatar)
    private RelativeLayout rl_layout_avatar;
    /**
     * 修改昵称布局
     */
    @ViewInject(R.id.rl_layout_nickname)
    private RelativeLayout rl_layout_nickname;

    /**
     * 开关打开关闭时显示文本
     */
    @ViewInject(R.id.tv_show_msg)
    private TextView tv_show_msg;
    /**
     * 是否展示我的老师信息
     */
    @ViewInject(R.id.sc_check)
    private SwitchCompat sc_check;
    /**
     * 修改密码布局
     */
    @ViewInject(R.id.rl_layout_password)
    private RelativeLayout rl_layout_password;
    /**
     * 修改支付密码布局
     */
    @ViewInject(R.id.rl_layout_pay_password)
    private RelativeLayout rl_layout_pay_password;
    /**
     * 更换手机布局
     */
    @ViewInject(R.id.rl_layout_mobile)
    private RelativeLayout rl_layout_mobile;
    /**
     * 修改性别布局
     */
    @ViewInject(R.id.rl_layout_sex)
    private RelativeLayout rl_layout_sex;
    /**
     * 修改机构黑名单
     */
    @ViewInject(R.id.rl_organization_blacklist)
    private RelativeLayout rl_organization_blacklist;
    /**
     * 修改邮箱布局
     */
    @ViewInject(R.id.rl_layout_email)
    private RelativeLayout rl_layout_email;
    /**
     * 修改联系人布局
     */
    @ViewInject(R.id.rl_layout_contact)
    private RelativeLayout rl_layout_contact;

    /**
     * 账号绑定布局
     */
    @ViewInject(R.id.rl_layout_binding)
    private RelativeLayout rl_layout_binding;

    /**
     * 绑定qq图标
     */
    @ViewInject(R.id.iv_binding_qq)
    private ImageView iv_binding_qq;
    /**
     * 绑定微信图标
     */
    @ViewInject(R.id.iv_binding_wx)
    private ImageView iv_binding_wx;
    /**
     * 绑定新浪图标
     */
    @ViewInject(R.id.iv_binding_sina)
    private ImageView iv_binding_sina;
    @ViewInject(R.id.tv_black_count)
    private TextView tv_black_count;
    /**
     * 跳转意图
     */
    private Intent intent;

    /**
     * 设置性别pop
     */
    private SexPopUpWindow sex_popup;
    /**
     * 照相Image
     */
    private CameraImage cameraImage;

    /**
     * file_category
     */
    private String file_category;
    /**
     * permit_code
     */
    private String permit_code;
    /**
     * key
     */
    private String key;
    /**
     * 头像的url
     */
    private String headUrl;
    /**
     * 三方qq信息
     */
    private String shared_third_qq;
    /**
     * 三方微信信息
     */
    private String shared_third_wechat;
    /**
     * 三方微博信息
     */
    private String shared_third_weibo;

    /**
     * 是否显示我的老师信息
     */
    private String value = "1";//默认显示
    //页面是否销毁的标志
    private boolean destroyFlag = false;


    @Override
    public int setContentViewId() {
        return R.layout.activity_personalcenter_information;
    }

    @Override
    public void initView() {
        head_title.setText(getResources().getString(R.string.person_information));

        cameraImage = new CameraImage(PersonalCenter_InformationActivity.this);
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        btn_quit.setOnClickListener(this);
        rl_layout_avatar.setOnClickListener(this);
        rl_layout_nickname.setOnClickListener(this);
        rl_layout_password.setOnClickListener(this);
        rl_layout_pay_password.setOnClickListener(this);
        rl_layout_sex.setOnClickListener(this);
        rl_layout_email.setOnClickListener(this);
        rl_layout_contact.setOnClickListener(this);
        rl_layout_binding.setOnClickListener(this);
        rl_layout_mobile.setOnClickListener(this);
        rl_organization_blacklist.setOnClickListener(this);

        sc_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {//
                    value = "1";//显示
                    tv_show_msg.setText(UIUtils.getString(R.string.text_show_open));
                } else {
                    value = "0";//不显示
                    tv_show_msg.setText(UIUtils.getString(R.string.text_show_close));
                }
                if ("-1".equals(StringUtils.getUid())) {
                    return;
                }
                HttpUserApi.getInstance().switchTeacherInfo(
                        StringUtils.getUid(), StringUtils.getUsername(),
                        StringUtils.getPassword(), StringUtils.getThirdSource(), value);
            }
        });
    }

    @Override
    public void initData() {
        // 设置头像
        String headUrl = SharedPreferencesUtils.getString(UIUtils.getContext(),
                getResources().getString(R.string.shared_avatar), "");
        // 根据头像地址加载图像
        if (headUrl != null && !"".equals(headUrl)) {
            Glide.with(this).load(headUrl)
                    .placeholder(R.drawable.user_unload)
                    .error(R.drawable.user_unload)
                    .into(iv_avatar_info);
        }

        // 设置昵称
        String nickName = SharedPreferencesUtils.getString(
                UIUtils.getContext(),
                UIUtils.getString(R.string.shared_nickname), getString(R.string.txt_not_set_up_nickname));

        if (nickName != null && !"".equals(nickName)) {
            tv_nickname.setText(nickName);
        } else {
            tv_nickname.setText(R.string.txt_not_set_up_nickname);
        }

        //设置是否显示我的老师信息
        String show_teacher = SharedPreferencesUtils.getString(this,
                getResources().getString(R.string.shared_show_teacher),
                "");
        boolean isCheck = true;
        if (!TextUtils.isEmpty(show_teacher)) {
            if ("0".equals(show_teacher)) {//不显示
                isCheck = false;
                tv_show_msg.setText(UIUtils.getString(R.string.text_show_close));
            } else if ("1".equals(show_teacher)) {//显示
                isCheck = true;
                tv_show_msg.setText(UIUtils.getString(R.string.text_show_open));
            }
        }
        sc_check.setChecked(isCheck);

        // 设置性别 (0设置 1男 2女)
        String sex = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_sex), "0");

        if (sex != null && !"".equals(sex)) {
            switch (Integer.valueOf(sex)) {
                case 0:
                    tv_sex_setting.setText(R.string.label_setting);
                    break;
                case 1:
                    tv_sex_setting.setText(R.string.label_male);
                    break;
                case 2:
                    tv_sex_setting.setText(R.string.label_female);
                    break;
                default:
                    break;
            }
        }

        // 设置手机号
        String mobile = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_mobile), "");
        if (mobile != null && !"".equals(mobile)) {
            tv_mobile.setText(mobile);
        } else {
            tv_mobile.setText(R.string.txt_not_binding_mobile_phone);
            // 三方登录后隐藏修改密码布局
            rl_layout_password.setVisibility(View.GONE);

        }

        // 设置邮箱
        String email = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_email), "");
        String local_email = "";
        if (!"-1".equals(StringUtils.getUid())) {
            local_email = SharedPreferencesUtils.getString(
                    this, "local_email_" + StringUtils.getUid(), "");
        }


        if (email != null && !"".equals(email)) {
            tv_email.setText(email);
        } else {
            if (local_email != null && !"".equals(local_email)) {
                tv_email.setText(R.string.txt_no_validation);
            } else {
                tv_email.setText(R.string.txt_strapped_down_email);
            }
        }

        //机构黑名单数量
        String black_count = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_black_count), "");
        if (black_count != null && !"".equals(black_count)) {
            tv_black_count.setText(black_count);
        }

        //设置第三方账号绑定图标
        refresh();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_relative:
                //退出
                onBackPressed();
                break;
            case R.id.rl_layout_avatar:
                //检测是否打开读写权限
                if (!PermissionsChecker.checkPermission(
                        PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS)) {
                    //调用相机
                    new CameraDialog(PersonalCenter_InformationActivity.this,
                            cameraImage).creatView();
                } else {
                    PermissionsChecker.requestPermissions(
                            this,
                            PermissionsChecker.READ_WRITE_SDCARD_PERMISSIONS
                    );
                }
                break;
            case R.id.rl_layout_nickname:
                //修改昵称
                intent = new Intent(UIUtils.getContext(),
                        PersonalCenter_ChangeNicknameActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_layout_password:
                //修改密码
                intent = new Intent(UIUtils.getContext(),
                        PersonalCenter_ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_layout_pay_password:// 修改支付密码
                EjuPaySDKUtil.initEjuPaySDK(new EjuPaySDKUtil.IEjuPayInit() {
                    @Override
                    public void onSuccess() {
                        EjuPayManager.getInstance().startChangePassWord(UIUtils.getContext());
                    }
                });
                break;
            case R.id.rl_layout_sex:
                //修改性别
                if ("-1".equals(StringUtils.getUid())) {
                    return;
                }
                sex_popup = new SexPopUpWindow(this, StringUtils.getUid(), true, null, "");
                sex_popup.showAtLocation(rl_layout_sex, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.rl_organization_blacklist:
                //机构黑名单管理
                intent = new Intent(UIUtils.getContext(),
                        OrganizationBlacklistManagementActivity.class);
                startActivityForResult(intent, 5);
                break;
            case R.id.rl_layout_email:
                //修改邮箱
                intent = new Intent(UIUtils.getContext(),
                        PersonalCenter_ChangeEmailActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_layout_contact:
                //修改联系人
                intent = new Intent(UIUtils.getContext(),
                        ContactInformationActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_layout_mobile:
                // 设置手机号
                String mobile = SharedPreferencesUtils.getString(UIUtils.getContext(),
                        UIUtils.getString(R.string.shared_mobile), "");

//                手机用户-->有手机-->发送验证码-->验证验证码-->修改手机成功modify_phone type1
//                三方用户-->无手机-->跳third_bind_mobile,显示密码 type2
//                三方用户-->有手机-->跳third_bind_mobile,隐藏密码 type3

                Intent changeMobileIntent = new Intent(this, PersonalCenter_ChangeMobileActivity.class);
//                changeMobileIntent.putExtra("mobile", mobile);
//                if (mobile != null && !"".equals(mobile)) {
//                    //有手机号
//                    //判断是否是第三方绑定,
//                    if ("".equals(shared_third_qq) && "".equals(shared_third_wechat) && "".equals(shared_third_weibo)) {
//                        //无绑定过三方账号
//                        changeMobileIntent.putExtra("type", 1);
//                    } else {
//                        //有绑定过三方账号
//                        changeMobileIntent.putExtra("type", 3);
//                    }
//                } else {
//                    //无手机号,
//                    changeMobileIntent.putExtra("type", 2);
//                }
                startActivityForResult(changeMobileIntent, 0);
                break;

            case R.id.rl_layout_binding:
                // 账号绑定
                intent = new Intent(UIUtils.getContext(),
                        PersonalCenter_BindingThreeWayAccountActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_quit:
                // 登出
                if ("-1".equals(StringUtils.getUid())) {
                    return;
                }
                HttpUserApi.getInstance().logout(StringUtils.getUid(), StringUtils.getUsername(), StringUtils.getPassword(), StringUtils.getThirdSource());
                break;
            default:
                break;
        }
    }

    /**
     * 设置第三方账号的绑定图标
     */
    private void refresh() {
        shared_third_qq = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_qq), "");

        shared_third_wechat = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_wechat), "");

        shared_third_weibo = SharedPreferencesUtils.getString(UIUtils.getContext(),
                UIUtils.getString(R.string.shared_third_weibo), "");

        if ("".equals(shared_third_qq)) {
            iv_binding_qq.setImageResource(R.drawable.qq_icon);
        } else {
            iv_binding_qq.setImageResource(R.drawable.qq_share_icon);
        }

        if ("".equals(shared_third_wechat)) {
            iv_binding_wx.setImageResource(R.drawable.wechat_icon_gray);
        } else {
            iv_binding_wx.setImageResource(R.drawable.wechat_share_icon);
        }

        if ("".equals(shared_third_weibo)) {
            iv_binding_sina.setImageResource(R.drawable.sina_icon);
        } else {
            iv_binding_sina.setImageResource(R.drawable.weibo_share_icon);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                // 更新昵称
                tv_nickname.setText(SharedPreferencesUtils.getString(
                        UIUtils.getContext(),
                        UIUtils.getString(R.string.shared_nickname), "您还没有设置昵称"));
                break;

            case 2:
                // 更新邮箱
                String local_email = "";
                if (!"-1".equals(StringUtils.getUid())) {
                    local_email = SharedPreferencesUtils.getString(
                            this, "local_email_" + StringUtils.getUid(), "");
                }
                String email = SharedPreferencesUtils.getString(this,
                        UIUtils.getString(R.string.shared_email), "");

                if (!"".equals(email)) {
                    tv_email.setText(email);
                } else {
                    if ("".equals(local_email)) {
                        tv_email.setText(R.string.txt_unbound);
                    } else {
                        tv_email.setText(R.string.txt_no_validation);
                    }
                }

                break;

            case 3:
                //更新绑定信息
                //设置第三方账号绑定图标
                refresh();
                break;

            case 4:
                // 更新手机
                tv_mobile.setText(SharedPreferencesUtils.getString(UIUtils.getContext(),
                        UIUtils.getString(R.string.shared_mobile), ""));
                break;
            case 5:
                //更新黑名单数量
                String black_count = SharedPreferencesUtils.getString(UIUtils.getContext(),
                        UIUtils.getString(R.string.shared_black_count), "");
                if (black_count != null && !"".equals(black_count)) {
                    tv_black_count.setText(black_count);
                }
                break;
            default:
                break;
        }
        // 设置头像返回
        if (requestCode == CameraImage.CAMERA_WITH_DATA
                || requestCode == CameraImage.PHOTO_REQUEST_CUT
                || requestCode == CameraImage.PHOTO_REQUEST_GALLERY) {
            ResultCameraHandler
                    .getInstance()
                    .setCrop(true)
                    .getPhotoFile(PersonalCenter_InformationActivity.this,
                            data, requestCode, resultCode, cameraImage,
                            new CameraResult() {
                                @Override
                                public void result(File imageUri) {
                                    uploadingHeadImage(imageUri);
                                }
                            });
        }
    }

    /**
     * 性别点击
     *
     * @param position 位置
     */
    @Override
    public void onClickItem(int position) {
        switch (position) {
            case R.id.btn_male:
                tv_sex_setting.setText(R.string.label_male);
                break;
            case R.id.btn_female:
                tv_sex_setting.setText(R.string.label_female);
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onTouch(View arg0, android.view.MotionEvent event) {
        if (sex_popup != null && sex_popup.isShowing()) {
            sex_popup.dismiss();
            sex_popup = null;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_personal_center_information);
    }

    /**
     * 上传
     *
     * @param imageUri 图片文件流
     */
    private void uploadingHeadImage(File imageUri) {
        // 3次请求,获取授权码及key,请求内部写接口和上传头像
        HttpPhotoApi.getInstance().uploadPhoto(imageUri);
    }

    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        super.onRefreshEvent(event);
        if (DataManager.UPLOAD_PHOTO_DATA == event.getType()) {                         //上传图片返回数据
            PictureWrite pictureWrite = (PictureWrite) event.getData();
            if (pictureWrite != null
                    && pictureWrite.getResult() != null
                    && pictureWrite.getResult().getPic_id() != null) {                  //拼接图片地址
                headUrl = CommonUtil.jointHeadUrl(ParamConstant.PHOTO_LOAD_DOMAIN_NAME
                        + pictureWrite.getResult().getPic_id());
                if ("-1".equals(StringUtils.getUid())) {
                    return;
                }
                HttpPhotoApi.getInstance().saveUploadPhotoUrl(StringUtils.getUid(),
                        StringUtils.getUsername(), StringUtils.getPassword(), StringUtils.getThirdSource(), headUrl);                 //保存图片地址
            }
        } else if (DataManager.SAVE_PHOTO_URL == event.getType()) {                        //保存图片地址返回数据
            if (headUrl != null && !"".equals(headUrl)) {                               //根据头像地址加载图像
                if (!destroyFlag) {
                    Glide.with(BaseApplication.getmForegroundActivity())
                            .load(CommonUtil.jointHeadUrl(headUrl))
                            .placeholder(R.drawable.user_unload)
                            .error(R.drawable.user_unload)
                            .into(iv_avatar_info);
                    SharedPreferencesUtils.saveString(getApplicationContext(),
                            getResources().getString(R.string.shared_avatar), CommonUtil.jointHeadUrl(headUrl));         //更新图片，更改数据库的头像url
                }
            }
        } else if (DataManager.SWITCH_TEACHER_INFO == event.getType()) {                   //更改是否展示我的老师信息
            BaseEntity bean = (BaseEntity) event.data;
            if (bean != null) {
                SharedPreferencesUtils.saveString(this,
                        getResources().getString(R.string.shared_show_teacher),
                        value);
                UIUtils.showToastSafe(bean.getMsg());
            }
        } else if (DataManager.LOGOUT_DATA == event.getType()) {                           //注销返回数据
            BaseEntity bean = (BaseEntity) event.data;
            UIUtils.showToastSafe(bean.getMsg());
            CountControl.getInstance().loginOut(false);
            SharedPreferencesUtils.clearData();
            EjuPaySDKUtil.isInitSuccess = false;
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this, 3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyFlag = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults))) {
            //权限未获取
//        } else {
            UIUtils.showToastSafe(getString(R.string.toast_permission_read_write_sdcard));
        }
    }
}

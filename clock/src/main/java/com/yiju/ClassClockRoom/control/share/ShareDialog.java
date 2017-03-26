package com.yiju.ClassClockRoom.control.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.ShareBean;
import com.yiju.ClassClockRoom.bean.StoreShareBean;
import com.yiju.ClassClockRoom.control.ShareControl;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * 分享弹出框
 *
 * @author geliping
 */
public class ShareDialog implements OnClickListener {
    // 类型
    public static final int Type_Share_ClassRoom_Info = 1;
    public static final int Type_Share_Accompany_Video = 2;
    public static final int Type_Share_Accompany_Key = 3;
    public static final int Type_Share_Teacher_Detail = 4;
    public static final int Type_Share_Course_Detail = 5;
    public static final int Type_Share_Store_Detail = 6;
    public static final int Type_Share_Special = -1;

    // 方式
    private static final int Way_Share_SMS = 1;
    private static final int Way_Share_Wechat = 2;
    private static final int Way_Share_WechatCircle = 3;
    private static final int Way_Share_QQ = 4;
    private static final int Way_Share_Sina = 5;

    private int current_Type;
    private int current_Way;
    private boolean isVisiblePwd_Layout;

    private static ShareDialog instance;

    private static Activity mActivity;
    private AlertDialog dialog;

    // 是否分享陪读密码框
    private RelativeLayout accompany_pwd_layout;

    private TextView share_wechat;
    private TextView share_wechat_circle;
    private TextView share_qq;
    private TextView share_sina;
    private TextView share_sms;
    private TextView share_cancel;
    private SwitchCompat accompany_pwd_switch;

    private ShareGetData shareGetData;

    private String sid;
    private String address;
    private String tags;
    private String start_time;
    private String end_time;
    private String video_pas;
    private String vid;
    private String room_no;
    private String date;
    private String school_name;

    //老师分享传入参数(teacher_id、teacher_name)
    private String teacher_id;
    private String teacher_name;
    //课程分享传入参数(school_name、course_id、course_name、teacher_name)
    private String course_id;
    private String course_name;

    private String special_id;
    private StoreShareBean storeShareBean;
    private ShareBean shareBean;

    private ShareDialog() {
        mActivity = BaseApplication.getmForegroundActivity();
    }

    public synchronized static ShareDialog getInstance() {
        if (instance == null
                || mActivity != BaseApplication.getmForegroundActivity()) {
            instance = new ShareDialog();
        }
        return instance;
    }

    /**
     * 加载选择 Dialog VIew
     */
    private void creatView() {
        String shareData = SharedPreferencesUtils.getString(UIUtils.getContext(), UIUtils.getString(R.string.shared_data), null);
        if(StringUtils.isNotNullString(shareData)){
            storeShareBean = GsonTools.changeGsonToBean(shareData, StoreShareBean.class);
        }
        View view = LayoutInflater.from(mActivity).inflate(
                R.layout.dialog_share_layout, null);
        dialog = new AlertDialog.Builder(mActivity, R.style.dateDialogTheme)
                .create();
        Window window = dialog.getWindow();
        if (window != null){
            window.setGravity(Gravity.BOTTOM); // 设置dialog显示的位置
            window.setWindowAnimations(R.style.share_dialog_mystyle);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = CommonUtil.getScreenWidth(mActivity); // 设置宽度
            dialog.setContentView(view, lp);
        }

        share_wechat = (TextView) view.findViewById(R.id.share_wechat);
        accompany_pwd_layout = (RelativeLayout) view
                .findViewById(R.id.accompany_pwd_layout);
        share_wechat_circle = (TextView) view
                .findViewById(R.id.share_wechat_circle);
        share_qq = (TextView) view.findViewById(R.id.share_qq);
        share_sina = (TextView) view.findViewById(R.id.share_sina);
        share_sms = (TextView) view.findViewById(R.id.share_sms);
        share_cancel = (TextView) view.findViewById(R.id.share_cancel);
        accompany_pwd_switch = (SwitchCompat) view
                .findViewById(R.id.accompany_pwd_switch);

        share_wechat.setOnClickListener(this);
        share_wechat_circle.setOnClickListener(this);
        share_qq.setOnClickListener(this);
        share_sina.setOnClickListener(this);
        share_sms.setOnClickListener(this);
        share_cancel.setOnClickListener(this);

        if (current_Type == Type_Share_Accompany_Key || current_Type == Type_Share_Accompany_Video) {
            share_sina.setVisibility(View.GONE);
        } else {
            share_sina.setVisibility(View.VISIBLE);
        }
        if (isVisiblePwd_Layout && StringUtils.isNotNullString(video_pas)) {
            accompany_pwd_layout.setVisibility(View.VISIBLE);
            accompany_pwd_switch.setChecked(true);
        } else {
            accompany_pwd_layout.setVisibility(View.GONE);
            if (StringUtils.isNotNullString(video_pas)) {
                accompany_pwd_switch.setChecked(true);
            } else {
                accompany_pwd_switch.setChecked(false);
            }
        }

    }

    public void showDialog() {
        if (dialog == null || !dialog.isShowing()) {
            creatView();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_wechat:
                if(current_Type == Type_Share_Store_Detail){
                    goShare(SHARE_MEDIA.WEIXIN);
                }else {
                    ShareGetDate(SHARE_MEDIA.WEIXIN);
                }
                dialog.dismiss();
                break;
            case R.id.share_wechat_circle:
                if(current_Type == Type_Share_Store_Detail){
                    goShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                }else {
                    ShareGetDate(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                dialog.dismiss();
                break;
            case R.id.share_qq:
                if(current_Type == Type_Share_Store_Detail){
                    goShare(SHARE_MEDIA.QQ);
                }else {
                    ShareGetDate(SHARE_MEDIA.QQ);
                }
                dialog.dismiss();
                break;
            case R.id.share_sina:
                if(current_Type == Type_Share_Store_Detail){
                    goShare(SHARE_MEDIA.SINA);
                }else {
                    ShareGetDate(SHARE_MEDIA.SINA);
                }
                dialog.dismiss();
                break;
            case R.id.share_sms:
                if(current_Type == Type_Share_Store_Detail){
                    goShare(SHARE_MEDIA.SMS);
                }else {
                    ShareGetDate(SHARE_MEDIA.SMS);
                }
                dialog.dismiss();
                break;
            case R.id.share_cancel:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 分享数据已获得去分享
     *
     * @param share_MEDIA sm
     */
    private void goShare(SHARE_MEDIA share_MEDIA) {
        if (share_MEDIA == SHARE_MEDIA.WEIXIN) {
            shareBean = storeShareBean.getWeixin();
        } else if (share_MEDIA == SHARE_MEDIA.WEIXIN_CIRCLE) {
            shareBean = storeShareBean.getWeixin_friend();
        } else if (share_MEDIA == SHARE_MEDIA.QQ) {
            shareBean = storeShareBean.getQq();
        } else if (share_MEDIA == SHARE_MEDIA.SINA) {
            shareBean = storeShareBean.getWeibo();
        } else if (share_MEDIA == SHARE_MEDIA.SMS) {
            shareBean = storeShareBean.getSms();
        }
        if(null != shareBean){
            ShareControl.StartShare(mActivity, share_MEDIA, shareBean);
        }
    }

    /**
     * 获得分享数据并分享
     *
     * @param share_MEDIA sm
     */
    private void ShareGetDate(final SHARE_MEDIA share_MEDIA) {
        if (share_MEDIA == SHARE_MEDIA.WEIXIN) {
            current_Way = Way_Share_Wechat;
        } else if (share_MEDIA == SHARE_MEDIA.WEIXIN_CIRCLE) {
            current_Way = Way_Share_WechatCircle;
        } else if (share_MEDIA == SHARE_MEDIA.QQ) {
            current_Way = Way_Share_QQ;
        } else if (share_MEDIA == SHARE_MEDIA.SINA) {
            current_Way = Way_Share_Sina;
        } else if (share_MEDIA == SHARE_MEDIA.SMS) {
            current_Way = Way_Share_SMS;
        }

        if (current_Type == Type_Share_ClassRoom_Info) {
            shareGetData = new ShareGetData(Type_Share_ClassRoom_Info,
                    current_Way, school_name, sid, address, tags, false);
        } else if (current_Type == Type_Share_Accompany_Video) {
            shareGetData = new ShareGetData(Type_Share_Accompany_Video,
                    current_Way, school_name, start_time, end_time, video_pas,
                    vid, accompany_pwd_switch.isChecked());
        } else if (current_Type == Type_Share_Accompany_Key) {
            shareGetData = new ShareGetData(Type_Share_Accompany_Key,
                    current_Way, school_name, start_time, end_time, video_pas,
                    vid, room_no, date, accompany_pwd_switch.isChecked());
        } else if (current_Type == Type_Share_Teacher_Detail) {
            //老师详情分享
            shareGetData = new ShareGetData(Type_Share_Teacher_Detail, current_Way, teacher_id,
                    teacher_name);
        } else if (current_Type == Type_Share_Course_Detail) {
            //课程详情分享
            shareGetData = new ShareGetData(Type_Share_Course_Detail,current_Way,school_name,
                    course_id,course_name,teacher_name);
        }else if(current_Type == Type_Share_Special){
            //专题分享
            shareGetData = new ShareGetData(Type_Share_Special,current_Way,special_id);
        }

        if (shareGetData == null) {
            return;
        }
        shareGetData.setShareDateReturn(new IShareDateReturn() {

            @Override
            public void getShareBean(ShareBean bean) {
//				if (share_MEDIA == SHARE_MEDIA.SINA) {
//					Intent intent = new Intent(mActivity,
//							ShareEditeActivity.class);
//					intent.putExtra(ShareEditeActivity.Type_Param_Content, bean);
//					mActivity.startActivity(intent);
//				} else {
//					ShareControl.StartShare(mActivity, share_MEDIA, bean);
//				}
                ShareControl.StartShare(mActivity, share_MEDIA, bean);
            }
        });
    }

    public ShareDialog setSchool_name(String school_name) {
        this.school_name = school_name;
        return instance;
    }

    public ShareDialog setSid(String sid) {
        this.sid = sid;
        return instance;
    }

    public ShareDialog setAddress(String address) {
        this.address = address;
        return instance;
    }

    public ShareDialog setTags(String tags) {
        this.tags = tags;
        return instance;
    }

    public ShareDialog setStart_time(String start_time) {
        this.start_time = start_time;
        return instance;
    }

    public ShareDialog setEnd_time(String end_time) {
        this.end_time = end_time;
        return instance;
    }

    public ShareDialog setVideo_pas(String video_pas) {
        this.video_pas = video_pas;
        return instance;
    }

    public ShareDialog setVid(String vid) {
        this.vid = vid;
        return instance;
    }

    public ShareDialog setRoom_no(String room_no) {
        this.room_no = room_no;
        return instance;
    }

    public ShareDialog setDate(String date) {
        this.date = date;
        return instance;
    }

    public ShareDialog setCurrent_Type(int current_Type) {
        this.current_Type = current_Type;
        return instance;
    }

    public ShareDialog setVisiblePwd_Layout(boolean isVisiblePwd) {
        this.isVisiblePwd_Layout = isVisiblePwd;
        return instance;
    }

    public void shareSina(ShareBean bean) {
        ShareControl.StartShare(mActivity, SHARE_MEDIA.SINA, bean);
    }

    public ShareDialog setCourse_name(String course_name) {
        this.course_name = course_name;
        return instance;
    }

    public ShareDialog setCourse_id(String course_id) {
        this.course_id = course_id;
        return instance;
    }

    public ShareDialog setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
        return instance;
    }

    public ShareDialog setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
        return instance;
    }

    public ShareDialog setSpecial_id(String special_id) {
        this.special_id = special_id;
        return instance;
    }
}

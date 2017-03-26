package com.yiju.ClassClockRoom.receiver;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.CourseOrderDetailActivity;
import com.yiju.ClassClockRoom.act.OrderDetailActivity;
import com.yiju.ClassClockRoom.act.PersonalCenterActivity;
import com.yiju.ClassClockRoom.act.accompany.AccompanyReadStatusActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.bean.PushBean;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.common.constant.SharedPreferencesConstant;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.SchemeControl;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpCommonApi;

import java.util.List;
import java.util.Random;

public class PushClockReceiver extends BroadcastReceiver {
    //动作
    private static final String ACTION = "action";
    //通知栏点击后传递的参数
    private static final String Notify_Push_Id = "pushId";
    // 订单
    private static final int Action_Int_Order = 90001;
    private static final String Action_Order_Detail = "order_detail";
    // 购物车
    private static final int Action_Int_Cart = 90002;
    private static final String Action_Cart = "cart";
    // 视频
    private static final int Action_Int_Video = 90003;
    private static final String Action_Video = "video";
    // 活动页面
    private static final int Action_Int_Activity = 90004;
    private static final String Action_Activity = "activity";
    //老师详情页
    private static final int Action_Int_TeacherDetail = 90005;
    private static final String Action_Teacher_Detail = "teacher_detail";
    //机构加人
    private static final int Action_Int_Organization_Add = 90006;
    private static final String Action_Organization_Add = "organization_add";
    //机构修改成员权限
    private static final int Action_Int_Auth_Update = 90007;
    private static final String Action_Auth_Update = "auth_update";
    //退单申请
//    private static final int Action_Int_Refund = 90008;
//    private static final String Action_Refund = "refund";
    //机构移除老师
    private static final int Action_Int_Exit_Org = 90009;
    private static final String Action_Exit_Org = "exit_org";
    //老师退出机构
    private static final int Action_Int_Exit_Org_Self = 90010;
    private static final String Action_Exit_Org_Self = "exit_org_self";
    //订单完成
//    private static final int Action_Int_Order_Finish = 90011;
//    private static final String Action_Order_Finish = "order_finish";
    //订单消费提醒
    private static final int Action_Int_Class_Start = 90012;
    private static final String Action_Class_Start = "class_start";
    //订单消费完毕
    private static final int Action_Int_Class_Finish = 90013;
    private static final String Action_Class_Finish = "class_finish";
    //机构认证成功
    private static final int Action_Int_Organization_Success = 90014;
    private static final String Action_Organization_Success = "organization_success";
    //机构认证失败
    private static final int Action_Int_Organization_Fail = 90015;
    private static final String Action_Organization_Fail = "organization_fail";
    //课程审核成功
    private static final int ACTION_INT_COURSE_REVIEW_SUCCESS = 90017;
    private static final String ACTION_COURSE_REVIEW_SUCCESS = "course_review_success";
    //课程审核不通过
    private static final int ACTION_INT_COURSE_REVIEW_FAIL = 90018;
    private static final String ACTION_COURSE_REVIEW_FAIL = "course_review_fail";
    //老师取消课程
    private static final int ACTION_INT_CANCEL_COURSE_TEACHER = 90019;
    private static final String ACTION_CANCEL_COURSE_TEACHER = "cancel_course_teacher";
    //系统取消课程
    private static final int ACTION_INT_CANCEL_COURSE_SYSTEM = 90020;
    private static final String ACTION_CANCEL_COURSE_SYSTEM = "cancel_course_system";

    //支付前确认订单通过
    private static final int ACTION_INT_ORDER_PASS_PER_PAYMENT = 90021;
    private static final String ACTION_ORDER_PASS_PER_PAYMENT = "order_pass_per_payment";
    //支付前确认订单未通过
    private static final int ACTION_INT_ORDER_NOT_PASS_PER_PAYMENT = 90022;
    private static final String ACTION_ORDER_NOT_PASS_PER_PAYMENT = "order_notpass_per_payment";
    //支付后确认订单通过
    private static final int ACTION_INT_ORDER_PASS_AFTER_PAYMENT = 90023;
    private static final String ACTION_ORDER_PASS_AFTER_PAYMENT = "order_pass_after_payment";
    //支付后确认订单未通过
    private static final int ACTION_INT_ORDER_NOT_PASS_AFTER_PAYMENT = 90024;
    private static final String ACTION_ORDER_NOT_PASS_AFTER_PAYMENT = "order_notpass_after_payment";
    //开电子发票
    private static final int ACTION_INT_OPEN_ELECTRONIC_INVOICE = 90025;
    private static final String ACTION_OPEN_ELECTRONIC_INVOICE = "open_electronic_invoice";
    //老师审核通过
    private static final int ACTION_INT_TEACHER_REVIEW_SUCCESS = 90026;
    private static final String ACTION_TEACHER_REVIEW_SUCCESS = "teacher_review_success";
    //老师审核未通过
    private static final int ACTION_INT_TEACHER_REVIEW_FAIL = 90027;
    private static final String ACTION_TEACHER_REVIEW_FAIL = "teacher_review_fail";

    //无法识别的消息
    private static final int Action_Int_Null = 90016;
    // 延时
    private static final int Delayed_Time = 30000;

    private NotificationManager mNotificationManager;

    public static String cid;

    private PushBean bean_skip;

    private int tag_act_id = Integer.MAX_VALUE;

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView ==
     * null)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        //获取mid或者code
        int push_id = bundle.getInt(Notify_Push_Id, 0);//点击通知栏获得消息数据
        //通过mid或者自定义code获取bean
        bean_skip = DataManager.getInstance().getPushBeanById(push_id);
        //如果mid！=0，处理消息箱
        if (bean_skip != null && bean_skip.getMid() != 0) {
            readMessge(bean_skip.getMid());
        }

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                // String taskid = bundle.getString("taskid");
                // String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                // boolean result =
                // PushManager.getInstance().sendFeedbackMessage(context, taskid,
                // messageid, 90001);
                // System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    PushBean bean = GsonTools
                            .changeGsonToBean(data, PushBean.class);
                    if (bean == null) {
                        return;
                    }
                    Bundle extras = new Bundle();
                    int actionValue;
                    if (Action_Order_Detail.equals(bean.getAction())) {
                        actionValue = Action_Int_Order;
                    } else if (Action_Cart.equals(bean.getAction())) {
                        actionValue = Action_Int_Cart;
                    } else if (Action_Video.equals(bean.getAction())) {
                        actionValue = Action_Int_Video;
                    } else if (Action_Activity.equals(bean.getAction())) {
                        actionValue = Action_Int_Activity;
                    } else if (Action_Teacher_Detail.equals(bean.getAction())) {
                        actionValue = Action_Int_TeacherDetail;
                    } else if (Action_Organization_Success.equals(bean.getAction())) {
                        actionValue = Action_Int_Organization_Success;
                    } else if (Action_Organization_Add.equals(bean.getAction())) {
                        actionValue = Action_Int_Organization_Add;
                    } else if (Action_Auth_Update.equals(bean.getAction())) {
                        actionValue = Action_Int_Auth_Update;
                    } else if (Action_Exit_Org.equals(bean.getAction())) {
                        actionValue = Action_Int_Exit_Org;
                    } else if (Action_Exit_Org_Self.equals(bean.getAction())) {
                        actionValue = Action_Int_Exit_Org_Self;
                    } else if (Action_Class_Start.equals(bean.getAction())) {
                        actionValue = Action_Int_Class_Start;
                    } else if (Action_Class_Finish.equals(bean.getAction())) {
                        actionValue = Action_Int_Class_Finish;
                    } else if (Action_Organization_Fail.equals(bean.getAction())) {
                        actionValue = Action_Int_Organization_Fail;
                    } else if (ACTION_COURSE_REVIEW_SUCCESS.equals(bean.getAction())) {
                        actionValue = ACTION_INT_COURSE_REVIEW_SUCCESS;
                    } else if (ACTION_COURSE_REVIEW_FAIL.equals(bean.getAction())) {
                        actionValue = ACTION_INT_COURSE_REVIEW_FAIL;
                    } else if (ACTION_CANCEL_COURSE_TEACHER.equals(bean.getAction())) {
                        actionValue = ACTION_INT_CANCEL_COURSE_TEACHER;
                    } else if (ACTION_CANCEL_COURSE_SYSTEM.equals(bean.getAction())) {
                        actionValue = ACTION_INT_CANCEL_COURSE_SYSTEM;
                    } else if (ACTION_ORDER_PASS_PER_PAYMENT.equals(bean.getAction())) {
                        actionValue = ACTION_INT_ORDER_PASS_PER_PAYMENT;
                    } else if (ACTION_ORDER_NOT_PASS_PER_PAYMENT.equals(bean.getAction())) {
                        actionValue = ACTION_INT_ORDER_NOT_PASS_PER_PAYMENT;
                    } else if (ACTION_ORDER_PASS_AFTER_PAYMENT.equals(bean.getAction())) {
                        actionValue = ACTION_INT_ORDER_PASS_AFTER_PAYMENT;
                    } else if (ACTION_ORDER_NOT_PASS_AFTER_PAYMENT.equals(bean.getAction())) {
                        actionValue = ACTION_INT_ORDER_NOT_PASS_AFTER_PAYMENT;
                    } else if (ACTION_OPEN_ELECTRONIC_INVOICE.equals(bean.getAction())) {
                        actionValue = ACTION_INT_OPEN_ELECTRONIC_INVOICE;
                    } else if (ACTION_TEACHER_REVIEW_SUCCESS.equals(bean.getAction())) {
                        actionValue = ACTION_INT_TEACHER_REVIEW_SUCCESS;
                    } else if (ACTION_TEACHER_REVIEW_FAIL.equals(bean.getAction())) {
                        actionValue = ACTION_INT_TEACHER_REVIEW_FAIL;
                    } else {
                        actionValue = Action_Int_Null;
                    }
                    extras.putInt(ACTION, actionValue);
                    if (!isRuningApp(context)) {
                        StartApp(context);
                        return;
                    }
                    setNotification(bean, extras);
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                cid = bundle.getString("clientid");
                SharedPreferencesUtils.saveString(context,
                        SharedPreferencesConstant.Shared_Login_Cid, cid);
                break;

            case PushConsts.THIRDPART_FEEDBACK:
            /*
             * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 *
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
                break;
            case Action_Int_Cart://已经没有购物车了 未提交，购物车
//                if (!isRuningApp(context)) {
//                    StartApp(context);
//                    return;
//                }
//                intentCart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                BaseApplication.getmForegroundActivity().startActivity(intentCart);
                break;
            case Action_Int_Video:// 视频
                if (!isRuningApp(context)) {
                    StartApp(context);
                    return;
                }
                Intent intentVideo = new Intent(
                        BaseApplication.getmForegroundActivity(),
                        AccompanyReadStatusActivity.class);
//                intentVideo.putExtra(MainActivity.Param_Start_Fragment,
//                        FragmentFactory.TAB_VIDEO);
                intentVideo.putExtra(SchemeControl.PASSWORD, bean_skip.getContent().split("：")[1].split("，")[0].trim());
//                intentVideo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                BaseApplication.getmForegroundActivity().startActivity(
                        intentVideo);
                break;
            case Action_Int_Activity:// 活动页面
                if (!isCanContinue(context)) {
                    return;
                }
                if (StringUtils.isNotNullString(bean_skip.getUrl()) && StringUtils.isNotNullString(bean_skip.getTitle())) {
                    Intent intentActivity = new Intent(context, Common_Show_WebPage_Activity.class);
                    if (!"0".equals(bean_skip.getSpecial_id())) {
                        intentActivity.putExtra(UIUtils.getString(R.string.get_page_name),
                                WebConstant.WEB_value_special_teacher_Page);
                        intentActivity.putExtra("special_id", bean_skip.getSpecial_id());
                    }

                    intentActivity.putExtra(UIUtils.getString(R.string.redirect_open_url),
                            bean_skip.getUrl());
                    intentActivity.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                            bean_skip.getTitle());
//                    intentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    BaseApplication.getmForegroundActivity().startActivity(intentActivity);
                }
                break;
            case Action_Int_TeacherDetail://老师详情页
                if (!isCanContinue(context)) {
                    return;
                }
                if (StringUtils.isNotNullString(bean_skip.getTeacherID()) && StringUtils.isNotNullString(bean_skip.getTitle())) {
                    Intent intentActivity = new Intent(context, Common_Show_WebPage_Activity.class);
                    intentActivity.putExtra(UIUtils.getString(R.string.get_page_name),
                            WebConstant.WEB_Int_TeachInfo_Page);
                    intentActivity.putExtra(UIUtils.getString(R.string.redirect_tid),
                            bean_skip.getTeacherID());
                    intentActivity.putExtra(Common_Show_WebPage_Activity.Param_String_Title,
                            bean_skip.getTitle());
//                    intentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    BaseApplication.getmForegroundActivity().startActivity(intentActivity);
                }
                break;
            case Action_Int_Organization_Add://机构加人
            case Action_Int_Auth_Update://机构修改成员权限
            case Action_Int_Exit_Org_Self://老师退出机构
            case Action_Int_Organization_Success://机构认证成功
                break;
            case Action_Int_Exit_Org://机构移除老师
                if (!isRuningApp(context)) {
                    StartApp(context);
                    return;
                }
                Intent intentExitOrg = new Intent(
                        BaseApplication.getmForegroundActivity(),
                        PersonalCenterActivity.class);
//                intentExitOrg.putExtra(MainActivity.Param_Start_Fragment,
//                        FragmentFactory.TAB_MY);
//                intentExitOrg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                BaseApplication.getmForegroundActivity().startActivity(
                        intentExitOrg);
                break;
            case Action_Int_Order:// 订单 (已提交未支付)
            case Action_Int_Class_Start://订单消费提醒
            case Action_Int_Class_Finish://订单消费完毕
            case ACTION_INT_ORDER_PASS_PER_PAYMENT://支付前确认订单通过
            case ACTION_INT_ORDER_NOT_PASS_PER_PAYMENT://支付前确认订单未通过
            case ACTION_INT_ORDER_PASS_AFTER_PAYMENT://支付后确认订单通过
            case ACTION_INT_ORDER_NOT_PASS_AFTER_PAYMENT://支付后确认订单未通过
            case ACTION_INT_OPEN_ELECTRONIC_INVOICE://开电子发票
                if (!isRuningApp(context)) {
                    StartApp(context);
                    return;
                }
                if (bean_skip != null && StringUtils.isNotNullString(bean_skip.getOid())) {
                    Intent intentOrder;
                    if ("3".equals(bean_skip.getOrder_type())){
                        intentOrder = new Intent(context, CourseOrderDetailActivity.class);
                    }else {
                        intentOrder = new Intent(context, OrderDetailActivity.class);
                    }
                    intentOrder.putExtra("oid", bean_skip.getOid());
//                    intentOrder.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    BaseApplication.getmForegroundActivity().startActivity(intentOrder);
                }
                break;
            case Action_Int_Organization_Fail://机构认证失败
            case ACTION_INT_COURSE_REVIEW_SUCCESS://课程审核通过
            case ACTION_INT_COURSE_REVIEW_FAIL://课程审核不通过
            case ACTION_INT_CANCEL_COURSE_TEACHER://老师取消课程
            case ACTION_INT_CANCEL_COURSE_SYSTEM://系统取消课程
                break;
            case ACTION_INT_TEACHER_REVIEW_SUCCESS:
            case ACTION_INT_TEACHER_REVIEW_FAIL:
                break;
            default:
                break;
        }
    }


    // 设置通知
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setNotification(PushBean bean, Bundle extras) {
        int code;
        if (bean.getMid() <= 0) {
            code = Math.abs(new Random().nextInt());
            bean.setCode(code);
        } else {
            code = bean.getMid();
        }
        extras.putInt(Notify_Push_Id, code);
        Intent intent_Handler = new Intent(BaseApplication.getApplication(), PushClockReceiver.class);
        intent_Handler.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(BaseApplication.getApplication(),
                code, intent_Handler, 0);
        Notification notification = new Notification.Builder(BaseApplication.getApplication())
                .setAutoCancel(true)
                .setContentTitle(bean.getTitle())
                .setContentText(bean.getContent())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.clock_logo)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setWhen(System.currentTimeMillis())
                .build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(code, notification);
        }
        DataManager.getInstance().updatePushBeans(bean);
        DataManager.getInstance().updateNoReadCount(1);

        //30秒后关闭消息
//        Message msg = handler.obtainMessage(0, bean.getMid(), 0);
//        handler.sendMessageDelayed(msg, Delayed_Time);
    }

//    private Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            if (msg.what == 0 && mNotificationManager != null) {
////                DataManager.getInstance().deletePushById(msg.arg1);//删除消息
//                mNotificationManager.cancel(msg.arg1);
//            }
//        }
//    };


    /**
     * 是否可以继续跳转
     */
    private boolean isCanContinue(Context context) {
        if (isRuningApp(context) && bean_skip != null) {
            return true;
        } else {
            StartApp(context);
            return false;
        }
    }

    /**
     * 启动应用
     */
    private void StartApp(Context context) {
        Intent intentActivity = context.getPackageManager()
                .getLaunchIntentForPackage("com.yiju.ClassClockRoom");
        context.startActivity(intentActivity);
    }

    /**
     * 应用时否在运行
     */
    private boolean isRuningApp(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 已读消息关闭
     */
    private void readMessge(int mid) {
        HttpCommonApi.getInstance().readMessage(mid);
    }
}

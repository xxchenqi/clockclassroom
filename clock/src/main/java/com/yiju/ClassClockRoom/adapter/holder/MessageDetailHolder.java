package com.yiju.ClassClockRoom.adapter.holder;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.MemberDetailActivity;
import com.yiju.ClassClockRoom.act.MineOrganizationActivity;
import com.yiju.ClassClockRoom.act.OrderDetailActivity;
import com.yiju.ClassClockRoom.act.OrganizationCertificationStatusActivity;
import com.yiju.ClassClockRoom.act.PersonMineCourseDetailActivity;
import com.yiju.ClassClockRoom.act.PersonalCenterActivity;
import com.yiju.ClassClockRoom.act.ShopCartActivity;
import com.yiju.ClassClockRoom.act.accompany.AccompanyReadStatusActivity;
import com.yiju.ClassClockRoom.bean.MessageBox.MessageData;
import com.yiju.ClassClockRoom.control.SchemeControl;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageDetailHolder extends BaseRecyclerViewHolder {

    private LinearLayout ll_msg_item;
    private TextView tv_item_time;
    //    private TextView tv_item_title;
    private TextView tv_item_content;
    private MessageData messageData;

    public MessageDetailHolder(View view) {
        super(view);
    }

    @Override
    protected void findViews(View view) {
        super.findViews(view);
        tv_item_time = (TextView) view.findViewById(R.id.tv_item_time);
        ll_msg_item = (LinearLayout) view.findViewById(R.id.ll_msg_item);
//        tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
        tv_item_content = (TextView) view.findViewById(R.id.tv_item_content);
    }

    @Override
    public MessageData getModel() {
        return (MessageData) super.getModel();
    }

    @Override
    public void updateView(int position) {
        super.updateView(position);
        messageData = getModel();
        if (messageData == null) {
            return;
        }
        long s = Long.valueOf(messageData.getCreate_time());
        Date date = new Date(s * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String format = formatter.format(date);
        tv_item_time.setText(format);
        tv_item_content.setText(messageData.getContent());

    }


    @Override
    protected void setListeners() {
        super.setListeners();
        ll_msg_item.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int type = Integer.parseInt(messageData.getType());
        switch (type) {
            case 1://待支付订单
            case 7://退单申请(我的订单)
            case 10://订单成立(已支付页面)
            case 11://订单消费提醒
            case 12://订单消费完毕
            case 18://支付前确认通过
            case 19://支付前确认不通过
            case 20://支付后确认通过
            case 21://支付后确认不通过
            case 22://电子发票开票成功
                Intent intentOrder = new Intent(UIUtils.getContext(), OrderDetailActivity.class);
                intentOrder.putExtra("oid", messageData.getDetail_id());
                UIUtils.startActivity(intentOrder);
                break;
            case 2://购物车
                jumpMessageDetial("cart", new Intent(UIUtils.getContext(), ShopCartActivity.class));
                break;

            case 4://机构加人
            case 5://机构修改权限
            case 6://机构审核
            case 9://老师退出机构(我的机构)
                jumpMessageDetial("jigou", new Intent(UIUtils.getContext(), MineOrganizationActivity.class));
                break;
            case 3://陪读
//                jumpMessageDetial("read", new Intent(UIUtils.getContext(), MainActivity.class));
                String content = messageData.getContent().split("：")[1].split("，")[0].trim();
                Intent intent = new Intent();
                intent.setClass(UIUtils.getContext(), AccompanyReadStatusActivity.class);
                intent.putExtra(SchemeControl.PASSWORD, content);
                UIUtils.startActivity(intent);
                break;

            case 8://机构移除老师(个人中心)
                jumpMessageDetial("person", new Intent(UIUtils.getContext(), PersonalCenterActivity.class));
                break;
            case 13://机构审核失败
                jumpMessageDetial("fail", new Intent(UIUtils.getContext(), OrganizationCertificationStatusActivity.class));
                break;
            case 14://课程审核通过
            case 15://课程审核不通过
            case 16://老师取消课程
            case 17://系统取消课程
                Intent intent_personMineCourse = new Intent(UIUtils.getContext(), PersonMineCourseDetailActivity.class);
                intent_personMineCourse.putExtra("course_id", messageData.getDetail_id());
                UIUtils.startActivity(intent_personMineCourse);
                break;
            case 23://审核通过
            case 24://审核不通过
                Intent intent_member_detail = new Intent(UIUtils.getContext(), MemberDetailActivity.class);
                intent_member_detail.putExtra(MemberDetailActivity.ACTION_UID, messageData.getDetail_id());
                intent_member_detail.putExtra(MemberDetailActivity.ACTION_TITLE,
                        UIUtils.getString(R.string.teacher_detail));
                UIUtils.startActivity(intent_member_detail);
                break;

        }
    }

    private void jumpMessageDetial(String s, Intent intent) {

        if (null != s) {
            if ("read".equals(s)) {
                intent.putExtra("read", s);
            } else if ("person".equals(s)) {
                intent.putExtra("person", s);
            }/*else if("jigou".equals(s)){
                intent.putExtra("jigou", s);
            }else if("cart".equals(s)){
                UIUtils.startActivity(intent);
            }*/ else if ("fail".equals(s)) {
                intent.putExtra(OrganizationCertificationStatusActivity.STATUS,
                        OrganizationCertificationStatusActivity.STATUS_FAIL);
            } else if ("course".equals(s)) {
                intent.putExtra("course", s);
            } else {
                intent.putExtra("status", s);
            }
        }
        UIUtils.startActivity(intent);
    }
}

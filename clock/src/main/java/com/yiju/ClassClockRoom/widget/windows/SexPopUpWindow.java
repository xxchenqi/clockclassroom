package com.yiju.ClassClockRoom.widget.windows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.bean.result.MemberDetailResult;
import com.yiju.ClassClockRoom.bean.result.MineOrder;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpClassRoomApi;

public class SexPopUpWindow extends PopupWindow {
    private View contentView;
    private Button btn_male;
    private Button btn_female;
    private Button btn_cancel;
    private View v_shadow;
    private ListItemClickHelp mCallback;
    //传进来的id
    private String uid;
    private String title_flag;

    public SexPopUpWindow(ListItemClickHelp callback, final String uid, final boolean flag, final MemberDetailResult bean, final String title_flag) {
        this.title_flag = title_flag;
        mCallback = callback;
        this.uid = uid;
        LayoutInflater inflater = (LayoutInflater) UIUtils.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_personalcenter_sex, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);

        btn_male = (Button) contentView.findViewById(R.id.btn_male);
        btn_female = (Button) contentView.findViewById(R.id.btn_female);
        btn_cancel = (Button) contentView.findViewById(R.id.btn_sex_cancel);
        v_shadow = contentView.findViewById(R.id.v_shadow);

        btn_male.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
                mCallback.onClickItem(R.id.btn_male);
                if (flag) {
                    //用户性别修改
                    getHttpUtils("1");
                } else {
                    //老师性别修改
                    bean.getData().setSex("1");
                    HttpClassRoomApi.getInstance().askModifyMemberInfo(title_flag, uid, bean, true,true);
                }

            }
        });

        btn_female.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
                mCallback.onClickItem(R.id.btn_female);
                if (flag) {
                    //用户性别修改
                    getHttpUtils("2");
                } else {
                    //老师性别修改
                    bean.getData().setSex("2");
                    HttpClassRoomApi.getInstance().askModifyMemberInfo(title_flag, uid, bean, true,true);
                }

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        v_shadow.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(UIUtils.getContext().getResources().getColor(R.color.color_lucency));
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismissListener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 刷新状态
        this.update();
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);

    }

    private void getHttpUtils(final String sex) {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "modifyInfo");
        params.addBodyParameter("type", "sex");
        params.addBodyParameter("value", sex);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());

        httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result, sex);
                    }
                });
    }

    private void processData(String result, String sex) {
        MineOrder mineOrder = GsonTools.changeGsonToBean(result,
                MineOrder.class);
        if (mineOrder == null){
            return;
        }
        if ("1".equals(mineOrder.getCode())) {
            UIUtils.showToastSafe("更新成功");
            SharedPreferencesUtils.saveString(UIUtils.getContext(), contentView
                    .getResources().getString(R.string.shared_sex), sex);
        } else {
            UIUtils.showToastSafe(mineOrder.getMsg());
        }

    }


    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }
}

package com.yiju.ClassClockRoom.widget.windows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.util.UIUtils;

/**
 * 选择地图导航
 * Created by wh on 2016/5/3.
 */
public class NavigationWindow extends PopupWindow {

    private View contentView;
    private Button btn_baidu_map;
    private Button btn_auto_nav_map;
    private Button btn_map_cancel;
    private View v_shadow;

    private ListItemClickHelp mCallback;
    boolean isHaveBaidu;
    boolean isHaveGaoDe;

    public NavigationWindow(
            boolean isHaveBaidu,
            boolean isHaveGaoDe,
            ListItemClickHelp clickHelp
    ) {
        this.isHaveBaidu = isHaveBaidu;
        this.isHaveGaoDe = isHaveGaoDe;
        this.mCallback = clickHelp;

        LayoutInflater inflater = (LayoutInflater) UIUtils.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_navigation_map, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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

        btn_baidu_map = (Button) contentView.findViewById(R.id.btn_baidu_map);
        btn_auto_nav_map = (Button) contentView.findViewById(R.id.btn_auto_nav_map);
        btn_map_cancel = (Button) contentView.findViewById(R.id.btn_map_cancel);
        v_shadow = contentView.findViewById(R.id.v_shadow);

        if (!isHaveBaidu) {
            btn_baidu_map.setVisibility(View.GONE);
        } else {
            btn_baidu_map.setVisibility(View.VISIBLE);
        }

        if (!isHaveGaoDe) {
            btn_auto_nav_map.setVisibility(View.GONE);
        } else {
            btn_auto_nav_map.setVisibility(View.VISIBLE);
        }


        btn_baidu_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickItem(0);
            }
        });
        btn_auto_nav_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickItem(1);
            }
        });
        btn_map_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        v_shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}

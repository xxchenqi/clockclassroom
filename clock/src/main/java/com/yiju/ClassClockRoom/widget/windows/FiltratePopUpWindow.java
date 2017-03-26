package com.yiju.ClassClockRoom.widget.windows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.FiltrateAdapter;
import com.yiju.ClassClockRoom.bean.SchoolInfo;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:筛选框
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/6/16 16:10
 * ----------------------------------------
 */
public class FiltratePopUpWindow extends PopupWindow {

    //主view
    private final View contentView;
    //数据源
    private List<SchoolInfo> school;
    //gv
    private GridView gv_store_filtrate;
    //适配器
    private FiltrateAdapter adapter;
    //点击取消
    private View v_store_filtrate;
    //接口回调
    public ListItemClickHelp mCallback;

    public FiltratePopUpWindow(final List<SchoolInfo> school, final ListItemClickHelp mCallback) {
        this.mCallback = mCallback;
        this.school = school;
        LayoutInflater inflater = (LayoutInflater) UIUtils.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_filtrate, null);
        setContentView(contentView);
        gv_store_filtrate = (GridView) contentView.findViewById(R.id.gv_store_filtrate);
        v_store_filtrate = contentView.findViewById(R.id.v_store_filtrate);

        adapter = new FiltrateAdapter(UIUtils.getContext(), school, R.layout.item_filtrate);
        gv_store_filtrate.setAdapter(adapter);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(UIUtils.getContext().getResources().getColor(R.color.color_lucency_0));
        dw.setAlpha(25);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismissListener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 刷新状态
        this.update();
        v_store_filtrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltratePopUpWindow.this.dismiss();
            }
        });
        gv_store_filtrate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onClickItem(position);
                for (int i = 0; i < school.size(); i++) {
                    if (i == position) {
                        school.get(i).setFlag(true);
                    } else {
                        school.get(i).setFlag(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}

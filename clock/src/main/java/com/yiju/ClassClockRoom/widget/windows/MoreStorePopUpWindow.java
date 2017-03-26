package com.yiju.ClassClockRoom.widget.windows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.MoreStoreFiltrateAdapter;
import com.yiju.ClassClockRoom.bean.SchoolLeft;
import com.yiju.ClassClockRoom.common.callback.ListItemClickHelp;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:更多门店筛选
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/6/16 16:10
 * ----------------------------------------
 */
public class MoreStorePopUpWindow extends PopupWindow {

    //主view
    private final View contentView;
    //lv
    private ListView lv_pop_more_store;
    //适配器
    private MoreStoreFiltrateAdapter adapter;
    //点击取消
    private View v_more_store;

    public MoreStorePopUpWindow(Context context,
                                final List<SchoolLeft> datas_filtrate,
                                final ListItemClickHelp mCallback,
                                final ImageView head_right_image) {
        LayoutInflater inflater = (LayoutInflater) UIUtils.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_more_store, null);
        setContentView(contentView);
        v_more_store = contentView.findViewById(R.id.v_more_store);
        lv_pop_more_store = (ListView) contentView.findViewById(R.id.lv_pop_more_store);
        adapter = new MoreStoreFiltrateAdapter(context, datas_filtrate, R.layout.item_more_store_pop);
        lv_pop_more_store.setAdapter(adapter);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(UIUtils.getContext().getResources().getColor(R.color.color_lucency_0));
        dw.setAlpha(102);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismissListener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 刷新状态
        this.update();
        v_more_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoreStorePopUpWindow.this.dismiss();
            }
        });
        lv_pop_more_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < datas_filtrate.size(); i++) {
                    if (i == position) {
                        datas_filtrate.get(i).setFlag(true);
                    } else {
                        datas_filtrate.get(i).setFlag(false);
                    }
                }
                adapter.notifyDataSetChanged();
                MoreStorePopUpWindow.this.dismiss();
                mCallback.onClickItem(position);
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                head_right_image.setImageResource(R.drawable.down_pull);
            }
        });
    }
}

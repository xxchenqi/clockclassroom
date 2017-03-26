package com.yiju.ClassClockRoom.act.remind;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.CommonBaseAdapter;
import com.yiju.ClassClockRoom.bean.RemindAccompanyBean;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;

import java.util.List;

public class RemindAccompanyAdapter extends CommonBaseAdapter<RemindAccompanyBean> {
    private List<RemindAccompanyBean> datas;
    private String currentTime;
//    private IAccompanyRunnable iAccompanyRunnable;

    private IAccompanyRunnableSelectResult iAccompanyRunnableSelectResult;

    public interface IAccompanyRunnableSelectResult {
        void selectResult();
    }

    public RemindAccompanyAdapter(Context context, List<RemindAccompanyBean> datas, int layoutId,
                                  IAccompanyRunnableSelectResult selectResult) {
        super(context, datas, layoutId);
        this.datas = datas;
        this.iAccompanyRunnableSelectResult = selectResult;
    }

    @Override
    public void convert(final ViewHolder holder, final RemindAccompanyBean t) {
        holder.setText(R.id.item_accompany_time, t.getName());
        ImageView check = holder.getView(R.id.item_accompany_icon);
        check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckMode(t);
                notifyDataSetChanged();
                if (t.isIscheck()) {
                    currentTime = t.getTime();
                }
                iAccompanyRunnableSelectResult.selectResult();
            }
        });
        changeImage(check, t.isIscheck());
    }

    private void setCheckMode(final RemindAccompanyBean bean) {
//        if (bean.isIscheck()) {
//            //弹出处理
//            CustomDialog customDialog =
//                    new CustomDialog(mContext, "提示", "确认", "取消", "是否关闭陪读提醒！");
//            customDialog.setOnClickListener(new IOnClickListener() {
//
//                public void oncClick(boolean isOk) {
//                    if (isOk) {
//                        if (iAccompanyRunnable != null) {
//                            iAccompanyRunnable.onOffRemindAccompany(false);
//                        }
//                        bean.setIscheck(false);
//                    }
//                }
//            });
//        } else {
        for (RemindAccompanyBean mode : datas) {
            if (mode == bean) {
                mode.setIscheck(true);
            } else {
                mode.setIscheck(false);
            }
        }
//        }
    }

    private void changeImage(ImageView checkView, boolean isCheck) {
        if (isCheck) {
            checkView.setImageResource(R.drawable.order_choose_btn);
        } else {
            checkView.setImageResource(R.drawable.order_nonechoose_btn);
        }
    }

    public String getCurrentTime() {
        return currentTime;
    }

//    public void setOnOffRemind(IAccompanyRunnable iAccompanyRunnable) {
//        this.iAccompanyRunnable = iAccompanyRunnable;
//    }
}

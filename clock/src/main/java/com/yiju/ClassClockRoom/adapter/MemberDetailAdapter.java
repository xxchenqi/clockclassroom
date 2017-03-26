package com.yiju.ClassClockRoom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.holder.ViewHolder;
import com.yiju.ClassClockRoom.bean.MienBean;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.widget.RoundImageView;

import java.util.List;

/**
 * ----------------------------------------
 * 注释:
 * <p/>
 * 作者: cq
 * <p/>
 * 时间: on 2016/3/16 18:06
 * ----------------------------------------
 */
public class MemberDetailAdapter extends CommonBaseAdapter<MienBean> {

    private int size;
    private int mScreenWidth;
    private int mScreenHeight;
    private AddClickListener listener;
    private String user_org_auth;
    private boolean edit_flag;

    public MemberDetailAdapter(Context context, List<MienBean> datas, int layoutId, int size, int mScreenWidth, int mScreenHeight, AddClickListener listener, String user_org_auth, boolean edit_flag) {
        super(context, datas, layoutId);
        this.size = size;
        this.mScreenWidth = mScreenWidth;
        this.mScreenHeight = mScreenHeight;
        this.listener = listener;
        this.user_org_auth = user_org_auth;
        this.edit_flag = edit_flag;
    }

    @Override
    protected void convert(final ViewHolder holder, MienBean mienEntity) {
        RoundImageView iv_item_member = holder.getView(R.id.iv_item_member);

        ViewGroup.LayoutParams layoutParams = iv_item_member.getLayoutParams();
//        layoutParams.height = mScreenHeight/6;//一屏幕显示8行
        layoutParams.width = (mScreenWidth - UIUtils.dip2px(44)) / 2;//一屏显示两列
        iv_item_member.setLayoutParams(layoutParams);
        if (edit_flag == true) {
            if (size == 1) {
                iv_item_member.setBackgroundResource(R.drawable.add_picture_btn);
                iv_item_member.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.addClick();
                    }
                });

            } else {
                if (holder.getPosition() + 1 == size) {
                    iv_item_member.setBackgroundResource(R.drawable.add_picture_btn);
                    iv_item_member.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.addClick();
                        }
                    });
                } else {
                    Glide.with(UIUtils.getContext()).load(mienEntity.getPic()).into(iv_item_member);
                    iv_item_member.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.checkClick(holder.getPosition());
                        }
                    });
                }
            }
        } else {
            Glide.with(UIUtils.getContext()).load(mienEntity.getPic()).into(iv_item_member);
            iv_item_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.checkClick(holder.getPosition());
                }
            });
        }

    }

    public interface AddClickListener {
        void addClick();

        void checkClick(int pos);
    }


}

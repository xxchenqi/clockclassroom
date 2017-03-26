package com.yiju.ClassClockRoom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;

/*
 * Created by wh on 2016/3/4.
 */
public class StoreImageView extends FrameLayout {

//    private RatioLayout rl_ratio;
    private ImageView imageView;

    public StoreImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StoreImageView(Context context) {
        super(context);
    }

    public StoreImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImgResource(String pic_big) {
        if (StringUtils.isNotNullString(pic_big)) {
            /*if (pic_big.contains("/w/")) {
                String[] split = pic_big.split("/w/")[1].split("/h/");
                if (split.length == 2) {
                    int w = Integer.parseInt(split[0]);//宽
                    int h = Integer.parseInt(split[1]);//高
                    rl_ratio.setRatio((float) w / h);
                    rl_ratio.postInvalidate();
                }
            } else {
                rl_ratio.setRatio((float) 1.43);
                rl_ratio.postInvalidate();
            }*/
            Glide.with(UIUtils.getContext()).load(pic_big).placeholder(R.drawable.bg_placeholder_16_9).into(imageView);
        } else {
           /* rl_ratio.setRatio((float) 1.43);
            rl_ratio.postInvalidate();*/
            imageView.setImageResource(R.drawable.bg_placeholder_16_9);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.view_store_image, this);
//        rl_ratio = (RatioLayout) view.findViewById(R.id.rl_ratio);
        imageView = (ImageView) view.findViewById(R.id.iv_store_image);
    }
}

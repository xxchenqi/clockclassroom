package com.yiju.ClassClockRoom.util.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.yiju.ClassClockRoom.util.UIUtils;

class ViewAnimation extends Animation {

    private View mAnimationView = null;
    private RelativeLayout.LayoutParams mViewLayoutParams = null;
    private int mStart = 0;
    private int mEnd = 0;

    public ViewAnimation(View view){
        animationSettings(view, 1000);
    }

    public ViewAnimation(View view, int duration){
        animationSettings(view, duration);
    }

    private void animationSettings(View view, int duration){
        setDuration(duration);
        mAnimationView = view;
        mViewLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        mStart = mViewLayoutParams.bottomMargin;
        mEnd = (mStart == 0 ? (0 - UIUtils.getHeight(view)) : 0);//view.getHeight()
        view.setVisibility(View.VISIBLE);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if(interpolatedTime < 1.0f){
            mViewLayoutParams.bottomMargin = mStart + (int) ((mEnd - mStart) * interpolatedTime);
            // invalidate
            mAnimationView.requestLayout();
        }else{
            mViewLayoutParams.bottomMargin = mEnd;
            mAnimationView.requestLayout();
            if(mEnd != 0){
                mAnimationView.setVisibility(View.GONE);
            }
        }
    }


}

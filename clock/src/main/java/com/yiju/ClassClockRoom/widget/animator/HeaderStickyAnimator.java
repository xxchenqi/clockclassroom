package com.yiju.ClassClockRoom.widget.animator;


import android.view.View;

public class HeaderStickyAnimator extends BaseStickyHeaderAnimator {

    protected AnimatorBuilder mAnimatorBuilder;
    private float mBoundedTranslatedRatio;
    private boolean hasAnimatorBundles = false;

    @Override
    protected void onAnimatorReady() {
        super.onAnimatorReady();
        mAnimatorBuilder = getAnimatorBuilder();
        hasAnimatorBundles = (mAnimatorBuilder != null) && (mAnimatorBuilder.hasAnimatorBundles());
    }

    /**
     * Override if you want to load the animator builder
     */
    public AnimatorBuilder getAnimatorBuilder() {
        return null;
    }

    @Override
    public void onScroll(int scrolledY,View tv_bg_color, View tv_address_top,View tv_type_top) {
        super.onScroll(scrolledY,tv_bg_color,tv_address_top,tv_type_top);

        mBoundedTranslatedRatio = clamp(getTranslationRatio(), 0f, 1f);
        tv_address_top.setAlpha(mBoundedTranslatedRatio);// 设置顶部地址透明显示
        tv_type_top.setAlpha(mBoundedTranslatedRatio);
        tv_bg_color.setAlpha(clamp(getTranslationRatio(), 0f, 0.8f));
        if (hasAnimatorBundles) {
            mAnimatorBuilder.animateOnScroll(mBoundedTranslatedRatio, StikkyCompat.getTranslationY(getHeader()));
        }

    }

    public float getBoundedTranslatedRatio() {
        return mBoundedTranslatedRatio;
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }
}
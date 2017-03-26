package com.yiju.ClassClockRoom.widget.animator;


import android.view.View;

public class BaseStickyHeaderAnimator extends HeaderAnimator {

    private float mTranslationRatio;

    @Override
    protected void onAnimatorAttached() {
        //nothing to do
    }

    @Override
    protected void onAnimatorReady() {
        //nothing to do
    }

    @Override
    public void onScroll(int scrolledY,View tv_bg_color,View tv_address_top,View tv_type_top) {
        StikkyCompat.setTranslationY(mHeader, Math.max(scrolledY, getMaxTranslation()));
        mTranslationRatio = calculateTranslationRatio(scrolledY);
    }

    public float getTranslationRatio() {
        return mTranslationRatio;
    }

    private float calculateTranslationRatio(int scrolledY) {
        return (float) scrolledY / (float) getMaxTranslation();
    }

}

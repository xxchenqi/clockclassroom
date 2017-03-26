package com.yiju.ClassClockRoom.widget.animator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public abstract class StikkyHeader {

    protected Context mContext;

    protected View mHeader;
    protected int mMinHeightHeader;
    private HeaderAnimator mHeaderAnimator;
    protected int mHeightHeader;
    private View.OnTouchListener onTouchListenerOnHeaderDelegate;
    private boolean mAllowTouchBehindHeader;

    protected StikkyHeader(Context context, View header, int minHeightHeader, HeaderAnimator headerAnimator) {
        mContext = context;
        mHeader = header;
        mMinHeightHeader = minHeightHeader;
        mHeaderAnimator = headerAnimator;
    }

    protected abstract View getScrollingView();

    /**
     * Init method called when the StikkyHeader is created
     */
    protected void init() {
        setupAnimator();
        measureHeaderHeight();
    }

    void build(boolean allowTouchBehindHeader) {
        mAllowTouchBehindHeader = allowTouchBehindHeader;
        setOnTouchListenerOnHeader(onTouchListenerOnHeaderDelegate);
        init();
    }

    private void measureHeaderHeight() {
        int height = mHeader.getHeight();

        if (height == 0) {
            ViewGroup.LayoutParams lp = mHeader.getLayoutParams();
            if (lp != null) {
                height = lp.height;
            }
        }

        if (height > 0) {
            setHeightHeader(height);
        }

        mHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mHeightHeader != mHeader.getHeight()) {
                    setHeightHeader(mHeader.getHeight());
                }
            }
        });
    }

    protected void setHeightHeader(final int heightHeader) {
        mHeightHeader = heightHeader;
        mHeaderAnimator.setHeightHeader(heightHeader, mMinHeightHeader);
    }

    public void onScroll(int yScroll,View tv_bg_color,View tv_address_top,View tv_type_top) {
        mHeaderAnimator.onScroll(yScroll,tv_bg_color,tv_address_top,tv_type_top);
    }

    private void setupAnimator() {
        mHeaderAnimator.setupAnimator(mHeader);
    }

    public void setMinHeightHeader(int minHeightHeader) {
        this.mMinHeightHeader = minHeightHeader;
        mHeaderAnimator.setHeightHeader(mHeightHeader, mMinHeightHeader);
    }

    public void setOnTouchListenerOnHeader(View.OnTouchListener onTouchListenerOnHeaderDelegate) {
        this.onTouchListenerOnHeaderDelegate = onTouchListenerOnHeaderDelegate;
        StickyHeaderUtils.setOnTouchListenerOnHeader(mHeader, getScrollingView(), onTouchListenerOnHeaderDelegate, mAllowTouchBehindHeader);
    }

    public View getHeader() {
        return mHeader;
    }

}

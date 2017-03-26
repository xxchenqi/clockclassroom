package com.yiju.ClassClockRoom.widget.animator;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ListView;

import com.yiju.ClassClockRoom.R;


public abstract class StikkyHeaderBuilder {

    protected final Context mContext;

    protected View mHeader;
    protected View mHeaderBottom;
    protected View tv_bg_color;
    protected View tv_address_top;
    protected View tv_type_top;
    protected int mMinHeight;
    protected HeaderAnimator mAnimator;
    protected boolean mAllowTouchBehindHeader;

    protected StikkyHeaderBuilder(final Context context) {
        mContext = context;
        mMinHeight = 0;
        mAllowTouchBehindHeader = false;
    }

    public abstract StikkyHeader build();

    public static ListViewBuilder stickTo(final ListView listView) {
        return new ListViewBuilder(listView);
    }

//    public static RecyclerViewBuilder stickTo(final ViewGroup recyclerView) {
//        StickyHeaderUtils.checkRecyclerView(recyclerView);
//        return new RecyclerViewBuilder(recyclerView);
//    }
//
//    public static ScrollViewBuilder stickTo(final ScrollView scrollView) {
//        return new ScrollViewBuilder(scrollView);
//    }
//
//    public static TargetBuilder stickTo(final Context context) {
//        return new TargetBuilder(context);
//    }

    public StikkyHeaderBuilder setHeader(@IdRes final int idHeader, final View view) {
        mHeader = view.findViewById(idHeader);
        mHeaderBottom = view.findViewById(R.id.linearLayout1);
        tv_bg_color = view.findViewById(R.id.tv_bg_color);
        tv_address_top = view.findViewById(R.id.tv_address_top);
        tv_type_top = view.findViewById(R.id.tv_type_top);
        return this;
    }

    public StikkyHeaderBuilder setHeader(final View header) {
        mHeader = header;
        return this;
    }

    /**
     * Deprecated: use {@link #minHeightHeaderDim(int)}
     */
    @Deprecated
    public StikkyHeaderBuilder minHeightHeaderRes(@DimenRes final int resDimension) {
        return minHeightHeaderDim(resDimension);
    }

    public StikkyHeaderBuilder minHeightHeaderDim(@DimenRes final int resDimension) {
        mMinHeight = mContext.getResources().getDimensionPixelSize(resDimension);
        return this;
    }

    /**
     * Deprecated: use {@link #minHeightHeader(int)}
     */
    @Deprecated
    public StikkyHeaderBuilder minHeightHeaderPixel(final int minHeight) {
        return minHeightHeader(minHeight);
    }

    public StikkyHeaderBuilder minHeightHeader(final int minHeight) {
        mMinHeight = minHeight;
        return this;
    }

    public StikkyHeaderBuilder animator(final HeaderAnimator animator) {
        mAnimator = animator;
        return this;
    }

    /**
     * Allows the touch of the views behind the StikkyHeader. by default is false
     *
     * @param allow true to allow the touch behind the StikkyHeader, false to allow only the scroll.
     */
    public StikkyHeaderBuilder allowTouchBehindHeader(boolean allow) {
        mAllowTouchBehindHeader = allow;
        return this;
    }

    public static class ListViewBuilder extends StikkyHeaderBuilder {

        private final ListView mListView;

        protected ListViewBuilder(final ListView listView) {
            super(listView.getContext());
            mListView = listView;
        }

        @Override
        public StikkyHeaderListView build() {

            //if the animator has not been set, the default one is used
            if (mAnimator == null) {
                mAnimator = new HeaderStickyAnimator();
            }

            final StikkyHeaderListView stikkyHeaderListView = new StikkyHeaderListView(
                    mContext, mListView, mHeader, mHeaderBottom, tv_bg_color,
                    tv_address_top,tv_type_top, mMinHeight, mAnimator);
            stikkyHeaderListView.build(mAllowTouchBehindHeader);

            return stikkyHeaderListView;
        }
    }

//    public static class RecyclerViewBuilder extends StikkyHeaderBuilder {
//
//        private final RecyclerView mRecyclerView;
//
//        protected RecyclerViewBuilder(final ViewGroup mRecyclerView) {
//            super(mRecyclerView.getContext());
//            this.mRecyclerView = (RecyclerView) mRecyclerView;
//        }
//
//        @Override
//        public StikkyHeaderRecyclerView build() {
//
//            //if the animator has not been set, the default one is used
//            if (mAnimator == null) {
//                mAnimator = new HeaderStickyAnimator();
//            }
//
//            final StikkyHeaderRecyclerView stikkyHeaderRecyclerView = new StikkyHeaderRecyclerView(mContext, mRecyclerView, mHeader, mMinHeight, mAnimator);
//            stikkyHeaderRecyclerView.build(mAllowTouchBehindHeader);
//
//            return stikkyHeaderRecyclerView;
//        }
//
//    }

//    public static class ScrollViewBuilder extends StikkyHeaderBuilder {
//
//        private final ScrollView mScrollView;
//
//        protected ScrollViewBuilder(final ScrollView scrollView) {
//            super(scrollView.getContext());
//            this.mScrollView = scrollView;
//        }
//
//        @Override
//        public StikkyHeaderScrollView build() {
//
//            //if the animator has not been set, the default one is used
//            if (mAnimator == null) {
//                mAnimator = new HeaderStickyAnimator();
//            }
//
//            final StikkyHeaderScrollView stikkyHeaderScrollView = new StikkyHeaderScrollView(mContext, mScrollView, mHeader, mMinHeight, mAnimator);
//
//            stikkyHeaderScrollView.build(mAllowTouchBehindHeader);
//
//            return stikkyHeaderScrollView;
//        }
//
//    }

//    public static class TargetBuilder extends StikkyHeaderBuilder {
//
//        private final Context mContext;
//
//        protected TargetBuilder(final Context context) {
//            super(context);
//            mContext = context;
//        }
//
//        @Override
//        public StikkyHeaderTarget build() {
//
//            //if the animator has not been set, the default one is used
//            if (mAnimator == null) {
//                mAnimator = new HeaderStickyAnimator();
//            }
//
//            final StikkyHeaderTarget stikkyHeaderTarget = new StikkyHeaderTarget(mContext, mHeader, mMinHeight, mAnimator);
//            stikkyHeaderTarget.build(mAllowTouchBehindHeader);
//
//            return stikkyHeaderTarget;
//        }
//
//    }

}

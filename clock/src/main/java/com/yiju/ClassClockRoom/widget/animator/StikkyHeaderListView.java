package com.yiju.ClassClockRoom.widget.animator;


import android.content.Context;
import android.support.v4.widget.Space;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

public class StikkyHeaderListView extends StikkyHeader {

    private final ListView mListView;
    private View mHeaderBottom;
    private View tv_bg_color;
    private View tv_address_top;
    private View tv_type_top;
    private AbsListView.OnScrollListener mDelegateOnScrollListener;
    private View mFakeHeader;

    StikkyHeaderListView(
            final Context context,
            final ListView listView,
            final View header,
            final View mHeaderBottom,
            final View tv_bg_color,
            final View tv_address_top,
            final View tv_type_top,
            final int minHeightHeader,
            final HeaderAnimator headerAnimator
    ) {
        super(context, header, minHeightHeader, headerAnimator);
        this.mListView = listView;
        this.mHeaderBottom = mHeaderBottom;
        this.tv_bg_color = tv_bg_color;
        this.tv_address_top = tv_address_top;
        this.tv_type_top = tv_type_top;
    }

    @Override
    protected View getScrollingView() {
        return mListView;
    }

    protected void init() {
        createFakeHeader();
        super.init();
        setStickyOnScrollListener();
    }

    private void createFakeHeader() {
        mFakeHeader = new Space(mContext);

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mFakeHeader.setLayoutParams(lp);

        mListView.addHeaderView(mFakeHeader);
    }

    @Override
    protected void setHeightHeader(int heightHeader) {
        super.setHeightHeader(heightHeader);

        ViewGroup.LayoutParams lpFakeHeader = mFakeHeader.getLayoutParams();
        lpFakeHeader.height = heightHeader;
        mFakeHeader.setLayoutParams(lpFakeHeader);
    }

    private void setStickyOnScrollListener() {
        StickyOnScrollListener mStickyOnScrollListener = new StickyOnScrollListener();
        mListView.setOnScrollListener(mStickyOnScrollListener);
    }

    private class StickyOnScrollListener implements AbsListView.OnScrollListener {

        private int mScrolledYList = 0;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mDelegateOnScrollListener != null) {
                mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mScrolledYList = -calculateScrollYList();

            //notify the animator
            StikkyHeaderListView.this.onScroll(mScrolledYList, tv_bg_color,tv_address_top,tv_type_top);
            if (mScrolledYList == 0) {
                mHeaderBottom.setVisibility(View.VISIBLE);
            } else {
                mHeaderBottom.setVisibility(View.GONE);
            }

            if (mDelegateOnScrollListener != null) {
                mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        private int calculateScrollYList() {
            View c = mListView.getChildAt(0);
            if (c == null) {
                return 0;
            }

            //support more than 1 header?

            int firstVisiblePosition = mListView.getFirstVisiblePosition();

            int headerHeight = 0;
            if (firstVisiblePosition >= 1) { // >= number of header
                headerHeight = mHeightHeader;
            }

            return -c.getTop() + firstVisiblePosition * c.getHeight() + headerHeight;
        }

    }

    public void setOnScrollListener(final AbsListView.OnScrollListener onScrollListener) {
        mDelegateOnScrollListener = onScrollListener;
    }

}

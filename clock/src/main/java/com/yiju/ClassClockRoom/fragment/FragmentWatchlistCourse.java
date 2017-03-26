package com.yiju.ClassClockRoom.fragment;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.CourseDetailActivity;
import com.yiju.ClassClockRoom.adapter.WatchlistCourseAdapter;
import com.yiju.ClassClockRoom.bean.WatchlistCourseResult;
import com.yiju.ClassClockRoom.bean.base.BaseEntity;
import com.yiju.ClassClockRoom.common.callback.ListItemClickTwoData;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ResultCallImpl;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.util.net.api.HttpCommonApi;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注_课程
 * Created by wh on 2016/8/9.
 */
public class FragmentWatchlistCourse extends BaseFragment implements View.OnClickListener {
    private PullToRefreshListView lv_watchlist_course;
    private ImageView iv_mine_watchlist_course_none;
    private RelativeLayout ly_wifi;
    //刷新
    private Button btn_no_wifi_refresh;

    private WatchlistCourseAdapter adapter;
    private List<WatchlistCourseResult.DataEntity> mList = new ArrayList<>();

    //参数limit开始
    private int limit = 0;
    //参数limit数量
    private int limit_end = 5;
    //是否为下拉刷新，默认为是
    private boolean flag_down = true;

    @Override
    public void initIntent() {
        super.initIntent();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        lv_watchlist_course = (PullToRefreshListView) currentView.findViewById(R.id.lv_watchlist_course);
        iv_mine_watchlist_course_none = (ImageView) currentView.findViewById(R.id.iv_mine_watchlist_course_none);
        ly_wifi = (RelativeLayout) currentView.findViewById(R.id.ly_wifi);
        btn_no_wifi_refresh = (Button) currentView.findViewById(R.id.btn_no_wifi_refresh);

        adapter = new WatchlistCourseAdapter(mList, new ListItemClickTwoData() {
            @Override
            public void onClickItem(int position) {
            }

            @Override
            public void onDeleteItem(final View v, final int position) {
                if (mList == null) {
                    return;
                }
                if ("-1".equals(StringUtils.getUid())) {
                    return;
                }
                //取消关注
                HttpCommonApi.attentionAction("uninterest", StringUtils.getUid(), mList.get(position).getCourse_id(), "2",
                        new ResultCallImpl<BaseEntity>() {
                            @Override
                            public void onNext(BaseEntity bean) {
                                deleteCell(v, position);
                            }
                        }
                );

            }
        });
        lv_watchlist_course.setAdapter(adapter);
        //刷新设置
        lv_watchlist_course.setMode(PullToRefreshBase.Mode.BOTH);
        lv_watchlist_course.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                limit = 0;
                limit_end = 5;
                flag_down = true;
                getHttpRequest();
                //如果数据全部已请求完后，lv设置只能下拉刷新，重新下拉刷新后再去打开上拉加载
                lv_watchlist_course.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                limit += 5;
                limit_end = 5;
                flag_down = false;
                getHttpRequest();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_no_wifi_refresh.setOnClickListener(this);
        lv_watchlist_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                intent.putExtra("COURSE_ID", mList.get(position - 1).getCourse_id());
                intent.putExtra("pos", position - 1);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String Is_interest = data.getStringExtra("Is_interest");
            int pos = data.getIntExtra("pos", -1);
            if ("0".equals(Is_interest)) {//被取消关注
                if (mList == null) {
                    return;
                }
                mList.remove(pos);
                adapter.notifyDataSetChanged();
                if (mList != null && mList.size() == 0) {
                    iv_mine_watchlist_course_none.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void initData() {
        //请求
        if (NetWorkUtils.getNetworkStatus(getActivity())) {
            ly_wifi.setVisibility(View.GONE);
            lv_watchlist_course.setVisibility(View.VISIBLE);
            getHttpRequest();
        } else {
            ly_wifi.setVisibility(View.VISIBLE);
            lv_watchlist_course.setVisibility(View.GONE);
        }
    }

    private void getHttpRequest() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "get_my_interest_list");
        if (!"-1".equals(StringUtils.getUid())) {
            params.addBodyParameter("uid", StringUtils.getUid());
        }
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        params.addBodyParameter("type", "2");
        params.addBodyParameter("limit", limit + "," + limit_end);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_USER_API, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processData(arg0.result);

                    }
                }
        );
    }

    private void processData(String result) {
        WatchlistCourseResult courseResult = GsonTools.changeGsonToBean(result,
                WatchlistCourseResult.class);
        if (courseResult == null) {
            return;
        }
        if ("1".equals(courseResult.getCode())) {
            List<WatchlistCourseResult.DataEntity> list = courseResult.getData();
            if (list == null || list.size() == 0) {
                if (limit == 0) {
                    iv_mine_watchlist_course_none.setVisibility(View.VISIBLE);
                    lv_watchlist_course.setVisibility(View.GONE);
                }
                if (!flag_down) {
                    lv_watchlist_course.onRefreshComplete();
                    lv_watchlist_course.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    mList.clear();
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (limit == 0) {
                    iv_mine_watchlist_course_none.setVisibility(View.GONE);
                    lv_watchlist_course.setVisibility(View.VISIBLE);
                }
                if (flag_down) {
                    mList.clear();
                }
                mList.addAll(list);
                adapter.notifyDataSetChanged();
                lv_watchlist_course.onRefreshComplete();
            }
            flag_down = true;
        }
        lv_watchlist_course.onRefreshComplete();
    }

    @Override
    public int setContentViewId() {
        return R.layout.fragment_watchlist_course;
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_no_wifi_refresh:
                if (NetWorkUtils.getNetworkStatus(getActivity())) {
                    ly_wifi.setVisibility(View.GONE);
                    lv_watchlist_course.setVisibility(View.VISIBLE);
                    getHttpRequest();
                } else {
                    ly_wifi.setVisibility(View.VISIBLE);
                    lv_watchlist_course.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mList.remove(index);
                WatchlistCourseAdapter.ViewHolder vh = (WatchlistCourseAdapter.ViewHolder) v.getTag();
                vh.needInflate = true;
                adapter.notifyDataSetChanged();
                if (mList != null && mList.size() == 0) {
                    iv_mine_watchlist_course_none.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };
        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();
        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al != null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(300);
        v.startAnimation(anim);
    }
}

package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.CourseAdapter;
import com.yiju.ClassClockRoom.adapter.SchoolLeftAdapter;
import com.yiju.ClassClockRoom.adapter.SchoolRightAdapter;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.CourseMoreData;
import com.yiju.ClassClockRoom.bean.SchoolAll;
import com.yiju.ClassClockRoom.bean.SchoolInfo;
import com.yiju.ClassClockRoom.bean.SchoolLeft;
import com.yiju.ClassClockRoom.bean.SchoolRight;
import com.yiju.ClassClockRoom.control.ActivityControlManager;
import com.yiju.ClassClockRoom.control.ExtraControl;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.dialog.ProgressDialog;
import com.yiju.ClassClockRoom.widget.windows.FiltratePopUpWindow;

import java.util.ArrayList;
import java.util.List;

import static com.yiju.ClassClockRoom.R.string.price_old;

/**
 * ----------------------------------------
 * 注释: 更多课程
 * <p/>
 * 作者: sandy
 * <p/>
 * 时间: 2016/6/16 16:06
 * ----------------------------------------
 */
public class CourseMoreActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //后退
    @ViewInject(R.id.head_back_relative)
    private RelativeLayout head_back_relative;
    //标题
    @ViewInject(R.id.head_title)
    private TextView head_title;
    //lv
    @ViewInject(R.id.list_course_more)
    private PullToRefreshListView list_course_more;
    //门店筛选
    @ViewInject(R.id.ll_filtrate_store)
    private LinearLayout ll_filtrate_store;
    //门店筛选文字
    @ViewInject(R.id.tv_filtrate_store)
    private TextView tv_filtrate_store;
    //门店排序图片
    @ViewInject(R.id.iv_filtrate_store)
    private ImageView iv_filtrate_store;
    //价格排序
    @ViewInject(R.id.ll_filtrate_price)
    private LinearLayout ll_filtrate_price;
    //价格排序
    @ViewInject(R.id.tv_filtrate_price)
    private TextView tv_filtrate_price;
    //价格排序图片
    @ViewInject(R.id.iv_filtrate_price)
    private ImageView iv_filtrate_price;
    //分割线
    @ViewInject(R.id.v_course_divider)
    private View v_course_divider;

    @ViewInject(R.id.fl_back)
    private FrameLayout fl_back;

    //起始值
    private int limitStart = 0;
    //数量
    private int limitEnd = 5;
    //数据源
    private List<Object> mCourseLists = new ArrayList<>();
    //门店sid
    private List<SchoolInfo> school;
    //适配器
    private CourseAdapter courseAdapter;
    //是否为下拉刷新，默认为是
    private boolean flag_down = true;
    //筛选窗口
    private FiltratePopUpWindow filtratePopUpWindow;
    //是否是第一次加载
    private int isFirst = 0;
    //学校id
    private String sid;
    //当前的门店名字
//    private String currentSname;
    //默认价格从低到高
    private boolean price_flag = true;
    //默认经纬度排序
    private boolean distance_flag = true;
    private List<SchoolLeft> mListLefts;
    private SchoolLeftAdapter schoolLeftAdapter;
    private SchoolRightAdapter schoolRightAdapter;
    private ListView list_pop_left;
    private ListView list_pop_right;
    private TextView tv_price_old;
    private TextView tv_price_low;
    private TextView tv_price_high;
    private PopupWindow popupWindow;
    private String dist_id;
    private int positionOne = Integer.MAX_VALUE;
    private int positionTwo = Integer.MAX_VALUE;

    @Override
    public void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent != null) {
            sid = intent.getStringExtra("SCHOOL_ID");
        }
    }

    @Override
    protected void initView() {
        courseAdapter = new CourseAdapter(mCourseLists);
        list_course_more.setAdapter(courseAdapter);
        //刷新设置
        list_course_more.setMode(PullToRefreshBase.Mode.BOTH);
        list_course_more.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = true;
                limitStart = 0;
                limitEnd = 5;
                getData();
                list_course_more.setMode(PullToRefreshBase.Mode.BOTH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                flag_down = false;
                limitStart += 5;
                limitEnd = 5;
                getData();
            }
        });
    }

    @Override
    protected void initData() {
        head_title.setText(getString(R.string.course_more_title));
        getData();
    }

    @Override
    public void initListener() {
        super.initListener();
        head_back_relative.setOnClickListener(this);
        ll_filtrate_store.setOnClickListener(this);
        ll_filtrate_price.setOnClickListener(this);
        list_course_more.setOnItemClickListener(this);
//        ll_filtrate_store.setClickable(false);
//        ll_filtrate_price.setClickable(false);
    }

    /**
     * 获取数据
     */
    private void getData() {
        sendHttp("area_school_list", UrlUtils.SERVER_API_COMMON);
        sendHttp("get_course_list", UrlUtils.SERVER_COURSE);
    }

    private void sendHttp(final String action, String url) {
        ProgressDialog.getInstance().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        if (action.equals("get_course_list")) {
            if (distance_flag) {
                double latitude = LocationSingle.getInstance().getLatitude();
                double longitude = LocationSingle.getInstance().getLongitude();
                if (latitude != 0 && longitude != 0) {
                    params.addBodyParameter("lng_g", longitude + "");
                    params.addBodyParameter("lat_g", latitude + "");
                }
            }
            params.addBodyParameter("limit", limitStart + "," + limitEnd);
            if (sid != null && !"".equals(sid)) {
                params.addBodyParameter("sid", sid);
            }
            if (dist_id != null && !"".equals(dist_id)) {
                params.addBodyParameter("dist_id", dist_id);
            }
            if (price_flag) {
                params.addBodyParameter("order_price", "0");
            } else {
                params.addBodyParameter("order_price", "1");
            }
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        UIUtils.showToastSafe(R.string.fail_network_request);
                        ProgressDialog.getInstance().dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        ProgressDialog.getInstance().dismiss();
                        processData(arg0.result, action);

                    }
                });
    }

    /**
     * 处理返回的数据
     *
     * @param result 结果集
     * @param action 请求
     */
    private void processData(String result, String action) {
        if (null != result) {
            switch (action) {
                case "area_school_list":
                    SchoolAll schoolAll = GsonTools.changeGsonToBean(result, SchoolAll.class);
                    if (null != schoolAll) {
                        if ("1".equals(schoolAll.getCode())) {
                            mListLefts = schoolAll.getData();
                        }
                    }
                    break;
                case "get_course_list":
                    CourseMoreData coursemoredata = GsonTools.changeGsonToBean(result, CourseMoreData.class);
                    if (null != coursemoredata) {
                        if ("1".equals(coursemoredata.getCode())) {
                            CourseMoreData.Course_School data = coursemoredata.getData();
                            List<CourseInfo> courses = data.getCourse();
                            if (null != courses && courses.size() > 0) {
                                //如果是下拉刷新，数据全部清空
                                if (flag_down) {
                                    mCourseLists.clear();
                                }
                                mCourseLists.addAll(courses);
                                courseAdapter.notifyDataSetChanged();
                                list_course_more.onRefreshComplete();
                            } else {
                                if (!flag_down) {
                                    list_course_more.onRefreshComplete();
                                    list_course_more.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                                } else {
                                    mCourseLists.clear();
                                    courseAdapter.notifyDataSetChanged();
                                }
                            }
                            flag_down = true;
                        } else {
                            UIUtils.showToastSafe(coursemoredata.getMsg());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getPageName() {
        return getString(R.string.course_more);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_course_more;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_relative:
                onBackPressed();
                break;
            case R.id.ll_filtrate_store:
                if (null != mListLefts && mListLefts.size() > 0) {
                    showPopupWindow(ll_filtrate_store);
                }
                break;
            case R.id.ll_filtrate_price:
                showPopupWindow(ll_filtrate_price);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityControlManager.getInstance().finishCurrentAndOpenHome(this,1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = mCourseLists.get(position - 1);
        if (o instanceof CourseInfo) {
            CourseInfo courseInfo = (CourseInfo) o;
            Intent intent = new Intent(UIUtils.getContext(), NewCourseDetailActivity.class);
            intent.putExtra(ExtraControl.EXTRA_COURSE_ID, courseInfo.getId());
            UIUtils.startActivity(intent);
        }
    }

    /**
     * 弹出筛选窗口
     *
     * @param view 窗口位置基于的View
     */
    private void showPopupWindow(View view) {

        View contentView = null;
        switch (view.getId()) {
            case R.id.ll_filtrate_price:
                contentView = LayoutInflater.from(mContext).inflate(
                        R.layout.pop_window_price, null);
                tv_price_old = (TextView) contentView.findViewById(R.id.tv_price_old);
                tv_price_low = (TextView) contentView.findViewById(R.id.tv_price_low);
                tv_price_high = (TextView) contentView.findViewById(R.id.tv_price_high);
                tv_filtrate_price.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                iv_filtrate_price.setImageResource(R.drawable.up_green);
                String s = tv_filtrate_price.getText().toString();
                switch (s) {
                    case "默认排序":
                        changeTextColor(tv_price_old, tv_price_low, tv_price_high);
                        break;
                    case "价格从低到高":
                        changeTextColor(tv_price_low, tv_price_old, tv_price_high);
                        break;
                    case "价格从高到低":
                        changeTextColor(tv_price_high, tv_price_old, tv_price_low);
                        break;
                    default:
                        break;
                }
                tv_price_old.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changePrice(tv_price_old, tv_price_low, tv_price_high);
                    }
                });
                tv_price_low.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changePrice(tv_price_low, tv_price_old, tv_price_high);
                    }
                });
                tv_price_high.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changePrice(tv_price_high, tv_price_old, tv_price_low);
                    }
                });
                break;
            case R.id.ll_filtrate_store:
                contentView = LayoutInflater.from(mContext).inflate(
                        R.layout.pop_window_store, null);
                list_pop_left = (ListView) contentView.findViewById(R.id.list_pop_left);
                list_pop_right = (ListView) contentView.findViewById(R.id.list_pop_right);
                if (null != schoolLeftAdapter) {
                    schoolLeftAdapter = null;
                }
                schoolLeftAdapter = new SchoolLeftAdapter(mListLefts);
                list_pop_left.setAdapter(schoolLeftAdapter);
                if (positionOne == 0) {
                    schoolLeftAdapter.setClickPosition(positionOne);
                    schoolLeftAdapter.notifyDataSetChanged();
                }
                if (positionOne != Integer.MAX_VALUE && positionTwo != Integer.MAX_VALUE) {
                    schoolLeftAdapter.setClickPosition(positionOne);
                    schoolLeftAdapter.notifyDataSetChanged();
                    List<SchoolRight> school_list = mListLefts.get(positionOne).getSchool_list();
                    if (null != school_list && school_list.size() > 0) {
                        showRightList(school_list);
                        schoolRightAdapter.setClickPosition(positionTwo);
                        schoolRightAdapter.notifyDataSetChanged();
                    }
                }
                tv_filtrate_store.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                iv_filtrate_store.setImageResource(R.drawable.up_green);
                list_pop_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        positionOne = position;
                        schoolLeftAdapter.setClickPosition(position);
                        SchoolLeft schoolLeft = mListLefts.get(position);
                        if (null != schoolLeft) {
                            List<SchoolRight> school_list = schoolLeft.getSchool_list();
                            if (null != school_list && school_list.size() > 0) {
                                showRightList(school_list);
                            }
                        }
                        schoolLeftAdapter.notifyDataSetChanged();
                        if (position == 0) {
                            positionTwo = Integer.MAX_VALUE;
                            tv_filtrate_store.setText(UIUtils.getString(R.string.all_address));
                            tv_filtrate_store.setTextColor(UIUtils.getColor(R.color.order_black));
                            iv_filtrate_store.setImageResource(R.drawable.down_gray);
                            dist_id = null;
                            sid = null;
                            popupWindow.dismiss();
                            sendHttp("get_course_list", UrlUtils.SERVER_COURSE);
                        } else {
                            dist_id = mListLefts.get(position).getId();
                        }
                    }
                });
                break;
            default:
                break;
        }
        int height = 0;
        switch (view.getId()) {
            case R.id.ll_filtrate_price:
                height = LayoutParams.WRAP_CONTENT;
                break;
            case R.id.ll_filtrate_store:
                height = this.getWindowManager().getDefaultDisplay().getHeight() / 2;
                break;
            default:
                break;
        }
        popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, height, true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        backgroundAlpha(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(false);
                if (tv_filtrate_price.getText().toString().equals(UIUtils.getString(R.string.price_old))) {
                    tv_filtrate_price.setTextColor(UIUtils.getColor(R.color.order_black));
                    iv_filtrate_price.setImageResource(R.drawable.down_gray);
                } else {
                    tv_filtrate_price.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                    iv_filtrate_price.setImageResource(R.drawable.down_green);
                }
                if (tv_filtrate_store.getText().toString().equals(UIUtils.getString(R.string.all_address))) {
                    tv_filtrate_store.setTextColor(UIUtils.getColor(R.color.order_black));
                    iv_filtrate_store.setImageResource(R.drawable.down_gray);
                } else {
                    tv_filtrate_store.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                    iv_filtrate_store.setImageResource(R.drawable.down_green);
                }
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_back_white));
        popupWindow.showAsDropDown(view);
        //==================================================================================
    }

    /**
     * 价格排序选中操作
     *
     * @param greenText   绿色
     * @param grayTextOne 灰色
     * @param grayTextTwo 灰色
     */
    private void changePrice(TextView greenText, TextView grayTextOne, TextView grayTextTwo) {
        changeTextColor(greenText, grayTextOne, grayTextTwo);
        popupWindow.dismiss();
        if (greenText == tv_price_low) {
            distance_flag = false;
            price_flag = true;
            tv_filtrate_price.setText(UIUtils.getString(R.string.price_from_low_to_high));
            list_course_more.setMode(PullToRefreshBase.Mode.BOTH);
            tv_filtrate_price.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            iv_filtrate_price.setImageResource(R.drawable.down_green);
        } else if (greenText == tv_price_high) {
            distance_flag = false;
            price_flag = false;
            tv_filtrate_price.setText(UIUtils.getString(R.string.price_from_high_to_low));
            list_course_more.setMode(PullToRefreshBase.Mode.BOTH);
            tv_filtrate_price.setTextColor(UIUtils.getColor(R.color.app_theme_color));
            iv_filtrate_price.setImageResource(R.drawable.down_green);
        } else {
            distance_flag = true;
            tv_filtrate_price.setText(UIUtils.getString(price_old));
            tv_filtrate_price.setTextColor(UIUtils.getColor(R.color.order_black));
            iv_filtrate_price.setImageResource(R.drawable.down_gray);
        }


        limitStart = 0;
        limitEnd = 5;
        sendHttp("get_course_list", UrlUtils.SERVER_COURSE);
    }

    /**
     * 初始化颜色
     *
     * @param greenText   绿色
     * @param grayTextOne 灰色
     * @param grayTextTwo 灰色
     */
    private void changeTextColor(TextView greenText, TextView grayTextOne, TextView grayTextTwo) {
        greenText.setTextColor(UIUtils.getColor(R.color.app_theme_color));
        grayTextOne.setTextColor(UIUtils.getColor(R.color.order_black));
        grayTextTwo.setTextColor(UIUtils.getColor(R.color.order_black));
    }

    /**
     * 展示右边列表
     *
     * @param mList 右边列表数据
     */
    private void showRightList(final List<SchoolRight> mList) {

        if (null != schoolRightAdapter) {
            schoolRightAdapter = null;
        }
        schoolRightAdapter = new SchoolRightAdapter(mList);
        list_pop_right.setAdapter(schoolRightAdapter);
        list_pop_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sid = mList.get(i).getSid();
                positionTwo = i;
                schoolRightAdapter.setClickPosition(i);
                schoolRightAdapter.notifyDataSetChanged();
                tv_filtrate_store.setText(mList.get(i).getSchool_name());
                tv_filtrate_store.setTextColor(UIUtils.getColor(R.color.app_theme_color));
                iv_filtrate_store.setImageResource(R.drawable.down_green);
                popupWindow.dismiss();
                sendHttp("get_course_list", UrlUtils.SERVER_COURSE);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param flag 透明度
     */
    public void backgroundAlpha(boolean flag) {
        if (flag) {
            fl_back.setVisibility(View.VISIBLE);
        } else {
            fl_back.setVisibility(View.GONE);
        }
    }

}

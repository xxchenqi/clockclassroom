package com.yiju.ClassClockRoom.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yiju.ClassClockRoom.BaseApplication;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.LoginActivity;
import com.yiju.ClassClockRoom.act.Messages_Activity;
import com.yiju.ClassClockRoom.act.MoreStoreActivity;
import com.yiju.ClassClockRoom.act.SplashActivity;
import com.yiju.ClassClockRoom.act.search.SearchActivity;
import com.yiju.ClassClockRoom.adapter.NewIndexAdapter;
import com.yiju.ClassClockRoom.bean.CartStatus;
import com.yiju.ClassClockRoom.bean.ClassRoom;
import com.yiju.ClassClockRoom.bean.ClassRoomData;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.MessageBoxNoRead;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Room;
import com.yiju.ClassClockRoom.bean.TeacherInfoBean;
import com.yiju.ClassClockRoom.bean.result.ClassRoomResult;
import com.yiju.ClassClockRoom.common.DataManager;
import com.yiju.ClassClockRoom.control.map.LocationSingle;
import com.yiju.ClassClockRoom.util.GsonTools;
import com.yiju.ClassClockRoom.util.NetWorkUtils;
import com.yiju.ClassClockRoom.util.PermissionsChecker;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.ClassEvent;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.LoopViewPager;
import com.yiju.ClassClockRoom.widget.wheel.AbstractWheelAdapter;
import com.yiju.ClassClockRoom.widget.wheel.HorizontalWheelView;
import com.yiju.ClassClockRoom.widget.wheel.OnWheelScrollListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 首页
 *
 * @author wh
 */
public class NewIndexFragment extends BaseFragment
        implements OnClickListener {
    /**
     * 是否显示购物车的标志
     */
    public static boolean SHOP_CART_FLAG = false;

    private LinearLayout tab_content_layout;
    private HorizontalWheelView hWalking;
    private LoopViewPager vp_viewpager;
    private TextView tv_megs;
    private RelativeLayout ly_wifi;
    private LinearLayout ll_index;
    private ImageView iv_no_wifi;
    private TextView tv_no_wifi_content1;
    private TextView tv_no_wifi_content2;
    private AnimationDrawable drawable;

    private List<Room> mRoomLists = new ArrayList<>();
    private Order2 info;
    private String uid = "";
    private boolean destroyFlag = false;//标记当前fragment是否destroy
    private boolean isToast;//标志第一次进入app提示打开定位
    private boolean isShowDialog;//标志从第一次进入app首页只显示一次加载图

    public static String sid;
    private NewIndexAdapter newIndexAdapter;

    @Override
    public void initIntent() {
        super.initIntent();
        Bundle bundle = getArguments();
        if (bundle != null) {
            //从编辑页OrderEditDetailActivity回传数据
            info = (Order2) bundle.getSerializable("info");
        }
    }

    @Override
    protected void initView() {
        destroyFlag = false;
        tab_content_layout = (LinearLayout) currentView.findViewById(R.id.tab_content_layout);
        hWalking = (HorizontalWheelView) currentView.findViewById(R.id.hWalking);
        vp_viewpager = (LoopViewPager) currentView.findViewById(R.id.vp_viewpager);
        TextView iv_search = (TextView) currentView.findViewById(R.id.iv_search);
        tv_megs = (TextView) currentView.findViewById(R.id.tv_megs);

        ly_wifi = (RelativeLayout) currentView.findViewById(R.id.ly_wifi);
        Button btn_no_wifi_refresh = (Button) currentView.findViewById(R.id.btn_no_wifi_refresh);
        ll_index = (LinearLayout) currentView.findViewById(R.id.ll_index);
        ImageView loadingImageView = (ImageView) currentView.findViewById(R.id.loadingImageView);
        iv_no_wifi = (ImageView) currentView.findViewById(R.id.iv_no_wifi);
        tv_no_wifi_content1 = (TextView) currentView.findViewById(R.id.tv_no_wifi_content1);
        tv_no_wifi_content2 = (TextView) currentView.findViewById(R.id.tv_no_wifi_content2);
        TextView tv_more_store = (TextView) currentView.findViewById(R.id.tv_more_store);
        drawable = (AnimationDrawable) loadingImageView.getDrawable();

        iv_search.setOnClickListener(this);
        tv_megs.setOnClickListener(this);
        btn_no_wifi_refresh.setOnClickListener(this);
        tv_more_store.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        if (PermissionsChecker.checkPermission(PermissionsChecker.INDEX_PERMISSIONS)) {
            //缺少权限,请求打开权限
            PermissionsChecker.requestPermissions(this, PermissionsChecker.INDEX_PERMISSIONS);
        } else {
            BDLocation bdLocation = LocationSingle.getInstance().getCurrentLocation();
            if (bdLocation != null) {
                handleIndexData();
            } else {
                LocationSingle.getInstance().init(UIUtils.getContext(), !SplashActivity.isLocationInit,
                        new LocationSingle.ILocationRunnable() {
                            @Override
                            public void locationResult(BDLocation bdLocation) {
                                if (bdLocation != null) {
                                    if (newIndexAdapter != null) {
                                        newIndexAdapter.cleanFragment(getChildFragmentManager());
                                    }
                                    //权限已获取
                                    ClassRoomData classRoomData = SharedPreferencesUtils.getClassRoomData();
                                    if (classRoomData == null) {
                                        getHttpUtils();
                                    } else {
                                        handleData(classRoomData);
                                    }
                                }
                            }
                        });
            }
            return;
        }
        handleIndexData();
    }

    public void handleIndexData() {
        if (!DataManager.isRequest) {
            ClassRoomData classRoomData = SharedPreferencesUtils.getClassRoomData();
            if (classRoomData != null) {
                handleData(classRoomData);
            }
            getHttpUtils();
            DataManager.isRequest = true;
            return;
        }
        ClassRoomData classRoomData = SharedPreferencesUtils.getClassRoomData();
        if (classRoomData == null) {
            getHttpUtils();
        } else {
            handleData(classRoomData);
        }

    }

    @Override
    public int setContentViewId() {
        return R.layout.fragment_index_new;
    }

    private void getHttpUtils() {
//        ProgressDialog.getInstance().show();
        if (NetWorkUtils.getNetworkStatus(UIUtils.getContext())) {
            ly_wifi.setVisibility(View.GONE);
            tab_content_layout.setVisibility(View.VISIBLE);

            if (!isShowDialog) {
                drawable.start();
                ll_index.setVisibility(View.VISIBLE);
            }

            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("apiv", UrlUtils.API_VERSION);
            params.addBodyParameter("newpic", "1");//新版读图新加参数
            params.addBodyParameter("is_ios", "1");//传1代表会返回高德坐标
            switch (BaseApplication.FORMAL_ENVIRONMENT) {
                case 1:
                case 2:
                    params.addBodyParameter("is_test", "1");//传1代表会返回多一个测试门店
                    break;
                case 3:
                    break;
            }
            httpUtils.send(HttpMethod.POST, UrlUtils.SERVER_INDEX, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
//                        UIUtils.showToastSafe(R.string.fail_network_request);
                            ly_wifi.setVisibility(View.VISIBLE);
                            tab_content_layout.setVisibility(View.GONE);
                            if (NetWorkUtils.getNetworkStatus(UIUtils.getContext())) {
                                iv_no_wifi.setImageResource(R.drawable.broken);
                                tv_no_wifi_content1.setText(UIUtils.getString(R.string.broken_content1));
                                tv_no_wifi_content2.setText(UIUtils.getString(R.string.broken_content2));
                            } else {
                                iv_no_wifi.setImageResource(R.drawable.none_wifi);
                                tv_no_wifi_content1.setText(UIUtils.getString(R.string.nowifi_content1));
                                tv_no_wifi_content2.setText(UIUtils.getString(R.string.nowifi_content2));
                            }
//                        ProgressDialog.getInstance().dismiss();
                            drawable.stop();
                            ll_index.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            ly_wifi.setVisibility(View.GONE);
                            tab_content_layout.setVisibility(View.VISIBLE);
                            // 处理返回的数据
                            processData(arg0.result);
                        }
                    }
            );
        } else {
            iv_no_wifi.setImageResource(R.drawable.none_wifi);
            tv_no_wifi_content1.setText(UIUtils.getString(R.string.nowifi_content1));
            tv_no_wifi_content2.setText(UIUtils.getString(R.string.nowifi_content2));
            ly_wifi.setVisibility(View.VISIBLE);
            tab_content_layout.setVisibility(View.GONE);
        }
    }

    private void processData(String result) {
        if (StringUtils.isNullString(result)) {
            iv_no_wifi.setImageResource(R.drawable.broken);
            tv_no_wifi_content1.setText(UIUtils.getString(R.string.broken_content1));
            tv_no_wifi_content2.setText(UIUtils.getString(R.string.broken_content2));
            return;
        }
        ClassRoomResult classRoomResult = GsonTools.changeGsonToBean(
                result,
                ClassRoomResult.class
        );
        if (classRoomResult != null && !destroyFlag) {
            if ((classRoomResult.getCode() == 1) && "ok".equals(classRoomResult.getMsg())) {
                ClassRoomData classRoomData = classRoomResult.getData();
                if (classRoomData == null) {
                    return;
                }
                SharedPreferencesUtils.saveClassRoomData(classRoomData);
                handleData(classRoomData);
            }
        }
        if (!isShowDialog) {
            drawable.stop();
            ll_index.setVisibility(View.GONE);
            isShowDialog = true;
        }
    }

    public void handleData(ClassRoomData classRoomData) {
        if (NetWorkUtils.getNetworkStatus(UIUtils.getContext())) {
            ly_wifi.setVisibility(View.GONE);
            tab_content_layout.setVisibility(View.VISIBLE);
        }
        ClassRoom school_list = classRoomData.getSchool_list() != null ? classRoomData.getSchool_list() : null;//学校列表
        List<CourseInfo> course_recommend = classRoomData.getCourse_recommend();//推荐课程
        List<TeacherInfoBean> teacher_recommend = classRoomData.getTeacher_recommend();//推荐老师
        CartStatus cart_status = classRoomData.getCart_status() != null ? classRoomData.getCart_status() : null;//购物车状态
        if (cart_status != null) {
            if (cart_status.getData() == 0) {
                SHOP_CART_FLAG = false;
            } else if (cart_status.getData() == 1) {
                SHOP_CART_FLAG = true;
            }
        }
        List<Room> mLists = school_list != null ? school_list.getData() : null;
        mRoomLists.clear();
        if (mLists != null) {
            mRoomLists.addAll(mLists);
        }
        if (mRoomLists != null && mRoomLists.size() > 0) {
            sortDistanceData(mRoomLists);

            hWalking.setInterpolator(new AnticipateOvershootInterpolator());
            ActAdapter actAdapter = new ActAdapter(mRoomLists);
            hWalking.setViewAdapter(actAdapter);
            hWalking.addScrollingListener(new OnWheelScrollListener() {
                @Override
                public void onScrollingStarted(View wheel) {
                }

                @Override
                public void onScrollingFinished(View wheel) {
                    vp_viewpager.setCurrentItem(hWalking.getCurrentItem(), false);
                }
            });
            newIndexAdapter = new NewIndexAdapter(
                    getChildFragmentManager(),
                    mRoomLists,
                    info,
                    new NewIndexAdapter.ITagPosRunnable() {
                        @Override
                        public void savePos(int position) {
                            if (info != null && position != -1) {
                                vp_viewpager.setCurrentItem(position);
                            }
                        }
                    },
                    course_recommend,
                    teacher_recommend
            );
            vp_viewpager.setAdapter(newIndexAdapter);
            vp_viewpager.setOnPageChangeListener(new LoopViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    hWalking.setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            hWalking.setCurrentItem(vp_viewpager.getCurrentItem());
        }
    }

    @Override
    public void onClick(View v) {
        // 点击事件处理
        switch (v.getId()) {
            case R.id.iv_search://搜索
                Intent intent = new Intent(UIUtils.getContext(),
                        SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_megs://消息
                if (!"-1".equals(uid)) {
                    Intent intentMessage = new Intent(UIUtils.getContext(),
                            Messages_Activity.class);
                    UIUtils.startActivity(intentMessage);
                } else {
                    Intent intentLogin = new Intent(UIUtils.getContext(),
                            LoginActivity.class);
                    UIUtils.startActivity(intentLogin);
                }
                break;
            case R.id.btn_no_wifi_refresh://刷新
                initData();
                break;
            case R.id.tv_more_store://更多门店
                startActivity(new Intent(getActivity(), MoreStoreActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRoomLists.clear();
        try {
            Field childFragmentManager =Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /*
     * 列表数据排序
     */
    private void sortDistanceData(List<Room> rooms) {
        Collections.sort(rooms, new Comparator<Room>() {

            @Override
            public int compare(Room lhs, Room rhs) {
                if (StringUtils.getDistance(lhs.getLat(), lhs.getLng()) >=
                        StringUtils.getDistance(rhs.getLat(), rhs.getLng())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        sid = rooms.get(0).getId();
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_fragment_index);
    }

    @Override
    public void onResume() {
        super.onResume();
        uid = SharedPreferencesUtils.getString(getActivity(),
                getResources().getString(R.string.shared_id), "-1");
        if (NetWorkUtils.getNetworkStatus(UIUtils.getContext()) && !"-1".equals(uid)) {
            getHttpForNoReadMsg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyFlag = true;
    }

    /**
     * 获取未读消息数请求
     */
    public void getHttpForNoReadMsg() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", "message_box_noread");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("username", StringUtils.getUsername());
        params.addBodyParameter("password", StringUtils.getPassword());
        params.addBodyParameter("third_source", StringUtils.getThirdSource());
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlUtils.SERVER_API_COMMON, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 处理返回的数据
                        processDataForNoReadMsg(arg0.result);
                    }
                }
        );
    }

    /**
     * 未读消息接口请求结果处理
     *
     * @param result result
     */
    private void processDataForNoReadMsg(String result) {
        MessageBoxNoRead messageBoxNoread = GsonTools.changeGsonToBean(result, MessageBoxNoRead.class);
        if (messageBoxNoread != null) {
            if ("1".equals(messageBoxNoread.getCode())) {
                if (Integer.parseInt(messageBoxNoread.getNoread_count()) == 0) {
                    Drawable drawable = UIUtils.getDrawable(R.drawable.message);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv_megs.setCompoundDrawables(drawable, null, null, null);
                    }
                } else {
                    Drawable drawable = UIUtils.getDrawable(R.drawable.message_coming);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv_megs.setCompoundDrawables(drawable, null, null, null);
                    }
                }
            }
        }
    }

    /**
     * push接收到消息事件处理
     *
     * @param event event
     */
    @Override
    public void onRefreshEvent(ClassEvent<Object> event) {
        super.onRefreshEvent(event);
        if (event.getType() == DataManager.Index_No_Read_Data) {
            int noReadCount = (int) event.getData();
            if (noReadCount == 0) {
                Drawable drawable = UIUtils.getDrawable(R.drawable.message);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_megs.setCompoundDrawables(drawable, null, null, null);
                }
            } else {
                Drawable drawable = UIUtils.getDrawable(R.drawable.message_coming);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_megs.setCompoundDrawables(drawable, null, null, null);
                }
            }
        }
    }

    public class ActAdapter extends AbstractWheelAdapter {
        private List<Room> mLists = new ArrayList<>();

        public ActAdapter(List<Room> mRoomLists) {
            this.mLists = mRoomLists;
        }

        public void setmLists(List<Room> mLists) {
            this.mLists = mLists;
        }

        @Override
        public int getItemsCount() {
            return mLists.size();
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            ActViewHolder holder;
            if (convertView != null) {
                holder = (ActViewHolder) convertView.getTag();
            } else {
                convertView = View.inflate(UIUtils.getContext(), R.layout.item_index_tab_text, null);
                holder = new ActViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
                convertView.setTag(holder);
            }
            if (hWalking.getCurrentItem() == index) {
                if ("1".equals(mLists.get(index).getSchool_type())) {
                    Drawable drawable = UIUtils.getDrawable(R.drawable.ziying);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        holder.textView.setCompoundDrawables(drawable, null, null, null);
                        holder.textView.setCompoundDrawablePadding(12);
                    }
                }
                holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                holder.textView.setTextColor(UIUtils.getColor(R.color.color_green_1e));
            } else {
                if ("1".equals(mLists.get(index).getSchool_type())) {
                    Drawable drawable = UIUtils.getDrawable(R.drawable.ziying_gray);
                    if (drawable != null) {
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        holder.textView.setCompoundDrawables(drawable, null, null, null);
                        holder.textView.setCompoundDrawablePadding(12);
                    }
                }
                holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                holder.textView.setTextColor(UIUtils.getColor(R.color.color_gay_99));
            }
            holder.textView.setText(mLists.get(index).getName());
            return convertView;
        }

        public class ActViewHolder {
            public TextView textView;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionsChecker.REQUEST_EXTERNAL_STORAGE
                && PermissionsChecker.hasAllPermissionsGranted(grantResults)) {
            if (permissions[0].equals(PermissionsChecker.LOCATION_PERMISSIONS[0])
                    && permissions[1].equals(PermissionsChecker.LOCATION_PERMISSIONS[1])) {
                isToast = true;
                LocationSingle.getInstance().init(UIUtils.getContext(), SplashActivity.isLocationInit,
                        new LocationSingle.ILocationRunnable() {
                            @Override
                            public void locationResult(BDLocation bdLocation) {
                                if (bdLocation != null) {
                                    if (newIndexAdapter != null) {
                                        newIndexAdapter.cleanFragment(getChildFragmentManager());
                                    }
                                    //权限已获取
                                    ClassRoomData classRoomData = SharedPreferencesUtils.getClassRoomData();
                                    if (classRoomData == null) {
                                        getHttpUtils();
                                    } else {
                                        handleData(classRoomData);
                                    }
                                }
                            }
                        }
                );
            }
        } else {
            if (!isToast) {
                UIUtils.showToastSafe(fatherActivity.getString(R.string.toast_open_location));
                isToast = true;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}


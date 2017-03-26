package com.yiju.ClassClockRoom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.IndexDetailActivity;
import com.yiju.ClassClockRoom.act.common.Common_Show_WebPage_Activity;
import com.yiju.ClassClockRoom.adapter.NewIndexTypeAdapter;
import com.yiju.ClassClockRoom.bean.AdData;
import com.yiju.ClassClockRoom.bean.CourseInfo;
import com.yiju.ClassClockRoom.bean.Order2;
import com.yiju.ClassClockRoom.bean.Room;
import com.yiju.ClassClockRoom.bean.RoomTypeInfo;
import com.yiju.ClassClockRoom.bean.TeacherInfoBean;
import com.yiju.ClassClockRoom.common.constant.WebConstant;
import com.yiju.ClassClockRoom.control.map.NavigationUtils;
import com.yiju.ClassClockRoom.util.StringUtils;
import com.yiju.ClassClockRoom.util.UIUtils;
import com.yiju.ClassClockRoom.util.net.UrlUtils;
import com.yiju.ClassClockRoom.widget.StoreImageView;
import com.yiju.ClassClockRoom.widget.animator.AnimatorBuilder;
import com.yiju.ClassClockRoom.widget.animator.HeaderStickyAnimator;
import com.yiju.ClassClockRoom.widget.animator.StikkyHeaderBuilder;

import java.util.List;

/*
 * 首页view pager单页fragment
 * Created by wh on 2016/3/3.
 */
public class StoreFragment extends Fragment
        implements View.OnClickListener {

    private View rootView;
    private ListView lv_types;
    private StoreImageView siv_image;
    private TextView tv_top_name;
    private TextView tv_map;
    private TextView tv_address;
    private TextView tv_address_top;
    private TextView tv_type_top;
    private TextView tv_type_text;

    private Room room;
    private List<CourseInfo> course_recommend;//推荐课程
    private List<TeacherInfoBean> teacher_recommend;//推荐老师
    private AdData adData;
    private Order2 info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            room = (Room) getArguments().getSerializable("Room");//门店数据
            course_recommend = (List<CourseInfo>) getArguments().getSerializable("course_recommend");
            teacher_recommend = (List<TeacherInfoBean>) getArguments().getSerializable("teacher_recommend");
            adData = (AdData) getArguments().getSerializable("adData");//广告数据
            //从编辑页OrderEditDetailActivity回传的数据
            info = (Order2) getArguments().getSerializable("info");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_store, container, false);
            initMyView();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMyData();
    }


    private void initMyView() {
        if (rootView == null) {
            return;
        }
        tv_map = (TextView) rootView.findViewById(R.id.tv_map);
        LinearLayout ll_imgs = (LinearLayout) rootView.findViewById(R.id.ll_imgs);
        lv_types = (ListView) rootView.findViewById(R.id.lv_types);
        siv_image = (StoreImageView) rootView.findViewById(R.id.siv_image);
        tv_top_name = (TextView) rootView.findViewById(R.id.tv_top_name);
        tv_address = (TextView) rootView.findViewById(R.id.tv_address);
        tv_address_top = (TextView) rootView.findViewById(R.id.tv_address_top);
        tv_type_top = (TextView) rootView.findViewById(R.id.tv_type_top);
        tv_type_text = (TextView) rootView.findViewById(R.id.tv_type_text);

        StikkyHeaderBuilder.stickTo(lv_types)
                .setHeader(R.id.fl_header, rootView)
                .minHeightHeaderDim(R.dimen.DIMEN_140DP)
                .animator(new HeaderScrollToScaleAnimator())
                .build();

        ll_imgs.setOnClickListener(this); // 图集点击事件
        tv_map.setOnClickListener(this);// 位置
        siv_image.setOnClickListener(this);//门店大图点击事件
    }

    private void initMyData() {
        if (room == null) {
            return;
        }

        tv_top_name.setText(room.getName());
        siv_image.setImgResource(room.getPic_big());
        /*room.getDistanceStr(new IDistanceRunnable() {
            @Override
            public void distanceRunnable(String distance) {
                tv_map.setText(distance);
            }
        });*/

        tv_map.setText(StringUtils.getDistanceStr(room.getLat(), room.getLng()));
        tv_address.setText(room.getAddress());
        tv_address_top.setText(room.getAddress());
        tv_type_top.setText(room.getTags());
        tv_type_text.setText(room.getTags());


        // 预留广告位
        if (adData != null) {
            View mPlaceHolderView = View.inflate(UIUtils.getContext(), R.layout.view_store_header, null);
            ImageView iv_ad = (ImageView) mPlaceHolderView.findViewById(R.id.iv_ad);
            String picSmall = adData.getPic_url();
            if (StringUtils.isNotNullString(picSmall)) {
                Glide.with(UIUtils.getContext()).load(picSmall).into(iv_ad);
            }
            iv_ad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(adData.getUrl())) {
                        return;
                    }
                    int pageValue;
                    if (!"0".equals(adData.getTeacher_id())) {
                        //跳转个人老师资料详情
                        pageValue = WebConstant.WEB_Int_TeachInfo_Page;
                    } else {
                        //跳转活动页
                        pageValue = WebConstant.WEB_Int_Action_Page;
                    }
                    gotoWebPage(
                            adData.getUrl(),
                            adData.getName(),
                            pageValue,
                            adData.getTeacher_id()
                    );
                }
            });
            lv_types.addHeaderView(mPlaceHolderView);
        }

        lv_types.setAdapter(
                new NewIndexTypeAdapter(
                        getContext(),
                        room,
                        info,
                        course_recommend,
                        teacher_recommend
                )
        );
        // 已选择标识居中显示
        if (info != null) {
            List<RoomTypeInfo> room_types = room.getRoom_type();
            if (room_types != null && room_types.size() > 0) {
                for (int i = 0; i < room_types.size(); i++) {
                    RoomTypeInfo roomTypeInfo = room_types.get(i);
                    String id = roomTypeInfo.getId();
                    if (info.getType_id().equals(id)) {
                        int index = lv_types.getFirstVisiblePosition();
                        if (i >= index) {
                            View v = lv_types.getChildAt(i - index);
                            int top = (v == null) ? 0 : v.getTop();
                            lv_types.setSelectionFromTop(i, top);
                            break;
                        }
                    }
                }
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_imgs:
                if (room == null) {
                    return;
                }
                if (room.getRoom_type() == null || room.getRoom_type().size() <= 0) {
                    return;
                }
                String url_1 = UrlUtils.SERVER_WEB_PICDES
                        + "sid=" + room.getId()
//                        + "&typeid=" + room.getRoom_type().get(0).getId()
                        + "&piclist=piclist";
                String title_1 = room.getName() + UIUtils.getString(R.string.pic);
                int pageValue_1 = WebConstant.Picdes_Page;
                gotoWebPage(url_1, title_1, pageValue_1, "");
                break;
            case R.id.tv_map:
                if (room == null) {
                    return;
                }
                boolean isHaveBaidu = NavigationUtils.isInstallByread("com.baidu.BaiduMap");
                boolean isHaveGaoDe = NavigationUtils.isInstallByread("com.autonavi.minimap");
                String haveMap;
                if (!isHaveBaidu && !isHaveGaoDe) {
                    haveMap = "0";
                } else {
                    haveMap = "1";
                }
                String url = UrlUtils.SERVER_WEB_TO_CLASSDETAIL
                        + "trapmap=trapmap&title=" + room.getName()
                        + "&to=" + room.getLng_bd() + "," + room.getLat_bd()
                        + "&to_g=" + room.getLng() + "," + room.getLat()
                        + "&address=" + room.getAddress() + "&havemap=" + haveMap;
                String title = room.getName();
                int pageValue = WebConstant.WEB_Int_Map_Page;

                gotoWebPage(url, title, pageValue, "");
                break;
            case R.id.siv_image:
                if (room == null) {
                    return;
                }
                Intent intent = new Intent(UIUtils.getContext(),
                        IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sid", room.getId());
                bundle.putString("name", room.getName());
                bundle.putString("address", room.getAddress());
                bundle.putString("tags", room.getTags());
                bundle.putString("school_type", room.getSchool_type());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到导航页h5
     */
    private void gotoWebPage(String url, String title, int pageValue,
                             String tid) {
        Intent intent = new Intent(
                UIUtils.getContext(),
                Common_Show_WebPage_Activity.class
        );
        intent.putExtra(
                Common_Show_WebPage_Activity.Param_String_Title,
                title
        );
        intent.putExtra(
                UIUtils.getString(R.string.redirect_open_url),
                url
        );
        intent.putExtra(
                UIUtils.getString(R.string.get_page_name),
                pageValue
        );
        intent.putExtra(
                UIUtils.getString(R.string.redirect_tid),
                tid
        );
        startActivity(intent);
    }

    /**
     * 门店大图伸缩动画
     */
    private class HeaderScrollToScaleAnimator extends HeaderStickyAnimator {
        @Override
        public AnimatorBuilder getAnimatorBuilder() {
            View mHeader_image = getHeader().findViewById(R.id.siv_image);
            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }

}

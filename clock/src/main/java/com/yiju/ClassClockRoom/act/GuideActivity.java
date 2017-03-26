package com.yiju.ClassClockRoom.act;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.act.base.BaseActivity;
import com.yiju.ClassClockRoom.adapter.GuidePagerAdapter;
import com.yiju.ClassClockRoom.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements OnClickListener {

    @ViewInject(R.id.vp_guide)
    private ViewPager vp_guide;
    @ViewInject(R.id.tv_skip)
    private TextView tv_skip;
    @ViewInject(R.id.tv_experience)
    private TextView tv_experience;
    @ViewInject(R.id.rg_guide)
    private RadioGroup rg_guide;
    //数据源
    private List<ImageView> imageViews;
    private GuidePagerAdapter adapter;

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        SharedPreferencesUtils.saveInt(this, "firstLogin", 1);
        imageViews = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imageView.setImageResource(R.drawable.guid_1);
            } else if (i == 1) {
                imageView.setImageResource(R.drawable.guid_2);
            } else if (i == 2) {
                imageView.setImageResource(R.drawable.guid_3);
            } else {
                imageView.setImageResource(R.drawable.guid_4);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
        }
        adapter = new GuidePagerAdapter(imageViews);
        vp_guide.setAdapter(adapter);
        ((RadioButton) rg_guide.getChildAt(0)).setChecked(true);
    }

    @Override
    public void initListener() {
        super.initListener();
        tv_skip.setOnClickListener(this);
        tv_experience.setOnClickListener(this);
        vp_guide.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            //页面滑动监听
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (tv_skip.getVisibility() == View.VISIBLE && position == 2) {
                    tv_experience.setVisibility(View.VISIBLE);
                    tv_skip.setAlpha(1.0f - (positionOffset > 1.0f ? 1.0f : positionOffset));
                    tv_experience.setAlpha(positionOffset > 1.0f ? 1.0f : positionOffset);
                } else if (tv_skip.getVisibility() == View.GONE && position == 2) {
                    tv_skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) rg_guide.getChildAt(position)).setChecked(true);
                if (position == 3) {
                    rg_guide.setVisibility(View.GONE);
                    tv_skip.setVisibility(View.GONE);
                    tv_experience.setVisibility(View.VISIBLE);
                    tv_skip.setClickable(false);
                    tv_experience.setClickable(true);

                } else {
                    rg_guide.setVisibility(View.VISIBLE);
                    tv_skip.setVisibility(View.VISIBLE);
                    tv_experience.setVisibility(View.GONE);
                    tv_skip.setClickable(true);
                    tv_experience.setClickable(false);
                }
            }
        });
    }

    @Override
    public String getPageName() {
        return getString(R.string.title_act_index_guide);
    }

    @Override
    public int setContentViewId() {
        return R.layout.activity_guide;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_skip:
            case R.id.tv_experience:
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

}

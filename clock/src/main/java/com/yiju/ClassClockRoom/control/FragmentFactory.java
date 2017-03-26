package com.yiju.ClassClockRoom.control;

import android.util.SparseArray;

import com.yiju.ClassClockRoom.fragment.AccompanyReadFragment;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.fragment.CourseFragment;
import com.yiju.ClassClockRoom.fragment.ExperienceClassFragment;
import com.yiju.ClassClockRoom.fragment.IndexFragment;
import com.yiju.ClassClockRoom.fragment.PersonalCenterFragment;
import com.yiju.ClassClockRoom.fragment.TeacherFragment;
import com.yiju.ClassClockRoom.fragment.ThemeTemplateFragment;

/**
 * 用来创建fragment
 */
public class FragmentFactory {

    // 首页
    public static final int TAB_INDEX = 0;
    //主题首页
    public static final int TAB_THEME = 1;
    //体验课首页
    public static final int TAB_EXPERIENCE = 2;
    // 我的
    public static final int TAB_MY = 3;
    // 陪读
    public static final int TAB_VIDEO = 4;
    // 课程
    public static final int TAB_COURSE = 5;
    // 发现老师
    public static final int TAB_TEACHER = 6;
    // 用来缓存当前的hashMap
    private SparseArray<BaseFragment> mFragmentsHashMap = new SparseArray<>();

    public BaseFragment createFragment(int position) {
        // 从内存获取到fragment
        BaseFragment mBaseFragment = mFragmentsHashMap.get(position);

        if (mBaseFragment == null) {
            switch (position) {
                case TAB_INDEX://0
                    mBaseFragment = new IndexFragment();
                    break;
                case TAB_THEME://1
                    mBaseFragment = new ThemeTemplateFragment();
                    break;
                case TAB_EXPERIENCE://2
                    mBaseFragment = new ExperienceClassFragment();
                    break;
                case TAB_MY://3
                    mBaseFragment = new PersonalCenterFragment();
                    break;
                case TAB_VIDEO://4
                    mBaseFragment = new AccompanyReadFragment();
                    break;
                case TAB_COURSE://5
                    mBaseFragment = new CourseFragment();
                    break;
                case TAB_TEACHER://6
                    mBaseFragment = new TeacherFragment();
                    break;
            }
            //缓存fragment
            mFragmentsHashMap.put(position, mBaseFragment);
        }

        return mBaseFragment;
    }
}

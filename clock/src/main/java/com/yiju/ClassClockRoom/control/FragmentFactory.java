package com.yiju.ClassClockRoom.control;

import android.util.SparseArray;

import com.yiju.ClassClockRoom.fragment.AccompanyReadFragment;
import com.yiju.ClassClockRoom.fragment.BaseFragment;
import com.yiju.ClassClockRoom.fragment.CourseFragment;
import com.yiju.ClassClockRoom.fragment.NewIndexFragment;
import com.yiju.ClassClockRoom.fragment.PersonalCenterFragment;
import com.yiju.ClassClockRoom.fragment.TeacherFragment;

/**
 * 用来创建fragment
 */
public class FragmentFactory {

    // 首页
    public static final int TAB_INDEX = 0;
    // 陪读
    public static final int TAB_VIDEO = 4;
    // 课程
    public static final int TAB_COURSE = 1;
    // 发现
    public static final int TAB_TEACHER = 2;
    // 我的
    public static final int TAB_MY = 3;
    // 用来缓存当前的hashMap
    private static SparseArray<BaseFragment> mFragmentsHashMap = new SparseArray<>();

    public static BaseFragment createFragment(int position) {
        // 从内存获取到fragment
        BaseFragment mBaseFragment = mFragmentsHashMap.get(position);

        if (mBaseFragment == null) {
            switch (position) {
                case TAB_INDEX:
                    mBaseFragment = new NewIndexFragment();
                    break;
                case TAB_VIDEO:
                    mBaseFragment = new AccompanyReadFragment();
                    break;
                case TAB_COURSE:
                    mBaseFragment = new CourseFragment();
                    break;
                case TAB_TEACHER:
                    mBaseFragment = new TeacherFragment();
                    break;
                case TAB_MY:
                    mBaseFragment = new PersonalCenterFragment();
                    break;
            }
            //缓存fragment
            mFragmentsHashMap.put(position, mBaseFragment);
        }

        return mBaseFragment;
    }
}

package com.yiju.ClassClockRoom.control;

import android.app.Activity;
import android.content.Intent;

import com.yiju.ClassClockRoom.act.MainActivity;

import java.util.Stack;

/**
 * ----------------------------------------
 * 注释:activity控制管理类
 * <p>
 * 作者: cq
 * <p>
 * 时间: on 2016/11/8 11:05
 * ----------------------------------------
 */
public class ActivityControlManager {
    private static ActivityControlManager instance;
    private Stack<Activity> activityStack;

    private ActivityControlManager() {
    }

    public static ActivityControlManager getInstance() {
        if (instance == null) {
            instance = new ActivityControlManager();
        }
        return instance;
    }

    /**
     * 把一个activity压入栈中
     *
     * @param activity act
     */
    public void pushOneActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取栈顶的activity，先进后出原则
     *
     * @return act
     */
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * 移除一个activity
     *
     * @param activity act
     */
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
//                activity = null;
            }
        }
    }

    /**
     * 退出所有activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }

    /**
     * 检查栈里是否有首页(MainActivity)
     *
     * @return true 有 false无
     */
    public boolean checkMainActivity() {
        boolean flag = false;
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i) instanceof MainActivity) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 关闭当前页，打开首页
     *
     * @param activity act
     * @param page     0首页,1课程首页,2老师首页,3个人中心
     */
    public void finishCurrentAndOpenHome(Activity activity, int page) {
        if (checkMainActivity()) {//如果首页存在直接finish
            activity.finish();
        } else {
            if (activityStack.size() > 1) {
                activity.finish();
            } else {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra(MainActivity.Param_Start_Fragment, page);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }


    /**
     * 关闭所有页，打开首页
     *
     * @param activity act
     * @param page 0首页,1课程首页,2老师首页,3个人中心
     */
    public void finishAllAndOpenHome(Activity activity, int page) {
        if (!checkMainActivity()) {
            //首页不存在关闭所有页面在开启首页
            finishAllActivity();
        }
        //首页存在直接开启首页(singleTask)
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MainActivity.Param_Start_Fragment, page);
        activity.startActivity(intent);
    }


    /**
     * 关闭当前页面打开其他页面
     *
     * @param activity act
     * @param clz      指向页面
     */
    public void finishCurrentAndOpenOther(Activity activity, Class clz) {
        activity.finish();
        //首页存在直接开启首页(singleTask)
        Intent intent = new Intent(activity, clz);
        activity.startActivity(intent);
    }
    /**
     * 关闭当前页面打开其他页面
     *
     * @param activity act
     * @param intent i
     */
    public void finishCurrentAndOpenOther(Activity activity, Intent intent) {
        activity.finish();
        //首页存在直接开启首页(singleTask)
        activity.startActivity(intent);
    }


    /**
     * 关闭所有页面打开其他页面
     *
     * @param activity act
     * @param clz      指向页面
     */
    public void finishAllAndOpenOther(Activity activity, Class clz) {
        if (!checkMainActivity()) {
            //首页不存在关闭所有页面在开启首页
            finishAllActivity();
        }
        //首页存在直接开启首页(singleTask)
        Intent intent = new Intent(activity, clz);
        activity.startActivity(intent);
    }


    /**
     * 关闭所有页面打开其他页面
     *
     * @param activity act
     * @param intent i
     */
    public void finishAllAndOpenOther(Activity activity, Intent intent) {
        if (!checkMainActivity()) {
            //首页不存在关闭所有页面在开启首页
            finishAllActivity();
        }
        //首页存在直接开启首页(singleTask)
        activity.startActivity(intent);
    }

    /**
     * 关闭除首页的其他页面
     */
    public void finishNoMainActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity instanceof MainActivity) continue;
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }

}

package com.focustech.android.commonlibs;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

/**
 * @version :V1.0
 * @描述 ：activity堆栈管理类
 * @user ：yanguozhu
 * @date 创建时间 ： 2016/3/4
 */
public class ActivityManager {

    /**
     * 实例
     */
    private static ActivityManager instance;

    /**
     * 单例
     */
    public static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public static String currentActivityName = "";

    /**
     * activity列表
     */
    private Stack<Activity> activityStack = new Stack<Activity>();

    /**
     * 获取activity堆栈
     *
     * @return stack
     */
    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 移除Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 退出应用：遍历所有Activity并finish
     */
    public void exit() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
        System.exit(0);
    }

    /**
     * 遍历所有Activity并finish
     */
    public void removeAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }

    /**
     * 移除一个activity
     *
     * @param activity
     */
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }
        }
    }

    /**
     * 移除指定activity之上的所有activity
     *
     * @param activityName
     */
    public void popToActivity(String activityName) {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                if (activity.getClass().getName().equals(activityName)) break;
                popOneActivity(activity);
            }
        }
    }

    /**
     * 移除指定toActivityName之上的所有activity excActivityName：除了该页面
     *
     * @param toActivityName
     * @param excActivityName
     */
    @SuppressWarnings("rawtypes")
    public void popToActivityExcept(String toActivityName, String excActivityName) {
        if (activityStack != null) {
            Stack<Activity> newStack = new Stack<Activity>();
            int size = activityStack.size();
            for (int i = size - 1; i >= 0; i--) {
                newStack.add(activityStack.get(i));
            }
            Iterator iterator = newStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = (Activity) iterator.next();
                if (activity == null) break;
                if (activity.getClass().getName().equals(toActivityName)) break;
                if (activity.getClass().getName().equals(excActivityName)) continue;
                activity.finish();
                iterator.remove();
                newStack.remove(activity);
                activityStack.remove(activity);
            }
        }
    }

    /**
     * popExitActivity:pop指定的activity
     *
     * @param activityName className
     */
    public void popExitActivity(String activityName) {
        for (Activity activity : activityStack) {
            if (activity.getClass().getName().equals(activityName)) {
                activity.finish();
                activityStack.remove(activity);
                break;
            }
        }
    }

    /**
     * 获取栈顶的activity，先进后出原则
     *
     * @return
     */
    public Activity getLastActivity() {
        if (null != activityStack && activityStack.size() > 0) {
            return activityStack.lastElement();
        }
        return null;
    }


    /**
     * 获得指定的activity
     *
     * @param activityName
     * @return
     */
    public synchronized Activity getExitActivity(String activityName) {
        //是否存在指定的Activity
        Activity mActivity = null;
        for (Activity activity : activityStack) {
            if (activity.getClass().getName().equals(activityName)) {
                mActivity = activity;
                break;
            }
        }
        if (null == mActivity) {
            mActivity = activityStack.lastElement();
        }
        return mActivity;
    }

    private boolean isHasLoading = false;

    public void setHasLoading(boolean b) {
        isHasLoading = b;
    }

    /**
     * app被内存回收之后，恢复的时候，从加载页开始
     */
//    public static void restartApp(Activity activity) {
//        Intent intent = new Intent();
//        intent.setClass(activity, LoadingActivity.class);
//        activity.startActivity(intent);
//        activity.finish();
//    }
}

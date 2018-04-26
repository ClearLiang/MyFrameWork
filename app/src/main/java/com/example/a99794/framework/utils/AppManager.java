package com.example.a99794.framework.utils;

import android.app.Activity;

import java.util.Stack;

/**
 *@作者 ClearLiang
 *@日期 2018/4/13
 *@描述 App管理类 管理所有的Activity
 **/

public class AppManager {
    //单例
    public static Stack<Activity> sActivityStack;
    public static AppManager sAppManager;

    private AppManager() {}

    /**
     *@函数名 -getAppManager
     *@功能   -获取AppManager单例
     *@参数   -无
     *@返回值 -无
     **/
    public static AppManager getAppManager() {
        if (sAppManager == null) {
            synchronized (AppManager.class) {
                sAppManager = new AppManager();
            }
        }
        return sAppManager;
    }

    /**
     *@函数名 -addActivity
     *@功能   -添加Activity到堆栈
     *@参数   -activity 需要添加的Activity
     *@返回值 -无
     **/
    public void addActivity(Activity activity) {
        if (sActivityStack == null) {
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    /**
     *@函数名 -getCurrentActivity
     *@功能   -获取当前Activity
     *@参数   -无
     *@返回值 -无
     **/
    public Activity getCurrentActivity() {
        Activity activity = sActivityStack.lastElement();
        return activity;
    }

    /**
     *@函数名 -finishCurrentActivity
     *@功能   -结束当前Activity
     *@参数   -无
     *@返回值 -无
     **/
    public void finishCurrentActivity() {
        Activity activity = sActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     *@函数名 -finishActivity
     *@功能   -结束指定的Activity
     *@参数   -activity 需要结束的Activity
     *@返回值 -无
     **/
    public void finishActivity(Activity activity) {
        if (activity != null) {
            sActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     *@函数名 -finishActivity
     *@功能   -结束指定类名的Activity
     *@参数   -classActivity 需要结束的Activity的雷类名
     *@返回值 -无
     **/
    public void finishActivity(Class<?> classActivity) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(classActivity)) {
                finishActivity(activity);
            }
        }
    }

    /**
     *@函数名 -finishAllActivity
     *@功能   -结束所有Activity
     *@参数   -无
     *@返回值 -无
     **/
    public void finishAllActivity() {
        for (int i = 0; i < sActivityStack.size(); i++) {
            if (null != sActivityStack.get(i)) {
                sActivityStack.get(i).finish();
            }
        }
    }
}

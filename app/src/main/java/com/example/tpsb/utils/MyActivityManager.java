package com.example.tpsb.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

public class MyActivityManager {
    // Activity栈
    private static Stack<Activity> activityStack;
    // 单例模式
    private static MyActivityManager instance;

    private MyActivityManager() {
    }

    /**
     * 单一实例
     */
    public static MyActivityManager getInstance() {
        if (instance == null) {
            instance = new MyActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            //根据进程ID，杀死该进程
            //android.os.Process.killProcess(android.os.Process.myPid());
            //退出真个应用程序
            //System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

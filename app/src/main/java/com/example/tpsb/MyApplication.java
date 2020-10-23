package com.example.tpsb;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

public class MyApplication extends Application {
    private static Context context;

    //Application的创建，第一个创建，比Activity中的onCreate方法调用还要早
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    //监听系统变化的方法，如屏幕旋转，语言更改
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //回收内存
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    public static Context getContext(){
        return context;
    }
}

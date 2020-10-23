package com.example.tpsb.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.tpsb.BuildConfig;
import com.example.tpsb.MyApplication;


public class LogUtil {
    private static boolean islog = BuildConfig.ISLOG;
    /**
     * 截断输出日志
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0){
            return;
        }

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (islog){
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                Log.e(tag, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    Log.e(tag, logContent);
                }
                Log.e(tag, msg);// 打印剩余日志
            }

        }else {
            Log.e("The log has been ，", "intercepted without permission");
        }

    }


    public static void uiMsg(String msg){
        Toast.makeText(MyApplication.getContext(), msg,Toast.LENGTH_SHORT).show();
    }
}

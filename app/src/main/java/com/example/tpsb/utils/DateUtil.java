package com.example.tpsb.utils;

import com.example.tpsb.MainActivity;
import com.example.tpsb.MyApplication;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关操作类
 */
public class DateUtil {

    private static Gson gson = new Gson();

    @SuppressWarnings("hiding")
    public static <T> T parseJson(String response, Class<T> clazz) {
        try {
            return gson.fromJson(response, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("实体对象解析错误",e.toString());
            return null;
        }
    }

    public static String initCity() {
        StringBuilder stringBuilder = null;
        try {
            InputStream is = MyApplication.getContext().getClass().getClassLoader().
                    getResourceAsStream("assets/"+"cityInfo.json");
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader  = new BufferedReader(streamReader);
            String line;
            stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            reader.close();
            reader.close();
            is.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static String getDateFormatString(Date date) {
        String dateString = null;
        if (null != date) {
            SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");
            dateString = format.format(date);
        }

        return dateString;
    }

    public String getTime(){
        long time= System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

    public String getTime(String str){
        long time= System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat(str);
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

}

package com.husky.library.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author husky
 */
public class TimeUtils {

    public static final String TIME = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String TIME_SIMPLE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy年MM月dd日";
    public static final String BIRTHDAY_SIMPLE = "yyyy.MM.dd";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm";

    public static long getTimeStamp(String s,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        long time = 0;
        try {
            time = format.parse(s).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getTimeStr(long time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm",Locale.getDefault());
        return format.format(new Date(time));
    }

    public static String getDate(long time,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern,Locale.getDefault());
        return format.format(new Date(time));
    }

    /**
     * 转换时间
     * 与当前时间比较
     * @param time
     * @return
     */
    public static String getCompairedTime(long time){
        String mTime;
        long now = System.currentTimeMillis();
        long between = now - time;
        long day = between/(24*60*60*1000);
        long hour = (between/(60*60*1000)-day*24);
        long min = ((between/(60*1000))-day*24*60-hour*60);
        long s = (between/1000-day*24*60*60-hour*60*60-min*60);
        if (day == 0){
            if (hour == 0){
                if(min == 0){
                    mTime = s + "秒前";
                }else {
                    mTime = min + "分前";
                }
            }else {
                mTime = hour + "小时前";
            }
        }else if (day == 1){
            mTime = day + "天前";
        }else {
            Date date = new Date(time);
            SimpleDateFormat df = new SimpleDateFormat("MM月dd日", Locale.getDefault());
            mTime = df.format(date);
        }
        return mTime;
    }
}

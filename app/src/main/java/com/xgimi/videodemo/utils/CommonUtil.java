package com.xgimi.videodemo.utils;

import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by yub on 2017/6/9.
 */

public class CommonUtil {
    /**
     * 将uri转化为UTF-8
     * @param path
     * @return
     */
    public static String encodeUTF8(String path) {
        String utf_8_str = null;
        String s = null;
        if (path != null && path.length() > 0) {
            s = Uri.decode(path);
            utf_8_str = s.substring(path.lastIndexOf("/")+1, s.length());
        }
        return utf_8_str;
    }
    /**
     * will millisecond number formatting 00:00.
     *
     * @ param duration time value to seconds for the unit. @ return format for
     * 00:00 forms of string.
     */
    public static String formatDuration(int duration) {
        int time = duration / 1000;
        if (time == 0 && duration > 0) {
            time = 1;
        }

        int second = time % 60;
        int min = time / 60;
        long hour = 0;
        if (min > 60) {
            hour = time / 3600;
            min = time / 60 % 60;
        }
        if (hour > 0)
            return String.format("%02d:%02d:%02d", hour, min, second);
        else
            return String.format("00:%02d:%02d", min, second);
    }

    public static String getSystemTime() {
        Calendar c = Calendar.getInstance();
        String time = "";
        int h = c.get(Calendar.HOUR_OF_DAY);
        time = h + ":";
        int m = c.get(Calendar.MINUTE);
        time = m < 10 ?time +  "0" + m + ":" : time +  m + ":";
        int s = c.get(Calendar.SECOND);
        time = s < 10 ? time + "0" + s : time +s ;
        return time;
    }

}

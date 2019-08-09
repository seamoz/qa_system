package com.ps.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;

public class checkData {

    public static Boolean check(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = 0;
        long endTime = 0;
        try {
            startTime = sdf.parse(time1).getTime();
            endTime = sdf.parse(time2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nowTime = System.currentTimeMillis();

        if(nowTime > startTime && nowTime < endTime){
            return true;
        }
        return false;
    }
}

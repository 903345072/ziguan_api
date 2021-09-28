package com.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class Holiday {
    public static HashMap<Integer, List> map = new HashMap<Integer, List>() {
        {
            put(1, Arrays.asList(1,2,3,9,10,16,17,23,24,30,31));
            put(2, Arrays.asList(6,11,12,13,14,15,16,17,21,27,28));
            put(3, Arrays.asList(6,7,13,14,20,21,27,28));
            put(4, Arrays.asList(3,4,5,10,11,17,18,24));
            put(5, Arrays.asList(1,2,3,4,5,9,15,16,22,23,29,30));
            put(6, Arrays.asList(5,6,12,13,14,19,20,26,27));
            put(7, Arrays.asList(3,4,10,11,17,18,24,25,31));
            put(8, Arrays.asList(1,7,8,14,15,21,22,28,29));
            put(9, Arrays.asList(4,5,11,12,19,20,21,25,26));
            put(10, Arrays.asList(1,2,3,4,5,6,7,10,16,17,23,24,30,31));
            put(11, Arrays.asList(6,7,13,14,20,21,27,28));
            put(12, Arrays.asList(4,5,11,12,18,19,25,26));
        }
    };

    public static boolean is_trade_day(Calendar c){
        SimpleDateFormat sdf = new SimpleDateFormat("M-d");
        String tomorrow_day = sdf.format(c.getTime()); //7-1
        String[] split = tomorrow_day.split("-");
        Integer month_ = Integer.parseInt(split[0]);
        Integer day_ = Integer.parseInt(split[1]);
        List lists = Holiday.map.get(month_);
        if(!lists.contains(day_)){
            return true;
        }else{
            return false;
        }
    }

    public static  Date findEndTime(Integer limit_day){
        SimpleDateFormat formatter_  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY,14);
        c1.set(Calendar.MINUTE,50);
        c1.set(Calendar.SECOND,0);
        int i1 = new Date().compareTo(c1.getTime());
        if(limit_day == 1 && i1 == -1){
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY,15);
            c2.set(Calendar.MINUTE,0);
            c2.set(Calendar.SECOND,0);
            return c2.getTime();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,1);
        int i = 0;
        while (true){
            if(!is_trade_day(c)){ //如果不是交易日

            }else{
                i++;
            }
            if(i == limit_day){
                break;
            }
            c.add(Calendar.DATE,1);
        }

        c.set(Calendar.HOUR_OF_DAY,15);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
       return c.getTime();
    }
}

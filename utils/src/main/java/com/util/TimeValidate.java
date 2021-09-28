package com.util;

import java.text.ParseException;
import java.util.Calendar;

public class TimeValidate {
    public static boolean compareIsOpening() throws ParseException {

        Calendar calendar = Calendar.getInstance();
//        if ((calendar.get(Calendar.HOUR_OF_DAY) <= 9 &&  calendar.get(Calendar.MINUTE) < 30)
//                || (calendar.get(Calendar.HOUR_OF_DAY) == 11 &&  calendar.get(Calendar.MINUTE) > 30)
//                || (calendar.get(Calendar.HOUR_OF_DAY) == 12 )
//                || ( calendar.get(Calendar.HOUR_OF_DAY) >= 15) &&  calendar.get(Calendar.MINUTE) > 01)
//        {
//            return false;
//        }else {
//            return true;
//        }
        if(calendar.get(Calendar.HOUR_OF_DAY) >= 15){
            return false;
        }
        return true;
    }

    public static boolean compareIsOpeningAll() throws ParseException {

        Calendar calendar = Calendar.getInstance();
        if ((calendar.get(Calendar.HOUR_OF_DAY) <= 9 &&  calendar.get(Calendar.MINUTE) < 30)
                || (calendar.get(Calendar.HOUR_OF_DAY) == 11 &&  calendar.get(Calendar.MINUTE) > 30)
                || (calendar.get(Calendar.HOUR_OF_DAY) == 12 )
                || ( calendar.get(Calendar.HOUR_OF_DAY) >= 15) &&  calendar.get(Calendar.MINUTE) > 1)
        {
            return false;
        }else {
            return true;
        }


    }
}

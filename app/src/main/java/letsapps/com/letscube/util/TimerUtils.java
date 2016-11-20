package letsapps.com.letscube.util;


import android.content.Context;
import android.util.Log;

import letsapps.com.letscube.R;
import letsapps.com.letscube.singleton.TimerSettings;

public class TimerUtils {

    public static int TIME_NO_VALUE = 0;
    public static int TIME_DNF = -1;
    public static int TIME_2_PENALITY_VALUE = 2000;

    public static String TIME_NO_VALUE_TEXT = "--.--";
    public static String TIME_DNF_TEXT = "DNF";


    // return mm:ss.ccc
    // and ss.ccc if mm < 01
    // and m:ss.ccc if mm < 10
    public static String formatTime(long timeInMillis){

        if(timeInMillis == TIME_DNF){
            return TIME_DNF_TEXT;
        }
        if(timeInMillis <= TIME_NO_VALUE){
            return "0.000";
        }

        String formattedDate = "";

        int nbMilli = 0;
        int nbSeconds;
        int nbMinutes;

        double timeTemp;

        //to obtain s,dcm
        timeTemp = (double)timeInMillis / 1000; //62500 -> 62.5 | 62005 -> 62.005

        //to obtain dcm
        final String timeStr = String.valueOf(timeTemp); //62.5 -> "62.5" | 62.005 -> "62.005"
        String timeMilliStr = timeStr.split("\\.")[1]; //"62.5" -> "5" | "62.005" -> "005"

        while(timeMilliStr.length() < 3){
            timeMilliStr += "0";
        } //"5" -> "500" | "005" -> "005"

        nbMilli = Integer.parseInt(timeMilliStr);


        //to obtain sec
        timeTemp -= (nbMilli / 1000); //62.5 -> 62

        //to obtain nbSec
        nbSeconds = (int)timeTemp % 60; //62 -> 2

        //to obtain nbMin
        nbMinutes = ((int)timeTemp - nbSeconds) / 60; //62 -> 1

        if(nbMinutes > 0) {
            formattedDate = String.valueOf(nbMinutes);
            formattedDate += ":";
        }

        if(nbSeconds < 10 && nbMinutes > 0){
            formattedDate += "0" + nbSeconds;
        }else{
            formattedDate += nbSeconds;
        }

        formattedDate += ".";

        if(nbMilli < 10){
            formattedDate += "00" + nbMilli;
        }else if(nbMilli < 100){
            formattedDate += "0" + nbMilli;
        }else{
            formattedDate += String.valueOf(nbMilli);
        }

        return formattedDate;
    }

    public static String formatTime(int timeMs, boolean isAuthorizing0, int penalty, int format){
        String formattedTime = null;

        if(penalty == 0){
            penalty = DatabaseTime.NO_PENALTY;
        }

        if(penalty == DatabaseTime.PENALTY_DNF){
            return TIME_DNF_TEXT;
        }

        if(timeMs <= 0 && !isAuthorizing0){
            return TIME_NO_VALUE_TEXT;

        }else{
            timeMs = Math.max(timeMs, 0);
            if(penalty == DatabaseTime.NO_PENALTY){
                formattedTime = formatTime(timeMs);
            }else if(penalty == DatabaseTime.PENALTY_2){
                formattedTime = formatTime(timeMs + 2000);
            }
            final int formattedTimeLength = formattedTime.length();
            switch(format){
                case TimerSettings.TIMER_UPDATE_SECOND :
                    return formattedTime.substring(0, formattedTimeLength - 4);//to hide the point too
                case TimerSettings.TIMER_UPDATE_TENTH :
                    return formattedTime.substring(0, formattedTimeLength - 2);
                case TimerSettings.TIMER_UPDATE_HUNDREDTH:
                    return formattedTime.substring(0, formattedTimeLength - 1);
                default :
                    return formattedTime;
            }
        }
    }

    public static String formatTime(DatabaseTime time, boolean isAuthorizing0, int format){
        return formatTime(time.getTime(), isAuthorizing0, time.getPenalty(), format);
    }

    public static String formatTime(int timeMs, int format){
        return formatTime(timeMs, true, 0, format);
    }

    public static int roundTo5secBelow(int timeInMs){
        return timeInMs - (timeInMs % 5000);
    }

    public static int roundTo5secAbove(int timeInMs){
        return timeInMs + (5000 - (timeInMs % 5000));

    }
}
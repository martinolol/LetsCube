package letsapps.com.letscube.util;

import java.util.Calendar;

public class Date {

    public static final String getDate(){
        String formattedDate = "";

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1; // start from 0
        final int day = calendar.get(Calendar.DAY_OF_MONTH); // start from 1

        if(day < 10){
            formattedDate += "0";
        }
        formattedDate += (day + "/");


        if(month < 10){
            formattedDate += "0";
        }

        return formattedDate + (month + "/" + year);
    }
}

package letsapps.com.letscube.util;

import java.util.ArrayList;

public class ListUtil {

    public static int getMinValue(TimeList times){

        final int timeSize = times.size();

        int minValue = times.get(0).getTime();
        for(int i = 1; i < timeSize; i++){
            if(times.get(i).getTime() < minValue){
                minValue = times.get(i).getTime();
            }
        }

        return minValue;
    }

    public static int getMaxValue(TimeList times){

        final int timeSize = times.size();

                int maxValue = times.get(0).getTime();
                for(int i = 1; i < timeSize; i++){
                    if(times.get(i).getTime() > maxValue){
                maxValue = times.get(i).getTime();
            }
        }

        return maxValue;
    }

    public static int getNbTimeInInterval(TimeList times, int min, int max){

        int nbTimesInInterval = 0;

        for(int i = 0; i < times.size(); i++){
            if(times.get(i).getTime() >= min && times.get(i).getTime() < max){
                nbTimesInInterval++;
            }
        }

        return nbTimesInInterval;
    }
}

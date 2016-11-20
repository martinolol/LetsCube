package letsapps.com.letscube.util;

import java.util.ArrayList;
import java.util.List;

public class AverageCalculator {

    public static int calculateLastAverage(
            TimeList timeList, int numberOfSolvesToCount){

        if(timeList == null){
            return TimerUtils.TIME_NO_VALUE;
        }
        int[] lastTimes = new int[numberOfSolvesToCount];

        final int timeListSize = timeList.size();
        int nbDNF = 0;
        int currentWorstTime = 0;

        if(timeListSize < numberOfSolvesToCount){
            return TimerUtils.TIME_NO_VALUE;
        }

        for(int i = 0; i < numberOfSolvesToCount; i++){
            final DatabaseTime currentTime = timeList.get((timeListSize - 1) - i);
            lastTimes[i] = currentTime.getTimeAccordingToPenalty();
            if(lastTimes[i] == TimerUtils.TIME_DNF){
                nbDNF++;
                currentWorstTime = TimerUtils.TIME_DNF;
                if(nbDNF >= 2){
                    return TimerUtils.TIME_DNF;
                }
            }
        }

        int currentBestTime = lastTimes[0];
        if(currentWorstTime != TimerUtils.TIME_DNF){
            currentWorstTime = lastTimes[0];
        }

        int sum = 0;

        for(int i = 0; i < numberOfSolvesToCount; i++) {
            sum += lastTimes[i];
            if (lastTimes[i] < currentBestTime && lastTimes[i] != TimerUtils.TIME_DNF) {
                currentBestTime = lastTimes[i];
            } else if (lastTimes[i] > currentWorstTime && currentWorstTime != TimerUtils.TIME_DNF){
                currentWorstTime = lastTimes[i];
            }

        }

        sum -= (currentBestTime + currentWorstTime);

        return sum / (numberOfSolvesToCount - 2);
    }

    public static int calculateLastMean(TimeList timeList,
                                        int numberOfSolvesToCount){

        if(timeList == null || numberOfSolvesToCount <= 0){
            return TimerUtils.TIME_NO_VALUE;
        }

        int[] lastTimes = new int[numberOfSolvesToCount];

        final int timeListSize = timeList.size();

        if(timeListSize < numberOfSolvesToCount){
            return TimerUtils.TIME_NO_VALUE;
        }

        for(int i = 0; i < numberOfSolvesToCount; i++){
            lastTimes[i] = timeList.get((timeListSize - 1) - i).getTime();
            if(timeList.get((timeListSize - 1) - i).getPenalty() == DatabaseTime.PENALTY_2){
                lastTimes[i] += 2000;
            }else if(timeList.get((timeListSize - 1) - i).getPenalty() == DatabaseTime.PENALTY_DNF){
                return TimerUtils.TIME_DNF;
            }
        }

        int sum = 0;

        for(int i = 0; i < numberOfSolvesToCount; i++) {
            sum += lastTimes[i];
        }

        return sum / numberOfSolvesToCount;
    }

    public static int calculateFirstMean(TimeList timeList,
                                        int numberOfSolvesToCount){

        if(timeList == null || numberOfSolvesToCount <= 0){
            return TimerUtils.TIME_NO_VALUE;
        }

        int[] firstTimes = new int[numberOfSolvesToCount];

        final int timeListSize = timeList.size();

        if(timeListSize < numberOfSolvesToCount){
            return TimerUtils.TIME_NO_VALUE;
        }

        for(int i = 0; i < numberOfSolvesToCount; i++){
            firstTimes[i] = timeList.get(i).getTime();
            if(timeList.get((timeListSize - 1) - i).getPenalty() == DatabaseTime.PENALTY_2){
                firstTimes[i] += 2000;
            }else if(timeList.get((timeListSize - 1) - i).getPenalty() == DatabaseTime.PENALTY_DNF){
                return TimerUtils.TIME_DNF;
            }
        }

        int sum = 0;

        for(int i = 0; i < numberOfSolvesToCount; i++) {
            sum += firstTimes[i];
        }

        return sum / numberOfSolvesToCount;
    }

    public static final int calculateTotalMeanWithoutDNF(TimeList times){

        if(times == null){
            return TimerUtils.TIME_NO_VALUE;
        }
        if(times.size() == 0){
            return TimerUtils.TIME_NO_VALUE;
        }

        int[] lastTimes = new int[times.size()];

        final int timeListSize = lastTimes.length;

        for(int i = 0; i < timeListSize; i++){
            lastTimes[i] = times.get((timeListSize - 1) - i).getTime();
            if(times.get((timeListSize - 1) - i).getPenalty() == DatabaseTime.PENALTY_2){
                lastTimes[i] += 2000;
            }
        }

        int sum = 0;

        for(int i = 0; i < timeListSize; i++) {
            sum += lastTimes[i];
        }

        return sum / timeListSize;
    }

    public static int calculateMean(DatabaseTime[] times){

        if(times == null){
            return TimerUtils.TIME_NO_VALUE;
        }

        int sum = 0;
        //Log.d("AC (calculateMean)", "sum = " + sum);
        for(int i = 0; i < times.length; i++){
            sum += times[i].getTime();
            if(times[i].getPenalty() == DatabaseTime.PENALTY_2){
                sum += 2000;
            }else if(times[i].getPenalty() == DatabaseTime.PENALTY_DNF){
                return TimerUtils.TIME_DNF;
            }
            //Log.d("AC (calculateMean)", "currentTime = " + times[i].toString());
            //Log.d("AC (calculateMean)", "sum = " + sum);
        }

        return sum / times.length;
    }

    public static int calculateMean(List<DatabaseTime> times){

        if(times == null){
            return TimerUtils.TIME_NO_VALUE;
        }

        int sum = 0;
        //Log.d("AC (calculateMean)", "sum = " + sum);
        for(int i = 0; i < times.size(); i++){
            sum += times.get(i).getTime();
            if(times.get(i).getPenalty() == DatabaseTime.PENALTY_2){
                sum += 2000;
            }else if(times.get(i).getPenalty() == DatabaseTime.PENALTY_DNF){
                return TimerUtils.TIME_DNF;
            }
            //Log.d("AC (calculateMean)", "currentTime = " + times[i].toString());
            //Log.d("AC (calculateMean)", "sum = " + sum);
        }

        return sum / times.size();
    }

    public static int calculateMean(int[] times){
        if(times.length == 0) return TimerUtils.TIME_NO_VALUE;
        int sum = 0;
        final int nbTimes = times.length;
        for(int i = 0; i < nbTimes; i++){
            sum += times[i];
        }
        return sum / nbTimes;
    }

    public static int calculateAverage(DatabaseTime[] times){
        if(times == null){
            return TimerUtils.TIME_NO_VALUE;
        }

        int currentBestTime = times[0].getTime();
        int currentWorstTime = times[0].getTime();
        int nbDNF = 0;

        int sum = 0;

        //Log.d("AC", "----------timesLength : " + times.length);

        for(int i = 0; i < times.length; i++) {
            final int currentTimeAccordingToPenalty = times[i].getTimeAccordingToPenalty();
            sum += currentTimeAccordingToPenalty;
            if(currentTimeAccordingToPenalty == TimerUtils.TIME_DNF){
                nbDNF++;
                currentWorstTime = TimerUtils.TIME_DNF;
                if(nbDNF >= 2){
                    return TimerUtils.TIME_DNF;
                }
            }

            if (currentTimeAccordingToPenalty < currentBestTime &&
                    currentTimeAccordingToPenalty != TimerUtils.TIME_DNF) {
                currentBestTime = currentTimeAccordingToPenalty;
            } else if (currentTimeAccordingToPenalty > currentWorstTime &&
                    currentWorstTime != TimerUtils.TIME_DNF) {
                currentWorstTime = currentTimeAccordingToPenalty;
            }


            //Log.d("AC", "currentBestTime (" + i + ") : " + currentBestTime);
            //Log.d("AC", "currentWorstTime (" + i + ") : " + currentWorstTime);
            //Log.d("AC", "lastTimes[" + i + "] : " + currentTimeAccordingToPenalty);
        }

        //Log.d("AC", "----------nbDNF : " + nbDNF);

        sum -= (currentBestTime + currentWorstTime);

        return sum / (times.length - 2);
    }

    public static int calculateAverage(List<DatabaseTime> times){
        if(times == null){
            return TimerUtils.TIME_NO_VALUE;
        }

        int currentBestTime = times.get(0).getTime();
        int currentWorstTime = times.get(0).getTime();
        int nbDNF = 0;

        int sum = 0;

        //Log.d("AC", "----------timesLength : " + times.length);

        for(int i = 0; i < times.size(); i++) {
            final int currentTimeAccordingToPenalty = times.get(i).getTimeAccordingToPenalty();
            sum += currentTimeAccordingToPenalty;
            if(currentTimeAccordingToPenalty == TimerUtils.TIME_DNF){
                nbDNF++;
                currentWorstTime = TimerUtils.TIME_DNF;
                if(nbDNF >= 2){
                    return TimerUtils.TIME_DNF;
                }
            }

            if (currentTimeAccordingToPenalty < currentBestTime &&
                    currentTimeAccordingToPenalty != TimerUtils.TIME_DNF) {
                currentBestTime = currentTimeAccordingToPenalty;
            } else if (currentTimeAccordingToPenalty > currentWorstTime &&
                    currentWorstTime != TimerUtils.TIME_DNF) {
                currentWorstTime = currentTimeAccordingToPenalty;
            }


            //Log.d("AC", "currentBestTime (" + i + ") : " + currentBestTime);
            //Log.d("AC", "currentWorstTime (" + i + ") : " + currentWorstTime);
            //Log.d("AC", "lastTimes[" + i + "] : " + currentTimeAccordingToPenalty);
        }

        //Log.d("AC", "----------nbDNF : " + nbDNF);

        sum -= (currentBestTime + currentWorstTime);

        return sum / (times.size() - 2);
    }
}
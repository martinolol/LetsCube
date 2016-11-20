package letsapps.com.letscube.util;

public class TimerStats {
    int eventId;
    int nbTimes;
    float totalAverage;
    double standardDeviation;

    public TimerStats(){

    }

    public TimerStats(int eventId){
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getNbTimes() {
        return nbTimes;
    }

    public void setNbTimes(int nbTimes) {
        this.nbTimes = nbTimes;
    }

    public float getTotalAverage() {
        return totalAverage;
    }

    public void setTotalAverage(float totalAverage) {
        this.totalAverage = totalAverage;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
}

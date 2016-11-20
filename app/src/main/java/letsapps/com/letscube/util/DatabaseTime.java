package letsapps.com.letscube.util;

import android.util.Log;

import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.Event;

public class DatabaseTime {

    public static final int NO_PENALTY = 0x0; // CONSTANTES A NE PAS MODIFIER
    public static final int PENALTY_2 = 0x1;  // CONSTANTES A NE PAS MODIFIER
    public static final int PENALTY_DNF = 0x2;// CONSTANTES A NE PAS MODIFIER

    int id, sessionId, time, time2, mo3, avg5, avg12, mo50, mo100, penalty;
    Event event;
    Session session;
    String scramble, comment, date;

    public DatabaseTime() {
        penalty = NO_PENALTY;
    }

    public DatabaseTime(Event event) {
        this.event = event;
        penalty = NO_PENALTY;
    }

    public DatabaseTime(int time, Event event) {
        this.time = time;
        this.event = event;
        penalty = NO_PENALTY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public int getTimeAccordingToPenalty() {
        switch (penalty){
            case NO_PENALTY : return time;
            case PENALTY_2 : return time + TimerUtils.TIME_2_PENALITY_VALUE;
            case PENALTY_DNF : return TimerUtils.TIME_DNF;
        }
        return time;
    }

    public int getTimeAccordingToPenaltyWithoutDNF() {
        switch (penalty){
            case NO_PENALTY : return time;
            case PENALTY_2 : return time + TimerUtils.TIME_2_PENALITY_VALUE;
            case PENALTY_DNF : return time;
        }
        return time;
    }

    public int getTimeAccordingToPenaltyWithout2() {
        switch (penalty){
            case NO_PENALTY : return time;
            case PENALTY_2 : return time;
            case PENALTY_DNF : return TimerUtils.TIME_DNF;
        }
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTime(long time) {
        this.time = (int)time;
    }

    public int getTime2() {
        return time2;
    }

    public void setTime2(int time2) {
        this.time2 = time2;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getMo3() {
        return mo3;
    }

    public void setMo3(int mo3) {
        this.mo3 = mo3;
    }

    public int getAvg5() {
        return avg5;
    }

    public void setAvg5(int avg5) {
        this.avg5 = avg5;
    }

    public int getAvg12() {
        return avg12;
    }

    public void setAvg12(int avg12) {
        this.avg12 = avg12;
    }

    public int getMo50() {
        return mo50;
    }

    public void setMo50(int mo50) {
        this.mo50 = mo50;
    }

    public int getMo100() {
        return mo100;
    }

    public void setMo100(int mo100) {
        this.mo100 = mo100;
    }

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getMean(int meanLength){
        switch (meanLength){
            case 3 : return mo3;
            case 50 : return mo50;
            case 100 : return mo100;
            default : return 0;
        }
    }

    public void setMean(int meanLength, int meanValue){
        switch (meanLength){
            case 3 : setMo3(meanValue); break;
            case 50 : setMo50(meanValue); break;
            case 100 : setMo100(meanValue); break;
        }
    }

    public void setAverage(int avgLength, int avgValue){
        switch (avgLength){
            case 5 : setAvg5(avgValue); break;
            case 12 : setAvg12(avgValue); break;
        }
    }

    public void print(){
        Log.d("DBTime (print)", toString());
    }

    @Override
    public String toString(){
        return "id(" + id + ") event:" + (event == null ? "null" : event.getName()) +
                " | time:" + time + " | time2:" + time2 + " | mo3:" + mo3 +
                " | avg5:" + avg5 + " | avg12:" + avg12 + " | mo50:" + mo50 +
                " | mo100:" + mo100 + " | date:" + date + " | penalty:" + penalty +
                " | scramble:" + scramble;
    }
}

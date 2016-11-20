package letsapps.com.letscube.util.cube;

import java.util.ArrayList;
import java.util.List;

import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.AverageCalculator;

public class CubeRecord {

    public static final int TYPE_SINGLE = 0x1;
    public static final int TYPE_MO3 = 0x2;
    public static final int TYPE_AVG5 = 0x3;
    public static final int TYPE_AVG12 = 0x4;
    public static final int TYPE_MO50 = 0x5;
    public static final int TYPE_MO100 = 0x6;

    Event event;
    DatabaseTime single;
    DatabaseTime[] mo3;
    DatabaseTime[] avg5;
    DatabaseTime[] avg12;
    DatabaseTime[] mo50;
    DatabaseTime[] mo100;

    public CubeRecord(){

    }

    public CubeRecord(Event event){
        this.event = event;
    }

    public static String getName(int recordTypeId){
        switch(recordTypeId){
            case TYPE_SINGLE : return "Single";
            case TYPE_MO3 : return "Mo3";
            case TYPE_AVG5 : return "Avg5";
            case TYPE_AVG12 : return "Avg12";
            case TYPE_MO50 : return "Mo50";
            case TYPE_MO100 : return "Mo100";
            default : return null;
        }
    }

    public Event getEventId() {
        return event;
    }

    public void setEventId(Event eventId) {
        this.event = event;
    }

    public DatabaseTime getSingle() {
        return single;
    }

    public void setSingle(DatabaseTime single) {
        this.single = single;
    }

    public DatabaseTime[] getMo3() {
        return mo3;
    }

    public int getMo3Value() {
        return AverageCalculator.calculateMean(mo3);
    }

    public void setMo3(DatabaseTime[] mo3) {
        this.mo3 = mo3;
    }

    public void setMo3(List<DatabaseTime> times){
        final int nbTimes = times.size();
        DatabaseTime[] mo3calculated = new DatabaseTime[3];
        for(int i = 0; i < 3; i++){
            mo3calculated[i] = times.get(nbTimes - 3 + i);
        }
        setMo3(mo3calculated);
    }

    public DatabaseTime[] getAvg5() {
        return avg5;
    }

    public int getAvg5Value() {
        return AverageCalculator.calculateAverage(avg5);
    }

    public void setAvg5(DatabaseTime[] avg5) {
        this.avg5 = avg5;
    }

    public void setAvg5(List<DatabaseTime> times){

        final int nbTimes = times.size();
        DatabaseTime[] avg5calculated = new DatabaseTime[5];
        for(int i = 0; i < 5; i++){
            avg5calculated[i] = times.get(nbTimes - 5 + i);
        }
        setAvg5(avg5calculated);
    }

    public DatabaseTime[] getAvg12() {
        return avg12;
    }

    public int getAvg12Value() {
        return AverageCalculator.calculateAverage(avg12);
    }


    public void setAvg12(DatabaseTime[] avg12) {
        this.avg12 = avg12;
    }

    public void setAvg12(List<DatabaseTime> times){

        final int nbTimes = times.size();
        DatabaseTime[] avg12calculated = new DatabaseTime[12];
        for(int i = 0; i < 12; i++){
            avg12calculated[i] = times.get(nbTimes - 12 + i);
        }
        setAvg12(avg12calculated);
    }

    public DatabaseTime[] getMo50() {
        return mo50;
    }

    public int getMo50Value() {
        return AverageCalculator.calculateMean(mo50);
    }

    public void setMo50(DatabaseTime[] mo50) {
        this.mo50 = mo50;
    }

    public void setMo50(List<DatabaseTime> times){
        final int nbTimes = times.size();
        DatabaseTime[] mo50calculated = new DatabaseTime[50];
        for(int i = 0; i < 50; i++){
            mo50calculated[i] = times.get(nbTimes - 50 + i);
        }
        setMo50(mo50calculated);
    }

    public DatabaseTime[] getMo100() {
        return mo100;
    }

    public int getMo100Value() {
        return AverageCalculator.calculateMean(mo100);
    }

    public void setMo100(DatabaseTime[] mo100) {
        this.mo100 = mo100;
    }

    public void setMo100(List<DatabaseTime> times){
        final int nbTimes = times.size();
        DatabaseTime[] mo100calculated = new DatabaseTime[100];
        for(int i = 0; i < 100; i++){
            mo100calculated[i] = times.get(nbTimes - 100 + i);
        }
        setMo100(mo100calculated);
    }
}

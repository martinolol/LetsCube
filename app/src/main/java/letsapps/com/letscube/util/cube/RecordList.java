package letsapps.com.letscube.util.cube;

public class RecordList {

    // pour remplir par défaut la table des records.
    public static final int RECORD_NO_VALUE = -2; // != -1 qui représente DNF!

    int eventId;
    int singleRecord, mo3Record, avg5Record, avg12Record, mo50Record, mo100Record = RECORD_NO_VALUE;

    public RecordList(int eventId){
        this.eventId = eventId;
    }

    public RecordList(){
        setAll(RECORD_NO_VALUE);
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    public int getSingle() {
        return singleRecord;
    }

    public void setSingle(int singleRecord) {
        this.singleRecord = singleRecord;
    }

    public int getMo3() {
        return mo3Record;
    }

    public void setMo3(int mo3Record) {
        this.mo3Record = mo3Record;
    }

    public int getAvg5() {
        return avg5Record;
    }

    public void setAvg5(int avg5Record) {
        this.avg5Record = avg5Record;
    }

    public int getAvg12() {
        return avg12Record;
    }

    public void setAvg12(int avg12Record) {
        this.avg12Record = avg12Record;
    }

    public int getMo50() {
        return mo50Record;
    }

    public void setMo50(int mo50Record) {
        this.mo50Record = mo50Record;
    }

    public int getMo100() {
        return mo100Record;
    }

    public void setMo100(int mo100Record) {
        this.mo100Record = mo100Record;
    }

    public void setAll(int value){
        this.singleRecord = value;
        this.mo3Record = value;
        this.avg5Record = value;
        this.avg12Record = value;
        this.mo50Record = value;
        this.mo100Record = value;
    }
}

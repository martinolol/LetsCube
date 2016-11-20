package letsapps.com.letscube.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import letsapps.com.letscube.database.DatabaseStatsHandler;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.util.cube.RecordList;

public class TimeList {

    LinkedHashMap<Integer, ArrayList<DatabaseTime>> times;
    ArrayList<Session> sessions;
    LinkedHashMap<Integer, RecordList> records;
    LinkedHashMap<Integer, TimerStats> stats;

    Event event;

    public TimeList(){
        times = new LinkedHashMap<>();
        records = new LinkedHashMap<>();
        stats = new LinkedHashMap<>();
        sessions = new ArrayList<>();
    }

    public int nbEvents(){
        return times.size();
    }

    public Set<Integer> getAllEvents(){
        return times.keySet();
    }

    public void setEvent(Event event){
        this.event = event;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public Session getLastSession(){
        if(size() > 0) {
            return get(size() - 1).getSession();
        }
        return null;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    public int size(){
        if(times.get(event.getId()) != null){
            return times.get(event.getId()).size();
        }
        return 0;
    }

    public DatabaseTime get(int position){
        if(times.get(event.getId()) != null) {
            return times.get(event.getId()).get(position);
        }else{
            return null;
        }
    }

    public ArrayList<DatabaseTime> getCurrentList(){
        return times.get(event.getId());
    }

    public int indexOf(DatabaseTime time){
        return times.get(event.getId()).indexOf(time);
    }

    public List<DatabaseTime> getLastValues(int nbValues){
        return times.get(event.getId()).subList(size() - nbValues, size());
    }

    public List<DatabaseTime> subList(int start, int end){
        return times.get(event.getId()).subList(start, end);
    }

    public void set(int position, DatabaseTime newValue){
        times.get(event.getId()).set(position, newValue);
    }

    public void add(DatabaseTime time){
        if(times.get(time.getEvent().getId()) == null) {
            times.put(time.getEvent().getId(), new ArrayList<DatabaseTime>());
        }
        times.get(time.getEvent().getId()).add(time);
    }

    public void remove(int position){
        times.get(event.getId()).remove(position);
    }

    public void removeAll(){
        times.remove(event.getId());
    }

    public RecordList getRecord() {
        return records.get(event.getId());
    }

    public void setRecord(RecordList record) {
        Log.d("DTHinst", "TL start setting record");
        if(records.get(record.getEventId()) == null) {
            Log.d("DTHinst", "TL creating recordList");
            records.put(record.getEventId(), new RecordList());
        }
        Log.d("DTHinst", "TL putting recordList");
        records.put(event.getId(), record);
    }

    public TimerStats getStats() {
        return stats.get(event.getId());
    }

    public void setStats(TimerStats stats) {
        this.stats.put(event.getId(), stats);
    }
}

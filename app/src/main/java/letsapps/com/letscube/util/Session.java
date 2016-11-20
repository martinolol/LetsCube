package letsapps.com.letscube.util;

import java.util.ArrayList;

public class Session {
    ArrayList<DatabaseTime> times;
    int id;
    int eventId;
    String startDate;
    String endDate;
    String name;
    String comment;

    public Session(){
        times = new ArrayList<DatabaseTime>();
    }

    public ArrayList<DatabaseTime> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<DatabaseTime> times) {
        this.times = times;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

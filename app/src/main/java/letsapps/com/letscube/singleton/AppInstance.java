package letsapps.com.letscube.singleton;

import android.util.Log;

import letsapps.com.letscube.util.TimeList;
import letsapps.com.letscube.util.cube.Event;

public class AppInstance {

    TimeList times;
    Event event;

    public static AppInstance instance = null;

    private AppInstance(){};

    public static synchronized AppInstance getInstance(){
        if(instance == null){
            instance = new AppInstance();
        }
        return instance;
    }

    public TimeList getTimes() {
        if(times == null){
            times = new TimeList();
        }
        times.setEvent(this.event);
        return times;
    }

    public TimeList getTimes(Event timesEvent) {
        if(times == null){
            times = new TimeList();
        }
        times.setEvent(timesEvent);
        return times;
    }

    public void setTimes(TimeList times) {
        this.times = times;
    }

    // return the currentEvent.
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}

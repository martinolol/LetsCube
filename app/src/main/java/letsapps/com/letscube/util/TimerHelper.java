package letsapps.com.letscube.util;

import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import letsapps.com.letscube.singleton.TimerSettings;

/**
 * Created by marti on 26/08/2016.
 */
public class TimerHelper {

    TimerListener timerListener;
    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler();
    long systemNanoTimeAtStart, timeInMs;
    boolean isInCourse;

    public TimerHelper(TimerListener timerListener){
        isInCourse = false;
        timeInMs = 0;

        this.timerListener = timerListener;
    }

    private void initTimerTask(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        timeInMs = (System.nanoTime() - systemNanoTimeAtStart) / 1_000_000;
                        timerListener.onSchedule(timeInMs);
                    }
                });
            }
        };
    }

    public void start(int intervalMs){
        startFrom(0, intervalMs);
    }

    public void startFrom(int startMs, int intervalMs){
        Log.i("TimerHelper", "starting timer");
        if(isInCourse){
            Log.e("TimerHelper", "timer already launched");
            return;
        }
        isInCourse = true;
        systemNanoTimeAtStart = System.nanoTime() - (startMs * 1_000_000);
        initTimerTask();
        timer = new Timer();
        timer.schedule(timerTask, 0, intervalMs);
    }

    public long stop(){
        Log.i("TimerHelper", "stopping timer");
        if(!isInCourse){
            Log.e("TimerHelper", "timer already stopped");
            return 0;
        }
        isInCourse = false;
        timeInMs = 0;
        timer.cancel();
        timer = null;
        return (System.nanoTime() - systemNanoTimeAtStart) / 1_000_000;
    }

    public boolean isInCourse(){
        return isInCourse;
    }

    public int getTimeMs(){
        return (int)((System.nanoTime() - systemNanoTimeAtStart) / 1_000_000);
    }

    public interface TimerListener {
        void onSchedule(long elapsedTimeMs);
    }
}

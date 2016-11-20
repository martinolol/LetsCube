package letsapps.com.letscube.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import letsapps.com.letscube.R;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.TimerUtils;

public class LoadingTextView extends TextView {

    Timer timer;
    TimerTask timerTask;
    Handler timerHandler = new Handler();

    int nbPoints = 1;

    public LoadingTextView(Context context) {
        super(context);
        init();
    }

    public LoadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 500, 500);
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                timerHandler.post(new Runnable() {
                    public void run() {
                        nbPoints++;
                        nbPoints %= 4;
                        String text = "";
                        for (int i = 0; i < nbPoints; i++) {
                            text += ".";
                        }
                        setText(text);
                    }
                });
            }
        };
    }
}

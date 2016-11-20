package letsapps.com.letscube.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.TimerHelper;
import letsapps.com.letscube.util.TimerUtils;

/**
 * Created by marti on 26/08/2016.
 */
public class TimerOverlayLayout extends RelativeLayout {

    public static final int NO_TIMER = 0x0;
    public static final int TIMER_INSPECTION = 0x1;
    public static final int TIMER_SOLVE = 0x2;

    private int timeMsState;
    private int timerTypeState;

    boolean isShowing;

    TextView timeTV, textTV;
    HoleAnimationImageView holeIV;

    Animation apearAnimation, disapearAnimation;

    int timerUpdateIntervalSetting, timerUpdateIntervalInMs;

    TimerHelper inspectionTimerHelper, solveTimerHelper;

    public TimerOverlayLayout(Context context) {
        super(context);
        init();
    }

    public TimerOverlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerOverlayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.view_timer_overlay, this);

        timeTV = (TextView) rootView.findViewById(R.id.time);
        textTV = (TextView) rootView.findViewById(R.id.text);
        holeIV = (HoleAnimationImageView) rootView.findViewById(R.id.hole);

        textTV.setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());

        apearAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_appear);
        apearAnimation.setInterpolator(new AccelerateInterpolator());
        disapearAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_disappear);
        disapearAnimation.setInterpolator(new DecelerateInterpolator());

        timerUpdateIntervalSetting = TimerSettings.getInstance().getTimerUpdate();
        switch (timerUpdateIntervalSetting) {
            case TimerSettings.TIMER_UPDATE_HUNDREDTH:
                timerUpdateIntervalInMs = 25;
                break;
            case TimerSettings.TIMER_UPDATE_MILLISECOND:
                timerUpdateIntervalInMs = 11;
                break;
            case TimerSettings.NO_TIMER_UPDATE:
                timerUpdateIntervalInMs = 60000;
                break;
            default : timerUpdateIntervalInMs = timerUpdateIntervalSetting;
        }

        inspectionTimerHelper = new TimerHelper(inspectionTimerListener);
        solveTimerHelper = new TimerHelper(solveTimerListener);

        isShowing = false;

        setTime(0);

    }

    public void show(boolean withAnimation) {
        isShowing = true;
        if (withAnimation) {
            timeTV.startAnimation(apearAnimation);
            textTV.startAnimation(apearAnimation);
        } else {
            timeTV.setVisibility(View.VISIBLE);
            textTV.setVisibility(View.VISIBLE);
        }
        holeIV.close(true, (timeTV.getWidth() / 2));
    }

    public void hide(boolean withAnimation) {
        isShowing = false;
        if (withAnimation) {
            timeTV.startAnimation(disapearAnimation);
            textTV.startAnimation(disapearAnimation);
        } else {
            timeTV.setVisibility(View.INVISIBLE);
            textTV.setVisibility(View.INVISIBLE);
        }
        holeIV.open(withAnimation);
    }

    public void setTime(int timeInMs) {
        setTime((long)timeInMs, timerUpdateIntervalSetting);
    }

    public void setTime(long timeInMs) {
        setTime(timeInMs, timerUpdateIntervalSetting);
    }

    public void setTime(int timeInMs, int format) {
        setTime((long)timeInMs, format);
    }

    public void setTime(long timeInMs, int format) {
        if(format == TimerSettings.NO_TIMER_UPDATE){
            timeTV.setText(R.string.timer_no_update);
        }else{
            timeTV.setText(TimerUtils.formatTime((int)timeInMs, true, 0, format));
        }
        holeIV.setHoleRadius(false, timeTV.getWidth() / 2);
    }


    public void prepare() {
        if(TimerSettings.getInstance().isInspection()){
            setTime(TimerSettings.INSPECTION_TIME_MS, TimerSettings.TIMER_UPDATE_SECOND);
            textTV.setText(R.string.timer_inspecting);
        }else{
            setTime(0);
            textTV.setText(R.string.timer_solving);
        }
        show(true);
    }

    public void startInspection() {
        Log.i("TimerOverlay", "starting inspection timer");
        if (!isShowing) {
            show(true);
        }

        inspectionTimerHelper.start(TimerSettings.TIMER_UPDATE_SECOND);
    }

    public void stopInspection(boolean startSolve) {
        Log.i("TimerOverlay", "stopping inspection timer");

        inspectionTimerHelper.stop();

        if(startSolve){
            startSolve();
        }
    }

    public void startSolve() {
        Log.i("TimerOverlay", "starting solve timer");
        if (!isShowing) {
            show(true);
        }
        if(inspectionTimerHelper.isInCourse()){
            stopInspection(false);
        }

        solveTimerHelper.start(timerUpdateIntervalInMs);
        textTV.setText(R.string.timer_solving);
    }

    //return the timer time
    public long stopSolve() {
        Log.i("TimerOverlay", "stopping solve timer");
        hide(true);
        return solveTimerHelper.stop();
    }

    public void cancel() {
        Log.i("TimerOverlay", "canceling timers");
        if(!isShowing){
            return;
        }
        if(inspectionTimerHelper.isInCourse()){
            inspectionTimerHelper.stop();
        }
        if(solveTimerHelper.isInCourse()){
            solveTimerHelper.stop();
        }
        hide(true);
    }

    public void setTouched(boolean isTouched){
        if(isTouched){
            timeTV.setTextColor(Color.RED);
            holeIV.setBorderColor(Color.RED);
        }else{
            timeTV.setTextColor(Color.BLACK);
            holeIV.setBorderColor(Color.BLACK);
        }
    }

    public boolean isSolveTimerInCourse() {
        return solveTimerHelper.isInCourse();
    }

    public boolean isInspectionTimerInCourse() {
        return inspectionTimerHelper.isInCourse();
    }

    private TimerHelper.TimerListener inspectionTimerListener = new TimerHelper.TimerListener() {
        @Override
        public void onSchedule(long elapsedTimeMs) {
            //Log.d("TimerOverlay", "elapsedInspectionTimeMs : " + elapsedTimeMs);
            Log.d("TimerOvLay", "elapsedInspectionTimeMs : " + elapsedTimeMs);
            setTime(TimerSettings.INSPECTION_TIME_MS - elapsedTimeMs,
                    TimerSettings.TIMER_UPDATE_SECOND);
            if(elapsedTimeMs >= TimerSettings.INSPECTION_TIME_MS){
                stopInspection(true);
            }
        }
    };

    private TimerHelper.TimerListener solveTimerListener = new TimerHelper.TimerListener() {
        @Override
        public void onSchedule(long elapsedTimeMs) {
            setTime(elapsedTimeMs);
        }
    };

    /*@Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        if(isSolveTimerInCourse()){
            ss.timerTypeState = TIMER_SOLVE;
            ss.timeMsState = solveTimerHelper.getTimeMs();
        solveTimerHelper.stop();
    }else if(isInspectionTimerInCourse()){
        ss.timerTypeState = TIMER_INSPECTION;
        ss.timeMsState = inspectionTimerHelper.getTimeMs();
        inspectionTimerHelper.stop();
    }else{
        ss.timerTypeState = NO_TIMER;
        ss.timeMsState = -1;
    }
        Log.d("TimerOvLay", "save :: timerType : " + ss.timerTypeState + " | timeMs : " + ss.timeMsState);
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.timeMsState = ss.timeMsState;
        this.timerTypeState = ss.timerTypeState;

        if(ss.timerTypeState == TIMER_SOLVE){
            solveTimerHelper.startFrom(ss.timeMsState, timerUpdateIntervalInMs);
            textTV.setText(R.string.timer_solving);
            show(false);
        }else if(ss.timerTypeState == TIMER_INSPECTION){
            inspectionTimerHelper.startFrom(ss.timeMsState, TimerSettings.TIMER_UPDATE_SECOND);
            textTV.setText(R.string.timer_inspecting);
            show(false);
        }else{
            hide(false);
        }

        Log.d("TimerOvLay", "restore :: timerType : " + ss.timerTypeState + " | timeMs : " + ss.timeMsState);
    }

    static class SavedState extends BaseSavedState {
        int timeMsState;
        int timerTypeState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            timeMsState = in.readInt();
            timerTypeState = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(timeMsState);
            out.writeInt(timerTypeState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }*/
}
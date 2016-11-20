package letsapps.com.letscube.fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.listener.SingletonChange;
import letsapps.com.letscube.puzzle.Cube;
import letsapps.com.letscube.puzzle.Puzzle;
import letsapps.com.letscube.puzzle.Pyraminx;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.CubeEvents;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.util.scramble.ScrambleGenerator;
import letsapps.com.letscube.util.Date;
import letsapps.com.letscube.view.AdaptableTextView;
import letsapps.com.letscube.view.HomeInfoView;
import letsapps.com.letscube.view.TimerOverlayLayout;

public class TimerFragment extends LCTFragment implements SingletonChange {

    public static final String KEY_SCRAMBLE = "KEY_SCRAMBLE";

    TextView scrambleTitleTV, copyrightTV, timerTV = null;
    AdaptableTextView scrambleDescTV;
    TimerOverlayLayout timerOverlay = null;
    HomeInfoView homeInfoV = null;
    // fakeActionBar to not move views when hiding real action bar during solve.
    View mainLayout, fakeActionBar, dividerTop, dividerBottom = null;

    DatabaseTime currentTime;

    Animation apearAnimation, disapearAnimation;

    String currentScramble = null;
    Puzzle currentPuzzle;

    //int defaultBackgroundColor = Color.WHITE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        homeInfoV = (HomeInfoView) rootView.findViewById(R.id.home_info);
        timerTV = (TextView) rootView.findViewById(R.id.timer_text);
        scrambleTitleTV = (TextView) rootView.findViewById(R.id.scramble_title);
        scrambleDescTV = (AdaptableTextView) rootView.findViewById(R.id.scramble_desc);
        copyrightTV = (TextView) rootView.findViewById(R.id.copyright);
        mainLayout = rootView.findViewById(R.id.main_layout);
        fakeActionBar = rootView.findViewById(R.id.fake_action_bar);
        dividerTop = rootView.findViewById(R.id.divider_top);
        dividerBottom = rootView.findViewById(R.id.divider_bottom);
        timerOverlay = (TimerOverlayLayout) rootView.findViewById(R.id.timer_overlay);

        if(getContext().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            fakeActionBar.setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());
        }else{
            fakeActionBar.setBackgroundColor(Color.TRANSPARENT);
        }

        if(savedInstanceState != null){
            Log.d("TimerFr","savedInstanceState != null");
            currentScramble = savedInstanceState.getString(KEY_SCRAMBLE);
        }else{
            Log.d("TimerFr","savedInstanceState == null");
        }
        Log.d("TimerFr","currentScramble = " + currentScramble);

        apearAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_appear);
        apearAnimation.setInterpolator(new AccelerateInterpolator());
        disapearAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_disappear);
        disapearAnimation.setInterpolator(new DecelerateInterpolator());

        timerOverlay.hide(false);

        if(currentPuzzle == null){
            setCorrespondingCubePattern(AppInstance.getInstance().getEvent());
        }

        Log.d("TimerBGColor", "bgColor : " + TimerSettings.getInstance().getBackground().getName(getContext()));
        //copyrightTV.setTextColor(
        //        TimerSettings.getInstance().getBackground().getSecundaryColor());

        homeInfoV.showCorrectView(currentPuzzle);

        if(AppInstance.getInstance().getEvent() != null){
            generateNewScramble(currentScramble);
        }
        homeInfoV.refreshData();

        mainLayout.setOnTouchListener(touchMainLayout);
        //timerOverlay.setTimerListener(timerListener);

        return rootView;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private View.OnTouchListener touchMainLayout = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){

                if(timerOverlay.isSolveTimerInCourse()) {
                    stopTimer();
                    return false;
                }

                // si le solve n'est pas en cours, prépare juste la mise en page
                if(!TimerSettings.getInstance().isInspection()) {
                    if(!timerOverlay.isSolveTimerInCourse()) {
                        timerOverlay.prepare();
                        timerOverlay.setTouched(true);
                        showElements(false);
                    }
                }else {
                    if(timerOverlay.isInspectionTimerInCourse()) {
                        timerOverlay.setTouched(true);
                    }else{
                        timerOverlay.prepare();
                        timerOverlay.setTouched(true);
                        showElements(false);
                    }
                }

            }

            if(event.getAction() == MotionEvent.ACTION_UP){
                timerOverlay.setTouched(false);
                if(TimerSettings.getInstance().isInspection()){

                    if(!timerOverlay.isInspectionTimerInCourse() &&
                            !timerOverlay.isSolveTimerInCourse()){
                        timerOverlay.startInspection();
                        return false;
                    }
                    if(timerOverlay.isInspectionTimerInCourse()){
                        timerOverlay.stopInspection(true);
                        return false;
                    }
                }else{
                    if(!timerOverlay.isSolveTimerInCourse()){
                        timerOverlay.startSolve();
                    }
                }
                return false;
            }
            return true;
        }
    };

    public void stopTimer(){
        final int timeMs = (int)timerOverlay.stopSolve();
        Log.d("TimerFragmet", "stop timer at timeMs : " + timeMs);
        showElements(true);
        ((TimerActivity)getActivity()).setTimerInCourse(false);
        currentTime.setTime(timeMs);
        currentTime.setDate(Date.getDate());
        ((TimerActivity)getActivity()).addTimeToDatabase(currentTime);
        generateNewScramble(null);
        timerTV.setText(TimerUtils.formatTime(timeMs));
    }

    public void onCurrentTimesChange() {
        if(homeInfoV != null) {
            homeInfoV.refreshData();
        }
    }

    private void showElements(boolean showElements){

        //Log.d("TimerBGColor", "bgColor : " + TimerSettings.getInstance().getBackground().getName(getContext()));

        if(showElements){
            //mainLayout.setBackgroundColor(defaultBackgroundColor);
            ((TimerActivity)getActivity()).showActionBar(true);
            fakeActionBar.setVisibility(View.GONE);
            homeInfoV.startAnimation(apearAnimation);
            timerTV.startAnimation(apearAnimation);
            if(dividerTop != null) {
                dividerTop.startAnimation(apearAnimation);
            }
            dividerBottom.startAnimation(apearAnimation);
            copyrightTV.startAnimation(disapearAnimation);
            scrambleTitleTV.startAnimation(apearAnimation);
            scrambleDescTV.startAnimation(apearAnimation);
            //timerOverlay.hide(true);
        }else{
            //mainLayout.setBackgroundColor(
            //        TimerSettings.getInstance().getBackground().getMainColor());
            ((TimerActivity)getActivity()).showActionBar(false);
            fakeActionBar.setVisibility(View.VISIBLE);
            homeInfoV.startAnimation(disapearAnimation);
            scrambleTitleTV.startAnimation(disapearAnimation);
            scrambleDescTV.startAnimation(disapearAnimation);
            timerTV.startAnimation(disapearAnimation);
            if(dividerTop != null) {
                dividerTop.startAnimation(disapearAnimation);
            }
            dividerBottom.startAnimation(disapearAnimation);
            copyrightTV.startAnimation(apearAnimation);
            //timerOverlay.show(true);
        }

    }

    public void cancelTimer(){
        timerOverlay.cancel();
        showElements(true);
        ((TimerActivity)getActivity()).setTimerInCourse(false);
        generateNewScramble(null);
    }

    public void resetTimer(){
        if(timerTV != null) {
            timerOverlay.cancel();
            timerTV.setText(R.string.timer_default_text);
        }
    }

    public boolean isTimerInCourse(){
        return timerOverlay.isInspectionTimerInCourse() || timerOverlay.isSolveTimerInCourse();
    }

    private void generateNewScramble(String scramble){
        /*Log.i("TimerFragment", "(generateNewScramble) currentEvent : " +
                AppInstance.getInstance().getEvent().getName(getContext()));*/
        currentTime = new DatabaseTime();
        if(scramble == null) {
            currentScramble = ScrambleGenerator.getScramble(AppInstance.getInstance().getEvent());
        }else{
            currentScramble = scramble;
        }
        Log.d("TimerFr", "new calculated scramble : " + currentScramble);
        if(currentPuzzle == null) {
            setCorrespondingCubePattern(AppInstance.getInstance().getEvent());
        }
        if(currentPuzzle != null) {
            currentPuzzle.scramble(currentScramble);
            homeInfoV.showCorrectView(currentPuzzle);
        }
        currentTime.setScramble(currentScramble);
        currentTime.setEvent(AppInstance.getInstance().getEvent());
        currentTime.setSession(((TimerActivity)getActivity()).getLastSession());
        if(currentScramble != null) {
            scrambleTitleTV.setVisibility(View.VISIBLE);
            Log.d("TimerFr", "Going to write currentScramble : " + currentScramble);
            scrambleDescTV.setAdaptableText(
                    currentScramble, AppInstance.getInstance().getEvent().getScrambleTypeId());
        } else {
            scrambleTitleTV.setVisibility(View.GONE);
            scrambleDescTV.setAdaptableText(
                    getResources().getString(R.string.scramble_unavailable),
                    AppInstance.getInstance().getEvent().getScrambleTypeId());
        }
    }

    /* N'utiliser que pour les tests!*/
    /*
    public void add100TimesInDB(){
        Log.d("TF", "starting add100TimesInDB()");
        for(int i = 0; i < 100; i++) {
            currentTime = new DatabaseTime();
            currentScramble = ScrambleGenerator.getScramble(AppInstance.getInstance().getEvent());
            currentTime.setScramble(currentScramble);
            currentTime.setEvent(AppInstance.getInstance().getEvent());
            currentTime.setSession(((TimerActivity) getActivity()).getLastSession());
            currentTime.setTime((int)(Math.random() * 10000) + 9000);
            currentTime.setDate(Date.getDate());
            ((TimerActivity)getActivity()).addTimeToDatabase(currentTime);
        }
        Log.d("TF", "starting add100TimesInDB()");
    }
    */

    public void setCorrespondingCubePattern(Event event) {
        if(TimerSettings.getInstance().getHomeInfo() == TimerSettings.HOME_INFO_PATTERN_ID){
            if (event != null) {
                if (event.getId() == CubeEvents.CUBE2x2.getId()) {
                    currentPuzzle = new Cube(2);
                } else if (event.getId() == CubeEvents.CUBE3x3.getId() ||
                        event.getId() == CubeEvents.CUBE3x3OH.getId() ||
                        event.getId() == CubeEvents.CUBE3x3BLD.getId() ||
                        event.getId() == CubeEvents.CUBE3x3FT.getId()) {
                    currentPuzzle = new Cube(3);
                } else if (event.getId() == CubeEvents.CUBE4x4.getId() ||
                        event.getId() == CubeEvents.CUBE4x4BLD.getId()) {
                    currentPuzzle = new Cube(4);
                } else if (event.getId() == CubeEvents.CUBE5x5.getId() ||
                        event.getId() == CubeEvents.CUBE5x5BLD.getId()) {
                    currentPuzzle = new Cube(5);
                } else if (event.getId() == CubeEvents.CUBE6x6.getId()) {
                    currentPuzzle = new Cube(6);
                } else if (event.getId() == CubeEvents.CUBE7x7.getId()) {
                    currentPuzzle = new Cube(7);
                } else if (event.getId() == CubeEvents.PYRAMINX.getId()) {
                    currentPuzzle = new Pyraminx();
                } else {
                    currentPuzzle = null;
                }
                if (homeInfoV != null) {
                    homeInfoV.showCorrectView(currentPuzzle);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SCRAMBLE, currentScramble);
        Log.d("TimerFr", "Setting outstate, currentScramble = " + currentScramble);
    }

    @Override
    public void onCurrentEventChange(){

        if(homeInfoV != null){
            setCorrespondingCubePattern(AppInstance.getInstance().getEvent());
            homeInfoV.refreshData();
        }

        if(scrambleDescTV != null){
            generateNewScramble(null);
        }
    }
}

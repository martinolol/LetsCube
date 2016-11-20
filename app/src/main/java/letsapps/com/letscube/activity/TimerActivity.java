package letsapps.com.letscube.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

import letsapps.com.letscube.R;
import letsapps.com.letscube.database.DatabaseEventHandler;
import letsapps.com.letscube.database.DatabaseRecordsHandler;
import letsapps.com.letscube.database.DatabaseSessionHandler;
import letsapps.com.letscube.database.DatabaseStatsHandler;
import letsapps.com.letscube.database.DatabaseTimeHandler;
import letsapps.com.letscube.database.DatabaseSettingsHandler;
import letsapps.com.letscube.fragment.TimerTimelistFragment;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.fragment.SettingsFragment;
import letsapps.com.letscube.fragment.TimerFragment;
import letsapps.com.letscube.fragment.TimesFragment;
import letsapps.com.letscube.listener.EventClickListener;
import letsapps.com.letscube.listener.RecordListener;
import letsapps.com.letscube.listener.SettingsListener;
import letsapps.com.letscube.mixpanel.MixPanelHelper;
import letsapps.com.letscube.util.Session;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.cube.CubeEvents;
import letsapps.com.letscube.util.cube.CubeRecord;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.view.actionBar.LCActionBarV2;
import letsapps.com.letscube.view.dialog.RecordDialogView;
import letsapps.com.letscube.view.dialog.EventDialogView;
import letsapps.com.letscube.view.dialog.TextDialogView;

public class TimerActivity extends FragmentActivity {

    public static final String KEY_FRAGMENT = "KEY_FRAGMENT";

    public static final String FRAGMENT_TIMER = "FRAGMENT_TIMER";
    public static final String FRAGMENT_TIMES = "FRAGMENT_TIMES";
    public static final String FRAGMENT_SETTINGS = "FRAGMENT_SETTINGS";

    MixpanelAPI mixpanelInstance;

    LCActionBarV2 actionBar;

    TimerFragment timerFragment;
    TimesFragment timerTimesFragment;
    SettingsFragment settingsFragment;

    String currentFragmentId;

    boolean isTimerInCourse;

    ArrayList<Event> addedEvents;

    DatabaseTimeHandler timeDatabase;
    DatabaseRecordsHandler recordsDatabase;
    DatabaseEventHandler eventDatabase;
    DatabaseSessionHandler sessionDatabase;
    DatabaseSettingsHandler settingsDatabase;
    DatabaseStatsHandler statsDatabase;

    AlertDialog eventChoiceDialog;
    AlertDialog.Builder recordDialog;
    RecordDialogView recordDialogView;

    Animation appearAnimation, disappearAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Log.i("TA", "onCreateCalled.");

        if(TimerSettings.getInstance().getTheme() == null){
            Log.w("TimerActivity", "TimerSettings.getInstance().getTheme() == null");
            startActivity(new Intent(this, LoadingActivity.class));
            finish();
        }
        actionBar = (LCActionBarV2)findViewById(R.id.actionBar);
        mixpanelInstance = MixPanelHelper.getInstance(this);

        Log.d("TimerActivity", "savedInstanState " +
                (savedInstanceState == null ? " == null" : " != null"));

        timerTimesFragment = new TimesFragment();
        settingsFragment = new SettingsFragment();

        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getString(KEY_FRAGMENT);
            timerFragment = (TimerFragment)getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TIMER);
            if(timerFragment == null){
                timerFragment = new TimerFragment();
            }
        }else{
            timerFragment = new TimerFragment();
            currentFragmentId = FRAGMENT_TIMER;
            launchFragment(currentFragmentId);
        }

        Log.d("TimerActivity", "currentFragmentId = " +currentFragmentId);

        timeDatabase = new DatabaseTimeHandler(this);
        recordsDatabase = new DatabaseRecordsHandler(this);
        eventDatabase = new DatabaseEventHandler(this);
        settingsDatabase = new DatabaseSettingsHandler(this);
        sessionDatabase = new DatabaseSessionHandler(this);
        statsDatabase = new DatabaseStatsHandler(this);

        addedEvents = eventDatabase.getEvents();

        if(AppInstance.getInstance().getEvent() == null){
            AppInstance.getInstance().setEvent(CubeEvents.CUBE3x3);
        }

        if(addedEvents == null){
            addedEvents = new ArrayList<>();
        }

        appearAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_appear);
        appearAnimation.setDuration(200);
        disappearAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_disappear);
        disappearAnimation.setDuration(200);

        actionBar.setTitle(AppInstance.getInstance().getEvent().getName(this));
        actionBar.updateTheme();
        actionBar.selectCurrentMenu(currentFragmentId);

        AlertDialog.Builder eventChoiceDialogBuilder = new AlertDialog.Builder(this);
        eventChoiceDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentFragmentId) {
                    case FRAGMENT_TIMER: timerFragment.startAnimation(appearAnimation); break;
                    case FRAGMENT_TIMES: timerTimesFragment.startAnimation(appearAnimation); break;
                    case FRAGMENT_SETTINGS: settingsFragment.startAnimation(appearAnimation); break;
                }
            }
        });
        eventChoiceDialog = eventChoiceDialogBuilder.create();

        recordDialog = new AlertDialog.Builder(this);

        if(AppInstance.getInstance().getTimes().getRecord() == null){
            recordsDatabase.recomputeRecord(AppInstance.getInstance().getEvent().getId());
        }
        if(AppInstance.getInstance().getTimes().getStats() == null){
            statsDatabase.recomputeStats(AppInstance.getInstance().getEvent().getId());
        }
        TimerSettings.getInstance().addSettingsListener(settingsListener);

        timeDatabase.setRecordListener(recordListener);

        Log.d("TimerActivity", "onCreateFinished");
    }

    private EventClickListener clickEventInDialog = new EventClickListener() {
        @Override
        public void onEventClick(Event clickedEvent, boolean dismissDialog) {
            Log.i("TimerActivity", clickedEvent.getName() + " event selected");

            switch (currentFragmentId) {
                case FRAGMENT_TIMER: timerFragment.startAnimation(appearAnimation); break;
                case FRAGMENT_TIMES: timerTimesFragment.startAnimation(appearAnimation); break;
                case FRAGMENT_SETTINGS: settingsFragment.startAnimation(appearAnimation); break;
            }

            MixPanelHelper.sendEventChanged(
                    TimerActivity.this, mixpanelInstance, AppInstance.getInstance().getEvent(), clickedEvent);

            AppInstance.getInstance().setEvent(clickedEvent);
            if(AppInstance.getInstance().getTimes().getRecord() == null){
                recordsDatabase.recomputeRecord(AppInstance.getInstance().getEvent().getId());
            }
            if(AppInstance.getInstance().getTimes().getStats() == null){
                statsDatabase.recomputeStats(AppInstance.getInstance().getEvent().getId());
            }

            if(dismissDialog) {
                eventChoiceDialog.dismiss();
            }
            /*if(timerFragment != null){
                timerFragment.onCurrentEventChange();
            }*/
            actionBar.setTitle(clickedEvent.getName(TimerActivity.this));
            if(currentFragmentId == FRAGMENT_TIMER){
                timerFragment.onCurrentEventChange();
                timerFragment.onCurrentTimesChange();
                timerFragment.resetTimer();
            }else if(currentFragmentId == FRAGMENT_TIMES){
                timerTimesFragment.onCurrentEventChange();
                timerTimesFragment.onCurrentTimesChange();
            }else{
                //launchFragment(currentFragmentId); // to refresh the current fragment with the new event
            }

            //recordsDatabase.printDB();
            //statsDatabase.printDB();

        }
    };

    public void launchEventDialog(){

        final EventDialogView eventDialogView = new EventDialogView(TimerActivity.this, addedEvents,
                clickEventInDialog);
        eventChoiceDialog.setView(eventDialogView);
        eventChoiceDialog.show();

        switch (currentFragmentId){
            case FRAGMENT_TIMER : timerFragment.startAnimation(disappearAnimation); break;
            case FRAGMENT_TIMES : timerTimesFragment.startAnimation(disappearAnimation); break;
            case FRAGMENT_SETTINGS : settingsFragment.startAnimation(disappearAnimation); break;
        }
    }

    public void launchFragment(String fragmentId){

        /*switch (currentFragmentId) {
            case FRAGMENT_TIMER: timerFragment.startAnimation(disappearAnimation); break;
            case FRAGMENT_TIMES: timerTimesFragment.startAnimation(disappearAnimation); break;
            case FRAGMENT_SETTINGS: settingsFragment.startAnimation(disappearAnimation); break;
        }*/

        currentFragmentId = fragmentId;
        actionBar.selectCurrentMenu(currentFragmentId);

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        /*ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);*/

        switch(fragmentId){

            case FRAGMENT_TIMER :
                //timerFragment = new TimerFragment();
                ft.replace(R.id.container, timerFragment).addToBackStack(null).commit();
                /*timerFragment.onCurrentEventChange();
                timerFragment.onCurrentTimesChange();
                timerFragment.resetTimer();*/
                break;


            case FRAGMENT_TIMES :
                //timerTimesFragment = new TimesFragment();
                ft.replace(R.id.container, timerTimesFragment).addToBackStack(null).commit();
                break;

            case FRAGMENT_SETTINGS :
                //settingsFragment = new SettingsFragment();
                ft.replace(R.id.container, settingsFragment).addToBackStack(null).commit();
                break;
        }

        /*switch (currentFragmentId) {
            case FRAGMENT_TIMER: timerFragment.startAnimation(appearAnimation); break;
            case FRAGMENT_TIMES: timerTimesFragment.startAnimation(appearAnimation); break;
            case FRAGMENT_SETTINGS: settingsFragment.startAnimation(appearAnimation); break;
        }*/
    }

    private SettingsListener settingsListener = new SettingsListener() {
        @Override
        public void OnInspectionChange(boolean newInspection) {
            Log.i("TA (settingListener)", "newInspection setted : " + newInspection);
            settingsDatabase.updateInspection(newInspection);
            MixPanelHelper.sendInspectionChanged(mixpanelInstance);
        }

        @Override
        public void OnTimerUpdateChange(int newTimerUpdate) {
            Log.i("TA (settingListener)", "newTimerUpdate setted : " + newTimerUpdate);
            settingsDatabase.updateTimerUpdate(newTimerUpdate);
            MixPanelHelper.sendTimerUpdateChanged(mixpanelInstance);
        }

        @Override
        public void OnHomeContentChange(int newHomeInfoId) {
            Log.i("TA (settingListener)", "newHomeInfo setted : " + newHomeInfoId);
            settingsDatabase.updateHomeContent(newHomeInfoId);
            MixPanelHelper.sendHomeContentChanged(mixpanelInstance);
        }

        @Override
        public void OnThemeChange(Theme newTheme) {
            Log.i("TA (settingListener)", "newTheme setted : " +
                    newTheme.getName(TimerActivity.this));

            //to refresh eventDialog for the new theme color.
            final AlertDialog.Builder eventChoiceDialogBuilder =
                    new AlertDialog.Builder(TimerActivity.this);
            eventChoiceDialogBuilder.setNegativeButton(R.string.cancel, null);
            eventChoiceDialog = eventChoiceDialogBuilder.create();

            actionBar.updateTheme();
            settingsDatabase.updateTheme(newTheme);
            MixPanelHelper.sendThemeChanged(TimerActivity.this, mixpanelInstance);
        }

        @Override
        public void OnBackgroundChange(Theme newBackground) {
            Log.i("TA (settingListener)", "newBackground setted : " +
                    newBackground.getName(TimerActivity.this));

            settingsDatabase.updateBackground(newBackground);
            MixPanelHelper.sendBackgroundChanged(TimerActivity.this,  mixpanelInstance);
        }
    };

    private RecordListener recordListener = new RecordListener() {
        @Override
        public void newRecords(DatabaseTime lastTime, ArrayList<Integer> recordsTypeId,
                               ArrayList<Integer> previousRecordTime) {

            recordDialogView = new RecordDialogView(TimerActivity.this);
            recordDialog.setView(recordDialogView);

            final int nbRecords = recordsTypeId.size();

            for(int i = 0; i < nbRecords; i++){
                final int time;
                switch (recordsTypeId.get(i)){
                    case CubeRecord.TYPE_SINGLE : time = lastTime.getTime(); break;
                    case CubeRecord.TYPE_MO3 : time = lastTime.getMo3(); break;
                    case CubeRecord.TYPE_AVG5 : time = lastTime.getAvg5(); break;
                    case CubeRecord.TYPE_AVG12 : time = lastTime.getAvg12(); break;
                    case CubeRecord.TYPE_MO50 : time = lastTime.getMo50(); break;
                    case CubeRecord.TYPE_MO100 : time = lastTime.getMo100(); break;
                    default : time = 0;
                }
                recordDialogView.addRecord(CubeRecord.getName(recordsTypeId.get(i)),
                        time, previousRecordTime.get(i));
            }
            recordDialogView.showRecord();
            recordDialog.setNegativeButton(R.string.close, null);
            recordDialog.show();

            if(currentFragmentId == FRAGMENT_TIMES) {
                timerTimesFragment.onCurrentTimesChange();
            }
        }

        @Override
        public void removedRecord(DatabaseTime time, int recordTypeId) {

        }
    };

    public void showActionBar(boolean hasToShow){
        if(hasToShow){
            actionBar.setVisibility(View.VISIBLE);
        }else{
            actionBar.setVisibility(View.GONE);
        }
    }

    public void setTimerInCourse(boolean isTimerInCourse){
        this.isTimerInCourse = isTimerInCourse;
        if(isTimerInCourse) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    public void addTimeToDatabase(final DatabaseTime time) {

        if (time.getEvent() == null) {
            time.setEvent(AppInstance.getInstance().getEvent());
        }

        if (time.getTime() == 0) {
            return;
        }

        if (AppInstance.getInstance().getTimes().getRecord() != null &&
                AppInstance.getInstance().getTimes().size() >= 5) {

            final int recordSingle = AppInstance.getInstance().getTimes()
                    .getRecord().getSingle();
            final int recordAvg5 = AppInstance.getInstance().getTimes()
                    .getRecord().getAvg5();

            if (time.getTime() < (recordSingle / 3) || time.getTime() > (recordAvg5 * 3)) {
                final AlertDialog.Builder weirdTimeDialogBuilder = new AlertDialog.Builder(this);
                final TextDialogView weirdTimeDialogView = new TextDialogView(this, R.string.time_weird_dialog_title);
                if (time.getTime() < (recordSingle / 3)) {
                    weirdTimeDialogView.setText(getResources().getString(
                            R.string.time_weird_dialog_message_low,
                            TimerUtils.formatTime(time.getTime())));
                } else {
                    weirdTimeDialogView.setText(getResources().getString(
                            R.string.time_weird_dialog_message_high,
                            TimerUtils.formatTime(time.getTime())));
                }
                weirdTimeDialogBuilder.setView(weirdTimeDialogView);
                weirdTimeDialogBuilder.setPositiveButton(
                        R.string.time_weird_dialog_button_keep, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (time.getTime() < (recordSingle / 3)) {
                                    addTimeToDatabaseWithoutVerification(time, "ACCEPTED - LOW");
                                } else {
                                    addTimeToDatabaseWithoutVerification(time, "ACCEPTED - HIGH");
                                }
                            }
                        });

                weirdTimeDialogBuilder.setNegativeButton(R.string.time_weird_dialog_button_delete,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (time.getTime() < (recordSingle / 3)) {
                                    MixPanelHelper.sendTimeTimed(mixpanelInstance,
                                            TimerActivity.this, time, "UNACCEPTED - LOW");
                                } else {
                                    MixPanelHelper.sendTimeTimed(mixpanelInstance,
                                            TimerActivity.this, time, "UNACCEPTED - HIGH");
                                }

                            }
                        });
                weirdTimeDialogBuilder.setCancelable(false);
                weirdTimeDialogBuilder.show();

            } else {
                addTimeToDatabaseWithoutVerification(time, "-");
            }
        } else {
            addTimeToDatabaseWithoutVerification(time, "-");
        }
    }

    public void addTimeToDatabaseWithoutVerification(final DatabaseTime time, String comment){

        statsDatabase.timeAdded(time);

        Log.d("TimerActivity", "adding time in timeDatabase for event : " +
                time.getEvent().getName(this));
        Log.d("TimerActivity", "time.getEvent() : " + time.getEvent().getName(this));
        timeDatabase.addTime(time, AppInstance.getInstance().getTimes());
        if (currentFragmentId == FRAGMENT_TIMES) {
            timerTimesFragment.onCurrentTimesChange();
        }
        if (currentFragmentId == FRAGMENT_TIMER) {
            timerFragment.onCurrentTimesChange();
        }
        MixPanelHelper.sendTimeTimed(mixpanelInstance,
                TimerActivity.this, time, comment);
    }

    public void deleteTime(DatabaseTime time){
        timeDatabase.removeTime(time, AppInstance.getInstance().getTimes());
        statsDatabase.timeRemoved(time);

        AppInstance.getInstance().getTimes().setRecord(
                recordsDatabase.recomputeRecord(time.getEvent().getId()));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timerTimesFragment != null) {
                    timerTimesFragment.onCurrentTimesChange();
                }
                if (timerFragment != null) {
                    timerFragment.onCurrentTimesChange();
                }
            }
        });
        //timeDatabase.printDB();
    }

    public void deleteAllTimes(Event eventToDelete){
        statsDatabase.resetStats(eventToDelete.getId());

        final int nbDeletedTimes = timeDatabase.deleteEventTimes(eventToDelete);

        AppInstance.getInstance().getTimes().removeAll();

        AppInstance.getInstance().getTimes().setRecord(
                recordsDatabase.recomputeRecord(eventToDelete.getId()));

        //launchFragment(currentFragmentId);

        MixPanelHelper.sendEventTimesDeleted(this, mixpanelInstance, eventToDelete, nbDeletedTimes);
    }

    public void editTimePenalty(DatabaseTime time){
        Log.d("TA (editTimePenalty)", "editTimePenalty : " + time.toString());

        //timeDatabase.printDBTimes();
        timeDatabase.updateTimePenalty(AppInstance.getInstance().getTimes(), time);
        AppInstance.getInstance().getTimes().setRecord(
                recordsDatabase.recomputeRecord(time.getEvent().getId()));
        if (timerTimesFragment != null) {
            timerTimesFragment.onCurrentTimesChange();
        }
        if (timerFragment != null) {
            timerFragment.onCurrentTimesChange();
        }
        //timeDatabase.printDBTimes();
        // au cas où ça affecte un record

        MixPanelHelper.sendTimePenaltyEdited(this, mixpanelInstance, time);
    }

    public Session getLastSession(){
        return sessionDatabase.getLastSession(AppInstance.getInstance().getEvent().getId());
    }

    public void addEvent(Event event){
        addedEvents.add(eventDatabase.addEvent(event));
        MixPanelHelper.sendEventAdded(this, mixpanelInstance, event);
    }

    public void deleteEvent(Event event){
        eventDatabase.removeEvent(event);
        addedEvents.remove(event);
    }

    public void updateEvent(Event event){
        eventDatabase.updateEvent(event);
        AppInstance.getInstance().setEvent(event);
        if(timerFragment != null) {
            timerFragment.onCurrentEventChange();
        }
        if(timerTimesFragment != null) {
            timerTimesFragment.onCurrentEventChange();
        }
        actionBar.setTitle(event.getName());
    }

    public MixpanelAPI getMixpanelInstance(){
        return mixpanelInstance;
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FRAGMENT, currentFragmentId);
        try {
            getSupportFragmentManager().putFragment(outState, FRAGMENT_TIMER, timerFragment);
        }catch(IllegalStateException ise){
            Log.e("TimerActivity", "Unable to conserve timer fragment : " + ise.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        if(mixpanelInstance != null) {
            mixpanelInstance.flush();
        }
        TimerSettings.getInstance().removeSettingsListener(settingsListener);
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){

        if(currentFragmentId == FRAGMENT_TIMER){
            if(timerFragment != null) {
                if (timerFragment.isTimerInCourse()) {
                    showActionBar(true);
                    timerFragment.cancelTimer();
                    timerFragment.cancelTimer();
                } else {
                    finish();
                }
            }
        }else {
            launchFragment(FRAGMENT_TIMER);
        }

    }
}
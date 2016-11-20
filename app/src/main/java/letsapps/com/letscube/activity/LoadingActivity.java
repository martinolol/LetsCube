package letsapps.com.letscube.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import letsapps.com.letscube.R;
import letsapps.com.letscube.database.DatabaseSessionHandler;
import letsapps.com.letscube.database.DatabaseSettingsHandler;
import letsapps.com.letscube.database.DatabaseStatsHandler;
import letsapps.com.letscube.database.DatabaseTimeHandler;
import letsapps.com.letscube.database.DatabaseUserHandler;
import letsapps.com.letscube.mixpanel.MixPanelHelper;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.util.LoadingNoteGenerator;
import letsapps.com.letscube.util.User;
import letsapps.com.letscube.util.cube.CubeEvents;

public class LoadingActivity extends Activity{

    public static final int REQUEST_CODE_LOGIN = 1;

    Thread loadingThread;

    DatabaseUserHandler userDatabase;
    DatabaseTimeHandler timeDatabase;
    DatabaseSessionHandler sessionDatabase;
    DatabaseSettingsHandler settingsDatabase;
    DatabaseStatsHandler statsDatabase;

    MixpanelAPI mixpanelInstance;
    User user;
    boolean isLoginActivityAlreadyLaunched = false;

    TextView noteTV = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        noteTV = (TextView)findViewById(R.id.note);
        noteTV.setText(getString(R.string.note_start) + " " + LoadingNoteGenerator.getRandomNote(this));

        mixpanelInstance = MixPanelHelper.getInstance(this);

        timeDatabase = new DatabaseTimeHandler(this);
        sessionDatabase = new DatabaseSessionHandler(this);
        settingsDatabase = new DatabaseSettingsHandler(this);
        statsDatabase = new DatabaseStatsHandler(this);

        settingsDatabase.instantiateSettings();

        loadingThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    getTimesFromDatabase();
                    statsDatabase.instantiateStats();
                    Log.i("LoadingActivity", "Loading finished");
                } catch (Exception e) {
                    Log.e("LoadingActivity", "exception occure : " + e.getMessage());
                } finally {
                    startActivity(new Intent(LoadingActivity.this, TimerActivity.class));
                    finish();
                }
            }
        };

        userDatabase = new DatabaseUserHandler(this);
        user = userDatabase.getUser();
        if(MixPanelHelper.IS_MIXPANEL_TRACKING) {
            if (user != null) {
                isLoginActivityAlreadyLaunched = true;
            }
            if (user == null && !isLoginActivityAlreadyLaunched) {
                startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN);
                Log.i("TA", "startActivityForResult.");
            } else {
                isLoginActivityAlreadyLaunched = true;
                mixpanelInstance = MixPanelHelper.getInstance(this);
                if (mixpanelInstance != null) {
                    mixpanelInstance.getPeople().identify(user.getMixpanelID());
                }
            }
        }else{
            isLoginActivityAlreadyLaunched = true;
        }
        AppInstance.getInstance().setEvent(CubeEvents.CUBE3x3);

        if(isLoginActivityAlreadyLaunched) {
            loadingThread.start();
            Log.i("LoadingActivity", "loadingThread start");
        }else{
            Log.i("LoadingActivity", "loadingThread don't start");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("TA", "onActivityResult.");
        if(!MixPanelHelper.IS_MIXPANEL_TRACKING){
            loadingThread.start();
            isLoginActivityAlreadyLaunched = true;
        }
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_LOGIN){
                loadingThread.start();
                isLoginActivityAlreadyLaunched = true;
                user = new User();
                user.setName(data.getStringExtra(LoginActivity.KEY_USER_NAME));
                user.setEmail(data.getStringExtra(LoginActivity.KEY_USER_EMAIL));
                user.setMixpanelID(data.getStringExtra(LoginActivity.KEY_USER_MIXPANELID));
                userDatabase.addUser(user);
                MixPanelHelper.setUserProperties(mixpanelInstance, user);
            }
        }else{
            finish();
        }
    }

    private void getTimesFromDatabase(){
        timeDatabase.instantiateTimeList();
        AppInstance.getInstance().getTimes().setSessions(sessionDatabase.getSessions());
    }
}

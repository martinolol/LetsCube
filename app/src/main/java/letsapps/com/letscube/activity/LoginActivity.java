package letsapps.com.letscube.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import letsapps.com.letscube.R;
import letsapps.com.letscube.listener.SettingsListener;
import letsapps.com.letscube.mixpanel.MixPanelHelper;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.util.User;

public class LoginActivity extends Activity implements SettingsListener{

    public static final String KEY_USER_NAME = "USER_NAME";
    public static final String KEY_USER_EMAIL = "USER_EMAIL";
    public static final String KEY_USER_MIXPANELID = "USER_MIXPANELID";

    RelativeLayout mainLayout;
    EditText nameET, emailET;
    TextView info;
    Button button;

    MixpanelAPI mixpanelInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mixpanelInstance = MixPanelHelper.getInstance(this);
        MixPanelHelper.sendLoginActivity(mixpanelInstance, "LoginActivity launched");
        Log.i("LA", "onCreate called.");

        final Intent onBoardingIntent = new Intent(this, OnboardingActivity.class);
        onBoardingIntent.putExtra(OnboardingActivity.IS_SHOWING_FINAL_PAGE, true);

        startActivity(onBoardingIntent);

        TimerSettings.getInstance().addSettingsListener(this);

        mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        nameET = (EditText)findViewById(R.id.name);
        emailET = (EditText)findViewById(R.id.email);
        info = (TextView)findViewById(R.id.info);
        button = (Button)findViewById(R.id.button);

        if(TimerSettings.getInstance().getTheme() != null) {
            mainLayout.setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());
        }
        button.setOnClickListener(clickButton);
    }

    private View.OnClickListener clickButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!MixPanelHelper.IS_MIXPANEL_TRACKING){
                finish();
            }
            final User user = new User();
            if(!nameET.getText().toString().isEmpty()){
                user.setName(nameET.getText().toString());
            }else{
                info.setText(R.string.login_info_name_empty);
                //MixPanelHelper.sendLoginActivity(mixpanelInstance, "Empty name");
                return;
            }
            if(isValidEmail(emailET.getText().toString())){
                user.setEmail(emailET.getText().toString());
            }else{
                info.setText(R.string.login_info_email_invalid);
                //MixPanelHelper.sendLoginActivity(mixpanelInstance, "Invalid email");
                return;
            }
            MixPanelHelper.sendLoginActivity(mixpanelInstance, "LoginActivity login successfully");
            Log.i("LA", "login successfull.");

            user.setMixpanelID(emailET.getText() + ":" + nameET.getText());

            final Intent resultIntent = new Intent();
            resultIntent.putExtra(KEY_USER_NAME, user.getName());
            resultIntent.putExtra(KEY_USER_EMAIL, user.getEmail());
            resultIntent.putExtra(KEY_USER_MIXPANELID, user.getMixpanelID());

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };

    private boolean isValidEmail(String email){
        if(!email.contains("@") || !email.contains(".") || email.length() < 5){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onDestroy(){
        MixPanelHelper.sendLoginActivity(mixpanelInstance, "LoginActivity destroyed");
        Log.i("LA", "loginActivity destroyed.");
        TimerSettings.getInstance().removeSettingsListener(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void OnInspectionChange(boolean newInspection) {}

    @Override
    public void OnTimerUpdateChange(int newTimerUpdate) {}

    @Override
    public void OnHomeContentChange(int newHomeContentId) {}

    @Override
    public void OnThemeChange(Theme newTheme) {
        mainLayout.setBackgroundColor(TimerSettings.getInstance().getTheme().getMainColor());
    }

    @Override
    public void OnBackgroundChange(Theme newBackground) {}
}

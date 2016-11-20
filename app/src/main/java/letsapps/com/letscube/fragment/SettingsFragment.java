package letsapps.com.letscube.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import letsapps.com.letscube.R;
import letsapps.com.letscube.activity.OnboardingActivity;
import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.listener.SettingsListener;
import letsapps.com.letscube.mixpanel.MixPanelHelper;
import letsapps.com.letscube.singleton.AppInstance;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.util.scramble.ScrambleGenerator;
import letsapps.com.letscube.view.dialog.ContactDialogView;
import letsapps.com.letscube.view.dialog.ListDialogView;
import letsapps.com.letscube.view.dialog.SpectreDialogView;
import letsapps.com.letscube.view.dialog.TextDialogView;

public class SettingsFragment extends LCTFragment {

    RelativeLayout inspectionRL;
    CheckBox inspectionCB;

    RelativeLayout themeRL;
    TextView currentThemeTV;
    RelativeLayout backgroundRL;
    TextView currentBackgroundTV;

    RelativeLayout timerUpdateRL;
    TextView currentTimerUpdateTV;

    RelativeLayout homeContentRL;
    TextView currentHomeContentTV;

    RelativeLayout helpScrambleRL;
    TextView helpScrambleTV;
    RelativeLayout helpAppRL;
    RelativeLayout contactRL, supportRL, rateRL;

    AlertDialog listDialog = null;
    AlertDialog.Builder spectreDialog = null;
    SpectreDialogView spectreDialogView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        inspectionRL = (RelativeLayout)rootView.findViewById(R.id.inspection);
        inspectionCB = (CheckBox)rootView.findViewById(R.id.inspection_CB);
        timerUpdateRL = (RelativeLayout)rootView.findViewById(R.id.timer_update);
        currentTimerUpdateTV = (TextView)rootView.findViewById(R.id.timer_update_current);
        homeContentRL = (RelativeLayout)rootView.findViewById(R.id.timer_home_content);
        currentHomeContentTV = (TextView)rootView.findViewById(R.id.timer_home_content_current);
        themeRL = (RelativeLayout)rootView.findViewById(R.id.theme);
        currentThemeTV = (TextView)rootView.findViewById(R.id.theme_current);
        backgroundRL = (RelativeLayout)rootView.findViewById(R.id.background);
        currentBackgroundTV = (TextView)rootView.findViewById(R.id.background_current);
        helpScrambleRL = (RelativeLayout)rootView.findViewById(R.id.help_scramble);
        helpScrambleTV = (TextView)rootView.findViewById(R.id.help_scramble_desc);
        helpAppRL = (RelativeLayout)rootView.findViewById(R.id.help_app);
        contactRL = (RelativeLayout)rootView.findViewById(R.id.contact);
        supportRL = (RelativeLayout)rootView.findViewById(R.id.support);
        rateRL = (RelativeLayout)rootView.findViewById(R.id.rate);

        inspectionCB.setChecked(TimerSettings.getInstance().isInspection());
        if(TimerSettings.getInstance().getTheme() != null){
            currentThemeTV.setText(TimerSettings.getInstance().getTheme().getNameResId());
        }
        currentBackgroundTV.setText(TimerSettings.getInstance().getBackground().getNameResId());
        currentTimerUpdateTV.setText(TimerSettings.getInstance().getTimerUpdateStr(getContext()));
        helpScrambleTV.setText(getContext().getString(R.string.settings_help_scramble,
                AppInstance.getInstance().getEvent().getName(getContext())));
        switch(TimerSettings.getInstance().getHomeInfo()){
            case TimerSettings.HOME_INFO_NOTHING_ID :
                currentHomeContentTV.setText(R.string.settings_timer_home_content_nothing); break;
            case TimerSettings.HOME_INFO_AVERAGES_ID :
                currentHomeContentTV.setText(R.string.settings_timer_home_content_averages); break;
            case TimerSettings.HOME_INFO_PATTERN_ID :
                currentHomeContentTV.setText(R.string.settings_timer_home_content_pattern); break;
            case TimerSettings.HOME_INFO_CHART_LINE_ID :
                currentHomeContentTV.setText(R.string.settings_timer_home_content_chart_line); break;
            case TimerSettings.HOME_INFO_CHART_HISTOGRAM_ID :
                currentHomeContentTV.setText(R.string.settings_timer_home_content_chart_histogram); break;

        }

        inspectionRL.setOnClickListener(clickInspection);
        timerUpdateRL.setOnClickListener(clickTimerUpdate);
        homeContentRL.setOnClickListener(clickHomeContent);
        themeRL.setOnClickListener(clickTheme);
        backgroundRL.setOnClickListener(clickBackground);
        helpScrambleRL.setOnClickListener(clickHelpScramble);
        helpAppRL.setOnClickListener(clickHelpApp);
        contactRL.setOnClickListener(clickContact);
        //supportRL.setOnClickListener(clickSupport);
        rateRL.setOnClickListener(clickRate);

        return rootView;
    }

    private View.OnClickListener clickInspection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final boolean isCheckedAfterClick = !inspectionCB.isChecked();
            inspectionCB.setChecked(isCheckedAfterClick);
            TimerSettings.getInstance().setInspection(isCheckedAfterClick);
        }
    };

    private View.OnClickListener clickTimerUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final String[] timerUpdates = new String[]{
                    getContext().getString(R.string.settings_timer_update_desc_no),
                    getContext().getString(R.string.settings_timer_update_millisecond),
                    getContext().getString(R.string.settings_timer_update_hundredth),
                    getContext().getString(R.string.settings_timer_update_tenth),
                    getContext().getString(R.string.settings_timer_update_second),
            };
            ArrayAdapter listAdapter =
                    new ArrayAdapter(getContext(),
                            android.R.layout.simple_list_item_single_choice, timerUpdates);
            final ListDialogView dialogView = new ListDialogView(
                    getContext(), R.string.settings_timer_update_dialog_title, listAdapter);
            dialogView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            TimerSettings.getInstance().setTimerUpdate(TimerSettings.NO_TIMER_UPDATE);
                            break;
                        case 1:
                           TimerSettings.getInstance().setTimerUpdate(TimerSettings.TIMER_UPDATE_MILLISECOND);
                           break;
                        case 2:
                            TimerSettings.getInstance().setTimerUpdate(TimerSettings.TIMER_UPDATE_HUNDREDTH);
                            break;
                        case 3:
                            TimerSettings.getInstance().setTimerUpdate(TimerSettings.TIMER_UPDATE_TENTH);
                            break;
                        case 4:
                            TimerSettings.getInstance().setTimerUpdate(TimerSettings.TIMER_UPDATE_SECOND);
                            break;
                    }
                    currentTimerUpdateTV.setText(TimerSettings.getInstance().getTimerUpdateStr(getContext()));
                    listDialog.dismiss();
                }
            });
            builder.setView(dialogView);
            builder.setNegativeButton(R.string.cancel, null);
            listDialog = builder.create();
            listDialog.show();
        }
    };

    private View.OnClickListener clickHomeContent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final String[] timerHomeContent = new String[]{
                    getContext().getString(R.string.settings_timer_home_content_averages),
                    getContext().getString(R.string.settings_timer_home_content_pattern),
                    getContext().getString(R.string.settings_timer_home_content_chart_line),
                    getContext().getString(R.string.settings_timer_home_content_chart_histogram),
            };
            ArrayAdapter listAdapter =
                    new ArrayAdapter(getContext(),
                            android.R.layout.simple_list_item_single_choice, timerHomeContent);
            final ListDialogView dialogView = new ListDialogView(
                    getContext(), R.string.settings_timer_home_content_dialog_title, listAdapter);
            dialogView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            TimerSettings.getInstance().setHomeInfo(TimerSettings.HOME_INFO_AVERAGES_ID);
                            break;
                        case 1:
                            TimerSettings.getInstance().setHomeInfo(TimerSettings.HOME_INFO_PATTERN_ID);
                            break;
                        case 2:
                            TimerSettings.getInstance().setHomeInfo(TimerSettings.HOME_INFO_CHART_LINE_ID);
                            break;
                        case 3:
                            TimerSettings.getInstance().setHomeInfo(TimerSettings.HOME_INFO_CHART_HISTOGRAM_ID);
                            break;

                    }
                    currentHomeContentTV.setText(TimerSettings.getInstance().getHomeInfoStr(getContext()));
                    listDialog.dismiss();
                }
            });
            builder.setView(dialogView);
            builder.setNegativeButton(R.string.cancel, null);
            listDialog = builder.create();
            listDialog.show();
        }
    };

    private View.OnClickListener clickTheme = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            spectreDialog = new AlertDialog.Builder(getContext());
            spectreDialogView = new SpectreDialogView(getContext());
            spectreDialog.setView(spectreDialogView);
            spectreDialog.setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TimerSettings.getInstance().setTheme(spectreDialogView.getSelectedTheme());
                    currentThemeTV.setText(TimerSettings.getInstance().getTheme().getNameResId());
                }
            });
            spectreDialog.setNegativeButton(R.string.cancel, null);
            spectreDialog.show();
        }
    };

    private View.OnClickListener clickBackground = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final String[] backgrounds = new String[]{
                    TimerSettings.BACKGROUND_WHITE.getName(getContext()),
                    TimerSettings.BACKGROUND_BLACK.getName(getContext()),
            };
            ArrayAdapter listAdapter =
                    new ArrayAdapter(getContext(),
                            android.R.layout.simple_list_item_single_choice, backgrounds);
            final ListDialogView dialogView = new ListDialogView(
                    getContext(), R.string.settings_dialog_background_title, listAdapter);
            dialogView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0 : TimerSettings.getInstance().setBackground(TimerSettings.BACKGROUND_WHITE); break;
                        case 1 : TimerSettings.getInstance().setBackground(TimerSettings.BACKGROUND_BLACK); break;
                    }
                    currentBackgroundTV.setText(TimerSettings.getInstance().getBackground().getNameResId());
                    listDialog.dismiss();
                }
            });
            builder.setView(dialogView);
            builder.setNegativeButton(R.string.cancel, null);
            listDialog = builder.create();
            listDialog.show();
        }
    };

    private View.OnClickListener clickHelpScramble = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MixPanelHelper.sendHelpScramble(getContext(),
                    ((TimerActivity) getActivity()).getMixpanelInstance(),
                    AppInstance.getInstance().getEvent());

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final TextDialogView dialogView =
                    new TextDialogView(getContext(), R.string.settings_help_scramble_dialog_title);
            switch(AppInstance.getInstance().getEvent().getScrambleTypeId()){
                case ScrambleGenerator.SCRAMBLE_TYPE_2x2 :
                case ScrambleGenerator.SCRAMBLE_TYPE_3x3 :
                case ScrambleGenerator.SCRAMBLE_TYPE_3x3x2 :
                    dialogView.setText(R.string.settings_help_scramble_dialog_2x2_3x3); break;
                case ScrambleGenerator.SCRAMBLE_TYPE_4x4 :
                case ScrambleGenerator.SCRAMBLE_TYPE_5x5 :
                    dialogView.setText(R.string.settings_help_scramble_dialog_4x4_5x5); break;
                case ScrambleGenerator.SCRAMBLE_TYPE_6x6 :
                case ScrambleGenerator.SCRAMBLE_TYPE_7x7 :
                    dialogView.setText(R.string.settings_help_scramble_dialog_6x6_7x7); break;
                case ScrambleGenerator.SCRAMBLE_TYPE_SQUARE1 :
                    dialogView.setText(R.string.settings_help_scramble_dialog_square1); break;
                case ScrambleGenerator.SCRAMBLE_TYPE_SKEWB :
                    dialogView.setText(R.string.settings_help_scramble_dialog_skewb); break;
                case ScrambleGenerator.SCRAMBLE_TYPE_PYRAMINX :
                    dialogView.setText(R.string.settings_help_scramble_dialog_pyraminx); break;
                case ScrambleGenerator.SCRAMBLE_TYPE_MEGAMINX :
                    dialogView.setText(R.string.settings_help_scramble_dialog_megaminx); break;
                case ScrambleGenerator.SCRAMBLE_TYPE_CLOCK :
                    dialogView.setText(R.string.settings_help_scramble_dialog_coming_soon); break;
                default :
                    dialogView.setText(R.string.settings_help_scramble_dialog_no_scramble); break;
            }
            builder.setView(dialogView);
            builder.setNegativeButton(R.string.close, null);
            builder.show();
        }
    };

    private View.OnClickListener clickHelpApp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MixPanelHelper.sendHelpApp(
                    ((TimerActivity) getActivity()).getMixpanelInstance());

            final Intent onBoardingIntent = new Intent(getActivity(), OnboardingActivity.class);
            onBoardingIntent.putExtra(OnboardingActivity.IS_SHOWING_FINAL_PAGE, false);

            startActivity(onBoardingIntent);
            TimerSettings.getInstance().addSettingsListener(settingsListener);

        }
    };

    private View.OnClickListener clickContact = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final ContactDialogView dialogView = new ContactDialogView(getContext());
            builder.setView(dialogView);
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MixPanelHelper.sendContactRequested(
                            ((TimerActivity) getActivity()).getMixpanelInstance(),
                            dialogView.getText());
                }
            });
            listDialog = builder.create();
            listDialog.show();
        }
    };

    private SettingsListener settingsListener = new SettingsListener() {
        @Override
        public void OnInspectionChange(boolean newInspection) {}

        @Override
        public void OnTimerUpdateChange(int newTimerUpdate) {}

        @Override
        public void OnHomeContentChange(int newHomeContentId) {}

        @Override
        public void OnThemeChange(Theme newTheme) {
            currentThemeTV.setText(newTheme.getNameResId());
        }

        @Override
        public void OnBackgroundChange(Theme newBackground) {}
    };

    /*
    private View.OnClickListener clickSupport = new View.OnClickListener() {
        @Override
        public void onClick(View v){
        //AdBuddiz.showAd(getActivity());
        if (AdBuddiz.RewardedVideo.isReadyToShow(getActivity())) {
            AdBuddiz.RewardedVideo.setDelegate(delegate);
            AdBuddiz.RewardedVideo.show(getActivity());
            MixPanelHelper.sendSettingsInfo(
                    ((TimerActivity) getContext()).getMixpanelInstance(),
                    "Ad - ad showing");
        }else{
            AdBuddiz.RewardedVideo.fetch(getActivity());
            Toast.makeText(getContext(), R.string.settings_support_not_loaded,
                    Toast.LENGTH_SHORT).show();
        }

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                MixPanelHelper.sendSettingsInfo(
                        ((TimerActivity)getContext()).getMixpanelInstance(),
                        "Support - ad showing");
            }else{
                MixPanelHelper.sendSettingsInfo(
                        ((TimerActivity) getContext()).getMixpanelInstance(),
                        "Support - attempting to show ad but not loaded");
                Toast.makeText(getContext(), R.string.settings_support_not_loaded,
                        Toast.LENGTH_SHORT).show();
                if(!mInterstitialAd.isLoading()){
                    requestNewInterstitial();
                }
            }

        }
    };

    private AdBuddizRewardedVideoDelegate delegate = new AdBuddizRewardedVideoDelegate(){
        @Override
        public void didFetch() {

        }

        @Override
        public void didFail(AdBuddizRewardedVideoError adBuddizRewardedVideoError) {
            MixPanelHelper.sendSettingsInfo(
                    ((TimerActivity) getContext()).getMixpanelInstance(),
                    "Ad - ad error : " + adBuddizRewardedVideoError.getName());
        }

        @Override
        public void didComplete() {
            Toast.makeText(getContext(), R.string.settings_support_thanks, Toast.LENGTH_SHORT).show();
            AdBuddiz.RewardedVideo.fetch(getActivity());
            MixPanelHelper.sendSettingsInfo(
                    ((TimerActivity) getContext()).getMixpanelInstance(),
                    "Ad - ad shown completely");
        }

        @Override
        public void didNotComplete() {
            MixPanelHelper.sendSettingsInfo(
                    ((TimerActivity) getContext()).getMixpanelInstance(),
                    "Ad - ad shown incompletely");
        }
    };
*/

    private View.OnClickListener clickRate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //Try Google play
            intent.setData(Uri.parse("market://details?id=letsapps.com.letscube"));
            MixPanelHelper.sendSettingsInfo(
                    ((TimerActivity) getContext()).getMixpanelInstance(),
                    "Rate - app store launched");
            if (!tryIntent(intent)) {
                MixPanelHelper.sendSettingsInfo(
                        ((TimerActivity) getContext()).getMixpanelInstance(),
                        "Rate - browser launched");
                //Market (Google play) app seems not installed, let's try to open a webbrowser
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?letsapps.com.letscube"));
                if (!tryIntent(intent)) {
                    MixPanelHelper.sendSettingsInfo(
                            ((TimerActivity) getContext()).getMixpanelInstance(),
                            "Rate - nothing launched");
                    //Well if this also fails, we have run out of options, inform the user.
                    Toast.makeText(getContext(),
                            R.string.settings_rate_impossible, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private boolean tryIntent(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }
/*
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
*/

    @Override
    public void onDestroy(){
        super.onDestroy();
        TimerSettings.getInstance().removeSettingsListener(settingsListener);
    }

}

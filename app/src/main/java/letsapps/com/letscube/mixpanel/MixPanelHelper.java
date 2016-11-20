package letsapps.com.letscube.mixpanel;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import letsapps.com.letscube.activity.TimerActivity;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.DatabaseTime;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.util.User;
import letsapps.com.letscube.util.cube.Event;
import letsapps.com.letscube.util.scramble.ScrambleGenerator;

public class MixPanelHelper {
    public static final String PROJECT_TOKEN = "579f1b8aecbb6b723c7f24f2aa1533cc";
    public static final boolean IS_MIXPANEL_TRACKING = true;

    public static void sendEventChanged(
            Context context, MixpanelAPI mixpanelInstance, Event previousEvent, Event newEvent){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("PreviousEvent", previousEvent.getName(context));
            props.put("NewEvent", newEvent.getName(context));
            props.put("isChangement", previousEvent.getId() != newEvent.getId());
            mixpanelInstance.track("EventChanged", props);
        } catch (JSONException e) {
            Log.e("MPH (eventChange)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendEventAdded(
            Context context, MixpanelAPI mixpanelInstance, Event addedEvent){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Event", addedEvent.getName(context));
            props.put("ScrambleType", ScrambleGenerator.getScrambleTypeName(
                    context, addedEvent.getScrambleTypeId()));
            mixpanelInstance.track("EventAdded", props);
        } catch (JSONException e) {
            Log.e("MPH (eventAdd)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendTimeTimed(
            MixpanelAPI mixpanelInstance, Context context, DatabaseTime time, String comment){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        mixpanelInstance.getPeople().increment("nbTimesTimed", 1);

        try {
            JSONObject props = new JSONObject();
            props.put("Event", time.getEvent().getName(context));
            props.put("TimeMs", time.getTime());
            props.put("Theme", TimerSettings.getInstance().getTheme().getStrId());
            props.put("Background", TimerSettings.getInstance().getBackground().getStrId());
            props.put("Orientation",
                    context.getResources().getConfiguration().orientation ==
                            Configuration.ORIENTATION_LANDSCAPE ? "LANDSCAPE" : "PORTRAIT");
            if(comment == null){
                comment = "-";
            }
            props.put("Comment", comment);

            switch(TimerSettings.getInstance().getHomeInfo()){
                case TimerSettings.HOME_INFO_NOTHING_ID : props.put("HomeContent", "NOTHING");
                    break;
                case TimerSettings.HOME_INFO_AVERAGES_ID : props.put("HomeContent", "AVERAGES");
                    break;
                case TimerSettings.HOME_INFO_PATTERN_ID : props.put("HomeContent", "PATTERN");
                    break;
                case TimerSettings.HOME_INFO_CHART_HISTOGRAM_ID : props.put("HomeContent", "CHART DISTRIBUTION");
                    break;
                case TimerSettings.HOME_INFO_CHART_LINE_ID : props.put("HomeContent", "CHART LINE");
                    break;
            }
            props.put("IsInspection", TimerSettings.getInstance().isInspection());
            props.put("TimerUpdate", TimerSettings.getInstance().getTimerUpdate());

            mixpanelInstance.track("TimeTimed", props);
        } catch (JSONException e) {
            Log.e("MPH (timeTimed)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendTimeAddedManually(
            Context context, DatabaseTime time){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        final MixpanelAPI mixpanelInstance = ((TimerActivity)context).getMixpanelInstance();

        try {
            JSONObject props = new JSONObject();
            props.put("Event", time.getEvent().getName(context));
            props.put("TimeMs", time.getTime());
            mixpanelInstance.track("TimeAddedManually", props);
        } catch (JSONException e) {
            Log.e("MPH (timeAddedMan)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendTimePenaltyEdited(
            Context context, MixpanelAPI mixpanelInstance, DatabaseTime time){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Event", time.getEvent().getName(context));
            props.put("TimeMs", time.getTime());
            switch (time.getPenalty()){
                case DatabaseTime.NO_PENALTY : props.put("Penalty", "-"); break;
                case DatabaseTime.PENALTY_2 : props.put("Penalty", "+2"); break;
                case DatabaseTime.PENALTY_DNF : props.put("Penalty", "DNF"); break;
            }

            mixpanelInstance.track("TimePenaltyEdited", props);
        } catch (JSONException e) {
            Log.e("MPH (penaltyEdit)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendEventTimesDeleted(
            Context context, MixpanelAPI mixpanelInstance, Event event, int nbDeletedTimes){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Event", event.getName(context));
            props.put("NbDeletedTimes", nbDeletedTimes);

            mixpanelInstance.track("EventTimesDeleted", props);
        } catch (JSONException e) {
            Log.e("MPH (evTimesDel.)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendMultipleDeletion(MixpanelAPI mixpanelInstance, int nbRemovedTimes,
                                                  String eventName){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Event", eventName);
            props.put("NbDeletedTimes", nbRemovedTimes);

            mixpanelInstance.track("MultipleTimesDeleted", props);
        } catch (JSONException e) {
            Log.e("MPH (evTimesDel.)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendSingleTimeDeleted(Context context, DatabaseTime time){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Event", time.getEvent().getName(context));
            props.put("TimeMs", time.getTime());

            ((TimerActivity)context).getMixpanelInstance().track("SingleTimeDeleted", props);
        } catch (JSONException e) {
            Log.e("MPH (evTimesDel.)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendInspectionChanged(
            MixpanelAPI mixpanelInstance){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("IsInspection", TimerSettings.getInstance().isInspection());
            mixpanelInstance.track("InspectionChanged", props);
        } catch (JSONException e) {
            Log.e("MPH (inspecChange)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendTimerUpdateChanged(
            MixpanelAPI mixpanelInstance){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("TimerUpdate", TimerSettings.getInstance().getTimerUpdate());
            mixpanelInstance.track("TimerUpdateChanged", props);
        } catch (JSONException e) {
            Log.e("MPH (timerUpdChange)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendHomeContentChanged(
            MixpanelAPI mixpanelInstance){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            switch(TimerSettings.getInstance().getHomeInfo()){
                case TimerSettings.HOME_INFO_NOTHING_ID : props.put("HomeContent", "NOTHING");
                    break;
                case TimerSettings.HOME_INFO_AVERAGES_ID : props.put("HomeContent", "AVERAGES");
                    break;
                case TimerSettings.HOME_INFO_PATTERN_ID : props.put("HomeContent", "PATTERN");
                    break;
                case TimerSettings.HOME_INFO_CHART_HISTOGRAM_ID : props.put("HomeContent", "CHART DISTRIBUTION");
                    break;
                case TimerSettings.HOME_INFO_CHART_LINE_ID : props.put("HomeContent", "CHART LINE");
                    break;
            }
            mixpanelInstance.track("HomeContentChanged", props);
        } catch (JSONException e) {
            Log.e("MPH (homeInfoChange)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendThemeChanged(
            Context context, MixpanelAPI mixpanelInstance){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Theme", TimerSettings.getInstance().getTheme().getStrId());

            mixpanelInstance.track("ThemeChanged", props);
        } catch (JSONException e) {
            Log.e("MPH (themeChange)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendBackgroundChanged(
            Context context, MixpanelAPI mixpanelInstance){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            if(TimerSettings.getInstance().getBackground().getId() == TimerSettings.BACKGROUND_WHITE.getId()){
                props.put("Background", "WHITE");
            }else if(TimerSettings.getInstance().getBackground().getId() == TimerSettings.BACKGROUND_BLACK.getId()){
                props.put("Background", "BLACK");
            }

            mixpanelInstance.track("BackgroundChanged", props);
        } catch (JSONException e) {
            Log.e("MPH (backgrndChange)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendHelpScramble(
            Context context, MixpanelAPI mixpanelInstance, Event event){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Event", event.getName(context));
            mixpanelInstance.track("HelpScrambleOpened", props);
        } catch (JSONException e) {
            Log.e("MPH (helpScramble)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendOnboardingInfo(
            MixpanelAPI mixpanelInstance, String choosenTheme, String appDiscovering){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("OnboardingTheme", choosenTheme);
            props.put("OnboardingDiscover", appDiscovering);
            mixpanelInstance.track("OnboardingInfo", props);
        } catch (JSONException e) {
            Log.e("MPH (onboardInfo.)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void sendHelpApp(MixpanelAPI mixpanelInstance){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        mixpanelInstance.track("HelpAppOpened");

    }

    public static void sendContactRequested(MixpanelAPI mixpanelInstance, String contactContent){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("ContactContent", contactContent);
            mixpanelInstance.track("ContactRequested", props);
        } catch (JSONException e) {
            Log.e("MPH (contact)", "Unable to add properties to JSONObject", e);
        }

    }

    public static void setUserProperties(MixpanelAPI mixpanelInstance, User user){

        if(!IS_MIXPANEL_TRACKING){
            return;
        }
        Log.i("MPH (setUserProp)", "user properties : " + user.getName() + " | " +
                user.getEmail() + " | " + user.getMixpanelID());
        mixpanelInstance.alias(user.getMixpanelID(), null);
        mixpanelInstance.identify(user.getMixpanelID());
        mixpanelInstance.getPeople().identify(user.getMixpanelID());
        mixpanelInstance.getPeople().set("$name", user.getName());
        mixpanelInstance.getPeople().set("$email", user.getEmail());
        mixpanelInstance.getPeople().set("nbTimesTimed", 0);

        try {
            JSONObject props = new JSONObject();
            props.put("Username", user.getName());
            props.put("Email", user.getEmail());
            mixpanelInstance.track("Logged", props);
        } catch (JSONException e) {
            Log.e("MPH (logged)", "Unable to add properties to JSONObject", e);
        }
    }

    public static void sendLoginActivity(MixpanelAPI mixpanelInstance, String activity){
        if(!IS_MIXPANEL_TRACKING){
            return;
        }
        try {
            JSONObject props = new JSONObject();
            props.put("Activity", activity);
            mixpanelInstance.track("LoginActivity", props);
        } catch (JSONException e) {
            Log.e("MPH (loginAct.)", "Unable to add properties to JSONObject", e);
        }
    }

    public static void sendSettingsInfo(MixpanelAPI mixpanelInstance, String info){
        if(!IS_MIXPANEL_TRACKING){
            return;
        }
        try {
            JSONObject props = new JSONObject();
            props.put("Info", info);
            mixpanelInstance.track("SettingsInfo", props);
        } catch (JSONException e) {
            Log.e("MPH (sett.Info)", "Unable to add properties to JSONObject", e);
        }
    }

    public static MixpanelAPI getInstance(Context context){

        if(!IS_MIXPANEL_TRACKING){
            return null;
        }

        final MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, PROJECT_TOKEN);
        return mixpanel;
    }
}
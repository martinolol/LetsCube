package letsapps.com.letscube.singleton;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import letsapps.com.letscube.R;
import letsapps.com.letscube.listener.SettingsListener;
import letsapps.com.letscube.util.Theme;

public class TimerSettings {

    public static final int SETTINGS_INSPECTION_ID = 0x11;
    public static final int SETTINGS_TIMER_UPDATE_ID = 0x12;
    public static final int SETTINGS_HOME_INFO_ID = 0x13;
    public static final int SETTINGS_THEME_ID = 0x31;
    public static final int SETTINGS_BACKGROUND_ID = 0x32;

    public static final int INSPECTION_TIME_MS = 15000;

    public static final Theme[] THEMES = {
            new Theme(0x2, "BLUE", R.string.settings_theme_blue, "#00A0FF", "#FFFFBB"),
            new Theme(0x5, "CYAN", R.string.settings_theme_cyan, "#30CCFF", "#FFFFBB"),
            new Theme(0x3, "GREEN", R.string.settings_theme_green, "#0ACC10", "#FFFFBB"),
            new Theme(0x6, "GREEN_LIGHT", R.string.settings_theme_green_light, "#20E011", "#FFFFBB"),
            new Theme(0x4, "YELLOW", R.string.settings_theme_yellow, "#DADA00", "#FFFFBB"),
            new Theme(0x7, "ORANGE_LIGHT", R.string.settings_theme_orange_light, "#FAD020", "#FFFFBB"),
            new Theme(0x1, "ORANGE", R.string.settings_theme_orange, "#FFA400", "#FFFFBB"),
            new Theme(0x8, "RED", R.string.settings_theme_red, "#DA3030", "#FFFFBB"),
    };

    public static final Theme BACKGROUND_WHITE =
            new Theme(0x1, "WHITE", R.string.settings_background_white, "#FFFFFF", "#000000");
    public static final Theme BACKGROUND_BLACK =
            new Theme(0x2, "BLACK", R.string.settings_background_black, "#000000", "#FFFFFF");

    public static final int NO_TIMER_UPDATE = 0;
    public static final int TIMER_UPDATE_MILLISECOND = 1;
    public static final int TIMER_UPDATE_HUNDREDTH = 10;
    public static final int TIMER_UPDATE_TENTH = 100;
    public static final int TIMER_UPDATE_SECOND = 1000;

    public static final int HOME_INFO_NOTHING_ID = 0x0;
    public static final int HOME_INFO_AVERAGES_ID = 0x1;
    public static final int HOME_INFO_PATTERN_ID = 0x2;
    public static final int HOME_INFO_CHART_LINE_ID = 0x3;
    public static final int HOME_INFO_CHART_HISTOGRAM_ID = 0x4;

    boolean isInspection;
    int timerUpdate;
    int homeInfo;
    Theme theme;
    Theme background;

    List<SettingsListener> settingsListeners;

    public static TimerSettings instance = null;

    private TimerSettings() {
        settingsListeners = new ArrayList<>();
    }

    public static TimerSettings getInstance(){
        if(instance == null){
            instance = new TimerSettings();
        }
        return instance;
    }

    public void addSettingsListener(SettingsListener settingsListener){
        settingsListeners.add(settingsListener);
    }

    public void removeSettingsListener(SettingsListener settingsListener){
        if (settingsListeners.contains(settingsListener)) {
            settingsListeners.remove(settingsListener);
        }
    }

    public boolean isInspection() {
        return isInspection;
    }

    public void setInspection(boolean isInspection) {
        this.isInspection = isInspection;
        for(SettingsListener settingsListener : settingsListeners){
            settingsListener.OnInspectionChange(isInspection);
        }
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme){
        this.theme = theme;
        for(SettingsListener settingsListener : settingsListeners){
            settingsListener.OnThemeChange(theme);
        }
    }

    public Theme getBackground() {
        return background;
    }

    public void setBackground(Theme background){
        this.background = background;
        for(SettingsListener settingsListener : settingsListeners){
            settingsListener.OnBackgroundChange(background);
        }
    }

    public int getTimerUpdate() {
        return timerUpdate;
    }

    public String getTimerUpdateStr(Context context) {
        switch (timerUpdate){
            case NO_TIMER_UPDATE : return context.getString(R.string.settings_timer_update_desc_no);
            case TIMER_UPDATE_MILLISECOND:
                return context.getString(R.string.settings_timer_update_millisecond);
            case TIMER_UPDATE_HUNDREDTH :
                return context.getString(R.string.settings_timer_update_hundredth);
            case TIMER_UPDATE_TENTH :
                return context.getString(R.string.settings_timer_update_tenth);
            case TIMER_UPDATE_SECOND :
                return context.getString(R.string.settings_timer_update_second);
            default : return null;
        }
    }

    public void setTimerUpdate(int timerUpdate) {
        this.timerUpdate = timerUpdate;
        for(SettingsListener settingsListener : settingsListeners){
            settingsListener.OnTimerUpdateChange(timerUpdate);
        }
    }

    public void setSetting(int settingId, int intValue, String strValue){
        switch (settingId){
            case SETTINGS_INSPECTION_ID : setInspection(integerToBoolean(intValue)); break;
            case SETTINGS_THEME_ID :
                for(int i = 0; i < THEMES.length; i++){
                    if(THEMES[i].getId() == intValue){
                        setTheme(THEMES[i]);
                        break;
                    }
                } break;
            case SETTINGS_BACKGROUND_ID :
                if(intValue == BACKGROUND_BLACK.getId()){
                    setBackground(BACKGROUND_BLACK);
                }else if(intValue == BACKGROUND_WHITE.getId()){
                    setBackground(BACKGROUND_WHITE);
                } break;
            case SETTINGS_TIMER_UPDATE_ID : setTimerUpdate(intValue);
            case SETTINGS_HOME_INFO_ID : setHomeInfo(intValue);
        }
    }

    public int getHomeInfo() {
        return homeInfo;
    }

    public void setHomeInfo(int homeInfo) {
        this.homeInfo = homeInfo;
        for(SettingsListener settingsListener : settingsListeners){
            settingsListener.OnHomeContentChange(homeInfo);
        }
    }

    public String getHomeInfoStr(Context context){
        switch (homeInfo){
            case HOME_INFO_NOTHING_ID : return context.getString(R.string.settings_timer_home_content_nothing);
            case HOME_INFO_AVERAGES_ID :
                return context.getString(R.string.settings_timer_home_content_averages);
            case HOME_INFO_PATTERN_ID :
                return context.getString(R.string.settings_timer_home_content_pattern);
            case HOME_INFO_CHART_LINE_ID :
                return context.getString(R.string.settings_timer_home_content_chart_line);
            case HOME_INFO_CHART_HISTOGRAM_ID :
                return context.getString(R.string.settings_timer_home_content_chart_histogram);
            default : return null;
        }
    }

    private int booleanToInteger(boolean b){
        return b ? 1 : 0;
    }

    private boolean integerToBoolean(int i){
        return i == 0 ? false : true;
    }
}

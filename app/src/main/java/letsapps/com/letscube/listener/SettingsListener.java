package letsapps.com.letscube.listener;

import letsapps.com.letscube.util.Theme;

public interface SettingsListener {
    void OnInspectionChange(boolean newInspection);
    void OnTimerUpdateChange(int newTimerUpdate);
    void OnHomeContentChange(int newHomeContentId);
    void OnThemeChange(Theme newTheme);
    void OnBackgroundChange(Theme newBackground);
}

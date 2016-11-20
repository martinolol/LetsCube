package letsapps.com.letscube.util;

/**
 * Created by Martin on 23/02/2016.
 */
public class User {

    public static String APP_DISCOVER_YOUTUBE = "YOUTUBE";
    public static String APP_DISCOVER_FORUM = "FORUM";
    public static String APP_DISCOVER_ACQUAINTANCE = "ACQUAINTANCE";
    public static String APP_DISCOVER_PLAYSTORE = "PLAYSTORE";
    public static String APP_DISCOVER_OTHER = "OTHER";

    public String mixpanelID, name, email;
    public String appDiscover;

    public User() {
    }

    public String getMixpanelID() {
        return mixpanelID;
    }

    public void setMixpanelID(String mixpanelID) {
        this.mixpanelID = mixpanelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppDiscover() {
        return appDiscover;
    }

    public void setAppDiscover(String appDiscover) {
        this.appDiscover = appDiscover;
    }
}

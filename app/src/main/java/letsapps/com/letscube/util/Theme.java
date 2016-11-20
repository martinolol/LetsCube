package letsapps.com.letscube.util;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Martin on 17/02/2016.
 */
public class Theme {

    int id;
    String strId; // for mixpanel constants
    int nameResId;
    int mainColor;
    int secundaryColor;

    public Theme(int id, String strId, int nameResId, String mainColor, String secundaryColor) {
        this.id = id;
        this.strId = strId;
        this.nameResId = nameResId;
        if(mainColor != null) {
            this.mainColor = Color.parseColor(mainColor);
        }
        if(secundaryColor != null) {
            this.secundaryColor = Color.parseColor(secundaryColor);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public int getNameResId() {
        return nameResId;
    }

    public String getName(Context context){
        return context.getString(nameResId);
    }

    public void setNameResId(int nameResId) {
        this.nameResId = nameResId;
    }

    public int getMainColor() {
        return mainColor;
    }

    public void setMainColor(int mainColor) {
        this.mainColor = mainColor;
    }

    public int getSecundaryColor() {
        return secundaryColor;
    }

    public void setSecundaryColor(int secundaryColor) {
        this.secundaryColor = secundaryColor;
    }
}

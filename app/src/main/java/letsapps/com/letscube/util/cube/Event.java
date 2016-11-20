package letsapps.com.letscube.util.cube;

import android.content.Context;

public class Event {
    private int id;
    private String name;
    private int nameResId;
    private int pictureResId;
    private int scrambleTypeId;
    private boolean isOfficial;

    public Event(){};

    public Event(int id, int nameResId, int pictureResId, int scrambleTypeId, boolean isOfficial) {
        this.id = id;
        this.nameResId = nameResId;
        this.pictureResId = pictureResId;
        this.scrambleTypeId = scrambleTypeId;
        this.isOfficial = isOfficial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNameResId() {
        return nameResId;
    }

    public String getName(Context context) {
        if(nameResId != 0) {
            return context.getString(nameResId);
        }else{
            return getName();
        }
    }

    public void setNameResId(int nameResId) {
        this.nameResId = nameResId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getPictureResId() {
        return pictureResId;
    }

    public void setPictureResId(int pictureResId) {
        this.pictureResId = pictureResId;
    }

    public int getScrambleTypeId() {
        return scrambleTypeId;
    }

    public void setScrambleTypeId(int scrambleTypeId) {
        this.scrambleTypeId = scrambleTypeId;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }
}
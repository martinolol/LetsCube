package letsapps.com.letscube.util;

import android.content.Context;

import letsapps.com.letscube.R;

public class LoadingNoteGenerator {

    static final int[] notesId = {
            R.string.note_biggest_cube,
            R.string.note_combinaisons_3x3,
            R.string.note_combinaisons_7x7,
            R.string.note_god_3x3x3,
            R.string.note_world_championship,
            R.string.note_wr_3x3,
            R.string.note_wr_mirror
    };

    public static String getRandomNote(Context context){
        int random = (int)(Math.random() * notesId.length);
        return context.getResources().getString(notesId[random]);
    }
}

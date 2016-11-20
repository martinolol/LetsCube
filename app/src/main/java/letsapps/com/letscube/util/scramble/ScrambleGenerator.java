package letsapps.com.letscube.util.scramble;


import android.content.Context;

import letsapps.com.letscube.R;
import letsapps.com.letscube.util.cube.Event;

public class ScrambleGenerator {

    public static final int NO_SCRAMBLE_TYPE = 0x0;
    public static final int SCRAMBLE_TYPE_2x2 = 0x1;
    public static final int SCRAMBLE_TYPE_3x3 = 0x2;
    public static final int SCRAMBLE_TYPE_4x4 = 0x3;
    public static final int SCRAMBLE_TYPE_5x5 = 0x4;
    public static final int SCRAMBLE_TYPE_6x6 = 0x5;
    public static final int SCRAMBLE_TYPE_7x7 = 0x6;
    public static final int SCRAMBLE_TYPE_SQUARE1 = 0x7;
    public static final int SCRAMBLE_TYPE_SKEWB = 0x8;
    public static final int SCRAMBLE_TYPE_PYRAMINX = 0x9;
    public static final int SCRAMBLE_TYPE_MEGAMINX = 0xA;
    public static final int SCRAMBLE_TYPE_CLOCK = 0xB;
    public static final int SCRAMBLE_TYPE_3x3x2 = 0x11;

    // OFFICIAL EVENTS
    private static final int nbScrambleMoves2x2 = 10;
    private static final int nbScrambleMoves3x3 = 26;
    private static final int nbScrambleMoves4x4 = 40;
    private static final int nbScrambleMoves5x5 = 60;
    private static final int nbScrambleMoves6x6 = 75; // officialy 80
    private static final int nbScrambleMoves7x7 = 85; // officialy 100
    private static final int nbScrambleMovesPyra = 14;
    private static final int nbScrambleMovesSkewb = 10;
    private static final int nbScrambleMovesSquare1 = 13;
    private static final int nbScrambleMovesMegaminx = 7 * MegaminxScrambler.U_INTERVAL - 1;
    private static final int nbScrambleMovesClock = 14;

    // UNOFFICIAL EVENTS
    private static final int nbScrambleMoves3x3x2 = 15;

    public static final String getScramble(Event cubeEvent){
        switch (cubeEvent.getScrambleTypeId()) {

            // OFFICIAL SCRAMBLES
            case SCRAMBLE_TYPE_2x2:
                return Cube2Scrambler.generateEncodedScramble(nbScrambleMoves2x2);

            case SCRAMBLE_TYPE_3x3:
                return Cube3Scrambler.generateEncodedScramble(nbScrambleMoves3x3);

            case SCRAMBLE_TYPE_4x4:
                return Cube4Scrambler.generateEncodedScramble(nbScrambleMoves4x4);

            case SCRAMBLE_TYPE_5x5:
                return Cube5Scrambler.generateEncodedScramble(nbScrambleMoves5x5);

            case SCRAMBLE_TYPE_6x6:
                return Cube6Scrambler.generateEncodedScramble(nbScrambleMoves6x6);

            case SCRAMBLE_TYPE_7x7:
                return Cube7Scrambler.generateEncodedScramble(nbScrambleMoves7x7);

            case SCRAMBLE_TYPE_PYRAMINX:
                return PyraminxScrambler.generateEncodedScramble(nbScrambleMovesPyra);

            case SCRAMBLE_TYPE_SKEWB:
                return SkewbScrambler.generateEncodedScramble(nbScrambleMovesSkewb);

            case SCRAMBLE_TYPE_SQUARE1:
                return Square1Scrambler.generateSquare1Scramble(nbScrambleMovesSquare1);

            case SCRAMBLE_TYPE_CLOCK:
                return ClockScrambler.generateEncodedScramble(nbScrambleMovesClock);

            case SCRAMBLE_TYPE_MEGAMINX:
                return MegaminxScrambler.generateEncodedScramble(nbScrambleMovesMegaminx);

            // UNOFFICIAL SCRAMBLES
            case SCRAMBLE_TYPE_3x3x2:
                return Cube3x3x2Scrambler.generateEncodedScramble(nbScrambleMoves3x3x2);

            default :
                return null;
        }

    }

    public static final String[] getScrambleTypesStr(Context context){
        return new String[]{
                context.getString(R.string.event_2x2),
                context.getString(R.string.event_3x3),
                context.getString(R.string.event_4x4),
                context.getString(R.string.event_5x5),
                context.getString(R.string.event_6x6),
                context.getString(R.string.event_7x7),
                context.getString(R.string.event_square1),
                context.getString(R.string.event_skewb),
                context.getString(R.string.event_pyraminx),
                context.getString(R.string.event_megaminx),
                context.getString(R.string.event_clock),
                context.getString(R.string.event_3x3x2),
                context.getString(R.string.scramble_none),
        };
    }

    public static final int getScrambleTypeId(Context context, String scrambleTypeStrToFind){
        final String[] str = getScrambleTypesStr(context);
        if(scrambleTypeStrToFind.equals(str[0])){
            return SCRAMBLE_TYPE_2x2;
        }else if(scrambleTypeStrToFind.equals(str[1])){
            return SCRAMBLE_TYPE_3x3;
        }else if(scrambleTypeStrToFind.equals(str[2])){
            return SCRAMBLE_TYPE_4x4;
        }else if(scrambleTypeStrToFind.equals(str[3])){
            return SCRAMBLE_TYPE_5x5;
        }else if(scrambleTypeStrToFind.equals(str[4])){
            return SCRAMBLE_TYPE_6x6;
        }else if(scrambleTypeStrToFind.equals(str[5])){
            return SCRAMBLE_TYPE_7x7;
        }else if(scrambleTypeStrToFind.equals(str[6])){
            return SCRAMBLE_TYPE_SQUARE1;
        }else if(scrambleTypeStrToFind.equals(str[7])){
            return SCRAMBLE_TYPE_SKEWB;
        }else if(scrambleTypeStrToFind.equals(str[8])){
            return SCRAMBLE_TYPE_PYRAMINX;
        }else if(scrambleTypeStrToFind.equals(str[9])){
            return SCRAMBLE_TYPE_MEGAMINX;
        }else if(scrambleTypeStrToFind.equals(str[10])){
            return SCRAMBLE_TYPE_SKEWB;
        }else if(scrambleTypeStrToFind.equals(str[11])){
            return SCRAMBLE_TYPE_3x3x2;
        }else{
            return NO_SCRAMBLE_TYPE;
        }
    }

    public static final String getScrambleTypeName(Context context, int scrambleTypeId){
        final String[] scrambleTypesStr = getScrambleTypesStr(context);
        for(String str : scrambleTypesStr){
            if(getScrambleTypeId(context, str) == scrambleTypeId){
                return str;
            }
        }
        return null;
    }

}

package letsapps.com.letscube.util.Clock;

/**
 * Created by Martin on 04/04/2016.
 */
public class ClockNotations {
    public static final int FILTER_FACE = 0xF0;
    public static final int FILTER_TURN_TYPE = 0x0F;

    //0x{first parameter: face}{second parameter: turn type}
    public static final int NO_ROTATION = 0x00;

    public static final int L = 0x11;
    public static final int DL = 0x12;
    public static final int D = 0x13;
    public static final int DR = 0x14;
    public static final int R = 0x15;
    public static final int UR = 0x16;
    public static final int U = 0x17;
    public static final int UL = 0x18;
    public static final int ALL = 0x19;
    public static final int y2 = 0x21;

    public static String getTipNotation(ClockMove move){
        switch (move.getTip()){
            case L: return "L";
            case DL: return "DL";
            case D: return "D";
            case DR: return "DR";
            case R: return "R";
            case UR: return "UR";
            case U: return "U";
            case UL: return "UL";
            case ALL: return "ALL";
            case y2: return "y2";
            default: return null;
        }
    }

    public static String getNotation(ClockMove move){
        if(move.getTip() == y2){
            return getTipNotation(move);
        }
        if(move.getTurn() > 0){
            return getTipNotation(move) + move.getTurn() + "+";
        }else{
            return getTipNotation(move) + (-move.getTurn()) + "-";
        }
    }

    public static String encodeWithSpaces(ClockMove[] moves){

        String encodedScramble = "";

        for(ClockMove move : moves){
            encodedScramble += getNotation(move) + " ";
        }

        return encodedScramble;
    }
}

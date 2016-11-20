package letsapps.com.letscube.util.cube;

import android.util.Log;

import java.util.ArrayList;

public abstract class CubeNotations {

    public static final int FILTER_FACE = 0xF0;
    public static final int FILTER_TURN_TYPE = 0x0F;

    //0x{first parameter: face}{second parameter: turn type}
    public static final int NO_ROTATION = 0x00;

    public static final int U = 0x11;
    public static final int U_CCW = 0x12;
    public static final int U_2 = 0x13;
    public static final int Uw = 0x14;
    public static final int Uw_CCW = 0x15;
    public static final int Uw_2 = 0x16;
    public static final int U3w = 0x17;
    public static final int U3w_CCW = 0x18;
    public static final int U3w_2 = 0x19;
    public static final int u = 0x1A;
    public static final int u_CCW = 0x1B;

    public static final int D = 0x21;
    public static final int D_CCW = 0x22;
    public static final int D_2 = 0x23;
    public static final int Dw = 0x24;
    public static final int Dw_CCW = 0x25;
    public static final int Dw_2 = 0x26;
    public static final int D3w = 0x27;
    public static final int D3w_CCW = 0x28;
    public static final int D3w_2 = 0x29;

    public static final int R = 0x31;
    public static final int R_CCW = 0x32;
    public static final int R_2 = 0x33;
    public static final int Rw = 0x34;
    public static final int Rw_CCW = 0x35;
    public static final int Rw_2 = 0x36;
    public static final int R3w = 0x37;
    public static final int R3w_CCW = 0x38;
    public static final int R3w_2 = 0x39;
    public static final int r = 0x3A;
    public static final int r_CCW = 0x3B;

    public static final int L = 0x41;
    public static final int L_CCW = 0x42;
    public static final int L_2 = 0x43;
    public static final int Lw = 0x44;
    public static final int Lw_CCW = 0x45;
    public static final int Lw_2 = 0x46;
    public static final int L3w = 0x47;
    public static final int L3w_CCW = 0x48;
    public static final int L3w_2 = 0x49;
    public static final int l = 0x4A;
    public static final int l_CCW = 0x4B;

    public static final int F = 0x51;
    public static final int F_CCW = 0x52;
    public static final int F_2 = 0x53;
    public static final int Fw = 0x54;
    public static final int Fw_CCW = 0x55;
    public static final int Fw_2 = 0x56;
    public static final int F3w = 0x57;
    public static final int F3w_CCW = 0x58;
    public static final int F3w_2 = 0x59;

    public static final int B = 0x61;
    public static final int B_CCW = 0x62;
    public static final int B_2 = 0x63;
    public static final int Bw = 0x64;
    public static final int Bw_CCW = 0x65;
    public static final int Bw_2 = 0x66;
    public static final int B3w = 0x67;
    public static final int B3w_CCW = 0x68;
    public static final int B3w_2 = 0x69;
    public static final int b = 0x6A;
    public static final int b_CCW = 0x6B;

    public static final int M = 0x71;
    public static final int M_CCW = 0x72;
    public static final int M_2 = 0x73;
    public static final int E = 0x81;
    public static final int E_CCW = 0x82;
    public static final int E_2 = 0x83;
    public static final int S = 0x91;
    public static final int S_CCW = 0x92;
    public static final int S_2 = 0x93;

    public static final int x = 0xA1;
    public static final int x_CCW = 0xA2;
    public static final int x_2 = 0xA3;
    public static final int y = 0xB1;
    public static final int y_CCW = 0xB2;
    public static final int y_2 = 0xB3;
    public static final int z = 0xC1;
    public static final int z_CCW = 0xC2;
    public static final int z_2 = 0xC3;

    public static final int R2plus = 0xD1;
    public static final int R2minus = 0xD2;
    public static final int D2plus = 0xE1;
    public static final int D2minus = 0xE2;

    public static String getNotation(int rotationId){
        switch (rotationId){

            case U :        return "U";
            case U_CCW :    return "U'";
            case U_2 :    return "U2";
            case Uw :    return "Uw";
            case Uw_CCW :    return "Uw'";
            case Uw_2 :    return "Uw2";
            case U3w :    return "3Uw";
            case U3w_CCW :    return "3Uw'";
            case U3w_2 :    return "3Uw2";
            case u :        return "u";
            case u_CCW :    return "u'";

            case D :        return "D";
            case D_CCW :    return "D'";
            case D_2 :    return "D2";
            case Dw :    return "Dw";
            case Dw_CCW :    return "Dw'";
            case Dw_2 :    return "Dw2";
            case D3w :    return "3Dw";
            case D3w_CCW :    return "3Dw'";
            case D3w_2 :    return "3Dw2";

            case R :        return "R";
            case R_CCW :    return "R'";
            case R_2 :    return "R2";
            case Rw :    return "Rw";
            case Rw_CCW :    return "Rw'";
            case Rw_2 :    return "Rw2";
            case R3w :    return "3Rw";
            case R3w_CCW :    return "3Rw'";
            case R3w_2 :    return "3Rw2";
            case r :       return "r";
            case r_CCW :   return "r'";

            case L :        return "L";
            case L_CCW :    return "L'";
            case L_2 :    return "L2";
            case Lw :    return "Lw";
            case Lw_CCW :    return "Lw'";
            case Lw_2 :    return "Lw2";
            case L3w :    return "3Lw";
            case L3w_CCW :    return "3Lw'";
            case L3w_2 :    return "3Lw2";
            case l :       return "l";
            case l_CCW :   return "l'";

            case F :        return "F";
            case F_CCW :    return "F'";
            case F_2 :    return "F2";
            case Fw :    return "Fw";
            case Fw_CCW :    return "Fw'";
            case Fw_2 :    return "Fw2";
            case F3w :    return "3Fw";
            case F3w_CCW :    return "3Fw'";
            case F3w_2 :    return "3Fw2";

            case B :        return "B";
            case B_CCW :    return "B'";
            case B_2 :    return "B2";
            case Bw :    return "Bw";
            case Bw_CCW :    return "Bw'";
            case Bw_2 :    return "Bw2";
            case B3w :    return "3Bw";
            case B3w_CCW :    return "3Bw'";
            case B3w_2 :    return "3Bw2";
            case b :       return "b";
            case b_CCW :   return "b'";

            case M :        return "M";
            case M_CCW :    return "M'";
            case M_2 :    return "M2";
            case E :        return "E";
            case E_CCW :    return "E'";
            case E_2 :    return "E2";
            case S :        return "S";
            case S_CCW :    return "S'";
            case S_2 :    return "S2";

            case x :        return "x";
            case x_CCW :    return "x'";
            case x_2 :    return "x2";
            case y :        return "y";
            case y_CCW :    return "y'";
            case y_2 :    return "y2";
            case z :        return "z";
            case z_CCW :    return "z'";
            case z_2 :    return "z2";

            case R2plus :    return "R++";
            case R2minus :    return "R--";
            case D2plus :    return "D++";
            case D2minus :    return "D--";

            default : return "";
        }

    }

    public static String encodeWithSpaces(int[] rotations){
        final int rotationLength = rotations.length;

        //encode first the first rotation, to do after that a loop that add spaces before the rotation
        String encodedRotations = getNotation(rotations[0]);
        for(int i = 1; i < rotationLength; i++){
            if(rotations[i] != NO_ROTATION){
                encodedRotations += (" " + getNotation(rotations[i]));
            }
        }
        return encodedRotations;
    }

    public static int[] decode(String rotationsString){
        final ArrayList<Integer> rotationList = new ArrayList<Integer>();

        int rotationsLength = rotationsString.length();

        for(int i = 0; i < rotationsLength; i++){

            final char currentChar = rotationsString.charAt(i);

            if (currentChar != ' ') {

                int move = 0;

                if(currentChar == 'U') move = CubeNotations.U;
                else if(currentChar == 'D')  move = CubeNotations.D;
                else if(currentChar == 'R')  move = CubeNotations.R;
                else if(currentChar == 'L')  move = CubeNotations.L;
                else if(currentChar == 'F')  move = CubeNotations.F;
                else if(currentChar == 'B')  move = CubeNotations.B;
                else if(currentChar == 'u')  move = CubeNotations.u;
                else if(currentChar == 'l')  move = CubeNotations.l;
                else if(currentChar == 'r')  move = CubeNotations.r;
                else if(currentChar == 'b')  move = CubeNotations.b;
                //else if(currentChar == 'M') rotationList.add(M);
                //else if(currentChar == 'E') rotationList.add(E);
                //else if(currentChar == 'S') rotationList.add(S);


                char nextChar = i < rotationsLength - 1 ? rotationsString.charAt(i + 1) : 0;

                if(nextChar == 'w'){
                    if(i > 0){
                        if(rotationsString.charAt(i - 1) == '3'){
                            move = singleToTripleTurn(move);
                            nextChar = i < rotationsLength - 2 ? rotationsString.charAt(i + 2) : 0;
                            i++;
                        }else{
                            move = singleToWideTurn(move);
                            nextChar = i < rotationsLength - 2 ? rotationsString.charAt(i + 2) : 0;
                            i++;
                        }
                    }else{
                        move = singleToWideTurn(move);
                        nextChar = i < rotationsLength - 2 ? rotationsString.charAt(i + 2) : 0;
                        i++;
                    }
                }

                if(nextChar == '\''){
                    move = getInvertedRotationId(move);
                    i++;

                }else if(nextChar == '2'){
                    move = getDoubleRotationId(move);
                    i++;
                }

                rotationList.add(move);
            }
        }

        //Log.d("CubeNotations", "-------------------------");

        rotationsLength = rotationList.size();

        final int[] rotations = new int[rotationsLength];


        for(int i = 0; i < rotationsLength; i++){
            rotations[i] = rotationList.get(i);
        }

        return rotations;
    }

    public static int singleToWideTurn(int singleRotation){
        return (singleRotation & FILTER_FACE) + ((singleRotation + 0x03) & FILTER_TURN_TYPE);
    }

    public static int singleToTripleTurn(int singleRotation){
        return (singleRotation & FILTER_FACE) + ((singleRotation + 0x06) & FILTER_TURN_TYPE);
    }

    public static int getRotationsNumber(String encodedRotations){
        int rotationsNumber = 0;
        final int stringSize = encodedRotations.length();
        for(int i = 0; i < stringSize; i++){
            // les 2, 3, w, ' et espaces ne comptent pas comme des moves
            if(encodedRotations.charAt(i) != '\'' &&
                    encodedRotations.charAt(i) != '2' &&
                    encodedRotations.charAt(i) != '3' &&
                    encodedRotations.charAt(i) != ' ' &&
                    encodedRotations.charAt(i) != 'w'){
                rotationsNumber++;
            }
        }
        return rotationsNumber;
    }

    public static int getInvertedRotationId(int rotationId){
        if((rotationId & FILTER_TURN_TYPE) == (U & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (U_CCW & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (U_CCW & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (U & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (Uw & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (Uw_CCW & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (Uw_CCW & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (Uw & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (U3w & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (U3w_CCW & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (U3w_CCW & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (U3w & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (u & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (u_CCW & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (u_CCW & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (u & FILTER_TURN_TYPE);

        }else{
            return rotationId;
        }
    }

    public static int getOppositeRotationId(int rotationId){
        if((rotationId & FILTER_FACE)  == (U & FILTER_FACE)){
            return (D & FILTER_FACE) + (rotationId & FILTER_TURN_TYPE);
        }else if((rotationId & FILTER_FACE)  == (D & FILTER_FACE)){
            return (U & FILTER_FACE) + (rotationId & FILTER_TURN_TYPE);
        }else if((rotationId & FILTER_FACE)  == (R & FILTER_FACE)){
            return (L & FILTER_FACE) + (rotationId & FILTER_TURN_TYPE);
        }else if((rotationId & FILTER_FACE)  == (L & FILTER_FACE)){
            return (R & FILTER_FACE) + (rotationId & FILTER_TURN_TYPE);
        }else if((rotationId & FILTER_FACE)  == (F & FILTER_FACE)){
            return (B & FILTER_FACE) + (rotationId & FILTER_TURN_TYPE);
        }else if((rotationId & FILTER_FACE)  == (B & FILTER_FACE)){
            return (F & FILTER_FACE) + (rotationId & FILTER_TURN_TYPE);
        }else{
            return NO_ROTATION;
        }
    }

    public static boolean isQuarterTurn(int rotationId){
        return (rotationId & 0x0F) == 0x01 || (rotationId & 0x0F) == 0x02;
    }

    public static boolean isDoubleTurn(int rotationId){
        return (rotationId & 0x0F) == 0x03;
    }

    public static int getDoubleRotationId(int rotationId){
        if((rotationId & FILTER_TURN_TYPE) == (U & FILTER_TURN_TYPE) ||
                (rotationId & FILTER_TURN_TYPE) == (U_CCW & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (U_2 & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (Uw & FILTER_TURN_TYPE) ||
                (rotationId & FILTER_TURN_TYPE) == (Uw_CCW & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (Uw_2 & FILTER_TURN_TYPE);

        }else if((rotationId & FILTER_TURN_TYPE) == (U3w & FILTER_TURN_TYPE) ||
                (rotationId & FILTER_TURN_TYPE) == (U3w_CCW & FILTER_TURN_TYPE)){
            return (rotationId & FILTER_FACE) + (U3w_2 & FILTER_TURN_TYPE);
        }
        return rotationId;
    }
}
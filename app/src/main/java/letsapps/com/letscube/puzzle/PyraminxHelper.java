package letsapps.com.letscube.puzzle;

import java.util.Collection;

import letsapps.com.letscube.util.cube.CubeNotations;

public class PyraminxHelper extends PuzzleHelper{

    public static final int NB_FACES = 4;
    public static final int FACE_F = 0;
    public static final int FACE_L = 1;
    public static final int FACE_R = 2;
    public static final int FACE_D = 3;

    public static final int NB_LAYER = 3;
    public static final int NB_MAX_COL = (2 * NB_LAYER) - 1;

    public byte[][][] stickers;

    public static final byte EMPTY = 0;
    public static final byte ORANGE = 1;
    public static final byte BLUE = 2;
    public static final byte YELLOW = 3;
    public static final byte GREEN = 4;

    public PyraminxHelper() {
        stickers = new byte[NB_FACES][NB_LAYER][5];
    }

    public void reset(){
        fillFace(FACE_F, GREEN);
        fillFace(FACE_L, ORANGE);
        fillFace(FACE_R, BLUE);
        fillFace(FACE_D, YELLOW);
    }

    private void fillFace(int faceId, byte faceColor){
        for(int lin = 0; lin < 3; lin++){
            for(int col = 0; col < (2 * lin) + 1; col++) {
                stickers[faceId][lin][col] = faceColor;
            }
        }
    }

    public void applyMove(int rotationID){
        switch(rotationID){
            case CubeNotations.U : applyMoveU(2); break;
            case CubeNotations.U_CCW : applyMoveU(2); applyMoveU(2); break;
            case CubeNotations.u : applyMoveU(1); break;
            case CubeNotations.u_CCW : applyMoveU(1); applyMoveU(1); break;

            case CubeNotations.R : applyMoveR(2); break;
            case CubeNotations.R_CCW : applyMoveR(2); applyMoveR(2); break;
            case CubeNotations.r : applyMoveR(1); break;
            case CubeNotations.r_CCW : applyMoveR(1); applyMoveR(1); break;

            case CubeNotations.L : applyMoveL(2); break;
            case CubeNotations.L_CCW : applyMoveL(2); applyMoveL(2); break;
            case CubeNotations.l : applyMoveL(1); break;
            case CubeNotations.l_CCW : applyMoveL(1); applyMoveL(1); break;

            case CubeNotations.B : applyMoveB(2); break;
            case CubeNotations.B_CCW : applyMoveB(2); applyMoveB(2); break;
            case CubeNotations.b : applyMoveB(1); break;
            case CubeNotations.b_CCW : applyMoveB(1); applyMoveB(1); break;
        }
    }

    public void applyMoveU(int nbLayers){
        for(int line = 0; line < nbLayers; line++){
            final byte[] frontLine = stickers[FACE_F][line].clone();
            copyLineIntoLine(stickers[FACE_R][line], FACE_F, line);
            copyLineIntoLine(stickers[FACE_L][line], FACE_R, line);
            copyLineIntoLine(frontLine, FACE_L, line);
        }
    }

    public void applyMoveR(int nbLayers){
        for(int col = 0; col < nbLayers; col++){
            final byte[] frontColumn = columnToArray(FACE_F, 4 - col).clone();
            copyInvertColumnIntoColumn(FACE_D, 1 + col, FACE_F, 4 - col);
            copyColumnIntoColumn(FACE_R, 1 + col, FACE_D, 1 + col);
            copyInvertColumnIntoColumn(frontColumn, FACE_R, 1 + col);
        }
    }

    public void applyMoveL(int nbLayers){
        for(int col = 0; col < nbLayers; col++){
            final byte[] frontColumn = columnToArray(FACE_F, 1 + col).clone();
            copyInvertColumnIntoColumn(FACE_L, 4 - col, FACE_F, 1 + col);
            copyColumnIntoColumn(FACE_D, 4 - col, FACE_L, 4 - col);
            copyInvertColumnIntoColumn(frontColumn, FACE_D, 4 - col);
        }
    }

    public void applyMoveB(int nbLayers){
        for(int col = 0; col < nbLayers; col++){
            final byte[] downLine = stickers[FACE_D][col].clone();
            copyInvertColumnIntoLine(FACE_L, 1 + col, FACE_D, col);
            copyInvertColumnIntoColumn(FACE_R, 4 - col, FACE_L, 1 + col);
            copyArrayIntoColumn(downLine, FACE_R, 4 - col);
        }
    }

    public void copyLineIntoLine(byte[] lineToCopy, int faceId, int lineIndex){
        stickers[faceId][lineIndex] = lineToCopy;
    }

    public void copyArrayIntoColumn(byte[] array, int faceId, int colIndex){
        switch(colIndex){
            case 1 : stickers[faceId][2][0] = array[0]; break;
            case 2 :
                stickers[faceId][2][2] = array[2];
                stickers[faceId][2][1] = array[1];
                stickers[faceId][1][0] = array[0]; break;
            case 3 :
                stickers[faceId][1][2] = array[0];
                stickers[faceId][2][3] = array[1];
                stickers[faceId][2][2] = array[2]; break;
            case 4 : stickers[faceId][2][4] = array[0]; break;
        }
    }

    public void copyColumnIntoColumn(int faceIdToCopy, int colIndexToCopy, int faceId, int colIndex){
        final byte[] colToCopy = columnToArray(faceIdToCopy, colIndexToCopy).clone();
        copyArrayIntoColumn(colToCopy, faceId, colIndex);
    }

    public void copyInvertColumnIntoColumn(byte[] colToCopy, int faceId, int colIndex){
        switch(colIndex){
            case 1 : stickers[faceId][2][0] = colToCopy[0]; break;
            case 2 :
                stickers[faceId][2][2] = colToCopy[0];
                stickers[faceId][2][1] = colToCopy[1];
                stickers[faceId][1][0] = colToCopy[2]; break;
            case 3 :
                stickers[faceId][2][2] = colToCopy[0];
                stickers[faceId][2][3] = colToCopy[1];
                stickers[faceId][1][2] = colToCopy[2]; break;
            case 4 : stickers[faceId][2][4] = colToCopy[0]; break;
        }
    }

    public void copyInvertColumnIntoLine(int colFaceId, int colIndex, int linFaceId, int linIndex){
        final byte[] col = columnToArray(colFaceId, colIndex);
        for(int i = 0; i < col.length; i++){
            stickers[linFaceId][linIndex][i] = col[col.length - (i + 1)];
        }
    }

    public void copyInvertColumnIntoColumn(int faceIdToCopy, int colIndexToCopy, int faceId, int colIndex){
        final byte[] colToCopy = columnToArray(faceIdToCopy, colIndexToCopy).clone();
        copyInvertColumnIntoColumn(colToCopy, faceId, colIndex);
    }

    public byte[] columnToArray(int faceId, int colIndex){
        switch(colIndex){
            case 1 : return new byte[]{stickers[faceId][2][0]}.clone();
            case 2 : return new byte[]{
                    stickers[faceId][1][0],
                    stickers[faceId][2][1],
                    stickers[faceId][2][2]}.clone();
            case 3 : return new byte[]{
                    stickers[faceId][1][2],
                    stickers[faceId][2][3],
                    stickers[faceId][2][2]}.clone();
            case 4 : return new byte[]{stickers[faceId][2][4]}.clone();
            default : return null;
        }
    }

    public byte[][] getFace(int faceId){
        return stickers[faceId];
    }

    public byte getColor(int faceId, int line, int column){
        return stickers[faceId][line][column];
    }
}

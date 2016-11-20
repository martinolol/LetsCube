package letsapps.com.letscube.puzzle;

import android.util.Log;

import letsapps.com.letscube.util.cube.CubeNotations;

public class CubeHelper extends PuzzleHelper{

    public static final int NB_FACES = 6;
    public static final int FACE_F = 0;
    public static final int FACE_U = 1;
    public static final int FACE_D = 2;
    public static final int FACE_R = 3;
    public static final int FACE_L = 4;
    public static final int FACE_B = 5;

    public static final byte GREEN = 0;
    public static final byte WHITE = 1;
    public static final byte YELLOW = 2;
    public static final byte RED = 3;
    public static final byte ORANGE = 4;
    public static final byte BLUE = 5;

    int cubeSize;
    int maxArray;

    // array1 : faces [F, U, D, R, L, B]
    // array2 : line
    // array3 : column
    byte[][][] stickers;

    CubeHelper(int cubeSize){
        this.cubeSize = cubeSize;
        this.maxArray = cubeSize - 1;
        stickers = new byte[NB_FACES][cubeSize][cubeSize];
    }

    public void reset(){
        fillFace(FACE_F, GREEN);
        fillFace(FACE_U, WHITE);
        fillFace(FACE_D, YELLOW);
        fillFace(FACE_R, RED);
        fillFace(FACE_L, ORANGE);
        fillFace(FACE_B, BLUE);

    }

    public void fillFace(int faceId, byte color){
        for(int line = 0; line < cubeSize; line++) {
            for (int column = 0; column < cubeSize; column++) {
                stickers[faceId][line][column] = color;
            }
        }
    }

    public void applyMove(int rotationId){
        switch (rotationId){
            case CubeNotations.U : applyU(1); break;
            case CubeNotations.U_CCW : applyU(1); applyU(1); applyU(1); break;
            case CubeNotations.U_2 : applyU(1); applyU(1); break;

            case CubeNotations.Uw : applyU(2); break;
            case CubeNotations.Uw_CCW : applyU(2); applyU(2); applyU(2); break;
            case CubeNotations.Uw_2 : applyU(2); applyU(2); break;

            case CubeNotations.U3w : applyU(3); break;
            case CubeNotations.U3w_CCW : applyU(3); applyU(3); applyU(3); break;
            case CubeNotations.U3w_2 : applyU(3); applyU(3); break;

            case CubeNotations.D : applyD(1); break;
            case CubeNotations.D_CCW : applyD(1); applyD(1); applyD(1); break;
            case CubeNotations.D_2 : applyD(1); applyD(1); break;

            case CubeNotations.Dw : applyD(2); break;
            case CubeNotations.Dw_CCW : applyD(2); applyD(2); applyD(2); break;
            case CubeNotations.Dw_2 : applyD(2); applyD(2); break;

            case CubeNotations.D3w : applyD(3); break;
            case CubeNotations.D3w_CCW : applyD(3); applyD(3); applyD(3); break;
            case CubeNotations.D3w_2 : applyD(3); applyD(3); break;

            case CubeNotations.R : applyR(1); break;
            case CubeNotations.R_CCW : applyR(1); applyR(1); applyR(1); break;
            case CubeNotations.R_2 : applyR(1); applyR(1); break;

            case CubeNotations.Rw : applyR(2); break;
            case CubeNotations.Rw_CCW : applyR(2); applyR(2); applyR(2); break;
            case CubeNotations.Rw_2 : applyR(2); applyR(2); break;

            case CubeNotations.R3w : applyR(3); break;
            case CubeNotations.R3w_CCW : applyR(3); applyR(3); applyR(3); break;
            case CubeNotations.R3w_2 : applyR(3); applyR(3); break;

            case CubeNotations.L : applyL(1); break;
            case CubeNotations.L_CCW : applyL(1); applyL(1); applyL(1); break;
            case CubeNotations.L_2 : applyL(1); applyL(1); break;

            case CubeNotations.Lw : applyL(2); break;
            case CubeNotations.Lw_CCW : applyL(2); applyL(2); applyL(2); break;
            case CubeNotations.Lw_2 : applyL(2); applyL(2); break;

            case CubeNotations.L3w : applyL(3); break;
            case CubeNotations.L3w_CCW : applyL(3); applyL(3); applyL(3); break;
            case CubeNotations.L3w_2 : applyL(3); applyL(3); break;

            case CubeNotations.F : applyF(1); break;
            case CubeNotations.F_CCW : applyF(1); applyF(1); applyF(1); break;
            case CubeNotations.F_2 : applyF(1); applyF(1); break;

            case CubeNotations.Fw : applyF(2); break;
            case CubeNotations.Fw_CCW : applyF(2); applyF(2); applyF(2); break;
            case CubeNotations.Fw_2 : applyF(2); applyF(2); break;

            case CubeNotations.F3w : applyF(3); break;
            case CubeNotations.F3w_CCW : applyF(3); applyF(3); applyF(3); break;
            case CubeNotations.F3w_2 : applyF(3); applyF(3); break;

            case CubeNotations.B : applyB(1); break;
            case CubeNotations.B_CCW : applyB(1); applyB(1); applyB(1); break;
            case CubeNotations.B_2 : applyB(1); applyB(1); break;

            case CubeNotations.Bw : applyB(2); break;
            case CubeNotations.Bw_CCW : applyB(2); applyB(2); applyB(2); break;
            case CubeNotations.Bw_2 : applyB(2); applyB(2); break;

            case CubeNotations.B3w : applyB(3); break;
            case CubeNotations.B3w_CCW : applyB(3); applyB(3); applyB(3); break;
            case CubeNotations.B3w_2 : applyB(3); applyB(3); break;
        }
    }

    public void applyU(int nbLayers){
        //Log.d("Cube", "Rotation U" + nbLayers + " applying on the cube");
        for(int i = 0; i < nbLayers; i++) {
            final int minLayer = i;
            final int maxLayer = maxArray - i;
            //turn layer
            final byte[] frontUpLine = stickers[FACE_F][minLayer];
            copyLineIntoLine(stickers[FACE_R][minLayer], FACE_F, minLayer);
            copyLineIntoLine(stickers[FACE_B][minLayer], FACE_R, minLayer);
            copyLineIntoLine(stickers[FACE_L][minLayer], FACE_B, minLayer);
            copyLineIntoLine(frontUpLine, FACE_L, minLayer);
        }

        turnFaceWithoutLayer(FACE_U);
    }

    public void applyD(int nbLayers){
        //Log.d("Cube", "Rotation D" + nbLayers + " applying on the cube");
        for(int i = 0; i < nbLayers; i++) {
            final int minLayer = i;
            final int maxLayer = maxArray - i;
            //turn layer
            final byte[] frontDownLine = stickers[FACE_F][maxLayer];
            copyLineIntoLine(stickers[FACE_L][maxLayer], FACE_F, maxLayer);
            copyLineIntoLine(stickers[FACE_B][maxLayer], FACE_L, maxLayer);
            copyLineIntoLine(stickers[FACE_R][maxLayer], FACE_B, maxLayer);
            copyLineIntoLine(frontDownLine, FACE_R, maxLayer);
        }

        turnFaceWithoutLayer(FACE_D);
    }

    public void applyR(int nbLayers){
        //Log.d("Cube", "Rotation R" + nbLayers + " applying on the cube");
        for(int i = 0; i < nbLayers; i++) {
            final int minLayer = i;
            final int maxLayer = maxArray - i;
            //turn layer
            final byte[] frontRightColumn = columnToArray(FACE_F, maxLayer);
            copyColumnIntoColumn(FACE_D, maxLayer, FACE_F, maxLayer);
            copyInvertColumnIntoColumn(FACE_B, minLayer, FACE_D, maxLayer);
            copyInvertColumnIntoColumn(FACE_U, maxLayer, FACE_B, minLayer);
            copyColumnIntoColumn(frontRightColumn, FACE_U, maxLayer);
        }

        turnFaceWithoutLayer(FACE_R);
    }

    public void applyL(int nbLayers){
        //Log.d("Cube", "Rotation L" + nbLayers + " applying on the cube");
        for(int i = 0; i < nbLayers; i++) {
            final int minLayer = i;
            final int maxLayer = maxArray - i;
            //turn layer
            final byte[] frontLeftColumn = columnToArray(FACE_F, minLayer);
            copyColumnIntoColumn(FACE_U, minLayer, FACE_F, minLayer);
            copyInvertColumnIntoColumn(FACE_B, maxLayer, FACE_U, minLayer);
            copyInvertColumnIntoColumn(FACE_D, minLayer, FACE_B, maxLayer);
            copyColumnIntoColumn(frontLeftColumn, FACE_D, minLayer);
        }

        turnFaceWithoutLayer(FACE_L);
    }

    public void applyF(int nbLayers){
        //Log.d("Cube", "Rotation F" + nbLayers + " applying on the cube");
        for(int i = 0; i < nbLayers; i++) {
            final int minLayer = i;
            final int maxLayer = maxArray - i;
            //turn layer
            final byte[] upFrontLine = stickers[FACE_U][maxLayer];
            copyInvertColumnIntoLine(FACE_L, maxLayer, FACE_U, maxLayer);
            copyLineIntoColumn(stickers[FACE_D][minLayer], FACE_L, maxLayer);
            copyInvertColumnIntoLine(FACE_R, minLayer, FACE_D, minLayer);
            copyLineIntoColumn(upFrontLine, FACE_R, minLayer);
        }

        turnFaceWithoutLayer(FACE_F);
    }

    public void applyB(int nbLayers){
        //Log.d("Cube", "Rotation B" + nbLayers + " applying on the cube");
        for(int i = 0; i < nbLayers; i++) {
            final int minLayer = i;
            final int maxLayer = maxArray - i;
            //turn layer
            final byte[] upBackLine = stickers[FACE_U][minLayer].clone();
            copyColumnIntoLine(FACE_R, maxLayer, FACE_U, minLayer);
            copyInvertLineIntoColumn(stickers[FACE_D][maxLayer], FACE_R, maxLayer);
            copyColumnIntoLine(FACE_L, minLayer, FACE_D, maxLayer);
            copyInvertLineIntoColumn(upBackLine, FACE_L, minLayer);
        }

        turnFaceWithoutLayer(FACE_B);
    }

    private void turnFaceWithoutLayer(int faceId){
        final byte[][] turnedFace = new byte[cubeSize][cubeSize];

        for(int i = 0; i < cubeSize; i++){
            turnedFace[i] = InvertedColumnToArray(faceId, i);
        }

        stickers[faceId] = turnedFace;
    }

    public void copyLineIntoLine(byte[] lineToCopy, int faceId, int lineIndex){
        stickers[faceId][lineIndex] = lineToCopy;
    }

    public void copyLineIntoColumn(byte[] lineToCopy, int faceId, int columnIndex){
        final byte[] lineTemp = lineToCopy.clone();
        for(int i = 0; i < cubeSize; i++){
            stickers[faceId][i][columnIndex] = lineTemp[i];
        }
    }

    public void copyInvertLineIntoColumn(byte[] lineToCopy, int faceId, int columnIndex){
        final byte[] lineTemp = lineToCopy.clone();
        for(int i = 0; i < cubeSize; i++){
            stickers[faceId][i][columnIndex] = lineTemp[maxArray - i];
        }
    }

    public void copyColumnIntoLine(int colFaceId, int columnIndex, int linFaceId, int lineIndex){
        for(int i = 0; i < cubeSize; i++){
            stickers[linFaceId][lineIndex][i] = stickers[colFaceId][i][columnIndex];
        }
    }

    public void copyInvertColumnIntoLine(int colFaceId, int colIndex, int linFaceId, int lineIndex){
        final byte[] colTemp = new byte[cubeSize];
        for(int i = 0; i < cubeSize; i++){
            colTemp[i] = stickers[colFaceId][maxArray - i][colIndex];
        }
        stickers[linFaceId][lineIndex] = colTemp;
    }

    public void copyColumnIntoColumn(byte[] col1, int col2FaceId, int col2Index){
        for(int i = 0; i < cubeSize; i++){
            stickers[col2FaceId][i][col2Index] = col1[i];
        }
    }

    public void copyColumnIntoColumn(int col1FaceId, int col1Index, int col2FaceId, int col2Index){
        for(int i = 0; i < cubeSize; i++){
            stickers[col2FaceId][i][col2Index] = stickers[col1FaceId][i][col1Index];
        }
    }

    public void copyInvertColumnIntoColumn(int col1FaceId, int col1Index, int col2FaceId, int col2Index){
        for(int i = 0; i < cubeSize; i++){
            stickers[col2FaceId][i][col2Index] = stickers[col1FaceId][maxArray - i][col1Index];
        }
    }

    public byte[] columnToArray(int faceId, int colIndex){
        final byte[] colTemp = new byte[cubeSize];
        for(int i = 0; i < cubeSize; i++){
            colTemp[i] = stickers[faceId][i][colIndex];
        }
        return colTemp;
    }

    public byte[] InvertedColumnToArray(int faceId, int colIndex){
        final byte[] colTemp = new byte[cubeSize];
        for(int i = 0; i < cubeSize; i++){
            colTemp[i] = stickers[faceId][maxArray - i][colIndex];
        }
        return colTemp;
    }

    public byte getColor(int faceId, int line, int column){
        return stickers[faceId][line][column];
    }

    public byte[][] getFace(int faceId){
        return stickers[faceId];
    }
}

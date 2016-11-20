package letsapps.com.letscube.puzzle;

import android.util.Log;

import letsapps.com.letscube.util.cube.CubeNotations;

public class Cube extends Puzzle{

    int size;

    CubeHelper helper;

    // for exemple 3x3 have a size of 3.
    public Cube(int cubeSize){
        this.size = cubeSize;
        this.helper = new CubeHelper(cubeSize);

        helper.reset();
    }

    @Override
    public void scramble(String scramble){
        helper.reset();

        int[] moves = CubeNotations.decode(scramble);
        for(int move : moves){
            helper.applyMove(move);
        }
    }

    public int getSize(){
        return size;
    }

    public byte[][] getFace(int faceId){
        return helper.getFace(faceId);
    }

    /*
    private void printTest(){
        final int max = size - 1;
        printTest(CubeHelper.FACE_F, 0, 0, "F(0.0)");
        printTest(CubeHelper.FACE_R, 0, 0, "R(0.0)");
        printTest(CubeHelper.FACE_B, 0, 0, "B(0.0)");
        printTest(CubeHelper.FACE_L, 0, 0, "L(0.0)");
        printTest(CubeHelper.FACE_U, 0, 0, "U(0.0)");
        printTest(CubeHelper.FACE_U, 0, 1, "U(0.1)");
        printTest(CubeHelper.FACE_U, 0, max, "U(0." + max + ")");
        printTest(CubeHelper.FACE_U, 1, 2, "U(1.2)");
        printTest(CubeHelper.FACE_U, max, max, "U(" + max + "." + max + ")");
        printTest(CubeHelper.FACE_U, 2, 1, "U(2.1)");
        printTest(CubeHelper.FACE_U, max, 0, "U(" + max + ".0)");
        printTest(CubeHelper.FACE_U, 1, 0, "U(1.0)");
    }

    private void printTest(int faceId, int line, int column, String text){
        switch (helper.getColor(faceId, line, column)){
            case CubeHelper.GREEN : Log.d("Cube", text + " : GREEN"); break;
            case CubeHelper.WHITE : Log.d("Cube", text + " : WHITE"); break;
            case CubeHelper.YELLOW : Log.d("Cube", text + " : YELLOW"); break;
            case CubeHelper.RED : Log.d("Cube", text + " : RED"); break;
            case CubeHelper.ORANGE : Log.d("Cube", text + " : ORANGE"); break;
            case CubeHelper.BLUE : Log.d("Cube", text + " : BLUE"); break;
            default : Log.d("Cube", text + " : NO COLOR"); break;
        }
    }
*/
}

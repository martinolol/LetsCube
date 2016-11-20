package letsapps.com.letscube.puzzle;

import android.util.Log;

import letsapps.com.letscube.util.cube.CubeNotations;

public class Pyraminx extends Puzzle{

    PyraminxHelper helper;

    public Pyraminx(){
        this.helper = new PyraminxHelper();

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

    public byte[][] getFace(int faceId){
        return helper.getFace(faceId);
    }

    public byte getSticker(int faceId, int lin, int col){
        return helper.getColor(faceId, lin, col);
    }

    public int getNbColFromLine(int line){
        return (2 * line) + 1;
    }

}

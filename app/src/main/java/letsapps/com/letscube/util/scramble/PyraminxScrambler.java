package letsapps.com.letscube.util.scramble;

import letsapps.com.letscube.util.cube.CubeNotations;

public abstract class PyraminxScrambler {

    public static final int NB_TIPS = 4;

    public static String generateEncodedScramble(final int nbMoves) {
        final int[] moves = new int[nbMoves];
        final int[] tipsMoves = generateRandomTipsMoves();

        moves[0] = generateRandomMove();
        for (int i = 1; i < nbMoves - NB_TIPS; i++) {
            moves[i] = generateRandomMoveWithout(moves[i - 1]);
        }
        for(int i = nbMoves - NB_TIPS; i < nbMoves; i++){
            moves[i] = tipsMoves[i - (nbMoves - NB_TIPS)];
        }

        return CubeNotations.encodeWithSpaces(moves);
    }

    public static int generateRandomMove(){
        int randomTip = (int)(Math.random() * 4) + 1; // (entre 1 et 4 inclus)
        int randomTurnType = (int)(Math.random() * 2) + 1; // (entre 1 et 2 inclus)

        switch (randomTip){
            case 1: randomTip = (CubeNotations.U & CubeNotations.FILTER_FACE); break;
            case 2: randomTip = (CubeNotations.R & CubeNotations.FILTER_FACE); break;
            case 3: randomTip = (CubeNotations.L & CubeNotations.FILTER_FACE); break;
            case 4: randomTip = (CubeNotations.B & CubeNotations.FILTER_FACE); break;
        }

        switch (randomTurnType){
            case 1: randomTurnType = (CubeNotations.U & CubeNotations.FILTER_TURN_TYPE); break;
            case 2: randomTurnType = (CubeNotations.U_CCW & CubeNotations.FILTER_TURN_TYPE); break;
        }

        return  randomTip + randomTurnType;
    }

    public static int generateRandomMoveWithout(int previousMove){

        int move;

        do{
            move = generateRandomMove();
        }while ((move & CubeNotations.FILTER_FACE) == (previousMove & CubeNotations.FILTER_FACE));

        return  move;
    }

    public static int[] generateRandomTipsMoves(){
        final int tipsMoveR = (int)(Math.random() * 3);
        final int tipsMoveL = (int)(Math.random() * 3);
        final int tipsMoveU = (int)(Math.random() * 3);
        final int tipsMoveB = (int)(Math.random() * 3);

        int[] moves = new int[4];

        switch(tipsMoveR){
            case 0 : moves[0] = CubeNotations.r; break;
            case 1 : moves[0] = CubeNotations.r_CCW; break;
            case 2 : moves[0] = CubeNotations.NO_ROTATION; break;
        }
        switch(tipsMoveL){
            case 0 : moves[1] = CubeNotations.l; break;
            case 1 : moves[1] = CubeNotations.l_CCW; break;
            case 2 : moves[1] = CubeNotations.NO_ROTATION;break;
        }switch(tipsMoveU){
            case 0 : moves[2] = CubeNotations.u; break;
            case 1 : moves[2] = CubeNotations.u_CCW; break;
            case 2 : moves[2] = CubeNotations.NO_ROTATION; break;
        }
        switch(tipsMoveB){
            case 0 : moves[3] = CubeNotations.b; break;
            case 1 : moves[3] = CubeNotations.b_CCW; break;
            case 2 : moves[3] = CubeNotations.NO_ROTATION; break;
        }

        return moves;
    }
}
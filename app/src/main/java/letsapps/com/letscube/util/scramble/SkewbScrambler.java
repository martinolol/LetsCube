package letsapps.com.letscube.util.scramble;

import letsapps.com.letscube.util.cube.CubeNotations;

public abstract class SkewbScrambler {

    public static String generateEncodedScramble(final int nbMoves) {
        final int[] moves = new int[nbMoves];

        moves[0] = generateRandomMove();
        for (int i = 1; i < nbMoves; i++) {
            moves[i] = generateRandomMoveWithout(moves[i - 1]);
        }

        return CubeNotations.encodeWithSpaces(moves);
    }

    public static int generateRandomMove(){
        int randomMove = (int)(Math.random() * 4) + 1; // (entre 1 et 4 inclus)
        int randomTurnType = (int)(Math.random() * 2) + 1; // (entre 1 et 2 inclus)

        switch (randomMove){
            case 1: randomMove = (CubeNotations.U & CubeNotations.FILTER_FACE); break;
            case 2: randomMove = (CubeNotations.R & CubeNotations.FILTER_FACE); break;
            case 3: randomMove = (CubeNotations.L & CubeNotations.FILTER_FACE); break;
            case 4: randomMove = (CubeNotations.B & CubeNotations.FILTER_FACE); break;
        }

        switch (randomTurnType){
            case 1: randomTurnType = (CubeNotations.U & CubeNotations.FILTER_TURN_TYPE); break;
            case 2: randomTurnType = (CubeNotations.U_CCW & CubeNotations.FILTER_TURN_TYPE); break;
        }

        return  randomMove + randomTurnType;
    }

    public static int generateRandomMoveWithout(int previousMove){

        int move;

        do{
            move = generateRandomMove();
        }while ((move & CubeNotations.FILTER_FACE) == (previousMove & CubeNotations.FILTER_FACE));

        return  move;
    }

}

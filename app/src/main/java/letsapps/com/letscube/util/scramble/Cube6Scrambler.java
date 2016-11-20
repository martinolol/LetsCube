package letsapps.com.letscube.util.scramble;

import letsapps.com.letscube.util.cube.CubeNotations;

public abstract class Cube6Scrambler {

    public static String generateEncodedScramble(final int nbMoves) {
        final int[] moves = new int[nbMoves];

        // generate a random move before generate the others,
        // to do in the loop a next move different of the previous.
        moves[0] = generateRandomMove();
        for (int i = 1; i < nbMoves; i++) {
            moves[i] = generateRandomMoveWithout(moves[i - 1]);
        }
        return CubeNotations.encodeWithSpaces(moves);
    }

    public static int generateRandomMove(){
        int randomFace = (int)(Math.random() * 6);
        int randomTurnType = (int)(Math.random() * 9);

        switch (randomFace){
            case 0: randomFace = (CubeNotations.U & CubeNotations.FILTER_FACE); break;
            case 1: randomFace = (CubeNotations.R & CubeNotations.FILTER_FACE); break;
            case 2: randomFace = (CubeNotations.F & CubeNotations.FILTER_FACE); break;
            case 3: randomFace = (CubeNotations.D & CubeNotations.FILTER_FACE); break;
            case 4: randomFace = (CubeNotations.L & CubeNotations.FILTER_FACE); break;
            case 5: randomFace = (CubeNotations.B & CubeNotations.FILTER_FACE); break;
        }

        switch (randomTurnType){
            case 0: randomTurnType = (CubeNotations.R & CubeNotations.FILTER_TURN_TYPE); break;
            case 1: randomTurnType = (CubeNotations.R_CCW & CubeNotations.FILTER_TURN_TYPE); break;
            case 2: randomTurnType = (CubeNotations.R_2 & CubeNotations.FILTER_TURN_TYPE); break;
            case 3: randomTurnType = (CubeNotations.Rw & CubeNotations.FILTER_TURN_TYPE); break;
            case 4: randomTurnType = (CubeNotations.Rw_CCW & CubeNotations.FILTER_TURN_TYPE); break;
            case 5: randomTurnType = (CubeNotations.Rw_2 & CubeNotations.FILTER_TURN_TYPE); break;
            case 6: randomTurnType = (CubeNotations.R3w & CubeNotations.FILTER_TURN_TYPE); break;
            case 7: randomTurnType = (CubeNotations.R3w_CCW & CubeNotations.FILTER_TURN_TYPE); break;
            case 8: randomTurnType = (CubeNotations.R3w_2 & CubeNotations.FILTER_TURN_TYPE); break;
        }

        return  randomFace + randomTurnType;
    }

    public static int generateRandomMoveWithout(int previousMove){

        int move;

        do{
            move = generateRandomMove();
        }while ((move & CubeNotations.FILTER_FACE) == (previousMove & CubeNotations.FILTER_FACE) ||
                (move & CubeNotations.FILTER_FACE) ==
                        (CubeNotations.getOppositeRotationId(previousMove)
                                & CubeNotations.FILTER_FACE));

        return  move;
    }
}

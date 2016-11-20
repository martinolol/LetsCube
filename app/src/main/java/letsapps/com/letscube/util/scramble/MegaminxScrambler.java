package letsapps.com.letscube.util.scramble;

import letsapps.com.letscube.util.cube.CubeNotations;

public abstract class MegaminxScrambler {

    static final int U_INTERVAL = 8;

    public static String generateEncodedScramble(final int nbMoves) {
        final int[] moves = new int[nbMoves];

        // generate a random move before generate the others,
        // to do in the loop a next move different of the previous.
        // The first one is forced to be a R to avoid D between U
        moves[0] = generateRandomMoveWithout(CubeNotations.D2minus);
        for (int i = 1; i < nbMoves; i++) {
            if((i + 1) % U_INTERVAL == 0){
                final int randomMove = (int)(Math.random() * 2);
                switch (randomMove){
                    case 0: moves[i++] = CubeNotations.U; break;
                    case 1: moves[i++] = CubeNotations.U_CCW; break;
                }
                // To force to be a R, to avoid D between U
                moves[i] = generateRandomMoveWithout(CubeNotations.D2minus);
            }else{
                moves[i] = generateRandomMoveWithout(moves[i - 1]);
            }
        }
        return encodeWithSpaces(moves);
    }

    public static int generateRandomMove() {
        final int randomMove = (int)(Math.random() * 4); // (entre 1 et 6 inclus)

        switch (randomMove){
            case 0: return CubeNotations.R2plus;
            case 1: return CubeNotations.R2minus;
            case 2: return CubeNotations.D2plus;
            case 3: return CubeNotations.D2minus;
            default: return CubeNotations.NO_ROTATION;
        }
    }

    public static int generateRandomMoveWithout(int previousMove) {
        int move;

        do{
            move = generateRandomMove();
        }while ((move & CubeNotations.FILTER_FACE) == (previousMove & CubeNotations.FILTER_FACE) ||
                (move & CubeNotations.FILTER_FACE) ==
                        (CubeNotations.getOppositeRotationId(previousMove)
                                & CubeNotations.FILTER_FACE));

        return  move;
    }

    public static String encodeWithSpaces(int[] rotations){
        final int rotationLength = rotations.length;

        //encode first the first rotation, to do after that a loop that add spaces before the rotation
        String encodedRotations = CubeNotations.getNotation(rotations[0]);
        for(int i = 1; i < rotationLength; i++){

            if((rotations[i] & CubeNotations.FILTER_FACE) ==
                    (CubeNotations.U & CubeNotations.FILTER_FACE)){
                encodedRotations += (" " + CubeNotations.getNotation(rotations[i]));
                encodedRotations += "\n";
            }else if((rotations[i - 1] & CubeNotations.FILTER_FACE) ==
                    (CubeNotations.U & CubeNotations.FILTER_FACE)){
                encodedRotations += CubeNotations.getNotation(rotations[i]);
            }else{
                encodedRotations += (" " + CubeNotations.getNotation(rotations[i]));
            }
        }
        return encodedRotations;
    }
}

package letsapps.com.letscube.util.scramble;

import letsapps.com.letscube.util.cube.CubeNotations;

public abstract class Cube3x3x2Scrambler {

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

        public static int generateRandomMove() {
            int randomFace = (int)(Math.random() * 6); // (entre 1 et 6 inclus)

            switch (randomFace){
                case 0: return CubeNotations.R_2;
                case 1: return CubeNotations.F_2;
                case 2: return CubeNotations.L_2;
                case 3: return CubeNotations.B_2;
                case 4: randomFace = (CubeNotations.D & CubeNotations.FILTER_FACE); break;
                case 5: randomFace = (CubeNotations.U & CubeNotations.FILTER_FACE); break;
            }

            int randomTurnType = (int)(Math.random() * 3); // (entre 1 et 6 inclus)

            switch (randomTurnType){
                case 0: randomTurnType = (CubeNotations.R & CubeNotations.FILTER_TURN_TYPE); break;
                case 1: randomTurnType = (CubeNotations.R_CCW & CubeNotations.FILTER_TURN_TYPE); break;
                case 2: randomTurnType = (CubeNotations.R_2 & CubeNotations.FILTER_TURN_TYPE); break;
            }

            return  randomFace + randomTurnType;
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
}

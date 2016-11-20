package letsapps.com.letscube.util.scramble;

import android.util.Log;

import letsapps.com.letscube.util.Clock.ClockMove;
import letsapps.com.letscube.util.Clock.ClockNotations;
import letsapps.com.letscube.util.cube.CubeNotations;

public abstract class ClockScrambler {
    public static String generateEncodedScramble(int nbMoves) {
        final ClockMove[] movesClock = new ClockMove[nbMoves];

        final int yRotationPos = nbMoves / 2;

        // generate a random move before generate the others,
        // to do in the loop a next move different of the previous.
        movesClock[0] = generateRandomMove();
        for (int i = 1; i < nbMoves; i++) {
            if(i != yRotationPos) {
                movesClock[i] = generateRandomMoveWithout(movesClock[i - 1]);
            }else{
                movesClock[i] = new ClockMove(ClockNotations.y2, 0);
            }
        }
        return ClockNotations.encodeWithSpaces(movesClock);
    }

    public static ClockMove generateRandomMove() {
        int randomTip = (int)(Math.random() * 9);
        int randomTurn;
        do {
            randomTurn = (int) (Math.random() * 13) - 6;
        }while(randomTurn == 0);

        switch (randomTip){
            case 0: randomTip = ClockNotations.L; break;
            case 1: randomTip = ClockNotations.DL; break;
            case 2: randomTip = ClockNotations.D; break;
            case 3: randomTip = ClockNotations.DR; break;
            case 4: randomTip = ClockNotations.R; break;
            case 5: randomTip = ClockNotations.UR; break;
            case 6: randomTip = ClockNotations.U; break;
            case 7: randomTip = ClockNotations.UL; break;
            case 8: randomTip = ClockNotations.ALL; break;
        }

        return new ClockMove(randomTip, randomTurn);
    }

    public static ClockMove generateRandomMoveWithout(ClockMove previousMove) {
        ClockMove move;

        if(previousMove != null) {
            do {
                move = generateRandomMove();
            } while (move.getTip() == previousMove.getTip());
        }else{
            move = generateRandomMove();
        }

        return move;
    }
}

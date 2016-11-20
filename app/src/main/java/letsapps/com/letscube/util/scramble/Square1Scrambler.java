package letsapps.com.letscube.util.scramble;

import android.util.Log;

import letsapps.com.letscube.util.square1.Square1Model;
import letsapps.com.letscube.util.square1.Square1Move;

public abstract class Square1Scrambler {

    public static String generateSquare1Scramble(int nbMoves){
        Square1Move[] moves = new Square1Move[nbMoves];

        Square1Model model = new Square1Model();

        //do {

            moves[0] = model.generateRandomMove();
            Log.d("S1Mdl (genRdmMoves)", "firstMoveGenerated : " + moves[0]);
            for (int i = 1; i < nbMoves; i++) {
                model.applySlice();
                moves[i] = model.generateRandomMove();
                Log.d("S1Mdl (genRdmMoves)", "Move" + i + "Generated : " + moves[i]);
            }

        //}while(model.isScrambleOk());

        return encodeWithSpaces(moves);
    }

    public static String encodeWithSpaces(Square1Move[] rotations){
        final int rotationLength = rotations.length;

        Log.d("S1Mdl (genRdmMoves)", "firstMoveToEncode : " + rotations[0]);

        //encode first the first rotation, to do after that, a loop that add spaces before the rotation
        String encodedRotations = rotations[0].toString();
        for(int i = 1; i < rotationLength; i++){
            encodedRotations += (" / " + rotations[i].toString());
            Log.d("S1Mdl (genRdmMoves)", "Move" + i + "ToEncode : " + rotations[i]);

        }
        return encodedRotations;
    }
}

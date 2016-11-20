package letsapps.com.letscube.util.square1;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Square1Model {

    final int IMPOSIBLE_TO_COUNT_6 = 0;

    List<Byte> upPieceWidth;
    List<Byte> downPieceWidth;

    public Square1Model(){

        // pieces width, turning right from the slice separation.
        upPieceWidth = new ArrayList<>(Arrays.asList(new Byte[]{1, 2, 1, 2, 1, 2, 1, 2}));
        downPieceWidth = new ArrayList<>(Arrays.asList(new Byte[]{1, 2, 1, 2, 1, 2, 1, 2}));

    }

    public Square1Move generateRandomMove() {

        int randomUpNbPiecesMove, randomUpMove;
        int randomDownNbPiecesMove, randomDownMove;

        //Log.d("S1Mdl (genRdmMoves)", "-----------------------------");
        //Log.d("S1Mdl (genRdmMoves)", listToString(upPieceWidth, "UpPieces"));
        //Log.d("S1Mdl (genRdmMoves)", listToString(downPieceWidth, "DownPieces"));


        do {

            randomUpNbPiecesMove = 0;
            randomUpMove = 0;
            randomDownNbPiecesMove = 0;
            randomDownMove = 0;

            // génère des moves aléatoire jusqu'à qu'un slice soit faisable avec le move trouvé.
            // pour la face du haut.

            do {
                shiftArray(upPieceWidth, randomUpNbPiecesMove);
                randomUpNbPiecesMove = (int) ((Math.random()) * 7) - 3; // random entre -3 et 3
                shiftArray(upPieceWidth, -randomUpNbPiecesMove);
                //Log.d("S1Mdl (genRdmMoves)", "randomUpNbPiecesMove : " + randomUpNbPiecesMove);
                //Log.d("S1Mdl (genRdmMoves)", "nbMovesToCount 6 : " + getNbPiecesToCount6(upPieceWidth));
            } while (getNbPiecesToCount6(upPieceWidth) == IMPOSIBLE_TO_COUNT_6);

            //shiftArray(upPieceWidth, randomUpNbPiecesMove);

            if (randomUpNbPiecesMove > 0) {
                for (int i = upPieceWidth.size() - randomUpNbPiecesMove; i < upPieceWidth.size(); i++) {
                    randomUpMove += upPieceWidth.get(i);
                }
            } else if (randomUpNbPiecesMove < 0) {
                for (int i = 0; i < -randomUpNbPiecesMove; i++) {
                    randomUpMove -= upPieceWidth.get(i);
                }
            } // else randomUpNbPiecesMove = 0 : do nothing

            //Log.d("S1Mdl (genRdmMoves)", "randomUpNbPiecesMove : " + randomUpNbPiecesMove);
            //Log.d("S1Mdl (genRdmMoves)", "randomUpMove : " + randomUpMove);

            // Do it for downPieces :
            // génère des moves aléatoire jusqu'à qu'un slice soit faisable avec le move trouvé.
            // pour la face du bas.
            do {
                shiftArray(downPieceWidth, -randomDownNbPiecesMove);
                randomDownNbPiecesMove = (int) ((Math.random()) * 7) - 3; // random entre -3 et 3
                shiftArray(downPieceWidth, randomDownNbPiecesMove);
                //Log.d("S1Mdl (genRdmMoves)", "randomDownNbPiecesMove : " + randomDownNbPiecesMove);
                //Log.d("S1Mdl (genRdmMoves)", "nbMovesToCount 6 : " + getNbPiecesToCount6(downPieceWidth));
            } while (getNbPiecesToCount6(downPieceWidth) == IMPOSIBLE_TO_COUNT_6);

            //shiftArray(downPieceWidth, -randomDownNbPiecesMove);

            if (randomDownNbPiecesMove > 0) {
                for (int i = 0; i < randomDownNbPiecesMove; i++) {
                    randomDownMove += downPieceWidth.get(i);
                }
            } else if (randomDownNbPiecesMove < 0) {
                for (int i = downPieceWidth.size() + randomDownNbPiecesMove; i < downPieceWidth.size(); i++) {
                    randomDownMove -= downPieceWidth.get(i);
                }
            } // else randomDownNbPiecesMove = 0 : do nothing

            //Log.d("S1Mdl (genRdmMoves)", "randomDownNbPiecesMove : " + randomDownNbPiecesMove);
            //Log.d("S1Mdl (genRdmMoves)", "randomDownMove : " + randomDownMove);

        }while (randomUpMove == 0 && randomDownMove == 0);

        // to apply moves on the square1 model.
        // applyMoves(randomUpNbPiecesMove, randomDownNbPiecesMove);

        //Log.d("S1Mdl (genRdmMoves)", "randomUpMove : " + randomUpMove);
        //Log.d("S1Mdl (genRdmMoves)", "randomDownMove : " + randomDownMove);


        return new Square1Move(randomUpMove, randomDownMove);
    }

    public void applyMoves(int upPieceMoves, int downPieceMoves){

        //Log.d("Sq1Model (applyMoves)", "BEFORE :");


        //Log.d("Sq1Model (applyMoves)", listToString(upPieceWidth, "upPieceWidth"));
        //Log.d("Sq1Model (applyMoves)", listToString(downPieceWidth, "downPieceWidth"));

        // déplace de upPieceMoves la liste vers la droite (ou gauche si < 0)

        shiftArray(upPieceWidth, -upPieceMoves);
        shiftArray(downPieceWidth, downPieceMoves);

        //Log.d("Sq1Model (applyMoves)", "AFTER :");

        //Log.d("Sq1Model (applyMoves)", listToString(upPieceWidth, "upPieceWidth"));
        //Log.d("Sq1Model (applyMoves)", listToString(downPieceWidth, "downPieceWidth"));
    }

    public void applySlice(){

        //Log.d("Sq1Model (applySlice)", "-----------------------------");
        //Log.d("Sq1Model (applySlice)", "BEFORE :");

        //Log.d("Sq1Model (applyMoves)", listToString(upPieceWidth, "upPieceWidth"));
        //Log.d("Sq1Model (applyMoves)", listToString(downPieceWidth, "downPieceWidth"));

        //Log.d("Sq1Model (applySlice)", "up :");
        final int upNbPieceToCount6 = getNbPiecesToCount6(upPieceWidth);
        //Log.d("Sq1Model (applySlice)", "down :");
        final int downNbPieceToCount6 = getNbPiecesToCount6(downPieceWidth);

        final int upPieceWidthLength = upPieceWidth.size();
        final int downPieceWidthLength = downPieceWidth.size();

        List upFirstPiecesList = upPieceWidth.subList(0, upNbPieceToCount6);
        //Log.d("Sq1Model (applySlice)", listToString(upFirstPiecesList, "upFirstPiecesList"));

        List upLastPiecesList = upPieceWidth.subList(upNbPieceToCount6, upPieceWidthLength);
        //Log.d("Sq1Model (applySlice)", listToString(upLastPiecesList, "upLastPiecesList"));

        List downFirstPiecesList = downPieceWidth.subList(0, downNbPieceToCount6);
        //Log.d("Sq1Model (applySlice)", listToString(downFirstPiecesList, "downFirstPiecesList"));

        List downLastPiecesList = downPieceWidth.subList(downNbPieceToCount6, downPieceWidthLength);
        //Log.d("Sq1Model (applyMoves)", listToString(downLastPiecesList, "downLastPiecesList"));

        Collections.reverse(upFirstPiecesList);
        //Log.d("Sq1Model (applySlice)", listToString(upFirstPiecesList, "upFirstPiecesReversedList"));

        Collections.reverse(downFirstPiecesList);
        //Log.d("Sq1Model (applySlice)", listToString(downFirstPiecesList, "downFirstPiecesReversedList"));


        upPieceWidth = concatenate(downFirstPiecesList, upLastPiecesList);
        downPieceWidth = concatenate(upFirstPiecesList, downLastPiecesList);

        //Log.d("Sq1Model (applySlice)", "AFTER :");

        //Log.d("Sq1Model (applySlice)", listToString(upPieceWidth, "upPieceWidth"));
        //Log.d("Sq1Model (applySlice)", listToString(downPieceWidth, "downPieceWidth"));

    }

    private int getNbPiecesToCount6(List listToCount){
        final int upList3FirstValues = (byte)listToCount.get(0) + (byte)listToCount.get(1) + (byte)listToCount.get(2);
        if(upList3FirstValues == 6){
            return 3;
        }

        final int upList4FirstValues = upList3FirstValues + (byte)listToCount.get(3);
        if(upList4FirstValues == 6){
            return 4;
        }

        final int upList5FirstValues = upList4FirstValues + (byte)listToCount.get(4);
        if(upList5FirstValues == 6){
            return 5;
        }

        final int upList6FirstValues = upList5FirstValues + (byte)listToCount.get(5);
        if(upList6FirstValues == 6){
            return 6;
        }

        return IMPOSIBLE_TO_COUNT_6;
    }

    private void shiftArray(List list, int rightShift){

        if(rightShift > 0){
            for(int i = 0; i < rightShift; i++){
                shiftListToTheRight(list);
            }
        //si le rightShift est négatif, décale vers la gauche
        }else if(rightShift < 0){
            for(int i = 0; i < (-rightShift); i++){
                shiftListToTheLeft(list);
            }
        }

        //ne fait rien si c'est égal à 0
    }

    private void shiftListToTheLeft(List listToShift){

        final int listLength = listToShift.size();

        final byte firstByte = (Byte)listToShift.get(0);

        for(int i = 1; i < listLength; i++){
            listToShift.set(i - 1, listToShift.get(i));
        }
        listToShift.set(listLength - 1, firstByte);
    }

    private void shiftListToTheRight(List listToShift){

        final int listLength = listToShift.size();

        final byte lastByte = (Byte)listToShift.get(listLength - 1);

        for(int i = listLength - 2; i >= 0; i--){
            listToShift.set(i + 1, listToShift.get(i));
        }
        listToShift.set(0, lastByte);
    }

    private List concatenate(List list1, List list2){
        List concatenatedList = new ArrayList();

        final int list1Length = list1.size();
        for(int i = 0; i < list1Length; i++){
            concatenatedList.add(list1.get(i));
        }
        final int list2Length = list2.size();
        for(int i = 0; i < list2Length; i++){
            concatenatedList.add(list2.get(i));
        }

        return concatenatedList;
    }

    private String listToString(List list, String listName){
        String listLog = listName + " = {";
        for(int i = 0; i < (list.size() - 1); i++){
            listLog += (list.get(i) + "; ");
        }
        listLog += (list.get(list.size() - 1) + "}");

        return listLog;
    }

    // TODO : didn't work! Je sais même pas ce que ça fait mdrr
    public boolean isScrambleOk(){

        final List squareList1 = new ArrayList<>(Arrays.asList(new Byte[]{1, 2, 1, 2, 1, 2, 1, 2}));
        final List squareList2 = new ArrayList<>(Arrays.asList(new Byte[]{2, 1, 2, 1, 2, 1, 2, 1,}));

        boolean isUpSquare = false;
        boolean isDownSquare = false;

        if(upPieceWidth.equals(squareList1) || upPieceWidth.equals(squareList2)){
            isUpSquare = true;
        }

        if(downPieceWidth.equals(squareList1) || downPieceWidth.equals(squareList2)){
            isDownSquare = true;
        }

        return !(isUpSquare && isDownSquare);
    }
}

package letsapps.com.letscube.util.square1;

public class Square1Move {
    int upMove, downMove;

    public Square1Move() {
    }

    public Square1Move(int upMove, int downMove) {
        this.upMove = upMove;
        this.downMove = downMove;
    }

    public int getUpMove() {
        return upMove;
    }

    public void setUpMove(int upMove) {
        this.upMove = upMove;
    }

    public int getDownMove() {
        return downMove;
    }

    public void setDownMove(int downMove) {
        this.downMove = downMove;
    }

    @Override
    public String toString(){
        return "(" + upMove + "," + downMove + ")";
    }


}

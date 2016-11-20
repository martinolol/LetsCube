package letsapps.com.letscube.util.Clock;

public class ClockMove {

    int tip;
    int turn;

    public ClockMove(){

    }

    public ClockMove(int tip, int turn) {
        this.turn = turn;
        this.tip = tip;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }
}

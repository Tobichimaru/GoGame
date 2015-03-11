import java.util.ArrayList;

public class Saved_State {
    private int p1_moves;
    private int p2_moves;
    List pieces;

    Saved_State () {
        this.pieces = new List();
        p1_moves = 0;
        p2_moves = 0;
        this.pieces = null;		
    }

    Saved_State (int p1, int p2, List pieces) {
        this.pieces = new List();
        p1_moves = p1;
        p2_moves = p2;
        this.pieces = pieces;
    }

    public void setP1_Moves (int moves) {
        p1_moves = moves;
    }

    public void setP2_Moves (int moves) {
        p2_moves = moves;
    }

    public int getP1_Moves () {
        return p1_moves;
    }

    public int getP2_Moves () {
        return p2_moves;
    }

    public void setPieces (List pieces) {
        this.pieces = pieces;
    }

    public List getPieces () {
        return pieces;
    }
}

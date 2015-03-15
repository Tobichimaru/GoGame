import java.awt.*;
import java.util.ArrayList;

public class Board extends Component implements IBoard {
    private Image board, pwhite, pblack;
    private ArrayList<Piece> pieces;
    private int sizepiece = 28;
    private int xmax, xmin, ymin, ymax; //edges of board
    private int curr_move;
    private Player p1, p2;
	
    Board () {
        pieces = new ArrayList<>();
    }

    Board(Image img, Image w, Image b, Player p1, Player p2) {
        pieces = new ArrayList<>();
        board = img;
        pwhite = w;
        pblack = b;
        ymin = 80;
        ymax = 615;
        xmin = 35;
        xmax = 560;
        this.p1 = p1;
        this.p2 = p2;
        curr_move = 0;
    }

    protected void paintComponent (Graphics g, Frame a) {
        g.drawImage(board, 0, 45, a);
        Piece p = new Piece(0,0,0);
        for (int i = 0; i < curr_move; i++) {
            p = pieces.get(i);
            if (p.getColor() == 0) {
                g.drawImage(pwhite, p.getX(), p.getY(), a);
            } else {
                g.drawImage(pblack, p.getX(), p.getY(), a);
            }
        }
    }

    @Override
    public void Play(Piece p) {
        addPiece(p);
        Piece j = new Piece(0,0,0);
        for (int i = 0; i < curr_move; i++) {
            j = pieces.get(i);
            if (isSurrounded(j.getX(), j.getY(), j.getColor())) {
                System.out.println("remove and surrond");
                pieces.remove(i);
                curr_move--;
            } 
        }
    }
    
    private void addPiece(Piece p) {
        pieces.add(p);
        curr_move++;
    }
    
    @Override
    public void Undo() {
        if (curr_move > 0)
            curr_move--;
    }

    @Override
    public void Redo() {
        if (pieces.size() > curr_move) 
            curr_move++;
    }

    private int isPieceAt(int x, int y) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getX() == x && pieces.get(i).getY() == y) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean removePiece(int x, int y) {
        int pos = isPieceAt(x, y);
        if (pos != -1) {
            pieces.remove(pos);
            return true;
        }
        return false;
    }

    private boolean isSurrounded(int x, int y, int color) {
        if (color == 0)
            color = 1;
        else
            color = 0;

        int up = isPieceAt(x, y - sizepiece);
        int down = isPieceAt(x, y + sizepiece);
        int left = isPieceAt(x - sizepiece, y);
        int right = isPieceAt(x + sizepiece, y);
        
        if (up != -1 && down != -1 && left != -1 && right != -1) {
            if (pieces.get(up).getColor() == color &&
                pieces.get(down).getColor() == color &&
                pieces.get(left).getColor() == color &&
                pieces.get(right).getColor() == color) {
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPlayable(int x, int y, int color) {
        if (x < xmin || y < ymin || x > xmax || y > ymax) {
            return false;
        } 
        if (pieces.isEmpty() == false) {
            if (isPieceAt(x, y) != -1 || isSurrounded(x, y, color)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Piece getPieceAt(int x, int y) {
        Piece result = new Piece(0, 0, 0);
        if (isPieceAt(x,y) == -1) {
            return result;
        }
        result = pieces.get(isPieceAt(x,y));
        return result;
    }

    @Override
    public boolean DeleteLibsOf(Piece p, Piece caller) {
        return true;
    }

    @Override
    public void CheckLibs (Piece p) {
    }


    @Override
    public void SaveState() {
        Saved_State save_state = new Saved_State();
        save_state.setP1_Moves(p1.getMoves ());
        save_state.setP2_Moves(p2.getMoves ());
        save_state.setPieces(pieces);
    }
    
    @Override
    public void Score (Player p1, Player p2) {
    }
}

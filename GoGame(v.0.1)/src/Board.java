import java.awt.*;
import java.util.ArrayList;

public class Board extends Component implements IBoard {
    private Image board_img, white_stone, black_stone;
    private ArrayList<Stone> pieces;
    private int array_board [][]; 
    private int curr_move;
    private Player p1, p2;
	
    Board () {
        pieces = new ArrayList<>();
    }

    Board(Image img, Image w, Image b, Player p1, Player p2) {
        pieces = new ArrayList<>();
        array_board = new int[20][20];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                array_board[i][j] = -1;
            }
        }
        board_img = img;
        white_stone = w;
        black_stone = b;
        this.p1 = p1;
        this.p2 = p2;
        curr_move = 0;
    }

    protected void paintComponent(Graphics g, Frame a) {
        g.drawImage(board_img, 0, 45, a);
        Stone p = new Stone();
        for (int i = 0; i < curr_move; i++) {
            p = pieces.get(i);
            if (p.isVisible()) {
                if (p.getColor() == 0) {
                    g.drawImage(black_stone, p.getX(), p.getY(), a);
                } else {
                    g.drawImage(white_stone, p.getX(), p.getY(), a);
                }
            }
        }
    }

    @Override
    public void Play(int x, int y, int color) {
        System.out.println(x);
        System.out.println(y);
        System.out.println(x/cellsize);
        System.out.println(y/cellsize);
        if (x < xmin || y < ymin || x > xmax || y > ymax 
                || array_board[x/cellsize][y/cellsize] != -1) {
            return;
        } 
        int other_color;
        if (color == 0)
            other_color = 1;
        else
            other_color = 0;
        
        int dead_groups = 0;
        if (array_board[x/cellsize + 1][y/cellsize] != -1 &&
                isSurrounded(x/cellsize + 1, y/cellsize, other_color)) {
            dead_groups++;
        }
        if (array_board[x/cellsize - 1][y/cellsize] != -1 &&
                isSurrounded(x/cellsize - 1, y/cellsize, other_color)) {
            dead_groups++;
        }
        if (array_board[x/cellsize][y/cellsize + 1] != -1 &&
                isSurrounded(x/cellsize, y/cellsize + 1, other_color)) {
            dead_groups++;
        }
        if (array_board[x/cellsize][y/cellsize - 1] != -1 &&
                isSurrounded(x/cellsize, y/cellsize - 1, other_color)) {
            dead_groups++;
        }
        Stone s = new Stone(x, y, curr_move);
        addStone(s);
    }
    
     private boolean isSurrounded(int x, int y, int color) {
        Stone p = pieces.get(array_board[x][y]);
        if (!p.isVisible()) {
            return false;
        }
        if (color == 0)
            color = 1;
        else
            color = 0;
        
        return false;
    }
    
    private void addStone(Stone p) {
        pieces.add(p);
        int x = p.getX()/cellsize;
        int y = p.getY()/cellsize;
        array_board[x][y] = pieces.size();
        curr_move++;
    }
    
    @Override
    public void Undo() {
        if (curr_move > 0) {
            curr_move--;
            pieces.get(curr_move).setVisible(true);
        }
    }

    @Override
    public void Redo() {
        if (pieces.size() > curr_move) {
            curr_move++;
        }
    }

    @Override
    public boolean removeStone(int x, int y) {
        int pos = array_board[x][y];
        if (pos != -1) {
            pieces.remove(pos);
            return true;
        }
        return false;
    }

    @Override
    public void Score (Player p1, Player p2) {
    }
}

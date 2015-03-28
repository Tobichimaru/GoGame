import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Board extends Component implements IBoard {
    private Image board_img, white_stone, black_stone;
    private LinkedList<Stone> moves_stack;
    private ArrayList<Stone> stone_list;
    private int stone_array[][]; 
    private int curr_move;
    private Player p1, p2;
	
    Board() {
        initilize();
    }

    Board(Image img, Image w, Image b, Player p1, Player p2) {
        initilize();
        board_img = img;
        white_stone = w;
        black_stone = b;
        this.p1 = p1;
        this.p2 = p2;
    }
    
    private void initilize() {
        stone_list = new ArrayList<>();
        moves_stack = new LinkedList<>();
        stone_array = new int[20][20];
        initlizeArray();
        curr_move = 0;
    }
    
    private void initlizeArray() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                stone_array[i][j] = -1;
            }
        }
    }

    protected void paintComponent(Graphics g, Frame a) {
        g.drawImage(board_img, 0, 45, a);
        for (Stone s : stone_list) {
            if (s.getColor() == 0) {
                g.drawImage(black_stone, s.getX(), s.getY(), a);
            } else {
                g.drawImage(white_stone, s.getX(), s.getY(), a);
            }
        }
    }

    public void Play(int x, int y, int color) {
       // System.out.println(x);
       // System.out.println(y);
       // System.out.println(x/cellsize);
       // System.out.println(y/cellsize);
        
        if (x < xmin || y < ymin || x > xmax || y > ymax 
                || stone_array[x/cellsize][y/cellsize] != -1) {
            return;
        } 
        Stone s = new Stone(x, y, x/cellsize, y/cellsize, color);
        //int other_color = (color == 0)? 1: 0;
        addStone(s);
    }
    
     private boolean isSurrounded(int x, int y, int color) {
        return false;
    }
    
    private void addStone(Stone s) {
        stone_list.add(s);
        moves_stack.add(s);
        stone_array[s.getXPos()][s.getYPos()] = stone_list.size();
        curr_move++;
    }
    
    private void clear() {
        stone_list.clear();
        initlizeArray();
        Stone s = new Stone();
        for (int i = 0; i < curr_move; i++) {
            s = moves_stack.get(i);
             //Play()
            stone_list.add(s);
            stone_array[s.getXPos()][s.getYPos()] = stone_list.size();
        }
    }
    
    
    @Override
    public void Undo() {
        if (curr_move > 0) {
            curr_move--;
            clear();
        }
    }

    @Override
    public void Redo() {
        if (moves_stack.size() > curr_move) {
            curr_move++;
            clear();
        }
    }

    public boolean removeStone(int x, int y) {
        int pos = stone_array[x][y];
        if (pos != -1) {
            stone_list.remove(pos);
            stone_array[x][y] = -1;
            return true;
        }
        return false;
    }

}

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JPanel;

public class Board extends Component implements IBoard, Serializable {
    private Image board_img, white_stone, black_stone;
    private LinkedList<Stone> moves_stack;
    private ArrayList<Stone> stone_list, q;
    private int stone_array[][]; 
    private boolean marks[][];
    private int curr_move;
    private Player p1, p2;    
    
    private static final long serialVersionUID = -2518143671167959230L;
	
    Board() {
        initialize();
    }

    Board(Image img, Image w, Image b, Player p1, Player p2) {
        initialize();
        board_img = img;
        white_stone = w;
        black_stone = b;
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public void setImages(Image img, Image w, Image b) {
        board_img = img;
        white_stone = w;
        black_stone = b;
    }
    
    private void initialize() {
        stone_list = new ArrayList<>();
        moves_stack = new LinkedList<>();
        q = new ArrayList<>();
        stone_array = new int[20][20];
        marks = new boolean[20][20];
        initializeArray();
        initializeMarks();
        curr_move = 0;
    }
    
    private void initializeArray() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                stone_array[i][j] = -1;
            }
        }
    }
    
    private void initializeMarks() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                 marks[i][j] = false;
            }
        }
    }

    protected void paintComponent(Graphics g, JPanel a) {
        g.drawImage(board_img, 0, 0, a);
        for (Stone s : stone_list) {
            if (s.getColor() == 0) {
                g.drawImage(black_stone, s.getX(), s.getY(), a);
            } else {
                g.drawImage(white_stone, s.getX(), s.getY(), a);
            }
        }
    }

    /*
    * Play() method returns true if the stone was sucessfully placed on board, 
    * else returns false;
    */
    public boolean Play(int x, int y, int color, boolean rewrite) {    
        int xpos = x/cellsize - 1;
        int ypos = y/cellsize - 1;
        if (stone_array[xpos][ypos] != -1) {
            return false;
        } 
        Stone s = new Stone(x, y, xpos, ypos, color);
        int other_color = (color == 0)? 1: 0;
        int surr_stones = 0;
        if (rewrite) {
            placeStone(s);
        } else { 
            addStone(s);
        }
        
        surr_stones += isSurrounded(xpos, ypos + 1, other_color, true);
        surr_stones += isSurrounded(xpos, ypos - 1, other_color, true);
        surr_stones += isSurrounded(xpos + 1, ypos, other_color, true);
        surr_stones += isSurrounded(xpos - 1, ypos, other_color, true);
        if (surr_stones == 0 && isSurrounded(xpos, ypos, s.getColor(), false) == 1) {
            removeStone(s);
            return false;
        }
        return true;
    }
    
    /*
    * isSurrounded() method returns 1 if the stone grop is surrounded, 
    * else returns 0;
    */
    private int isSurrounded(int x, int y, int color, boolean delete) {
        Stone curr = new Stone();
        if (x < 0 || y < 0 || x > 18 || y > 18) {
            return 0;
        } 
        if (stone_array[x][y] == -1) { 
            return 0;
        }
        Stone s = find(x, y);
        if (s.getColor() != color) {
            return 0;
        }
        if (!findGroup(s, delete)) {
            return 0;
        }
        return 1;
    }
    
    
    /*
    * findGroup(Stone s) finds a connected group of given stone. If this group
    * is surrounded, this method remove the gruop of stones from the board and
    * returns true. In other case this group is remain on boerd and method 
    * retuns false;
    */
    private boolean findGroup(Stone s, boolean delete) {
        initializeMarks();
        q.clear();
        marks[s.getXPos()][s.getYPos()] = true;
        q.add(s);
        marks[s.getXPos()][s.getYPos()] = true;
        Stone curr = q.get(0);
        int x, y;
        int last = 1;
        while (last <= q.size()) {
            x = curr.getXPos();
            y = curr.getYPos();
            int sum = checkNeighborStone(x+1, y, curr.getColor()) +
                checkNeighborStone(x-1, y, curr.getColor()) +
                checkNeighborStone(x, y+1, curr.getColor()) +
                checkNeighborStone(x, y-1, curr.getColor());
            if (sum < 4) {
                return false;
            }
            if (last < q.size()) {
                curr = q.get(last);
            }
            last++;
        } 
        if (delete) {
            for (Stone st : q) {
                this.removeStone(st);
            }
        }
        return true;
    } 
    
    /*
    * This method checks the stone on existance. If the stone satisfy 
    * requerements, than it will be added in the queue. If checked place is
    * empty - return 0, else return 1.
    */
    private int checkNeighborStone(int x, int y, int color) {
        if (x < 0 || y < 0 || x > 18 || y > 18) {
            return 1;
        } 
        Stone other = new Stone();
        if (stone_array[x][y] == -1) {
            return 0;
        }
        if (marks[x][y] == false) {
            other = find(x, y);
            if (other.getColor() == color) {
                q.add(other);
                marks[x][y] = true;
            }
        }
        return 1;
    }
    
    private Stone find(int x, int y) {
        for (Stone s : stone_list) {
            if (s.getXPos() == x && s.getYPos() == y) 
                return s;
        }
        return new Stone();
    }
    
    private void addStone(Stone s) {
        stone_list.add(s);
        moves_stack.add(s);
        stone_array[s.getXPos()][s.getYPos()] = 1;
        curr_move++;
    }
    
    private void placeStone(Stone s) {
        stone_list.add(s);
        stone_array[s.getXPos()][s.getYPos()] = 1;
    }
    
    private void rewriteArray() {
        stone_list.clear();
        initializeArray();
        
        Stone s = new Stone();
        System.out.println(moves_stack.size());
        for (int i = 0; i < curr_move; i++) {
            s = moves_stack.get(i);
            Play(s.getX(), s.getY(), s.getColor(), true);
        }
    }
    
    
    @Override
    public void Undo() {
        if (curr_move > 0) {
            curr_move--;
            rewriteArray();
        }
    }

    @Override
    public void Redo() {
        if (moves_stack.size() > curr_move) {
            curr_move++;
            rewriteArray();
        }
    }

    @Override
    public boolean removeStone(Stone s) {
        int pos = stone_list.indexOf(s);
        if (pos != -1) {
            stone_list.remove(pos);
            stone_array[s.getXPos()][s.getYPos()]= -1;
            return true;
        }
        return false;
    }
    
    public void clear() {
        moves_stack.clear();
        stone_list.clear();
        initializeArray();
    }
    
    private void writeObject(ObjectOutputStream o) throws IOException {  
        o.writeObject(p1);  
        o.writeObject(p2);
        o.writeObject(curr_move);
        o.writeObject(moves_stack);
        o.writeObject(stone_list);
        o.writeObject(stone_array);
    }
  
    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {  
        initialize();
        p1 = (Player) o.readObject();  
        p2 = (Player) o.readObject();
        curr_move = (int) o.readObject();
        moves_stack = (LinkedList<Stone>) o.readObject();
        stone_list = (ArrayList<Stone>) o.readObject();
        stone_array = (int[][]) o.readObject();
    }
    
    public int getTurn() {
        return moves_stack.get(curr_move - 1).getColor();
    }
}

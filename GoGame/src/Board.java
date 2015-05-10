import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import javax.swing.JPanel;

/**
 * Implements the game board with grid
 *
 * @author Saia
 */
public class Board extends Component implements IBoard, Serializable {
    private Image board_img, white_stone, black_stone;
    private LinkedList<Stone> moves_stack;
    private ArrayList<Stone> stone_list, q;
    private Queue<Stone> terr_q;
    private int stone_array[][]; 
    private boolean marks[][];
    private int curr_move;
    public Player p1, p2;    
    int black, white;
    private ArrayList<Integer> hashes;
    
    private static final long serialVersionUID = -2518143671167959230L;
	
    Board() {
        initialize();
    }

    /**
     * Constructor of class with all needed params
     * @param img - image of board
     * @param w - image of white stone
     * @param b - image of balck stone
     */
    Board(Image img, Image w, Image b, Player p1, Player p2) {
        initialize();
        board_img = img;
        white_stone = w;
        black_stone = b;
        this.p1 = p1;
        this.p2 = p2;
    }
    
    /**
     * Set all images for the board
     * @param img - image of board
     * @param w - image of white stone
     * @param b - image of balck stone
     */
    public void setImages(Image img, Image w, Image b) {
        board_img = img;
        white_stone = w;
        black_stone = b;
    }
    
    private void initialize() {
        stone_list = new ArrayList<>();
        moves_stack = new LinkedList<>();
        q = new ArrayList<>();
        terr_q = new LinkedList<>();
        stone_array = new int[20][20];
        hashes = new ArrayList<>();
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

    /**
     * Paint all stones on the board
     * @param g - object of Graphics
     * @param a - panel for drawing
     */
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

    /**
     * Used for validation of stone
     * @param x - the x-coordinate of stone on board
     * @param y - the y-coordinate of stone on board
     * @param color - the color of stone
     * @param rewrite - true, if we place this stone for the first time
     * @return true if the stone was sucessfully placed on board, 
     * else returns false
     */
    public boolean Play(int x, int y, int color, boolean rewrite) { 
        System.out.println("play");
        p1.setPass(false);
        p2.setPass(false);
        
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
        
        Integer h = hashCode();
        for (Integer hash : hashes) {
            if (hash.equals(h)) {
                removeStone(s);
                Undo();
                return false;
            }
        }
        hashes.add(h);
        return true;
    }
    
    /**
     * Used for validation if the stone is surrounded by enemy's stones
     * @param x - the x-coordinate of stone in array
     * @param y - the y-coordinate of stone in array
     * @param color - the color of stone
     * @param delete - true if we are not allowed to delete the stone
     * @return returns 1 if the stone grop is surrounded, else returns 0;
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
    
    /**
     * finds a connected group of given stone
     * @param s - Stone for finding the group
     * @param delete - true if we are not allowed to delete the stone
     * @return If this group is surrounded, this method remove the group of 
     * stones from the board and returns true. In other case this group is 
     * remain on board and method retuns false;
     */
    private boolean findGroup(Stone s, boolean delete) {
        initializeMarks();
        q.clear();
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
        if (delete && !q.isEmpty()) {
            if (q.get(0).getColor() == 0) {
                p1.increaseScore(-q.size());
            } else {
                p2.increaseScore(-q.size());
            }
            for (Stone st : q) {
                this.removeStone(st);
            }
        }
        return true;
    } 
    
    /**
     * This method checks the stone on existance. 
     * @param x - the x-coordinate of stone in array
     * @param y - the y-coordinate of stone in array
     * @param color - the color of stone
     * @return If the stone satisfy requerements, than it will be added in the 
     * queue. If checked place is empty - return 0, else return 1.
     */
    private int checkNeighborStone(int x, int y, int color) {
        if (x < 0 || y < 0 || x > 18 || y > 18) {
            return 1;
        } 
        Stone other;
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
    
    public Stone findStoneByPos(int x, int y) {
        for (Stone s : stone_list) {
            if (s.getX() == x && s.getY() == y) 
                return s;
        }
        return new Stone();
    }
    
    private void addStone(Stone s) {
        stone_list.add(s);
        while (curr_move < moves_stack.size()) {
            moves_stack.removeLast();
        }
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
        hashes.clear();
        initializeArray();
        Stone s;
        for (int i = 0; i < curr_move; i++) {
            s = moves_stack.get(i);
            Play(s.getX(), s.getY(), s.getColor(), true);
        }
    }
    
    
    /**
     * This method cancel the last move. 
     */
    @Override
    public void Undo() {
        if (curr_move > 0) {
            curr_move--;
            rewriteArray();
        }
    }

    /**
     * This method returns the canceled move. 
     */
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
        o.writeObject(hashes);
    }
  
    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {  
        initialize();
        p1 = (Player) o.readObject();  
        p2 = (Player) o.readObject();
        curr_move = (int) o.readObject();
        moves_stack = (LinkedList<Stone>) o.readObject();
        stone_list = (ArrayList<Stone>) o.readObject();
        stone_array = (int[][]) o.readObject();
        hashes = (ArrayList<Integer>) o.readObject();
    }
    
    public int getTurn() {
        if (curr_move > 1) {
            return moves_stack.get(curr_move - 1).getColor();
        } else {
            return 0;
        }
    }
    
    /**
     * This method calculates score in the end of the game
     */
    public void calculateScore() {
        initializeMarks();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (stone_array[i][j] == -1 && !marks[i][j]) {
                    findTerritory(i, j);
                }
            }
        }
        p2.increaseScore(5.5);
    }
   
    private boolean findTerritory(int x, int y) {
        Stone s = new Stone(x, y, x, y, -1);
        terr_q.clear();
        terr_q.add(s);
        marks[s.getXPos()][s.getYPos()] = true;
        Stone curr;
        white = 0;
        black = 0;
        int count = 0;
        int xx, yy;
        while (!terr_q.isEmpty()) {
            curr = terr_q.poll();
            xx = curr.getXPos();
            yy = curr.getYPos();
            checkNeighborTerritory(xx+1, yy);
            checkNeighborTerritory(xx-1, yy);
            checkNeighborTerritory(xx, yy+1);
            checkNeighborTerritory(xx, yy-1);
            count++;
        } 
        
        if (white == 0 && black > 0) {
            p1.increaseScore(count);
        } else if (white != 0 && black == 0) {
            p2.increaseScore(count);
        }
        
        return true;
    } 
    
    private int checkNeighborTerritory(int x, int y) {
        if (x < 0 || y < 0 || x > 18 || y > 18) {
            return 1;
        } 
        Stone other;
        if (marks[x][y] == false && stone_array[x][y] == -1) {
            other = new Stone(x, y, x, y, -1);
            terr_q.add(other);
            marks[x][y] = true;
        } else if (stone_array[x][y] != -1) {
            other = find(x, y);
            if (other.getColor() == 0) {
                black++;
            } else {
                white++;
            }
        }
        return 1;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.stone_list);
        return hash;
    }
    
}

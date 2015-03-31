import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Board extends Component implements IBoard, Serializable {
    private Image board_img, white_stone, black_stone;
    private LinkedList<Stone> moves_stack;
    private ArrayList<Stone> stone_list;
    private int stone_array[][]; 
    private int curr_move;
    private Player p1, p2;
    private ArrayList<Stone> q;
    private boolean marks[][]; 
    
    private static final long serialVersionUID = -2518143671167959230L;
	
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
    
    public void setImages(Image img, Image w, Image b) {
        board_img = img;
        white_stone = w;
        black_stone = b;
    }
    
    private void initilize() {
        stone_list = new ArrayList<>();
        moves_stack = new LinkedList<>();
        q = new ArrayList<>();
        stone_array = new int[20][20];
        marks = new boolean[20][20];
        initlizeArray();
        curr_move = 0;
    }
    
    private void initlizeArray() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                stone_array[i][j] = -1;
                marks[i][j] = false;
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
        if (x < xmin || y < ymin || x > xmax || y > ymax 
                || stone_array[x/cellsize - 1][y/cellsize - 2] != -1) {
            return;
        } 
        System.out.print(x/cellsize - 1);
        System.out.print(' ');
        System.out.println(y/cellsize - 2);
        Stone s = new Stone(x, y, x/cellsize - 1, y/cellsize - 2, color);
        int other_color = (color == 0)? 1: 0;
        int surr_stones = 0;
        addStone(s);
        surr_stones += isSurrounded(s.getXPos(), s.getYPos() + 1, other_color);
        surr_stones += isSurrounded(s.getXPos(), s.getYPos() - 1, other_color);
        surr_stones += isSurrounded(s.getXPos() + 1, s.getYPos(), other_color);
        surr_stones += isSurrounded(s.getXPos() - 1, s.getYPos(), other_color);
        if (surr_stones == 0 && isSurrounded(s.getXPos(), s.getYPos(), s.getColor()) == 1) {
            removeStone(s.getXPos(), s.getYPos());
        }
    }
    
    private int isSurrounded(int x, int y, int color) {
        System.out.print("isSurrounded: ");
        System.out.print(x);
        System.out.print(' ');
        System.out.print(y);
        System.out.print(' ');
        System.out.println(color);
        Stone curr = new Stone();
        if (x < 0 || y < 0 || x > 18 || y > 18) {
            return 0;
        } 
        if (stone_array[x][y] == -1) { 
            return 0;
        }
       
        Stone s = stone_list.get(stone_array[x][y]);
        if (s.getColor() != color) {
            return 0;
        }
        if (!findGroup(s)) {
            return 0;
        }
        return 1;
    }
    
    //false - has empty neighbor
    private boolean findGroup(Stone s) {
        q.clear();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                marks[i][j] = false;
            }
        }
        q.add(s);
        marks[s.getXPos()][s.getYPos()] = true;
        System.out.print("find Group: ");
        System.out.print(s.getXPos());
        System.out.print(' ');
        System.out.print(s.getYPos());
        System.out.print(' ');
        System.out.println(s.getColor());
        Stone curr = new Stone();
        int x, y;
        int last = 0;
        while (last < q.size()) {
            curr = q.get(last);
            x = curr.getXPos();
            y = curr.getYPos();
            if (!checkStone(x+1, y, curr.getColor()) ||
                !checkStone(x-1, y, curr.getColor()) ||
                !checkStone(x, y+1, curr.getColor()) ||
                !checkStone(x, y-1, curr.getColor())) {
                return false;
            }
            last++;
        }
        System.out.println("findGroup true : ");
        for (Stone st : q) {
             System.out.print("delete queue : ");
            System.out.print(st.getXPos());
            System.out.print(' ');
            System.out.print(st.getYPos());
            System.out.print(' ');
            System.out.println(st.getColor());
            this.removeStone(st.getXPos(), st.getYPos());
        }
        return true;
    } 
    
     //false - has empty neighbor
    private boolean checkStone(int x, int y, int color) {
        if (x < 0 || y < 0 || x > 18 || y > 18) {
            return true;
        } 
         System.out.print("checkStone: ");
        System.out.print(x);
        System.out.print(' ');
        System.out.print(y);
        System.out.print(' ');
        System.out.println(color);
        Stone other = new Stone();
        if (stone_array[x][y] != -1 && marks[x][y] == false) {
            other = stone_list.get(stone_array[x][y]);
            if (other.getColor() == color) {
                q.add(other);
                marks[x][y] = true;
            }
            return true;
        } else {
            return false;
        }
    }
    
    private void addStone(Stone s) {
        stone_list.add(s);
        moves_stack.add(s);
        stone_array[s.getXPos()][s.getYPos()] = stone_list.size() - 1;
        curr_move++;
    }
    
    private void rewriteArray() {
        stone_list.clear();
        initlizeArray();
        Stone s = new Stone();
        for (int i = 0; i < curr_move; i++) {
            s = moves_stack.get(i);
             //Play()
            stone_list.add(s);
            stone_array[s.getXPos()][s.getYPos()] = stone_list.size() - 1;
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
    public boolean removeStone(int x, int y) {
        int pos = stone_array[x][y];
        if (pos != -1) {
            stone_list.remove(pos);
            initlizeArray();
            for (Stone s: stone_list) {
                stone_array[s.getXPos()][s.getYPos()] = stone_list.size() - 1;
            }
            repaint();
            return true;
        }
        return false;
    }

    public void clear() {
        moves_stack.clear();
        stone_list.clear();
        this.initlizeArray();
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
        System.out.print("readObject");
        p1 = (Player) o.readObject();  
        p2 = (Player) o.readObject();
        curr_move = (int) o.readObject();
        moves_stack = (LinkedList<Stone>) o.readObject();
        stone_list = (ArrayList<Stone>) o.readObject();
        stone_array = (int[][]) o.readObject();
        q = new ArrayList<>();
        marks = new boolean[20][20];
         for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                marks[i][j] = false;
            }
        }
        System.out.println(moves_stack.size());
    }
}

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Stone extends Component implements Serializable {
    private int color; // 0 black 1 white
    private int x, y; // position on board
    private int xpos, ypos; // index in matrix
    
    Stone() {
        x = 0;
        y = 0;
        xpos = 0;
        ypos = 0;
        color = 0;
    }
    
     Stone(int x, int y, int xpos, int ypos, int c) {
        this.x = x; 
        this.y = y;
        this.xpos = xpos; 
        this.ypos = ypos;
        color = c;
    }

    public void setColor(int color) {
        this.color = color;
    }
    
    public int getColor() {
        return color;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    @Override
    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    @Override
     public int getY() {
        return y;
    }

    public void setXPos(int x) {
        this.xpos = x;
    }
    
    public int getXPos() {
        return xpos;
    }

    public void setYPos(int y) {
        this.ypos = y;
    }
    
     public int getYPos() {
        return ypos;
    }

    protected void paintComponent(Graphics g, Frame a, int x, int y, Image p) {
        g.drawImage(p, x, y, a);
    }
    
    private void writeObject(ObjectOutputStream o) throws IOException {  
        o.writeObject(color);
        o.writeObject(x);
        o.writeObject(y);
        o.writeObject(xpos);
        o.writeObject(ypos);
    }
  
    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {  
        color = (int) o.readObject();
        x = (int) o.readObject();
        y = (int) o.readObject();
        xpos = (int) o.readObject();
        ypos = (int) o.readObject();
    }
    
    public void print() { 
        System.out.print("Stone: ");
        System.out.print(x);
        System.out.print(' ');
        System.out.print(y);
        System.out.print(' ');
        System.out.println(color);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.color;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        hash = 71 * hash + this.xpos;
        hash = 71 * hash + this.ypos;
        return hash;
    }
    
    @Override
    public String toString() {
        return Integer.toString(x) + ' ' +Integer.toString(y) + ' ' 
                + Integer.toString(color);
    }
    
    public void fromString(String s) {
        int p = s.indexOf(" ");
        x = Integer.parseInt(s.substring(0, p));
        s = s.substring(p+1);
        p = s.indexOf(" ");
        y = Integer.parseInt(s.substring(0, p));
        s = s.substring(p+1);
        color = Integer.parseInt(s);
    }
}

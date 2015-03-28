import java.awt.*;

public class Stone extends Component {
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
}

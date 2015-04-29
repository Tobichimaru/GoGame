import java.awt.*;

public class Stone extends Component {
    private int color; // 0 black 1 white
    private int x, y; // position on board
    private int liberties;
    private boolean visible;
    
    Stone() {
        x = 0;
        y = 0;
    }

    Stone(int x, int y, int c) {
        this.x = x; this.y = y;
        color = c;
        liberties = 0;
        visible = true;
    }

    public void setLiberties(int l) {
        liberties = l;
    }

    public int getLiberties() {
        return liberties;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public void setVisible(boolean vis) {
        visible = vis;
    }

    protected void paintComponent(Graphics g, Frame a, int x, int y, Image p) {
        g.drawImage(p, x, y, a);
    }
}

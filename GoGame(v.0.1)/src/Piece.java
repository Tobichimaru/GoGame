import java.awt.*;

public class Piece extends Component {
    private int color; // 0 black 1 white
    private int x, y; // position on board
    private int liberties;
    private int checked;
    private boolean bool;

    Piece(int x, int y, int c) {
        System.out.println("Piece()");
        this.x = x; this.y = y;
        color = c;
        liberties = 0;
    }

    public void setLiberties(int l) {
        liberties = l;
    }

    public int getLiberties() {
        return liberties;
    }

    public void setChecked(int c) {
        checked = c;
    }

    public int getChecked() {
        return checked;
    }

    public void setBool(boolean c) {
        bool = c;
    }

    public boolean getBool() {
        return bool;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected void paintComponent(Graphics g, Frame a, int x, int y, Image p) {
        System.out.println("Piece paint()");
        g.drawImage(p, x, y, a);
    }
}

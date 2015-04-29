
import java.awt.Graphics;
import javax.swing.JPanel;


/**
 * @author Saia
 */
public class DrawPanel extends JPanel {
    public Board board;
    
    DrawPanel(Board b) {
        board = b;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        board.paintComponent(g, this);
    }
}

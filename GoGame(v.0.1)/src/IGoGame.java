import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface IGoGame extends MouseListener {
        
    void Score();
    void Undo();
    void Redo();
    
    void newGame();
    void restartGame();
    void loadGame();
    void saveGame();
    void exitGame();
    
    void mouseEntered(MouseEvent e);
    void mouseExited(MouseEvent e);
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseClicked(MouseEvent e);
    boolean mouseDownServer(int x, int y);
}

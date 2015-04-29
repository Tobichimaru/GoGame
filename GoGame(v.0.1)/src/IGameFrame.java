import java.awt.Event;
import java.awt.Graphics;

public interface IGameFrame {
    void syncMenu(); 
    void paint(Graphics g);
    boolean action(Event e, Object o);
}

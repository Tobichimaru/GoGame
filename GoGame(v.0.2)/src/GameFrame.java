import java.awt.*;
import javax.swing.JFrame;

public class GameFrame extends Frame implements IGameFrame {
    protected MenuBar menubar;
    protected MenuItem newGameItem, restartGameItem, loadGameItem, saveGameItem, exitGameItem;
    protected MenuItem undoItem, redoItem;
    protected Menu m1, m2;
    protected GoGame game;
    protected int width, height;
    protected Panel p;
    protected Image w, b, img;
    protected Player p1, p2;
    public Board board;

    public GameFrame(JFrame app, int width, int height, Player p1, Player p2) {
        super("GoGame");
        
        img = Toolkit.getDefaultToolkit().getImage("src\\board.png");
        w = Toolkit.getDefaultToolkit().getImage("src\\white.png"); 
        b = Toolkit.getDefaultToolkit().getImage("src\\black.png");
        this.p1 = p1;
        this.p2 = p2;
        
        board = new Board(img, w, b, p1, p2);
        this.width = width;
        this.height = height;
        this.game = (GoGame)app;
        setResizable(false);
        syncMenu();
        resize(605, 645);
    }
    
    public void syncMenu() {
        menubar = new MenuBar();
       
        newGameItem = new MenuItem("New Game");
        restartGameItem = new MenuItem("Restart Game");
        loadGameItem = new MenuItem("Load Game");
        saveGameItem = new MenuItem("Save Game");
        exitGameItem = new MenuItem("Exit Game");

        m1 = new Menu("Game");
        m1.add(newGameItem);
        m1.add(restartGameItem);
        m1.add(loadGameItem);
        m1.add(saveGameItem);
        m1.add(exitGameItem);
        menubar.add(m1);

        undoItem = new MenuItem("Undo");
        redoItem = new MenuItem("Redo");
        
        m2 = new Menu("Edit");
        m2.add(undoItem);
        m2.add(redoItem);
        menubar.add(m2);

        setMenuBar(menubar);
    }
    
    @Override
    public void paint(Graphics g) {
        board.paintComponent(g, this);
    }

    @Override
    public boolean action(Event e, Object o) {
        if (e.target instanceof MenuItem) {
            String s = (String)o;
            if (e.target == newGameItem) {
                game.newGame();
            } else if (e.target == restartGameItem) {
                game.restartGame();
            } else if (e.target == loadGameItem) {
                game.loadGame();
                board.setImages(img, w, b);
                
            } else if (e.target == saveGameItem) {
                game.saveGame();
            } else if (e.target == exitGameItem) {
                hide();
                game.exitGame();
            } else if (e.target == undoItem) {
                game.Undo();
            } else if (e.target == redoItem) {
                game.Redo();
            }

            return true;
        }

        return false;
    }
}

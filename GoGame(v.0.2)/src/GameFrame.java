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
    protected Board board;

    public GameFrame(JFrame app, int width, int height, Board board) {
        super("GoGame");
        this.board = board;
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

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.lang.System.exit;

public class GoGame extends JFrame implements IGoGame, MouseListener {
    private MenuBar menubar;
    private MenuItem newGameItem, restartGameItem, loadGameItem, saveGameItem, exitGameItem;
    private MenuItem undoItem, redoItem, passItem, howtoItem;
    private Menu m1, m2, m3;
    private Player p1, p2;
    private Image w, b, img;
    private int turn;
    private DrawPanel panel;
    
    private static final long serialVersionUID = -250003671167959230L;
    
    public GoGame() {
        LocalPlayersNameWindow gameSetup = new LocalPlayersNameWindow();
        gameSetup.show();
        p1 = new Player(gameSetup.getPlayerOne());
        p2 = new Player(gameSetup.getPlayerTwo());
        img = Toolkit.getDefaultToolkit().getImage("src\\board.png");
        w = Toolkit.getDefaultToolkit().getImage("src\\white.png"); 
        b = Toolkit.getDefaultToolkit().getImage("src\\black.png");
        syncMenu();
        addMouseListener(this);
        turn = 0;
        panel = new DrawPanel(new Board(img, w, b, p1, p2));
        resize(605, 645);
        setResizable(false);
        setTitle("Go Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       
        add(panel);
        setVisible(true);
    }
    
    private void syncMenu() {
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
        passItem = new MenuItem("Pass");
        
        m2 = new Menu("Edit");
        m2.add(undoItem);
        m2.add(redoItem);
        m2.add(passItem);
        menubar.add(m2);
        
        howtoItem = new MenuItem("How to play?");
        m3 = new Menu("Help");
        m3.add(howtoItem);
        menubar.add(m3);

        setMenuBar(menubar);
    }

    @Override
    public boolean action(Event e, Object o) {
        if (!(e.target instanceof MenuItem)) {
            return false;
        }
        String s = (String)o;
        if (e.target == newGameItem) {
            newGame();
        } else if (e.target == restartGameItem) {
            restartGame();
        } else if (e.target == loadGameItem) {
            loadGame();
            panel.board.setImages(img, w, b);
        } else if (e.target == saveGameItem) {
            saveGame();
        } else if (e.target == exitGameItem) {
            exitGame();
        } else if (e.target == undoItem) {
            Undo();
        } else if (e.target == redoItem) {
            Redo();
        } else if (e.target == passItem) {
            if (turn == 0) {
                p1.setPass(true);
            } else {
                p2.setPass(true);
            }
            if (p1.getPass() == true && p2.getPass () == true)
                Score();	
        } else if (e.target == howtoItem) {
            HelpWindow helpWindow = new HelpWindow();
            helpWindow.show();
        }
        panel.repaint();
        return true;
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
 
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() < xmin || e.getY() < ymin || e.getX() > xmax || e.getY() > ymax) {
            return;
        }
        
        int x = ((int)(e.getX() - xmargin)/cellsize) * cellsize + xmargin;
        int y = ((int)(e.getY()/cellsize)) * cellsize - ymargin;
        
        if (panel.board.Play(x, y, turn, false)) {
            turn = (turn == 0) ? 1 : 0;
        }
        panel.repaint();
    }
	
    @Override
    public void Score() {
        if (p1.getScore() > p2.getScore()) {
            new WinnerDialog(p1.getName(), p1.getScore(), p2.getScore()).show();
        } else if (p1.getScore() < p2.getScore()) {
            new WinnerDialog(p2.getName(), p1.getScore(), p2.getScore()).show();
        } else {
            new WinnerDialog("No One", p1.getScore(), p2.getScore()).show();
        }
        System.out.println ("Player 1 Score: " + p1.getScore ());
        System.out.println ("Player 2 Score: " + p2.getScore ());
    }
	
    @Override
    public void newGame() {
        LocalPlayersNameWindow gameSetup = new LocalPlayersNameWindow();
        gameSetup.show();
        p1 = new Player(gameSetup.getPlayerOne());
        p2 = new Player(gameSetup.getPlayerTwo());
        panel.board.clear();
        panel.board = new Board(
               Toolkit.getDefaultToolkit().getImage("src\\board.png"), 
               Toolkit.getDefaultToolkit().getImage("src\\white.png"), 
               Toolkit.getDefaultToolkit().getImage("src\\black.png"),
               p1, p2);
        turn = 0;
    }

    @Override
    public void restartGame() {
        turn = 0;
        panel.board.clear();
    }

    @Override
    public void loadGame() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("Savestate.txt");
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
            panel.board = (Board) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException");
        }
        turn = panel.board.getTurn();
        turn = (turn == 0) ? 1 : 0;
    }

    @Override
    public void saveGame() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("Savestate.txt");
        } catch (FileNotFoundException ex) {
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(panel.board);
            fos.close();
        } catch (IOException ex) {
        }
    }

    @Override
    public void exitGame() {
        hide();
        exit(0);
    }

    @Override
    public void Undo() {
        panel.board.Undo();
        turn = (turn == 0) ? 1 : 0;
    }

    @Override
    public void Redo() {
        panel.board.Redo();
        turn = (turn == 0) ? 1 : 0;
    }
}
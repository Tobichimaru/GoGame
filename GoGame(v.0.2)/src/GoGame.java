import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;

public class GoGame extends JFrame implements IGoGame, MouseListener {
    private MenuBar menubar;
    private MenuItem newGameItem, restartGameItem, loadGameItem, saveGameItem, exitGameItem;
    private MenuItem undoItem, redoItem, passItem, howtoItem, resignItem;
    private Menu m1, m2, m3;
    
    private Image w, b, img;
    private int turn;
    private DrawPanel panel;
    
    private static final long serialVersionUID = -250003671167959230L;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    
    public GoGame(boolean server) {
        img = Toolkit.getDefaultToolkit().getImage("src\\board.png");
        w = Toolkit.getDefaultToolkit().getImage("src\\white.png"); 
        b = Toolkit.getDefaultToolkit().getImage("src\\black.png");
        panel = new DrawPanel(new Board(img, w, b, new Player("Black"),
                new Player("White")));
       
        syncMenu();
        addMouseListener(this);
        turn = 0;
       
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
        resignItem  = new MenuItem("Resign");
        
        m2 = new Menu("Edit");
        m2.add(undoItem);
        m2.add(redoItem);
        m2.add(passItem);
        m2.add(resignItem);
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
        if (e.target == passItem) {
            if (turn == 0) {
                panel.board.p1.setPass(true);
            } else {
                panel.board.p2.setPass(true);
            }
            if (panel.board.p1.getPass() && panel.board.p2.getPass()) {
                Score();	
            }
            turn = (turn == 0) ? 1 : 0;
        } else if (e.target == saveGameItem) {
            saveGame();
        } else {
            panel.board.p1.setPass(false);
            panel.board.p2.setPass(false);
            
            if (e.target == newGameItem) {
                newGame();
            } else if (e.target == restartGameItem) {
                restartGame();
            } else if (e.target == loadGameItem) {
                loadGame();
                panel.board.setImages(img, w, b);
            }  else if (e.target == exitGameItem) {
                exitGame();
            } else if (e.target == undoItem) {
                Undo();
            } else if (e.target == redoItem) {
                Redo();
            } else if (e.target == howtoItem) {
                HelpWindow helpWindow = new HelpWindow();
                helpWindow.show();
            } else if (e.target == resignItem) {
                new WinnerDialog(panel.board.p2.getName()).show();
            }
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
        panel.board.calculateScore();
        double p1_score = panel.board.p1.getScore();
        double p2_score = panel.board.p2.getScore();
        if (p1_score < 0) {
            p2_score -= p1_score;
            p1_score = 0;
        } else if (p2_score < 0) {
            p1_score -= p2_score;
            p2_score = 0;
        }
        if (p1_score > p2_score) {
            new WinnerDialog(panel.board.p1.getName(), p1_score, p2_score).show();
        } else {
            new WinnerDialog(panel.board.p2.getName(), p1_score, p2_score).show();
        }
    }
    
    @Override
    public void newGame() {
        panel.board.clear();
        panel.board = new Board(
               Toolkit.getDefaultToolkit().getImage("src\\board.png"), 
               Toolkit.getDefaultToolkit().getImage("src\\white.png"), 
               Toolkit.getDefaultToolkit().getImage("src\\black.png"),
               new Player("Black"), new Player("White"));
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
        dispose();
        new MainMenu();
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

    void setSocket(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }
    
    void setPlayers(String name1, String name2) {
        panel.board.p1.setName(name1);
        panel.board.p2.setName(name2);
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;

public class GoGame extends JFrame implements IGoGame {
    private GameFrame frame;
    private Player p1, p2;
    int turn;
    
    private static final long serialVersionUID = -250003671167959230L;
    
    public GoGame() {
        turn = 0;
        LocalPlayersNameWindow gameSetup = new LocalPlayersNameWindow();
        gameSetup.show();
        p1 = new Player(gameSetup.getPlayerOne());
        p2 = new Player(gameSetup.getPlayerTwo());
        frame = new GameFrame(this, width, height, p1, p2);
        frame.add(frame.board);
        frame.addMouseListener(this);
        frame.setVisible(true);
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
        int x = ((int)(e.getX() - xmargin)/cellsize) * cellsize + xmargin;
        int y = ((int)(e.getY() - ymargin)/cellsize) * cellsize + ymargin;

        if (e.isShiftDown()) {
            frame.board.Undo();
        } else if (e.isControlDown()) {
            if (turn == 0) {
                p1.setPass(true);
            } else {
                p2.setPass(true);
            }
            if (p1.getPass() == true && p2.getPass () == true)
                Score();	
        } else {
            frame.board.Play(x, y, turn);
        }
        turn = (turn == 0) ? 1 : 0;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.board.repaint();
            }
        });
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
        frame.board.clear();
        frame.board = new Board(
               Toolkit.getDefaultToolkit().getImage("src\\board.png"), 
               Toolkit.getDefaultToolkit().getImage("src\\white.png"), 
               Toolkit.getDefaultToolkit().getImage("src\\black.png"),
               p1, p2);
          frame.board.repaint();
    }

    @Override
    public void restartGame() {
        frame.board.clear();
        frame.board.repaint();
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
            frame.board = (Board) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException");
        }
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
            oos.writeObject(frame.board);
            fos.close();
        } catch (IOException ex) {
        }
    }

    @Override
    public void exitGame() {
        exit(0);
    }

    @Override
    public void Undo() {
        frame.board.Undo();
        turn = (turn == 0) ? 1 : 0;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 frame.board.repaint();
            }
        });
    }

    @Override
    public void Redo() {
        frame.board.Redo();
        turn = (turn == 0) ? 1 : 0;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 frame.board.repaint();
            }
        });
    }
}
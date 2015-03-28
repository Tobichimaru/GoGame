import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import static java.lang.System.exit;

public class GoGame extends JFrame implements IGoGame {
    private GameFrame frame;
    private Board board;
    private Player p1, p2;
    int turn;
    
    public GoGame() {
        turn = 0;
        LocalPlayersNameWindow gameSetup = new LocalPlayersNameWindow();
        gameSetup.show();
        p1 = new Player(gameSetup.getPlayerOne());
        p2 = new Player(gameSetup.getPlayerTwo());

        board = new Board(
                Toolkit.getDefaultToolkit().getImage("src\\board.png"), 
                Toolkit.getDefaultToolkit().getImage("src\\white.png"), 
                Toolkit.getDefaultToolkit().getImage("src\\black.png"),
                p1, p2);
        
        frame = new GameFrame(this, width, height, board);
        frame.add(board);
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
            board.Undo();
        } else if (e.isControlDown()) {
            if (turn == 0) {
                p1.setPass(true);
            } else {
                p2.setPass(true);
            }
            if (p1.getPass() == true && p2.getPass () == true)
                Score();	
        } else {
            board.Play(x, y, turn);
        }
        turn = (turn == 0) ? 1 : 0;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                board.repaint();
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
    }

    @Override
    public void restartGame() {
    }

    @Override
    public void loadGame() {
    }

    @Override
    public void saveGame() {
    }

    @Override
    public void exitGame() {
        exit(0);
    }

    @Override
    public void Undo() {
        board.Undo();
        turn = (turn == 0) ? 1 : 0;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 board.repaint();
            }
        });
    }

    @Override
    public void Redo() {
        board.Redo();
        turn = (turn == 0) ? 1 : 0;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 board.repaint();
            }
        });
    }
}
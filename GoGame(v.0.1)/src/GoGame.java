import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.Socket;
import static java.lang.System.exit;

public class GoGame extends JFrame implements IGoGame {
    private GameFrame frame;
    private Board board;
    private Player p1, p2, current_player;
    private final int sizepiece = 28, width = 605, height = 645, xmargin = 38, ymargin = 80;
    
    boolean myTurn = false;
    int turn;
    Socket s;
    Piece p;
    
    public GoGame() {
        turn = 1;
        myTurn = true;
        current_player = p1;
        
        Setup gameSetup = new Setup();
        gameSetup.show();
        p1 = new Player (gameSetup.getPlayerOne());
        p2 = new Player (gameSetup.getPlayerTwo());

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
        if (!myTurn) {
            return;
        }
        int x = ((int)(e.getX() - xmargin)/sizepiece) * sizepiece + xmargin;
        int y = ((int)(e.getY() - ymargin)/sizepiece) * sizepiece + ymargin;

        if (e.isShiftDown()) {
            board.Undo();
            changeTurn();
        } else if (e.isControlDown()) {
            current_player.setPass(true);
            if (p1.getPass() == true && p2.getPass () == true)
                Score();	
            changeTurn();
            board.SaveState();
        } else {
            p = new Piece(x, y, turn);
            if (board.isPlayable(p.getX(), p.getY(), p.getColor())) {
                board.Play(p);
                changeTurn();
            }
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                board.repaint();
            }
        });
    }
	
    @Override
    public void Score() {
        board.Score (p1, p2);

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
        changeTurn();
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
        changeTurn();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 board.repaint();
            }
        });
    }
    
    private void changeTurn() {
        if (current_player == p1) {
            turn = 1;
            current_player = p2;
        } else {
            turn = 0;
            current_player = p1;
        }
    }
}
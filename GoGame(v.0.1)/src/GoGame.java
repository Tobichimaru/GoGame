import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.MouseEvent;
import java.net.Socket;
import static java.lang.System.exit;

public class GoGame extends JFrame implements IGoGame {
    private Board board;
    private Image board_img, white_stone, black_stone; 
    private Player p1, p2, current_player;
    private final int sizepiece = 28;
    private GameFrame frame;
    boolean usingP2P = false;
    boolean goFirst = true;
    boolean myTurn = false;
    int turn;
    PrintStream os;
    Socket s;
    Piece p;
    
    public GoGame() {
        turn = 0;
        new P2PServerManager(this).start();
        System.out.println("GoGame()");
        GametypeDialog gametype = new GametypeDialog();
        gametype.show();
		
        if (gametype.getGameType() == 0) {
            System.out.println("Local game");
            Setup gameSetup = new Setup();
            gameSetup.show();
            p1 = new Player (gameSetup.getPlayerOne());
            p2 = new Player (gameSetup.getPlayerTwo());
            myTurn = true;
        } else {
            System.out.println("P2P game");
            P2PSetup gameSetup = new P2PSetup();
            gameSetup.show();
            p1 = new Player (gameSetup.getPlayer());
            p2 = new Player ("Remote");	
            usingP2P = true;
			
            try {
                System.out.println("Starting client");
                s = new Socket(gameSetup.getHost(),0);
                os = new PrintStream(new BufferedOutputStream(s.getOutputStream(), 1024), false);
                System.out.println("Done starting client");
                if (goFirst) {
                    myTurn = true;
                }
            } catch(Exception e) {
                System.out.print("Could not connect!" + e);		
            }
        }
        current_player = p1;

        setBackground(Color.black);
        board_img = Toolkit.getDefaultToolkit().getImage("src\\board.png");
        white_stone = Toolkit.getDefaultToolkit().getImage("src\\white.png");
        black_stone = Toolkit.getDefaultToolkit().getImage("src\\black.png");
        
        board = new Board(board_img, white_stone, black_stone, p1, p2);
        
        frame = new GameFrame(this, 605, 645, board);
        frame.add(board);
        frame.addMouseListener(this);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
 
    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (myTurn) {
            Move m, mo;
            x -= 38;
            y -= 80;
            int xTest = (x/sizepiece);
            int yTest = (y/sizepiece);
            x = 38 + xTest*sizepiece;
            y = 80 + yTest*sizepiece;
            
//            if (usingP2P) {
//                os.println("m:"+x+":"+y);
//                os.flush();
//                myTurn = false;
//            }

            if (e.isShiftDown()) {
                board.Undo();
                changeTurn();
            } else if (e.isControlDown()) {
                current_player.setPass (true);
                if (p1.getPass() == true && p2.getPass () == true)
                    Score();	
                changeTurn();
                board.SaveState ();
            } else {
                p = new Piece(x, y, turn);
                m = new Move (x, y);
                if (board.Play(p)) {
                    changeTurn();
                    if (current_player == p1) {
                        p1.addMove();
                    } else {
                        p2.addMove();
                    }
                }
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        board.repaint();
                    }
                });
        }
    }
	
    public boolean mouseDownServer (int x, int y) {
        myTurn = true;
        Move m, mo;

        // normalize mouse click position on board
        int xTest = (x/19) ;
        int yTest = (y/19) ;

        double xx = xTest + .5;
        double yy = yTest + .5;

        double xTest2 = ((double)x/19);
        if (xTest2 > xx) {
            x = (x/19)*19 + 19;
        } else {
            x = (x/19)*19;
        }
        
        double yTest2 = ((double)y/19);
        if (yTest2 > yy) {
            y =  (y/19)*19 + 19;
        } else {
            y = (y/19)*19;
        }

        System.out.println("Mouse After: " + x + " " + y);

        final Piece p = new Piece (x-(sizepiece)/2, y-(sizepiece)/2, turn);
        m = new Move (x-(sizepiece)/2, y-(sizepiece)/2);

        if (board.Play (p)) {
            if (current_player == p1) {
                p1.addMove();
                turn = 1;
                current_player = p2;
            } else {
                p2.addMove();
                turn = 0;
                current_player = p1;
            }
        }
       
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                p.setVisible(true);
            }
        });
        return true;
    }	
	
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
	
    public void newGame() {
    }

    public void restartGame() {
    }

    public void loadGame() {
    }

    public void saveGame() {
    }

    public void exitGame() {
        exit(0);
    }

    public void Undo() {
        board.Undo();
        changeTurn();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 board.repaint ();
            }
        });
    }

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
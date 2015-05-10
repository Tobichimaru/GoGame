import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Saia
 */
public class BoardTest {
    
    public BoardTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of Play method, of class Board.
     */
    @Test
    public void testPlay() {
        System.out.println("Play");
        GoGame game = new GoGame(false);
        boolean result = game.panel.board.Play(100, 100, 0, false);
        game.exitGame();
        assertTrue(result);
    }

    /**
     * Test of Undo method, of class Board.
     */
    @Test
    public void testUndo() {
        System.out.println("Undo");
        GoGame game = new GoGame(false);
        game.panel.board.Play(100, 100, 0, false);
        game.panel.board.Undo();
        Stone s = game.panel.board.findStoneByPos(100, 100);
        game.exitGame();
        assertTrue(s.getX() == 0);
    }

    /**
     * Test of Redo method, of class Board.
     */
    @Test
    public void testRedo() {
        System.out.println("Redo");
        GoGame game = new GoGame(false);
        game.panel.board.Play(100, 100, 0, false);
        game.panel.board.Undo();
        game.panel.board.Redo();
        Stone s = game.panel.board.findStoneByPos(100, 100);
        game.exitGame();
        assertTrue(s.getX() == 100);
    }

    /**
     * Test of removeStone method, of class Board.
     */
    @Test
    public void testRemoveStone() {
        System.out.println("removeStone");
        GoGame game = new GoGame(false);
        game.panel.board.Play(100, 100, 0, false);
        Stone st = game.panel.board.findStoneByPos(100, 100);
        boolean result = game.panel.board.removeStone(st);
        game.exitGame();
        assertTrue(result);
    }

    /**
     * Test of clear method, of class Board.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        GoGame game = new GoGame(false);
        game.panel.board.Play(100, 100, 0, false);
        game.panel.board.clear();
        Stone st = game.panel.board.findStoneByPos(100, 100);
        game.exitGame();
        assertTrue(st.getX() == 0);
    }

    /**
     * Test of getTurn method, of class Board.
     */
    @Test
    public void testGetTurn() {
        System.out.println("getTurn");
        GoGame game = new GoGame(false);
        int result = game.panel.board.getTurn();
        game.exitGame();
        assertEquals(0, result);
    }

    /**
     * Test of calculateScore method, of class Board.
     */
    @Test
    public void testCalculateScore() {
        System.out.println("calculateScore");
        GoGame game = new GoGame(false);
        game.panel.board.calculateScore();
        double result = game.panel.board.p2.getScore();
        game.exitGame();
        assertEquals(5.5, result, 0);
    }
    
}

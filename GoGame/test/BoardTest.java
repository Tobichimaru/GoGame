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
        int x = 0;
        int y = 0;
        int color = 0;
        boolean rewrite = false;
        Board instance = new Board();
        boolean expResult = false;
        boolean result = instance.Play(x, y, color, rewrite);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Undo method, of class Board.
     */
    @Test
    public void testUndo() {
        System.out.println("Undo");
        Board instance = new Board();
        instance.Undo();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Redo method, of class Board.
     */
    @Test
    public void testRedo() {
        System.out.println("Redo");
        Board instance = new Board();
        instance.Redo();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeStone method, of class Board.
     */
    @Test
    public void testRemoveStone() {
        System.out.println("removeStone");
        Stone s = null;
        Board instance = new Board();
        boolean expResult = false;
        boolean result = instance.removeStone(s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class Board.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Board instance = new Board();
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTurn method, of class Board.
     */
    @Test
    public void testGetTurn() {
        System.out.println("getTurn");
        Board instance = new Board();
        int expResult = 0;
        int result = instance.getTurn();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculateScore method, of class Board.
     */
    @Test
    public void testCalculateScore() {
        System.out.println("calculateScore");
        Board instance = new Board();
        instance.calculateScore();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

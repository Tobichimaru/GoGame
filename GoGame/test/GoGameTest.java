
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
public class GoGameTest {
    
    public GoGameTest() {
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
     * Test of loadGame method and saveGame method, of class GoGame.
     */
    @Test
    public void testSaveLoadGame() {
        System.out.println("loadGame");
        GoGame instance = new GoGame(false);
        instance.panel.board.Play(150, 150, 0, false);
        instance.saveGame();
        instance.exitGame();
        instance.loadGame();
        Stone st = instance.panel.board.findStoneByPos(150, 150);
        assertTrue(st.getX() == 150);
    }
}

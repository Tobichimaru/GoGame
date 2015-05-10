
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
public class StoneTest {
    
    public StoneTest() {
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
     * Test of fromString method, of class Stone.
     */
    @Test
    public void testFromString() {
        System.out.println("fromString");
        Stone instance = new Stone(10, 10, 1, 1, 0);
        String s = instance.toString();
        instance = new Stone();
        instance.fromString(s);
        assertEquals(instance.getX(), 10);
    }
    
}

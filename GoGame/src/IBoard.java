/**
 *
 * @author Saia
 */
public interface IBoard {
    public final int cellsize = 28;
    
    void Undo();
    void Redo();
    boolean removeStone(Stone s);
    
}

import java.awt.Image;

public interface IBoard {
    void SaveState();
    void Undo();
    void Redo();
    
    void Play(Piece p);
    
    boolean removePiece(int x, int y);
    
    boolean isPlayable(int x, int y, int color);
    
    Piece getPieceAt(int x, int y);
    
    void CheckLibs(Piece p);
    boolean DeleteLibsOf(Piece p, Piece caller);
    
    void Score(Player p1, Player p2);
    
}

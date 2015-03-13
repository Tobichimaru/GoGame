import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;

public interface IBoard {
    void setImage(Image img);
    void SaveState();
    void Undo();
    void Redo();
    void Score(Player p1, Player p2);
    void CheckLibs(Piece p);
    boolean removePiece(int x, int y);
    boolean isPlayable(int x, int y, int color);
    boolean CheckSub(Piece p, Piece caller);
    boolean DeleteLibsOf(Piece p, Piece caller);
    boolean Play(Piece p);
    Piece getPieceAt(int x, int y);
}

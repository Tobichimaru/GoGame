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
    void addPiece(Piece p);
    void setCheckedZero();
    boolean removePiece(int x, int y);
    boolean isPieceAt(int x, int y);
    boolean isSurrounded(int x, int y, int color);
    boolean isPlayable(int x, int y, int color);
    boolean CheckSub(Piece p, Piece caller);
    boolean DeleteLibsOf(Piece p, Piece caller);
    boolean Play(Piece p);
    int Score_Sub(int x, int y, int color);
    int Capture_Score (Player p, int color);
    Piece getPieceAt(int x, int y);
}

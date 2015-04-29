
public interface IGoGame {
    public final int cellsize = 28,
        xmargin = 38, ymargin = 46,
        ymin = 55, ymax = 600,
        xmin = 35, xmax = 560;
    
    void Score();
    void Undo();
    void Redo();
    
    void newGame();
    void restartGame();
    void loadGame();
    void saveGame();
    void exitGame();
}

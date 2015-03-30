public interface IBoard {
    public final int cellsize = 28, 
        ymin = 80, ymax = 615,
        xmin = 35, xmax = 560,
        width = 605, height = 645,
        xmargin = 38, ymargin = 80;
    
    void Undo();
    void Redo();
    boolean removeStone(int x, int y);
    
}

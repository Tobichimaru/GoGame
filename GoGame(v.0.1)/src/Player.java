public class Player {
    private String name;
    private int score;
    private int moves;
    boolean pass;

    Player(String n) {
        moves = 0;
        name = n;
    }

    public void setName(String n) {
        name = n;
    }

    public void increaseScore(int n) {
        score += n;
    }

    public void addMove() {
        moves++;
    }

    public int removeMove() {
        return moves--;		
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int m) {
        moves = m;
    }

    public String getName() {
        return name;
    }

    public void setScore(int sc) {
        score = sc;
    }

    public int getScore() {
        return score;
    }

    public void setPass(boolean set) {
        pass = set;
    }

    public boolean getPass() {
        return pass;
    }
}

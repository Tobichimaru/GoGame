public class Player {
    private String name;
    private int score;
    boolean pass;

    Player(String n) {
        name = n;
    }

    public void setName(String n) {
        name = n;
    }

    public void increaseScore(int n) {
        score += n;
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

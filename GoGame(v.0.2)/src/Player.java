
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int score;
    boolean pass;
    
    private static final long serialVersionUID = -2518143646456679590L;

    Player(String n) {
        name = n;
    }

    public void setName(String n) {
        name = n;
    }
    
    public String getName() {
        return name;
    }
    
    public void increaseScore(int n) {
        score += n;
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
    
    private void writeObject(ObjectOutputStream o) throws IOException {  
        o.writeObject(name);
        o.writeObject(score);
        o.writeObject(pass);
    }
  
    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {  
        name = (String) o.readObject();
        score = (int) o.readObject();
        pass = (boolean) o.readObject();
    }
    
}

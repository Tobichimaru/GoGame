
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Saia
 */
public class Player implements Serializable {
    private String name;
    private double score;
    private boolean pass;
    
    private static final long serialVersionUID = -2518143646456679590L;

    Player(String n) {
        name = n;
        score = 0;
        pass = false;
    }

    Player() {
        name = "";
        score = 0;
        pass = false;
    }

    public void setName(String n) {
        name = n;
    }
    
    public String getName() {
        return name;
    }
    
    public void increaseScore(double n) {
        score += n;
    }

    public void setScore(double sc) {
        score = sc;
    }

    public double getScore() {
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
        score = (double) o.readObject();
        pass = (boolean) o.readObject();
    }
    
}

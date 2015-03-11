import java.net.*;
import java.io.*;

public class ClientManager implements Runnable {
    Thread kicker = null;

    ClientManager() {
        kicker = new Thread(this);
        kicker.start();
    }

    public void start() {
        if (kicker == null)
            kicker = new Thread(this);
    }

    public void run() {
    }

    public void stop() {
            kicker = null;
    }

    String getData() {
        Socket s;
        DataInputStream ds;
        PrintStream ps;
        String temp = null;

        try {
            s = new Socket("diginux.net", 2357);
            ds = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
        } catch (Exception e) {
            return null;
        }

        return temp;
    }
}

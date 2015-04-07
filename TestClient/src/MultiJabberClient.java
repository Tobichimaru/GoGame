
import java.io.IOException;
import java.net.InetAddress;

public class MultiJabberClient {
   static final int MAX_THREADS = 40;
   static final int PORT = 9991;

   
   public static void main(String[] args) throws IOException,
         InterruptedException {
      InetAddress addr = InetAddress.getByName(null);
      while (true) {
         if (JabberClientThread.threadCount() < MAX_THREADS)
            new JabberClientThread(addr);
         Thread.currentThread().sleep(100);
      }
   }
} // /:~
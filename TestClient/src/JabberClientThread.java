//: c15:MultiJabberClient.java
// Клиент, который проверяет MultiJabberServer,
// запуская несколько клиентов.
// {RunByHand}
import java.net.*;

import java.io.*;

class JabberClientThread extends Thread {
   private Socket socket;
   private BufferedReader in;
   private PrintWriter out;
   private static int counter = 0;
   private int id = counter++;
   private static int threadcount = 0;
   
   public static int threadCount() {
      return threadcount;
   }
   
   public JabberClientThread(InetAddress addr) {
      System.out.println("Making client " + id);
      threadcount++;
      try {
         socket = new Socket(addr, MultiJabberClient.PORT);
      } catch (IOException e) {
         System.err.println("Socket failed");
      }
      
      try {
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
         start();
      }  catch (IOException e) {
         try {
            socket.close();
         } catch (IOException e2) {
            System.err.println("Socket not closed");
         }
      }
    }
   
   public void run() {
      try {
         for (int i = 0; i < 25; i++) {
            out.println("Client " + id + ": " + i);
            String str = in.readLine();
            System.out.println(str);
         }
         out.println("END");
      }
      catch (IOException e) {
         System.err.println("IO Exception");
      }
      finally {
         // Всегда закрывает:
         try {
            socket.close();
         }
         catch (IOException e) {
            System.err.println("Socket not closed");
         }
         threadcount--; // Завершаем эту нить
      }
   }
}


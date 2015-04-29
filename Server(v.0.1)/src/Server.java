import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Server {

    private List<Connection> connections = 
        Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;
    private int id = 0;

    /**
    * Конструктор создаёт сервер. Затем для каждого подключения создаётся
    * объект Connection и добавляет его в список подключений.
    */
    public Server() {
        try {
            server = new ServerSocket(9991);
            while (true) {
                Socket socket = server.accept();
                id++;
                Connection con = new Connection(socket, id);
                connections.add(con);
                con.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    /**
     * Закрывает все потоки всех соединений а также серверный сокет
     */
    private void closeAll() {
        try {
            server.close();

            synchronized(connections) {
                Iterator<Connection> iter = connections.iterator();
                while(iter.hasNext()) {
                    ((Connection) iter.next()).close();
                }
            }
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    /**
     * Класс содержит данные, относящиеся к конкретному подключению:
     * <ul>
     * <li>имя пользователя</li>
     * <li>сокет</li>
     * <li>входной поток BufferedReader</li>
     * <li>выходной поток PrintWriter</li>
     * </ul>
     * Расширяет Thread и в методе run() получает информацию от пользователя и
     * пересылает её другим
     * 
     * @author Влад
     */
    private class Connection extends Thread {
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        public String name = "";
        private int id;

        /**
         * Инициализирует поля объекта и получает имя пользователя
         * 
         * @param socket
         *            сокет, полученный из server.accept()
         */
        public Connection(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
            try {
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

        /**
         * Запрашивает имя пользователя и ожидает от него сообщений. При
         * получении каждого сообщения, оно вместе с именем пользователя
         * пересылается всем остальным.
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            try {
                out.println(Integer.toString(id));
                name = in.readLine();

                String str = new String();
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) {
                        break;
                    }

                    if (str.equals("list")) {
                        Iterator<Connection> iter = connections.iterator();
                        out.println(Integer.toString(connections.size()));
                        while(iter.hasNext()) {
                            out.println(iter.next().name);
                        }
                    }  else {
                        synchronized(connections) {
                            Iterator<Connection> iter = connections.iterator();
                            while(iter.hasNext()) {
                                ((Connection) iter.next()).out.println(name + ": " + str);
                            }
                        }
                    }
                }

                synchronized(connections) {
                    Iterator<Connection> iter = connections.iterator();
                    while(iter.hasNext()) {
                        ((Connection) iter.next()).out.println(name + " has left");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        /**
         * Закрывает входной и выходной потоки и сокет
         */
        public void close() {
            try {
                in.close();
                out.close();
                socket.close();

                connections.remove(this);
//                if (connections.size() == 0) {
//                        Server.this.closeAll();
//                        System.exit(0);
//                }
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
            }
        }
    }
}
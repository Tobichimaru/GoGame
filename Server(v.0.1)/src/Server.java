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
     * @author Saia
     */
    private class Connection extends Thread {
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        public String str_id = "";
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
                str_id = Integer.toString(id);
                out.println(str_id);

                String str = new String();
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) {
                        break;
                    } else if (str.equals("list")) {
                        Iterator<Connection> iter = connections.iterator();
                        out.println(Integer.toString(connections.size()));
                        while(iter.hasNext()) {
                            out.println(iter.next().str_id);
                        }
                    } else if (str.equals("connect")) {
                        String opp_id = in.readLine();
                        Iterator<Connection> iter = connections.iterator();
                        Connection con = null;
                        while (iter.hasNext()) {
                            con = iter.next();
                            if (con.str_id.equals(opp_id)) {
                                break;
                            }
                        }
                        con.out.println("request");
                        con.out.println(str_id);
                        while (true) {
                            out.println(con.in.readLine()); //read from opponent, to client
                            str = in.readLine();
                            con.out.println(str); // read from client, to opponent
                            if (str.equals("break")) {
                                break;
                            }
                        }
                    } else if (str.equals("request")) {
                        String opp_name = in.readLine();
                        Iterator<Connection> iter = connections.iterator();
                        Connection con = null;
                        while (iter.hasNext()) {
                            con = iter.next();
                            if (con.str_id.equals(opp_name)) {
                                break;
                            }
                        }
                        while (true) {
                            str = in.readLine();
                            con.out.println(str); // read from client, to opponent
                            if (str.equals("break")) {
                                break;
                            }
                            out.println(con.in.readLine()); //read from opponent, to client
                        }
                    } else {
                        synchronized(connections) {
                            Iterator<Connection> iter = connections.iterator();
                            while(iter.hasNext()) {
                                ((Connection) iter.next()).out.println(str_id + ": " + str);
                            }
                        }
                    }
                }

                synchronized(connections) {
                    Iterator<Connection> iter = connections.iterator();
                    while(iter.hasNext()) {
                        ((Connection) iter.next()).out.println(str_id + " has left");
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
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
            }
        }
    }
}
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
    private ArrayList<Boolean> playble = new ArrayList<>(); 
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
                playble.add(Boolean.TRUE);
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
            str_id = Integer.toString(id);
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
                System.out.println(str_id);
                out.println(str_id);
                
                while (connections.size() < 2) {}
                Iterator<Connection> iter = connections.iterator(), jtr;
                Connection first = null, second = null;
                while (iter.hasNext()) {
                    first = iter.next();
                    if (playble.get(connections.indexOf(first))) {
                        jtr = iter;
                        boolean f = false;
                        while (jtr.hasNext()) {
                            f = false;
                            second = jtr.next();
                            if (!second.str_id.equals(first.str_id) && 
                                    playble.get(connections.indexOf(second)) ) {
                                playble.set(connections.indexOf(first), false);
                                playble.set(connections.indexOf(second), false);

                                first.out.println(second.str_id);
                                first.out.println("black");

                                second.out.println(first.str_id);
                                second.out.println("white");
                                f = true;
                                break;
                            }
                            if (f) break;
                        }
                    }
                }
                String str = null;
                while (true) {
                    str = first.in.readLine();
                    System.out.println(str);
                    if (str.equals("exit")) {
                        break;
                    }
                    second.out.println(str); // read from client, to opponent
                    str = second.in.readLine();
                    System.out.println(str);
                    first.out.println(str); //read from opponent, to client
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
                int pos = connections.indexOf(this);
                connections.remove(this);
                playble.remove(pos);
            } catch (Exception e) {
                System.err.println("Потоки не были закрыты!");
            }
        }
    }
}
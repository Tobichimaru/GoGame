import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * Обеспечивает работу программы в режиме клиента
 * 
 * @author Saia
 */
public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private String id;

    /**
     * Запрашивает у пользователя ник и организовывает обмен сообщениями с
     * сервером
     */
    public Client() {
        Scanner scan = new Scanner(System.in);
        try {
                // Подключаемся в серверу и получаем потоки(in и out) для передачи сообщений
                socket = new Socket("localhost", 9991);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                Resender resender = new Resender();
                resender.start();

                id = in.readLine();

                out.println("list");

                int n = Integer.valueOf(in.readLine());
                String str = new String();
                LinkedList<String> names = new LinkedList<>();

                for (int i = 0; i < n; i++) {
                    str = in.readLine();
                    if (!str.equals(id)) 
                        names.add(str);
                }
                
                PlayerListWindow window = new PlayerListWindow(n, names);
                String opp_name = window.getChoise();
                out.println("connect");
                out.println(opp_name);
                GoGame game = new GoGame(true);
                game.setSocket(socket, in, out);
                game.setInfo("Wait for opponent's turn!");
                        while (true) {
                        }

        } catch (Exception e) {
                e.printStackTrace();
        } finally {
                close();
        }
    }

    /**
     * Закрывает входной и выходной потоки и сокет
     */
    private void close() {
            try {
                    in.close();
                    out.close();
                    socket.close();
            } catch (Exception e) {
                    System.err.println("Потоки не были закрыты!");
            }
    }

    /**
     * Класс в отдельной нити пересылает все сообщения от сервера в консоль.
     * Работает пока не будет вызван метод setStop().
     * 
     * @author Влад
     */
    private class Resender extends Thread {

            private boolean stoped;

            /**
             * Прекращает пересылку сообщений
             */
            public void setStop() {
                    stoped = true;
            }

            /**
             * Считывает все сообщения от сервера и печатает их в консоль.
             * Останавливается вызовом метода setStop()
             * 
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                    try {
                            while (!stoped) {
                                    String str = in.readLine();
                                    System.out.println(str);
                            }
                    } catch (IOException e) {
                            System.err.println("Ошибка при получении сообщения.");
                            e.printStackTrace();
                    }
            }
    }

}
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    private GoGame game;

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
                
            System.out.println("Waiting for opponent...");
            
            String color = in.readLine();
            System.out.println(color);
            
            game = new GoGame(true);
            game.setSocket(socket, in, out);
            if (color.equals("black")) {
                game.setInfo("Your turn!");
                game.setPlayerColor(0);
            } else {
                game.setInfo("Wait for opponent's turn!");
                game.setPlayerColor(1);
            }
            
            Resender resender = new Resender();
            resender.run();
            
            while (!game.isOver()) {
            }
            resender.setStop();
            out.println("exit");
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
     * Класс в отдельной нити пересылает все сообщения от сервера.
     * Работает пока не будет вызван метод setStop().
     */
    private class Resender extends Thread {
        private boolean stoped;

        public void setStop() {
                stoped = true;
        }

        /**
        * Считывает все сообщения от сервера.
        * Останавливается вызовом метода setStop()
        * 
        * @see java.lang.Thread#run()
        */
        @Override
        public void run() {
            try {
                String str;
                while (!stoped) {
                    str = in.readLine();
                    if (str == null) { continue;}
                    System.out.println(str);
                    game.receiveMessage(str);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при получении сообщения.");
                e.printStackTrace();
            }
        }
    }

}

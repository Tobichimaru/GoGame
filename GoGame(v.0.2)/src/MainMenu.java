
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Saia
 */
class MainMenu extends JFrame {
    private JPanel panel;

    public MainMenu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setSize(400, 400); 
        setVisible(true); 
 
        panel = new JPanel();
        panel.setLayout(new GridLayout(3,3)); 
 
        // к панели добавляем кнопку и устанавливаем для нее менеджер в верхнее расположение. 
        JButton local_game = new JButton("Local game");
        local_game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
                dispose();
                new GoGame();
            }
        });
        panel.add(local_game); 
        
        JButton server_game = new JButton("Server game");
        server_game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
                dispose();
                new Client();
            }
        });
        panel.add(server_game); 

        JButton howto = new JButton("How to play?");
        howto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HelpWindow();
            }
        });
        panel.add(howto); 
        
        add(panel);
    }
    
}

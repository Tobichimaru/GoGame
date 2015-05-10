
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Saia
 */
class MainMenu extends JFrame {
    private final JPanel panel;

    public MainMenu() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(2,2)); 
 
        JButton local_game = new JButton("Local game");
        local_game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
                dispose();
                new GoGame(false);
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

        add(panel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setSize(200, 200); 
        setResizable(false);
        setVisible(true); 
        setLocationRelativeTo(null);
    }
    
}

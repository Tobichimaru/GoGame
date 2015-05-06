
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * @author Saia
 */
class PlayerListWindow extends JDialog {
    private JPanel panel;

    public PlayerListWindow(int n, LinkedList<String> names) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout()); 
        
 
        JButton button;
        for (String s : names) { 
            button = new JButton(s);
            System.out.println(s);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hide();
                    dispose();
                    new GoGame(true);
                }
            });
            panel.add(button); 
        }
        
        add(panel);
        setSize(400, 400);
        setLocationRelativeTo(null);
    }
    
}

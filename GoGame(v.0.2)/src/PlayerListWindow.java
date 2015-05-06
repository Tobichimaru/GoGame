
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
    private LinkedList<JButton> buttons;
    private String chosen;

    public PlayerListWindow(int n, LinkedList<String> names) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout()); 
        
        buttons = new LinkedList<>();
        int i = 0;
        JButton button;
        for (String s : names) { 
            button = new JButton(s);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chosen = ((JButton) e.getSource()).getText();
                    hide();
                    dispose();
                }
            });
            panel.add(button); 
            buttons.add(button);
        }
        
        add(panel);
        setSize(400, 400);
        setLocationRelativeTo(null);
    }
    
    public String getChoise() {
        return chosen;
    }
    
}

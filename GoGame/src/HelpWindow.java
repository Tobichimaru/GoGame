
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * @author Saia
 */
public class HelpWindow extends JFrame {
    JButton next, prev;
    JPanel a,b;
    int page;
    JEditorPane editorPane;
    JScrollPane editorScrollPane;

    public HelpWindow() throws IOException {
        page = 1;
        
        setTitle("How to play?");
        setSize(620, 675);
        setResizable(false);
        
        setLayout(new BorderLayout());
        
        a = new JPanel(new BorderLayout());
        b = new JPanel(new FlowLayout());
        add(a, BorderLayout.CENTER);
        add(b, BorderLayout.SOUTH); 
 
        editorPane = new JEditorPane();
        File localFile = new File("src\\Help\\1.html");
        editorPane.setPage("file:///" + localFile.getAbsolutePath());
        
        a.add(editorPane);
        
        prev = new JButton();
        next = new JButton();
        prev.setText("Previous");
        next.setText("Next");
        
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page++;
                try {
                    File localFile = new File("src\\Help\\" + Integer.toString(page) + ".html");
                    editorPane.setPage("file:///" + localFile.getAbsolutePath());
                } catch (IOException ex) {
                }
                prev.show();
                next.show();
                if (page == 1) prev.hide();
                if (page == 7) next.hide();
            }
        });
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                page--;
                try {
                    File localFile = new File("src\\Help\\" + Integer.toString(page) + ".html");
                    editorPane.setPage("file:///" + localFile.getAbsolutePath());
                } catch (IOException ex) {
                }
                prev.show();
                next.show();
                if (page == 1) prev.hide();
                if (page == 7) next.hide();
            }
        });
        
        b.add(prev);
        b.add(next);
        
        prev.show();
        next.show();
        if (page == 1) prev.hide();
        if (page == 7) next.hide();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }   
    
}

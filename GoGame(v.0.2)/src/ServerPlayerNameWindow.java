import java.awt.*;

public class ServerPlayerNameWindow extends Dialog {
    Button b = new Button("Start Game");
    TextField name = new TextField("Player");

    public ServerPlayerNameWindow() {
        super(new Frame("Setup Server"), "Setup Server", true);

        setLayout(new FlowLayout());
        add(name);
        add(b);
        pack();
        setLocationRelativeTo(null);
    }

    public boolean action(Event e, Object o) {
        if (e.target instanceof Button) {
            hide();
            dispose();
            return true;
        }
        return false;
    }
	
    public String getName() {
        return name.getText();
    }
    
    public void setName(String id) {
        name.setText("Player" + id);
    }
}

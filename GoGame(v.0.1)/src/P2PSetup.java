import java.awt.*;

public class P2PSetup extends Dialog {
	
    Button b = new Button("Start Game");
    TextField textHost = new TextField("Host");
    TextField textPlayer = new TextField("Player Name");

    public P2PSetup() {
        super(new Frame("Setup P2P"), "Setup P2P", true);

        setLayout(new FlowLayout());
        add(textHost);
        add(textPlayer);
        add(b);
        pack();
    }

    public boolean action(Event e, Object o) {
        if (e.target instanceof Button) {
            hide();
            dispose();
            return true;
        }
        return false;
    }

    public String getHost() {
        return textHost.getText();
    }

    public String getPlayer() {
        return textPlayer.getText();
    }
}

import java.awt.*;

public class GametypeDialog extends Dialog {
    CheckboxGroup choice = new CheckboxGroup();
    Checkbox local = new Checkbox("Local ", choice, true);
    Checkbox p2p = new Checkbox("P2P", choice, false);
    Button b = new Button("OK");

    public GametypeDialog() {
        super(new Frame("Game Type"), "Game Type", true);

        setLayout(new FlowLayout());
        add(local);
        add(p2p);
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

    int getGameType() {
        if (local.getState()) 
                return 0;
        return 1;
    }
}
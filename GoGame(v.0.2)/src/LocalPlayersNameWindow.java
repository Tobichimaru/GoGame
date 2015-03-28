import java.awt.*;

public class LocalPlayersNameWindow extends Dialog {
    Button b = new Button("Start Game");
    TextField textPlayerOne = new TextField("Player One");
    TextField textPlayerTwo = new TextField("Player Two");

    public LocalPlayersNameWindow() {
        super(new Frame("Setup Local"), "Setup Local", true);

        setLayout(new FlowLayout());
        add(textPlayerOne);
        add(textPlayerTwo);
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
	
    public String getPlayerOne() {
        return textPlayerOne.getText();
    }
	
    public String getPlayerTwo() {
        return textPlayerTwo.getText();
    }
}

import java.awt.*;

public class WinnerDialog extends Dialog {
    Label textWinner, textScore;
    Button b = new Button("OK");

    public WinnerDialog(String playerName, double score1, double score2) {
        super(new Frame("Winner!"), "Winner!", true);

        textWinner = new Label(playerName + " won!");
        textScore = new Label(score1 + " - "  + score2);

        setLayout(new FlowLayout());
        add(textWinner);
        add(textScore);
        add(b);
        pack();
        setLocationRelativeTo(null);
    }   
    
    public WinnerDialog(String playerName) {
        super(new Frame("Winner!"), "Winner!", true);

        textWinner = new Label(playerName + " won by resign!");

        setLayout(new FlowLayout());
        add(textWinner);
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
}

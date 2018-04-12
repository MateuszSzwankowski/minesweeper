import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TopPanel extends JPanel {

    private JLabel flagsLbl;
    private JLabel timerLbl;
    private int numMines;

    TopPanel(Minesweeper game) {
        this.numMines = game.getConfig().getNumMines();
        this.setLayout(new GridLayout(1, 3));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.timerLbl = new JLabel();
        this.timerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.timerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        this.setTimerLabel(0);
        this.add(this.timerLbl);

        JButton restartBtn = new JButton("restart");
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.addActionListener(e -> game.restart());
        this.add(restartBtn);

        this.flagsLbl = new JLabel();
        this.flagsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.flagsLbl.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFlagsLabel(0);
        this.add(this.flagsLbl);
    }

    void reset(int numMines) {
        this.numMines = numMines;
        this.setFlagsLabel(0);
        this.setTimerLabel(0);
    }

    void setFlagsLabel(int numFlags) {
        this.flagsLbl.setText(
                "<html> <p align=center>flags<br>"
                        + String.valueOf(numFlags) + "/"
                        + String.valueOf(this.numMines)
                        + "</p></html>"
        );
    }

    void setTimerLabel(int timeInSeconds) {
        int minutes = Math.floorDiv(timeInSeconds, 60);
        int seconds = timeInSeconds % 60;
        this.timerLbl.setText(
                "<html> <p align=center>time<br>"
                        + String.format("%02d", minutes)
                        + ":"
                        + String.format("%02d", seconds)
                        + "</p></html>"
        );
    }

}

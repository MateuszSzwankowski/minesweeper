package main.java.app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


class TopPanel {

    private JPanel panel;
    private JLabel flagsLbl;
    private JLabel timerLbl;
    private JButton restartBtn;
    private int numMines;

    TopPanel(int numMines) {
        this.numMines = numMines;

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        timerLbl = new JLabel();
        timerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        setTimerLabel(0);
        panel.add(timerLbl);

        restartBtn = new JButton("restart");
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(restartBtn);

        flagsLbl = new JLabel();
        flagsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        flagsLbl.setHorizontalAlignment(SwingConstants.CENTER);
        setFlagsLabel(0);
        panel.add(flagsLbl);
    }

    void reset(int numMines) {
        this.numMines = numMines;
        setFlagsLabel(0);
        setTimerLabel(0);
    }

    void setFlagsLabel(int numFlags) {
        flagsLbl.setText(
                "<html> <p align=center>flags<br>"
                        + String.valueOf(numFlags)
                        + "/"
                        + String.valueOf(numMines)
                        + "</p></html>"
        );
    }

    void setTimerLabel(int timeInSeconds) {
        int minutes = Math.floorDiv(timeInSeconds, 60);
        int seconds = timeInSeconds % 60;
        timerLbl.setText(
                "<html> <p align=center>time<br>"
                        + String.format("%02d", minutes)
                        + ":"
                        + String.format("%02d", seconds)
                        + "</p></html>"
        );
    }

    JPanel getPanel() {
        return panel;
    }

    JButton getRestartButton() {
        return restartBtn;
    }
}

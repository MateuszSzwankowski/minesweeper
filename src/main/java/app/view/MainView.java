package main.java.app.view;

import main.java.app.MinesweeperConfig;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;


public class MainView implements PropertyChangeListener {

    private final JFrame frame;
    private final TopPanel topPanel;
    private final GameMenu menu;
    private final MinesweeperTimer timer;
    private BoardView boardView;

    public MainView(MinesweeperConfig config) {
        frame = new JFrame("Minesweeper");
        BoxLayout layout = new BoxLayout(this.frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.getContentPane().setLayout(layout);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopTimer();
                frame.dispose();
            }
        });

        timer = new MinesweeperTimer();
        timer.getTimerLabelUpdater().addPropertyChangeListener("timer", this);

        topPanel = new TopPanel(config.getNumMines());
        frame.add(topPanel.getPanel());

        menu = new GameMenu();
        frame.setJMenuBar(menu.getMenuBar());

        makeBoard(config);

        frame.setVisible(true);
    }

    private void makeBoard(MinesweeperConfig config) {
        boardView = new BoardView(config);
        frame.add(boardView.getPanel());
        frame.invalidate();
        frame.validate();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        setFrameSize();
    }

    public void rebuildBoard(MinesweeperConfig config) {
        topPanel.reset(config.getNumMines());
        frame.remove(boardView.getPanel());
        makeBoard(config);
    }

    private void setFrameSize() {
        if (boardView.isScrollBarNeeded()) {
            frame.setResizable(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setResizable(false);
        }
    }

    public void disposeFrame() {
        frame.dispose();
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("flags")) {
            setFlagsLabel((int) evt.getNewValue());
        } else if (evt.getPropertyName().equals("timer")){
            setTimerLabel((int) evt.getNewValue());
        }
    }

    private void setFlagsLabel(int numFlags) {
        topPanel.setFlagsLabel(numFlags);
    }

    private void setTimerLabel(int elapsedTime) {
        topPanel.setTimerLabel(elapsedTime);
    }

    public JMenuItem getExitButton() {
        return menu.getExitButton();
    }

    public JButton getRestartButton() {
        return topPanel.getRestartButton();
    }

    public HashMap<MinesweeperConfig, JMenuItem> getNewGameButtons() {
        return menu.getNewGameButtons();
    }

    public TileView[][] getTileViews() {
        return boardView.getTileViews();
    }

}

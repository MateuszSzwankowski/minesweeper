package app.view;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;


class MinesweeperTimer {

    private final Timer timer;
    private final SwingPropertyChangeSupport timerUpdater;
    private long startTime;
    private int old_value;

    MinesweeperTimer() {
        timer = new Timer(250, e -> timerCallback());
        timerUpdater = new SwingPropertyChangeSupport(this);
        old_value = 0;
    }

    SwingPropertyChangeSupport getTimerLabelUpdater() {
        return timerUpdater;
    }

    private void timerCallback() {
        int elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        timerUpdater.firePropertyChange("timer", old_value, elapsedTime);
        old_value = elapsedTime;
    }

    void start() {
        startTime = System.currentTimeMillis();
        old_value = 0;
        timer.start();
    }

    void stop() {
        timer.stop();
    }

}
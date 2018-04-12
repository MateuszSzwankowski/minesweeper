import javax.swing.*;
import java.util.function.Consumer;

class MinesweeperTimer {

    private final Timer timer;
    private long startTime;
    private Consumer<Integer> labelUpdater;

    MinesweeperTimer(Consumer<Integer> labelUpdater) {
        this.labelUpdater = labelUpdater;
        this.timer = new Timer(250, e -> timerCallback());
    }

    private void timerCallback() {
        int elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        labelUpdater.accept(elapsedTime);
    }

    void start() {
        startTime = System.currentTimeMillis();
        timer.start();
    }

    void stop() {
        timer.stop();
    }

}
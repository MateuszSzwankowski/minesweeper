import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


class Minesweeper {

    private final JFrame frame;
    private final TopPanel topPanel;
    private final MinesweeperTimer timer;
    private Board board;
    private MinesweeperConfig config = MinesweeperConfig.INTERMEDIATE;

    public static void main(String[] args) {
        new Minesweeper();
    }

    private Minesweeper() {
        this.frame = new JFrame("Minesweeper");
        BoxLayout layout = new BoxLayout(this.frame.getContentPane(), BoxLayout.Y_AXIS);
        this.frame.getContentPane().setLayout(layout);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
            }
        });

        this.topPanel = new TopPanel(this);
        this.timer = new MinesweeperTimer(topPanel::setTimerLabel);
        this.board = new Board(this);

        this.frame.add(topPanel);
        this.frame.add(board.getPanel());
        this.frame.setJMenuBar(new MinesweeperMenu(this));
        this.frame.pack();

        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.setFrameSize();
    }

    private void setFrameSize() {
        if (this.board.isScrollBarNeeded()) {
            this.frame.setResizable(true);
            this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.frame.setResizable(false);
        }
    }

    void gameStarted() {
        this.timer.start();
    }

    void gameFinished() {
        this.timer.stop();
    }

    void newGame(MinesweeperConfig config) {
        this.config = config;
        this.restart();
    }

    void restart() {
        this.timer.stop();
        this.topPanel.reset(config.getNumMines());

        this.frame.remove(board.getPanel());
        this.board = new Board(this);
        this.frame.add(board.getPanel());

        this.frame.invalidate();
        this.frame.validate();
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.setFrameSize();
    }

    void exit() {
        this.frame.dispose();
    }

    MinesweeperConfig getConfig() {
        return this.config;
    }

    TopPanel getTopPanel() {
        return this.topPanel;
    }

}

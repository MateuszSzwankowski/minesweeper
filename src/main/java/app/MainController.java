package app;

import app.model.BoardModel;
import app.view.MainView;

import javax.swing.*;
import java.util.HashMap;


class MainController
{

    private final BoardController boardController;
    private final MainView GUI;
    private BoardModel boardModel;
    private MinesweeperConfig config = MinesweeperConfig.INTERMEDIATE;

    public MainController() {
        boardModel = new BoardModel(config);
        GUI = new MainView(config);
        boardController = new BoardController(boardModel, GUI);
        boardController.bindTiles();
        GUI.getRestartButton().addActionListener(e -> restart());
        GUI.getExitButton().addActionListener(e -> exit());

        HashMap<MinesweeperConfig, JMenuItem> newGameButtons = GUI.getNewGameButtons();
        for (MinesweeperConfig difficulty : MinesweeperConfig.values()) {
            JMenuItem menuItem = newGameButtons.get(difficulty);
            menuItem.addActionListener(e -> newGame(difficulty));
        }
    }

     private void newGame(MinesweeperConfig config) {
        this.config = config;
        restart();
    }

     private void restart() {
        GUI.stopTimer();
        GUI.rebuildBoard(config);
        boardModel = new BoardModel(config);
        boardController.setModel(boardModel);
        boardController.bindTiles();
    }

     private void exit() {
        GUI.stopTimer();
        GUI.disposeFrame();
    }

}

package app.view;

import app.MinesweeperConfig;

import javax.swing.*;
import java.util.HashMap;


class GameMenu {

    private JMenuBar menuBar;
    private JMenuItem exitMenuItem;
    private HashMap<MinesweeperConfig, JMenuItem> newGameItems;

    GameMenu() {
        menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        newGameItems = new HashMap<>();
        for (MinesweeperConfig difficulty : MinesweeperConfig.values()) {
            JMenuItem menuItem = new JMenuItem(capitalize(difficulty.name()));
            newGameItems.put(difficulty, menuItem);
            gameMenu.add(menuItem);
        }

        gameMenu.addSeparator();

        exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);

    }

    private String capitalize(String str) {
        if (str.length() <= 1) {
            return str.toUpperCase();
        } else {
            return str.substring(0, 1).toUpperCase()
                    + str.substring(1).toLowerCase();
        }
    }

    JMenuBar getMenuBar() {
        return menuBar;
    }

    JMenuItem getExitButton() {
        return exitMenuItem;
    }

    HashMap<MinesweeperConfig, JMenuItem> getNewGameButtons() {
        return newGameItems;
    }

}

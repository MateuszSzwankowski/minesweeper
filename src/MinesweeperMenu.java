import javax.swing.*;
import java.util.function.Consumer;

class MinesweeperMenu extends JMenuBar {

    MinesweeperMenu(Minesweeper game) {
        super();
        JMenu gameMenu = new JMenu("Game");
        this.add(gameMenu);

        for (MinesweeperConfig difficulty : MinesweeperConfig.values()) {
            JMenuItem menuItem = new JMenuItem(capitalize(difficulty.name()));
            menuItem.addActionListener(e -> game.newGame(difficulty));
            gameMenu.add(menuItem);
        }

        gameMenu.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> game.exit());
        gameMenu.add(exit);
    }

    private String capitalize(String str) {
        if (str.length() <= 1) {
            return str.toUpperCase();
        } else {
            return str.substring(0, 1).toUpperCase()
                    + str.substring(1).toLowerCase();
        }
    }

}

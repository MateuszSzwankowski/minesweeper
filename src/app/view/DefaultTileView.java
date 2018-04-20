package app.view;

import app.TileState;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;


class DefaultTileView implements TileView {

    private final JButton button;
    private int surroundingMinesCount = 0;
    private final String MINE_SYMBOL = "\u2622";
    private final String FLAG_SYMBOL = "\u2691";

    DefaultTileView() {
        button = new JButton(" ");
        button.setFont(new Font(null, Font.BOLD, 15));
        button.setMargin(new Insets(0, 0, 0, 0));

        Dimension d = button.getPreferredSize();
        int size = (int) Math.max(d.getHeight(), d.getWidth());
        button.setPreferredSize(new Dimension(size, size));

        button.setFocusable(false);
    }

    private void showRevealed() {
        button.setText(String.valueOf(surroundingMinesCount));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(getFontColor(surroundingMinesCount));
        button.setBorder(BorderFactory.createBevelBorder(1));
    }

    private void showMine() {
        button.setText(MINE_SYMBOL);
    }

    private void showFalseFlag() {
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
    }

    private void showExplosion() {
        button.setText(MINE_SYMBOL);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.RED);
    }

    private void showFlag(boolean flag) {
        if (flag) {
            button.setText(FLAG_SYMBOL);
            button.setForeground(Color.RED);
        } else {
            button.setText(" ");
            button.setForeground(null);
        }
    }

    private Color getFontColor(int mineCount) {
        Color color = null;
        switch (mineCount) {
            case 0: color = Color.LIGHT_GRAY;
                break;
            case 1: color = Color.BLUE;
                break;
            case 2: color = new Color(0,128,0); // green
                break;
            case 3: color = Color.RED;
                break;
            case 4: color = new Color(0,0,139); // dark blue
                break;
            case 5: color = new Color(139,69,19); // brown
                break;
            case 6: color = new Color(0, 255, 255); //cyan
                break;
            case 7: color = Color.BLACK;
                break;
            case 8: color = Color.GRAY;
                break;
        }
        return color;
    }

    @Override
    public JButton getButton() {
        return button;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("mineCount")) {
            setSurroundingMinesCount((int) evt.getNewValue());
        } else if (evt.getPropertyName().equals("state")) {
            handleStateChange((TileState) evt.getNewValue());
        }
    }

    private void handleStateChange(TileState newState) {
        switch (newState) {
            case EXPLODED:
                showExplosion();
                break;
            case FLAGGED:
                showFlag(true);
                break;
            case HIDDEN:
                showFlag(false);
                break;
            case REVEALED:
                showRevealed();
                break;
            case FALSE_FLAG:
                showFalseFlag();
                break;
            case HIDDEN_MINE:
                showMine();
                break;
        }
    }

    public void setSurroundingMinesCount(int surroundingMinesCount) {
        this.surroundingMinesCount = surroundingMinesCount;
    }

}

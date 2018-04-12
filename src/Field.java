import javax.swing.*;
import java.awt.*;


class Field {

    private final String MINE_SYMBOL = "\u2622";
    private final String FLAG_SYMBOL = "\u2691";
    private final JButton button;
    private final int row;
    private final int column;
    private boolean mine;
    private boolean flag;
    private boolean revealed;
    private int mineCount;

    Field(int row, int column) {
        this.button = new JButton(" ");
        this.button.setFont(new Font(null, Font.BOLD, 15));
        this.button.setMargin(new Insets(0, 0, 0, 0));

        Dimension d = this.button.getPreferredSize();
        int size = (int) Math.max(d.getHeight(), d.getWidth());
        this.button.setPreferredSize(new Dimension(size, size));

        this.button.setFocusable(false);

        this.row = row;
        this.column = column;
        this.mineCount = 0;
        this.mine = false;
        this.flag = false;
        this.revealed = false;
    }

    void reveal() {
        this.button.setText(String.valueOf(this.mineCount));
        this.button.setBackground(Color.LIGHT_GRAY);
        this.button.setForeground(this.getFontColor());
        this.button.setBorder(BorderFactory.createBevelBorder(1));
        this.revealed = true;
    }

    void explode() {
        this.button.setText(MINE_SYMBOL);
        this.button.setForeground(Color.BLACK);
        this.button.setBackground(Color.RED);
    }

    void gameOver() {
        if (this.isMine() && !this.isRevealed() && !this.isFlagged()) {
            this.button.setText(MINE_SYMBOL);
        } else if (this.isFlagged() && !this.isMine()) {
            this.button.setBackground(Color.RED);
            this.button.setForeground(Color.WHITE);
        }
    }

    private Color getFontColor() {
        Color color = null;
        switch (this.mineCount) {
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

    int getRow() {
        return this.row;
    }

    int getColumn() {
        return column;
    }

    boolean isMine() {
        return mine;
    }

    void putMine() {
        this.mine = true;
    }

    void removeMine() {
        this.mine = false;
    }

    boolean isFlagged() {
        return flag;
    }

    void setFlag(boolean flag) {
        this.flag = flag;
        if (flag) {
            this.button.setText(FLAG_SYMBOL);
            this.button.setForeground(Color.RED);
        } else {
            this.button.setText(" ");
            this.button.setForeground(null);
        }
    }

    boolean isRevealed() {
        return revealed;
    }

    int getMineCount() {
        return this.mineCount;
    }

    void increaseMineCount() {
        this.mineCount++;
    }

    void decreaseMineCount() {
        this.mineCount--;
    }

    JComponent getButton() {
        return button;
    }

}

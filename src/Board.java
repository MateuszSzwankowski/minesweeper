import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashSet;
import java.awt.event.*;
import java.util.function.Consumer;


class Board{

    private final Field[][] fields;
    private final JScrollPane panel;
    private final Minesweeper game;
    private final int numColumns;
    private final int numRows;
    private boolean active = false;
    private boolean firstClick = true;
    private int fieldsToDiscover;
    private int numFlags;
    private HashSet<Field> fieldsToReveal = new HashSet<>();

    Board(Minesweeper game) {
        this.numColumns = game.getConfig().getNumColumns();
        this.numRows = game.getConfig().getNumRows();
        int numMines = game.getConfig().getNumMines();
        this.game = game;
        this.fieldsToDiscover = numRows * numColumns - numMines;
        this.numFlags = 0;

        JPanel innerPanel = new JPanel();
        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        innerPanel.setLayout(new GridLayout(numRows, numColumns));

        this.fields = new Field[numRows][numColumns];
        this.makeFields(innerPanel);
        this.placeMines(numMines);

        this.panel = new JScrollPane(innerPanel);
        this.panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void makeFields(JComponent innerPanel) {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                Field field = new Field(row, column);
                this.fields[row][column] = field;
                innerPanel.add(field.getButton());
                field.getButton().addMouseListener(
                        (MousePressedListener)(e)-> click(field, e)
                );
            }
        }
    }

    private void placeMines(int minesToPlace) {
        while (minesToPlace > 0) {
            int col = (int) (Math.random() * this.numColumns);
            int row = (int) (Math.random() * this.numRows);
            Field field = this.fields[row][col];
            if (!field.isMine()) {
                field.putMine();
                minesToPlace--;
                for (Field neighbour : this.getAllNeighbours(field)) {
                    neighbour.increaseMineCount();
                }
            }
        }
    }

    private void repositionMine(Field field) {
        field.removeMine();
        for (Field neighbour : this.getAllNeighbours(field)) {
            neighbour.decreaseMineCount();
        }
        this.placeMines(1);
    }

    private void click(Field field, MouseEvent event) {
        if (!isActive() && !isFirstClick()) {
            return;
        } else if (SwingUtilities.isLeftMouseButton(event)) {
            leftClick(field);
        } else if (SwingUtilities.isRightMouseButton(event)) {
            rightClick(field);
        }
    }

    private void leftClick(Field field) {
        if (field.isFlagged()) {
            return;
        } else if (this.isFirstClick()) {
            this.firstClickDone();
            if (field.isMine()) {
                this.repositionMine(field);
            }
        }

        if (field.isRevealed()) {
            if ( field.getMineCount() == this.countFlaggedNeighbours(field) ) {
                this.revealNeighbours(field);
            }
        } else {
            this.revealField(field);
        }
    }

    private void rightClick(Field field) {
        if (field.isRevealed()) {
            return;
        } else if (!field.isFlagged()) {
            field.setFlag(true);
            this.increaseNumFlags();
        } else {
            field.setFlag(false);
            this.decreaseNumFlags();
        }
    }

    private void revealField(Field field) {
        if (field.isMine()) {
            field.explode();
            this.deactivate(false);
        } else {
            field.reveal();
            this.decreaseFieldsToDiscover();
            if (field.getMineCount() == 0) {
                this.revealNeighbours(field);
            }
        }
    }

    private void revealNeighbours(Field field) {
        if (this.anyFieldsToReveal()) {
            this.enqueueNeighboursToReveal(field);
        } else {
            this.fieldsToReveal = this.getNeighboursToReveal(field);
            while (!this.fieldsToReveal.isEmpty() && this.isActive()) {
                Field neighbour = this.fieldsToReveal.iterator().next();
                this.fieldsToReveal.remove(neighbour);
                this.revealField(neighbour);
            }
        }
    }

    private HashSet<Field> getAllNeighbours(Field field) {
        int row = field.getRow();
        int column = field.getColumn();
        HashSet<Field> neighbours = new HashSet<>();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if ((i != row || j != column)
                        && i >= 0 && i < numRows
                        && j >= 0 && j < numColumns) {
                    neighbours.add(this.fields[i][j]);
                }
            }
        }
        return neighbours;
    }

    private HashSet<Field> getNeighboursToReveal(Field field){
        HashSet<Field> neighbours = this.getAllNeighbours(field);
        HashSet<Field> neighboursToReveal = new HashSet<>();
        for (Field neighbour : neighbours) {
            if (!neighbour.isRevealed() && !neighbour.isFlagged()) {
                neighboursToReveal.add(neighbour);
            }
        }
        return neighboursToReveal;
    }

    private boolean anyFieldsToReveal() {
        return !this.fieldsToReveal.isEmpty();
    }

    private void enqueueNeighboursToReveal(Field field) {
        HashSet<Field> unrevealedNeighbours = this.getNeighboursToReveal(field);
        this.fieldsToReveal.addAll(unrevealedNeighbours);
    }

    private int countFlaggedNeighbours(Field field) {
        HashSet<Field> neighbours = this.getAllNeighbours(field);
        int numNeighboursFlagged = 0;
        for (Field neighbour : neighbours) {
            if (neighbour.isFlagged()) {
                numNeighboursFlagged++;
            }
        }
        return numNeighboursFlagged;
    }

    private void decreaseFieldsToDiscover() {
        this.fieldsToDiscover--;
        if (this.isFinished()) {
            this.deactivate(true);
        }
    }

    private void increaseNumFlags() {
        this.numFlags++;
        this.game.getTopPanel().setFlagsLabel(this.numFlags);
    }

    private void decreaseNumFlags() {
        this.numFlags--;
        this.game.getTopPanel().setFlagsLabel(this.numFlags);
    }

    private boolean isActive() {
        return active;
    }

    private void deactivate(boolean win) {
        this.active = false;
        if (win) this.showFlags(); else this.showMines();
        this.game.gameFinished();
    }

    private void showMines() {
        for (Field[] row : this.fields) {
            for (Field field : row) {
                field.gameOver();
            }
        }
    }

    private void showFlags() {
        for (Field[] row : this.fields) {
            for (Field field : row) {
                if (field.isMine() && !field.isFlagged()){
                    field.setFlag(true);
                    this.increaseNumFlags();
                }
            }
        }
    }

    private boolean isFinished() {
        return this.fieldsToDiscover == 0;
    }

    private boolean isFirstClick() {
        return firstClick;
    }

    private void firstClickDone() {
        this.firstClick = false;
        this.active = true;
        this.game.gameStarted();
    }

    JComponent getPanel() {
        return panel;
    }

    boolean isScrollBarNeeded() {
        return (this.panel.getVerticalScrollBar().isVisible() ||
                this.panel.getHorizontalScrollBar().isVisible());
    }

}

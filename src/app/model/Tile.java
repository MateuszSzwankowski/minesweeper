package app.model;

import app.TileState;
import static app.TileState.*;

import javax.swing.event.SwingPropertyChangeSupport;


public class Tile {

    private final SwingPropertyChangeSupport stateChange;
    private final SwingPropertyChangeSupport mineCountChange;
    private TileState state = HIDDEN;
    private final int row;
    private final int column;
    private boolean mine = false;
    private int mineCount = 0;

    Tile(int row, int column) {
        this.row = row;
        this.column = column;
        stateChange = new SwingPropertyChangeSupport(this);
        mineCountChange = new SwingPropertyChangeSupport(this);
    }

    private void changeState(TileState newState) {
        TileState oldState = state;
        state = newState;
        stateChange.firePropertyChange("state", oldState, newState);
    }

    public void reveal() {
        changeState(REVEALED);
    }

    public void explode() {
        changeState(EXPLODED);
    }

    public void gameOver() {
        if (isMine() && state.equals(HIDDEN)) {
            this.changeState(HIDDEN_MINE);
        } else if (this.state.equals(FLAGGED) && !isMine()) {
            changeState(FALSE_FLAG);
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isMine() {
        return mine;
    }

    public void putMine() {
        mine = true;
    }

    public void removeMine() {
        mine = false;
    }

    public boolean isFlagged() {
        return state.equals(FLAGGED);
    }

    public void setFlag(boolean flag) {
        if (flag) changeState(FLAGGED);
        else changeState(HIDDEN);
    }

    public boolean isRevealed() {
        return state.equals(REVEALED);
    }

    public int getSurroundingMinesCount() {
        return mineCount;
    }

    public void increaseMineCount() {
        fireMineCountChange(mineCount + 1);
    }

    public void decreaseMineCount() {
        fireMineCountChange(mineCount - 1);
    }

    private void fireMineCountChange(int newMineCount) {
        mineCountChange.firePropertyChange("mineCount", mineCount, newMineCount);
        mineCount = newMineCount;
    }

    public SwingPropertyChangeSupport getStateChangeSupport() {
        return stateChange;
    }

    public SwingPropertyChangeSupport getMineCountChangeSupport() {
        return mineCountChange;
    }

}

package test.java.app.model;

import main.java.app.model.Tile;
import main.java.app.TileState;
import static main.java.app.TileState.*;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class TileTest {

    private Tile tile;
    private Field stateField;
    private Field mineField;
    private Field mineCountField;
    private Field rowField;
    private Field columnField;

    @Before
    public void setUp() throws Exception {
        stateField = Tile.class.getDeclaredField("state");
        stateField.setAccessible(true);

        mineField = Tile.class.getDeclaredField("mine");
        mineField.setAccessible(true);

        mineCountField = Tile.class.getDeclaredField("mineCount");
        mineCountField.setAccessible(true);

        rowField = Tile.class.getDeclaredField("row");
        rowField.setAccessible(true);

        columnField = Tile.class.getDeclaredField("column");
        columnField.setAccessible(true);

        tile = new Tile(3,4);
    }

    @Test
    public void reveal() throws Exception {
        tile.reveal();
        TileState state = (TileState) stateField.get(tile);
        assertEquals(state, REVEALED);
    }

    @Test
    public void explode() throws Exception {
        tile.explode();
        TileState state = (TileState) stateField.get(tile);
        assertEquals(state, EXPLODED);
    }

    @Test
    public void gameOver() throws Exception {
        mineField.set(tile, true);
        stateField.set(tile, HIDDEN);
        tile.gameOver();
        assertEquals(stateField.get(tile), HIDDEN_MINE);

        mineField.set(tile, false);
        stateField.set(tile, FLAGGED);
        tile.gameOver();
        assertEquals(stateField.get(tile), FALSE_FLAG);
    }

    @Test
    public void getRow() throws Exception {
        assertEquals(3, tile.getRow());
        rowField.set(tile, 11);
        assertEquals(11, tile.getRow());
    }

    @Test
    public void getColumn() throws Exception {
        assertEquals(4, tile.getColumn());
        columnField.set(tile, 11);
        assertEquals(11, tile.getColumn());
    }

    @Test
    public void isMine() throws Exception{
        mineField.set(tile, false);
        assertFalse(tile.isMine());
        mineField.set(tile, true);
        assertTrue(tile.isMine());
    }

    @Test
    public void putMine() throws Exception {
        tile.putMine();
        assertTrue(mineField.getBoolean(tile));
    }

    @Test
    public void removeMine() throws Exception {
        tile.removeMine();
        assertFalse(mineField.getBoolean(tile));
    }

    @Test
    public void isFlagged() throws Exception {
        stateField.set(tile, FLAGGED);
        assertTrue(tile.isFlagged());
        stateField.set(tile, HIDDEN);
        assertFalse(tile.isFlagged());
    }

    @Test
    public void setFlag() throws Exception {
        tile.setFlag(true);
        assertEquals(FLAGGED, stateField.get(tile));
        tile.setFlag(false);
        assertEquals(HIDDEN, stateField.get(tile));
    }

    @Test
    public void isRevealed() throws Exception {
        assertFalse(tile.isRevealed());
        stateField.set(tile, REVEALED);
        assertTrue(tile.isRevealed());
    }

    @Test
    public void getSurroundingMinesCount() throws Exception {
        assertEquals(tile.getSurroundingMinesCount(), mineCountField.getInt(tile));
    }

    @Test
    public void increaseMineCount() throws Exception {
        int old_value = mineCountField.getInt(tile);
        tile.increaseMineCount();
        assertEquals(old_value + 1, mineCountField.getInt(tile));
    }

    @Test
    public void decreaseMineCount() throws Exception {
        int old_value = mineCountField.getInt(tile);
        tile.decreaseMineCount();
        assertEquals(old_value - 1, mineCountField.getInt(tile));
    }

}
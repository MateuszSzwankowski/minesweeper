package app.model;

import app.MinesweeperConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class BoardModelTest {
    private BoardModel board;
    private int numRows;
    private int numColumns;
    private int numMines;

    @Before
    public void setUp(){
        MinesweeperConfig config = MinesweeperConfig.INTERMEDIATE;
        numColumns = config.getNumColumns();
        numMines = config.getNumMines();
        numRows = config.getNumRows();

        BoardModel.setRandomSeed(42);
        board = new BoardModel(config);
    }

    @Test
    public void getFields() {
        Tile[][] tiles = board.getTiles();

        assertEquals(tiles.length, numRows);
        assertEquals(tiles[0].length, numColumns);

        assertEquals(tiles[0][0].getRow(), 0);
        assertEquals(tiles[0][0].getColumn(), 0);

        assertEquals(tiles[3][8].getRow(), 3);
        assertEquals(tiles[3][8].getColumn(), 8);
    }

    @Test
    public void repositionMine() {
        Tile tileWithMine = board.getTiles()[2][2];
        Tile repositionTarget = board.getTiles()[15][2];

        assertTrue(tileWithMine.isMine());
        assertFalse(repositionTarget.isMine());

        board.repositionMine(tileWithMine);

        assertFalse(tileWithMine.isMine());
        assertTrue(repositionTarget.isMine());
    }

    @Test
    public void getSurroundingFields() {
        Tile tile = board.getTiles()[0][0];
        HashSet<Tile> neighbours = board.getSurroundingFields(tile);
        assertEquals(neighbours.size(), 3);
        assertTrue(neighbours.contains(board.getTiles()[0][1]));
        assertTrue(neighbours.contains(board.getTiles()[1][0]));
        assertTrue(neighbours.contains(board.getTiles()[1][1]));

        tile = board.getTiles()[1][1];
        neighbours = board.getSurroundingFields(tile);
        assertEquals(neighbours.size(), 8);
        assertTrue(neighbours.contains(board.getTiles()[0][0]));
        assertTrue(neighbours.contains(board.getTiles()[0][1]));
        assertTrue(neighbours.contains(board.getTiles()[0][2]));
        assertTrue(neighbours.contains(board.getTiles()[1][0]));
        assertTrue(neighbours.contains(board.getTiles()[1][2]));
        assertTrue(neighbours.contains(board.getTiles()[2][0]));
        assertTrue(neighbours.contains(board.getTiles()[2][1]));
        assertTrue(neighbours.contains(board.getTiles()[2][2]));
    }

    @Test
    public void decreaseFieldsToDiscover() {
        board.activate();
        int numFieldsToDiscover = numColumns * numRows - numMines;
        for (int i = 0; i < numFieldsToDiscover; i++) {
            assertEquals(board.getState(), BoardState.ACTIVE);
            board.decreaseFieldsToDiscover();
        }
        assertEquals(board.getState(), BoardState.FINISHED);
    }

    @Test
    public void activate() {
        board.activate();
        assertEquals(board.getState(), BoardState.ACTIVE);
    }

    @Test
    public void deactivate() {
        board.deactivate();
        assertEquals(board.getState(), BoardState.FINISHED);
    }

    @Test
    public void getState() {
        assertEquals(board.getState(), BoardState.BEFORE_START);
    }

}
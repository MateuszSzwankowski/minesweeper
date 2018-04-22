package app.model;

import app.MinesweeperConfig;

import static app.model.BoardState.*;

import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
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
        board = new BoardModel(config); // todo: mock tiles
    }

    @Test
    public void getTiles() {
        Tile[][] tiles = board.getTiles();

        assertEquals(tiles.length, numRows);
        assertEquals(tiles[0].length, numColumns);

        assertEquals(tiles[0][0].getRow(), 0);
        assertEquals(tiles[0][0].getColumn(), 0);

        assertEquals(tiles[3][8].getRow(), 3);
        assertEquals(tiles[3][8].getColumn(), 8);
    }

    @Test
    public void getSurroundingTiles() {
        Tile tile = board.getTiles()[0][0];
        HashSet<Tile> neighbours = board.getSurroundingTiles(tile);
        assertEquals(neighbours.size(), 3);
        assertTrue(neighbours.contains(board.getTiles()[0][1]));
        assertTrue(neighbours.contains(board.getTiles()[1][0]));
        assertTrue(neighbours.contains(board.getTiles()[1][1]));

        tile = board.getTiles()[1][1];
        neighbours = board.getSurroundingTiles(tile);
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
    public void decreaseTilesToDiscover() {
        board.activate();
        int numTilesToDiscover = numColumns * numRows - numMines;
        for (int i = 0; i < numTilesToDiscover; i++) {
            assertEquals(board.getState(), ACTIVE);
            board.decreaseTilesToDiscover();
        }
        assertEquals(board.getState(), FINISHED);
    }

    @Test
    public void activate() {
        board.activate();
        assertEquals(board.getState(), ACTIVE);
    }

    @Test
    public void deactivate() {
        board.deactivate();
        assertEquals(board.getState(), FINISHED);
    }

    @Test
    public void getState() {
        assertEquals(board.getState(), BEFORE_START);
    }

}
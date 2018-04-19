package test;

import app.model.BoardModel;
import app.model.BoardState;
import app.model.Field;
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
        Field[][] fields = board.getFields();

        assertEquals(fields.length, numRows);
        assertEquals(fields[0].length, numColumns);

        assertEquals(fields[0][0].getRow(), 0);
        assertEquals(fields[0][0].getColumn(), 0);

        assertEquals(fields[3][8].getRow(), 3);
        assertEquals(fields[3][8].getColumn(), 8);
    }

    @Test
    public void repositionMine() {
        Field fieldWithMine = board.getFields()[2][2];
        Field repositionTarget = board.getFields()[15][2];

        assertTrue(fieldWithMine.isMine());
        assertFalse(repositionTarget.isMine());

        board.repositionMine(fieldWithMine);

        assertFalse(fieldWithMine.isMine());
        assertTrue(repositionTarget.isMine());
    }

    @Test
    public void getSurroundingFields() {
        Field field = board.getFields()[0][0];
        HashSet<Field> neighbours = board.getSurroundingFields(field);
        assertEquals(neighbours.size(), 3);
        assertTrue(neighbours.contains(board.getFields()[0][1]));
        assertTrue(neighbours.contains(board.getFields()[1][0]));
        assertTrue(neighbours.contains(board.getFields()[1][1]));

        field = board.getFields()[1][1];
        neighbours = board.getSurroundingFields(field);
        assertEquals(neighbours.size(), 8);
        assertTrue(neighbours.contains(board.getFields()[0][0]));
        assertTrue(neighbours.contains(board.getFields()[0][1]));
        assertTrue(neighbours.contains(board.getFields()[0][2]));
        assertTrue(neighbours.contains(board.getFields()[1][0]));
        assertTrue(neighbours.contains(board.getFields()[1][2]));
        assertTrue(neighbours.contains(board.getFields()[2][0]));
        assertTrue(neighbours.contains(board.getFields()[2][1]));
        assertTrue(neighbours.contains(board.getFields()[2][2]));
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
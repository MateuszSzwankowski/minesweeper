package app;

import app.model.BoardModel;
import app.model.Tile;
import app.view.MainView;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class BoardControllerTest {
    private BoardController controller;
    private BoardModel model;
    private final MouseEvent LEFT_CLICK = new MouseEvent(new JButton(), 0, 0,
                InputEvent.BUTTON1_DOWN_MASK, 100, 100, 1, false);
    private final MouseEvent RIGHT_CLICK = new MouseEvent(new JButton(), 0, 0,
                InputEvent.BUTTON3_DOWN_MASK, 100, 100, 1, false);


    @Before
    public void setUp() {
        BoardModel.setRandomSeed(42);
        model = new BoardModel(MinesweeperConfig.INTERMEDIATE);
        MainView dummy = mock(MainView.class);

        controller = new BoardController(model, dummy);
    }

    @Test
    public void click() { // todo: burn all down and start again
        Tile[][] tiles = model.getTiles();

        controller.click(tiles[0][13], LEFT_CLICK);
        assertTrue(tiles[0][14].isRevealed());
        assertTrue(tiles[1][14].isRevealed());
        assertTrue(tiles[0][12].isRevealed());
        assertTrue(tiles[1][13].isRevealed());
        assertFalse(tiles[0][15].isRevealed());
        assertFalse(tiles[2][13].isRevealed());

        controller.click(tiles[1][15], RIGHT_CLICK);
        assertTrue(tiles[1][15].isFlagged());
        controller.click(tiles[1][15], LEFT_CLICK);
        assertTrue(tiles[1][15].isFlagged());
        assertFalse(tiles[1][15].isRevealed());

        controller.click(tiles[1][15], RIGHT_CLICK);
        assertFalse(tiles[1][15].isFlagged());

        controller.click(tiles[1][15], RIGHT_CLICK);
        assertTrue(tiles[1][15].isFlagged());

        controller.click(tiles[2][14], RIGHT_CLICK);
        controller.click(tiles[1][14], LEFT_CLICK);
        assertTrue(tiles[0][15].isRevealed());
        assertTrue(tiles[2][13].isRevealed());
        assertTrue(tiles[2][15].isRevealed());
        assertFalse(tiles[1][15].isRevealed());
        assertFalse(tiles[2][14].isRevealed());

        controller.click(tiles[8][0], LEFT_CLICK);
        assertFalse(tiles[10][5].isRevealed());
        assertTrue(tiles[15][0].isRevealed());

        controller.click(tiles[15][2], LEFT_CLICK);
        assertFalse(tiles[15][2].isRevealed());
    }

    @Test
    public void setModel() throws Exception {
        Field stateField = BoardController.class.getDeclaredField("model");
        stateField.setAccessible(true);

        assertEquals(model, stateField.get(controller));

        BoardModel newModel = new BoardModel(MinesweeperConfig.BEGINNER);
        controller.setModel(newModel);

        assertNotEquals(model, stateField.get(controller));
    }

}
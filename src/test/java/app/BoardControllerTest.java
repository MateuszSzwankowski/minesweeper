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

        controller.click(tiles[2][3], RIGHT_CLICK);
        assertFalse(tiles[1][15].isFlagged()); // does not flag tile before game starts

        controller.click(tiles[2][3], LEFT_CLICK);
        assertTrue(tiles[2][3].isRevealed()); // reveals tile on left click
        assertTrue(tiles[1][1].isRevealed()); // reveals neighbours after clicking empty tile
        assertTrue(tiles[1][4].isRevealed());
        assertTrue(tiles[3][1].isRevealed());
        assertTrue(tiles[3][4].isRevealed());
        assertFalse(tiles[2][0].isRevealed()); // does not reveal more than it should
        assertFalse(tiles[4][5].isRevealed());

        controller.click(tiles[2][0], RIGHT_CLICK);
        assertTrue(tiles[2][0].isFlagged()); //puts a flag
        controller.click(tiles[2][0], LEFT_CLICK);
        assertTrue(tiles[2][0].isFlagged()); // tile stays flagged after left click
        assertFalse(tiles[2][0].isRevealed()); // does not reveal flagged tile

        controller.click(tiles[2][0], RIGHT_CLICK);
        assertFalse(tiles[2][0].isFlagged()); // removes flag

        controller.click(tiles[2][0], RIGHT_CLICK);
        assertTrue(tiles[2][0].isFlagged()); // puts flag again

        controller.click(tiles[4][1], RIGHT_CLICK);
        controller.click(tiles[3][1], LEFT_CLICK); // left click on revealed field
        assertTrue(tiles[3][0].isRevealed()); // reveals neighbours when tile value
        assertTrue(tiles[4][0].isRevealed()); // is equal to number of surrounding flags
        assertTrue(tiles[4][2].isRevealed());
        assertFalse(tiles[2][0].isRevealed()); // does not reveal flagged neighbours
        assertFalse(tiles[4][1].isRevealed());

        controller.click(tiles[0][2], LEFT_CLICK); // click on tile with mine
        assertFalse(tiles[0][2].isRevealed()); // does not reveal field with mine
        assertFalse(tiles[0][3].isRevealed()); // and its neighbours

        controller.click(tiles[0][0], LEFT_CLICK);
        assertFalse(tiles[0][0].isRevealed()); // does not reveal tiles when game is over
    }

    @Test
    public void setModel() throws Exception {
        Field stateField = BoardController.class.getDeclaredField("model");
        stateField.setAccessible(true);

        assertEquals(model, stateField.get(controller));
        BoardModel newModel = new BoardModel(MinesweeperConfig.BEGINNER);
        controller.setModel(newModel);
        assertEquals(newModel, stateField.get(controller));
    }

}
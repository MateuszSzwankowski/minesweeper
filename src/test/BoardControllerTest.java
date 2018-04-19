package test;

import app.BoardController;
import app.model.BoardModel;
import app.model.Field;
import app.MinesweeperConfig;

import app.view.MainView;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;

public class BoardControllerTest {
    private BoardController controller;
    private BoardModel model;
    private MouseEvent LEFT_CLICK = new MouseEvent(new JButton(), 0, 0,
                InputEvent.BUTTON1_DOWN_MASK, 100, 100, 1, false);
    private MouseEvent RIGHT_CLICK = new MouseEvent(new JButton(), 0, 0,
                InputEvent.BUTTON3_DOWN_MASK, 100, 100, 1, false);


    @Before
    public void setUp() {
        BoardModel.setRandomSeed(42);
        model = new BoardModel(MinesweeperConfig.INTERMEDIATE);
//        MainView dummy = new mock(MainView.class);
        MainView dummy = new MainView(MinesweeperConfig.INTERMEDIATE);
        controller = new BoardController(model, dummy);
    }

    @Test
    public void click() {
        Field[][] fields = model.getFields();

        controller.click(fields[0][13], LEFT_CLICK);
        assertTrue(fields[0][14].isRevealed());
        assertTrue(fields[1][14].isRevealed());
        assertTrue(fields[0][12].isRevealed());
        assertTrue(fields[1][13].isRevealed());
        assertFalse(fields[0][15].isRevealed());
        assertFalse(fields[2][13].isRevealed());

        controller.click(fields[1][15], RIGHT_CLICK);
        assertTrue(fields[1][15].isFlagged());
        controller.click(fields[1][15], LEFT_CLICK);
        assertTrue(fields[1][15].isFlagged());
        assertFalse(fields[1][15].isRevealed());

        controller.click(fields[1][15], RIGHT_CLICK);
        assertFalse(fields[1][15].isFlagged());

        controller.click(fields[1][15], RIGHT_CLICK);
        assertTrue(fields[1][15].isFlagged());

        controller.click(fields[2][14], RIGHT_CLICK);
        controller.click(fields[1][14], LEFT_CLICK);
        assertTrue(fields[0][15].isRevealed());
        assertTrue(fields[2][13].isRevealed());
        assertTrue(fields[2][15].isRevealed());
        assertFalse(fields[1][15].isRevealed());
        assertFalse(fields[2][14].isRevealed());

        controller.click(fields[8][0], LEFT_CLICK);
        assertFalse(fields[10][5].isRevealed());
        assertTrue(fields[15][0].isRevealed());

        controller.click(fields[15][2], LEFT_CLICK);
        assertFalse(fields[15][2].isRevealed());
    }

    @Test
    public void getModel() {
        assertEquals(model, controller.getModel());
    }

    @Test
    public void setModel() {
        BoardModel newModel = new BoardModel(MinesweeperConfig.INTERMEDIATE);
        controller.setModel(newModel);
        assertNotEquals(model, controller.getModel());
    }

}
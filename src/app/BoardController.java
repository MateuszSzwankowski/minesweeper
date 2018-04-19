package app;

import app.model.Field;
import app.view.FieldView;
import app.view.MainView;
import app.model.BoardModel;
import static app.model.BoardState.*;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.util.HashSet;
import java.awt.event.*;
import java.util.Set;


public class BoardController {

    private BoardModel model;
    private final MainView GUI;
    private int numFlags = 0;
    private final Set<Field> fieldsToReveal = new HashSet<>();
    private final SwingPropertyChangeSupport flagsLabelUpdater;

    public BoardController(BoardModel model, MainView GUI) {
        this.model = model;
        this.GUI = GUI;
        bindFields();
        flagsLabelUpdater = new SwingPropertyChangeSupport(this);
        flagsLabelUpdater.addPropertyChangeListener("flags", GUI);
    }

    public void setModel(BoardModel boardModel) {
        model = boardModel;
        bindFields();
        reset();
    }

    private void bindFields() {
        FieldView[][] fieldViews = GUI.getFieldViews();
        Field[][] fields = model.getFields();
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                Field field = fields[i][j];
                FieldView fieldView = fieldViews[i][j];
                fieldView.setSurroundingMinesCount(field.getSurroundingMinesCount());
                field.getStateChangeSupport().addPropertyChangeListener("state", fieldView);
                field.getMineCountChangeSupport().addPropertyChangeListener("mineCount", fieldView);
                fieldView.getButton().addMouseListener((app.MousePressedListener)(e)-> click(field, e));
            }
        }
    }

    private void reset() {
        numFlags = 0;
        fieldsToReveal.clear();
    }

    public void click(Field field, MouseEvent event) {
        if (!model.getState().equals(FINISHED)) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                leftClick(field);
            } else if (SwingUtilities.isRightMouseButton(event)) {
                rightClick(field);
            }
        }
    }

    private void leftClick(Field field) {
        if (field.isFlagged()) {
            return;
        } else if (model.getState().equals(BEFORE_START)) {
            model.activate();
            GUI.startTimer();
            if (field.isMine()) {
                model.repositionMine(field);
            }
        }

        if (field.isRevealed()) {
            if ( field.getSurroundingMinesCount() == countFlaggedNeighbours(field) ) {
                revealNeighbours(field);
            }
        } else {
            revealField(field);
        }
    }

    private void rightClick(Field field) {
        if (!field.isRevealed() && model.getState().equals(ACTIVE)) {
            if (!field.isFlagged()) {
                field.setFlag(true);
                numFlags++;
            } else {
                field.setFlag(false);
                numFlags--;
            }
            updateFlagsLabel();
        }
    }

    private void updateFlagsLabel() {
        flagsLabelUpdater.firePropertyChange("flags", null, numFlags);
    }

    private void revealField(Field field) {
        if (field.isMine()) {
            field.explode();
            gameFinished(false);
        } else {
            field.reveal();
            model.decreaseFieldsToDiscover();

            if (field.getSurroundingMinesCount() == 0) {
                revealNeighbours(field);
            }

            if (model.getState().equals(FINISHED)) {
                gameFinished(true);
            }
        }
    }

    private void revealNeighbours(Field field) {
        if (anyFieldsToReveal()) {
            enqueueNeighboursToReveal(field);
        } else {
            enqueueNeighboursToReveal(field);
            while (!fieldsToReveal.isEmpty() && model.getState().equals(ACTIVE)) {
                Field neighbour = fieldsToReveal.iterator().next();
                fieldsToReveal.remove(neighbour);
                revealField(neighbour);
            }
        }
    }

    private HashSet<Field> getNeighboursToReveal(Field field){
        HashSet<Field> neighbours = model.getSurroundingFields(field);
        HashSet<Field> neighboursToReveal = new HashSet<>();
        for (Field neighbour : neighbours) {
            if (!neighbour.isRevealed() && !neighbour.isFlagged()) {
                neighboursToReveal.add(neighbour);
            }
        }
        return neighboursToReveal;
    }

    private boolean anyFieldsToReveal() {
        return !fieldsToReveal.isEmpty();
    }

    private void enqueueNeighboursToReveal(Field field) {
        HashSet<Field> unrevealedNeighbours = getNeighboursToReveal(field);
        fieldsToReveal.addAll(unrevealedNeighbours);
    }

    private int countFlaggedNeighbours(Field field) {
        HashSet<Field> neighbours = model.getSurroundingFields(field);
        int numNeighboursFlagged = 0;
        for (Field neighbour : neighbours) {
            if (neighbour.isFlagged()) {
                numNeighboursFlagged++;
            }
        }
        return numNeighboursFlagged;
    }

    private void gameFinished(boolean win) {
        if (win) showFlags(); else showMines();
        GUI.stopTimer();
        model.deactivate();
    }

    private void showMines() {
        for (Field[] row : model.getFields()) {
            for (Field field : row) {
                field.gameOver();
            }
        }
    }

    private void showFlags() {
        for (Field[] row : model.getFields()) {
            for (Field field : row) {
                if (field.isMine() && !field.isFlagged()){
                    field.setFlag(true);
                    numFlags++;
                    updateFlagsLabel();
                }
            }
        }
    }

    public BoardModel getModel() {
        return model;
    }

}

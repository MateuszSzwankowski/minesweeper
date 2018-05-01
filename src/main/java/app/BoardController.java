package app;

import app.model.Tile;
import app.view.TileView;
import app.view.MainView;
import app.model.BoardModel;
import static app.model.BoardState.*;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.util.HashSet;
import java.awt.event.*;
import java.util.Set;


class BoardController {

    private BoardModel model;
    private final MainView GUI;
    private int numFlags = 0;
    private final Set<Tile> tilesToReveal = new HashSet<>();
    private final SwingPropertyChangeSupport flagsLabelUpdater;

    BoardController(BoardModel model, MainView GUI) {
        this.model = model;
        this.GUI = GUI;
        flagsLabelUpdater = new SwingPropertyChangeSupport(this);
        flagsLabelUpdater.addPropertyChangeListener("flags", GUI);
    }

    public void setModel(BoardModel boardModel) {
        model = boardModel;
    }

    void bindTiles() {
        TileView[][] tileViews = GUI.getTileViews();
        Tile[][] tiles = model.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                Tile tile = tiles[i][j];
                TileView tileView = tileViews[i][j];
                tileView.setSurroundingMinesCount(tile.getSurroundingMinesCount());
                tile.getStateChangeSupport().addPropertyChangeListener("state", tileView);
                tile.getMineCountChangeSupport().addPropertyChangeListener("mineCount", tileView);
                tileView.getButton().addMouseListener((MousePressedListener)(e)-> click(tile, e));
            }
        }
    }

    public void click(Tile tile, MouseEvent event) {
        if (model.getState() == FINISHED) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(event)) {
            if (model.getState() == BEFORE_START) {
                startGame(tile);
            }
            leftClick(tile);
        } else if (SwingUtilities.isRightMouseButton(event)
                && model.getState() == ACTIVE) {
            rightClick(tile);
        }
    }

    private void startGame(Tile firstClickTile) {
        model.placeMines(firstClickTile);
        model.activate();
        GUI.startTimer();
    }

    private void leftClick(Tile tile) {
        if (tile.isFlagged()) {
            return;
        }

        if (tile.isRevealed()) {
            if ( tile.getSurroundingMinesCount() == countFlaggedNeighbours(tile) ) {
                revealNeighbours(tile);
            }
        } else {
            revealTile(tile);
        }
    }

    private void rightClick(Tile tile) {
        if (!tile.isRevealed()) {
            if (!tile.isFlagged()) {
                tile.setFlag(true);
                numFlags++;
            } else {
                tile.setFlag(false);
                numFlags--;
            }
            updateFlagsLabel();
        }
    }

    private void updateFlagsLabel() {
        flagsLabelUpdater.firePropertyChange("flags", null, numFlags);
    }

    private void revealTile(Tile tile) {
        if (tile.isMine()) {
            tile.explode();
            gameFinished(false);
        } else {
            tile.reveal();
            model.decreaseTilesToDiscover();

            if (tile.getSurroundingMinesCount() == 0) {
                revealNeighbours(tile);
            }

            if (model.getState() == FINISHED) {
                gameFinished(true);
            }
        }
    }



    private void revealNeighbours(Tile tile) {
        if (anyTilesToReveal()) {
            enqueueNeighboursToReveal(tile);
        } else {
            enqueueNeighboursToReveal(tile);
            while (!tilesToReveal.isEmpty()) {
                Tile neighbour = tilesToReveal.iterator().next();
                tilesToReveal.remove(neighbour);
                revealTile(neighbour);
            }
        }
    }

    private HashSet<Tile> getNeighboursToReveal(Tile tile){
        HashSet<Tile> neighbours = model.getSurroundingTiles(tile);
        HashSet<Tile> neighboursToReveal = new HashSet<>();
        for (Tile neighbour : neighbours) {
            if (!neighbour.isRevealed() && !neighbour.isFlagged()) {
                neighboursToReveal.add(neighbour);
            }
        }
        return neighboursToReveal;
    }

    private boolean anyTilesToReveal() {
        return !tilesToReveal.isEmpty();
    }

    private void enqueueNeighboursToReveal(Tile tile) {
        HashSet<Tile> unrevealedNeighbours = getNeighboursToReveal(tile);
        tilesToReveal.addAll(unrevealedNeighbours);
    }

    private int countFlaggedNeighbours(Tile tile) {
        HashSet<Tile> neighbours = model.getSurroundingTiles(tile);
        int numNeighboursFlagged = 0;
        for (Tile neighbour : neighbours) {
            if (neighbour.isFlagged()) {
                numNeighboursFlagged++;
            }
        }
        return numNeighboursFlagged;
    }

    private void gameFinished(boolean win) {
        if (win) showFlags(); else showMines();
        numFlags = 0;
        tilesToReveal.clear();
        GUI.stopTimer();
        model.deactivate();
    }

    private void showMines() {
        for (Tile[] row : model.getTiles()) {
            for (Tile tile : row) {
                tile.gameOver();
            }
        }
    }

    private void showFlags() {
        for (Tile[] row : model.getTiles()) {
            for (Tile tile : row) {
                if (tile.isMine() && !tile.isFlagged()){
                    tile.setFlag(true);
                    numFlags++;
                    updateFlagsLabel();
                }
            }
        }
    }

}

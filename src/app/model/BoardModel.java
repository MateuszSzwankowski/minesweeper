package app.model;

import app.MinesweeperConfig;
import static app.model.BoardState.*;

import java.util.HashSet;
import java.util.Random;


public class BoardModel {

    private final Tile[][] tiles;
    private final int numRows;
    private final int numColumns;
    private static Random rnd = new Random();
    private int fieldsToDiscover;
    private BoardState state = BEFORE_START;

    public static void setRandomSeed(int seed) {
        rnd = new Random(seed);
    }

    public BoardModel(MinesweeperConfig config) {
        numColumns = config.getNumColumns();
        numRows = config.getNumRows();
        int numMines = config.getNumMines();
        fieldsToDiscover = numRows * numColumns - numMines;
        tiles = new Tile[numRows][numColumns];
        makeFields();
        placeMines(numMines);
    }

    private void makeFields() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                tiles[row][column] = new Tile(row, column);
            }
        }
    }

    private void placeMines(int minesToPlace) {
        while (minesToPlace > 0) {
            int col = rnd.nextInt(numColumns);
            int row = rnd.nextInt(numRows);
            Tile tile = tiles[row][col];
            if (!tile.isMine()) {
                tile.putMine();
                minesToPlace--;
                getSurroundingFields(tile).forEach(Tile::increaseMineCount);
            }
        }
    }

    public void repositionMine(Tile tile) {
        tile.removeMine();
        getSurroundingFields(tile).forEach(Tile::decreaseMineCount);
        placeMines(1);
    }

    public HashSet<Tile> getSurroundingFields(Tile tile) {
        int row = tile.getRow();
        int column = tile.getColumn();
        HashSet<Tile> neighbours = new HashSet<>();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if ((i != row || j != column)
                        && i >= 0 && i < numRows
                        && j >= 0 && j < numColumns) {
                    neighbours.add(tiles[i][j]);
                }
            }
        }
        return neighbours;
    }

    public void decreaseFieldsToDiscover() {
        fieldsToDiscover--;
        if (fieldsToDiscover == 0) state = FINISHED;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void activate() {
        state = ACTIVE;
    }

    public void deactivate() {
        state = FINISHED;
    }

    public BoardState getState() {
        return state;
    }

}

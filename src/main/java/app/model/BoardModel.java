package app.model;

import app.MinesweeperConfig;
import static app.model.BoardState.*;

import java.util.*;
import java.util.stream.Collectors;


public class BoardModel {

    private final Tile[][] tiles;
    private final int numRows;
    private final int numColumns;
    private final int numMines;
    private static Random rnd = new Random();
    private int tilesToDiscover;
    private BoardState state = BEFORE_START;

    public static void setRandomSeed(int seed) {
        rnd = new Random(seed);
    }

    public BoardModel(MinesweeperConfig config) {
        numColumns = config.getNumColumns();
        numRows = config.getNumRows();
        numMines = config.getNumMines();
        tilesToDiscover = numRows * numColumns - numMines;
        tiles = new Tile[numRows][numColumns];
        makeTiles();
    }

    private void makeTiles() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                tiles[row][column] = new Tile(row, column);
            }
        }
    }

    public void placeMines(Tile excludedTile) {
        List<Tile> tilesList = Arrays.stream(tiles)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        tilesList.remove(excludedTile);

        for (int i = 0; i < numMines; i++) {
            int idx = rnd.nextInt(tilesList.size());
            Tile tile = tilesList.remove(idx);
            tile.putMine();
            getSurroundingTiles(tile).forEach(Tile::increaseMineCount);
        }
    }

    public HashSet<Tile> getSurroundingTiles(Tile tile) {
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

    public void decreaseTilesToDiscover() {
        tilesToDiscover--;
        if (tilesToDiscover == 0) state = FINISHED;
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

package app.model;

import app.MinesweeperConfig;
import static app.model.BoardState.*;

import java.util.HashSet;
import java.util.Random;


public class BoardModel {

    private final Field[][] fields;
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
        fields = new Field[numRows][numColumns];
        makeFields();
        placeMines(numMines);
    }

    private void makeFields() {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                fields[row][column] = new Field(row, column);
            }
        }
    }

    private void placeMines(int minesToPlace) {
        while (minesToPlace > 0) {
            int col = rnd.nextInt(numColumns);
            int row = rnd.nextInt(numRows);
            Field field = fields[row][col];
            if (!field.isMine()) {
                field.putMine();
                minesToPlace--;
                getSurroundingFields(field).forEach(Field::increaseMineCount);
            }
        }
    }

    public void repositionMine(Field field) {
        field.removeMine();
        getSurroundingFields(field).forEach(Field::decreaseMineCount);
        placeMines(1);
    }

    public HashSet<Field> getSurroundingFields(Field field) {
        int row = field.getRow();
        int column = field.getColumn();
        HashSet<Field> neighbours = new HashSet<>();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if ((i != row || j != column)
                        && i >= 0 && i < numRows
                        && j >= 0 && j < numColumns) {
                    neighbours.add(fields[i][j]);
                }
            }
        }
        return neighbours;
    }

    public void decreaseFieldsToDiscover() {
        fieldsToDiscover--;
        if (fieldsToDiscover == 0) state = FINISHED;
    }

    public Field[][] getFields() {
        return fields;
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

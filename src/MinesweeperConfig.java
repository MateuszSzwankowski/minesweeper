
enum MinesweeperConfig {

    BEGINNER (9, 9, 10),
    INTERMEDIATE (16,16,40),
    EXPERT (16, 30, 99),
    PRO (24, 40, 200),
    INSANE (50, 50, 625);

    private final int numRows;
    private final int numColumns;
    private final int numMines;

    MinesweeperConfig(int numRows, int numColumns, int numMines) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.numMines = numMines;
    }

    int getNumRows() {
        return numRows;
    }

    int getNumColumns() {
        return numColumns;
    }

    int getNumMines() {
        return numMines;
    }

}

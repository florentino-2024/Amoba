package Model;

import java.util.Arrays;
import java.util.Objects;

public final class Board {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    public Board(int rows, int cols) {
        if (rows < 5 || rows > 25 || cols < 5 || cols > 25 || cols > rows) {
            throw new IllegalArgumentException("Invalid board dimensions");
        }

        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Cell.EMPTY;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        validatePosition(row, col);
        return grid[row][col];
    }

    public Cell getCell(Position position) {
        return getCell(position.getRow(), position.getCol());
    }

    public void setCell(int row, int col, Cell cell) {
        validatePosition(row, col);
        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null");
        }
        grid[row][col] = cell;
    }

    public void setCell(Position position, Cell cell) {
        setCell(position.getRow(), position.getCol(), cell);
    }

    private void validatePosition(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException("Position out of bounds");
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isValidPosition(Position position) {
        return isValidPosition(position.getRow(), position.getCol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return rows == board.rows && cols == board.cols && Arrays.deepEquals(grid, board.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(grid);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int j = 0; j < cols; j++) {
            sb.append(" ").append((char) ('a' + j));
        }
        sb.append("\n");

        for (int i = 0; i < rows; i++) {
            sb.append(String.format("%2d ", i + 1));
            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j].getSymbol()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public Board copy() {
        Board copy = new Board(rows, cols);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, copy.grid[i], 0, cols);
        }
        return copy;
    }
}

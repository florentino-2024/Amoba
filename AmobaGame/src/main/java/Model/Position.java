package Model;

import java.util.Objects;

public final class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        char colChar = (char) ('a' + col);
        return "" + colChar + (row + 1);
    }

    public static Position fromString(String positionStr) {
        if (positionStr == null || positionStr.length() < 2) {
            throw new IllegalArgumentException("Invalid position format");
        }

        char colChar = Character.toLowerCase(positionStr.charAt(0));
        int col = colChar - 'a';
        int row = Integer.parseInt(positionStr.substring(1)) - 1;

        return new Position(row, col);
    }
}
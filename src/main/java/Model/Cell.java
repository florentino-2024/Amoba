package Model;

public enum Cell {
    EMPTY(" "),
    X("x"),
    O("o");

    private final String symbol;

    Cell(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Cell fromSymbol(String symbol) {
        for (Cell cell : values()) {
            if (cell.symbol.equals(symbol)) {
                return cell;
            }
        }
        throw new IllegalArgumentException("Unknown cell symbol: " + symbol);
    }
}
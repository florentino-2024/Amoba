package Model;

import java.util.Objects;

public final class Player {
    private final String name;
    private final Cell symbol;

    public Player(String name, Cell symbol) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (symbol == null || symbol == Cell.EMPTY) {
            throw new IllegalArgumentException("Invalid player symbol");
        }

        this.name = name.trim();
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public Cell getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && symbol == player.symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbol);
    }

    @Override
    public String toString() {
        return name + " (" + symbol.getSymbol() + ")";
    }
}
package Model;

import java.util.Objects;

public final class GameState {
    private final Board board;
    private final Player humanPlayer;
    private final Player aiPlayer;
    private final Player currentPlayer;
    private final boolean gameOver;
    private final Player winner;

    private GameState(Builder builder) {
        this.board = builder.board;
        this.humanPlayer = builder.humanPlayer;
        this.aiPlayer = builder.aiPlayer;
        this.currentPlayer = builder.currentPlayer;
        this.gameOver = builder.gameOver;
        this.winner = builder.winner;
    }


    public Board getBoard() { return board; }
    public Player getHumanPlayer() { return humanPlayer; }
    public Player getAiPlayer() { return aiPlayer; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return gameOver; }
    public Player getWinner() { return winner; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Board board;
        private Player humanPlayer;
        private Player aiPlayer;
        private Player currentPlayer;
        private boolean gameOver = false;
        private Player winner = null;

        public Builder board(Board board) {
            this.board = board;
            return this;
        }

        public Builder humanPlayer(Player humanPlayer) {
            this.humanPlayer = humanPlayer;
            return this;
        }

        public Builder aiPlayer(Player aiPlayer) {
            this.aiPlayer = aiPlayer;
            return this;
        }

        public Builder currentPlayer(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
            return this;
        }

        public Builder gameOver(boolean gameOver) {
            this.gameOver = gameOver;
            return this;
        }

        public Builder winner(Player winner) {
            this.winner = winner;
            return this;
        }

        public GameState build() {
            return new GameState(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return gameOver == gameState.gameOver &&
                Objects.equals(board, gameState.board) &&
                Objects.equals(humanPlayer, gameState.humanPlayer) &&
                Objects.equals(aiPlayer, gameState.aiPlayer) &&
                Objects.equals(currentPlayer, gameState.currentPlayer) &&
                Objects.equals(winner, gameState.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, humanPlayer, aiPlayer, currentPlayer, gameOver, winner);
    }
}

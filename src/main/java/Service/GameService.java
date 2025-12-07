package Service;

import Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final BoardService boardService;
    private final AIService aiService;
    private GameState currentState;

    public GameService(BoardService boardService, AIService aiService) {
        this.boardService = boardService;
        this.aiService = aiService;
    }

    public void initializeGame(String humanPlayerName, int rows, int cols) {
        logger.info("Initializing new game with {}x{} board", rows, cols);

        Board board = new Board(rows, cols);
        Player humanPlayer = new Player(humanPlayerName, Cell.X);
        Player aiPlayer = new Player("AI", Cell.O);

        this.currentState = GameState.builder()
                .board(board)
                .humanPlayer(humanPlayer)
                .aiPlayer(aiPlayer)
                .currentPlayer(humanPlayer) // Human starts
                .gameOver(false)
                .winner(null)
                .build();
    }

    public boolean makeHumanMove(Position position) {
        if (currentState.isGameOver()) {
            logger.warn("Attempted move after game over");
            return false;
        }

        if (currentState.getCurrentPlayer().getSymbol() != Cell.X) {
            logger.warn("Not human player's turn");
            return false;
        }

        if (!boardService.isValidMove(currentState.getBoard(), position)) {
            logger.warn("Invalid move attempted at {}", position);
            return false;
        }

        return applyMove(position, currentState.getHumanPlayer());
    }

    public Position makeAIMove() {
        if (currentState.isGameOver()) {
            logger.warn("Attempted AI move after game over");
            return null;
        }

        if (currentState.getCurrentPlayer().getSymbol() != Cell.O) {
            logger.warn("Not AI player's turn");
            return null;
        }

        Position aiMove = aiService.makeMove(currentState.getBoard());
        if (aiMove != null && applyMove(aiMove, currentState.getAiPlayer())) {
            return aiMove;
        }

        return null;
    }

    private boolean applyMove(Position position, Player player) {
        Board newBoard = currentState.getBoard().copy();
        newBoard.setCell(position, player.getSymbol());

        boolean isWinner = boardService.checkWinner(newBoard, player.getSymbol());
        boolean isBoardFull = boardService.isBoardFull(newBoard);
        boolean gameOver = isWinner || isBoardFull;

        Player nextPlayer = (player.getSymbol() == Cell.X) ? currentState.getAiPlayer() : currentState.getHumanPlayer();
        Player winner = isWinner ? player : null;

        this.currentState = GameState.builder()
                .board(newBoard)
                .humanPlayer(currentState.getHumanPlayer())
                .aiPlayer(currentState.getAiPlayer())
                .currentPlayer(nextPlayer)
                .gameOver(gameOver)
                .winner(winner)
                .build();

        logger.info("Move applied at {} by {}", position, player.getName());
        if (isWinner) {
            logger.info("Player {} won the game!", player.getName());
        } else if (isBoardFull) {
            logger.info("Game ended in a draw");
        }

        return true;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public boolean isGameOver() {
        return currentState.isGameOver();
    }

    public Player getWinner() {
        return currentState.getWinner();
    }

    public Player getCurrentPlayer() {
        return currentState.getCurrentPlayer();
    }
}

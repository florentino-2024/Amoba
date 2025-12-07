package Util;

import Model.Board;
import Model.Cell;
import Model.GameState;
import Model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

    public static GameState loadGame(String filename) throws IOException {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            logger.warn("Save file not found: {}", filename);
            return null;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            // Read board dimensions
            String[] dims = reader.readLine().split(" ");
            int rows = Integer.parseInt(dims[0]);
            int cols = Integer.parseInt(dims[1]);

            Board board = new Board(rows, cols);

            // Read board state
            for (int i = 0; i < rows; i++) {
                String line = reader.readLine();
                for (int j = 0; j < cols; j++) {
                    String symbol = line.substring(j * 2, j * 2 + 1);
                    board.setCell(i, j, Cell.fromSymbol(symbol));
                }
            }

            // Read game state
            String humanName = reader.readLine();
            String currentPlayerSymbol = reader.readLine();
            boolean gameOver = Boolean.parseBoolean(reader.readLine());
            String winnerName = reader.readLine();

            Player humanPlayer = new Player(humanName, Cell.X);
            Player aiPlayer = new Player("AI", Cell.O);
            Player currentPlayer = currentPlayerSymbol.equals("x") ? humanPlayer : aiPlayer;
            Player winner = winnerName.equals("null") ? null :
                    winnerName.equals(humanName) ? humanPlayer : aiPlayer;

            logger.info("Game loaded successfully from {}", filename);

            return GameState.builder()
                    .board(board)
                    .humanPlayer(humanPlayer)
                    .aiPlayer(aiPlayer)
                    .currentPlayer(currentPlayer)
                    .gameOver(gameOver)
                    .winner(winner)
                    .build();
        }
    }

    public static void saveGame(GameState gameState, String filename) throws IOException {
        Path path = Paths.get(filename);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            Board board = gameState.getBoard();

            // Write board dimensions
            writer.write(board.getRows() + " " + board.getCols());
            writer.newLine();

            // Write board state
            for (int i = 0; i < board.getRows(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < board.getCols(); j++) {
                    line.append(board.getCell(i, j).getSymbol()).append(" ");
                }
                writer.write(line.toString().trim());
                writer.newLine();
            }

            // Write game state
            writer.write(gameState.getHumanPlayer().getName());
            writer.newLine();
            writer.write(gameState.getCurrentPlayer().getSymbol().getSymbol());
            writer.newLine();
            writer.write(String.valueOf(gameState.isGameOver()));
            writer.newLine();
            writer.write(gameState.getWinner() != null ? gameState.getWinner().getName() : "null");
            writer.newLine();
        }

        logger.info("Game saved successfully to {}", filename);
    }

    public static boolean saveFileExists(String filename) {
        return Files.exists(Paths.get(filename));
    }
}
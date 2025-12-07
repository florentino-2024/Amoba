package com.example.helloworld;

import Model.GameState;
import Model.Position;
import Service.AIService;
import Service.BoardService;
import Service.GameService;
import Util.FileHandler;
import Util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class AmobaGame {
    private static final Logger logger = LoggerFactory.getLogger(AmobaGame.class);
    private static final String SAVE_FILE = "saved-games/current_game.txt";

    private final GameService gameService;
    private final Scanner scanner;

    public AmobaGame() {
        BoardService boardService = new BoardService();
        AIService aiService = new AIService(boardService);
        this.gameService = new GameService(boardService, aiService);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        logger.info("Starting Amőba Game");
        System.out.println("=== Amőba Játék ===");

        try {
            FileHandler FileHandler = new FileHandler();
            if (FileHandler.saveFileExists(SAVE_FILE)) {
                System.out.print("Szeretné betölteni a mentett játékot? (y/n): ");
                String loadChoice = scanner.nextLine().trim();

                if (loadChoice.equalsIgnoreCase("y")) {
                    GameState savedState = FileHandler.loadGame(SAVE_FILE);
                    if (savedState != null) {
                        System.out.println("A mentett játék betöltése a második fázisban lesz implementálva.");
                    }
                }
            }

            initializeNewGame();

            gameLoop();

        } catch (Exception e) {
            logger.error("Error during game execution", e);
            System.out.println("Hiba történt a játék során: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void initializeNewGame() {
        System.out.print("Adja meg a nevét: ");
        String playerName = scanner.nextLine().trim();

        int rows = 10;
        int cols = 10;

        gameService.initializeGame(playerName, rows, cols);

        System.out.println("Új játék inicializálva " + rows + "x" + cols + " pályán.");
        System.out.println("Az első lépésnek a tábla középső területén kell lennie.");
    }

    private void gameLoop() {
        while (!gameService.isGameOver()) {
            printGameState();

            if (gameService.getCurrentPlayer().getSymbol().getSymbol().equals("x")) {
                humanTurn();
            } else {
                aiTurn();
            }

            try {
                FileHandler.saveGame(gameService.getCurrentState(), SAVE_FILE);
            } catch (IOException e) {
                logger.warn("Failed to auto-save game", e);
            }
        }

        printGameResult();
    }

    private void humanTurn() {
        System.out.println("Ön következik (" + gameService.getCurrentPlayer().getSymbol().getSymbol() + ")");

        while (true) {
            System.out.print("Adja meg a lépést (pl. e5): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("quit")) {
                System.out.println("Játék megszakítva.");
                System.exit(0);
            }

            if (!ValidationUtil.isValidPositionString(input, 10, 10)) {
                System.out.println("Érvénytelen pozíció formátum. Használjon pl. 'e5' formátumot.");
                continue;
            }

            try {
                Position position = Position.fromString(input);
                if (gameService.makeHumanMove(position)) {
                    break;
                } else {
                    System.out.println("Érvénytelen lépés. Próbálja újra.");
                }
            } catch (Exception e) {
                System.out.println("Hiba: " + e.getMessage());
            }
        }
    }

    private void aiTurn() {
        System.out.println("AI gondolkozik...");

        try {
            Thread.sleep(1000); // Small delay for better UX
            Position aiMove = gameService.makeAIMove();

            if (aiMove != null) {
                System.out.println("AI lépett: " + aiMove);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void printGameState() {
        System.out.println("\n" + gameService.getCurrentState().getBoard());
        System.out.println("Játékos: " + gameService.getCurrentState().getHumanPlayer().getName() + " (x)");
        System.out.println("AI: " + gameService.getCurrentState().getAiPlayer().getName() + " (o)");
        System.out.println("Következő: " + gameService.getCurrentPlayer().getName());
    }

    private void printGameResult() {
        printGameState();

        if (gameService.getWinner() != null) {
            System.out.println("🎉 " + gameService.getWinner().getName() + " nyert! 🎉");
        } else {
            System.out.println("Döntetlen!");
        }
    }

    public static void main(String[] args) {
        new AmobaGame().start();
    }
}
package Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Model.Board;
import Model.Cell;
import Model.Position;

public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
    private static final int WINNING_COUNT = 4;

    public boolean isValidMove(Board board, Position position) {
        if (!board.isValidPosition(position)) {
            logger.debug("Position {} is out of bounds", position);
            return false;
        }

        if (board.getCell(position) != Cell.EMPTY) {
            logger.debug("Position {} is already occupied", position);
            return false;
        }

        if (!touchesExistingSymbols(board, position)) {
            logger.debug("Position {} does not touch any existing symbols diagonally", position);
            return false;
        }

        return true;
    }

    private boolean touchesExistingSymbols(Board board, Position position) {
        if (isBoardEmpty(board)) {
            return isInCenterArea(board, position);
        }

        int[] rowDirs = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirs = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < rowDirs.length; i++) {
            int newRow = position.getRow() + rowDirs[i];
            int newCol = position.getCol() + colDirs[i];

            if (board.isValidPosition(newRow, newCol) &&
                    board.getCell(newRow, newCol) != Cell.EMPTY) {
                return true;
            }
        }

        return false;
    }

    private boolean isBoardEmpty(Board board) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (board.getCell(i, j) != Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isInCenterArea(Board board, Position position) {
        int centerRow = board.getRows() / 2;
        int centerCol = board.getCols() / 2;
        int row = position.getRow();
        int col = position.getCol();

        return Math.abs(row - centerRow) <= 1 && Math.abs(col - centerCol) <= 1;
    }

    public List<Position> getValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                Position position = new Position(i, j);
                if (isValidMove(board, position)) {
                    validMoves.add(position);
                }
            }
        }

        logger.debug("Found {} valid moves", validMoves.size());
        return validMoves;
    }

    public boolean checkWinner(Board board, Cell symbol) {
        return checkHorizontal(board, symbol) ||
                checkVertical(board, symbol) ||
                checkDiagonal(board, symbol) ||
                checkAntiDiagonal(board, symbol);
    }

    private boolean checkHorizontal(Board board, Cell symbol) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j <= board.getCols() - WINNING_COUNT; j++) {
                boolean win = true;
                for (int k = 0; k < WINNING_COUNT; k++) {
                    if (board.getCell(i, j + k) != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) return true;
            }
        }
        return false;
    }

    private boolean checkVertical(Board board, Cell symbol) {
        for (int j = 0; j < board.getCols(); j++) {
            for (int i = 0; i <= board.getRows() - WINNING_COUNT; i++) {
                boolean win = true;
                for (int k = 0; k < WINNING_COUNT; k++) {
                    if (board.getCell(i + k, j) != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) return true;
            }
        }
        return false;
    }

    private boolean checkDiagonal(Board board, Cell symbol) {
        for (int i = 0; i <= board.getRows() - WINNING_COUNT; i++) {
            for (int j = 0; j <= board.getCols() - WINNING_COUNT; j++) {
                boolean win = true;
                for (int k = 0; k < WINNING_COUNT; k++) {
                    if (board.getCell(i + k, j + k) != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) return true;
            }
        }
        return false;
    }

    private boolean checkAntiDiagonal(Board board, Cell symbol) {
        for (int i = 0; i <= board.getRows() - WINNING_COUNT; i++) {
            for (int j = WINNING_COUNT - 1; j < board.getCols(); j++) {
                boolean win = true;
                for (int k = 0; k < WINNING_COUNT; k++) {
                    if (board.getCell(i + k, j - k) != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) return true;
            }
        }
        return false;
    }

    public boolean isBoardFull(Board board) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (board.getCell(i, j) == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}

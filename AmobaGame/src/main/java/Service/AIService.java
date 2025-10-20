package Service;

import Model.Board;
import Model.Position;

import java.util.List;
import java.util.Random;

public class AIService {
    private final BoardService boardService;
    private final Random random;

    public AIService(BoardService boardService) {
        this.boardService = boardService;
        this.random = new Random();
    }

    public Position makeMove(Board board) {
        List<Position> validMoves = boardService.getValidMoves(board);

        if (validMoves.isEmpty()) {
            throw new IllegalStateException("No valid moves available for AI");
        }
        return validMoves.get(random.nextInt(validMoves.size()));
    }
}
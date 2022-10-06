package assignment;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TetrisBoardTest {
    @Test
    public void checkConstructor() {
        TetrisBoard board = new TetrisBoard(10, 20);
        assert board.getWidth() == 10;
        assert board.getHeight() == 20;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                assert board.getGrid(j,i) == null;
            }
        }
    }

    @Test
    public void checkEmptyBoard() {
        TetrisBoard board = new TetrisBoard(10, 20);
        assert board.move(Board.Action.DROP)== Board.Result.NO_PIECE;
    }

    @Test
    public void checkPiece() {
        TetrisBoard board = new TetrisBoard(10, 20);
        board.nextPiece(new TetrisPiece(Piece.PieceType.STICK), new Point(4, 17));
        assert (board.move(Board.Action.DOWN) == Board.Result.SUCCESS || board.move(Board.Action.DOWN) == Board.Result.PLACE);
        assert (board.move(Board.Action.LEFT) == Board.Result.SUCCESS || board.move(Board.Action.LEFT) == Board.Result.OUT_BOUNDS);
        assert (board.move(Board.Action.RIGHT) == Board.Result.SUCCESS || board.move(Board.Action.RIGHT) == Board.Result.OUT_BOUNDS);
        assert (board.move(Board.Action.CLOCKWISE) == Board.Result.SUCCESS || board.move(Board.Action.CLOCKWISE) == Board.Result.OUT_BOUNDS);
    }
}
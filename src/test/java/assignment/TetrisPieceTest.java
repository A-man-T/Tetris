package assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TetrisPieceTest {
    @Test
    public void checkConstructor() {
        for (Piece.PieceType i : Piece.PieceType.values()) {
            TetrisPiece piece = new TetrisPiece(i);
            if (i.equals(Piece.PieceType.STICK)) {
                assert piece.getWidth() == 4;
                assert piece.getHeight() == 4;
            }
            else if (i.equals(Piece.PieceType.SQUARE)) {
                assert piece.getWidth() == 2;
                assert piece.getHeight() == 2;
            }
            else {
                assert piece.getHeight() == 3;
                assert piece.getWidth() == 3;
            }
        }
    }

    @Test
    public void checkRotations() {
        TetrisPiece piece = new TetrisPiece(Piece.PieceType.STICK);
        for (int i = 0; i < 4; i++) {
            int currentIndex = piece.getRotationIndex();
            piece = (TetrisPiece) piece.clockwisePiece();
            assert piece.getRotationIndex() == (currentIndex+1)%4;
        }
        for (int i = 0; i < 4; i++) {
            int currentIndex = piece.getRotationIndex();
            piece = (TetrisPiece) piece.counterclockwisePiece();
            assert piece.getRotationIndex() == (currentIndex+3)%4;
        }
    }

}
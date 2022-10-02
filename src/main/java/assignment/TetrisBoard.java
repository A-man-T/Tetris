package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2D grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2D board.
 */
public final class TetrisBoard implements Board {

    private Piece cp;
    Point pos;
    Piece.PieceType grid[][];

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {grid = new Piece.PieceType[height][width];}

    @Override
    public Result move(Action act) {
        if (act == Action.DOWN) {

        }
        return Result.NO_PIECE;
    }

    @Override
    public Board testMove(Action act) { return null; }

    @Override
    public Piece getCurrentPiece() { return cp; }

    @Override
    public Point getCurrentPiecePosition() { return pos; }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        this.cp = p;
        this.pos = spawnPosition;
    }

    @Override
    public boolean equals(Object other) { return false; }

    @Override
    public Result getLastResult() { return Result.NO_PIECE; }

    @Override
    public Action getLastAction() { return Action.NOTHING; }

    @Override
    public int getRowsCleared() { return -1; }

    @Override
    public int getWidth() { return grid[0].length;}

    @Override
    public int getHeight() { return grid.length; }

    @Override
    public int getMaxHeight() { return -1; }

    @Override
    public int dropHeight(Piece piece, int x) {
        return -1;
    }

    @Override
    public int getColumnHeight(int x) {
        int count = 0;
        int i = grid.length-1;
        while (grid[i][x] == null) {
            count++;
        }
        return count;
    }

    @Override
    public int getRowWidth(int y) {
        int count = 0;
        for (Piece.PieceType i :grid[grid.length-y]) {
            if (i == (null)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        return grid[grid.length-y][x];
    }
}

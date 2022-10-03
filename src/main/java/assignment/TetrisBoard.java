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
        Result result = Result.NO_PIECE;
        switch (act) {
            case CLOCKWISE -> {
                cp = cp.clockwisePiece();
                result = Result.SUCCESS;
            }
            case COUNTERCLOCKWISE -> {
                cp = cp.counterclockwisePiece();
                result = Result.SUCCESS;
            }
            case LEFT -> {
                if (pos.x == 0) {
                    result = Result.OUT_BOUNDS;
                }
                else {
                    pos = new Point(pos.x - 1, pos.y);
                    result = Result.SUCCESS;
                }
            }
            case RIGHT -> {
                if (pos.x+cp.getWidth() == getWidth()) {
                    result = Result.OUT_BOUNDS;
                }
                else {
                    pos = new Point(pos.x+1, pos.y);
                    result = Result.SUCCESS;
                }
            }
            case DOWN -> {
                if (Helper.howMuchLower(this, cp.getSkirt(), pos)) {
                    this.grid = (Helper.place(grid, cp.getBody(), pos, cp.getType(), this)).clone();
                    result = Result.PLACE;
                    break;
                }
                pos = new Point(pos.x, pos.y-1);
                result = Result.SUCCESS;
            }
            case NOTHING -> {
            }
        }

        return result;
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
    public int getMaxHeight() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < getWidth(); i++) {
            max = Math.max(max, getColumnHeight(i));
        }
        return max;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        return -1;
    }

    @Override
    public int getColumnHeight(int x) {
        int count = 0;
        for (int i = getHeight()-1; i >= 0; i--) {
            if (grid[i][x] != null) {
                break;
            }
            count++;
        }
        return getHeight()-count;
    }

    @Override
    public int getRowWidth(int y) {
        int count = 0;
        for (Piece.PieceType i :grid[y]) {
            if (i != (null)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        return grid[y][x];
    }
}

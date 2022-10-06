package assignment;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a Tetris board -- essentially a 2D grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2D board.
 */
public final class TetrisBoard implements Board {

    private Piece cp;
    private int numCleared;
    Point pos;
    Piece.PieceType grid[][];

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {grid = new Piece.PieceType[height][width];numCleared = 0;}

    @Override
    public Result move(Action act) {
        Result result = Result.NO_PIECE;
        switch (act) {
            case DROP -> {
                int drop = Integer.MAX_VALUE;
                for (int i = 0; i < cp.getSkirt().length; i++) {
                    if (cp.getSkirt()[i] == Integer.MAX_VALUE || cp.getSkirt()[i] == Integer.MIN_VALUE) {
                        continue;
                    }
                    int j = pos.y+cp.getSkirt()[i]-1;
                    int count = 0;
                    while (j >=0 && getGrid(pos.x+i, j)== null) {
                        count++;
                        j--;
                        System.out.println(count +" " +j);
                    }
                    drop = Math.min(drop, count);
                    System.out.println(drop);
                }
                this.grid = (place(grid, cp.getBody(), new Point(pos.x, pos.y-drop), cp.getType(), this)).clone();
                result = Result.PLACE;
            }
            case CLOCKWISE -> {
                Piece temp = cp.clockwisePiece();
                Point[] translation;
                Point fake;
                if (temp.getType() == Piece.PieceType.STICK) {
                    translation = Piece.I_CLOCKWISE_WALL_KICKS[cp.getRotationIndex()];
                }
                else {
                    translation = Piece.NORMAL_CLOCKWISE_WALL_KICKS[cp.getRotationIndex()];
                }
                for (Point p : translation) {
                    boolean isInBound = true;
                    for (Point b : temp.getBody()) {
                        fake = new Point(p.x+b.x + pos.x, p.y+b.y + pos.y);
                        if (fake.x >= getWidth() || fake.x < 0 || fake.y < 0 || getGrid(fake.x, fake.y)!= null) {
                            isInBound = false;
                            break;
                        }
                    }
                    if (isInBound) {
                        cp = temp;
                        pos = new Point(pos.x+p.x, pos.y+p.y);
                        result = Result.SUCCESS;
                        break;
                    }
                }
                if (result == Result.NO_PIECE) {
                    result = Result.OUT_BOUNDS;
                }
            }
            case COUNTERCLOCKWISE -> {
                Piece temp = cp.counterclockwisePiece();
                Point[] translation;
                Point fake;
                if (temp.getType() == Piece.PieceType.STICK) {
                    translation = Piece.I_COUNTERCLOCKWISE_WALL_KICKS[cp.getRotationIndex()];
                }
                else {
                    translation = Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[cp.getRotationIndex()];
                }
                for (Point p : translation) {
                    boolean isInBound = true;
                    for (Point b : temp.getBody()) {
                        fake = new Point(p.x+b.x + pos.x, p.y+b.y + pos.y);
                        if (fake.x >= getWidth() || fake.x < 0 || fake.y < 0 || getGrid(fake.x, fake.y)!= null) {
                            isInBound = false;
                            break;
                        }
                    }
                    if (isInBound) {
                        cp = temp;
                        pos = new Point(pos.x+p.x, pos.y+p.y);
                        result = Result.SUCCESS;
                        break;
                    }
                }
                if (result == Result.NO_PIECE) {
                    result = Result.OUT_BOUNDS;
                }
            }
            case LEFT -> {
                if (checkLeft()) {
                    result = Result.OUT_BOUNDS;
                }
                else {
                    pos = new Point(pos.x - 1, pos.y);
                    result = Result.SUCCESS;
                }
            }
            case RIGHT -> {
                if (checkRight()) {
                    result = Result.OUT_BOUNDS;
                }
                else {
                    pos = new Point(pos.x+1, pos.y);
                    result = Result.SUCCESS;
                }
            }
            case DOWN -> {
                if (howMuchLower(this, cp.getSkirt(), pos)) {
                    this.grid = (place(grid, cp.getBody(), pos, cp.getType(), this)).clone();
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
    public int getRowsCleared() { return numCleared; }

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

    private Piece.PieceType[][] place(Piece.PieceType[][] grid, Point[] body, Point pos, Piece.PieceType type, TetrisBoard board) {
        for (Point p : body) {
            grid[pos.y+p.y][pos.x+p.x] = type;
        }
        for (int i = 0; i < grid.length; i++) {
            if (board.getRowWidth(i) == grid[i].length) {
                Arrays.fill(grid[i], null);
                int k = i;
                while (board.getRowWidth(k+1) != 0) {
                    grid[k] = grid[k+1].clone();
                    k++;
                }
                numCleared++;
            }
        }
        for (Piece.PieceType[] i : grid) {
            for (Piece.PieceType t : i) {
                System.out.print(t + " ");
            }
            System.out.println();
        }
        System.out.println();
        return grid;
    }

    private boolean howMuchLower(TetrisBoard board, int[] skirt, Point pos) {

        for (int i = 0; i < skirt.length; i++) {
            if (skirt[i] == Integer.MAX_VALUE) continue;
            if (pos.y+skirt[i] == 0) {
                return true;
            }

            else if (board.getGrid(pos.x + i, pos.y+skirt[i]-1) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLeft() {
        int[] leftskirt = leftSkirt();
        for (int i = 0; i < leftskirt.length; i++) {
            if (leftskirt[i] == Integer.MAX_VALUE) continue;
            if (pos.x+leftskirt[i] == 0) {
                return true;
            }

            else if (getGrid(pos.x + leftskirt[i]-1, pos.y+i) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRight() {
        int[] rightskirt = rightSkirt();
        System.out.println(Arrays.toString(rightskirt));
        for (int i = 0; i < rightskirt.length; i++) {
            if (rightskirt[i] == Integer.MIN_VALUE) continue;
            if (pos.x+cp.getWidth()-rightskirt[i]+1 > this.grid[0].length) {
                return true;
            }

            else if (getGrid(pos.x + cp.getWidth() - rightskirt[i], pos.y+i) != null) {
                return true;
            }
        }
        return false;
    }

    private int[] leftSkirt() {
        int[] leftskirt = new int[cp.getHeight()];
        Arrays.fill(leftskirt, Integer.MAX_VALUE);
        for (Point p : cp.getBody()) {
            leftskirt[p.y] = Math.min(p.x, leftskirt[p.y]);
        }
        return leftskirt;
    }

    private int[] rightSkirt() {
        int[] rightskirt = new int[cp.getHeight()];
        Arrays.fill(rightskirt, Integer.MIN_VALUE);
        for (Point p : cp.getBody()) {
            System.out.println(p.x + " " + p.y);
            rightskirt[p.y] = Math.max(p.x, rightskirt[p.y]);
        }
        for (int i = 0; i < rightskirt.length; i++) {
            if (rightskirt[i] == Integer.MIN_VALUE) continue;
            rightskirt[i] = cp.getWidth()-rightskirt[i]-1;
        }
        return rightskirt;
    }
}

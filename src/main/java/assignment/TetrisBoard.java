package assignment;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a Tetris board -- essentially a 2D grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2D board.
 */
public final class TetrisBoard implements Board {

    // private variables
    private Piece cp;
    private int numCleared;
    private Point pos;
    private Piece.PieceType grid[][];
    private Result lastResult;
    private Action lastAction;
    private int[] columnHeights;
    private int[] rowWidths;
    private int maxHeight;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        if (width <= 0 || height <= 0) {
            System.err.print("Invalid grid dimensions.");
            throw new IllegalArgumentException();
        }
        grid = new Piece.PieceType[height][width];
        numCleared = 0;
        columnHeights = new int[width];
        rowWidths = new int[height];
        maxHeight = Integer.MIN_VALUE;
    }

    private TetrisBoard(Piece.PieceType[][] grid, int numCleared, Piece cp, Point pos, Result lastResult, Action lastAction, int[] columnHeights, int[] rowWidths, int maxHeight) {
        this.cp = cp;
        this.grid = grid;
        this.pos = pos;
        this.numCleared = numCleared;
        this.lastResult = lastResult;;
        this.lastAction = lastAction;
        this.maxHeight = maxHeight;
        this.columnHeights = columnHeights;
        this.rowWidths = rowWidths;
    }

    @Override
    public Result move(Action act) {
        lastAction = act;
        Result result = Result.NO_PIECE;
        if (cp == null) {
            this.lastResult = result;
            return result;
        }
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
                    }
                    drop = Math.min(drop, count);
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
                        if (fake.x >= getWidth() || fake.x < 0 || fake.y < 0 || fake.y >= grid.length || getGrid(fake.x, fake.y)!= null) {
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
        for (int k = 0; k < columnHeights.length; k++) {
            int count = 0;
            for (int i = getHeight() - 1; i >= 0; i--) {
                if (grid[i][k] != null) {
                    break;
                }
                count++;
            }
            columnHeights[k] = count;
        }

        for (int i : columnHeights) {
            maxHeight = Math.max(maxHeight, i);
        }

        for (int k = 0; k < rowWidths.length; k++) {
            int count = 0;
            for (Piece.PieceType i :grid[k]) {
                if (i != (null)) {
                    count++;
                }
            }
            rowWidths[k] = count;
        }
        lastResult = result;
        return result;
    }

    @Override
    public Board testMove(Action act) {


        Piece.PieceType  newgrid[][] = new Piece.PieceType[grid.length][grid[0].length];
        for(int y =0;y<grid.length;y++)
            for(int x =0;x<grid[0].length;x++)
                newgrid[y][x] = grid[y][x];

        Point pos1;
        pos1 = new Point((int)pos.getX(), (int)pos.getY());
        Piece cp1 = cp;
        Result r1 = lastResult;
        Action a1 = lastAction;
        int[] cols = Arrays.copyOf(columnHeights, columnHeights.length);
        int[] rows = Arrays.copyOf(rowWidths, rowWidths.length);
        int max = maxHeight;

        TetrisBoard board = new TetrisBoard(newgrid, this.numCleared, cp1, pos1, r1,a1, cols, rows, max);
        board.move(act);
        return board;
    }

    @Override
    public Piece getCurrentPiece() { return cp; }

    @Override
    public Point getCurrentPiecePosition() { return pos; }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        if (spawnPosition.x < 0 || spawnPosition.x > grid[0].length-p.getWidth() || spawnPosition.y < 0 || spawnPosition.y > grid.length - p.getHeight() || p == null) {
            System.err.print("Next piece spawns out of grid.");
            throw new IllegalArgumentException();
        }
        this.cp = p;
        this.pos = spawnPosition;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof TetrisBoard)) return false;
        TetrisBoard otherBoard = (TetrisBoard) other;

        boolean valid = true;
        if (!this.getCurrentPiece().equals(otherBoard.getCurrentPiece())) valid = false;
        if (this.getHeight() != otherBoard.getHeight()) valid = false;
        if (this.getWidth() != otherBoard.getWidth()) valid = false;
        if (!this.getCurrentPiecePosition().equals(otherBoard.getCurrentPiecePosition())) valid = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (this.getGrid(j, i) != otherBoard.getGrid(j, i)) valid = false;
            }
        }

        return valid;
    }

    @Override
    public Result getLastResult() { return this.lastResult; }

    @Override
    public Action getLastAction() { return this.lastAction; }

    @Override
    public int getRowsCleared() { return numCleared; }

    @Override
    public int getWidth() { return grid[0].length;}

    @Override
    public int getHeight() { return grid.length; }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        int drop = Integer.MAX_VALUE;
        for (int i = 0; i < cp.getSkirt().length; i++) {
            if (cp.getSkirt()[i] == Integer.MAX_VALUE || cp.getSkirt()[i] == Integer.MIN_VALUE) {
                continue;
            }
            int j = pos.y+cp.getSkirt()[i]-1;
            int count = 0;
            while (j >=0 && getGrid(x+i, j)== null) {
                count++;
                j--;
            }
            drop = Math.min(drop, count);
        }
        return drop;
    }

    @Override
    public int getColumnHeight(int x) {
        return columnHeights[x];
    }

    @Override
    public int getRowWidth(int y) {
        return rowWidths[y];
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        return grid[y][x];
    }

    private Piece.PieceType[][] place(Piece.PieceType[][] grid, Point[] body, Point pos, Piece.PieceType type, TetrisBoard board) {
        for (Point p : body) {
            grid[pos.y+p.y][pos.x+p.x] = type;
        }
        int i = 0;
        int m = grid.length;
        while (i < m) {
            if (board.getRowWidth(i) == 0) {
                break;
            }
            if (board.getRowWidth(i) == grid[i].length) {
                Arrays.fill(grid[i], null);
                int k = i;
                while (board.getRowWidth(k+1) != 0) {
                    grid[k] = Arrays.copyOf(grid[k+1], grid[k+1].length);
                    k++;
                }
                grid[k] = Arrays.copyOf(grid[k+1], grid[k+1].length);
                numCleared++;
                i--;
                m--;
            }
            i++;
        }

/*
        for (Piece.PieceType[] p : grid) {
            for (Piece.PieceType t : p) {
                System.out.print(t + " ");
            }
            System.out.println();
        }
        System.out.println();

 */


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
            //System.out.println(p.x + " " + p.y);
            rightskirt[p.y] = Math.max(p.x, rightskirt[p.y]);
        }
        for (int i = 0; i < rightskirt.length; i++) {
            if (rightskirt[i] == Integer.MIN_VALUE) continue;
            rightskirt[i] = cp.getWidth()-rightskirt[i]-1;
        }
        return rightskirt;
    }

    private TetrisBoard copy() {
        return new TetrisBoard(grid, numCleared, cp, pos, lastResult,lastAction, columnHeights, rowWidths, maxHeight);
    }
}

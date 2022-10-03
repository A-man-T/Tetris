package assignment;

import java.awt.*;
import java.util.Arrays;

public class Helper {
    static int[][] rotateClockwise(int[][] matrix) {
        int rowNum = matrix.length;
        int colNum = matrix[0].length;
        int[][] temp = new int[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                temp[i][j] = matrix[rowNum - j - 1][i];
            }
        }
        return temp;
    }

    public static Piece.PieceType[][] place(Piece.PieceType[][] grid, Point[] body, Point pos, Piece.PieceType type, TetrisBoard board) {
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

    public static boolean howMuchLower(TetrisBoard board, int[] skirt, Point pos) {
        for (int i = 0; i < skirt.length; i++) {
            if (pos.y+skirt[i] == 0) {
                return true;
            }
            if (board.getGrid(pos.x + i, pos.y+skirt[i]-1) != null) {
                return true;
            }
        }
        return false;
    }
}

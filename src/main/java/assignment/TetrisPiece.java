package assignment;

import java.awt.*;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 *
 * All operations on a TetrisPiece should be constant time, except for its
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do pre-computation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {
    private static final TetrisPiece[] T, SQUARE, STICK, LEFT_L, RIGHT_L, LEFT_DOG, RIGHT_DOG;
    static {
        T = new TetrisPiece[4];
        SQUARE = new TetrisPiece[1];
        STICK = new TetrisPiece[4];
        LEFT_L = new TetrisPiece[4];
        RIGHT_L = new TetrisPiece[4];
        LEFT_DOG = new TetrisPiece[4];
        RIGHT_DOG = new TetrisPiece[4];

        SQUARE[0] = new TetrisPiece(PieceType.SQUARE, 0);

        for (int i = 0; i < 4; i++) {
            T[i] = new TetrisPiece(PieceType.T, i);
            STICK[i] = new TetrisPiece(PieceType.STICK, i);
            LEFT_L[i] = new TetrisPiece(PieceType.LEFT_L, i);
            RIGHT_L[i] = new TetrisPiece(PieceType.RIGHT_L, i);
            LEFT_DOG[i] = new TetrisPiece(PieceType.LEFT_DOG, i);
            RIGHT_DOG[i] = new TetrisPiece(PieceType.RIGHT_DOG, i);
        }
    }
    private TetrisPiece[] rots;
    private final int right, left, width, height, rotationIndex;
    private final Point[] body;
    private final PieceType type;
    private final int[] skirt;
    /**
     * Construct a tetris piece of the given type. The piece should be in its spawn orientation,
     * i.e., a rotation index of 0.
     *
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) { this(type, 0);
    }
    private TetrisPiece(PieceType type, int r) {
        this.type = type;
        rotationIndex = r;
        height = type.getBoundingBox().height;
        width = type.getBoundingBox().width;

        if (rotationIndex == 0) {
            body = type.getSpawnBody();
        }
        else {
            Point[] spawnBody = type.getSpawnBody();
            body = new Point[4];
            if (rotationIndex == 1) {
                for (int i = 0; i < spawnBody.length; i++) {
                    body[i] = new Point(spawnBody[i].y, (-1*spawnBody[i].x)+width-1);
                }
            }
            else if (rotationIndex == 2) {
                for (int i = 0; i < spawnBody.length; i++) {
                    body[i] = new Point((-1*spawnBody[i].x)+width-1, (-1*spawnBody[i].y)+height-1);
                }
            }
            else {
                for (int i = 0; i < spawnBody.length; i++) {
                    body[i] = new Point((-1*spawnBody[i].y)+height-1, spawnBody[i].x);
                }
            }
        }
        if (this.type.equals(PieceType.SQUARE)) {
            left = 0;
            right = 0;
        }
        else {
            right = (rotationIndex + 1) % 4;
            left = (rotationIndex + 3) % 4;
        }

        if (type.equals(PieceType.T)) {
            rots = T;
        }
        else if (type.equals(PieceType.SQUARE)) {
            rots = SQUARE;
        }
        else if (type.equals(PieceType.STICK)) {
            rots = STICK;
        }
        else if (type.equals(PieceType.LEFT_DOG)) {
            rots = LEFT_DOG;
        }
        else if (type.equals(PieceType.RIGHT_DOG)) {
            rots = RIGHT_DOG;
        }
        else if (type.equals(PieceType.LEFT_L)) {
            rots = LEFT_L;
        }
        else if (type.equals(PieceType.RIGHT_L)) {
            rots = RIGHT_L;
        }

        skirt = new int[width];
        Arrays.fill(skirt, Integer.MAX_VALUE);
        for (Point p : body) {
            skirt[p.x] = Math.min(p.y, skirt[p.x]);
        }
    }

    @Override
    public PieceType getType() {
        return this.type;
    }

    @Override
    public int getRotationIndex() {
        return rotationIndex;
    }

    @Override
    public Piece clockwisePiece() {
        return rots[right];
    }

    @Override
    public Piece counterclockwisePiece() {
        return rots[left];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Point[] getBody() {
        return body;
    }

    @Override
    public int[] getSkirt() {
        return skirt;
    }

    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        // TODO: Implement me.
        return false;
    }
}

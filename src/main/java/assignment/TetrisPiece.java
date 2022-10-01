package assignment;

import java.awt.*;
import java.util.LinkedList;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for its
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do pre-computation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {
    private int[][] box;
    private Point[] body;
    /**
     * Construct a tetris piece of the given type. The piece should be in its spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) {
        box = new int[(int)type.getBoundingBox().getWidth()][(int)type.getBoundingBox().getHeight()];
        body = type.getSpawnBody();
    }

    @Override
    public PieceType getType() {
        return null;
    }

    @Override
    public int getRotationIndex() {
        // TODO: Implement me.
        return -1;
    }

    @Override
    public Piece clockwisePiece() {
        // TODO: Implement me.
        return null;
    }

    @Override
    public Piece counterclockwisePiece() {
        // TODO: Implement me.
        return null;
    }

    @Override
    public int getWidth() {
        return box[0].length;
    }

    @Override
    public int getHeight() {
        return box.length;
    }

    @Override
    public Point[] getBody() {
        return body;
    }

    @Override
    public int[] getSkirt() {
        // TODO: Implement me.
        return null;
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

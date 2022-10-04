package assignment;

import java.awt.*;
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
    private int[][][] box;
    private Point[] body;
    private PieceType type;
    private int rotationIndex = 0;
    private ArrayList<Point[]> bodies = new ArrayList<>();
    private int size = 0;
    private int[] skirt;
    /**
     * Construct a tetris piece of the given type. The piece should be in its spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) {
        box = new int[4][(int)type.getBoundingBox().getWidth()][(int)type.getBoundingBox().getHeight()];
        body = type.getSpawnBody();
        skirt = new int[box[0][0].length];

        for (Point point : body) {
            box[0][box[0].length-point.y-1][point.x] = 1;
            size++;
        }
        box[1] = Helper.rotateClockwise(box[0]);
        box[2] = Helper.rotateClockwise(box[1]);
        box[3] = Helper.rotateClockwise(box[2]);

        for (int i = 0; i < box.length; i++) {
            Point[] temp = new Point[size];
            int m = 0;
            for (int j = 0; j < box[i].length; j++) {
                for (int k = 0; k < box[i][j].length; k++) {
                    if (box[i][j][k] == 1) {
                        temp[m] = new Point(box[i].length-j-1, k);
                        m++;
                    }
                }
            }
            bodies.add(temp);
        }


        for (int i = 0; i < skirt.length; i++) {
            int height = 0;
            while (height < skirt.length) {
                if (box[0][box[0].length-height-1][i] == 1) {
                    break;
                }
                height++;
            }
            skirt[i] = height;
        }
        for (int i: skirt) {
            if (i == skirt.length) {
                i = Integer.MAX_VALUE;
            }
        }

        this.type = type;
    }

    public TetrisPiece(ArrayList<Point[]> bodies, PieceType type, int rotationIndex, int[][][] box, int[] skirt) {
        this.box = box;
        this.rotationIndex = rotationIndex;
        this.type = type;
        this.bodies = bodies;
        this.skirt = skirt;
        this.body = bodies.get(rotationIndex);
        for (int i = 0; i < skirt.length; i++) {
            int height = 0;
            while (height < skirt.length) {
                if (box[this.rotationIndex][box[this.rotationIndex].length-height-1][i] == 1) {
                    break;
                }
                height++;
            }
            skirt[i] = height;
        }
        for (int i: skirt) {
            if (i == skirt.length) {
                i = Integer.MAX_VALUE;
            }
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
        TetrisPiece rotated = null;
        rotationIndex++;
        rotationIndex = rotationIndex%4;
        if (rotationIndex == 0) {
            rotated = new TetrisPiece(bodies, type, rotationIndex, box, skirt);
            System.out.println(Arrays.toString(bodies.get(0)));
        }
        else if (rotationIndex == 1) {
            rotated = new TetrisPiece(bodies, type, rotationIndex, box, skirt);
            System.out.println(Arrays.toString(bodies.get(1)));
        }
        else if (rotationIndex == 2) {
            rotated = new TetrisPiece(bodies, type, rotationIndex, box, skirt);
            System.out.println(Arrays.toString(bodies.get(2)));
        }
        else if (rotationIndex == 3) {
            rotated = new TetrisPiece(bodies, type, rotationIndex, box, skirt);
            System.out.println(Arrays.toString(bodies.get(3)));
        }
        return rotated;
    }

    @Override
    public Piece counterclockwisePiece() {
        TetrisPiece rotated = new TetrisPiece(type);
        rotationIndex--;
        if (rotationIndex < 0) {
            rotationIndex = 3;
        }
        if (rotationIndex == 0) {
            rotated.body = bodies.get(0);
        }
        else if (rotationIndex == 1) {
            rotated.body = bodies.get(1);
        }
        else if (rotationIndex == 2) {
            rotated.body = bodies.get(2);
        }
        else if (rotationIndex == 3) {
            rotated.body = bodies.get(3);
        }
        return rotated;
    }

    @Override
    public int getWidth() {
        return box[0][0].length;
    }

    @Override
    public int getHeight() {
        return box[0].length;
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

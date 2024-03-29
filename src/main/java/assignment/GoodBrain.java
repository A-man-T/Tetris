package assignment;

import java.util.*;

/**
 * A Lame Brain implementation for JTetris; tries all possible places to put the
 * piece (but ignoring rotations, because we're lame), trying to minimize the
 * total height of pieces on the board.
 */
public class GoodBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);


        int best = Integer.MIN_VALUE;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            int score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        //System.out.println("Highest Score: " +best + "index: "+bestIndex);

        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {


        // We can always drop our current Piece
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);

        // Now we'll add all the places to the left we can DROP
        Board left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.LEFT);
            left.move(Board.Action.LEFT);
        }


        // And then the same thing to the right
        Board right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.RIGHT);
            right.move(Board.Action.RIGHT);
        }

        currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);

        left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
            left.move(Board.Action.LEFT);
        }


        // And then the same thing to the right
        right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
            right.move(Board.Action.RIGHT);
        }

        currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);

        left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
            left.move(Board.Action.LEFT);
        }


        // And then the same thing to the right
        right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
            right.move(Board.Action.RIGHT);
        }

        currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);

        left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.COUNTERCLOCKWISE);
            left.move(Board.Action.LEFT);
        }


        // And then the same thing to the right
        right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.COUNTERCLOCKWISE);
            right.move(Board.Action.RIGHT);
        }
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private int aggregateHeight(Board newBoard) {
        int total = 0;
        for (int x = 0; x < newBoard.getWidth(); x++)
            total += newBoard.getColumnHeight(x);
        return total;
    }


    private int bumpiness(Board newBoard){
        int total =0;
        for (int x = 0; x < newBoard.getWidth()-1; x++)
            total += Math.abs(newBoard.getColumnHeight(x)-newBoard.getColumnHeight(x+1));
        return total;
    }

    private int countHoles(Board newBoard){
        int total = 0;
        for(int y =0; y<newBoard.getHeight()-1;y++) {
            for (int x = 0; x < newBoard.getWidth(); x++) {
                if ((newBoard.getGrid(x, y) != null) && (newBoard.getGrid(x, y + 1) == null))
                    total++;
            }
        }
        return total;
    }

    private int scoreBoard(Board newBoard) {
        if(newBoard.getMaxHeight()> 20) {
            return Integer.MIN_VALUE;
        }
        //System.out.println(-50*aggregateHeight(newBoard)+75*(newBoard.getRowsCleared())-35*countHoles(newBoard)-18*bumpiness(newBoard));
        //System.out.println(-50*aggregateHeight(newBoard)+75*(newBoard.getRowsCleared())+-35*countHoles(newBoard)+-18*bumpiness(newBoard));
        return (-50*aggregateHeight(newBoard)+75*(newBoard.getRowsCleared())-35*countHoles(newBoard)-18*bumpiness(newBoard));
        //return 100 - (newBoard.getMaxHeight() * 5)+newBoard.getRowsCleared()*100;
    }

}

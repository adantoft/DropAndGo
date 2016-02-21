package model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int[][] board;
    private String hashString;
    private final int BOARD_SIZE_X = 9; //NOTE: Code currently assumes board is always square
    private final int BOARD_SIZE_Y = BOARD_SIZE_X; //need to add X and Y lengths to all methods below to handle non-square
    private final int SCORING_X = 2; //number of points awarded for horizontal points touching
    private final int SCORING_Y = 2; //number of points awarded for vertical points touching
    private final int SCORING_Z = 1; //number of points awarded for diagonal points touching

    /**
     * BOARD PIECE DEFINITIONS:
     * 0 == open spot (no player has placed piece here)
     * 1 == player 1 has placed a piece
     * 2 == player 2 has placed a piece
     * [number] == player [number] has placed a piece; could do more than 2 players :-)
     */

    public Board() { //TODO add ability to make NxN, non-square board
        this.board = new int[BOARD_SIZE_X][BOARD_SIZE_Y];
    }

    /**
     * Places move on the board
     *
     * @param player     player number 1 to x
     * @param moveColumn column in which move is being made 1 to board horizontal length
     * @return true if move successful, false if move failed (column is full)
     */
    public boolean makeMove(int player, int moveColumn) {

        if (player < 1 || //must be either player 1 or 2
                player > 2 ||
                moveColumn < 1 || //move must fit on board
                moveColumn > BOARD_SIZE_X
                ) {
            throw new IndexOutOfBoundsException();
        }

        try {
            board[moveColumn - 1][getNextRow(moveColumn) - 1] = player;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * Provides the next available row in a column (1 spot above the highest game piece)
     *
     * @param col Column to search for next available spot (1 to board length)
     * @return row of free spot
     */

    public int getNextRow(int col) {
        int i = 0;

        while (i < BOARD_SIZE_Y) { //loops to find next available spot (denoted by zero)
            if (board[col - 1][i] == 0) {
                return i + 1;
            }
            i++;
        }
        throw new IndexOutOfBoundsException(); //no free spot in column
    }

    /**
     * Provides a copy of int array
     *
     * @return 2 dimensional int array
     */

    public int[][] getStateArrayCopy() { //creates copy of int array
        int[][] arrCopy = new int[BOARD_SIZE_X][BOARD_SIZE_Y];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                arrCopy[i][j] = board[i][j];
            }
        }
        return arrCopy;
    }

    /**
     * Provides a copy of int array form of an ArrayList
     *
     * @return 2 dimensional ArrayList
     */
    public List<List<Integer>> getStateArrayListCopy() {  //creates copy of int array in ArrayList form
        List<List<Integer>> arrCopy = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                arrCopy.get(i).add(board[i][j]);
            }
        }
        return arrCopy;
    }

    /**
     * Gets size of board
     *
     * @return int of board length
     */
    public int getBoardLength() {
        return BOARD_SIZE_X;
    }

    /**
     * Sets board to new int array (new pieces played)
     *
     * @param arr new 2d int array
     */
    private void setBoard(int[][] arr) {
        this.board = arr;
    }

    /**
     * Determines if there are still open moves on the board
     *
     * @return true if there are moves available
     */
    public boolean hasAvailableMoves() {
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][board.length - 1] == 0) return true;
        }
        return false;
    }

    /**
     * Provides copy of the board
     *
     * @return board copy
     */
    @Override
    public Board clone() {
        Board cloneBoard = new Board();
        cloneBoard.setBoard(getStateArrayCopy());
        return cloneBoard;
    }


    /**
     * Calculates and returns score of player
     *
     * @param player player in which to evaluate score
     * @return player score
     */
    public int getPlayerScore(int player) {
        int pointsX = 0;
        int pointsY = 0;
        int pointsZ = 0;
        boolean endOfBoardX = false;

        //TODO if reach empty spot on board, column can stop being evaluated to improve code performance
        //TODO also could make score just evaluate the most recent move and add to previous score - even faster

        for (int i = 0; i < board.length; i++) {//vertical score evaluation
            if (i == board.length - 1) {
                endOfBoardX = true;
            }
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == player) {  //tests if the current spot is player piece
                    if (j < board.length - 1 && board[i][j + 1] == player) {  //vertical evaluation - is up player piece?
                        pointsY += SCORING_Y;
                    }
                    if (!endOfBoardX) { //prevents evaluating off the horizontal board
                        if (board[i + 1][j] == player) {  //horizontal evaluation - is right player piece?
                            pointsX += SCORING_X;
                        }
                        if (j < board.length - 1 && board[i + 1][j + 1] == player) {  //diagonal evaluation - is right and up player piece?
                            pointsZ += SCORING_Z;
                        }
                        if (j > 0 && board[i + 1][j - 1] == player) {  //diagonal evaluation - is right and down player piece?
                            pointsZ += SCORING_Z;
                        }
                    }
                }
            }
        }
        return pointsX + pointsY + pointsZ;
    }

    /**
     * Calculates hashcode for board
     *
     * @return hashcode int
     */
    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder(); //this is used to create a string of numbers of the board state
        for (int i = 0; i < board.length; i++) {//needs to be recalculated after every move as the board is mutable
            for (int j = 0; j < board[0].length; j++) {
                sb.append(board[i][j]);
            }
        }
        hashString = sb.toString(); //actual hash is a hash of the number string
        return hashString.hashCode();
    }

    /**
     * Provides string of int of all pieces on board
     *
     * @return string of ints
     */
    @Override
    public String toString() {
        hashCode();
        return hashString;
    }

    /**
     * Tests if board equals another board
     *
     * @param obj other board
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return this.hashCode() == obj.hashCode();
    }


}

package view;

import model.Board;

public class TextUI {

    private Board board;


    public TextUI(Board board) {
        this.board = board;
    }

    /**
     * Prints text output of board to command line
     */
    public void printBoard() { //when given node n, the path from start to finish is calculated and printed out
        StringBuilder sb = new StringBuilder();
        char tmp;

        int[][] arr = board.getStateArrayCopy();

        sb.append(getBaseline(board.getBoardLength()));
        for (int j = board.getBoardLength() - 1; j >= 0; j--) {
            sb.append(" \n|");
            for (int i = 0; i < board.getBoardLength(); i++) {
                tmp = Integer.toString(arr[i][j]).charAt(0);
                if (tmp == '0') tmp = ' '; //replaces zero with blank
                if (tmp == '1') tmp = 'o'; //replaces player 1 with O
                if (tmp == '2') tmp = 'x'; //replaces player 2 with X
                sb.append(tmp + "|");
            }
        }
        sb.append(getBaseline(board.getBoardLength()));
        System.out.println(sb);
    }

    /**
     * Provides string of dashes to represent bottom of game board
     *
     * @param n board length
     * @return String of dahses
     */

    private String getBaseline(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i <= n * 2; i++) {
            sb.append("-");
        }
        return sb.toString();
    }

    public void printScore() {
        System.out.println("Total Move Cost = \t\t");

    }

}

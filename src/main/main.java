package main;

import model.AI;
import model.Board;
import model.Factory;
import view.TextUI;

import java.util.Scanner;

public class main {

    public static void main(final String[] args) {

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        Board board = Factory.getBoard(); //gets game board
        TextUI ui = new TextUI(board); //gets user interface

        int move = 0;
        boolean gameState = true; //game will loop turns until false
        int player = 1; //default player to start
        int moveCount = 0; //keeps track of moves
        int aiDifficulty; //ply depth
        int moveLimit; //once this is reached gamestate is set to false
        boolean pruning = false;

        if (board.getBoardLength() % 2 == 0) { //if board has odd number of pieces, only 1 blank space means game over so both players have equal turns
            moveLimit = board.getBoardLength() * board.getBoardLength(); // even
        } else {
            moveLimit = board.getBoardLength() * board.getBoardLength() - 1; //odd
        }

        System.out.println("--- Welcome to Simacogo! ---");

        System.out.print("Enable Pruning? (1 = yes/ all other num = no): ");

        if (reader.nextInt() == '1'){
            pruning = true;
        }

        System.out.print("Pick difficulty (1 to 20): ");
        aiDifficulty = reader.nextInt();
        AI ai = new AI(board, 2, 1, aiDifficulty, pruning);

//        System.out.print("Pick difficulty for player 1(1 to 20): "); //used to make AIs battle each other
//        AI ai2 = new AI(board, 1, 2, reader.nextInt()); //used to make AIs battle each other

        System.out.println("Ready??");
        System.out.println("Fight!!");

        while (gameState) {
            ui.printBoard();
            System.out.println("P1 Score: " + board.getPlayerScore(1) + " P2 Score: " + board.getPlayerScore(2));
            System.out.print("Player " + player + " pick your move (1 to 9): ");

            if (player == 1) {
//                move = ai2.getMove(); //used to make AIs battle each other
                move = reader.nextInt(); //user input
            } else if (player == 2) {
                move = ai.getMove(); //asks AI for a move
            }

            if (move >= 1 && move <= 9 && board.makeMove(player, move)) { //tests user input and if move was successful
                moveCount++;
                player = (player == 1) ? 2 : 1; //switch players
            } else {
                System.out.print("Invalid move; please try again.");
            }
            if (!board.hasAvailableMoves() || moveCount == moveLimit) { //checks if there are available moves or if move limit is reached
                gameState = false;     // also will leave 1 spot open in odd by odd board length games to not give first player an extra move
            }
        }

        ui.printBoard();
        System.out.println("Game over! Final Scores:");
        System.out.println("Player 1 Total Score: " + board.getPlayerScore(1));
        System.out.println("------------------------------");
        System.out.println("Player 2 Total Score: " + board.getPlayerScore(2));
        if (board.getPlayerScore(1) > board.getPlayerScore(2)) {
            System.out.println("Player 1 Wins!!!!!!!!!!!");
        } else if (board.getPlayerScore(1) < board.getPlayerScore(2)) {
            System.out.println("Player 2 Wins!!!!!!!!!!!");
        } else {
            System.out.println("It's a tie!");
        }
    }
}

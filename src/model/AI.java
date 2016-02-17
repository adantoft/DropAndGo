package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AI {

    Board board;
    int plyLevel;
    int playerNumber;
    int opponentPlayerNumber;
    boolean evaluateMax; //used to determine if Node should be evaluated for Min or Max
    boolean pruning; //pruning enabled/disabled

    public AI(Board board, int playerNumber, int opponentPlayerNumber, int plyLevel, boolean pruning) {
        this.board = board;
        this.playerNumber = playerNumber;
        this.opponentPlayerNumber = opponentPlayerNumber;
        this.plyLevel = plyLevel;
        this.pruning = pruning;
        if (plyLevel % 2 == 0) {  //uses ply to determine if the base node will be Max or Min's time to evaluate
            evaluateMax = true; //ply is even
        } else {
            evaluateMax = false; //ply is odd
        }
    }

    public int getMove() { //asks the AI for the next move
        Integer alphaVal = Integer.MIN_VALUE; //sets alpha beta
        Integer betaVal = Integer.MAX_VALUE;

        Node rootNode = new Node(null, board, null, 0); //makes root node that has the current game board as a starting point
        List<Node> goodMoveNodes = new ArrayList<>(); //list of nodes that are good moves
        if (pruning) {
            makePruningMoveTree(rootNode, playerNumber, alphaVal, betaVal, evaluateMax);  //run minmax with pruning
        }else {
            makeMoveTree(rootNode, playerNumber); //create minmax tree
            evalMoveTree(rootNode, evaluateMax); //bubble up the tree evaluating moves
        }


        for (Node n : rootNode.getChildNodes()) {
            if (n.getEvalValue() == rootNode.getEvalValue()) { //grabs nodes that are good moves
                goodMoveNodes.add(n);
            }
        }
        if (goodMoveNodes.size() == 0) { //there are no good moves (game over)
            Random r = new Random();
            return rootNode.getChildNodes().get(r.nextInt(rootNode.getChildNodes().size())).getAction(); //picks move at random
        } else if (goodMoveNodes.size() > 1) {//there is more than 1 good move; pick 1 at random
            Random r = new Random();
            return goodMoveNodes.get(r.nextInt(goodMoveNodes.size())).getAction(); //picks move at random
        } else {
            return goodMoveNodes.get(0).getAction(); //there is only 1 good move
        }

    }

    public void makePruningMoveTree(Node node, int player, Integer alphaVal, Integer betaVal, boolean evaluateMax) {
        if (node.getDepth() < plyLevel - 1) {
            for (Node n : node.getSuccessorStates(player)) {
                if (player == playerNumber) {
                    makePruningMoveTree(n, opponentPlayerNumber, alphaVal, betaVal, evaluateMax);
                } else {
                    makePruningMoveTree(n, playerNumber, alphaVal, betaVal, evaluateMax);
                }

                if (evaluateMax) { // MAX Node
                    n.setEvalValue(evaluate(n));
                    alphaVal = Math.max(n.getEvalValue(), alphaVal);
                    if (alphaVal >= betaVal) { //prune
                        alphaVal = Integer.MAX_VALUE;
                        break;
                    } else { //no prune
                        node.setEvalValue(n.getEvalValue());
                        betaVal = Math.min(node.getEvalValue(), betaVal);
                    }
                } else { //MIN Node
                    n.setEvalValue(evaluate(n));
                    betaVal = Math.min(n.getEvalValue(), betaVal);
                    if (betaVal <= alphaVal) { //prune
                        betaVal = Integer.MAX_VALUE;
                        break;
                    } else { //no prune
                        node.setEvalValue(n.getEvalValue());
                        alphaVal = Math.max(node.getEvalValue(), alphaVal);
                    }
                }
            }
        } else {
            if (node.getSuccessorStates(player).size() != 0) {

                if (evaluateMax) { // MAX Node
                    for (Node n : node.getChildNodes()) {
                        n.setEvalValue(evaluate(n));
                        alphaVal = Math.max(n.getEvalValue(), alphaVal);
                        if (alphaVal >= betaVal) { //prune
                            alphaVal = Integer.MAX_VALUE;
                            break;
                        } else { //no prune
                            node.setEvalValue(n.getEvalValue());
                            betaVal = Math.min(node.getEvalValue(), betaVal);
                        }

                    }

                } else { //MIN Node
                    for (Node n : node.getChildNodes()) {
                        n.setEvalValue(evaluate(n));
                        betaVal = Math.min(n.getEvalValue(), betaVal);

                        if (betaVal <= alphaVal) { //prune
                            betaVal = Integer.MAX_VALUE;
                            break;
                        } else { //no prune
                            node.setEvalValue(n.getEvalValue());
                            alphaVal = Math.max(node.getEvalValue(), alphaVal);
                        }

                    }
                }

            }
        }
    }



    public void makeMoveTree(Node node, int player) { //recursive depth first function; creates node tree
        if (node.getDepth() < plyLevel - 1) {
            for (Node n : node.getSuccessorStates(player)) {
                if (player == playerNumber) { //alternates players Min Max
                    makeMoveTree(n, opponentPlayerNumber);
                } else {
                    makeMoveTree(n, playerNumber);
                }
            }
        } else {
            if (node.getSuccessorStates(player).size() != 0) {
                for (Node n : node.getChildNodes()) {
                    n.setEvalValue(evaluate(n)); //set node evaluation values at base of tree
                }
            }
        }
    }

    public void evalMoveTree(Node node, boolean evaluateMax) { //recursive depth first function; finds base node
        if (node.getDepth() < plyLevel - 1) {
            if (node.hasChildren()) {
                for (Node n : node.getChildNodes()) {
                    evalMoveTree(n, !evaluateMax);
                }
            }
            node.setEvalValue(getMinMaxBubbleUp(node, evaluateMax)); //evaluate moves
        } else {
            node.setEvalValue(getMinMaxBubbleUp(node, evaluateMax)); //evaluate moves
        }
    }

    private Integer getMinMaxBubbleUp(Node node, boolean evaluateMax) { //function to set node evaluation value based on its children's evaluations
        if (node.getChildNodes() == null) {
            return evaluate(node);
        } else {
            Integer evalValue = node.getChildNodes().get(0).getEvalValue();
            for (Node n : node.getChildNodes()) {
                assert n.getEvalValue() != null;
                if (evaluateMax) {
                    evalValue = Math.max(n.getEvalValue(), evalValue); //evaluate for Max
                } else {
                    evalValue = Math.min(n.getEvalValue(), evalValue); //evaluate for min
                }
            }
            return evalValue;
        }

    }


    private int evaluate(Node node) { //evaluation function. gets player score minus opponent player score

        Integer eval = node.getBoard().getPlayerScore(playerNumber) - node.getBoard().getPlayerScore(opponentPlayerNumber);

        if (!node.getBoard().hasAvailableMoves()) { //if there are no moves left (game over)
            if (eval > 0) {
                eval = Integer.MAX_VALUE; //max winning value
            } else {
                eval = Integer.MIN_VALUE; //min winning value
            }
        }

        return eval;
    }



}

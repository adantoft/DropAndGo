package model;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private Board board;
    private Node parentNode;
    private List<Node> childNodes = new ArrayList<>();
    private Integer depth;
    private Integer action;
    private Integer evalValue = null;


    public Node(Node parentNode, Board board, Integer action, Integer depth) {
        this.parentNode = parentNode;
        this.board = board;
        this.action = action;
        this.depth = depth;
    }

    public Board getBoard() {
        return board;
    }

    public int getAction() {
        return action;
    }

    public Node getParent() {
        return parentNode;
    }

    public Integer getDepth() {
        return depth;
    }

    public boolean hasChildren() {
        if (childNodes.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Integer getEvalValue() {
        return evalValue;
    }

    public void setEvalValue(Integer evalValue) {
        this.evalValue = evalValue;
    }

    public List<Node> getChildNodes() { //returns list of successor states (children of this node)

        if (childNodes.size() == 0) {
            return null;
        } else {
            return childNodes; //does not check for visiting a state since all states will be a new state (can't undo moves)
        }

    }

    public List<Node> getSuccessorStates(int player) { //returns list of successor states (children of this node)
        if (!hasChildren()) {
            makeChildren(player); //makes children if they do not exist
        }
        return childNodes; //does not check for visiting a state since all states will be a new state (can't undo moves)
    }

    private void makeChildren(int player) { //makes children (all possible moves) from current board state
        //illegal moves cause yield null child nodes

        Board tmpBoard;

        for (int i = 1; i < board.getBoardLength() + 1; i++) {

            tmpBoard = board.clone();

            assert tmpBoard.equals(board); //tests clone function is working properly
            if (tmpBoard.makeMove(player, i)) { //tries to make move and tests success
                childNodes.add(new Node(this, tmpBoard, i, depth + 1)); //move successful; makes new node
            } else { //move failed; do not add it to list of children
//                childNodes.add(null); //might not be needed since each node has an index (order doesn't matter)
            }
        }
    }

    @Override
    public String toString() {
        return "State: " + board + " Action: " + action + " Eval: " + evalValue;
    }

}

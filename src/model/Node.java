package model;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private Board board;
    private Node parentNode;
    private List<Node> childNodes = new ArrayList<>();
    private Integer depth;
    private Integer action;
    private Integer evalValue = null;  //this is the value of the board based on the evaluation function set in the AI

    /**
     * Node is primarily used to keep track of board states and relationships between boards
     *
     * @param parentNode the parent of this Node (what the board was before this board state)
     * @param board      the board that this node contains
     * @param action     the action made to get to this board
     * @param depth      number of layers from the root node
     */
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

    /**
     * Determines if this mode has children
     *
     * @return true if node has children
     */
    public boolean hasChildren() {
        if (childNodes.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Provides the value of the evaluation as described in the AI
     *
     * @return Integer value of board
     */
    public Integer getEvalValue() {
        return evalValue;
    }

    /**
     * Sets the value of the evaluation as described in the AI
     *
     * @param evalValue value to be set
     */
    public void setEvalValue(Integer evalValue) {
        this.evalValue = evalValue;
    }

    /**
     * @return List of child nodes
     */
    public List<Node> getChildNodes() { //returns list of successor states (children of this node)

        if (childNodes.size() == 0) {
            return null;
        } else {
            return childNodes; //does not check for visiting a state since all states will be a new state (can't undo moves)
        }

    }

    /**
     * @param player whose success states to get
     * @return list of child nodes (possible moves from current board for player)
     */
    public List<Node> getSuccessorStates(int player) { //returns list of successor states (children of this node)
        if (!hasChildren()) {
            makeChildren(player); //makes children if they do not exist
        }
        return childNodes; //does not check for visiting a state since all states will be a new state (can't undo moves)
    }

    /**
     * Makes children nodes (available board states -> possible moves) for player
     *
     * @param player whose success states to calculate
     */
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

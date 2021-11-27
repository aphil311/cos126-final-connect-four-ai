import java.awt.*;
import java.util.ArrayList;

public class MonteCarlo extends Player {
    private int opp;

    public MonteCarlo(int val, Board board, Color color, int opp) {
        super(val, board, color);
        this.opp = opp;
    }

    public void move() {
        // create root of tree
        Node root = new Node(board, val, opp);
        // create nodes
        root.getAllStates();

        // for certain number of iterations

        for (int timer = 0; timer < 1000; timer++) {
            // calculate exploration/exploitation
            Node node = root.returnBestUTC();
            node.incrementTotalVisits();

            // choose node
            while (!node.isLeaf()) {
                node = node.returnBestUTC();
                node.incrementTotalVisits();
            }

            // if unexplored, roll out ... if explored, expand nodes
            if (node.getTotalVisits() == 0) {
                node.rollout(node.getBoard(), node.getHeights());
            } else {
                node.getAllStates();
                Node child = node.returnBestUTC();
                child.rollout(child.getBoard(), child.getHeights());
                child.incrementTotalVisits();
            }
        }

        board.insert(root.returnBestUTC().getColumn(), val);
    }

    private class Node {
        private final Board state;
        private int totalVisits;
        private int winValue;
        private Node parent;
        private ArrayList<Node> children;
        private final int val, opp;
        private int column = -1;

        private Node(Board board, int val, int opp) {
            this.val = val;
            this.opp = opp;
            this.state = new Board(board.getBoard(), board.getHeights());
            children = new ArrayList<>();
            totalVisits = 0;
        }

        private Node(Board board, int val, int opp, int col) {
            this.val = val;
            this.opp = opp;
            this.state = new Board(board.getBoard(), board.getHeights());
            children = new ArrayList<>();
            totalVisits = 0;
            this.column = col;
        }

        private void getAllStates() {
            for (int i = 0; i < state.getCols(); i++) {
                if (!state.isFull(i)) {
                    StdOut.println("Created new state");
                    Board newBoard = new Board(state.getBoard(), state.getHeights());
                    newBoard.insert(i, val);
                    // assume optimal play from opponent
                    newBoard.insert(5, opp);
                    Node node = new Node(newBoard, opp, val, i);
                    node.setParent(this);
                    children.add(node);
                    StdOut.println(children.get(i).getColumn());
                }
            }
        }


        private double calculateUCT() {
            if (totalVisits == 0) return Double.POSITIVE_INFINITY;
            return (double) winValue / totalVisits + 1.41 * Math.sqrt(Math.log(parent.getTotalVisits()) / totalVisits);
        }

        private Node returnBestUTC() {
            if (children.size() == 0) throw new RuntimeException("no children found");
            Node best = children.get(0);
            double bestUTC = best.calculateUCT();
            for (int i = 1; i < children.size(); i++) {
                Node potential = children.get(i);
                double potentialUTC = potential.calculateUCT();
                if (potentialUTC > bestUTC) {
                    best = potential;
                    bestUTC = potentialUTC;
                }
            }
            return best;
        }

        private void rollout(int[][] board, int[] height) {
            Board randomGame = new Board(board, height);
            Player us = new Random(val, randomGame, Color.BLACK);
            Player opponent = new Random(opp, randomGame, Color.BLACK);
            Player[] players = {us, opponent};
            int i = 0;
            while (!randomGame.isFull() && randomGame.checkWinner() == -1) {
                players[i].move();
                if (i == 0) i = 1;
                else i = 0;
            }
            // if winner is val then win ++, if winner is opp then win --, else no change
            if (randomGame.checkWinner() == val) winValue++;
            else if (randomGame.checkWinner() == opp) winValue--;
        }

        private void setParent(Node parent) {
            this.parent = parent;
        }

        private boolean isLeaf() {
            return children.size() == 0;
        }

        private int getColumn() {
            return column;
        }

        private int[][] getBoard() {
            return state.getBoard();
        }

        private int[] getHeights() {
            return state.getHeights();
        }

        private double getTotalVisits() {
            return totalVisits;
        }

        private void incrementTotalVisits() {
            totalVisits++;
        }
    }

    public static void main(String[] args) {

    }
}

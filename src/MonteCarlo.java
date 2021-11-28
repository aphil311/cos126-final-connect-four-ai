import java.awt.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MonteCarlo extends Player {
    // integer representation of the opponent
    private final int opp;
    private final int iterations;

    public MonteCarlo(int val, Board board, Color color, int opp, int iterations) {
        super(val, board, color);
        this.opp = opp;
        this.iterations = iterations;
    }

    /**
     * uses a monte carlo tree search of a certain depth given by iterations
     */
    public void move() {
        // create root of tree
        Node root = new Node(board, val, opp);
        // create nodes
        root.getAllStates();

        // for certain number of iterations

        for (int timer = 0; timer < iterations; timer++) {
            // calculate exploration/exploitation
            Node node = root.returnBestUTC();
            node.incrementTotalVisits();

            // choose node
            while (!node.isLeaf()) {
                node = node.returnBestUTC();
                node.incrementTotalVisits();
            }
            System.out.println(node.getColumn());

            // if unexplored, roll out ... if explored, expand nodes
            if (node.getTotalVisits() == 0) {
                //StdOut.println("Rollout");
                node.rollout(node.getBoard(), node.getHeights());
                node.incrementTotalVisits();
            } else {
                //StdOut.println("Expand");
                node.getAllStates();
                Node child = node.returnBestUTC();
                child.rollout(child.getBoard(), child.getHeights());
                child.incrementTotalVisits();
            }
        }
        // play
        for (int f = 0; f < root.children.size(); f++) {
            System.out.print(root.children.get(f).calculateUCT() + " ");
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

        /**
         * Links all possible plays to this node
         */
        private void getAllStates() {
            for (int i = 0; i < state.getCols(); i++) {
                if (!state.isFull(i)) {
                    Board newBoard = new Board(state.getBoard(), state.getHeights());
                    newBoard.insert(i, val);
                    // TODO: Once this class stops being a pain and giving me errors replace with another MCTS
                    // this algorithm requires the assumption of optimal play from its opponent
                    int attempt1 = StdRandom.uniform(7);
                    while (newBoard.isFull(attempt1)) {
                        attempt1 = StdRandom.uniform(7);
                    }
//                    Player player2 = new MonteCarlo(opp, newBoard, Color.BLACK, val, 500);
//                    player2.move();
                    newBoard.insert(attempt1, opp);


                    //System.out.println("State:\n" + newBoard);
                    Node node = new Node(newBoard, val, opp, i);
                    node.setParent(this);
                    children.add(node);
                }
            }
        }

        /**
         * Calculate upper confidence bound given by w/n + C sqrt{ln(N)/n}
         *
         * @return upper confidence bound
         */
        private double calculateUCT() {
            if (totalVisits == 0) return Double.POSITIVE_INFINITY;
            return (double) winValue / (double) totalVisits + 100.0 * (Math.sqrt(Math.log((double) parent.getTotalVisits()) / totalVisits));
        }

        /**
         * Iterates through all child nodes searching for highest UTC value
         *
         * @return the child node with the highest UCT value
         */
        private Node returnBestUTC() {
            if (children.size() == 0) throw new NoSuchElementException("no children");
            Node best = children.get(0);
            double bestUTC = best.calculateUCT();
            for (int i = 1; i < children.size(); i++) {
                // save node and UTC to avoid repeated method calls
                Node potential = children.get(i);
                double potentialUTC = potential.calculateUCT();
                // replace best node if a better one is found
                if (potentialUTC > bestUTC) {
                    best = potential;
                    bestUTC = potentialUTC;
                }
            }
            return best;
        }

        /**
         * Simulates a random game from a given position
         *
         * @param board  two dimensional array representing the board
         * @param height array that keeps track of how much space is left per column
         */
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
            //StdOut.println("Winner of rollout is: " + randomGame.checkWinner());
            if (randomGame.checkWinner() == val) winValue++;
            else if (randomGame.checkWinner() == opp) winValue--;
        }

        /**
         * Setter method that links this node to its parent node
         *
         * @param parent the node that should be above this object in the tree
         */
        private void setParent(Node parent) {
            this.parent = parent;
        }

        /**
         * Tells whether or not this node has undergone node expansion
         *
         * @return true if this node has no children
         */
        private boolean isLeaf() {
            return children.size() == 0;
        }

        /**
         * Accessor method for the column that this node assumes the ai plays
         *
         * @return integer representing the column that this node added a piece to
         */
        private int getColumn() {
            return column;
        }

        /**
         * Accessor method to return the board
         *
         * @return
         */
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

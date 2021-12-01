import java.awt.*;
import java.util.ArrayList;

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
        root.getAllStatesFromRoot();
        int win = 0;

        // for certain number of iterations

        for (int timer = 0; timer < iterations; timer++) {
            // calculate exploration/exploitation
            Node node = root.returnBestUCT();

            // choose node
            //System.out.println("selection");
            while (!node.isLeaf()) {
                if (!node.state.isFull())
                    node = node.returnBestUCT();
            }
            //System.out.println(node.getColumn());

            // if unexplored, roll out ... if explored, expand nodes
            if (node.getTotalVisits() == 0) {
                //StdOut.println("rollout");
                win = node.rollout(node.getBoard(), node.getHeights());
            } else {
                if (!node.state.isFull()) {
                    node.getAllStates();
                    Node child = node.returnBestUCT();
                    win = child.rollout(child.getBoard(), child.getHeights());
                    child.incrementTotalVisits();
                    node.setWinValue(win);
                }
            }

            // backpropagation
            node.incrementTotalVisits();
            while (node.getParent() != root) {
                node = node.getParent();
                node.incrementTotalVisits();
                node.setWinValue(win);
            }
            root.incrementTotalVisits();
            root.setWinValue(win);
        }

        // play
//        for (int f = 0; f < root.children.size(); f++) {
//            System.out.print(root.children.get(f).calculateUCT() + " ");
//        }
        board.insert(root.returnBestUCT().getColumn(), val);
    }

    private class Node {
        private final Board state;
        private int totalVisits;
        private double winValue;
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
            winValue = 0;
        }

        private Node(Board board, int val, int opp, int col) {
            this.val = val;
            this.opp = opp;
            this.state = new Board(board.getBoard(), board.getHeights());
            children = new ArrayList<>();
            totalVisits = 0;
            winValue = 0;
            if (board.checkWinner() == val) {
                winValue = Double.POSITIVE_INFINITY;
                //System.out.println("found win next move: " + col);
                totalVisits = 1;
            } else if (board.checkWinner() == opp) {
                winValue = Double.NEGATIVE_INFINITY;
                //System.out.println("opponent wins next move");
            }
            this.column = col;
        }

        /**
         * Links all possible plays to this node
         */
        private void getAllStatesFromRoot() {
            for (int i = 0; i < state.getCols(); i++) {
                if (!state.isFull(i)) {
                    Board newBoard = new Board(state.getBoard(), state.getHeights());
                    newBoard.insert(i, val);
                    // TODO: Once this class stops being a pain and giving me errors replace with another MCTS
                    // this algorithm requires the assumption of optimal play from its opponent
                    newBoard.insert(educatedRandom(newBoard.getBoard(), newBoard.getHeights()), opp);


                    //System.out.println("State:\n" + newBoard);
                    Node node = new Node(newBoard, val, opp, i);
                    node.setParent(this);
                    children.add(node);
                }
            }
        }

        private void getAllStates() {
            for (int i = 0; i < state.getCols(); i++) {
                if (!state.isFull(i)) {
                    Board newBoard = new Board(state.getBoard(), state.getHeights());
                    newBoard.insert(i, val);
                    // TODO: Once this class stops being a pain and giving me errors replace with another MCTS
                    // this algorithm requires the assumption of optimal play from its opponent

                    if (!newBoard.isFull())
                        newBoard.insert(educatedRandom(newBoard.getBoard(), newBoard.getHeights()), opp);

                    //System.out.println("State:\n" + newBoard);
                    Node node = new Node(newBoard, val, opp);
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
            //System.out.println("Win Value : " + winValue + "; Total Visits: " + totalVisits + "; Parent Total Visits: " + parent.getTotalVisits());
            return winValue / (double) totalVisits + 1.44 * (Math.sqrt(Math.log((double) parent.getTotalVisits()) / totalVisits));
        }

        /**
         * Iterates through all child nodes searching for highest UCT value
         *
         * @return the child node with the highest UCT value
         */
        private Node returnBestUCT() {
            if (children.size() == 0) System.out.println(this.state);
            Node best = children.get(0);
            double bestUCT = best.calculateUCT();
            for (int i = 1; i < children.size(); i++) {
                // save node and UCT to avoid repeated method calls
                Node potential = children.get(i);
                double potentialUCT = potential.calculateUCT();
                // replace best node if a better one is found
                if (potentialUCT > bestUCT) {
                    best = potential;
                    bestUCT = potentialUCT;
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
        private int rollout(int[][] board, int[] height) {
            Board randomGame = new Board(board, height);
            Player us = new Random(val, randomGame, Color.BLACK, opp);
            Player opponent = new Random(opp, randomGame, Color.BLACK, val);
            Player[] players = {us, opponent};
            int i = 0;
            while (!randomGame.isFull() && randomGame.checkWinner() == -1) {
                players[i].move();
                if (i == 0) i = 1;
                else i = 0;
            }
            // if winner is val then win ++, if winner is opp then win --, else no change
            //StdOut.println("Winner of rollout is: " + randomGame.checkWinner());
            if (randomGame.checkWinner() == val) {
                winValue++;
                return 1;
            } else if (randomGame.checkWinner() == opp) {
                winValue--;
                return -1;
            }
            return 0;
        }

        private void setWinValue(int win) {
            winValue += win;
        }

        /**
         * Setter method that links this node to its parent node
         *
         * @param parent the node that should be above this object in the tree
         */
        private void setParent(Node parent) {
            this.parent = parent;
        }

        private Node getParent() {
            return parent;
        }

        private int educatedRandom(int[][] board, int[] height) {
            for (int i = 0; i < height.length; i++) {
                Board boardCopy = new Board(board, height);
                if (!boardCopy.isFull(i)) {
                    boardCopy.insert(i, opp);
                    if (boardCopy.checkWinner() != -1)
                        return i;
                }
            }
            for (int i = 0; i < height.length; i++) {
                Board boardCopy = new Board(board, height);
                if (!boardCopy.isFull(i)) {
                    boardCopy.insert(i, val);
                    if (boardCopy.checkWinner() != -1)
                        return i;
                }
            }

            Board boardCopy = new Board(board, height);
            int attempt;
            do {
                attempt = StdRandom.uniform(7);
            } while (boardCopy.isFull(attempt));

            return attempt;
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

        private int getTotalVisits() {
            return totalVisits;
        }

        private void incrementTotalVisits() {
            totalVisits++;
            //System.out.println(totalVisits);
        }
    }

    public static void main(String[] args) {

    }
}

import java.awt.Color;
import java.util.ArrayList;

/**
 * Uses a a slightly modified Monte Carlo Tree Search algorithm to play
 * Connect Four with relatively high accuracy.
 */
public class MonteCarlo extends Player {
    private final int opp;          // integer representation of the opponent
    private final int iterations;   // number of times this tree should be
    private final boolean verbose;  // print monte carlo visualizer
    // iterated through

    /**
     * Creates a new MonteCarlo Tree
     *
     * @param val        integer representation of the monte carlo controlled
     *                   player
     * @param board      object representing the connect four board
     * @param color      color representation of this player
     * @param opp        integer representation of the opponent
     * @param iterations number of times this tree should be searched per move
     * @param verb       verbose mode prints visualization of the ai
     */
    public MonteCarlo(int val, Board board, Color color, int opp,
                      int iterations, String verb) {
        super(val, board, color);
        this.opp = opp;
        this.iterations = iterations;
        if (verb.equals("-v"))
            verbose = true;
        else verbose = false;
    }

    /**
     * Uses a monte carlo tree search of a certain depth given by iterations
     */
    public void move() {
        // create root of tree
        Node root = new Node(board, this.getVal(), opp);
        // create nodes
        root.getAllStatesFromRoot();
        int win = 0;

        // for certain number of iterations
        for (int timer = 0; timer < iterations; timer++) {
            // calculate exploration/exploitation
            Node node = root.returnBestUCT();

            // choose node
            while (!node.isLeaf()) {
                if (!node.state.isFull())
                    node = node.returnBestUCT();
            }

            // if unexplored, roll out ... if explored, expand nodes
            if (node.getTotalVisits() == 0) {
                win = node.rollout(node.getBoard(), node.getHeights());
            }
            else {
                // if board is full returnBestUCT gives error (no children)
                if (!node.state.isFull()) {
                    node.getAllStates();                // expand
                    Node child = node.returnBestUCT();  // select
                    // rollout
                    win = child.rollout(child.getBoard(), child.getHeights());
                    // first step of backpropagation
                    child.incrementTotalVisits();
                    node.setWinValue(win);
                }
            }

            // backpropagation up entire tree
            node.incrementTotalVisits();
            while (node.getParent() != root) {
                node = node.getParent();
                node.incrementTotalVisits();
                node.setWinValue(win);
            }
            root.incrementTotalVisits();
            root.setWinValue(win);

            // lets the terminal show a visual of what column is preferred
            // while the algorithm is running (just interesting)
            if (timer % 100 == 0 && timer < 1001 && verbose) {
                System.out.print(root.returnBestUCT().getColumn() + " >> ");
            }
        }
        System.out.println(root.returnBestUCT().getColumn() + "\n");
        // play
        board.insert(root.returnBestUCT().getColumn(), this.getVal());
    }

    /**
     * Node of the tree stores a state, total visits, and an integer value
     * representing win probability.
     */
    private class Node {
        private final Board state; // whatever board setup this node represents
        private int totalVisits; // number of times this board has been seen
        // number of times playout of this board results in a win
        private double winValue;
        private Node parent; // parent node
        private ArrayList<Node> children; // child nodes
        // stores integer representations of both players
        private final int val, opp;
        private int column = -1; // column of play this node represents

        /**
         * creates a new node that is a child of one of the branches off of root
         *
         * @param board whatever board setup this node represents
         * @param val   integer representation of the MCTS player
         * @param opp   integer representation of the human player
         */
        private Node(Board board, int val, int opp) {
            this.val = val;
            this.opp = opp;
            this.state = new Board(board.getBoard(), board.getHeights());
            children = new ArrayList<>();
            totalVisits = 0;
            winValue = 0;
        }

        /**
         * Creates a new branch off of root
         *
         * @param board whatever board setup this node represents
         * @param val   integer representation of the MCTS player
         * @param opp   integer representation of the human player
         * @param col   column of play this node represents
         */
        private Node(Board board, int val, int opp, int col) {
            this.val = val;
            this.opp = opp;
            this.state = new Board(board.getBoard(), board.getHeights());
            children = new ArrayList<>();
            totalVisits = 0;
            winValue = 0;
            // if next move is a win than it should be played automatically
            if (board.checkWinner() == val) {
                winValue = Double.POSITIVE_INFINITY;
                totalVisits = 1;
            }
            // if next move is a loss than it should be avoided automatically
            else if (board.checkWinner() == opp) {
                winValue = Double.NEGATIVE_INFINITY;
            }
            this.column = col;
        }

        /**
         * Links all possible plays to this node
         */
        private void getAllStatesFromRoot() {
            // iterate through each possible move
            for (int i = 0; i < state.getCols(); i++) {
                // prevent from inserting in a column that is full
                if (!state.isFull(i)) {
                    // create a defensive copy
                    Board newBoard = new Board(state.getBoard(),
                                               state.getHeights());
                    newBoard.insert(i, val);
                    // this algorithm requires the assumption of strong play
                    // from its opponent
                    newBoard.insert(educatedRandom(newBoard.getBoard(),
                                                   newBoard.getHeights()), opp);
                    // create node and link in both directions
                    Node node = new Node(newBoard, val, opp, i);
                    node.setParent(this);
                    children.add(node);
                }
            }
        }

        /**
         * Get all states but do not pay attention to column of play
         */
        private void getAllStates() {
            for (int i = 0; i < state.getCols(); i++) {
                if (!state.isFull(i)) {
                    // Defensive copy
                    Board newBoard = new Board(state.getBoard(),
                                               state.getHeights());
                    newBoard.insert(i, val);

                    // prevents infinite loop when board is full and random
                    // method keeps trying to insert
                    if (!newBoard.isFull())
                        // this algorithm requires the assumption of strong
                        // play from its opponent
                        newBoard.insert(educatedRandom(newBoard.getBoard(),
                                                       newBoard.getHeights()),
                                        opp);
                    // create child node and link both ways
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
        public double calculateUCT() {
            // for zero in denominator
            if (totalVisits == 0) return Double.POSITIVE_INFINITY;
            return winValue / (double) totalVisits + 1.44 * (Math
                    .sqrt(Math.log(parent.getTotalVisits()) / totalVisits));
        }

        /**
         * Iterates through all child nodes searching for highest UCT value
         *
         * @return the child node with the highest UCT value
         */
        private Node returnBestUCT() {
            if (children.size() == 0) throw new RuntimeException("no children");
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
         * @param height array that keeps track of how much space is left per
         *               column
         * @return +1 if a win, -1 if a loss, 0 if a draw
         */
        private int rollout(int[][] board, int[] height) {
            Board randomGame = new Board(board, height);

            // create new game and play through randomly until completion
            Player us = new Random(val, randomGame, Color.BLACK, opp);
            Player opponent = new Random(opp, randomGame, Color.BLACK, val);
            Player[] players = { us, opponent };
            int i = 0;
            while (!randomGame.isFull() && randomGame.checkWinner() == -1) {
                players[i].move();
                if (i == 0) i = 1;
                else i = 0;
            }
            // if winner is val then win ++, if winner is opp then win --,
            // else no change
            if (randomGame.checkWinner() == val) {
                winValue++;
                return 1;
            }
            else if (randomGame.checkWinner() == opp) {
                winValue--;
                return -1;
            }
            return 0;
        }

        /**
         * Setter method to increment or decrement win value
         *
         * @param win +1 for a win, -1 for a loss
         */
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

        /**
         * Accessor method to return parent node
         *
         * @return parent node
         */
        private Node getParent() {
            return parent;
        }

        /**
         * Returns random open column as long as a win is not possible
         *
         * @param board  two dimensional array representation of the board to
         *               be played on
         * @param height current heights of each column on the board
         * @return column that should be played, otherwise random number
         */
        private int educatedRandom(int[][] board, int[] height) {
            // Check if opponent can win next move
            for (int i = 0; i < height.length; i++) {
                Board boardCopy = new Board(board, height);
                if (!boardCopy.isFull(i)) {
                    boardCopy.insert(i, opp);
                    if (boardCopy.checkWinner() != -1)
                        return i;
                }
            }

            // Check if ai can win next move
            for (int i = 0; i < height.length; i++) {
                Board boardCopy = new Board(board, height);
                if (!boardCopy.isFull(i)) {
                    boardCopy.insert(i, val);
                    if (boardCopy.checkWinner() != -1)
                        return i;
                }
            }

            // otherwise play randomly
            Board boardCopy = new Board(board, height);
            int attempt;
            do {
                // StdRandom does not play nice with jar or JavaFX
                attempt = (int) (Math.random() * 7);
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
         * @return integer representing the column that this node added a piece
         * to
         */
        private int getColumn() {
            return column;
        }

        /**
         * Accessor method to return the board
         *
         * @return two dimensional array representation of the state of this
         * node
         */
        private int[][] getBoard() {
            return state.getBoard();
        }

        /**
         * Accessor method to return the height
         *
         * @return one dimensional array representation of the state of this
         * board
         */
        private int[] getHeights() {
            return state.getHeights();
        }

        /**
         * Accessor method for total visits to this node
         *
         * @return total number of times this node has been visited in the tree
         */
        private int getTotalVisits() {
            return totalVisits;
        }

        /**
         * Increments the totalVisits variable by one
         */
        private void incrementTotalVisits() {
            totalVisits++;
        }
    }

    /**
     * Main method tests this class
     *
     * @param args Single integer representing iterations the tree should go
     */
    public static void main(String[] args) {
        int its = Integer.parseInt(args[0]);
        Board board = new Board();
        Player random = new MonteCarlo(1, board, Color.RED, 2, its, "-v");
        Player human = new Human(2, board, Color.YELLOW);

        // a few moves to make sure everything works fine
        for (int i = 0; i < 30; i++) {
            random.move();
            System.out.println(board);
            human.move();
        }
    }
}

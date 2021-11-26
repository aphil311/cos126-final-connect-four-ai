import java.awt.*;

public class MonteCarlo extends Player {
    private int opp;

    public MonteCarlo(int val, Board board, Color color, int opp) {
        super(val, board, color);
        this.opp = opp;
    }

    public void move() {
        // create root of tree
        

        // for certain number of iterations

        // create nodes
        Node root = new Node(board, val, opp, true);

        // calculate exploration/exploitation
        

        // choose node

        // if unexplored, roll out

        // if explored, expand nodes
    }

    private class Node() {
        private Board state;
        private int totalVisits;
        private int winValue;
        private Node parent;
        private ArrayList<Node> children;
        private int val, opp;

        private Node(Board board, int val, int opp) {
            this.val = val;
            this.opp = opp;
            this.state = new Board(board);
            children = new ArrayList<Node>();
            getAllStates();
        }
        private void getAllStates() {
            for (int i = 0; i < state.getCols(); i++) {
                if (!state.isFull(i)) {
                    Board newBoard = new Board(state);
                    newBoard.insert(i, val);
                    Node node = new Node(newBoard, opp, val);
                    node.setParent(this);
                    children.add(node);
                }
            }
        }
        private void setParent(Node parent) {
            this.parent = parent;
        }
        private double getTotalVisits() {
            return (double) totalVisits;
        }
        private double calculateUCT() {
            if (totalVisits == 0) return Double.POSITIVE_INFINITY;
            return (double) winValue/totalVisits + 1.41 * Math.sqrt(Math.log(parent.getTotalVisits())/totalVisits);
        }
    }

    public static void main(String[] args) {

    }
}

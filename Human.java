import java.awt.Color;
import java.util.Scanner;

/**
 * Play Connect Four with user input
 */
public class Human extends Player {
    Scanner scan;

    /**
     * Creates a new object allowing for user input
     *
     * @param val
     * @param b
     * @param color
     */
    public Human(int val, Board b, Color color) {
        super(val, b, color);
        scan = new Scanner(System.in);
    }

    /**
     * asks the player to choose a column and attempts to play there
     */
    public void move() {
        int column;
        do {
            System.out.print("Column selection: ");
            column = scan.nextInt();
        } while (isIllegalMove(column));
        board.insert(column, this.getVal());
    }

    /**
     * checks whether it is possible to play a given column
     *
     * @param c column in question
     * @return true if move is illegal, false otherwise
     */
    private boolean isIllegalMove(int c) {
        return (c < 0 || c >= board.getCols() || board.isFull(c));
    }

    /**
     * Main method that tests each method in this class
     *
     * @param args Command line arguments (should be empty)
     */
    public static void main(String[] args) {
        Board board = new Board();
        Human player1 = new Human(1, board, Color.RED);
        System.out.println(player1.isIllegalMove(90)); // return true
        System.out.println(player1.isIllegalMove(2)); // return false

        // lets me repeatedly test inputs
        // test column being full by going all the way up one
        for (int i = 0; i < 43; i++) {
            player1.move();
            System.out.println(board);
        }
    }
}

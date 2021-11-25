import java.awt.*;

public class Human extends Player {
    public Human(int val, Board b, int opp, Color color) {
        super(val, b, opp, color);
    }

    /**
     * asks the player to choose a column and attempts to play there
     */
    public void move() {
        int column;
        do {
            StdOut.print("Column selection: ");
            column = StdIn.readInt();
        } while (isIllegalMove(column));
        board.insert(column, val);
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

    public static void main(String[] args) {
        Board board = new Board();
        Human player1 = new Human(1, board, 2, Color.RED);

        // lets me repeatedly test inputs
        for (int i = 0; i < 43; i++) {
            player1.move();
            StdOut.println(board);
        }
    }
}

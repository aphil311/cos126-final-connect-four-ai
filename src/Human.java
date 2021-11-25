import java.awt.*;

public class Human extends Player {
    public Human(int val, Board b, int opp, Color color) {
        super(val, b, opp, color);
    }

    public void move() {
        int column;
        do {
            StdOut.print("Column selection: ");
            column = StdIn.readInt();
        } while (column < 0 || column >= board.getCols() || board.isFull(column));
        board.insert(column, val);
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

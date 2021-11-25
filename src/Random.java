import java.awt.*;

public class Random extends Player {
    public Random(int val, Board board, Color color) {
        super(val, board, color);
    }

    /**
     * randomly chooses a column to play
     */
    public void move() {
        int column;
        do {
            column = StdRandom.uniform(board.getCols());
            // column = (int) (Math.random() * board.getCols());
        } while (board.isFull(column));
        board.insert(column, val);
    }

    public static void main(String[] args) {
        Board board = new Board();
        Player random = new Random(1, board, Color.RED);
        Player human = new Human(2, board, Color.YELLOW);
        // a few moves to make sure everything works fine
        for (int i = 0; i < 7; i++) {
            random.move();
            System.out.println(board);
            human.move();
        }
    }
}

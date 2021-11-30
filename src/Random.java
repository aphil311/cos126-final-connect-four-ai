import java.awt.*;

public class Random extends Player {
    private final int opp;

    public Random(int val, Board board, Color color, int opp) {
        super(val, board, color);
        this.opp = opp;
    }

    /**
     * randomly chooses a column to play
     */
    public void move() {
        board.insert(educatedRandom(board.getBoard(), board.getHeights()), val);
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

    public static void main(String[] args) {
        Board board = new Board();
        Player random = new Random(1, board, Color.RED, 2);
        Player human = new Human(2, board, Color.YELLOW);
        // a few moves to make sure everything works fine
        for (int i = 0; i < 7; i++) {
            random.move();
            System.out.println(board);
            human.move();
        }
    }
}

/**
 * Randomly plays ConnectFour with the exception of blocking when an opponent
 * has three in a row.
 */
public class Random extends Player {
    private final int opp; // integer representation of the opponent

    /**
     * Creates a new opponent that plays approximately randomly.
     *
     * @param val   Integer value representing this player
     * @param board Board to be played on/edited
     * @param opp   Inter representation of the opponent
     */
    public Random(int val, Board board, int opp) {
        super(val, board);
        this.opp = opp;
    }

    /**
     * randomly chooses a column to play
     */
    public void move() {
        board.insert(educatedRandom(board.getBoard(), board.getHeights()),
                     this.getVal());
    }

    /**
     * Attempt to block potential win and will complete a four in a row if
     * available.
     *
     * @param board  Board that is currently being played on
     * @param height One dimensional array storing the current heights of each
     *               column
     * @return Column that should be played
     */
    private int educatedRandom(int[][] board, int[] height) {
        // check every column for an opponent win
        for (int i = 0; i < height.length; i++) {
            Board boardCopy = new Board(board, height);
            if (!boardCopy.isFull(i)) {
                boardCopy.insert(i, opp);
                if (boardCopy.checkWinner() != -1)
                    return i;
            }
        }
        // check every column for a potential win
        for (int i = 0; i < height.length; i++) {
            Board boardCopy = new Board(board, height);
            if (!boardCopy.isFull(i)) {
                boardCopy.insert(i, this.getVal());
                if (boardCopy.checkWinner() != -1)
                    return i;
            }
        }

        // otherwise pick a random column
        Board boardCopy = new Board(board, height);
        int attempt;
        do {
            // StdRandom does not play nice with JavaFx
            attempt = (int) (Math.random() * 7);
        } while (boardCopy.isFull(attempt));

        return attempt;
    }

    /**
     * Main method to check every method
     *
     * @param args Command line arguments (should be empty for this class)
     */
    public static void main(String[] args) {
        Board board = new Board();
        Player random = new Random(1, board, 2);
        Player human = new Human(2, board);

        // a few moves to make sure everything works fine
        for (int i = 0; i < 7; i++) {
            random.move();
            System.out.println(board);
            human.move();
        }
    }
}

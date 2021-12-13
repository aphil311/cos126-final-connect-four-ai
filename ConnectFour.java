import java.awt.Color;

/**
 * Play Connect Four in the command line!
 */
public class ConnectFour {
    /**
     * Primary game loop
     *
     * @param args "ai" for Monte Carlo, "easy" for random, anything else for
     *             human
     */
    public static void main(String[] args) {
        // initialize board and players
        Board board = new Board();
        Player player1 = new Human(1, board, Color.RED);
        Player player2;
        String arg1 = "";
        if (args.length > 1)
            arg1 = args[1];

        // takes command line argument to determine opponent
        if (args.length > 0 && args[0].equals("easy"))
            player2 = new Random(2, board, Color.YELLOW, 1);
        else if (args.length > 0 && args[0].equals("ai"))
            player2 = new MonteCarlo(2, board, Color.YELLOW, 1, 25000, arg1);
        else
            player2 = new Human(2, board, Color.YELLOW);

        Player[] players = { player1, player2 };
        int i = 0;

        // main game loop
        while (!board.isFull() && board.checkWinner() == -1) {
            System.out.println(board);
            // debugging
            players[i].move();
            // not the prettiest but it works
            if (i == 1)
                i = 0;
            else
                i = 1;
        }

        // print the winner
        if (board.checkWinner() == player1.getVal())
            System.out.println("Player 1 wins!");
        else if (board.checkWinner() == player2.getVal())
            System.out.println("Player 2 wins!");
        else
            System.out.println("There was a draw!");
        System.out.println(board);
    }
}

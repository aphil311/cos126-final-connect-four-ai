import java.awt.*;

public class ConnectFour {
    public static void main(String[] args) {
        // initialize board and players
        Board board = new Board();
        Player player1 = new Human(1, board, Color.RED);
        Player player2;

        // takes command line argument to determine opponent
        if (args[0].equals("easy"))
            player2 = new Random(1, board, Color.RED);
        else
            player2 = new Human(1, board, Color.RED);

        Player[] players = {player1, player2};
        int i = 0;

        // main game loop
        while (!board.isFull() && board.checkWinner() == -1) {
            StdOut.println(board);
            players[i].move();
            // not the prettiest but it works
            if (i == 1)
                i = 0;
            else
                i = 1;
        }

        // print the winner
        if (board.checkWinner() == player1.getVal())
            StdOut.println("Player 1 wins!");
        else if (board.checkWinner() == player2.getVal())
            StdOut.println("Player 2 wins!");
        else
            StdOut.println("There was a draw!");
    }
}

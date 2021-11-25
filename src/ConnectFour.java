import java.awt.*;

public class ConnectFour {
    public static void main(String[] args) {
        // initialize board and players
        Board board = new Board();
        Player player1 = new Human(1, board, 2, Color.RED);
        Player player2 = new Human(2, board, 1, Color.YELLOW);
        Player[] players = {player1, player2};
        int i = 0;

        // main game loop
        while (!board.isFull() && board.checkWinner() == -1) {
            StdOut.println(board);
            players[i].move();
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

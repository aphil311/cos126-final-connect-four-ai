public class Board {
    // two dimensional array representation of the connect four board
    private int[][] board = new int[6][7];
    // array containing height of the stacks of each column is also index of next placement
    private int[] height = {5, 5, 5, 5, 5, 5, 5};

    public Board() {
    }

    /**
     * checks whether or not a certain column is full
     *
     * @param j the column that should be checked
     * @return true if column is full, false otherwise
     */
    public boolean isFull(int j) {
        if (j >= board[0].length || j < 0) throw new IllegalArgumentException("isFull parameter not within board size");
        return height[j] == -1;
    }


    /**
     * inserts a players piece into a column
     *
     * @param c the column the player wishes to play in
     * @param p the integer representation of the player
     */
    public void insert(int c, int p) {
        if (c >= board[0].length || c < 0) throw new IllegalArgumentException("insert parameter not within board size");
        if (this.isFull(c)) throw new IllegalArgumentException("cannot insert ... column is already full");
        board[height[c]][c] = p;
        --height[c];
    }

    /**
     * checks if the entire board is full
     *
     * @return true if no more playable spaces, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < board.length; i++) {
            if (!isFull(i)) return false;
        }
        return true;
    }

    /**
     * check if a player has won the game
     *
     * @return -1 if no winner, otherwise return value of the winning player
     */
    public int checkWinner() {
        if (checkRows() != -1)
            return checkRows();
        else if (checkColumns() != 1)
            return checkColumns();
        else
            return checkDiags();
    }

    /**
     * checks each row
     * assumes that there are not multiple winners (holds true if we check after each move)
     *
     * @return integer representation of the winning player otherwise return -1
     */
    private int checkRows() {
        int check = -1;
        int count = 0;
        for (int[] ints : board) {
            for (int anInt : ints) {
                // if next piece is same as last one
                if (anInt == check) {
                    count++;
                    if (count == 4) return check;
                } else {
                    count = 1;
                    if (anInt != 0) check = anInt;
                }
            }
            count = 0;
            check = -1;
        }
        return -1;
    }

    /**
     * checks each column
     * assumes that there are not multiple winners (holds true if we check after each move)
     *
     * @return integer representation of the winning player otherwise return -1
     */
    private int checkColumns() {
        int check = -1;
        int count = 0;
        for (int j = 0; j < board[0].length; j++) {
            for (int[] ints : board) {
                // if next piece is same as last one
                if (ints[j] == check) {
                    count++;
                    if (count == 4) return check;
                } else {
                    count = 1;
                    if (ints[j] != 0) check = ints[j];
                }
            }
        }
        return -1;
    }

    /**
     * checks each possible diagonal
     * assumes that there are not multiple winners (holds true if we check after each move)
     *
     * @return integer representation of the winning player otherwise return -1
     */
    private int checkDiags() {
        int check = -1;
        int count = 0;
        // TODO: write this entire method ... oof

        return -1;
    }

    /**
     * accessor method
     *
     * @return number of columns
     */
    public int getCols() {
        return board[0].length;
    }

    /**
     * @return string representation of the board
     */
    public String toString() {
        StringBuilder statement = new StringBuilder();
        for (int[] ints : board) {
            for (int anInt : ints) {
                statement.append(anInt).append(" ");
            }
            statement.append("\n");
        }
        return statement.toString();
    }

    public static void main(String[] args) {
        Board board1 = new Board();
        board1.insert(1, 1);
        board1.insert(1, 2);
        StdOut.println(board1);     // should have a 1 and a 2 in the second column
        board1.insert(1, 1);
        board1.insert(1, 1);
        board1.insert(1, 1);
        board1.insert(1, 1);
        StdOut.println(board1);     // should have four 1s stacked on top
        Board board2 = new Board();
        board2.insert(0, 1);
        board2.insert(1, 1);
        board2.insert(2, 1);
        StdOut.println("Winner is: " + board2.checkWinner());
        StdOut.println(board2);
        board2.insert(3, 1);
        StdOut.println("Winner is: " + board2.checkWinner());
        Board board3 = new Board();
        board3.insert(0, 2);
        board3.insert(0, 2);
        board3.insert(0, 2);
        board3.insert(0, 2);
        StdOut.println("Winner is: " + board3.checkWinner());
        board1.insert(1, 1);            // illegal argument exception
    }
}

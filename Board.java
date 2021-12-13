import java.util.Arrays;

/**
 * Object that represents a Connect Four board and handles logic.
 */
public class Board {
    // two dimensional array representation of the connect four board
    private int[][] board;
    // array containing height of the stacks of each column is also index of
    // next placement
    private int[] height;

    /**
     * Stores all
     */
    public Board() {
        board = new int[6][7];
        height = new int[7];
        Arrays.fill(height, 5);
    }

    /**
     * Creates a new board objet from an existing one by taking a 2d
     * representation and a height array.
     *
     * @param board  2d Array representing the board
     * @param height Array representing the current height of each column
     */
    public Board(int[][] board, int[] height) {
        if (board.length != 6 && board[0].length != 7)
            throw new IllegalArgumentException("board is the incorrect size");
        // copy the board
        this.board = new int[6][7];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[0].length);
        }
        if (height.length != board[0].length)
            throw new IllegalArgumentException("height length does not match "
                                                       + "board size");
        // copy the height array
        this.height = new int[board[0].length];
        for (int k = 0; k < height.length; k++) {
            this.height[k] = height[k];
        }
    }

    /**
     * Checks whether or not a certain column is full.
     *
     * @param j the column that should be checked
     * @return true if column is full, false otherwise
     */
    public boolean isFull(int j) {
        if (j >= board[0].length || j < 0)
            throw new IllegalArgumentException(
                    "isFull parameter not within board size");
        return height[j] == -1;
    }

    /**
     * Inserts a players piece into a column.
     *
     * @param c the column the player wishes to play in
     * @param p the integer representation of the player
     */
    public int[] insert(int c, int p) {
        if (c >= board[0].length || c < 0) throw new IllegalArgumentException(
                "insert parameter not within board size");
        if (this.isFull(c)) throw new IllegalArgumentException(
                "cannot insert ... column is already full");
        board[height[c]][c] = p;    // insert piece into array
        int[] coordinates = { c, height[c] };
        --height[c];    // adjust height
        return coordinates;
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
        else if (checkColumns() != -1)
            return checkColumns();
        else
            return checkDiags();
    }

    /**
     * checks each row
     * assumes that there are not multiple winners (holds true if we check
     * after each move)
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
                }
                else {
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
     * assumes that there are not multiple winners (holds true if we check
     * after each move)
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
                }
                else {
                    count = 1;
                    if (ints[j] != 0) check = ints[j];
                }
            }
            count = 0;
            check = -1;
        }
        return -1;
    }

    /**
     * checks each possible diagonal
     * assumes that there are not multiple winners (holds true if we check
     * after each move)
     *
     * @return integer representation of the winning player otherwise return -1
     */
    private int checkDiags() {
        /*
        NOTE: this one was pretty brutal to write, also though it is not
        explicit in test method each possibility has been tested
        */
        int check = -1;
        int count = 0;
        // bottom left to top right bounded by left side
        for (int k = 3; k < board.length; k++) {
            int j = 0;
            for (int i = k; i >= 0; i--) {
                // System.out.println("[" + i + "][" + j + "]");
                int num = board[i][j];
                if (num == check) {
                    count++;
                    if (count == 4) return check;
                }
                else {
                    count = 1;
                    if (num != 0) check = num;
                }
                j++;
            }
            count = 0;
            check = -1;
        }
        check = -1;
        count = 0;
        // bottom left to top right bounded by bottom
        for (int k = 1; k < 4; k++) {
            int i = 5;
            for (int j = k; j < board[0].length; j++) {
                if (board[i][j] == check) {
                    count++;
                    if (count == 4) return check;
                }
                else {
                    count = 1;
                    if (board[i][j] != 0) check = board[i][j];
                }
                i--;
            }
            count = 0;
            check = -1;
        }
        check = -1;
        count = 0;
        // bottom right to top left bounded by right side
        for (int k = 3; k < board.length; k++) {
            int j = 6;
            for (int i = k; i >= 0; i--) {
                int num = board[i][j];
                if (num == check) {
                    count++;
                    if (count == 4) return check;
                }
                else {
                    count = 1;
                    if (num != 0) check = num;
                }
                j--;
            }
            count = 0;
            check = -1;
        }
        check = -1;
        count = 0;
        // bottom right to top left bounded by bottom
        for (int k = 5; k >= 3; k--) {
            int i = 5;
            for (int j = k; j >= 0; j--) {
                if (board[i][j] == check) {
                    count++;
                    if (count == 4) return check;
                }
                else {
                    count = 1;
                    if (board[i][j] != 0) check = board[i][j];
                }
                i--;
            }
            count = 0;
            check = -1;
        }

        return -1;
    }

    /**
     * Accessor method to get the number of columns.
     *
     * @return number of columns
     */
    public int getCols() {
        return board[0].length;
    }

    /**
     * Accessor method to get the value stored in the board given an x and y
     * value.
     *
     * @param x
     * @param y
     * @return
     */
    public int getCell(int x, int y) {
        return board[y][x];
    }

    /**
     * Return a defensive copy of the height array
     *
     * @return integer array containing height of each column
     */
    public int[] getHeights() {
        return Arrays.copyOf(height, height.length);
    }

    /**
     * Return a defensive copy of the board array
     *
     * @return 2d integer array representation of the board
     */
    public int[][] getBoard() {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, board[0].length);
        }
        return newBoard;
    }

    /**
     * Returns a string representation of the board for CLI or debugging
     *
     * @return string representation of the board
     */
    public String toString() {
        StringBuilder statement = new StringBuilder();
        for (int[] ints : board) {
            statement.append("|");
            for (int anInt : ints) {
                if (anInt == 1)
                    statement.append("X|");
                else if (anInt == 2)
                    statement.append("O|");
                else
                    statement.append(" |");
            }
            statement.append("\n");
        }
        return statement.toString() + " 0 1 2 3 4 5 6 ";
    }

    /**
     * Tester method to test each method in this class
     *
     * @param args
     */
    public static void main(String[] args) {
        Board board1 = new Board();
        board1.insert(1, 1);
        board1.insert(1, 2);
        // should have a 1 and a 2 in the second column
        System.out.println(board1);
        board1.insert(1, 1);
        board1.insert(1, 1);
        board1.insert(1, 1);
        board1.insert(1, 1);
        System.out.println(board1);     // should have four 1s stacked on top
        Board board2 = new Board();
        board2.insert(0, 1);
        board2.insert(1, 1);
        board2.insert(2, 1);
        System.out.println("Winner is: " + board2.checkWinner());
        System.out.println(board2);
        board2.insert(3, 1);
        System.out.println("Winner is: " + board2.checkWinner());
        Board board3 = new Board();
        board3.insert(0, 2);
        board3.insert(0, 2);
        board3.insert(0, 2);
        board3.insert(0, 2);
        System.out.println("Winner is: " + board3.checkWinner());
        Board board4 = new Board(board3.getBoard(), board3.getHeights());
        System.out.println("Board 4\n" + board4);
        board4.insert(1, 6);
        System.out.println("Board 3\n" + board4);
        System.out.println(board4.getCols());
        System.out.println(board4.getCell(1, 1));
        System.out.println(board4.getHeights()[2]);
        System.out.println(board4.getBoard()[1][5]);
        board1.insert(1, 1);            // illegal argument exception
    }
}

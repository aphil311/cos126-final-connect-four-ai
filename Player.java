/**
 * Abstract class storing critical information to a player and an abstract
 * move method to be implemented.
 */
abstract class Player {
    private final int val;        // integer representation of this player
    Board board;            // board to modify

    public Player(int val, Board b) {
        this.val = val;
        board = b;
    }

    abstract void move();

    /**
     * accessor method for the val variable
     *
     * @return the integer representation of this player
     */
    public int getVal() {
        return val;
    }
}

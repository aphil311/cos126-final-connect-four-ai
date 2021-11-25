import java.awt.*;

abstract class Player {
    protected final int val;        // integer representation of this player
    private final Color color;    // color assigned to this player
    Board board;            // board to modify

    public Player(int val, Board b, Color color) {
        this.val = val;
        board = b;
        this.color = color;
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

    /**
     * accessor method for the color variable
     *
     * @return the color assigned to this player
     */
    public int getColor() {
        return val;
    }
}

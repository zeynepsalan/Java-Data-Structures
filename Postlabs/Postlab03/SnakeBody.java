/**
 * CSE201 Post-Lab 3 - provided interface. Do not modify or submit it.
 *
 * The sequence of board cells the snake occupies, from the head to the tail.
 * Both SlowSnakeBody and your FastSnakeBody implement it, and the game works
 * with either one.
 */
public interface SnakeBody {

    /** The number of segments. */
    int size();

    /** True only when there are no segments. */
    boolean isEmpty();

    /** The first segment, or null when the body is empty. */
    Position getHead();

    /** The last segment, or null when the body is empty. */
    Position getTail();

    /**
     * Adds p as the new first segment. A null position is ignored; a position
     * that is not on the board causes IllegalArgumentException.
     */
    void addHead(Position p);

    /** Removes and returns the last segment, or null when the body is empty. */
    Position removeTail();

    /**
     * True when at least one segment is at p. A null position, or one that is
     * not on the board, returns false.
     */
    boolean contains(Position p);

    /** A new array of all segments, from head to tail. */
    Position[] toArray();

    /** Removes all segments. The body can be used again afterwards. */
    void clear();
}

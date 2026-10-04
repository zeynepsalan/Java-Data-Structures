/**
 * CSE201 Post-Lab 3 - provided board coordinate. Do not modify or submit it.
 *
 * A Position is immutable: once created, its column and row never change, so a
 * position may be stored and compared freely. Two positions are equal when both
 * coordinates are equal.
 */
public final class Position {

    private final int col;
    private final int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    /** The column, counted from 0 at the left edge. */
    public int col() {
        return col;
    }

    /** The row, counted from 0 at the top edge. */
    public int row() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!Position.class.isInstance(o)) {
            return false;
        }
        Position other = (Position) o;
        return col == other.col && row == other.row;
    }

    @Override
    public int hashCode() {
        return 31 * col + row;
    }

    @Override
    public String toString() {
        return "(" + col + ", " + row + ")";
    }
}

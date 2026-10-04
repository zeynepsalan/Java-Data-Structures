/**
 * CSE201 Post-Lab 3 - last year's working snake body. Read it, but do not
 * submit it and do not call it from FastSnakeBody.
 *
 * It stores the segments in a singly linked list that starts at the head. That
 * is enough to make the game correct, and it is where the stutter comes from:
 * three of the operations the game calls every tick walk the whole list.
 *
 * Your job is to keep every result exactly the same while making each of those
 * operations take the same amount of time no matter how long the snake is.
 */
public class SlowSnakeBody implements SnakeBody {

    private final int columns;
    private final int rows;

    private SlowNode head;
    private int size;

    public SlowSnakeBody(int columns, int rows) {
        if (columns <= 0 || rows <= 0) {
            throw new IllegalArgumentException(
                "the board must have at least one column and one row");
        }
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Position getHead() {
        return head == null ? null : head.position;
    }

    /** Walks the whole list to reach the last node. */
    @Override
    public Position getTail() {
        if (head == null) {
            return null;
        }
        SlowNode node = head;
        while (node.next != null) {
            node = node.next;
        }
        return node.position;
    }

    @Override
    public void addHead(Position p) {
        if (p == null) {
            return;
        }
        if (!onBoard(p)) {
            throw new IllegalArgumentException("position is not on the board: " + p);
        }
        head = new SlowNode(p, head);
        size++;
    }

    /** Walks the whole list to reach the node before the last one. */
    @Override
    public Position removeTail() {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            Position only = head.position;
            head = null;
            size = 0;
            return only;
        }
        SlowNode beforeLast = head;
        while (beforeLast.next.next != null) {
            beforeLast = beforeLast.next;
        }
        Position last = beforeLast.next.position;
        beforeLast.next = null;
        size--;
        return last;
    }

    /** Walks the list until it finds the position. */
    @Override
    public boolean contains(Position p) {
        if (p == null || !onBoard(p)) {
            return false;
        }
        for (SlowNode node = head; node != null; node = node.next) {
            if (node.position.equals(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Position[] toArray() {
        Position[] all = new Position[size];
        int i = 0;
        for (SlowNode node = head; node != null; node = node.next) {
            all[i] = node.position;
            i++;
        }
        return all;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    private boolean onBoard(Position p) {
        return p.col() >= 0 && p.col() < columns && p.row() >= 0 && p.row() < rows;
    }
}

/**
 * One segment of a SlowSnakeBody, linked toward the tail.
 *
 * It is a top-level class, not a class declared inside SlowSnakeBody: no class in this
 * course is ever declared inside another one. It is called SlowNode rather than Node so
 * that the node class you write in FastSnakeBody.java can have the obvious name without
 * clashing with this one when both files are compiled together.
 */
class SlowNode {

    final Position position;
    SlowNode next;

    SlowNode(Position position, SlowNode next) {
        this.position = position;
        this.next = next;
    }
}

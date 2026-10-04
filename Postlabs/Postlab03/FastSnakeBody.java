/**
 * CSE201 Post-Lab 3 - your deliverable.
 *
 * Read TODO.md before you start. FastSnakeBody must behave exactly like
 * SlowSnakeBody, but size, isEmpty, getHead, getTail, addHead, removeTail and
 * contains must all take the same amount of time no matter how long the snake
 * is. Only toArray and clear may depend on the length.
 *
 * No java.util collection may be used. Arrays and your own node class are
 * allowed, and both must be declared in this file.
 */
class FastNode {
    final Position position;
    FastNode next;
    FastNode prev;

    public FastNode(Position position, FastNode next, FastNode prev) {
        this.position = position;
        this.next = next;
        this.prev = prev;
    }
}

public class FastSnakeBody implements SnakeBody {
    private final int columns;
    private final int rows;

    private FastNode head;
    private FastNode tail;
    private int size;
    int[][] kordinat;

    public FastSnakeBody(int columns, int rows) {
        if (columns <= 0 || rows <= 0)
            throw new IllegalArgumentException();
        this.columns = columns;
        this.rows = rows;
        kordinat = new int[rows][columns];
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
        return isEmpty() ? null : head.position;
    }

    @Override
    public Position getTail() {
        return isEmpty() ? null : tail.position;
    }

    @Override
    public void addHead(Position p) {
        if (p == null) {
            return;
        }
        if (p.row() < 0 || p.row() >= rows || p.col() < 0 || p.col() >= columns)
            throw new IllegalArgumentException();
        if (head == null) {
            FastNode node = new FastNode(p, null, null);
            tail = node;
            head = node;
            size++;
            kordinat[p.row()][p.col()]++;
            return;
        }
        FastNode node = new FastNode(p, head, null);
        head.prev = node;
        head = node;
        kordinat[p.row()][p.col()]++;
        size++;
    }

    @Override
    public Position removeTail() {
        if (size == 0) {
            return null;
        }
        Position p = tail.position;
        if (size == 1) {
            tail = null;
            head = null;
            size = 0;
            kordinat[p.row()][p.col()]--;
            return p;
        }
        FastNode deletedprev = tail.prev;
        deletedprev.next = null;
        tail = deletedprev;
        size--;
        kordinat[p.row()][p.col()]--;
        return p;
    }

    @Override
    public boolean contains(Position p) {
        if (p == null || p.row() < 0 || p.row() >= rows || p.col() < 0 || p.col() >= columns)
            return false;
        return kordinat[p.row()][p.col()] > 0;
    }

    @Override
    public Position[] toArray() {
        Position[] p = new Position[size];
        if (head == null) {
            return p;
        }
        int i = 0;
        for (FastNode node = head; node != null; node = node.next, i++) {
            p[i] = node.position;
        }
        return p;
    }

    @Override
    public void clear() {
        FastNode node = head;
        for (int i = 0; i < size; i++, node = node.next) {
            kordinat[node.position.row()][node.position.col()] = 0;
        }
        head = null;
        tail = null;
        size = 0;
    }
}

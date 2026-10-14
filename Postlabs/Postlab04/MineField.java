/**
 * CSE201 Post-Lab 4 - your deliverable.
 *
 * Read TODO.md before you start. The internal representation is up to you, for
 * example a few parallel boolean[][] grids.
 *
 * The cascade in reveal must be recursive. Do not simulate it with a Stack,
 * Queue, Deque, LinkedList or any other collection. Loops are fine elsewhere,
 * for example when counting neighbouring mines or building the view.
 *
 * Methods that receive coordinates outside the board must not throw; they
 * return the value TODO.md states. Do not add a package declaration.
 */
public class MineField {
    boolean[][] mines;
    boolean[][] revealed;
    boolean[][] flagged;
    boolean Lost;

    /** mines[r][c] is true when the cell holds a mine. Store your own copy. */
    public MineField(boolean[][] mines) {
        if (mines == null || mines.length == 0 || mines[0].length == 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < mines.length - 1; i++) {
            if (mines[i].length != mines[i + 1].length) {
                throw new IllegalArgumentException();
            }
        }
        this.mines = new boolean[mines.length][mines[0].length];
        for (int r = 0; r < mines.length; r++) {
            for (int c = 0; c < mines[0].length; c++) {
                this.mines[r][c] = mines[r][c];
            }
        }
        revealed = new boolean[rows()][cols()];
        flagged = new boolean[rows()][cols()];
    }

    public int rows() {
        return mines.length;
    }

    public int cols() {
        return mines[0].length;
    }

    public boolean isMine(int r, int c) {
        if (r < 0 || r >= mines.length || c < 0 || c >= mines[0].length) {
            return false;
        }
        return mines[r][c];
    }

    /** Mines among the up to eight neighbours; -1 for invalid coordinates. */
    public int adjacentMines(int r, int c) {
        if (r < 0 || r >= mines.length || c < 0 || c >= mines[0].length) {
            return -1;
        }
        int count = 0;
        for (int a = -1; a < 2; a++) {
            for (int b = -1; b < 2; b++) {
                if (a == 0 && b == 0)
                    continue;
                if (isMine(r + a, c + b)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isRevealed(int r, int c) {
        if (r < 0 || r >= mines.length || c < 0 || c >= mines[0].length) {
            return false;
        }
        return revealed[r][c];
    }

    public boolean isFlagged(int r, int c) {
        if (r < 0 || r >= mines.length || c < 0 || c >= mines[0].length) {
            return false;
        }
        return flagged[r][c];
    }

    /** Flags or unflags a hidden cell and returns the new flag state. */
    public boolean toggleFlag(int r, int c) {
        if (r < 0 || r >= mines.length || c < 0 || c >= mines[0].length) {
            return false;
        }
        if (isRevealed(r, c) || isLost() || isWon()) {
            return false;
        }
        flagged[r][c] = !isFlagged(r, c);
        return flagged[r][c];
    }

    /** Reveals cells and returns how many became revealed by this call. */
    public int reveal(int r, int c) {
        if (r < 0 || r >= mines.length || c < 0 || c >= mines[0].length) {
            return 0;
        }
        if (revealed[r][c] || isFlagged(r, c) || isLost() || isWon())
            return 0;
        if (isMine(r, c)) {
            Lost = true;
            revealed[r][c] = true;
            return 1;
        }

        int count = adjacentMines(r, c);
        revealed[r][c] = true;
        int counter = 1;
        if (count == 0) {
            for (int a = -1; a < 2; a++) {
                for (int b = -1; b < 2; b++) {
                    if (a == 0 && b == 0)
                        continue;
                    counter += reveal(r + a, c + b);
                }
            }
        }

        return counter;

    }

    public boolean isLost() {
        return Lost;
    }

    public boolean isWon() {
        return hiddenSafeCells() == 0 && !isLost();
    }

    public int hiddenSafeCells() {
        int hiddenSafeCells = 0;
        for (int a = 0; a < rows(); a++) {
            for (int b = 0; b < cols(); b++) {
                if (!isRevealed(a, b) && !isMine(a, b)) {
                    hiddenSafeCells++;
                }
            }
        }
        return hiddenSafeCells;
    }

    /** A new array describing every cell: '#', 'F', '*' or '0' to '8'. */
    public char[][] view() {
        char[][] minefield = new char[rows()][cols()];
        for (int r = 0; r < rows(); r++) {
            for (int c = 0; c < cols(); c++) {
                if (!isRevealed(r, c) && !isFlagged(r, c)) {
                    minefield[r][c] = '#';
                } else if (!isRevealed(r, c) && isFlagged(r, c)) {
                    minefield[r][c] = 'F';
                } else if (isRevealed(r, c) && !isMine(r, c)) {
                    int a = adjacentMines(r, c);
                    minefield[r][c] = (a + "").charAt(0);
                } else if (isRevealed(r, c)) {
                    minefield[r][c] = '*';
                }
            }
        }
        return minefield;
    }
}

import java.util.Random;

/**
 * CSE201 Post-Lab 4 - provided mine-layout generator.
 * Do not modify or submit this file.
 *
 * Every layout comes from a seed, so a board can be reproduced exactly. The
 * game window shows the seed in its title bar.
 */
public final class BoardGenerator {

    private BoardGenerator() {
    }

    /** A layout with the given number of mines placed at random. */
    public static boolean[][] generate(int rows, int cols, int mines, long seed) {
        return generateAvoiding(rows, cols, mines, seed, -1, -1);
    }

    /**
     * The same, except that the cell (safeRow, safeCol) never holds a mine.
     * The game uses it so the first click is always safe. Pass -1, -1 when no
     * cell has to stay free.
     */
    public static boolean[][] generateAvoiding(int rows, int cols, int mines, long seed,
                                               int safeRow, int safeCol) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("the board must have at least one cell");
        }
        int cells = rows * cols;
        boolean hasSafeCell = safeRow >= 0 && safeRow < rows && safeCol >= 0 && safeCol < cols;
        int placeable = hasSafeCell ? cells - 1 : cells;
        if (mines < 0 || mines > placeable) {
            throw new IllegalArgumentException(
                "cannot place " + mines + " mines in " + placeable + " cells");
        }

        boolean[][] layout = new boolean[rows][cols];
        Random random = new Random(seed);
        int placed = 0;
        while (placed < mines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            if (layout[r][c] || (hasSafeCell && r == safeRow && c == safeCol)) {
                continue;
            }
            layout[r][c] = true;
            placed++;
        }
        return layout;
    }
}

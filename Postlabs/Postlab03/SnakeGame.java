import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * CSE201 Post-Lab 3 - the Snake game. Provided and complete.
 * Do not modify or submit this file.
 *
 * The game never depends on which SnakeBody it is using: the Implementation
 * menu swaps SlowSnakeBody for your FastSnakeBody and nothing else changes.
 *
 * Marathon mode is the measurement: a 300 x 300 board with an autopilot snake
 * that grows on every tick, and a graph of how long each tick took. With the
 * slow body the graph climbs as the snake gets longer; with a constant-time
 * body it stays flat.
 *
 * Every call into a body is wrapped, so an exception from FastSnakeBody stops
 * the game with a readable message instead of a stack trace in the console.
 */
public class SnakeGame extends JFrame {

    private static final long serialVersionUID = 1L;

    private enum Impl { SLOW, FAST }

    private enum Mode { CLASSIC, MARATHON }

    private static final int CLASSIC_COLUMNS = 22;
    private static final int CLASSIC_ROWS = 18;
    private static final int CLASSIC_CELL = 26;
    private static final int CLASSIC_DELAY_MS = 110;

    private static final int MARATHON_SIZE = 300;
    private static final int MARATHON_CELL = 2;
    private static final int MARATHON_DELAY_MS = 1;

    private static final int GRAPH_SAMPLES = 240;
    private static final int GRAPH_HEIGHT = 90;

    private static final Color BACKGROUND = new Color(18, 24, 22);
    private static final Color GRID = new Color(28, 38, 35);
    private static final Color SNAKE = new Color(86, 196, 134);
    private static final Color SNAKE_HEAD = new Color(198, 244, 216);
    private static final Color FOOD = new Color(228, 96, 92);
    private static final Color TEXT = new Color(228, 238, 234);
    private static final Color GRAPH_LINE = new Color(240, 190, 96);

    private Impl implementation = Impl.SLOW;
    private Mode mode = Mode.CLASSIC;

    private int columns = CLASSIC_COLUMNS;
    private int rows = CLASSIC_ROWS;
    private int cell = CLASSIC_CELL;

    private SnakeBody body;
    private Position food;
    private int dirCol = 1;
    private int dirRow;
    private int pendingDirCol = 1;
    private int pendingDirRow;
    private int score;

    private boolean running;
    private boolean paused;
    private boolean gameOver;
    private String failure;

    private final Random random = new Random(2026);
    private final Timer timer = new Timer(CLASSIC_DELAY_MS, e -> tick());

    /** Tick durations in microseconds, oldest first. */
    private final long[] frameTimes = new long[GRAPH_SAMPLES];
    private int frameCount;

    private final BoardPanel boardPanel = new BoardPanel();
    private final JLabel statusBar = new JLabel(" ");

    public SnakeGame() {
        super("Snake - CSE201 Post-Lab 3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(buildMenuBar());

        statusBar.setForeground(TEXT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        JPanel statusHolder = new JPanel(new BorderLayout());
        statusHolder.setBackground(BACKGROUND);
        statusHolder.add(statusBar, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(statusHolder, BorderLayout.SOUTH);

        boardPanel.setFocusable(true);
        boardPanel.addKeyListener(new Keys());

        newGame();
        pack();
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------ menu

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu game = new JMenu("Game");
        JMenuItem restart = new JMenuItem("New game");
        restart.addActionListener(e -> newGame());
        game.add(restart);
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(e -> dispose());
        game.add(quit);
        bar.add(game);

        JMenu impl = new JMenu("Implementation");
        ButtonGroup implGroup = new ButtonGroup();
        impl.add(implItem("Slow (SlowSnakeBody)", Impl.SLOW, implGroup, true));
        impl.add(implItem("Fast (FastSnakeBody)", Impl.FAST, implGroup, false));
        bar.add(impl);

        JMenu modes = new JMenu("Mode");
        ButtonGroup modeGroup = new ButtonGroup();
        modes.add(modeItem("Classic (22 x 18)", Mode.CLASSIC, modeGroup, true));
        modes.add(modeItem("Marathon (300 x 300)", Mode.MARATHON, modeGroup, false));
        bar.add(modes);

        return bar;
    }

    private JRadioButtonMenuItem implItem(String text, Impl value, ButtonGroup group,
                                         boolean selected) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(text, selected);
        group.add(item);
        item.addActionListener(e -> {
            implementation = value;
            newGame();
        });
        return item;
    }

    private JRadioButtonMenuItem modeItem(String text, Mode value, ButtonGroup group,
                                          boolean selected) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(text, selected);
        group.add(item);
        item.addActionListener(e -> {
            mode = value;
            newGame();
        });
        return item;
    }

    // ------------------------------------------------------------- game flow

    private void newGame() {
        timer.stop();
        failure = null;
        gameOver = false;
        paused = false;
        running = false;
        score = 0;
        frameCount = 0;

        if (mode == Mode.CLASSIC) {
            columns = CLASSIC_COLUMNS;
            rows = CLASSIC_ROWS;
            cell = CLASSIC_CELL;
            timer.setDelay(CLASSIC_DELAY_MS);
        } else {
            columns = MARATHON_SIZE;
            rows = MARATHON_SIZE;
            cell = MARATHON_CELL;
            timer.setDelay(MARATHON_DELAY_MS);
        }

        body = createBody();
        if (body == null) {
            boardPanel.resize();
            boardPanel.repaint();
            updateStatus();
            return;
        }

        dirCol = 1;
        dirRow = 0;
        pendingDirCol = 1;
        pendingDirRow = 0;

        if (mode == Mode.CLASSIC) {
            // The starting snake, tail first, so the head ends up on the right.
            final int midRow = rows / 2;
            for (int col = 0; col <= 2; col++) {
                final int c = col;
                if (!safe(() -> body.addHead(new Position(c, midRow)))) {
                    break;
                }
            }
            placeFood();
        } else {
            // Marathon starts at the top-left corner of the serpentine path.
            safe(() -> body.addHead(new Position(0, 0)));
            food = null;
        }
        boardPanel.resize();
        pack();
        boardPanel.repaint();
        boardPanel.requestFocusInWindow();
        updateStatus();

        if (mode == Mode.MARATHON && failure == null) {
            start();
        }
    }

    /**
     * Builds the chosen body. A FastSnakeBody that is not finished yet throws
     * here, so the game reports it and falls back to the slow implementation.
     */
    private SnakeBody createBody() {
        if (implementation == Impl.SLOW) {
            return new SlowSnakeBody(columns, rows);
        }
        try {
            return new FastSnakeBody(columns, rows);
        } catch (Throwable t) {
            failure = "FastSnakeBody(" + columns + ", " + rows + ") -> " + describe(t);
            return new SlowSnakeBody(columns, rows);
        }
    }

    private void start() {
        if (gameOver || failure != null) {
            newGame();
            return;
        }
        running = true;
        paused = false;
        timer.start();
        updateStatus();
    }

    private void togglePause() {
        if (!running || gameOver) {
            return;
        }
        paused = !paused;
        if (paused) {
            timer.stop();
        } else {
            timer.start();
        }
        updateStatus();
    }

    private void tick() {
        long startNanos = System.nanoTime();

        boolean ok = mode == Mode.CLASSIC ? classicStep() : marathonStep();

        long micros = (System.nanoTime() - startNanos) / 1000L;
        frameTimes[frameCount % frameTimes.length] = micros;
        frameCount++;

        if (!ok) {
            timer.stop();
            running = false;
        }
        boardPanel.repaint();
        updateStatus();
    }

    /** One step of the normal game. Returns false when the game has to stop. */
    private boolean classicStep() {
        dirCol = pendingDirCol;
        dirRow = pendingDirRow;

        Position head = get(() -> body.getHead());
        if (failure != null || head == null) {
            return false;
        }

        final Position next = new Position(head.col() + dirCol, head.row() + dirRow);
        if (next.col() < 0 || next.col() >= columns || next.row() < 0 || next.row() >= rows) {
            gameOver = true;
            return false;
        }

        boolean grows = next.equals(food);
        Position tail = get(() -> body.getTail());
        if (failure != null) {
            return false;
        }

        // Moving into the cell the tail is leaving is allowed.
        Boolean occupied = get(() -> Boolean.valueOf(body.contains(next)));
        if (failure != null) {
            return false;
        }
        if (Boolean.TRUE.equals(occupied) && !(!grows && next.equals(tail))) {
            gameOver = true;
            return false;
        }

        // The head is added before the tail is removed, so for a moment the
        // cell the tail is leaving holds two segments.
        if (!safe(() -> body.addHead(next))) {
            return false;
        }
        if (!grows) {
            if (!safe(() -> body.removeTail())) {
                return false;
            }
        } else {
            score++;
            placeFood();
            if (failure != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Marathon: the snake follows a fixed snake-shaped path over the whole board
     * and grows on every tick, so the body only gets longer. Nothing about the
     * path depends on the body, which makes the tick time a clean measurement.
     */
    private boolean marathonStep() {
        Position head = get(() -> body.getHead());
        if (failure != null || head == null) {
            return false;
        }

        int col = head.col();
        int row = head.row();
        boolean leftToRight = row % 2 == 0;
        if (leftToRight && col + 1 < columns) {
            col++;
        } else if (!leftToRight && col > 0) {
            col--;
        } else if (row + 1 < rows) {
            row++;
        } else {
            // The board is full: start over with a short snake.
            if (!safe(() -> body.clear())) {
                return false;
            }
            score = 0;
            return safe(() -> body.addHead(new Position(0, 0)));
        }

        final Position next = new Position(col, row);
        // contains is called every tick here too, exactly as the real game does.
        get(() -> Boolean.valueOf(body.contains(next)));
        if (failure != null) {
            return false;
        }
        score++;
        return safe(() -> body.addHead(next));
    }

    private void placeFood() {
        for (int attempt = 0; attempt < 10000; attempt++) {
            final Position candidate =
                new Position(random.nextInt(columns), random.nextInt(rows));
            Boolean taken = get(() -> Boolean.valueOf(body.contains(candidate)));
            if (failure != null) {
                return;
            }
            if (!Boolean.TRUE.equals(taken)) {
                food = candidate;
                return;
            }
        }
        food = null;
    }

    // --------------------------------------------------------------- helpers

    /** Runs a body call; records the failure and returns false if it throws. */
    private boolean safe(Runnable call) {
        if (failure != null) {
            return false;
        }
        try {
            call.run();
            return true;
        } catch (Throwable t) {
            failure = describe(t);
            return false;
        }
    }

    private interface Query<R> {
        R run();
    }

    private <R> R get(Query<R> call) {
        if (failure != null) {
            return null;
        }
        try {
            return call.run();
        } catch (Throwable t) {
            failure = describe(t);
            return null;
        }
    }

    private static String describe(Throwable t) {
        String message = t.getMessage();
        return t.getClass().getSimpleName() + (message == null ? "" : ": " + message);
    }

    private void updateStatus() {
        if (failure != null) {
            statusBar.setText("FastSnakeBody failed: " + failure
                + "   -   the slow body is being used instead.");
            return;
        }
        String state = gameOver ? "game over - press Enter"
            : !running ? "press Enter or Space to start"
            : paused ? "paused - press P" : "running";
        Integer length = get(() -> Integer.valueOf(body.size()));
        statusBar.setText(
            implementation + " body   -   " + mode.toString().toLowerCase()
            + "   -   length " + (length == null ? "?" : length)
            + "   -   score " + score
            + "   -   " + state
            + (mode == Mode.MARATHON ? "   -   last tick " + lastTickText() : ""));
    }

    private String lastTickText() {
        if (frameCount == 0) {
            return "-";
        }
        long micros = frameTimes[(frameCount - 1) % frameTimes.length];
        return micros + " us";
    }

    // ------------------------------------------------------------- rendering

    private final class BoardPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private BoardPanel() {
            setBackground(BACKGROUND);
            resize();
        }

        private void resize() {
            int graph = mode == Mode.MARATHON ? GRAPH_HEIGHT : 0;
            setPreferredSize(new Dimension(columns * cell, rows * cell + graph));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            drawBoard(g2);
            drawSnake(g2);
            if (mode == Mode.CLASSIC) {
                drawFood(g2);
                drawOverlay(g2);
            } else {
                drawGraph(g2);
            }
        }

        private void drawBoard(Graphics2D g2) {
            g2.setColor(BACKGROUND);
            g2.fillRect(0, 0, getWidth(), getHeight());
            if (mode != Mode.CLASSIC) {
                return;
            }
            g2.setColor(GRID);
            for (int c = 0; c <= columns; c++) {
                g2.drawLine(c * cell, 0, c * cell, rows * cell);
            }
            for (int r = 0; r <= rows; r++) {
                g2.drawLine(0, r * cell, columns * cell, r * cell);
            }
        }

        /**
         * Classic mode draws every segment. Marathon mode draws only the cells
         * inside the visible board area, and only the head is new each tick, so
         * the drawing stays cheap next to the body operations being measured.
         */
        private void drawSnake(Graphics2D g2) {
            if (body == null) {
                return;
            }
            if (mode == Mode.MARATHON) {
                Position head = get(() -> body.getHead());
                if (head == null) {
                    return;
                }
                // Repaint the filled part of the serpentine path directly.
                g2.setColor(SNAKE);
                for (int row = 0; row < head.row(); row++) {
                    g2.fillRect(0, row * cell, columns * cell, cell);
                }
                int filled = head.row() % 2 == 0 ? head.col() + 1 : columns - head.col();
                int x = head.row() % 2 == 0 ? 0 : head.col() * cell;
                g2.fillRect(x, head.row() * cell, filled * cell, cell);
                g2.setColor(SNAKE_HEAD);
                g2.fillRect(head.col() * cell, head.row() * cell, cell, cell);
                return;
            }

            Position[] segments = get(() -> body.toArray());
            if (segments == null) {
                return;
            }
            for (int i = segments.length - 1; i >= 0; i--) {
                Position p = segments[i];
                g2.setColor(i == 0 ? SNAKE_HEAD : SNAKE);
                g2.fillRoundRect(p.col() * cell + 1, p.row() * cell + 1,
                    cell - 2, cell - 2, 8, 8);
            }
        }

        private void drawFood(Graphics2D g2) {
            if (food == null) {
                return;
            }
            g2.setColor(FOOD);
            g2.fillOval(food.col() * cell + 4, food.row() * cell + 4, cell - 8, cell - 8);
        }

        private void drawOverlay(Graphics2D g2) {
            if (running && !paused && !gameOver && failure == null) {
                return;
            }
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, columns * cell, rows * cell);
            g2.setColor(TEXT);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            String text = failure != null ? "FastSnakeBody is not ready"
                : gameOver ? "Game over - Enter to play again"
                : paused ? "Paused - P to continue"
                : "Enter or Space to start";
            int width = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, (columns * cell - width) / 2, rows * cell / 2);
        }

        /** Tick durations over time. A rising line means the body is the problem. */
        private void drawGraph(Graphics2D g2) {
            int top = rows * cell;
            g2.setColor(new Color(12, 16, 15));
            g2.fillRect(0, top, getWidth(), GRAPH_HEIGHT);

            long peak = 1;
            int samples = Math.min(frameCount, frameTimes.length);
            for (int i = 0; i < samples; i++) {
                peak = Math.max(peak, frameTimes[i]);
            }

            g2.setColor(TEXT);
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            g2.drawString("tick time, newest on the right - peak " + peak + " us",
                8, top + 14);

            g2.setColor(GRAPH_LINE);
            g2.setStroke(new BasicStroke(1.5f));
            int width = getWidth();
            int previousX = -1;
            int previousY = -1;
            for (int i = 0; i < samples; i++) {
                int index = (frameCount - samples + i) % frameTimes.length;
                if (index < 0) {
                    index += frameTimes.length;
                }
                int x = width * i / Math.max(1, frameTimes.length - 1);
                int y = top + GRAPH_HEIGHT - 6
                    - (int) ((GRAPH_HEIGHT - 26) * frameTimes[index] / peak);
                if (previousX >= 0) {
                    g2.drawLine(previousX, previousY, x, y);
                }
                previousX = x;
                previousY = y;
            }
        }
    }

    // --------------------------------------------------------------- controls

    private final class Keys extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    turn(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    turn(1, 0);
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    turn(0, -1);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    turn(0, 1);
                    break;
                case KeyEvent.VK_P:
                    togglePause();
                    break;
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_SPACE:
                    if (gameOver || failure != null) {
                        newGame();
                    } else if (!running) {
                        start();
                    }
                    break;
                default:
                    break;
            }
        }

        /** A snake cannot turn straight back into itself. */
        private void turn(int newCol, int newRow) {
            if (mode != Mode.CLASSIC || !running || paused) {
                return;
            }
            if (newCol == -dirCol && newRow == -dirRow) {
                return;
            }
            pendingDirCol = newCol;
            pendingDirRow = newRow;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeGame().setVisible(true));
    }
}

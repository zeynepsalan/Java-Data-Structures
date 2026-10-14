import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * CSE201 Post-Lab 4 - the Minesweeper window. Provided and complete.
 * Do not modify or submit this file.
 *
 * The window draws the board from MineField.view() and calls only the public
 * methods listed in TODO.md. Every call is wrapped, so an exception from your
 * code appears in the message line instead of freezing the window.
 *
 * The first click is always safe: the layout is generated again, avoiding the
 * clicked cell, before the first reveal. That work happens here, so MineField
 * never needs any randomness.
 */
public class MinesweeperApp extends JFrame {

    private static final long serialVersionUID = 1L;

    private enum Level {
        BEGINNER("Beginner (9 x 9)", 9, 9, 10),
        INTERMEDIATE("Intermediate (16 x 16)", 16, 16, 40),
        EXPERT("Expert (16 x 30)", 16, 30, 99);

        private final String label;
        private final int rows;
        private final int cols;
        private final int mines;

        Level(String label, int rows, int cols, int mines) {
            this.label = label;
            this.rows = rows;
            this.cols = cols;
            this.mines = mines;
        }
    }

    private static final int CELL = 28;

    private static final Color HIDDEN = new Color(189, 189, 189);
    private static final Color HIDDEN_EDGE = new Color(232, 232, 232);
    private static final Color HIDDEN_SHADOW = new Color(123, 123, 123);
    private static final Color OPEN = new Color(215, 215, 215);
    private static final Color LINE = new Color(160, 160, 160);
    private static final Color MINE_CELL = new Color(226, 122, 118);
    private static final Color[] NUMBER_COLOR = {
        OPEN,
        new Color(24, 62, 190), new Color(23, 116, 44), new Color(196, 36, 36),
        new Color(24, 24, 130), new Color(129, 32, 32), new Color(24, 128, 128),
        new Color(30, 30, 30), new Color(96, 96, 96)
    };

    private final Random seeds = new Random();

    private Level level = Level.BEGINNER;
    private long seed;
    private MineField field;
    private boolean firstClickDone;
    private String failure;

    private final BoardPanel board = new BoardPanel();
    private final JButton smiley = new JButton(":-)");
    private final JLabel flagsLabel = new JLabel();
    private final JLabel clockLabel = new JLabel();
    private final JLabel message = new JLabel(" ");
    private final Timer clock = new Timer(1000, e -> updateClock());

    private long startedAt;
    private boolean clockRunning;

    public MinesweeperApp() {
        super("Minesweeper - CSE201 Post-Lab 4");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(buildMenuBar());

        smiley.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        smiley.setFocusable(false);
        smiley.addActionListener(e -> newGame());

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        flagsLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        clockLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        top.add(flagsLabel, BorderLayout.WEST);
        top.add(smiley, BorderLayout.CENTER);
        top.add(clockLabel, BorderLayout.EAST);

        message.setBorder(BorderFactory.createEmptyBorder(4, 10, 6, 10));
        message.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(message, BorderLayout.SOUTH);

        newGame();
        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu game = new JMenu("Game");
        ButtonGroup group = new ButtonGroup();
        for (Level value : Level.values()) {
            JRadioButtonMenuItem item =
                new JRadioButtonMenuItem(value.label, value == Level.BEGINNER);
            group.add(item);
            item.addActionListener(e -> {
                level = value;
                newGame();
                pack();
            });
            game.add(item);
        }
        bar.add(game);
        return bar;
    }

    // ------------------------------------------------------------- game flow

    private void newGame() {
        failure = null;
        firstClickDone = false;
        seed = seeds.nextLong();
        stopClock();
        clockLabel.setText("time 0");
        smiley.setText(":-)");

        boolean[][] layout =
            BoardGenerator.generate(level.rows, level.cols, level.mines, seed);
        field = build(layout);

        setTitle("Minesweeper - CSE201 Post-Lab 4   -   seed " + seed);
        board.setPreferredSize(new Dimension(level.cols * CELL, level.rows * CELL));
        refresh();
    }

    private MineField build(boolean[][] layout) {
        try {
            return new MineField(layout);
        } catch (Throwable t) {
            failure = "new MineField(...) -> " + describe(t);
            return null;
        }
    }

    private void leftClick(int r, int c) {
        if (field == null || finished()) {
            return;
        }
        if (!firstClickDone) {
            // Make sure the very first click cannot be a mine.
            Boolean mine = get(() -> Boolean.valueOf(field.isMine(r, c)));
            if (Boolean.TRUE.equals(mine)) {
                boolean[][] safer = BoardGenerator.generateAvoiding(
                    level.rows, level.cols, level.mines, seed, r, c);
                field = build(safer);
                if (field == null) {
                    refresh();
                    return;
                }
            }
            firstClickDone = true;
            startClock();
        }

        Integer opened = get(() -> Integer.valueOf(field.reveal(r, c)));
        if (opened != null && failure == null) {
            say("reveal(" + r + ", " + c + ") opened " + opened + " cell(s).");
        }
        afterMove();
    }

    private void rightClick(int r, int c) {
        if (field == null || finished()) {
            return;
        }
        get(() -> Boolean.valueOf(field.toggleFlag(r, c)));
        afterMove();
    }

    private void afterMove() {
        Boolean lost = get(() -> Boolean.valueOf(field.isLost()));
        Boolean won = get(() -> Boolean.valueOf(field.isWon()));
        if (Boolean.TRUE.equals(lost)) {
            smiley.setText(":-(");
            stopClock();
            say("You hit a mine. Press the smiley for a new board.");
        } else if (Boolean.TRUE.equals(won)) {
            smiley.setText("8-)");
            stopClock();
            say("Cleared. Press the smiley for a new board.");
        }
        refresh();
    }

    private boolean finished() {
        Boolean lost = get(() -> Boolean.valueOf(field.isLost()));
        Boolean won = get(() -> Boolean.valueOf(field.isWon()));
        return Boolean.TRUE.equals(lost) || Boolean.TRUE.equals(won);
    }

    private void refresh() {
        char[][] picture = field == null ? null : get(() -> field.view());
        board.picture = picture;

        int flags = 0;
        if (picture != null) {
            for (char[] row : picture) {
                for (char cell : row) {
                    if (cell == 'F') {
                        flags++;
                    }
                }
            }
        }
        flagsLabel.setText("mines " + (level.mines - flags));

        Integer hidden = field == null ? null
            : get(() -> Integer.valueOf(field.hiddenSafeCells()));
        if (failure != null) {
            message.setText("MineField failed: " + failure);
        } else if (hidden != null) {
            message.setText("Left click reveals, right click flags."
                + "   -   " + hidden + " safe cell(s) still hidden.");
        }
        board.repaint();
    }

    private void startClock() {
        startedAt = System.currentTimeMillis();
        clockRunning = true;
        clock.start();
    }

    private void stopClock() {
        clockRunning = false;
        clock.stop();
    }

    private void updateClock() {
        if (!clockRunning) {
            return;
        }
        clockLabel.setText("time " + (System.currentTimeMillis() - startedAt) / 1000);
    }

    // --------------------------------------------------------------- helpers

    private interface Query<R> {
        R run();
    }

    private <R> R get(Query<R> call) {
        if (field == null) {
            return null;
        }
        try {
            return call.run();
        } catch (Throwable t) {
            failure = describe(t);
            message.setText("MineField failed: " + failure);
            return null;
        }
    }

    private static String describe(Throwable t) {
        String text = t.getMessage();
        return t.getClass().getSimpleName() + (text == null ? "" : ": " + text);
    }

    private void say(String text) {
        if (failure == null) {
            message.setText(text);
        }
    }

    // ------------------------------------------------------------- rendering

    private final class BoardPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private transient char[][] picture;

        private BoardPanel() {
            setBackground(OPEN);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int r = e.getY() / CELL;
                    int c = e.getX() / CELL;
                    if (r < 0 || r >= level.rows || c < 0 || c >= level.cols) {
                        return;
                    }
                    if (SwingUtilities.isRightMouseButton(e)) {
                        rightClick(r, c);
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        leftClick(r, c);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

            if (picture == null) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Finish MineField.java to play.", 16, 28);
                return;
            }

            for (int r = 0; r < picture.length; r++) {
                for (int c = 0; c < picture[r].length; c++) {
                    paintCell(g2, r, c, picture[r][c]);
                }
            }
        }

        private void paintCell(Graphics2D g2, int r, int c, char what) {
            int x = c * CELL;
            int y = r * CELL;

            if (what == '#' || what == 'F') {
                g2.setColor(HIDDEN);
                g2.fillRect(x, y, CELL, CELL);
                g2.setColor(HIDDEN_EDGE);
                g2.drawLine(x, y, x + CELL - 1, y);
                g2.drawLine(x, y, x, y + CELL - 1);
                g2.setColor(HIDDEN_SHADOW);
                g2.drawLine(x, y + CELL - 1, x + CELL - 1, y + CELL - 1);
                g2.drawLine(x + CELL - 1, y, x + CELL - 1, y + CELL - 1);
                if (what == 'F') {
                    g2.setColor(new Color(190, 40, 40));
                    g2.fillRect(x + 9, y + 6, 10, 8);
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(x + 8, y + 6, 2, 16);
                }
                return;
            }

            g2.setColor(what == '*' ? MINE_CELL : OPEN);
            g2.fillRect(x, y, CELL, CELL);
            g2.setColor(LINE);
            g2.drawRect(x, y, CELL, CELL);

            if (what == '*') {
                g2.setColor(Color.BLACK);
                g2.fillOval(x + 8, y + 8, CELL - 16, CELL - 16);
            } else if (what != '0') {
                int digit = what - '0';
                g2.setColor(NUMBER_COLOR[Math.max(0, Math.min(8, digit))]);
                int width = g2.getFontMetrics().stringWidth(String.valueOf(what));
                g2.drawString(String.valueOf(what),
                    x + (CELL - width) / 2, y + CELL - 8);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MinesweeperApp().setVisible(true));
    }
}

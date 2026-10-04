package Postlabs.Postlab02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MusicPlayerApp extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Color INK = new Color(24, 51, 47);
    private static final Color GREEN = new Color(20, 125, 100);
    private static final Color PALE = new Color(234, 247, 243);
    private static final Color LIGHT = new Color(247, 250, 249);
    private static final Color LINE = new Color(201, 216, 212);
    private static final Color MUTED = new Color(94, 107, 104);
    private static final Color WARNING = new Color(255, 248, 217);

    private final Path musicFolder = Paths.get("musics");
    private final DoublyCircularLinkedList<Path> playlist = new DoublyCircularLinkedList<Path>();
    private final DefaultListModel<Path> libraryModel = new DefaultListModel<Path>();
    private final DefaultListModel<String> playlistModel = new DefaultListModel<String>();
    private final JList<Path> libraryList = new JList<Path>(libraryModel);
    private final JList<String> playlistList = new JList<String>(playlistModel);
    private final JLabel nowPlaying = new JLabel("No track selected");
    private final JLabel status = new JLabel(" ");

    private Clip activeClip;

    public MusicPlayerApp() {
        super("Circular Music Player");
        configureWindow();
        buildInterface();
        loadMusicFolder(true);
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 620));
        setSize(1040, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(LIGHT);
    }

    private void buildInterface() {
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(createLists(), BorderLayout.CENTER);
        add(createTransportArea(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(INK);
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Circular Music Player");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));

        JLabel subtitle = new JLabel(
                "A playlist powered by your doubly circular linked list");
        subtitle.setForeground(new Color(205, 229, 222));
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 3));
        text.setOpaque(false);
        text.add(title);
        text.add(subtitle);
        header.add(text, BorderLayout.WEST);
        return header;
    }

    private JSplitPane createLists() {
        configureLibraryList();
        configurePlaylistList();

        JPanel libraryPanel = createListPanel(
                "Available in musics/",
                "Select a track to add it to either end of the playlist.",
                new JScrollPane(libraryList),
                createLibraryButtons());

        JPanel playlistPanel = createListPanel(
                "Doubly circular playlist",
                "Double-click a track to make it the current node.",
                new JScrollPane(playlistList),
                createPlaylistButtons());

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, libraryPanel, playlistPanel);
        split.setResizeWeight(0.45);
        split.setDividerSize(8);
        split.setBorder(new EmptyBorder(18, 18, 8, 18));
        split.setBackground(LIGHT);
        return split;
    }

    private JPanel createListPanel(
            String titleText, String subtitleText,
            JScrollPane scrollPane, JPanel buttons) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(16, 16, 14, 16)));

        JLabel title = new JLabel(titleText);
        title.setForeground(INK);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));

        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));

        JPanel heading = new JPanel(new GridLayout(2, 1, 0, 2));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);

        scrollPane.setBorder(BorderFactory.createLineBorder(LINE));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(heading, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void configureLibraryList() {
        libraryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        libraryList.setFixedCellHeight(34);
        libraryList.setBackground(Color.WHITE);
        libraryList.setForeground(INK);
        libraryList.setSelectionBackground(PALE);
        libraryList.setSelectionForeground(INK);
        libraryList.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean selected, boolean focused) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, selected, focused);
                Path track = (Path) value;
                label.setText(displayName(track));
                label.setBorder(new EmptyBorder(0, 10, 0, 10));
                return label;
            }
        });
    }

    private void configurePlaylistList() {
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlistList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    jumpToRow(playlistList.locationToIndex(event.getPoint()));
                }
            }
        });
        playlistList.setFixedCellHeight(34);
        playlistList.setBackground(Color.WHITE);
        playlistList.setForeground(INK);
        playlistList.setSelectionBackground(GREEN);
        playlistList.setSelectionForeground(Color.WHITE);
        playlistList.setFocusable(false);
        playlistList.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean selected, boolean focused) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, selected, focused);
                label.setText((index + 1) + ".  " + value);
                label.setBorder(new EmptyBorder(0, 10, 0, 10));
                return label;
            }
        });
    }

    private JPanel createLibraryButtons() {
        JPanel buttons = new JPanel(new GridLayout(1, 3, 8, 0));
        buttons.setOpaque(false);
        buttons.add(button("Refresh", false, () -> loadMusicFolder(false)));
        buttons.add(button("Add First", true, () -> addSelected(true)));
        buttons.add(button("Add Last", true, () -> addSelected(false)));
        return buttons;
    }

    private JPanel createPlaylistButtons() {
        JPanel buttons = new JPanel(new GridLayout(1, 3, 8, 0));
        buttons.setOpaque(false);
        buttons.add(button("Remove First", false, () -> removeEndpoint(true)));
        buttons.add(button("Remove Last", false, () -> removeEndpoint(false)));
        buttons.add(button("Remove Playing", false, this::removePlaying));
        return buttons;
    }

    private JPanel createTransportArea() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(LIGHT);
        outer.setBorder(new EmptyBorder(6, 18, 16, 18));

        JPanel player = new JPanel(new BorderLayout(16, 0));
        player.setBackground(Color.WHITE);
        player.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(13, 16, 13, 16)));

        JPanel trackInfo = new JPanel(new GridLayout(2, 1, 0, 2));
        trackInfo.setOpaque(false);
        JLabel caption = new JLabel("CURRENT TRACK");
        caption.setForeground(GREEN);
        caption.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        nowPlaying.setForeground(INK);
        nowPlaying.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        trackInfo.add(caption);
        trackInfo.add(nowPlaying);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);
        controls.add(button("Previous", false, () -> move(false)));
        controls.add(button("Play", true, this::playCurrent));
        controls.add(button("Stop", false, () -> stopPlayback(true)));
        controls.add(button("Next", false, () -> move(true)));

        player.add(trackInfo, BorderLayout.CENTER);
        player.add(controls, BorderLayout.EAST);

        status.setForeground(MUTED);
        status.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        status.setBorder(new EmptyBorder(7, 3, 0, 3));

        outer.add(player, BorderLayout.CENTER);
        outer.add(status, BorderLayout.SOUTH);
        return outer;
    }

    private JButton button(String text, boolean primary, Runnable action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setMargin(new Insets(8, 12, 8, 12));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        if (primary) {
            button.setBackground(GREEN);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(PALE);
            button.setForeground(INK);
        }
        button.addActionListener(event -> action.run());
        return button;
    }

    private void loadMusicFolder(boolean populatePlaylist) {
        try {
            Files.createDirectories(musicFolder);
            List<Path> tracks;
            try (Stream<Path> files = Files.list(musicFolder)) {
                tracks = files
                        .filter(Files::isRegularFile)
                        .filter(this::isSupportedAudio)
                        .sorted(Comparator.comparing(
                                path -> path.getFileName().toString().toLowerCase(Locale.ROOT)))
                        .collect(Collectors.toList());
            }

            libraryModel.clear();
            for (Path track : tracks) {
                libraryModel.addElement(track);
            }
            if (!tracks.isEmpty()) {
                libraryList.setSelectedIndex(0);
            }

            if (populatePlaylist && playlist.isEmpty()) {
                try {
                    for (Path track : tracks) {
                        playlist.addLast(track);
                    }
                } catch (UnsupportedOperationException e) {
                    showImplementationRequired();
                }
            }

            refreshPlaylistView();
            if (tracks.isEmpty()) {
                setStatus("No WAV, AIFF, AIF, or AU files were found in musics/.");
            } else {
                setStatus("Found " + tracks.size() + " track(s) in musics/.");
            }
        } catch (IOException e) {
            showError("Could not scan the musics folder", e);
        } catch (RuntimeException e) {
            handleListFailure(e);
        }
    }

    private boolean isSupportedAudio(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.endsWith(".wav") || name.endsWith(".aiff")
                || name.endsWith(".aif") || name.endsWith(".au");
    }

    private void addSelected(boolean first) {
        Path selected = libraryList.getSelectedValue();
        if (selected == null) {
            setStatus("Select a track from the music folder first.");
            return;
        }

        runListAction(() -> {
            if (first) {
                playlist.addFirst(selected);
            } else {
                playlist.addLast(selected);
            }
            refreshPlaylistView();
            setStatus("Added " + displayName(selected)
                    + (first ? " at the beginning." : " at the end."));
        });
    }

    private void removeEndpoint(boolean first) {
        runListAction(() -> {
            stopPlayback(false);
            Path removed = first ? playlist.removeFirst() : playlist.removeLast();
            refreshPlaylistView();
            if (removed == null) {
                setStatus("The playlist is already empty.");
            } else {
                setStatus("Removed " + displayName(removed) + ".");
            }
        });
    }

    private void move(boolean next) {
        runListAction(() -> {
            boolean resume = activeClip != null && activeClip.isRunning();
            stopPlayback(false);
            Path selected = next ? playlist.moveNext() : playlist.movePrevious();
            refreshPlaylistView();
            if (selected == null) {
                setStatus("The playlist is empty.");
            } else if (resume) {
                playCurrent();
            } else {
                setStatus("Selected " + displayName(selected) + ".");
            }
        });
    }

    private void removePlaying() {
        runListAction(() -> {
            stopPlayback(false);
            Path removed = playlist.removeCurrent();
            refreshPlaylistView();
            if (removed == null) {
                setStatus("The playlist is already empty.");
            } else {
                setStatus("Removed " + displayName(removed)
                        + " and moved to the next track.");
            }
        });
    }

    private void jumpToRow(int row) {
        if (row < 0) {
            return;
        }
        runListAction(() -> {
            stopPlayback(false);
            if (!playlist.jumpTo(row)) {
                setStatus("There is no track at position " + (row + 1) + ".");
                return;
            }
            refreshPlaylistView();
            playCurrent();
        });
    }

    private void playCurrent() {
        runListAction(() -> {
            Path track = playlist.getCurrent();
            if (track == null) {
                setStatus("Add a track to the playlist before pressing Play.");
                return;
            }

            stopPlayback(false);
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(track.toFile())) {
                Clip newClip = AudioSystem.getClip();
                newClip.open(stream);
                activeClip = newClip;
                newClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP
                            && activeClip == newClip
                            && newClip.getFramePosition() >= newClip.getFrameLength()) {
                        SwingUtilities.invokeLater(() -> setStatus("Finished " + displayName(track) + "."));
                    }
                });
                newClip.start();
                refreshPlaylistView();
                setStatus("Playing " + displayName(track) + ".");
            } catch (Exception e) {
                stopPlayback(false);
                showError("This audio file could not be played", e);
            }
        });
    }

    private void stopPlayback(boolean reportStatus) {
        Clip clip = activeClip;
        activeClip = null;
        if (clip != null) {
            clip.stop();
            clip.close();
            if (reportStatus) {
                setStatus("Playback stopped.");
            }
        } else if (reportStatus) {
            setStatus("Nothing is currently playing.");
        }
    }

    private void refreshPlaylistView() {
        playlistModel.clear();
        int count = playlist.size();
        for (int i = 0; i < count; i++) {
            Path track = playlist.get(i);
            playlistModel.addElement(displayName(track));
        }

        int currentIndex = playlist.currentIndex();
        if (currentIndex >= 0 && currentIndex < playlistModel.size()) {
            playlistList.setSelectedIndex(currentIndex);
            playlistList.ensureIndexIsVisible(currentIndex);
        } else {
            playlistList.clearSelection();
        }

        Path current = playlist.getCurrent();
        nowPlaying.setText(current == null ? "No track selected" : displayName(current));
    }

    private String displayName(Path path) {
        if (path == null) {
            return "";
        }
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            name = name.substring(0, dot);
        }
        return name.replace('_', ' ');
    }

    private void runListAction(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException e) {
            handleListFailure(e);
        }
    }

    private void handleListFailure(RuntimeException e) {
        if (e instanceof UnsupportedOperationException) {
            showImplementationRequired();
        } else {
            showError("The linked-list operation failed", e);
        }
    }

    private void showImplementationRequired() {
        status.setOpaque(true);
        status.setBackground(WARNING);
        status.setForeground(INK);
        status.setText(
                " Complete DoublyCircularLinkedList.java to enable the playlist.");
    }

    private void setStatus(String message) {
        status.setOpaque(false);
        status.setForeground(MUTED);
        status.setText(message);
    }

    private void showError(String title, Exception e) {
        setStatus(title + ": " + e.getMessage());
        JOptionPane.showMessageDialog(
                this,
                title + ".\n\n" + e.getClass().getSimpleName() + ": " + e.getMessage(),
                "Music Player",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // The default Swing look and feel remains available.
            }
            new MusicPlayerApp().setVisible(true);
        });
    }
}

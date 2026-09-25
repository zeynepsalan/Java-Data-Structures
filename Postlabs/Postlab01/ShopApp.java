import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 * CSE201 Post-Lab 1 - ShopCart store window.
 *
 * Provided and complete. Do not modify or submit this file.
 *
 * The window calls only the methods listed in TODO.md. Every call into
 * Shop.java is wrapped, so an exception thrown by your code appears in the
 * message area at the bottom instead of closing the window.
 */
public class ShopApp {

    /** A call into student code that may fail while the file is unfinished. */
    private interface Call<R> {
        R run() throws Exception;
    }

    private static final String CATALOG_FILE = "catalog.csv";

    private final List<Product> catalog = new ArrayList<Product>();
    private final List<Discount> allCoupons = new ArrayList<Discount>();
    private final List<Discount> applied = new ArrayList<Discount>();

    /**
     * What the window believes each line holds. The Cart contract has no way to
     * read a single line's quantity back, so the window remembers what it asked
     * for. Line order and totals always come from the Cart itself, so a cart
     * that disagrees with this mirror is visible on screen.
     */
    private final Map<String, Integer> shownQuantity = new LinkedHashMap<String, Integer>();

    private Cart<Product> cart = new Cart<Product>();

    private JPanel catalogPanel;
    private JPanel cartPanel;
    private JComboBox<String> sortBox;
    private JTextField couponField;
    private JLabel appliedLabel;
    private JLabel subtotalLabel;
    private JLabel shippingLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JTextArea messages;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShopApp().show();
            }
        });
    }

    private void show() {
        JFrame frame = new JFrame("ShopCart - CSE201 Post-Lab 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(8, 8));

        frame.add(buildCatalogSide(), BorderLayout.CENTER);
        frame.add(buildCartSide(), BorderLayout.EAST);
        frame.add(buildMessages(), BorderLayout.SOUTH);

        loadCatalog();
        createCoupons();
        refreshCatalog();
        refreshCart();

        frame.setSize(1000, 680);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ---------------------------------------------------------------- layout

    private JPanel buildCatalogSide() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        top.add(new JLabel("Sort by:"));
        sortBox = new JComboBox<String>(
            new String[] { "Name", "Price (low to high)", "Rating (high to low)" });
        sortBox.addActionListener(e -> refreshCatalog());
        top.add(sortBox);

        catalogPanel = new JPanel();
        catalogPanel.setLayout(new BoxLayout(catalogPanel, BoxLayout.Y_AXIS));

        JPanel side = new JPanel(new BorderLayout());
        side.setBorder(BorderFactory.createTitledBorder("Catalog"));
        side.add(top, BorderLayout.NORTH);
        side.add(new JScrollPane(catalogPanel), BorderLayout.CENTER);
        return side;
    }

    private JPanel buildCartSide() {
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));

        JScrollPane scroller = new JScrollPane(cartPanel);
        scroller.setPreferredSize(new Dimension(380, 260));

        JPanel coupon = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        couponField = new JTextField(10);
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applyCoupon());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            applied.clear();
            refreshCart();
        });
        coupon.add(new JLabel("Coupon:"));
        coupon.add(couponField);
        coupon.add(applyButton);
        coupon.add(clearButton);

        appliedLabel = new JLabel(" ");
        appliedLabel.setFont(appliedLabel.getFont().deriveFont(Font.PLAIN, 11f));

        JPanel summary = new JPanel(new GridLayout(4, 2, 4, 2));
        subtotalLabel = new JLabel();
        shippingLabel = new JLabel();
        discountLabel = new JLabel();
        totalLabel = new JLabel();
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 15f));
        summary.add(new JLabel("Subtotal"));
        summary.add(subtotalLabel);
        summary.add(new JLabel("Shipping"));
        summary.add(shippingLabel);
        summary.add(new JLabel("Discount"));
        summary.add(discountLabel);
        summary.add(new JLabel("Total"));
        summary.add(totalLabel);

        JButton newCart = new JButton("Empty the cart");
        newCart.addActionListener(e -> {
            cart = new Cart<Product>();
            shownQuantity.clear();
            applied.clear();
            refreshCart();
        });

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(coupon);
        bottom.add(appliedLabel);
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(summary);
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(newCart);

        JPanel side = new JPanel(new BorderLayout(0, 6));
        side.setBorder(BorderFactory.createTitledBorder("Cart"));
        side.add(scroller, BorderLayout.CENTER);
        side.add(bottom, BorderLayout.SOUTH);
        return side;
    }

    private JScrollPane buildMessages() {
        messages = new JTextArea(5, 80);
        messages.setEditable(false);
        messages.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroller = new JScrollPane(messages);
        scroller.setBorder(BorderFactory.createTitledBorder("Messages"));
        return scroller;
    }

    // ------------------------------------------------------------- rendering

    private void refreshCatalog() {
        catalogPanel.removeAll();

        Comparator<Product> order = null;
        int choice = sortBox.getSelectedIndex();
        if (choice == 1) {
            order = call("Shop.byPriceAscending()", () -> Shop.byPriceAscending(), null);
        } else if (choice == 2) {
            order = call("Shop.byRatingDescending()", () -> Shop.byRatingDescending(), null);
        }

        final Comparator<Product> chosen = order;
        List<Product> shown = call("Shop.sorted(...)",
            () -> Shop.sorted(catalog, chosen), catalog);

        for (Product product : shown) {
            catalogPanel.add(catalogRow(product));
        }
        catalogPanel.revalidate();
        catalogPanel.repaint();
    }

    private JPanel catalogRow(final Product product) {
        String title = call("Product.toString()", () -> product.toString(), "?");
        String price = call("Product.getPrice()",
            () -> Money.format(product.getPrice()), "?");
        String rating = call("Product.getRating()",
            () -> stars(product.getRating()), "?");
        String category = call("Product.category()", () -> product.category(), "?");
        String shipping = call("Product.shippingCost()",
            () -> Money.format(product.shippingCost()), "?");

        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xDD, 0xDD, 0xDD)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JLabel name = new JLabel(title);
        name.setFont(name.getFont().deriveFont(Font.BOLD, 13f));
        JLabel detail = new JLabel(
            category + "  -  " + rating + "  -  shipping " + shipping);
        detail.setFont(detail.getFont().deriveFont(Font.PLAIN, 11f));
        detail.setForeground(Color.DARK_GRAY);

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        text.add(name);
        text.add(detail);

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 13f));

        JButton add = new JButton("Add to cart");
        add.addActionListener(e -> addToCart(product));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(priceLabel);
        right.add(add);

        row.add(text, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    private void refreshCart() {
        cartPanel.removeAll();

        List<Product> lines = call("Cart.products()",
            () -> cart.products(), new ArrayList<Product>());
        for (Product product : lines) {
            cartPanel.add(cartRow(product));
        }
        if (lines.isEmpty()) {
            JLabel empty = new JLabel("  The cart is empty.");
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            cartPanel.add(empty);
        }

        long subtotal = call("Cart.subtotal()", () -> cart.subtotal(), 0L);
        long shipping = call("Cart.shipping()", () -> cart.shipping(), 0L);
        int quantity = call("Cart.totalQuantity()", () -> cart.totalQuantity(), 0);
        Discount best = call("Shop.bestDiscount(...)",
            () -> Shop.bestDiscount(cart, applied), null);
        final Discount chosen = best;
        long off = chosen == null ? 0L
            : call("Discount.amountOff(...)",
                () -> chosen.amountOff(subtotal, quantity), 0L);
        long total = call("Cart.total(...)", () -> cart.total(chosen), 0L);

        int lineCount = call("Cart.lineCount()", () -> cart.lineCount(), lines.size());

        subtotalLabel.setText(Money.format(subtotal)
            + "   (" + lineCount + " line(s), " + quantity + " item(s))");
        shippingLabel.setText(Money.format(shipping));
        String bestCode = chosen == null ? "none"
            : call("Discount.code()", () -> chosen.code(), "?");
        discountLabel.setText("- " + Money.format(off) + "   (" + bestCode + ")");
        totalLabel.setText(Money.format(total));
        appliedLabel.setText("Entered codes: " + appliedCodes());

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private JPanel cartRow(final Product product) {
        final String sku = call("Product.getSku()", () -> product.getSku(), "?");
        String title = call("Product.toString()", () -> product.toString(), "?");
        int quantity = shownQuantity.containsKey(sku) ? shownQuantity.get(sku) : 1;

        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel name = new JLabel(title);
        name.setFont(name.getFont().deriveFont(Font.PLAIN, 12f));

        final JSpinner spinner =
            new JSpinner(new SpinnerNumberModel(Math.max(1, Math.min(99, quantity)), 1, 99, 1));
        spinner.setPreferredSize(new Dimension(56, 24));
        spinner.addChangeListener(e -> {
            int value = ((Integer) spinner.getValue()).intValue();
            Boolean changed = call("Cart.setQuantity(...)",
                () -> Boolean.valueOf(cart.setQuantity(sku, value)), Boolean.FALSE);
            if (Boolean.TRUE.equals(changed)) {
                shownQuantity.put(sku, Integer.valueOf(value));
            } else {
                say("Cart.setQuantity(\"" + sku + "\", " + value + ") returned false.");
            }
            refreshCart();
        });

        JButton remove = new JButton("X");
        remove.setMargin(new java.awt.Insets(0, 6, 0, 6));
        remove.addActionListener(e -> {
            call("Cart.remove(...)", () -> Boolean.valueOf(cart.remove(sku)), Boolean.FALSE);
            shownQuantity.remove(sku);
            refreshCart();
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        right.setOpaque(false);
        right.add(spinner);
        right.add(remove);

        row.add(name, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    // --------------------------------------------------------------- actions

    private void addToCart(final Product product) {
        final String sku = call("Product.getSku()", () -> product.getSku(), null);
        boolean ok = run("Cart.add(...)", () -> cart.add(product, 1));
        if (ok && sku != null) {
            Integer old = shownQuantity.get(sku);
            shownQuantity.put(sku, Integer.valueOf(old == null ? 1 : old.intValue() + 1));
        }
        refreshCart();
    }

    private void applyCoupon() {
        String typed = couponField.getText().trim();
        couponField.setText("");
        if (typed.isEmpty()) {
            return;
        }
        for (final Discount coupon : allCoupons) {
            String code = call("Discount.code()", () -> coupon.code(), null);
            if (code != null && code.equalsIgnoreCase(typed)) {
                if (applied.contains(coupon)) {
                    say("Coupon " + code + " was already entered.");
                } else {
                    applied.add(coupon);
                    say("Coupon " + code + " entered.");
                }
                refreshCart();
                return;
            }
        }
        say("There is no coupon with the code " + typed + ".");
    }

    private String appliedCodes() {
        if (applied.isEmpty()) {
            return "(none)";
        }
        StringBuilder sb = new StringBuilder();
        for (final Discount coupon : applied) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(call("Discount.code()", () -> coupon.code(), "?"));
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ data

    private void loadCatalog() {
        List<String[]> rows = readCsv();
        int failed = 0;
        for (String[] row : rows) {
            Product product = buildProduct(row);
            if (product == null) {
                failed++;
            } else {
                catalog.add(product);
            }
        }
        if (failed > 0) {
            say(failed + " of " + rows.size() + " catalog rows could not be created. "
                + "Finish the Product constructors in Shop.java.");
        }
    }

    private Product buildProduct(final String[] row) {
        final String sku = row[1];
        final String name = row[2];
        final long price = Long.parseLong(row[3]);
        final int rating = Integer.parseInt(row[4]);
        final int extra = Integer.parseInt(row[5]);
        if (row[0].equals("digital")) {
            return call("new DigitalProduct(" + sku + ")",
                () -> new DigitalProduct(sku, name, price, rating, extra), null);
        }
        return call("new PhysicalProduct(" + sku + ")",
            () -> new PhysicalProduct(sku, name, price, rating, extra), null);
    }

    private List<String[]> readCsv() {
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(CATALOG_FILE), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    rows.add(parts);
                } else {
                    say("Ignored a catalog line with " + parts.length + " fields: " + line);
                }
            }
        } catch (IOException e) {
            say("Could not read " + CATALOG_FILE + " from "
                + System.getProperty("user.dir") + ": " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                    // nothing useful to do here
                }
            }
        }
        return rows;
    }

    /**
     * The coupons the store offers, in the order bestDiscount sees them.
     * SAVE5 and TL100 remove the same amount on a 200.000 kurus subtotal, which
     * is where the "earlier coupon wins" rule becomes visible.
     */
    private void createCoupons() {
        addCoupon(call("new PercentOff(SAVE10)",
            () -> new PercentOff("SAVE10", 10, 15000), null));
        addCoupon(call("new FixedOff(TL100)",
            () -> new FixedOff("TL100", 10000, 100000), null));
        addCoupon(call("new PercentOff(SAVE5)",
            () -> new PercentOff("SAVE5", 5, 20000), null));
        addCoupon(call("new FixedOff(TL25)",
            () -> new FixedOff("TL25", 2500, 0), null));
        if (allCoupons.isEmpty()) {
            say("No coupons could be created. Finish PercentOff and FixedOff.");
        } else {
            say("Coupons: SAVE10, TL100, SAVE5, TL25");
        }
    }

    private void addCoupon(Discount coupon) {
        if (coupon != null) {
            allCoupons.add(coupon);
        }
    }

    // --------------------------------------------------------------- helpers

    private static String stars(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < rating ? '*' : '.');
        }
        return sb.toString();
    }

    /** Runs a call into student code and reports a failure instead of crashing. */
    private <R> R call(String what, Call<R> body, R fallback) {
        try {
            return body.run();
        } catch (Throwable t) {
            say(what + " -> " + describe(t));
            return fallback;
        }
    }

    private boolean run(String what, Runnable body) {
        try {
            body.run();
            return true;
        } catch (Throwable t) {
            say(what + " -> " + describe(t));
            return false;
        }
    }

    private static String describe(Throwable t) {
        String message = t.getMessage();
        return t.getClass().getSimpleName() + (message == null ? "" : ": " + message);
    }

    private void say(String text) {
        messages.append(text + System.lineSeparator());
        messages.setCaretPosition(messages.getDocument().getLength());
    }
}

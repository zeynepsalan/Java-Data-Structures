// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.FlowLayout;
// import java.awt.Font;
// import java.awt.GridBagConstraints;
// import java.awt.GridBagLayout;
// import java.awt.Insets;
// import java.util.ArrayList;
// import java.util.List;
// import javax.swing.BorderFactory;
// import javax.swing.ButtonGroup;
// import javax.swing.DefaultListModel;
// import javax.swing.JButton;
// import javax.swing.JComboBox;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JList;
// import javax.swing.JPanel;
// import javax.swing.JRadioButton;
// import javax.swing.JScrollPane;
// import javax.swing.JSpinner;
// import javax.swing.JSplitPane;
// import javax.swing.JTextArea;
// import javax.swing.JTextField;
// import javax.swing.SpinnerNumberModel;
// import javax.swing.SwingUtilities;
// import javax.swing.UIManager;

// /**
//  * CSE201 Post-Lab 0 - the SplitPal window. Provided and complete.
//  * Do not modify or submit this file.
//  *
//  * The window parses what you type into whole kurus itself, so BillSplitter only
//  * ever sees long values. Every call into BillSplitter is wrapped: an exception
//  * from your code appears in the message line instead of closing the window.
//  */
// public class SplitPalApp extends JFrame {

//     private static final long serialVersionUID = 1L;

//     private static final Color INK = new Color(28, 38, 52);
//     private static final Color ACCENT = new Color(36, 110, 185);
//     private static final Color MUTED = new Color(96, 108, 124);
//     private static final Color WARNING = new Color(160, 42, 42);

//     /** One recorded expense. The window keeps these and recomputes everything. */
//     private static final class Expense {
//         private final String description;
//         private final int payer;
//         private final long amount;
//         private final int tipPercent;
//         private final boolean evenly;
//         private final int[] weights;

//         private Expense(String description, int payer, long amount, int tipPercent,
//                         boolean evenly, int[] weights) {
//             this.description = description;
//             this.payer = payer;
//             this.amount = amount;
//             this.tipPercent = tipPercent;
//             this.evenly = evenly;
//             this.weights = weights;
//         }
//     }

//     private final DefaultListModel<String> peopleModel = new DefaultListModel<String>();
//     private final DefaultListModel<String> expenseModel = new DefaultListModel<String>();
//     private final List<Expense> expenses = new ArrayList<Expense>();

//     private final JList<String> peopleList = new JList<String>(peopleModel);
//     private final JList<String> expenseList = new JList<String>(expenseModel);
//     private final JTextField personField = new JTextField(12);
//     private final JTextField descriptionField = new JTextField(14);
//     private final JTextField amountField = new JTextField(8);
//     private final JTextField weightsField = new JTextField(10);
//     private final JComboBox<String> payerBox = new JComboBox<String>();
//     private final JSpinner tipSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
//     private final JRadioButton evenlyButton = new JRadioButton("Split evenly", true);
//     private final JRadioButton weightsButton = new JRadioButton("Split by weights");
//     private final JTextArea summary = new JTextArea(12, 34);
//     private final JLabel message = new JLabel(" ");

//     public SplitPalApp() {
//         super("SplitPal - CSE201 Post-Lab 0");
//         setDefaultCloseOperation(EXIT_ON_CLOSE);

//         JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//             buildLeftSide(), buildRightSide());
//         split.setResizeWeight(0.58);
//         split.setDividerSize(8);
//         split.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));

//         message.setBorder(BorderFactory.createEmptyBorder(2, 14, 10, 14));
//         message.setForeground(MUTED);

//         setLayout(new BorderLayout());
//         add(split, BorderLayout.CENTER);
//         add(message, BorderLayout.SOUTH);

//         addStarterGroup();
//         refresh();

//         setSize(1020, 620);
//         setLocationRelativeTo(null);
//     }

//     // ---------------------------------------------------------------- layout

//     private JPanel buildLeftSide() {
//         JPanel people = new JPanel(new BorderLayout(6, 6));
//         people.setBorder(BorderFactory.createTitledBorder("Who is sharing"));
//         peopleList.setVisibleRowCount(5);
//         people.add(new JScrollPane(peopleList), BorderLayout.CENTER);

//         JPanel peopleButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
//         JButton addPerson = new JButton("Add");
//         addPerson.addActionListener(e -> addPerson());
//         JButton removePerson = new JButton("Remove");
//         removePerson.addActionListener(e -> removePerson());
//         peopleButtons.add(personField);
//         peopleButtons.add(addPerson);
//         peopleButtons.add(removePerson);
//         people.add(peopleButtons, BorderLayout.SOUTH);

//         JPanel expensesPanel = new JPanel(new BorderLayout(6, 6));
//         expensesPanel.setBorder(BorderFactory.createTitledBorder("Expenses"));
//         expensesPanel.add(new JScrollPane(expenseList), BorderLayout.CENTER);
//         JPanel expenseButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
//         JButton removeExpense = new JButton("Remove selected");
//         removeExpense.addActionListener(e -> removeExpense());
//         JButton clear = new JButton("Remove all");
//         clear.addActionListener(e -> {
//             expenses.clear();
//             refresh();
//         });
//         expenseButtons.add(removeExpense);
//         expenseButtons.add(clear);
//         expensesPanel.add(expenseButtons, BorderLayout.SOUTH);

//         JPanel left = new JPanel(new BorderLayout(0, 10));
//         left.add(people, BorderLayout.NORTH);
//         left.add(buildExpenseForm(), BorderLayout.CENTER);
//         left.add(expensesPanel, BorderLayout.SOUTH);
//         return left;
//     }

//     private JPanel buildExpenseForm() {
//         JPanel form = new JPanel(new GridBagLayout());
//         form.setBorder(BorderFactory.createTitledBorder("Add an expense"));

//         GridBagConstraints g = new GridBagConstraints();
//         g.insets = new Insets(3, 6, 3, 6);
//         g.anchor = GridBagConstraints.WEST;

//         int row = 0;
//         g.gridx = 0;
//         g.gridy = row;
//         form.add(new JLabel("What for"), g);
//         g.gridx = 1;
//         form.add(descriptionField, g);

//         row++;
//         g.gridx = 0;
//         g.gridy = row;
//         form.add(new JLabel("Who paid"), g);
//         g.gridx = 1;
//         form.add(payerBox, g);

//         row++;
//         g.gridx = 0;
//         g.gridy = row;
//         form.add(new JLabel("Amount"), g);
//         g.gridx = 1;
//         JPanel amountRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
//         amountRow.add(amountField);
//         JLabel hint = new JLabel("TL, for example 450,00");
//         hint.setForeground(MUTED);
//         hint.setFont(hint.getFont().deriveFont(Font.PLAIN, 11f));
//         amountRow.add(hint);
//         form.add(amountRow, g);

//         row++;
//         g.gridx = 0;
//         g.gridy = row;
//         form.add(new JLabel("Tip"), g);
//         g.gridx = 1;
//         JPanel tipRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
//         tipSpinner.setPreferredSize(new Dimension(60, 24));
//         tipRow.add(tipSpinner);
//         tipRow.add(new JLabel("percent, added before splitting"));
//         form.add(tipRow, g);

//         row++;
//         ButtonGroup group = new ButtonGroup();
//         group.add(evenlyButton);
//         group.add(weightsButton);
//         evenlyButton.addActionListener(e -> weightsField.setEnabled(false));
//         weightsButton.addActionListener(e -> weightsField.setEnabled(true));
//         weightsField.setEnabled(false);
//         g.gridx = 0;
//         g.gridy = row;
//         form.add(evenlyButton, g);
//         g.gridx = 1;
//         JPanel weightRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
//         weightRow.add(weightsButton);
//         weightRow.add(weightsField);
//         JLabel weightHint = new JLabel("one number per person, e.g. 2 1 1");
//         weightHint.setForeground(MUTED);
//         weightHint.setFont(weightHint.getFont().deriveFont(Font.PLAIN, 11f));
//         weightRow.add(weightHint);
//         form.add(weightRow, g);

//         row++;
//         g.gridx = 1;
//         g.gridy = row;
//         JButton add = new JButton("Add expense");
//         add.addActionListener(e -> addExpense());
//         form.add(add, g);

//         return form;
//     }

//     private JPanel buildRightSide() {
//         summary.setEditable(false);
//         summary.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
//         summary.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

//         JPanel right = new JPanel(new BorderLayout());
//         right.setBorder(BorderFactory.createTitledBorder("Balances and who pays whom"));
//         right.add(new JScrollPane(summary), BorderLayout.CENTER);
//         return right;
//     }

//     // --------------------------------------------------------------- actions

//     private void addStarterGroup() {
//         peopleModel.addElement("Ece");
//         peopleModel.addElement("Can");
//         peopleModel.addElement("Deniz");
//     }

//     private void addPerson() {
//         String name = personField.getText().trim();
//         if (name.isEmpty()) {
//             say("Type a name first.", true);
//             return;
//         }
//         peopleModel.addElement(name);
//         personField.setText("");
//         expenses.clear();
//         say("Added " + name + ". The expense list was cleared, "
//             + "because the shares depend on who is in the group.", false);
//         refresh();
//     }

//     private void removePerson() {
//         int index = peopleList.getSelectedIndex();
//         if (index < 0) {
//             say("Select a person to remove.", true);
//             return;
//         }
//         String name = peopleModel.remove(index);
//         expenses.clear();
//         say("Removed " + name + ". The expense list was cleared.", false);
//         refresh();
//     }

//     private void removeExpense() {
//         int index = expenseList.getSelectedIndex();
//         if (index < 0 || index >= expenses.size()) {
//             say("Select an expense to remove.", true);
//             return;
//         }
//         expenses.remove(index);
//         refresh();
//     }

//     private void addExpense() {
//         int people = peopleModel.size();
//         if (people == 0) {
//             say("Add at least one person first.", true);
//             return;
//         }
//         int payer = payerBox.getSelectedIndex();
//         if (payer < 0) {
//             say("Choose who paid.", true);
//             return;
//         }

//         long amount;
//         try {
//             amount = parseKurus(amountField.getText());
//         } catch (IllegalArgumentException e) {
//             say("The amount has to look like 450 or 450,00.", true);
//             return;
//         }

//         int[] weights = null;
//         if (weightsButton.isSelected()) {
//             weights = parseWeights(weightsField.getText(), people);
//             if (weights == null) {
//                 say("Give one whole number per person, for example 2 1 1 for "
//                     + people + " people.", true);
//                 return;
//             }
//         }

//         String description = descriptionField.getText().trim();
//         if (description.isEmpty()) {
//             description = "expense";
//         }

//         expenses.add(new Expense(description, payer, amount,
//             ((Integer) tipSpinner.getValue()).intValue(),
//             evenlyButton.isSelected(), weights));
//         descriptionField.setText("");
//         amountField.setText("");
//         say("Added the expense.", false);
//         refresh();
//     }

//     // ------------------------------------------------------------ the maths

//     /** Rebuilds every view from the expense list, through BillSplitter. */
//     private void refresh() {
//         int people = peopleModel.size();

//         String selectedPayer = (String) payerBox.getSelectedItem();
//         payerBox.removeAllItems();
//         for (int i = 0; i < people; i++) {
//             payerBox.addItem(peopleModel.get(i));
//         }
//         if (selectedPayer != null) {
//             payerBox.setSelectedItem(selectedPayer);
//         }
//         if (payerBox.getSelectedIndex() < 0 && people > 0) {
//             payerBox.setSelectedIndex(0);
//         }

//         long[] paid = new long[people];
//         long[] owed = new long[people];

//         expenseModel.clear();
//         for (Expense expense : expenses) {
//             Long tip = call("tipAmount",
//                 () -> Long.valueOf(BillSplitter.tipAmount(expense.amount, expense.tipPercent)));
//             if (tip == null) {
//                 return;
//             }
//             final long total = expense.amount + tip.longValue();

//             long[] shares = call(expense.evenly ? "splitEvenly" : "splitByWeights",
//                 () -> expense.evenly
//                     ? BillSplitter.splitEvenly(total, people)
//                     : BillSplitter.splitByWeights(total, expense.weights));
//             if (shares == null) {
//                 return;
//             }
//             if (shares.length != people) {
//                 fail("The split returned " + shares.length + " shares for "
//                     + people + " people.");
//                 return;
//             }

//             paid[expense.payer] += total;
//             for (int i = 0; i < people; i++) {
//                 owed[i] += shares[i];
//             }

//             String label = format(total) + "  -  " + expense.description
//                 + "  (" + peopleModel.get(expense.payer) + " paid"
//                 + (expense.tipPercent > 0 ? ", tip " + expense.tipPercent + "%" : "")
//                 + (expense.evenly ? ", evenly" : ", by weights") + ")";
//             expenseModel.addElement(label);
//         }

//         showSummary(paid, owed, people);
//     }

//     private void showSummary(long[] paid, long[] owed, int people) {
//         StringBuilder text = new StringBuilder();

//         long[] balance = call("balances", () -> BillSplitter.balances(paid, owed));
//         if (balance == null) {
//             return;
//         }

//         text.append("BALANCES\n");
//         for (int i = 0; i < people; i++) {
//             String state = balance[i] > 0 ? "gets back"
//                 : balance[i] < 0 ? "owes     " : "is even  ";
//             long shown = balance[i] < 0 ? -balance[i] : balance[i];
//             text.append(pad(peopleModel.get(i), 12)).append(state).append(' ')
//                 .append(format(shown)).append('\n');
//         }

//         long[][] transfers = call("settleUp", () -> BillSplitter.settleUp(balance));
//         if (transfers == null) {
//             summary.setText(text.toString());
//             return;
//         }

//         text.append("\nWHO PAYS WHOM\n");
//         if (transfers.length == 0) {
//             text.append("Nobody owes anything.\n");
//         }
//         for (long[] transfer : transfers) {
//             if (transfer == null || transfer.length != 3) {
//                 fail("settleUp returned a transfer that is not {from, to, amount}.");
//                 return;
//             }
//             text.append(pad(nameAt((int) transfer[0]), 12))
//                 .append("pays ")
//                 .append(pad(nameAt((int) transfer[1]), 12))
//                 .append(format(transfer[2]))
//                 .append('\n');
//         }

//         long totalSpent = 0;
//         for (long amount : paid) {
//             totalSpent += amount;
//         }
//         text.append("\nTotal spent: ").append(format(totalSpent)).append('\n');
//         summary.setText(text.toString());
//         summary.setCaretPosition(0);
//     }

//     private String nameAt(int index) {
//         return index >= 0 && index < peopleModel.size() ? peopleModel.get(index)
//             : "person " + index;
//     }

//     private String format(long kurus) {
//         String text = call("formatTL", () -> BillSplitter.formatTL(kurus));
//         return text == null ? kurus + " kurus" : text;
//     }

//     // --------------------------------------------------------------- helpers

//     private interface Call<R> {
//         R run();
//     }

//     /** Runs a BillSplitter call and reports a failure instead of crashing. */
//     private <R> R call(String what, Call<R> body) {
//         try {
//             return body.run();
//         } catch (Throwable t) {
//             String detail = t.getMessage();
//             String reason = t.getClass().getSimpleName()
//                 + (detail == null ? "" : ": " + detail);
//             String extra = "";
//             if ("settleUp".equals(what) && t instanceof IllegalArgumentException) {
//                 extra = "   (the balances do not add up to zero, which usually means"
//                     + " a split does not hand out exactly the total)";
//             }
//             fail("BillSplitter." + what + " -> " + reason + extra);
//             return null;
//         }
//     }

//     private void fail(String text) {
//         say(text, true);
//         summary.setText("The calculation stopped.\n\n" + text
//             + "\n\nFinish BillSplitter.java and try again.");
//     }

//     private void say(String text, boolean problem) {
//         message.setForeground(problem ? WARNING : MUTED);
//         message.setText(text);
//     }

//     private static String pad(String text, int width) {
//         StringBuilder out = new StringBuilder(text);
//         while (out.length() < width) {
//             out.append(' ');
//         }
//         return out.toString();
//     }

//     /** "450,00" and "450" and "450.5" all become whole kurus. */
//     private static long parseKurus(String text) {
//         String cleaned = text.trim().replace('.', ',').replace(" ", "");
//         if (cleaned.isEmpty()) {
//             throw new IllegalArgumentException("empty");
//         }
//         boolean negative = cleaned.startsWith("-");
//         if (negative) {
//             throw new IllegalArgumentException("an expense cannot be negative");
//         }
//         int comma = cleaned.indexOf(',');
//         String liraPart = comma < 0 ? cleaned : cleaned.substring(0, comma);
//         String kurusPart = comma < 0 ? "" : cleaned.substring(comma + 1);
//         if (liraPart.isEmpty()) {
//             liraPart = "0";
//         }
//         if (kurusPart.length() > 2 || cleaned.indexOf(',', comma + 1) >= 0) {
//             throw new IllegalArgumentException("too many decimals");
//         }
//         while (kurusPart.length() < 2) {
//             kurusPart = kurusPart + "0";
//         }
//         for (int i = 0; i < liraPart.length(); i++) {
//             if (!Character.isDigit(liraPart.charAt(i))) {
//                 throw new IllegalArgumentException("not a number");
//             }
//         }
//         for (int i = 0; i < kurusPart.length(); i++) {
//             if (!Character.isDigit(kurusPart.charAt(i))) {
//                 throw new IllegalArgumentException("not a number");
//             }
//         }
//         return Long.parseLong(liraPart) * 100 + Long.parseLong(kurusPart);
//     }

//     /** "2 1 1" becomes {2, 1, 1}; null when the text does not fit the group. */
//     private static int[] parseWeights(String text, int people) {
//         String cleaned = text.trim().replace(',', ' ').replace(';', ' ');
//         List<Integer> values = new ArrayList<Integer>();
//         int i = 0;
//         while (i < cleaned.length()) {
//             while (i < cleaned.length() && cleaned.charAt(i) == ' ') {
//                 i++;
//             }
//             int start = i;
//             while (i < cleaned.length() && cleaned.charAt(i) != ' ') {
//                 i++;
//             }
//             if (start == i) {
//                 break;
//             }
//             try {
//                 values.add(Integer.valueOf(cleaned.substring(start, i)));
//             } catch (NumberFormatException e) {
//                 return null;
//             }
//         }
//         if (values.size() != people) {
//             return null;
//         }
//         int[] weights = new int[people];
//         for (int k = 0; k < people; k++) {
//             weights[k] = values.get(k).intValue();
//         }
//         return weights;
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             try {
//                 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//             } catch (Exception ignored) {
//                 // The default Swing look and feel remains available.
//             }
//             new SplitPalApp().setVisible(true);
//         });
//     }
// }

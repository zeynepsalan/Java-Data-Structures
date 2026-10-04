import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class PracticeLab03Tester {
    private static double score = 0;
    private static int passed = 0;
    private static int total = 0;
    @FunctionalInterface private interface CheckedAction { void run() throws Exception; }

    public static void main(String[] args) {
        int[] a = {4, 7, 2, 9, 5, 1, 8, 3};
        int[] b = {6, 1, 7, 4, 2};
        ArrayList<Integer> list = new ArrayList<>();
        for (int value : a) list.add(value);
        System.out.println("========================================================");
        System.out.println(" CSE 201 - Data Structures | Practice Lab 03 Grader");
        System.out.println(" 30 questions | detailed output | total: 100 points");
        System.out.println("========================================================");
        grade(1,4,"1",()->PracticeLab03.question01(a)); grade(2,4,"n",()->PracticeLab03.question02(a)); grade(3,4,"n^2",()->PracticeLab03.question03(a)); grade(4,4,"n^2",()->PracticeLab03.question04(a)); grade(5,4,"n^3",()->PracticeLab03.question05(a)); grade(6,4,"logn",()->PracticeLab03.question06(a)); grade(7,4,"nlogn",()->PracticeLab03.question07(a)); grade(8,4,"2^n",()->PracticeLab03.question08(a)); grade(9,4,"n*m",()->PracticeLab03.question09(a,b)); grade(10,4,"n+m",()->PracticeLab03.question10(a,b));
        grade(11,3,"n^2",()->PracticeLab03.question11(a)); grade(12,3,"n",()->PracticeLab03.question12(a)); grade(13,3,"logn",()->PracticeLab03.question13(a)); grade(14,3,"n^2",()->PracticeLab03.question14(a)); grade(15,3,"n",()->PracticeLab03.question15(a)); grade(16,3,"n",()->PracticeLab03.question16(a)); grade(17,3,"n",()->PracticeLab03.question17(a)); grade(18,3,"nlogn",()->PracticeLab03.question18(a)); grade(19,3,"n^2",()->PracticeLab03.question19(a)); grade(20,3,"n^2",()->PracticeLab03.question20(list));
        grade(21,3,"n^3",()->PracticeLab03.question21(a)); grade(22,3,"nlogn",()->PracticeLab03.question22(a)); grade(23,3,"n+m",()->PracticeLab03.question23(a,b)); grade(24,3,"nlogn",()->PracticeLab03.question24(a)); grade(25,3,"2^n",()->PracticeLab03.question25(a)); grade(26,3,"n^2",()->PracticeLab03.question26(a)); grade(27,3,"n",()->PracticeLab03.question27(a)); grade(28,3,"n*m",()->PracticeLab03.question28(a,b)); grade(29,3,"n",()->PracticeLab03.question29(a)); grade(30,3,"1",()->PracticeLab03.question30(a));
        System.out.println("\n========================================================");
        System.out.printf(" FINAL SCORE: %.0f / 100%n", score);
        System.out.printf(" Passed: %d / %d questions%n", passed, total);
        System.out.println("========================================================");
    }

    private static void grade(int number, int points, String expected, CheckedAction action) {
        total++; String actual = extractAnswer(capture(action));
        if (expected.equals(actual)) { score += points; passed++; System.out.printf("[PASS] Q%02d | +%d | O(%s)%n", number, points, actual); }
        else System.out.printf("[FAIL] Q%02d | +0/%d | expected O(%s), received O(%s)%n", number, points, expected, actual.isEmpty() ? "blank" : actual);
    }
    private static String capture(CheckedAction action) {
        PrintStream original = System.out; ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream captured = new PrintStream(buffer)) { System.setOut(captured); action.run(); }
        catch (Exception error) { return ""; } finally { System.setOut(original); }
        return buffer.toString();
    }
    private static String extractAnswer(String output) {
        int open = output.indexOf("O("); int close = output.indexOf(')', open);
        return open >= 0 && close >= 0 ? output.substring(open + 2, close).trim() : "";
    }
}

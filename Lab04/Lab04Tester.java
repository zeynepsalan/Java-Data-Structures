package Lab04;

/**
 * Lab04 için edge-case odaklı tester.
 * Node ve buildList hocanın yardımcıları; test edilmez, sadece setup için kullanılır.
 *
 * Çalıştırma (Lab04 klasöründen):
 *   javac Lab04.java Lab04Tester.java && java Lab04.Lab04Tester
 * veya proje kökünden:
 *   javac Lab04/*.java && java Lab04.Lab04Tester
 */
public class Lab04Tester {

    private static int passed = 0;
    private static int failed = 0;
    private static int total = 0;

    @FunctionalInterface
    private interface Case {
        void run() throws Exception;
    }

    public static void main(String[] args) {
        System.out.println("========================================================");
        System.out.println(" Lab04 Edge-Case Tester");
        System.out.println("========================================================\n");

        testFactorial();
        testSum();
        testPower();
        testBinarySearch();
        testReverse();
        testFib();
        testTailSum();
        testListSum();
        testContains();
        testReverseList();

        System.out.println("\n========================================================");
        System.out.printf(" RESULT: %d passed, %d failed, %d total%n", passed, failed, total);
        System.out.println("========================================================");
    }

    // ------------------------------------------------------------------
    // factorial
    // ------------------------------------------------------------------
    private static void testFactorial() {
        section("factorial");

        check("factorial(0) == 1", () -> assertEq(1L, Lab04.factorial(0)));
        check("factorial(1) == 1", () -> assertEq(1L, Lab04.factorial(1)));
        check("factorial(2) == 2", () -> assertEq(2L, Lab04.factorial(2)));
        check("factorial(5) == 120", () -> assertEq(120L, Lab04.factorial(5)));
        check("factorial(10) == 3628800", () -> assertEq(3628800L, Lab04.factorial(10)));
    }

    // ------------------------------------------------------------------
    // sum — n: dahil edilecek son index (inclusive)
    // ------------------------------------------------------------------
    private static void testSum() {
        section("sum");

        int[] arr = { 2, 3, 5, 6, 9, 10, 11, 15, 20 };
        int[] single = { 42 };
        int[] negatives = { -5, 10, -3 };

        check("sum tek eleman (n=0)", () -> assertEq(42, Lab04.sum(single, 0)));
        check("sum ilk eleman (n=0)", () -> assertEq(2, Lab04.sum(arr, 0)));
        check("sum ilk 2 (n=1)", () -> assertEq(5, Lab04.sum(arr, 1)));
        check("sum ilk 4 (n=3)", () -> assertEq(16, Lab04.sum(arr, 3)));
        check("sum tüm dizi", () -> assertEq(81, Lab04.sum(arr, arr.length - 1)));
        check("sum negatif değerler", () -> assertEq(2, Lab04.sum(negatives, 2)));
    }

    // ------------------------------------------------------------------
    // power
    // ------------------------------------------------------------------
    private static void testPower() {
        section("power");

        check("power(5, 1) == 5", () -> assertEq(5L, Lab04.power(5, 1)));
        check("power(5, 5) == 3125", () -> assertEq(3125L, Lab04.power(5, 5)));
        check("power(2, 10) == 1024", () -> assertEq(1024L, Lab04.power(2, 10)));
        check("power(1, 100) == 1", () -> assertEq(1L, Lab04.power(1, 100)));
        check("power(0, 5) == 0", () -> assertEq(0L, Lab04.power(0, 5)));
        check("power(-2, 3) == -8", () -> assertEq(-8L, Lab04.power(-2, 3)));
        check("power(-2, 4) == 16", () -> assertEq(16L, Lab04.power(-2, 4)));
        // Tipik beklenen: herhangi bir base^0 = 1
        check("power(7, 0) == 1", () -> assertEq(1L, Lab04.power(7, 0)));
    }

    // ------------------------------------------------------------------
    // binarySearch — sıralı dizi; bulunursa index, yoksa -1
    // high: inclusive (klasik)
    // ------------------------------------------------------------------
    private static void testBinarySearch() {
        section("binarySearch");

        int[] arr = { 2, 3, 5, 6, 9, 10, 11, 15, 20 };
        int hi = arr.length - 1;

        check("bul: ilk eleman (2)", () -> assertEq(0, Lab04.binarySearch(arr, 2, 0, hi)));
        check("bul: son eleman (20)", () -> assertEq(8, Lab04.binarySearch(arr, 20, 0, hi)));
        check("bul: orta eleman (9)", () -> assertEq(4, Lab04.binarySearch(arr, 9, 0, hi)));
        check("bul: 6", () -> assertEq(3, Lab04.binarySearch(arr, 6, 0, hi)));
        check("bul: 15", () -> assertEq(7, Lab04.binarySearch(arr, 15, 0, hi)));

        check("yok: diziden küçük (-1)", () -> assertEq(-1, Lab04.binarySearch(arr, -1, 0, hi)));
        check("yok: diziden büyük (100)", () -> assertEq(-1, Lab04.binarySearch(arr, 100, 0, hi)));
        check("yok: aralıkta olmayan (7)", () -> assertEq(-1, Lab04.binarySearch(arr, 7, 0, hi)));
        check("yok: aralıkta olmayan (12)", () -> assertEq(-1, Lab04.binarySearch(arr, 12, 0, hi)));

        int[] one = { 7 };
        check("tek eleman: bulunur", () -> assertEq(0, Lab04.binarySearch(one, 7, 0, 0)));
        check("tek eleman: bulunamaz", () -> assertEq(-1, Lab04.binarySearch(one, 3, 0, 0)));

        int[] two = { 1, 3 };
        check("iki eleman: sol", () -> assertEq(0, Lab04.binarySearch(two, 1, 0, 1)));
        check("iki eleman: sağ", () -> assertEq(1, Lab04.binarySearch(two, 3, 0, 1)));
        check("iki eleman: yok", () -> assertEq(-1, Lab04.binarySearch(two, 2, 0, 1)));
    }

    // ------------------------------------------------------------------
    // reverse (String)
    // ------------------------------------------------------------------
    private static void testReverse() {
        section("reverse (String)");

        check("tek karakter", () -> assertEq("a", Lab04.reverse("a")));
        check("iki karakter", () -> assertEq("ba", Lab04.reverse("ab")));
        check("normal kelime", () -> assertEq("cba", Lab04.reverse("abc")));
        check("palindrome", () -> assertEq("kayak", Lab04.reverse("kayak")));
        check("sayısal string", () -> assertEq("54321", Lab04.reverse("12345")));
        check("boş string", () -> assertEq("", Lab04.reverse("")));
        check("boşluk içeren", () -> assertEq("cba a", Lab04.reverse("a abc")));
    }

    // ------------------------------------------------------------------
    // fib
    // ------------------------------------------------------------------
    private static void testFib() {
        section("fib");

        check("fib(0) == 0", () -> assertEq(0L, Lab04.fib(0)));
        check("fib(1) == 1", () -> assertEq(1L, Lab04.fib(1)));
        check("fib(2) == 1", () -> assertEq(1L, Lab04.fib(2)));
        check("fib(3) == 2", () -> assertEq(2L, Lab04.fib(3)));
        check("fib(5) == 5", () -> assertEq(5L, Lab04.fib(5)));
        check("fib(10) == 55", () -> assertEq(55L, Lab04.fib(10)));
        check("fib(negatif) == 0", () -> assertEq(0L, Lab04.fib(-3)));
    }

    // ------------------------------------------------------------------
    // tailSum — 1..n toplamı (acc ile)
    // ------------------------------------------------------------------
    private static void testTailSum() {
        section("tailSum");

        check("tailSum(0, 0) == 0", () -> assertEq(0L, Lab04.tailSum(0, 0)));
        check("tailSum(1, 0) == 1", () -> assertEq(1L, Lab04.tailSum(1, 0)));
        check("tailSum(7, 0) == 28", () -> assertEq(28L, Lab04.tailSum(7, 0)));
        check("tailSum(10, 0) == 55", () -> assertEq(55L, Lab04.tailSum(10, 0)));
        check("tailSum(5, 10) == 25", () -> assertEq(25L, Lab04.tailSum(5, 10))); // 10+5+4+3+2+1
    }

    // ------------------------------------------------------------------
    // listSum
    // ------------------------------------------------------------------
    private static void testListSum() {
        section("listSum");

        check("null liste == 0", () -> assertEq(0, Lab04.listSum(null)));
        check("tek düğüm", () -> assertEq(5, Lab04.listSum(Lab04.buildList(5))));
        check("çoklu düğüm", () -> assertEq(15, Lab04.listSum(Lab04.buildList(1, 2, 3, 4, 5))));
        check("negatif içeren", () -> assertEq(0, Lab04.listSum(Lab04.buildList(-5, 10, -5))));
        check("sıfırlar", () -> assertEq(0, Lab04.listSum(Lab04.buildList(0, 0, 0))));
    }

    // ------------------------------------------------------------------
    // contains
    // ------------------------------------------------------------------
    private static void testContains() {
        section("contains");

        Node list = Lab04.buildList(1, 2, 3, 4, 5, 6);

        check("null listede yok", () -> assertEq(false, Lab04.contains(null, 1)));
        check("ilk eleman var", () -> assertEq(true, Lab04.contains(list, 1)));
        check("son eleman var", () -> assertEq(true, Lab04.contains(list, 6)));
        check("orta eleman var", () -> assertEq(true, Lab04.contains(list, 3)));
        check("olmayan değer", () -> assertEq(false, Lab04.contains(list, -123)));
        check("olmayan pozitif", () -> assertEq(false, Lab04.contains(list, 99)));

        Node single = Lab04.buildList(42);
        check("tek düğüm: var", () -> assertEq(true, Lab04.contains(single, 42)));
        check("tek düğüm: yok", () -> assertEq(false, Lab04.contains(single, 0)));
    }

    // ------------------------------------------------------------------
    // reverseList
    // ------------------------------------------------------------------
    private static void testReverseList() {
        section("reverseList");

        check("null -> null", () -> assertEq(true, Lab04.reverseList(null) == null));

        check("tek düğüm aynı kalır", () -> {
            Node r = Lab04.reverseList(Lab04.buildList(7));
            assertEq("7", listToString(r));
        });

        check("iki düğüm reverse", () -> {
            Node r = Lab04.reverseList(Lab04.buildList(1, 2));
            assertEq("2 -> 1", listToString(r));
        });

        check("üç düğüm reverse", () -> {
            Node r = Lab04.reverseList(Lab04.buildList(1, 2, 3));
            assertEq("3 -> 2 -> 1", listToString(r));
        });

        check("uzun liste reverse", () -> {
            Node r = Lab04.reverseList(Lab04.buildList(1, 2, 3, 4, 5));
            assertEq("5 -> 4 -> 3 -> 2 -> 1", listToString(r));
        });

        check("aynı değerler reverse", () -> {
            Node r = Lab04.reverseList(Lab04.buildList(9, 9, 1, 9));
            assertEq("9 -> 1 -> 9 -> 9", listToString(r));
        });
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------
    private static void section(String name) {
        System.out.println("--- " + name + " ---");
    }

    private static void check(String label, Case c) {
        total++;
        try {
            c.run();
            passed++;
            System.out.println("[PASS] " + label);
        } catch (AssertionError e) {
            failed++;
            System.out.println("[FAIL] " + label + " | " + e.getMessage());
        } catch (StackOverflowError e) {
            failed++;
            System.out.println("[FAIL] " + label + " | StackOverflowError (sonsuz özyineleme?)");
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] " + label + " | " + e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
    }

    private static void assertEq(long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError("expected " + expected + ", got " + actual);
        }
    }

    private static void assertEq(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("expected " + expected + ", got " + actual);
        }
    }

    private static void assertEq(boolean expected, boolean actual) {
        if (expected != actual) {
            throw new AssertionError("expected " + expected + ", got " + actual);
        }
    }

    private static void assertEq(String expected, String actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("expected \"" + expected + "\", got \"" + actual + "\"");
        }
    }

    private static String listToString(Node head) {
        if (head == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        Node curr = head;
        int guard = 0;
        while (curr != null) {
            if (guard++ > 10_000) {
                return sb.append("... (cycle?)").toString();
            }
            sb.append(curr.data);
            if (curr.next != null) {
                sb.append(" -> ");
            }
            curr = curr.next;
        }
        return sb.toString();
    }
}

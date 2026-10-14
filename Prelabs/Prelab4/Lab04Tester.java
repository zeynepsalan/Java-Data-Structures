import java.util.IdentityHashMap;

/** Behavioral practice grader: 45 independent cases, 150 points. */
public class Lab04Tester {
    private static int score;
    private static int passed;
    private static int total;

    @FunctionalInterface
    private interface TestCase { boolean run(); }

    public static void main(String[] args) {
        System.out.println("CSE 201 - Data Structures | Practice Lab 04: Recursion");

        check("factorial: zero", 3, () -> Lab04.factorial(0) == 1);
        check("factorial: six", 3, () -> Lab04.factorial(6) == 720);
        check("factorial: negative rejected", 4, () -> rejects(() -> Lab04.factorial(-1)));

        check("sum: empty prefix", 3, () -> Lab04.sum(new int[]{5, 8}, 0) == 0);
        check("sum: partial prefix", 3, () -> Lab04.sum(new int[]{4, -2, 9}, 2) == 2);
        check("sum: whole array and invalid n", 4, () ->
                Lab04.sum(new int[]{4, -2, 9}, 3) == 11 &&
                rejects(() -> Lab04.sum(new int[]{1}, 2)));

        check("power: exponent zero", 3, () -> Lab04.power(0, 0) == 1);
        check("power: odd and even", 3, () ->
                Lab04.power(-3, 5) == -243 && Lab04.power(2, 10) == 1024);
        check("power: large exponent and negative rejected", 4, () ->
                Lab04.power(1, 1_000_000) == 1 && rejects(() -> Lab04.power(2, -1)));

        int[] sorted = {-8, -1, 0, 3, 7, 12};
        check("binarySearch: first and last", 3, () ->
                Lab04.binarySearch(sorted, -8, 0, 5) == 0 &&
                Lab04.binarySearch(sorted, 12, 0, 5) == 5);
        check("binarySearch: missing", 3, () ->
                Lab04.binarySearch(sorted, 4, 0, 5) == -1);
        check("binarySearch: subrange and empty range", 4, () ->
                Lab04.binarySearch(sorted, -8, 1, 4) == -1 &&
                Lab04.binarySearch(sorted, 7, 1, 4) == 4 &&
                Lab04.binarySearch(sorted, 3, 2, 1) == -1);

        check("reverse: empty", 3, () -> "".equals(Lab04.reverse("")));
        check("reverse: one character", 3, () -> "A".equals(Lab04.reverse("A")));
        check("reverse: word", 4, () -> "AYLATNA".equals(Lab04.reverse("ANTALYA")));

        check("fib: base cases", 3, () ->
                Lab04.fib(0) == 0 && Lab04.fib(1) == 1);
        check("fib: eighth", 3, () -> Lab04.fib(8) == 21);
        check("fib: 35 and negative rejected", 4, () ->
                Lab04.fib(35) == 9_227_465 && rejects(() -> Lab04.fib(-1)));

        check("tailSum: zero keeps accumulator", 3, () -> Lab04.tailSum(0, 17) == 17);
        check("tailSum: positive", 3, () -> Lab04.tailSum(8, 0) == 36);
        check("tailSum: offset and negative rejected", 4, () ->
                Lab04.tailSum(4, 10) == 20 && rejects(() -> Lab04.tailSum(-1, 0)));

        check("listSum: empty", 3, () -> Lab04.listSum(null) == 0);
        check("listSum: singleton", 3, () -> Lab04.listSum(list(-4)) == -4);
        check("listSum: mixed values", 4, () -> Lab04.listSum(list(3, -7, 10, 0)) == 6);

        check("contains: empty", 3, () -> !Lab04.contains(null, 2));
        check("contains: first and last", 3, () ->
                Lab04.contains(list(1, 2, 3), 1) && Lab04.contains(list(1, 2, 3), 3));
        check("contains: absent", 4, () -> !Lab04.contains(list(1, 2, 3), 4));

        check("reverseList: empty", 3, () -> Lab04.reverseList(null) == null);
        check("reverseList: singleton identity", 3, () -> {
            Node node = list(9);
            return Lab04.reverseList(node) == node && node.next == null;
        });
        check("reverseList: links, identity, no cycle", 4, () -> {
            Node first = list(1, 2, 3, 4);
            Node second = first.next, third = second.next, fourth = third.next;
            Node result = Lab04.reverseList(first);
            return result == fourth && fourth.next == third &&
                    third.next == second && second.next == first &&
                    first.next == null && hasExactlyFourNodes(result);
        });

        check("gcd: zero", 3, () -> Lab04.gcd(0, 0) == 0);
        check("gcd: common divisor", 3, () -> Lab04.gcd(54, 24) == 6);
        check("gcd: order and negatives", 4, () ->
                Lab04.gcd(17, 5) == 1 && Lab04.gcd(24, 54) == 6 &&
                rejects(() -> Lab04.gcd(-2, 4)));

        check("isPalindrome: empty and one character", 3, () ->
                Lab04.isPalindrome("") && Lab04.isPalindrome("x"));
        check("isPalindrome: even and odd", 3, () ->
                Lab04.isPalindrome("abba") && Lab04.isPalindrome("racecar"));
        check("isPalindrome: mismatch", 4, () ->
                !Lab04.isPalindrome("recursion") && !Lab04.isPalindrome("abca"));

        check("max: singleton", 3, () -> Lab04.max(new int[]{-7}, 1) == -7);
        check("max: mixed signs", 3, () -> Lab04.max(new int[]{-8, -2, -5, 4}, 4) == 4);
        check("max: prefix and invalid sizes", 4, () ->
                Lab04.max(new int[]{9, 3, 7}, 2) == 9 &&
                rejects(() -> Lab04.max(new int[]{1}, 0)));

        check("countOccurrences: empty prefix", 3, () ->
                Lab04.countOccurrences(new int[]{2, 2}, 2, 0) == 0);
        check("countOccurrences: matches", 3, () ->
                Lab04.countOccurrences(new int[]{2, 1, 2, 2, 3}, 2, 5) == 3);
        check("countOccurrences: prefix and invalid size", 4, () ->
                Lab04.countOccurrences(new int[]{2, 1, 2}, 2, 2) == 1 &&
                rejects(() -> Lab04.countOccurrences(new int[]{1}, 1, 2)));

        check("toBinary: zero", 3, () -> "0".equals(Lab04.toBinary(0)));
        check("toBinary: powers and odd values", 3, () ->
                "1000".equals(Lab04.toBinary(8)) && "101101".equals(Lab04.toBinary(45)));
        check("toBinary: negative rejected", 4, () -> rejects(() -> Lab04.toBinary(-1)));

        System.out.printf("%nScore: %d / 150 | Passed: %d / %d cases%n", score, passed, total);
    }

    private static void check(String label, int points, TestCase test) {
        total++;
        try {
            if (test.run()) {
                score += points;
                passed++;
                System.out.printf("[PASS] +%d %s%n", points, label);
            } else {
                System.out.printf("[FAIL] +0/%d %s%n", points, label);
            }
        } catch (Throwable error) {
            System.out.printf("[FAIL] +0/%d %s (%s)%n", points, label,
                    error.getClass().getSimpleName());
        }
    }

    private static boolean rejects(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException expected) {
            return true;
        }
    }

    private static Node list(int... values) {
        Node head = null, tail = null;
        for (int value : values) {
            Node node = new Node(value);
            if (head == null) head = node;
            else tail.next = node;
            tail = node;
        }
        return head;
    }

    private static boolean hasExactlyFourNodes(Node head) {
        IdentityHashMap<Node, Boolean> seen = new IdentityHashMap<>();
        int count = 0;
        while (head != null) {
            if (seen.put(head, true) != null) return false;
            count++;
            head = head.next;
        }
        return count == 4;
    }
}

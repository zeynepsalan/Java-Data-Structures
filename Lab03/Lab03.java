package Lab03;

public class Lab03 {
    // 1298

    // For each method below, determine a Big-O asymptotic bound
    // Write your answer within quotes for answer variable.
    // Choose and use one of these complexities as your answers:
    // O(1), O(logn), O(n), O(nlogn), O(n^2), O(n^3), O(2^n)
    // Make sure to use whatever within paranthesis, for example "logn" for O(logn)
    // answer

    public static long cubeWalk(int[] a) {
        String answer = "n^3";
        System.out.printf("Your answer: O(%s)\n", answer);

        long s = 0;
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    s += a[(i + j + k) % n];
                }
            }
        }
        return s;
    }

    public static long halvingProbe(int[] a) {
        String answer = "logn";
        System.out.printf("Your answer: O(%s)\n", answer);

        long s = 0;
        int n = a.length;
        int m = Math.max(1, n);
        while (m > 1) {
            int idx = m % n;
            s += a[idx];
            m = m / 2;
        }
        return s;
    }

    public static long subsetXor(int[] a) {
        String answer = "2^n";
        System.out.printf("Your answer: O(%s)\n", answer);

        return subsetXorHelper(a, 0, 0L);
    }

    private static long subsetXorHelper(int[] a, int idx, long acc) {
        if (idx == a.length) {
            return acc;
        }
        long left = subsetXorHelper(a, idx + 1, acc + a[idx]);
        long right = subsetXorHelper(a, idx + 1, acc - a[idx]);
        return left ^ right;
    }

    public static long triangularPairs(int[] a) {
        String answer = "n^2";
        System.out.printf("Your answer: O(%s)\n", answer);

        long s = 0;
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                s += a[(i + j) % n];
            }
        }
        return s;
    }

    public static long touchFew(int[] a) {
        String answer = "1";
        System.out.printf("Your answer: O(%s)\n", answer);

        long s = 0;
        int n = a.length;
        if (n > 0) {
            s += a[0];
        }
        if (n > 1) {
            s += a[n - 1];
        }
        if (n > 2) {
            s += a[n / 2];
        }
        return s;
    }

    public static long phasedSweeps(int[] a) {
        String answer = "nlogn";
        System.out.printf("Your answer: O(%s)\n", answer);

        long s = 0;
        int n = a.length;
        for (int stride = 1; stride <= Math.max(1, n); stride = stride * 2) {
            for (int i = 0; i < n; i++) {
                s += a[i];
            }
        }
        return s;
    }

    public static long singleScan(int[] a) {
        String answer = "n";
        System.out.printf("Your answer: O(%s)\n", answer);

        long s = 0;
        int n = a.length;
        for (int i = 0; i < n; i++) {
            s += a[i];
        }
        return s;
    }

    // For each of the following questions, write the time complexity (Big-O
    // notation)
    // of the specified operation for both an Array and a Singly Linked List (node
    // implementation).
    // Use one of the following forms for your answers:
    // O(1), O(n), O(n+m)

    // Q1: What is the time complexity of accessing an element by index?
    public static void question1() {
        String arrayAnswer = "1";
        String sllAnswer = "n";
        System.out.printf("Your answer for Array: O(%s)\n", arrayAnswer);
        System.out.printf("Your answer for SLL: O(%s)\n", sllAnswer);
    }

    // Q2: What is the time complexity of searching an element in an unsorted
    // structure?
    public static void question2() {
        String arrayAnswer = "n";
        String sllAnswer = "n";
        System.out.printf("Your answer for Array: O(%s)\n", arrayAnswer);
        System.out.printf("Your answer for SLL: O(%s)\n", sllAnswer);
    }

    // Q3: What is the time complexity of inserting an element at the end?
    public static void question3() {
        String arrayAnswer = "1";
        String sllAnswer = "n";
        System.out.printf("Your answer for Array: O(%s)\n", arrayAnswer);
        System.out.printf("Your answer for SLL: O(%s)\n", sllAnswer);
    }

    // Q4: What is the time complexity of deleting the first element?
    public static void question4() {
        String arrayAnswer = "n";
        String sllAnswer = "1";
        System.out.printf("Your answer for Array: O(%s)\n", arrayAnswer);
        System.out.printf("Your answer for SLL: O(%s)\n", sllAnswer);
    }

    // Q5: What is the time complexity of concatenating two structures (SLL has a
    // tail pointer)?
    public static void question5() {
        String arrayAnswer = "n+m";
        String sllAnswer = "1";
        System.out.printf("Your answer for Array: O(%s)\n", arrayAnswer);
        System.out.printf("Your answer for SLL (with tail pointer): O(%s)\n", sllAnswer);
    }
}

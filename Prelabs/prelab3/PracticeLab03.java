import java.util.ArrayList;

public class PracticeLab03 {

    // CSE 201 - Data Structures | Practice Lab 03: Algorithm Analysis
    //
    // For every question, write your Big-O answer inside the quotes of answer.
    // Allowed forms: 1, logn, n, nlogn, n^2, n^3, 2^n, n+m, n*m.
    // Do not change method signatures or print statements.

    public static long question01(int[] a) {
        String answer = "1";
        System.out.printf("Q01: O(%s)%n", answer);

        if (a.length == 0) {
            return 0;
        }

        return a[0] + a[a.length - 1];
    }

    public static long question02(int[] a) {
        String answer = "n";
        System.out.printf("Q02: O(%s)%n", answer);

        long sum = 0;

        for (int value : a) {
            sum += value;
        }

        return sum;
    }

    public static long question03(int[] a) {
        String answer = "n^2";
        System.out.printf("Q03: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j <= i; j++) {
                sum += a[j];
            }
        }

        return sum;
    }

    public static long question04(int[] a) {
        String answer = "n^2";
        System.out.printf("Q04: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                sum += a[i] * a[j];
            }
        }

        return sum;
    }

    public static long question05(int[] a) {
        String answer = "n^3";
        System.out.printf("Q05: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                for (int k = 0; k < a.length; k++) {
                    int index = (i + j + k) % a.length;
                    sum += a[index];
                }
            }
        }

        return sum;
    }

    public static long question06(int[] a) {
        String answer = "logn";
        System.out.printf("Q06: O(%s)%n", answer);

        if (a.length == 0) {
            return 0;
        }

        long sum = 0;
        int size = a.length;

        while (size > 1) {
            int index = size % a.length;
            sum += a[index];
            size /= 2;
        }

        return sum;
    }

    public static long question07(int[] a) {
        String answer = "nlogn";
        System.out.printf("Q07: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            int size = a.length;

            while (size > 1) {
                sum += a[i];
                size /= 2;
            }
        }

        return sum;
    }

    public static long question08(int[] a) {
        String answer = "2^n";
        System.out.printf("Q08: O(%s)%n", answer);

        return branch(a, 0);
    }

    private static long branch(int[] a, int index) {
        if (index == a.length) {
            return 1;
        }

        long left = branch(a, index + 1);
        long right = branch(a, index + 1);

        return left + right;
    }

    public static long question09(int[] a, int[] b) {
        String answer = "n*m";
        System.out.printf("Q09: O(%s)%n", answer);

        long sum = 0;

        for (int x : a) {
            for (int y : b) {
                sum += x + y;
            }
        }

        return sum;
    }

    public static long question10(int[] a, int[] b) {
        String answer = "n+m";
        System.out.printf("Q10: O(%s)%n", answer);

        long sum = 0;

        for (int x : a) {
            sum += x;
        }

        for (int y : b) {
            sum += y;
        }

        return sum;
    }

    public static long question11(int[] a) {
        String answer = "n^2";
        System.out.printf("Q11: O(%s)%n", answer);

        long pairs = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                pairs += a[i] + a[j];
            }
        }

        return pairs;
    }

    public static long question12(int[] a) {
        String answer = "n";
        System.out.printf("Q12: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i += 2) {
            sum += a[i];
        }

        return sum;
    }

    public static long question13(int[] a) {
        String answer = "logn";
        System.out.printf("Q13: O(%s)%n", answer);

        long sum = 0;
        int width = 1;

        while (width < a.length) {
            sum += width;
            width *= 2;
        }

        return sum;
    }

    public static long question14(int[] a) {
        String answer = "n^2";
        System.out.printf("Q14: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                sum += a[j];
            }
        }

        return sum;
    }

    public static long question15(int[] a) {
        String answer = "n";
        System.out.printf("Q15: O(%s)%n", answer);

        return linearRecursion(a, 0);
    }

    private static long linearRecursion(int[] a, int index) {
        if (index == a.length) {
            return 0;
        }

        return a[index] + linearRecursion(a, index + 1);
    }

    public static long question16(int[] a) {
        String answer = "n";
        System.out.printf("Q16: O(%s)%n", answer);

        long sum = 0;
        int left = 0;
        int right = a.length - 1;

        while (left <= right) {
            sum += a[left];
            left++;
            right--;
        }

        return sum;
    }

    public static long question17(int[] a) {
        String answer = "n";
        System.out.printf("Q17: O(%s)%n", answer);

        long sum = 0;
        int i = 1;

        while (i < a.length) {
            for (int j = 0; j < i; j++) {
                sum += j;
            }

            i *= 2;
        }

        return sum;
    }

    public static long question18(int[] a) {
        String answer = "nlogn";
        System.out.printf("Q18: O(%s)%n", answer);

        long sum = 0;
        int step = 1;

        while (step <= a.length) {
            for (int value : a) {
                sum += value;
            }

            step *= 2;
        }

        return sum;
    }

    public static long question19(int[] a) {
        String answer = "n";
        System.out.printf("Q19: O(%s)%n", answer);

        String text = "";

        for (int value : a) {
            text += value;
        }

        return text.length();
    }

    public static long question20(ArrayList<Integer> list) {
        String answer = "n^2";
        System.out.printf("Q20: O(%s)%n", answer);

        long count = 0;

        for (Integer value : list) {
            if (list.contains(value)) {
                count++;
            }
        }

        return count;
    }

    public static long question21(int[] a) {
        String answer = "n^3";
        System.out.printf("Q21: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < j; k++) {
                    sum++;
                }
            }
        }

        return sum;
    }

    public static long question22(int[] a) {
        String answer = "nlogn";
        System.out.printf("Q22: O(%s)%n", answer);

        long sum = 0;
        int size = a.length;

        while (size > 0) {
            for (int value : a) {
                sum += value;
            }

            size /= 2;
        }

        return sum;
    }

    public static long question23(int[] a, int[] b) {
        String answer = "n+m";
        System.out.printf("Q23: O(%s)%n", answer);

        long sum = 0;

        for (int x : a) {
            sum += x;
        }

        for (int y : b) {
            sum += y;
        }

        for (int x : a) {
            sum += x;
        }

        return sum;
    }

    public static long question24(int[] a) {
        String answer = "nlogn";
        System.out.printf("Q24: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            int index = a.length - 1;

            while (index > 0) {
                sum += a[index];
                index /= 2;
            }
        }

        return sum;
    }

    public static long question25(int[] a) {
        String answer = "2^n";
        System.out.printf("Q25: O(%s)%n", answer);

        return fibonacciLike(a.length);
    }

    private static long fibonacciLike(int n) {
        if (n <= 1) {
            return n;
        }

        long previous = fibonacciLike(n - 1);
        long beforePrevious = fibonacciLike(n - 2);

        return previous + beforePrevious;
    }

    public static long question26(int[] a) {
        String answer = "n^2";
        System.out.printf("Q26: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                for (int k = 0; k < 5; k++) {
                    sum += a[i] + a[j];
                }
            }
        }

        return sum;
    }

    public static long question27(int[] a) {
        String answer = "n";
        System.out.printf("Q27: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }

        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }

        return sum;
    }

    public static long question28(int[] a, int[] b) {
        String answer = "n*m";
        System.out.printf("Q28: O(%s)%n", answer);

        long count = 0;

        for (int x : a) {
            for (int y : b) {
                if (x == y) {
                    count++;
                }
            }
        }

        return count;
    }

    public static long question29(int[] a) {
        String answer = "n";
        System.out.printf("Q29: O(%s)%n", answer);

        long sum = 0;

        for (int i = 0; i < a.length; i++) {
            if (a[i] % 2 == 0) {
                sum += a[i];
            } else {
                sum -= a[i];
            }
        }

        return sum;
    }

    public static long question30(int[] a) {
        String answer = "1";
        System.out.printf("Q30: O(%s)%n", answer);

        if (a.length < 3) {
            return a.length;
        }

        int middle = a.length / 2;

        return a[0] + a[middle] + a[a.length - 1];
    }
}

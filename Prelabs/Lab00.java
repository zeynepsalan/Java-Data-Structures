import java.util.Arrays;
import java.util.ArrayList;

public class Lab00 {
    public static void main(String[] args) {
        // int[] arr = { 3, 12, -5, 7, 0, 7, 8, 32, 7, 31 };
        // int[] arr2 = { 1, 2, 3, 4 };

        // System.out.println(countOccurrences(arr, 7));
        // System.out.println(secondLargest(arr));
        // int[] x = runningSum(arr2);
        // for (int i = 0; i < x.length; i++) {
        // System.out.println(x[i]);
        // }
        // System.out.println(isAnagram("zeynep", "Penyez"));
        // System.out.println(compress("aaabcca"));
        // int[][] m = { { 1, 2 }, { 3, 4 }, { 5, 6 } };
        // int[][] matrix = transpose(m);
        // for (int i = 0; i < matrix.length; i++) {
        // System.out.print("[ ");
        // for (int j = 0; j < matrix[i].length; j++) {
        // System.out.print(matrix[i][j] + " ");
        // }
        // System.out.println("]");
        // }

        ArrayList<Integer> primes = primesUpTo(0);
        System.out.print("[ ");
        for (Integer integer : primes) {
            System.out.print(integer + " ");
        }
        System.out.print("]");

    }

    public static int countOccurrences(int[] a, int x) {
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            if (x == a[i]) {
                count++;
            }
        }
        return count;
    }

    public static int secondLargest(int[] a) {
        int x = Integer.MIN_VALUE;
        int y = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > x) {
                y = x;
                x = a[i];
            }
            if (a[i] > y && a[i] != x) {
                y = a[i];
            }
        }
        return y;
    }

    public static int[] runningSum(int[] a) {
        if (a == null)
            throw new IllegalArgumentException();
        int[] b = a.clone();
        int count = 0;
        for (int i = 0; i < b.length; i++) {
            count += b[i];
            b[i] = count;
        }
        return b;
    }

    public static String reverseWords(String s) {
        if (s == null)
            return null;
        if (s.trim().isEmpty())
            return "";

        String newSentence = "";
        for (int i = 0; i < s.length(); i++) {
            // System.out.println(s.charAt(i));
            if (s.charAt(i) != ' ') {

                for (int j = i; j < s.length(); j++) {
                    // System.out.println(s.charAt(j));

                    if (s.charAt(j) == ' ') {
                        newSentence += s.substring(i, j);
                        newSentence += " ";
                        i = j;
                        break;
                    }

                    if (j == s.length() - 1) {
                        newSentence += s.substring(i);
                        i = j;
                        break;
                    }

                }
            }
        }
        String[] arr = newSentence.split(" ");
        // System.out.println(arr.length);
        // for (int i = 0; i < arr.length; i++) {
        // System.out.println(arr[i]);
        // }

        String x = "";
        for (int i = arr.length - 1; i >= 0; i--) {
            // System.out.println(arr[i]);
            if (i == 0) {
                x += arr[i];
                break;
            }
            x += arr[i] + " ";
        }

        return x;
    }

    public static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        char[] a_chars = new char[a.length()];
        char[] b_chars = new char[b.length()];

        a = a.toLowerCase();
        b = b.toLowerCase();
        for (int i = 0; i < a.length(); i++) {
            a_chars[i] = a.charAt(i);
        }
        for (int i = 0; i < b.length(); i++) {
            b_chars[i] = b.charAt(i);
        }

        for (int i = 0; i < a_chars.length; i++) {
            for (int j = 0; j < a_chars.length - 1; j++) {
                if (a_chars[j] < a_chars[j + 1]) {
                    char c = a_chars[j];
                    a_chars[j] = a_chars[j + 1];
                    a_chars[j + 1] = c;
                }
            }
        }
        for (int i = 0; i < b_chars.length; i++) {
            for (int j = 0; j < b_chars.length - 1; j++) {
                if (b_chars[j] < b_chars[j + 1]) {
                    char c = b_chars[j];
                    b_chars[j] = b_chars[j + 1];
                    b_chars[j + 1] = c;
                }
            }
        }
        // for (int i = 0; i < a_chars.length; i++) {
        // if (a_chars[i] != b_chars[i]) {
        // return false;
        // }
        // }
        if (Arrays.equals(a_chars, b_chars)) {
            return true;
        }
        return false;
    }

    public static String compress(String s) {
        String compressed = "";
        int count = 0;
        char current_char = s.charAt(0);
        for (int i = 0; i < s.length(); i++) {
            // System.out.println("for: " + i + ".");
            if (current_char == s.charAt(i)) {
                // System.out.println(i + ". if: count: " + count);
                // System.out.println(i + ". if: char: " + current_char);
                count++;
            } else {
                // System.out.println(i + ". else: count: " + count);
                // System.out.println(i + ". else: char: " + current_char);
                compressed += current_char + "" + count;
                current_char = s.charAt(i);
                count = 1;
            }
        }
        compressed += current_char + "" + count;
        return compressed;
    }

    public static int[][] transpose(int[][] m) {
        if (m == null || m.length == 0 || m[0].length == 0)
            throw new IllegalArgumentException();

        int expectedLength = m[0].length;
        for (int i = 1; i < m.length; i++) {
            if (m[i] == null || m[i].length != expectedLength) {
                throw new IllegalArgumentException();
            }
        }

        int[][] matrix = new int[m[0].length][m.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = m[j][i];
            }
        }
        return matrix;
    }

    public static ArrayList<Integer> primesUpTo(int n) {
        ArrayList<Integer> primes = new ArrayList<>();
        if (n < 2) {
            return primes;
        }
        for (int i = 2; i <= n; i++) {
            boolean isPrime = true;
            for (int j = 2; j <= i / 2; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime)
                primes.add(i);
        }
        return primes;
    }
}

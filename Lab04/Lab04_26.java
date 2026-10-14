package Lab04;

import java.util.ArrayList;
import java.util.List;

public class Lab04_26 {

    public static void main(String[] args) {

        System.out.println("Reversed: " + reverse(null));
        System.out.println("Sum Digits: " + sumDigits(-904512));
        System.out.println("gcd: " + gcd(53, 18));
        System.out.println("Palindrome: " + isPalindrome("Kayak"));
        int[] arr = { -3, 2, 0, 1, -3 };
        System.out.println("Binary Search: " + binarySearch(null, 250));
        System.out.println("Power: " + power(2, 10) + " count: " + powerCalls);
        System.out.println("Count Occurrences: " + countOccurrences(arr, -3));

    }

    public static String reverse(String s) {
        if (s == null) {
            return null;
        }
        if (s == "") {
            return "";
        }

        return s.substring(s.length() - 1) + reverse(s.substring(0, s.length() - 1));
    }

    public static int sumDigits(int n) {
        if (n < 0) {
            return -1;
        }
        if (n == 0) {
            return 0;
        }
        return n % 10 + sumDigits(n / 10);
    }

    public static int gcd(int a, int b) {
        if (a < 0 || b < 0) {
            return -1;
        }
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static int countOccurrences(int[] a, int x) {
        if (a == null) {
            return 0;
        }
        if (a.length == 0) {
            return 0;
        }
        return countOccurrencesHelper(a, x, a.length - 1);
    }

    public static int countOccurrencesHelper(int[] a, int x, int index) {
        if (index < 0) {
            return 0;
        }
        int count = countOccurrencesHelper(a, x, index - 1);
        if (a[index] == x) {
            count++;
        }
        return count;
    }

    public static boolean isPalindrome(String s) {
        if (s == null) {
            return false;
        }
        if (s == "") {
            return true;
        }
        if (s.length() == 1) {
            return true;
        }
        if (s.charAt(0) != s.charAt(s.length() - 1)) {
            return false;
        }
        return isPalindrome(s.substring(1, s.length() - 1));
    }

    public static int binarySearch(int[] a, int target) {
        if (a == null) {
            return -1;
        }
        if (a.length == 0) {
            return -1;
        }
        return binarySearchHelper(a, target, 0, a.length - 1);
    }

    public static int binarySearchHelper(int[] a, int target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (a[mid] == target) {
            return mid;
        } else if (a[mid] < target) {
            return binarySearchHelper(a, target, mid + 1, high);
        } else if (a[mid] > target) {
            return binarySearchHelper(a, target, low, mid - 1);
        } else {
            return -1;
        }
    }

    public static long power(long base, int exp) {
        if (exp == 0) {
            powerCalls++;
            return 1;
        }
        long a = power(base, exp / 2);
        if (exp % 2 == 1) {
            powerCalls++;
            return a * a * base;
        } else {
            powerCalls++;
            return a * a;
        }
    }

    public static int countPaths(boolean[][] blocked) {
        if (blocked == null || blocked.length == 0) {
            return 0;
        }
        return 1;
    }

    public static int countPathsHelper(boolean[][] blocked, int x, int y) {
        if (x < 0 | x >= blocked.length || y < 0 || y >= blocked[x].length || blocked[x][y] == true) {
            return 0;
        }
        if (x == blocked.length - 1 && y == blocked[0].length - 1) {
            return 1;
        }

        int xPath = countPathsHelper(blocked, x + 1, y);
        int yPath = countPathsHelper(blocked, x, y + 1);

        return xPath + yPath;
    }

    public static List<String> subsets(String letters) {
        if (letters == null) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        return subsetsHelper(letters, list);
    }

    public static List<String> subsetsHelper(String letters, List<String> list) {
        if (letters.length() == 0) {
            list.add(letters);
            return list;
        }
        if (letters.length() == 1) {
            list.add(letters);
            return list;
        }

        subsetsHelper(letters.substring(1), list);
    }

    /**
     * Provided. Your power method must increase this by one at the start of
     * every call, including every recursive call. Never reset it; the tests
     * do that themselves. Do not rename it, and do not remove it.
     */
    public static int powerCalls;
}

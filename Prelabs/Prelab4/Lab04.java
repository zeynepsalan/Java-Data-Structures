/**
 * CSE 201 - Data Structures | Practice Lab 04: Recursion
 *
 * Complete each TODO using recursion. Keep the public signatures unchanged.
 * The methods are deliberately unfinished so the tester can compile before
 * you start. Read PracticeLab04.pdf for the full contracts and constraints.
 */
public class Lab04 {
    public static void main(String[] args) {
        System.out.println(fib(0));
        System.out.println(fib(1));
        System.out.println(fib(2));
        System.out.println(fib(3));
        System.out.println(fib(4));
        System.out.println(fib(5));
        System.out.println(fib(6));
        System.out.println(fib(7));
        System.out.println(fib(8));
        System.out.println(fib(9));
        System.out.println(fib(10));
        System.out.println(fib(11));
        System.out.println(fib(12));
        System.out.println(toBinary(675));
    }

    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
        // TODO: 0! = 1. Reject negative n with IllegalArgumentException.
    }

    public static int sum(int[] values, int n) {
        if (n == 0) {
            return 0;
        }
        if (n < 0 || n > values.length) {
            throw new IllegalArgumentException();
        }
        // TODO: sum the first n elements. Validate 0 <= n <= values.length
        return values[n - 1] + sum(values, n - 1);
    }

    public static long power(long base, int exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException();
        }
        if (exponent == 0) {
            return 1;
        }
        // TODO: exponentiation by squaring; exponent must be nonnegative.
        return base * power(base, exponent - 1);
    }

    public static int binarySearch(int[] values, int target, int low, int high) {
        // TODO: search the inclusive [low, high] range of a sorted array.
        if (low < 0 || high >= values.length) {
            throw new IllegalArgumentException();
        }
        if (high < low) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (values[mid] == target) {
            return mid;
        } else if (values[mid] < target) {
            return binarySearch(values, target, mid + 1, high);
        } else if (values[mid] > target) {
            return binarySearch(values, target, low, mid - 1);
        } else {
            return -1;
        }

    }

    public static String reverse(String text) {
        // TODO: recursively reverse a string; the empty string stays empty
        if (text == null) {
            return null;
        }
        if (text.length() == 0) {
            return "";
        }
        return text.substring(text.length() - 1) + reverse(text.substring(0, text.length() - 1));
    }

    public static long fib(int n) {
        // TODO: F(0) = 0, F(1) = 1; consider a helper for O(n) calls.
        return fibHelp(n)[1];
    }

    // 0 n-1 1 n
    // 0 1 1 2 3 5 8
    public static long[] fibHelp(int n) {
        long[] a = new long[2];

        if (n <= 0) {
            a[0] = 0;
            a[1] = 0;
            return a;
        }

        if (n == 1) {
            a[0] = 0;
            a[1] = 1;
            return a;
        }

        long[] prev = fibHelp(n - 1);
        // prev[0] => n-2
        // prev[1] => n-1

        long toplam = prev[0] + prev[1];
        a[0] = prev[1];
        a[1] = toplam;

        return a;
    }

    public static long tailSum(int n, long accumulator) {
        // TODO: return accumulator + 1 + ... + n using a tail call.
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n <= 0) {
            return accumulator;
        }
        return tailSum(n - 1, accumulator + n);
    }

    public static int listSum(Node head) {
        // TODO: recursively sum nodes; a null head has sum zero.
        if (head == null) {
            return 0;
        }
        if (head.next == null) {
            return head.data;
        }
        return head.data + listSum(head.next);
    }

    public static boolean contains(Node head, int target) {
        // TODO: recursively search nodes; null means false.
        if (head == null) {
            return false;
        }
        if (head.data == target) {
            return true;
        }
        return contains(head.next, target);
    }

    public static Node reverseList(Node head) {
        // TODO: reverse links in place using recursion. Allocate no new nodes.
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return head;
        }
        Node a = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return a;
    }

    public static int gcd(int a, int b) {
        // TODO: Euclid's algorithm; inputs must be nonnegative.
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException();
        }
        if (a < b)
            return gcd(b, a);
        if (a % b == 0) {
            return b;
        }

        return 0;
    }

    public static boolean isPalindrome(String text) {
        if (text == null) {
            return false;
        }
        if (text == "") {
            return true;
        }
        if (text.length() == 1) {
            return true;
        }
        if (text.charAt(0) != text.charAt(text.length() - 1)) {
            return false;
        }
        // TODO: compare inward recursively; the empty string is a palindrome.
        return isPalindrome(text.substring(1, text.length() - 1));
    }

    public static int max(int[] values, int n) {
        if (n < 1 || n > values.length) {
            throw new IllegalArgumentException();
        }
        if (n == 1) {
            return values[0];
        }
        if (n == 2) {
            return Math.max(values[n - 1], values[n - 2]);
        }
        return Math.max(values[n - 1], max(values, n - 1));
    }

    public static int countOccurrences(int[] values, int target, int n) {
        // TODO: count target in the first n elements; require 0 <= n <= length.
        if (values == null) {

        }
        if (n < 0 || n > values.length) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return 0;
        }
        int count = countOccurrences(values, target, n - 1);
        if (values[n - 1] == target) {
            count++;
        }
        return count;
    }

    public static String toBinary(int n) {
        // TODO: convert a nonnegative integer to binary using recursion.
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return "0";
        }
        if (n == 1) {
            return "1";
        }

        String binary = toBinary(n / 2);
        binary += n % 2;
        return binary;
    }
}

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

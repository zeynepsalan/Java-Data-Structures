package Lab04;

public class Lab04 {

    public static void main(String[] args) {
        Node a = buildList(1);
        System.out.println(factorial(2));
        int[] arr = { 2, 3, 5, 6, 9, 10, 11, 15, 20 };
        System.out.println(binarySearch(arr, 20, 0, arr.length - 1));
    }

    // Implement each function below recursively.

    public static long factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static int sum(int[] arr, int n) {
        // TODO: implement recursive array sum
        if (n == 0)
            return arr[0];
        return arr[n] + sum(arr, n - 1);
    }

    public static long power(long base, int exp) {
        // TODO: implement recursive fast power,
        if (exp == 0) {
            return 1;
        }
        return base * power(base, exp - 1);
    }

    public static int binarySearch(int[] arr, int target, int low, int high) {
        if (low < 0 || high >= arr.length)
            return -1;
        if (low > high) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (target > arr[mid]) {
            return binarySearch(arr, target, mid + 1, high);
        } else if (target < arr[mid]) {
            return binarySearch(arr, target, low, mid - 1);
        } else {
            return -1;
        }
    }

    public static String reverse(String s) {
        if (s.length() == 0) {
            return "";
        }
        // TODO: implement recursive string reversal
        if (s.length() == 1) {
            return s.substring(0, 1);
        }
        return s.substring(s.length() - 1) + reverse(s.substring(0, s.length() - 1));
    }

    String s = "0, 1, 1, 2, 3, 5, 8, 13, 21";

    public static long fib(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static long tailSum(int n, long acc) {
        if (n == 0) {
            return acc;
        }
        acc += n;
        return tailSum(n - 1, acc);
    }

    public static int listSum(Node head) {
        // TODO: recursively sum linked list elements
        if (head == null) {
            return 0;
        }
        return head.data + listSum(head.next);
    }

    public static boolean contains(Node head, int target) {
        // TODO: recursively search for target value
        if (head == null) {
            return false;
        }
        if (head.data == target) {
            return true;
        }
        return contains(head.next, target);
    }

    public static Node reverseList(Node head) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return head;
        }
        Node a = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        head = a;
        return head;

        // TODO: recursively reverse the linked list
    }

    public static Node node(Node head) {
        while (head.next != null) {
            head = head.next;
        }
        return head;
    }

    public static Node buildList(int... vals) {
        if (vals.length == 0) {
            return null;
        } else {
            Node head = new Node(vals[0]);
            Node curr = head;
            for (int i = 1; i < vals.length; i++) {
                curr.next = new Node(vals[i]);
                curr = curr.next;
            }
            return head;
        }
    }
}

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

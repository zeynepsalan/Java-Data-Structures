package Recursion;

public class ReverseStrings {

    public static void main(String[] args) {
        System.out.println(reverseString("abcd"));
    }

    public static String reverseString(String s) {
        if (s.length() == 1) {
            return s.substring(0, 1);
        }
        return s.substring(s.length() - 1) + reverseString(s.substring(0, s.length() - 1));
    }
}

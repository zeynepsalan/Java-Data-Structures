/**
 * CSE201 Post-Lab 1 - provided helper. Do not modify or submit this file.
 *
 * Formats an amount of whole kurus in the Turkish style, for example
 * 123450 kurus becomes "1.234,50 TL".
 */
public final class Money {

    private Money() {
    }

    /** Formats kurus as Turkish lira, for example "1.234,50 TL". */
    public static String format(long kurus) {
        boolean negative = kurus < 0;
        long absolute = negative ? -kurus : kurus;

        long lira = absolute / 100;
        long cents = absolute % 100;

        StringBuilder sb = new StringBuilder(groupThousands(lira));
        sb.append(',');
        if (cents < 10) {
            sb.append('0');
        }
        sb.append(cents);
        sb.append(" TL");

        if (negative) {
            sb.insert(0, '-');
        }
        return sb.toString();
    }

    /** 1234567 becomes "1.234.567". */
    private static String groupThousands(long value) {
        String digits = Long.toString(value);
        StringBuilder sb = new StringBuilder();
        int untilDot = digits.length() % 3;
        if (untilDot == 0) {
            untilDot = 3;
        }
        for (int i = 0; i < digits.length(); i++) {
            if (untilDot == 0) {
                sb.append('.');
                untilDot = 3;
            }
            sb.append(digits.charAt(i));
            untilDot--;
        }
        return sb.toString();
    }
}

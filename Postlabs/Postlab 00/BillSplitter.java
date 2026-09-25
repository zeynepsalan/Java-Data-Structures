import java.util.ArrayList;

/**
 * CSE201 Post-Lab 0 - SplitPal.
 *
 * Read TODO.md before you start. Everything you write goes into this file.
 *
 * All amounts are whole kurus in long values: 12,34 TL is 1234. Do not use
 * double, float or BigDecimal anywhere in this file, and do not use
 * String.format or NumberFormat in formatTL.
 *
 * Only static methods, no package declaration, no printing, and no array that
 * is passed in may be modified.
 */
public class BillSplitter {
    public static void main(String[] args) {
        System.out.println(formatTL(1231231450));
        System.out.println(formatTL(-99999));
        System.out.println(formatTL(10));

        // long[] balances = { -300, 500, -200 };
        // long[][] m = settleUp(balances);
        // for (int i = 0; i < m.length; i++) {
        // for (int j = 0; j < m[i].length; j++) {
        // System.out.print(m[i][j] + " ");
        // }
        // System.out.println();
        // }
    }

    /**
     * Part 1. One share per person: the even share rounded down, with the
     * leftover kurus given one each to the people at the front of the array.
     * The shares must add up exactly to total.
     */
    public static long[] splitEvenly(long total, int people) {
        if (total < 0 || people <= 0) {
            throw new IllegalArgumentException();
        }
        long[] divideds = new long[people];
        long divided = total / people;
        long remain = total % people;
        for (int i = 0; i < people; i++) {
            divideds[i] = divided;
            if (remain > 0) {
                divideds[i] += 1;
                remain--;
            }
        }

        return divideds;
        // throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Part 2. Shares proportional to the weights, using the largest-reder
     * method: the leftover kurus go to the largest remainders, and equal
     * remainders are settled by the lower index.
     */
    public static long[] splitByWeights(long total, int[] weights) {
        if (weights == null || weights.length == 0 || total < 0) {
            throw new IllegalArgumentException();
        }
        boolean AllZero = true;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != 0)
                AllZero = false;
            if (weights[i] < 0) {
                throw new IllegalArgumentException();
            }
        }
        if (AllZero) {
            throw new IllegalArgumentException();
        }
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
        }
        long[] splitteds = new long[weights.length];
        for (int i = 0; i < weights.length; i++) {
            splitteds[i] = total * weights[i] / sum;
        }
        long remainder = total % sum;
        int[] remainders = new int[weights.length];
        for (int i = 0; i < weights.length; i++) {
            if ((remainder / sum * weights[i]) % 1 == 0) {
                remainders[i] = 0;
                continue;
            }
            remainders[i] = (int) ((remainder) / sum * weights[i] * 10.0);
        }

        int largestRemained = remainders[0];

        for (int i = 1; i < remainders.length; i++) {
            if (remainders[i] > largestRemained) {
                largestRemained = remainders[i];
            }
        }

        while (remainder > 0) {
            for (int i = 0; i < splitteds.length; i++) {
                if (remainders[i] == largestRemained) {
                    splitteds[i] += 1;
                    remainder--;
                }
                if (remainder == 0)
                    break;
            }
        }
        return splitteds;
    }

    /**
     * Part 3. subtotal x percent / 100, rounded to the nearest kurus, halves up.
     */
    public static long tipAmount(long subtotal, int percent) {
        if (subtotal < 0 || percent < 0) {
            throw new IllegalArgumentException();
        }
        return Math.round((subtotal * percent + 50) / 100);
    }

    /** Part 4. paid[i] - owed[i] for every person. */
    public static long[] balances(long[] paid, long[] owed) {
        if (paid == null || owed == null || owed.length != paid.length) {
            throw new IllegalArgumentException();
        }
        long[] balanceds = new long[paid.length];
        for (int i = 0; i < paid.length; i++) {
            balanceds[i] = paid[i] - owed[i];
        }
        return balanceds;
    }

    /**
     * Part 4. Transfers {from, to, amount} that bring every balance to zero,
     * built with the exact procedure in TODO.md. The argument is not modified.
     */
    public static long[][] settleUp(long[] balances) {
        if (balances == null) {
            throw new IllegalArgumentException();
        }
        int sum = 0;
        for (int i = 0; i < balances.length; i++) {
            sum += balances[i];
        }
        if (sum != 0)
            throw new IllegalArgumentException();

        long[] newBalances = balances.clone();
        ArrayList<Integer> froms = new ArrayList<>();
        ArrayList<Integer> tos = new ArrayList<>();
        ArrayList<Long> transfereds = new ArrayList<>();
        for (int i = 0; i < newBalances.length; i++) {
            boolean allZero = true;
            for (int k = 0; k < newBalances.length; k++) {
                // System.out.println("BALANCES ZORT" + newBalances[k]);
                if (newBalances[k] != 0) {
                    allZero = false;
                    break;
                }
            }
            // System.out.println("All Zero:" + allZero);
            if (allZero) {
                // System.out.println("BREAK DAYI BREAK;");
                break;
            }
            // System.out.println(i + ". i foru girişi");
            long transfered = 0;
            int fromIndex = -1;
            int toIndex = -1;
            for (int j = 0; j < newBalances.length; j++) {
                if (newBalances[j] == 0) {
                    continue;
                }
                // System.out.println(i + " " + j + ". j foru girişi");
                if (newBalances[j] < 0 && fromIndex == -1) {
                    // System.out.println("from ifi ");
                    fromIndex = j;
                }
                if (newBalances[j] > 0 && toIndex == -1) {
                    // System.out.println("to ifi ");
                    toIndex = j;
                }
                // System.out.println(i + " " + j + ". j foru çıkışı: fromIndex: " + fromIndex +
                // " toIndex: " + toIndex);
            }

            long from = newBalances[fromIndex];
            long to = newBalances[toIndex];
            if (-1 * from < to) {
                newBalances[fromIndex] = 0;
                newBalances[toIndex] = to + from;
                transfered = -1 * from;
                // // System.out.println(from);
            }
            if (to < -1 * from) {
                newBalances[toIndex] = 0;
                newBalances[fromIndex] = from + to;
                transfered = to;
                // // System.out.println(to);
            }
            if (to == -1 * from) {
                newBalances[toIndex] = 0;
                newBalances[fromIndex] = 0;
                transfered = to;
            }
            if (transfered != 0) {
                froms.add(fromIndex);
                tos.add(toIndex);
                transfereds.add(transfered);
            }
        }
        long[][] settledUp = new long[froms.size()][3];
        for (int i = 0; i < settledUp.length; i++) {
            settledUp[i][0] = froms.get(i);
            settledUp[i][1] = tos.get(i);
            settledUp[i][2] = transfereds.get(i);
        }
        return settledUp;
    }

    /** Part 5. Turkish format, for example 123450 becomes "1.234,50 TL". */
    // public static String formatTL(long kurus) {
    // long newKurus = kurus;
    // String tl = "";
    // if (kurus < 0) {
    // tl += "-";
    // }
    // int count = 1;
    // while (newKurus > 10) {
    // newKurus /= 10;
    // count++;
    // }
    // newKurus = kurus;
    // long[] basamak = new long[count];
    // for (int i = 0; i < count; i++) {
    // basamak[i] = newKurus % 10;
    // newKurus /= 10;
    // }
    // for (int i = count - 1; i >= 0; i--) {
    // if (i == 1) {
    // tl += ",";
    // }
    // tl += "" + basamak[i];
    // if (i % 3 == 2 && i != 2) {
    // tl += ".";
    // }
    // }
    // tl += " TL";
    // return tl;
    // }

    public static String formatTL(long kurus) {
        String str = Math.abs(kurus) + "";

        if (str.length() == 1) {
            str = "00" + str;
        }
        if (str.length() == 2) {
            str = "0" + str;
        }
        String res = "";
        int isThree = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            res = str.charAt(i) + res;
            if (i == str.length() - 2) {
                res = "," + res;
            }

            if (i < str.length() - 2 && i != 0) {
                isThree++;
            }
            if (isThree == 3) {
                res = "." + res;
                isThree = 0;
            }
        }

        if (kurus < 0) {
            res = "-" + res;
        }

        return res + " TL";
    }
}

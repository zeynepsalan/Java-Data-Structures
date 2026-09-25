import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;

/**
 * CSE201 Post-Lab 1 - ShopCart.
 *
 * Read TODO.md before you start. Everything you write goes into this file.
 * The declarations below are the ones TODO.md requires; fill in the bodies.
 *
 * Only Shop is public. Do not add a package declaration.
 * All money is whole kurus in long values: no double and no float anywhere.
 * Do not use instanceof, and do not compare category() values.
 */

/** Part 1. */
abstract class Product implements Comparable<Product> {

    private final String sku;
    private String name;
    private long price;
    private int rating;

    public Product(String sku, String name, long price, int rating) {
        if (sku == null || name == null || name.isBlank() || sku.isBlank() || price < 0 || rating > 5 || rating < 1) {
            throw new IllegalArgumentException();
        }

        this.sku = sku;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public int getRating() {
        return rating;
    }

    /** The cost of shipping one parcel containing this product. */
    public abstract long shippingCost();

    /** The product category. */
    public abstract String category();

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        try {
            return this.sku.equals(((Product) o).getSku());
        } catch (ClassCastException e) {
            return false;
        }
        // if (o instanceof Product) {
        // if (((Product) o).getSku().equals(this.sku)) {
        // return true;
        // }
        // }
        // return false;
    }

    @Override
    public int hashCode() {
        return this.sku.hashCode();
    }

    @Override
    public int compareTo(Product o) {
        if (o.getName().equalsIgnoreCase(this.name))
            return this.sku.compareTo(o.getSku());
        return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
    }

    @Override
    public String toString() {
        return "[" + this.sku + "] " + this.name;
    }
}

/** Part 2. */
class PhysicalProduct extends Product {

    private final int weightGrams;

    public PhysicalProduct(String sku, String name, long price, int rating, int weightGrams) {
        super(sku, name, price, rating);
        if (weightGrams <= 0) {
            throw new IllegalArgumentException();
        }
        this.weightGrams = weightGrams;
    }

    @Override
    public long shippingCost() {
        return (long) (1500 + 500 * Math.ceil(weightGrams / 1000.0));
    }

    @Override
    public String category() {
        return "Physical";
    }
}

/** Part 2. */
class DigitalProduct extends Product {

    private final int sizeMB;

    public DigitalProduct(String sku, String name, long price, int rating, int sizeMB) {
        super(sku, name, price, rating);
        if (sizeMB <= 0) {
            throw new IllegalArgumentException();
        }
        this.sizeMB = sizeMB;
    }

    @Override
    public long shippingCost() {
        return 0;
    }

    @Override
    public String category() {
        return "Digital";
    }
}

/** Part 3. */
interface Discount {

    String code();

    long amountOff(long subtotal, int totalQuantity);
}

/** Part 3. */
class PercentOff implements Discount {

    private final String code;
    private final int percent;
    private final long maxOff;

    public PercentOff(String code, int percent, long maxOff) {
        if (percent < 1 || percent > 100 || maxOff < 0 || code == null || code.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.code = code;
        this.percent = percent;
        this.maxOff = maxOff;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public long amountOff(long subtotal, int totalQuantity) {
        return (long) Math.min(Math.floor(subtotal * percent / 100), maxOff);
    }
}

/** Part 3. */
class FixedOff implements Discount {

    private final String code;
    private final long amount;
    private final long minSubtotal;

    public FixedOff(String code, long amount, long minSubtotal) {
        if (amount < 0 || minSubtotal < 0 || code == null || code.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.amount = amount;
        this.minSubtotal = minSubtotal;
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public long amountOff(long subtotal, int totalQuantity) {
        if (subtotal >= minSubtotal) {
            return Math.min(amount, subtotal);
        }
        return 0;
    }
}

class CartItem<T extends Product> {
    public T product;
    public long quantity;

    public CartItem(T product, long quantity) {
        if (product == null || quantity <= 0 || quantity > 99) {
            throw new IllegalArgumentException();
        }
        this.product = product;
        this.quantity = quantity;
    }
}

/**
 * Part 4. A cart of lines. Each line holds one product and a quantity, and the
 * lines stay in the order in which each product was first added.
 *
 * Choose your own storage for the lines; java.util collections are allowed.
 */
class Cart<T extends Product> {
    private Queue<CartItem<T>> Products = new LinkedList<>();

    public void add(T product, int quantity) {
        for (CartItem<T> p : Products) {
            if (p.product.equals(product)) {
                if (p.quantity + quantity > 99) {
                    throw new IllegalArgumentException();
                }
                p.quantity += quantity;
                return;
            }
        }
        CartItem<T> newItem = new CartItem<>(product, quantity);
        Products.offer(newItem);
    }

    public boolean setQuantity(String sku, int quantity) {
        if (quantity < 0 || quantity > 99)
            throw new IllegalArgumentException();
        for (CartItem<T> cartItem : Products) {
            if (cartItem.product.getSku().equals(sku)) {
                cartItem.quantity = quantity;
                if (quantity == 0) {
                    remove(sku);
                }
                return true;
            }
        }
        return false;
    }

    public boolean remove(String sku) {
        Queue<CartItem<T>> temp = new LinkedList<>();
        boolean isRemoved = false;
        while (Products.peek() != null) {
            if (Products.peek().product.getSku().equals(sku)) {
                Products.poll();
                isRemoved = true;
                continue;
            }
            temp.offer(Products.poll());
        }
        Products = temp;
        return isRemoved;
    }

    public int lineCount() {
        return Products.size();
    }

    public int totalQuantity() {
        int totalQuantity = 0;
        for (CartItem<T> cartItem : Products) {
            totalQuantity += cartItem.quantity;
        }
        return totalQuantity;
    }

    public List<T> products() {
        List<T> productsList = new ArrayList<>();

        Queue<CartItem<T>> temp = new LinkedList<>();
        while (Products.peek() != null) {
            productsList.add(Products.peek().product);
            temp.offer(Products.poll());
        }
        Products = temp;
        return productsList;
    }

    public long subtotal() {
        int subtotal = 0;
        for (CartItem<T> cartItem : Products) {
            subtotal += cartItem.product.getPrice() * cartItem.quantity;
        }
        return subtotal;
    }

    public long shipping() {
        if (subtotal() >= 200000) {
            return 0;
        }
        List<T> productList = products();
        long largest = 0;
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).shippingCost() > largest) {
                largest = productList.get(i).shippingCost();
            }
        }
        return largest;
    }

    public long total(Discount d) {
        long discount = 0;
        if (d != null) {
            discount = d.amountOff(subtotal(), totalQuantity());
        }
        return subtotal() - discount + shipping();
    }
}

/** Part 5. */
public class Shop {

    public static Comparator<Product> byPriceAscending() {
        return (p1, p2) -> {
            int comp = Long.compare(p1.getPrice(), p2.getPrice());
            if (comp == 0) {
                return p1.compareTo(p2);
            }
            return comp;
        };
    }

    public static Comparator<Product> byRatingDescending() {
        return (p1, p2) -> {
            int comp = -1 * Integer.compare(p1.getRating(), p2.getRating());
            if (comp == 0) {
                return p1.compareTo(p2);
            }
            return comp;
        };
    }

    public static <T extends Product> List<T> sorted(List<T> items, Comparator<? super T> order) {
        List<T> newItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            T t = items.get(i);
            newItems.add(t);
        }
        newItems.sort(order);
        return newItems;
    }

    public static Discount bestDiscount(Cart<?> cart, List<Discount> coupons) {
        if (coupons == null || coupons.size() == 0) {
            return null;
        }
        Discount largest = coupons.get(0);
        for (int i = 0; i < coupons.size(); i++) {
            Discount discount = coupons.get(i);
            long current_discount = discount.amountOff(cart.subtotal(), cart.totalQuantity());
            if (largest.amountOff(cart.subtotal(), cart.totalQuantity()) < current_discount) {
                largest = discount;
            }
        }
        if (largest.amountOff(cart.subtotal(), cart.totalQuantity()) <= 0) {
            return null;
        }
        return largest;
    }
}

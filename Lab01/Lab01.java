package Lab01;

import java.util.*;

public class Lab01 {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // 1. Ödeme Yöntemleri ve Müşterilerin Oluşturulması
        PaymentMethod cc = new CreditCardPayment("1234-5678-9012-3456", "Ahmet Yilmaz");
        PaymentMethod pp = new PayPalPayment("ahmet@example.com");

        Customer cust1 = new RegularCustomer("CUST101", "Ahmet Yilmaz", cc);
        Customer cust2 = new PremiumCustomer("CUST102", "Ayse Demir", pp);

        bank.addCustomer(cust1);
        bank.addCustomer(cust2);

        // 2. Hesapların Oluşturulması ve Bankaya Kaydedilmesi
        Account checking = new CheckingAccount("ACC1001", 500.0, 200.0); // 200 TL overdraft
        Account saving = new SavingsAccount("ACC2001", 1000.0, 0.05); // %5 faiz

        bank.openAccountForCustomer("CUST101", checking);
        bank.openAccountForCustomer("CUST102", saving);

        // 3. Hesap İşlemleri ve Overdraft Testi
        System.out.println("=== HESAP ISLEMLERI VE OVERDRAFT TESTI ===");
        checking.deposit(300.0);
        checking.withdraw(600.0); // Bakiye 800 -> 200 kaldı
        checking.withdraw(300.0); // Overdraft kullanıldı: Bakiye -100 (Limit: 200)
        checking.withdraw(500.0); // Limit aşıldı (Reddedilmeli)

        saving.deposit(500.0);
        ((SavingsAccount) saving).applyInterest(); // Faiz işletildi

        // 4. TreeSet ile Sıralı İşlem Geçmişinin Yazdırılması
        System.out.println("\n=== TRANSACTION HISTORY (TreeSet Sirali) ===");
        checking.printTransactionHistory();

        // 5. Strategy Pattern Testi (Runtime'da Ödeme Yöntemi Değiştirme)
        System.out.println("\n=== STRATEGY PATTERN & PAYMENT TESTI ===");
        System.out.print("Regular Customer (CC): ");
        cust1.makePayment(100.0); // 100 + 2.5 fee

        System.out.println("Ödeme yöntemi PayPal olarak değiştiriliyor...");
        cust1.setPaymentMethod(pp); // Runtime Strategy değişimi
        System.out.print("Regular Customer (PayPal): ");
        cust1.makePayment(100.0);

        // 6. Banka HashMap O(1) Arama Testi
        System.out.println("\n=== BANK HASHMAP LOOKUP TESTI ===");
        Account foundAcc = bank.findAccount("ACC1001");
        if (foundAcc != null) {
            System.out.println("Hesap bulundu. Guncel Bakiye: " + foundAcc.getBalance());
        }
    }
}

interface PaymentMethod {
    void pay(double amount);
}

class CreditCardPayment implements PaymentMethod {
    private String cardNumber;
    private String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    @Override
    public void pay(double amount) {
        String maskedCard = maskCardNumber(this.cardNumber);
        System.out.println("Your payment with amount: " + amount +
                ", card number: " + maskedCard +
                " is successful.");
    }

    private String maskCardNumber(String card) {
        if (card == null || card.length() < 4) {
            return "****";
        }
        // "1234-5678-9012-3456" -> "****-****-****-3456"
        String lastFour = card.substring(card.length() - 4);
        return "****-****-****-" + lastFour;
    }

    public void verifySecurePayment() {
        System.out.println("Payment verified.");
    }

    public void confirmTransaction() {
        System.out.println("Transaction confirmed.");
    }
}

class PayPalPayment implements PaymentMethod {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println(email + " Your payment is succesfull! ");
    }

    public void connectServer() {
        System.out.println("Connecting to server...");
    }

    public void retreiveInfo() {
        System.out.println("User info retrieved.");
    }
}

class Transaction implements Comparable<Transaction> {
    private Date transactionDate;
    private String transactionType;
    private double amount;
    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITHDRAWAL = "WITHDRAWAL";

    public Transaction(String transactionType, double amount) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = new Date();
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setTransactionDate(Date d) {
        this.transactionDate = d;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(Transaction t) {
        if (this.transactionDate.compareTo(t.transactionDate) == 0) {
            return Double.compare(this.amount, t.amount);
        }
        return this.transactionDate.compareTo(t.transactionDate);
    }

    @Override
    public String toString() {
        return transactionType + " - Amount: " + amount + " - Date: " + transactionDate;
    }

}

abstract class Account {
    private String accountNumber;
    private double balance;
    private Set<Transaction> transactionHistory = new TreeSet<>();

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean addTransaction(Transaction t) {
        return transactionHistory.add(t);
    }

    public void deposit(double amount) {
        balance += amount;
        this.addTransaction(new Transaction(Transaction.DEPOSIT, amount));
    }

    public abstract void withdraw(double amount);

    public void printTransactionHistory() {
        String s = "";
        for (Transaction t : transactionHistory) {
            s += "\n" + t.getTransactionType() + " with amount: " + t.getAmount() +
                    ". Current balance: " + balance +
                    " date: " + t.getTransactionDate();
        }
        System.out.println(s);
    }
}

class CheckingAccount extends Account {
    private double overDraftLimit;

    public CheckingAccount(String accountNumber, double balance, double overDraftLimit) {
        super(accountNumber, balance);
        this.overDraftLimit = overDraftLimit;
    }

    public void withdraw(double amount) {
        if (getBalance() + overDraftLimit - amount < 0)
            return;
        setBalance(getBalance() - amount);
        super.addTransaction(new Transaction(Transaction.WITHDRAWAL, amount));
    }
}

class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, double balance, double interestRate) {
        super(accountNumber, balance);
        this.interestRate = interestRate;
    }

    public void withdraw(double amount) {
        if (getBalance() - amount < 0)
            return;
        setBalance(getBalance() - amount);
        super.addTransaction(new Transaction(Transaction.WITHDRAWAL, amount));

    }

    public void applyInterest() {
        double interestAmount = getBalance() * interestRate;
        deposit(interestAmount);
    }

}

abstract class Customer {
    private String customerId;
    private String name;
    private List<Account> accounts = new ArrayList<>();
    private PaymentMethod paymentMethod;

    public Customer(String customerId, String name, PaymentMethod paymentMethod) {
        this.customerId = customerId;
        this.name = name;
        this.paymentMethod = paymentMethod;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void setPaymentMethod(PaymentMethod method) {
        this.paymentMethod = method;
    }

    public void makePayment(double amount) {
        amount += getTransactionFee();
        paymentMethod.pay(amount);
    }

    public abstract double getTransactionFee();
}

class RegularCustomer extends Customer {
    public RegularCustomer(String customerId, String name, PaymentMethod paymentMethod) {
        super(customerId, name, paymentMethod);
    }

    @Override
    public double getTransactionFee() {
        return 2.5;
    }
}

class PremiumCustomer extends Customer {
    public PremiumCustomer(String customerId, String name, PaymentMethod paymentMethod) {
        super(customerId, name, paymentMethod);
    }

    @Override
    public double getTransactionFee() {
        return 1.0;
    }
}

class Bank {
    private ArrayList<Customer> customers = new ArrayList<>();
    private HashMap<String, Account> accountsMap = new HashMap<>();

    public Bank() {

    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void openAccountForCustomer(String customerId, Account account) {
        Customer customer = findCustomer(customerId);
        if (customer == null)
            return;
        customer.addAccount(account);
        accountsMap.put(account.getAccountNumber(), account);
    }

    public Account findAccount(String accountNumber) {
        return accountsMap.get(accountNumber);
    }

    public Customer findCustomer(String customerId) {
        Customer customer = null;
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                customer = c;
                break;
            }
        }
        return customer;
    }
}

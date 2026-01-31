import java.math.BigDecimal;
public class Account {

    private String accountID;
    private BigDecimal balance;

    Account(String ID, BigDecimal b){
        this.accountID = ID;
        this.balance = b;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountID() {
        return accountID;
    }

    public void deposit(BigDecimal amount){
        balance = balance.add(amount);
    }

    public void withraw(BigDecimal amount){
        balance = balance.subtract(amount);
    }
}

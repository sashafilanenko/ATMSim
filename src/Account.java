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
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount){
        balance = balance.subtract(amount);
    }
}

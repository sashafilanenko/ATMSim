import exceptions.InsufficientFundsException;

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
        if(amount == null){
            throw new IllegalArgumentException("сумма не может быть равна нулю");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) throws InsufficientFundsException{
        if(amount == null){
            throw new InsufficientFundsException("сумма не может быть равна нулю");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("сумма должна быть положительной");
        }
        if(balance.compareTo(amount) < 0){
            throw new InsufficientFundsException("недостаточно среств на счете " + accountID);
        }
        balance = balance.subtract(amount);
    }
}

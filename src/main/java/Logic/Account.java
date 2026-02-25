package main.java.Logic;

import main.java.exceptions.InsufficientFundsException;


import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final String accountID;
    private BigDecimal balance;
    private final ReentrantLock lock = new ReentrantLock();

    Account(String ID, BigDecimal b){
        this.accountID = ID;
        this.balance = b;
    }
    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public String getAccountID() {
        return accountID;
    }

    void lock() { lock.lock(); }
    void unlock() { lock.unlock(); }

    void depositUnsafe(BigDecimal amount) {
        balance = balance.add(amount);
    }

    void withdrawUnsafe(BigDecimal amount) throws InsufficientFundsException {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("???????????? ??????? ?? ????? " + accountID);
        }
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount){
        if(amount == null){
            throw new IllegalArgumentException("????? ?? ????? ???? null");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("????? ?????? ???? ?????????????");
        }
        lock.lock();
        try {
            depositUnsafe(amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) throws InsufficientFundsException{
        if(amount == null){
            throw new IllegalArgumentException("????? ?? ????? ???? null");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("????? ?????? ???? ?????????????");
        }
        lock.lock();
        try {
            withdrawUnsafe(amount);
        } finally {
            lock.unlock();
        }
    }

}

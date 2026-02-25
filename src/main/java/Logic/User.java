package main.java.Logic;

import main.java.exceptions.AccountNotFoundException;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final String login;
    private final String fullName;
    private String passwordHash;
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public User(String login, String fullName, String passwordHash) {
        this.login = login;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
    }

    public void addAccount(Account account){
        accounts.put(account.getAccountID(), account);
    }

    public String getLogin() { return login; }
    public String getFullName() { return fullName; }

    public boolean verifyPassword(String hashToVerify) {
        return this.passwordHash.equals(hashToVerify);
    }

    public Collection<Account> getAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    public int getNumOfAccounts(){
        return accounts.size();
    }

    public BigDecimal getBalanceByID(String accountID) throws AccountNotFoundException{
        Account account = getAccountById(accountID);
        return account.getBalance();
    }


    public Account getAccountById(String accountId) throws AccountNotFoundException {
        if (!accounts.containsKey(accountId)) {
            throw new AccountNotFoundException("—чет с ID " + accountId + " не найден.");
        }
        return accounts.get(accountId);
    }

}

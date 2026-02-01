import exceptions.AccountNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public class User {
    private String name;
    private String password;
    private List<Account> accounts;

    User(String n, String p, List<Account> ac){
        this.name = n;
        this.password = p;
        this.accounts = ac;
    }


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getNumOfAccounts(){
        return accounts.size();
    }

    public BigDecimal getBalanceByID(String accountID) throws AccountNotFoundException{
        Account account = getAccountById(accountID);
        return account.getBalance();
    }

    public Account getAccountById(String accountId) throws AccountNotFoundException{
        for (Account account : accounts) {
            if (account.getAccountID().equals(accountId)) {
                return account;
            }
        }
        throw new AccountNotFoundException("счет не найден " + accountId + "у пользователя " + name);
    }


}

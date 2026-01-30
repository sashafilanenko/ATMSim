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

    public Double getBalanceByID(String accountID){
        for(Account account : accounts){
            if(account.getAccountID().equals(accountID)){
                return  account.getBalance();
            }
        }
        return null;
    }

    public Account getAccountById(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountID().equals(accountId)) {
                return account;
            }
        }
        return null;
    }


}

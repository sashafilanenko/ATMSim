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

    public Double getBalanceByID(String accountID){
        for(Account account : accounts){
            if(account.getAccountID().equals(accountID)){
                return  account.getBalance();
            }
        }
        return null;
    }


}

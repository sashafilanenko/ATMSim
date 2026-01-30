public class Account {

    private String accountID;
    private Double balance;

    Account(String ID, double b){
        this.accountID = ID;
        this.balance = b;
    }

    public Double getBalance() {
        return balance;
    }

    public String getAccountID() {
        return accountID;
    }

    public void deposit(double amount){
        balance +=amount;
    }

    public void withraw(double amount){
        balance -= amount;
    }
}

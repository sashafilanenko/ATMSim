public class Account {

    private String accountID;
    private Double balance;

    Account(String ID, double b){
        this.accountID = ID;
        this.balance = b;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public String getAccountID() {
        return accountID;
    }
}

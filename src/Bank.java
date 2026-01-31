import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<User> users = new ArrayList<>();

    public void addUser(User user){
        if(user != null){
            users.add(user);
        }
    }

    public List<User> getUsers(){
        return List.copyOf(users);
    }

    public User authenticate(String login, String password){
        for(User user:users){
            if(login.equals(user.getName()) && password.equals(user.getPassword())){
                return user;
            }
        }
        return null;
    }

    public User getTargetUser(String login){
        for(User user:users){
            if(login.equals(user.getName()) && user.getNumOfAccounts() != 0){
                return user;
            }
        }
        return null;
    }

    public void transaction(User user, String id){
        Account account = user.getAccountById(id);
        account.withraw(BigDecimal.valueOf(20));
    }
    public void deposit(User user, String id, BigDecimal amount){
        Account account = user.getAccountById(id);
        account.deposit(amount);
    }

    public void withraw(User user, String id, BigDecimal amount){
        Account account = user.getAccountById(id);
        account.withraw(amount);
    }
    public List<BigDecimal> checkBalance(User user){
        List<BigDecimal> balances = new ArrayList<>();
        for(int i = 1; i <= user.getNumOfAccounts(); i++){
            balances.add(user.getBalanceByID(String.valueOf(i)));
        }
        return balances;
    }

    public void transfer(User currentUser, String currentID, User targetUser, String targetID, BigDecimal cost){
        Account account1 = currentUser.getAccountById(currentID);
        Account account2 = targetUser.getAccountById(targetID);

        account1.withraw(cost);
        account2.deposit(cost);
    }

}

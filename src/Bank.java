import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private Map<String, User> users = new HashMap<>();

    public void addUser(User user){
        if(user != null){
            users.put(user.getName(), user);
        }
    }

    public List<User> getUsers(){
        return List.copyOf(users.values());
    }

    public User authenticate(String login, String password){

        User user = users.get(login);

        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    public User getTargetUser(String login){

        User user = users.get(login);
        if(user != null && user.getNumOfAccounts() !=0){
            return user;
        }
        return null;
    }

    public void transaction(User user, String id){
        Account account = user.getAccountById(id);
        account.withdraw(BigDecimal.valueOf(20));
    }
    public void deposit(User user, String id, BigDecimal amount){
        Account account = user.getAccountById(id);
        account.deposit(amount);
    }

    public void withdraw(User user, String id, BigDecimal amount){
        Account account = user.getAccountById(id);
        account.withdraw(amount);
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

        account1.withdraw(cost);
        account2.deposit(cost);
    }

}

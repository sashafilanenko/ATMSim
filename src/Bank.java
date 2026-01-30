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

}

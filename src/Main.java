import java.util.List;

public class Main {
    public static void main(String[] args) {


        Account acc1 = new Account("1", 0);
        Account acc2 = new Account("2", 100);

        Account acc3 = new Account("3", 10000);

        User user = new User("Alex", "dddfff", List.of(acc1, acc2));
        User user2 = new User("Sam", "12345", List.of(acc3));

        System.out.println("пользователи: \n" + user.getName() + ", аккаунт: " + acc1.getAccountID() + ", баланс: " + user.getBalanceByID("1") + "\n");
        System.out.println("пользователи: \n" + user.getName() + ", аккаунт: " + acc2.getAccountID() + ", баланс: " + user.getBalanceByID("2") + "\n");

        System.out.println("пользователи: \n" + user2.getName() + ", аккаунт: " + acc3.getAccountID() + ", баланс: " + user2.getBalanceByID("2") + "\n");



    }
}
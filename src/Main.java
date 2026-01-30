import java.util.List;

public class Main {
    public static void main(String[] args) {

        Bank bank = new Bank();

        Account acc1 = new Account("1", 0);
        Account acc2 = new Account("2", 100);

        Account acc3 = new Account("3", 10000);

        User user = new User("Alex", "dddfff", List.of(acc1, acc2));
        User user2 = new User("Sam", "12345", List.of(acc3));

        bank.addUser(user);
        bank.addUser(user2);

        System.out.println("Пользователь: \n" + user.getName() + ", аккаунт: " + acc1.getAccountID() + ", баланс: " + user.getBalanceByID("1") + "\n");
        System.out.println("Пользователь: \n" + user.getName() + ", аккаунт: " + acc2.getAccountID() + ", баланс: " + user.getBalanceByID("2") + "\n");

        System.out.println("Пользователь: \n" + user2.getName() + ", аккаунт: " + acc3.getAccountID() + ", баланс: " + user2.getBalanceByID("3") + "\n");

        ATM atm = new ATM(bank);
        atm.start();


    }
}
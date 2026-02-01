import exceptions.AccountNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Bank bank = new Bank();

        //TODO: вносить ID в список и при создании нового аккаунта присваивать уникальный
        //TODO: поправить счетчик, проходить по листу, а не считать циклом (долго)

        Account acc1 = new Account("1", BigDecimal.valueOf(0));
        Account acc2 = new Account("2", BigDecimal.valueOf(100));

        Account acc3 = new Account("3", BigDecimal.valueOf(1000));

        User user = new User("Alex", "dddfff", List.of(acc1, acc2));
        User user2 = new User("Sam", "12345", List.of(acc3));

        try {
            bank.addUser(user);
            bank.addUser(user2);
        } catch (AccountNotFoundException e) {
            System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
            return;
        }

        try {
            System.out.println("Пользователь: " + user.getName() + ", аккаунт: " + acc1.getAccountID() + ", баланс: " + user.getBalanceByID("1"));
            System.out.println("Пользователь: " + user.getName() + ", аккаунт: " + acc2.getAccountID() + ", баланс: " + user.getBalanceByID("2"));
            System.out.println("Пользователь: " + user2.getName() + ", аккаунт: " + acc3.getAccountID() + ", баланс: " + user2.getBalanceByID("3"));
        } catch (AccountNotFoundException e) {
            System.out.println("Ошибка при чтении баланса: " + e.getMessage());
        }

        ATM atm = new ATM(bank);
        atm.start();


    }
}
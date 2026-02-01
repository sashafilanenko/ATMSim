import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.AuthException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ATM {

    private final Bank bank;
    private final Scanner scan = new Scanner(System.in);;

    private User currentUser;

    ATM(Bank bank){
        this.bank = bank;
    }

    public void setCurrentUser(User current){
        this.currentUser = current;
    }

    public void start() {
        System.out.println("Добро пожаловать, войдите в аккаунт");
        while (currentUser == null) {
            try {
                System.out.print("Введите логин: ");
                String login = scan.nextLine();

                System.out.println("Введите пароль:");
                String password = scan.nextLine();
                setCurrentUser(bank.authenticate(login, password));
                System.out.println("Успешный вход, привет " + currentUser.getName());

            } catch (AuthException ae) {
                System.out.println("Авторизация не удалась: " + ae.getMessage());
            }

        }
        menu();
    }

    public void menu() {
        boolean running = true;

        while (running) {
            System.out.println("Выберите действие: \n" +
                    "1) оплатить обслуживание карты\n" +
                    "2) пополнить карту\n" +
                    "3) снять деньги со счета\n" +
                    "4) проверить баланс\n" +
                    "5) перевести деньги другому пользователю\n" +
                    "6) выйти");

            int action;
            try {
                action = Integer.parseInt(scan.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Завершение сеанса.");
                return;
            }

            switch (action) {
                case 1 -> {
                    try {
                        System.out.println("Введите ID счёта для оплаты обслуживания:");
                        String id = scan.nextLine();
                        bank.transaction(currentUser, id);
                        System.out.println("Оплата выполнена.");
                    } catch (AccountNotFoundException | InsufficientFundsException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        System.out.println("Введите ID счёта для пополнения:");
                        String id = scan.nextLine();
                        System.out.println("Введите сумму (например 100.50):");
                        BigDecimal amount = new BigDecimal(scan.nextLine().trim());
                        bank.deposit(currentUser, id, amount);
                        System.out.println("Пополнение выполнено.");
                    } catch (AccountNotFoundException | IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        System.out.println("Введите ID счёта для снятия:");
                        String id = scan.nextLine();
                        System.out.println("Введите сумму:");
                        BigDecimal amount = new BigDecimal(scan.nextLine().trim());
                        bank.withdraw(currentUser, id, amount);
                        System.out.println("Снятие выполнено.");
                    } catch (AccountNotFoundException | InsufficientFundsException | IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }
                case 4 -> {
                    try {
                        List<BigDecimal> balances = bank.checkBalance(currentUser);
                        System.out.println("Балансы: " + balances);
                    } catch (AccountNotFoundException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }
                case 5 -> {
                    try {
                        System.out.println("Введите ID вашего счёта для перевода:");
                        String fromId = scan.nextLine();
                        System.out.println("Введите имя получателя:");
                        String targetName = scan.nextLine();
                        System.out.println("Введите ID счёта получателя:");
                        String toId = scan.nextLine();
                        System.out.println("Введите сумму перевода:");
                        BigDecimal amount = new BigDecimal(scan.nextLine().trim());

                        User targetUser = bank.getTargetUser(targetName);
                        bank.transfer(currentUser, fromId, targetUser, toId, amount);
                        System.out.println("Перевод выполнен.");
                    } catch (AccountNotFoundException | InsufficientFundsException | IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }
                case 6 -> running = false;
                default -> System.out.println("Неверный выбор");
            }
        }
    }
}

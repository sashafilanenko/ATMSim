import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.AuthException;
import exceptions.TransactionException;

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

    //функциональный интерфейс, просто абстракция
    @FunctionalInterface
    interface UserAction{
        void execute() throws Exception;
    }

    //обработчик ошибок
    private void performAction(UserAction action){
        try {
            action.execute();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода числа. Попробуйте снова.");
        } catch (AccountNotFoundException | InsufficientFundsException | AuthException | IllegalArgumentException e) {
            System.out.println("Ошибка операции: " + e.getMessage());
        } catch (TransactionException e) {
            System.out.println("Транзакция отменена: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла системная ошибка: " + e.getMessage());
        }
    }

    //процессор ввода/вывода дабы избежать копипаст
    private String prompt(String message){
        System.out.println(message);
        return scan.nextLine().trim();
    }

    //процессор ввода/вывода переменных, заведомо типа BigDecimal
    private BigDecimal promptBigDecimal(String message){
        System.out.println(message);
        String input = scan.nextLine().trim().replace(",", ".");
        return new BigDecimal(input);
    }

    private void printBalances(User currentUser) {
        var accounts = currentUser.getAccounts();

        System.out.println("\n--- Ваши счета ---");
        if (accounts.isEmpty()) {
            System.out.println("У вас пока нет открытых счетов.");
        } else {
            for (Account acc : accounts) {
                System.out.printf("Счет #%-8s | Баланс: %,.2f\n",
                        acc.getAccountID(),
                        acc.getBalance());
            }
        }
        System.out.println("------------------\n");
    }

    //TODO: пора бы и махнуть на Swing
    //TODO: хешировать пароли, шифровать + соль, возможно мигрировать на проект с maven

    // приветственное меню
    public void start() {
        System.out.println("Добро пожаловать, войдите в аккаунт");
        while (currentUser == null) {
            try {
                System.out.print("Введите логин: ");
                String login = scan.nextLine();

                System.out.println("Введите пароль:");
                String password = scan.nextLine();
                setCurrentUser(bank.authenticate(login, password));
                System.out.println("Успешный вход, привет " + currentUser.getLogin());

            } catch (AuthException ae) {
                System.out.println("Авторизация не удалась: " + ae.getMessage());
            }

        }
        menu();
    }

    // меню основное (выбор действий)
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

            String input = prompt("Выберите действие:");
            int action;

            try {
                action = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите число. ");
                continue;
            }

            //TODO: добавить маппинг в энам

            Operations op = null;
            if (action == 1) op = Operations.TRANSACTION;
            else if (action == 2) op = Operations.DEPOSIT;
            else if (action == 3) op = Operations.WITHDRAW;
            else if (action == 4) op = Operations.CHECK_BALANCE;
            else if (action == 5) op = Operations.TRANSFER;
            else if (action == 6) { running = false; continue; }

            if (op == null) {
                System.out.println("Неверная команда");
                continue;
            }



            switch (op) {
                case TRANSACTION -> performAction(() -> {
                    String id = prompt("Введите ID счёта для оплаты обслуживания: ");
                    bank.transaction(currentUser, id);
                    System.out.println("Оплата выполнена.");
                });
                case DEPOSIT -> performAction(() -> {
                    String id = prompt("Введите ID счёта для пополнения: ");
                    BigDecimal amount = promptBigDecimal("Введите сумму (например 100.50): ");
                    bank.deposit(currentUser, id, amount);
                    System.out.println("Пополнение выполнено.");
                });
                case WITHDRAW -> performAction(() -> {
                    String id = prompt("Введите ID счёта для снятия:");
                    printBalances(currentUser);
                    BigDecimal amount = promptBigDecimal("Введите сумму:");
                    bank.withdraw(currentUser, id, amount);
                    System.out.println("Снятие выполнено.");
                });
                case CHECK_BALANCE -> performAction(() -> {
                    printBalances(currentUser);
                });
                case TRANSFER -> performAction(() -> {
                    String fromId = prompt("Введите ID вашего счёта для перевода:" );
                    String targetLogin = prompt("Введите Логин получателя:");
                    String toId = prompt("Введите ID счёта получателя:" );
                    BigDecimal amount = promptBigDecimal("Введите сумму перевода: ");

                    bank.transfer(currentUser, fromId, targetLogin, toId, amount);
                    System.out.println("Перевод выполнен.");
                });
                default -> System.out.println("Неверный выбор");
            }
        }
    }
}

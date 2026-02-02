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

    private List<BigDecimal> promptBalanceList(User currentUser){
        List<BigDecimal> balances = bank.checkBalance(currentUser);
        System.out.println("Балансы: " + balances);
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
                System.out.println("Успешный вход, привет " + currentUser.getName());

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

            int action;

            try {
                action = Integer.parseInt(scan.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Завершение сеанса.");
                return;
            }

            if (action == 6) { running = false; continue;}

            switch (action) {
                case 1 -> performAction(() -> {
                    String id = prompt("Введите ID счёта для оплаты обслуживания: ");
                    bank.transaction(currentUser, id);
                    System.out.println("Оплата выполнена.");
                });
                case 2 -> performAction(() -> {
                    String id = prompt("Введите ID счёта для пополнения: ");
                    BigDecimal amount = promptBigDecimal("Введите сумму (например 100.50): ");
                    bank.deposit(currentUser, id, amount);
                    System.out.println("Пополнение выполнено.");
                });
                case 3 -> performAction(() -> {
                    String id = prompt("Введите ID счёта для снятия:");
                    List<BigDecimal> balances = bank.checkBalance(currentUser);
                    System.out.println("Балансы: " + balances);
                    BigDecimal amount = promptBigDecimal("Введите сумму:");
                    bank.withdraw(currentUser, id, amount);
                    System.out.println("Снятие выполнено.");
                });
                case 4 -> performAction(() -> {
                    List<BigDecimal> balances = bank.checkBalance(currentUser);
                    System.out.println("Балансы: " + balances);
                });
                case 5 -> performAction(() -> {
                    String fromId = prompt("Введите ID вашего счёта для перевода:" );
                    String targetName = prompt("Введите имя получателя:" );
                    String toId = prompt("Введите ID счёта получателя:" );
                    BigDecimal amount = promptBigDecimal("Введите сумму перевода: ");

                    User targetUser = bank.getTargetUser(targetName);
                    bank.transfer(currentUser, fromId, targetUser, toId, amount);
                    System.out.println("Перевод выполнен.");
                });
                default -> System.out.println("Неверный выбор");
            }
        }
    }
}

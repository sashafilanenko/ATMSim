import exceptions.*;
import java.math.BigDecimal;

public class ATM {
    private final Bank bank;
    private final ConsoleUIService ui;
    private User currentUser;

    public ATM(Bank bank, ConsoleUIService ui) {
        this.bank = bank;
        this.ui = ui;
    }

    public void start() {
        ui.print("Добро пожаловать в систему");
        authenticateUser();
        runMenuLoop();
    }

    private void authenticateUser() {
        while (currentUser == null) {
            try {
                String login = ui.prompt("Введите логин: ");
                String password = ui.prompt("Введите пароль: ");
                this.currentUser = bank.authenticate(login, password);
                ui.print("Успешный вход, привет " + currentUser.getLogin());
            } catch (AuthException ae) {
                ui.print("Ошибка: " + ae.getMessage());
            }
        }
    }

    private void runMenuLoop() {
        boolean running = true;
        while (running) {
            ui.showMenu();

            int actionId = ui.promptInt("Ваш выбор: ");

            Operations op = Operations.getByID(actionId).orElse(null);

            if (op == null) {
                ui.print("Неверная команда");
                continue;
            }

            try {
                if (op == Operations.EXIT) {
                    running = false;
                    ui.print("До свидания!");
                } else {
                    processOperation(op);
                }
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void processOperation(Operations op) throws Exception {
        switch (op) {
            case TRANSACTION -> {
                String id = ui.prompt("ID счета для оплаты обслуживания: ");
                bank.transaction(currentUser, id);
                ui.print("Оплата выполнена.");
            }
            case DEPOSIT -> {
                String id = ui.prompt("ID счета для пополнения: ");
                BigDecimal amount = ui.promptBigDecimal("Сумма: ");
                bank.deposit(currentUser, id, amount);
                ui.print("Пополнение выполнено.");
            }
            case WITHDRAW -> {
                String id = ui.prompt("ID счета для снятия: ");
                printBalances(); // Переиспользовал метод
                BigDecimal amount = ui.promptBigDecimal("Сумма: ");
                bank.withdraw(currentUser, id, amount);
                ui.print("Снятие выполнено.");
            }
            case CHECK_BALANCE -> printBalances();
            case TRANSFER -> {
                String fromId = ui.prompt("ID вашего счета: ");
                String targetLogin = ui.prompt("Логин получателя: ");
                String toId = ui.prompt("ID счета получателя: ");
                BigDecimal amount = ui.promptBigDecimal("Сумма перевода: ");
                bank.transfer(currentUser, fromId, targetLogin, toId, amount);
                ui.print("Перевод выполнен.");
            }
        }
    }

    private void printBalances() {
        ui.print("\n--- Ваши счета ---");
        var accounts = currentUser.getAccounts();
        if (accounts.isEmpty()) {
            ui.print("Счетов нет.");
        } else {
            for (Account acc : accounts) {
                String info = String.format("Счет #%-8s | Баланс: %,.2f", acc.getAccountID(), acc.getBalance());
                ui.print(info);
            }
        }
        ui.print("------------------\n");
    }

    private void handleException(Exception e) {
        if (e instanceof AccountNotFoundException ||
                e instanceof InsufficientFundsException ||
                e instanceof IllegalArgumentException) {
            ui.print("Ошибка операции: " + e.getMessage());
        } else if (e instanceof TransactionException) {
            ui.print("Транзакция отменена: " + e.getMessage());
        } else {
            ui.print("Системная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
import exceptions.*;
import javax.swing.*;
import java.math.BigDecimal;

public class SwingBankController {
    private final Bank bank;
    private final MainFrame view;
    private User currentUser;

    public SwingBankController(Bank bank, MainFrame view) {
        this.bank = bank;
        this.view = view;
        initController();
    }

    private void initController() {
        view.getLoginPanel().addLoginListener(e -> handleLogin());

        DashboardPanel dashboard = view.getDashboardPanel();

        dashboard.addLogoutListener(e -> handleLogout());
        dashboard.addBalanceListener(e -> refreshBalance());
        dashboard.addTransactionListener(e -> handleTransaction());
        dashboard.addDepositListener(e -> handleDeposit());
        dashboard.addWithdrawListener(e -> handleWithdraw());
        dashboard.addTransferListener(e -> handleTransfer());
    }

    private void handleLogin() {
        String login = view.getLoginPanel().getLogin();
        String password = view.getLoginPanel().getPassword();

        if (login.isEmpty() || password.isEmpty()) {
            view.showError("Введите логин и пароль.");
            return;
        }

        try {
            currentUser = bank.authenticate(login, password);
            view.showDashboard(currentUser.getFullName());
            refreshBalance();
        } catch (AuthException e) {
            view.showError("Ошибка входа: " + e.getMessage());
        } catch (Exception e) {
            view.showError("Системная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(view,
                "Вы уверены, что хотите выйти?",
                "Выход",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            currentUser = null;
            view.showLogin();
        }
    }

    private void refreshBalance() {
        if (currentUser == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("--- Ваши счета ---\n");

        var accounts = currentUser.getAccounts();
        if (accounts.isEmpty()) {
            sb.append("Счетов нет.\n");
        } else {
            for (Account acc : accounts) {
                sb.append(String.format("ID: %-8s | Баланс: %,.2f\n",
                        acc.getAccountID(), acc.getBalance()));
            }
        }
        sb.append("------------------");

        view.getDashboardPanel().updateInfoArea(sb.toString());
    }

    private void handleDeposit() {
        String accountId = askForAccountId("пополнения");
        if (accountId == null) return;

        BigDecimal amount = askForAmount("Введите сумму пополнения:");
        if (amount == null) return;

        try {
            bank.deposit(currentUser, accountId, amount);
            view.showInfo("Счет " + accountId + " успешно пополнен на " + amount);
            refreshBalance();
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            view.showError("Ошибка операции: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    private void handleWithdraw() {
        String accountId = askForAccountId("снятия");
        if (accountId == null) return;

        BigDecimal amount = askForAmount("Введите сумму снятия:");
        if (amount == null) return;

        try {
            bank.withdraw(currentUser, accountId, amount);
            view.showInfo("Снятие " + amount + " со счета " + accountId + " выполнено.");
            refreshBalance();
        } catch (InsufficientFundsException e) {
            view.showError("Недостаточно средств: " + e.getMessage());
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            view.showError("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    public void handleTransaction(){
        String accountId = askForAccountId("jgkfnf eckeu");
        if (accountId == null) return;

        try {
            bank.transaction(currentUser, accountId);
            view.showInfo("оплата готова");
            refreshBalance();
        } catch (InsufficientFundsException e) {
            view.showError("Недостаточно средств: " + e.getMessage());
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            view.showError("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    private void handleTransfer() {
        String fromId = askForAccountId("списания (Ваш счет)");
        if (fromId == null) return;

        String targetLogin = view.promptInput("Введите логин получателя:");
        if (targetLogin == null || targetLogin.trim().isEmpty()) return;

        String toId = view.promptInput("Введите ID счета получателя:");
        if (toId == null || toId.trim().isEmpty()) return;

        BigDecimal amount = askForAmount("Введите сумму перевода:");
        if (amount == null) return;

        int confirm = JOptionPane.showConfirmDialog(view,
                String.format("Перевести %s пользователю %s?\nС счета: %s\nНа счет: %s",
                        amount, targetLogin, fromId, toId),
                "Подтверждение перевода",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            bank.transfer(currentUser, fromId, targetLogin, toId, amount);
            view.showInfo("Перевод выполнен успешно!");
            refreshBalance();
        } catch (AccountNotFoundException e) {
            view.showError("Ошибка реквизитов: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            view.showError("Недостаточно средств: " + e.getMessage());
        } catch (TransactionException e) {
            view.showError("Ошибка транзакции: " + e.getMessage());
        } catch (Exception e) {
            handleSystemError(e);
        }
    }

    private String askForAccountId(String operationName) {
        String input = view.promptInput("Введите ID счета для " + operationName + ":");
        if (input == null) return null;

        input = input.trim();
        if (input.isEmpty()) {
            view.showError("ID счета не может быть пустым");
            return null;
        }
        return input;
    }

    private BigDecimal askForAmount(String message) {
        while (true) {
            String input = view.promptInput(message);
            if (input == null) return null;

            input = input.trim().replace(",", ".");

            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    view.showError("Сумма должна быть положительной!");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                view.showError("Некорректный формат числа. Пример: 100.50");
            }
        }
    }

    private void handleSystemError(Exception e) {
        view.showError("Критическая ошибка: " + e.getMessage());
        e.printStackTrace();
    }
}